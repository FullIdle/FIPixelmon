package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.helper.ScriptEngineHelper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import lombok.Setter;
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
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.Callable;

@Getter
@Setter
public class ExtendClassConfig {
    private final String name;
    private final File file;
    private ScriptContext scriptContext;
    private final Class<?> representedClass;

    public ExtendClassConfig(File file, String packageName, Class<?>... needExtend) {
        this.file = file;
        load();//scriptContext
        val global = ((ScriptObjectMirror) scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).get("nashorn.global"));
        val NAME = Objects.requireNonNull(global.get("NAME").toString());
        val EXTEND = Objects.requireNonNull(global.get("EXTEND").toString());
        this.name = NAME;
        if (NAME.contains(" ")) throw new IllegalArgumentException("Ability name cannot contain space");
        try {
            val extend = Class.forName(EXTEND);

            for (Class<?> clazz : needExtend)
                if (!clazz.isAssignableFrom(extend))
                    throw new IllegalArgumentException("Ability extend class " + clazz + " is not assignable from " + extend);

            val newClassName = packageName + "." + NAME;

            try {
                Class.forName(newClassName);
                throw new RuntimeException("Ability class already exists " + newClassName);
            } catch (ClassNotFoundException ignored) {
            }

            try (val make = new ByteBuddy()
                    .subclass(extend)
                    .name(newClassName)
                    .defineField("fIPixelmon$config", ExtendClassConfig.class, Modifier.PUBLIC | Modifier.STATIC)
                    .constructor(ElementMatchers.isDefaultConstructor())
                    .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(ConstructorInterceptor.INSTANCE)))
                    .method(ElementMatchers.not(ElementMatchers.isConstructor())
                            .and(ElementMatchers.not(ElementMatchers.isTypeInitializer()))
                            .and(ElementMatchers.not(ElementMatchers.isStatic())))
                    .intercept(MethodDelegation.to(MethodInterceptor.INSTANCE))
                    .make()) {
                this.representedClass = make.load(this.getClass().getClassLoader()).getLoaded();
                val field = this.representedClass.getDeclaredField("fIPixelmon$config");
                field.set(null, this);
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

    public static class ConstructorInterceptor {
        public static final ConstructorInterceptor INSTANCE = new ConstructorInterceptor();

        @RuntimeType
        public void intercept(
                @This Object proxy,
                @FieldValue("fIPixelmon$config") ExtendClassConfig config
        ) throws Exception {
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
                @SuperCall Callable<?> superCall,
                @FieldValue("fIPixelmon$config") ExtendClassConfig config
        ) throws Exception {
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
}
