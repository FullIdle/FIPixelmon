package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.ScriptEngineHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.ArrayUtils;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @see AbilityBase
 */
public class AbilityConfig {
    public final String name;
    public final File file;
    public ScriptContext scriptContext;
    public final Class<? extends AbilityBase> representedClass;

    private AbilityConfig(File file) {
        this.file = file;
        load();//scriptContext
        val global = ((ScriptObjectMirror) scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).get("nashorn.global"));
        val NAME = Objects.requireNonNull(global.get("NAME").toString());
        val EXTEND = Objects.requireNonNull(global.get("EXTEND").toString());
        this.name = NAME;
        if (NAME.contains(" ")) throw new IllegalArgumentException("Ability name cannot contain space");
        try {
            val extend = ((Class<? extends AbilityBase>) Class.forName(EXTEND));
            val newClassName = "com.pixelmonmod.pixelmon.entities.pixelmon.abilities." + NAME;
            try {
                Class.forName(newClassName);
                throw new RuntimeException("Ability class already exists " + newClassName);
            } catch (ClassNotFoundException ignored) {
            }

            try (val make = new ByteBuddy()
                    .subclass(extend)
                    .name(newClassName)
                    .constructor(ElementMatchers.isDefaultConstructor())
                    .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(ConstructorInterceptor.INSTANCE)))
                    .method(ElementMatchers.not(ElementMatchers.isConstructor())
                            .and(ElementMatchers.not(ElementMatchers.isTypeInitializer()))
                            .and(ElementMatchers.not(ElementMatchers.isStatic())))
                    .intercept(MethodDelegation.to(MethodInterceptor.INSTANCE))
                    .make()) {
                this.representedClass = make.load(this.getClass().getClassLoader()).getLoaded();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 仅加载脚本上下文的函数，其他成员如已被使用将无效
     */
    public void load() {
        this.scriptContext = new SimpleScriptContext();
        try (val reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            ScriptEngineHelper.usingEngine.eval(reader, this.scriptContext);
        } catch (IOException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    //clinit
    public static final Map<String, AbilityConfig> extraAbilities;
    public static final Map<Class<? extends AbilityBase>, AbilityConfig> clazzAbilities;

    public static class ConstructorInterceptor {
        public static final ConstructorInterceptor INSTANCE = new ConstructorInterceptor();

        @RuntimeType
        public void intercept(
                @This Object proxy
        ) throws Exception {
            val config = clazzAbilities.get(proxy.getClass());
            if (config == null) return;
            try {
                config.invokeFunction("$init", proxy);
            } catch (NoSuchMethodException ignored) {
            }
        }
    }

    public static class MethodInterceptor {
        public static final MethodInterceptor INSTANCE = new MethodInterceptor();

        @RuntimeType
        public Object intercept(
                @This Object proxy,
                @Origin Method method,
                @AllArguments Object[] args,
                @SuperCall Callable<?> superCall
        ) throws Exception {
            val config = clazzAbilities.get(proxy.getClass());
            if (config != null) try {
                return config.invokeFunction(method.getName(), ArrayUtils.add(args, 0, proxy));
            } catch (NoSuchMethodException ignored) {
            }
            return superCall.call();
        }
    }

    private Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        val engine = ScriptEngineHelper.usingEngine;
        val invocable = (Invocable) engine;
        val old = engine.getContext();
        engine.setContext(this.scriptContext);
        try {
            return invocable.invokeFunction(name, args);
        } finally {
            engine.setContext(old);
        }
    }

    static {
        extraAbilities = new HashMap<>();
        clazzAbilities = new HashMap<>();

        val logger = FIPixelmon.logger;

        val files = FIPixelmon.abilitiesFolder.listFiles();
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val config = new AbilityConfig(file);
            extraAbilities.put(config.name, config);
            clazzAbilities.put(config.representedClass, config);
            logger.info("Loaded ability {}", config.name);
        }
    }
}
