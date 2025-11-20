package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.ScriptEngineHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import jdk.internal.dynalink.beans.StaticClass;
import lombok.val;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @see AbilityBase
 */
public class AbilityConfig {
    /**
     * 特性的名字
     * 通常是文件名，如果文件名有空格理论会被替换成空字符
     */
    public final String name;
    /**
     * 继承 {@link AbilityBase} 的方法重写脚本
     */
    public final String script;
    /**
     * 获取，特性的类
     */
    public final Class<? extends AbilityBase> representedClass;

    public AbilityConfig(String name, String script) throws ScriptException {
        this.name = name;
        this.script = script;
        this.representedClass = (Class<? extends AbilityBase>) ((StaticClass)
                ScriptEngineHelper.usingEngine.eval(script)).getRepresentedClass();
    }


    //clinit
    public static final Map<String, AbilityConfig> extraAbilities = new HashMap<>();
    public static final Map<Class<? extends AbilityBase>, AbilityConfig> clazzAbilities = new HashMap<>();

    static {
        val files = FIPixelmon.abilitiesFolder.listFiles();
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val name = RegexPatterns.SPACE_SYMBOL.matcher(fileName.substring(0, fileName.length() - 3)).replaceAll("");
            try {
                val config = new AbilityConfig(name, new String(Files.readAllBytes(file.toPath())).intern());
                extraAbilities.put(name, config);
                clazzAbilities.put(config.representedClass, config);
            } catch (IOException | ScriptException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
