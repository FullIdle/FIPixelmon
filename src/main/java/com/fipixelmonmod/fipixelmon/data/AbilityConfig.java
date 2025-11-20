package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import lombok.val;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @see AbilityBase
 */
public class AbilityConfig extends NeedExtendClassScriptConfig<AbilityBase>{
    /**
     * 特性的名字
     * 通常是文件名，如果文件名有空格理论会被替换成空字符
     */
    public final String name;

    private AbilityConfig(String name,File file) {
        super(file);
        this.name = name;
    }


    //clinit
    public static final Map<String, AbilityConfig> extraAbilities;
    public static final Map<Class<? extends AbilityBase>, AbilityConfig> clazzAbilities;

    static {
        extraAbilities = new HashMap<>();
        clazzAbilities = new HashMap<>();

        val files = FIPixelmon.abilitiesFolder.listFiles();
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val name = RegexPatterns.SPACE_SYMBOL.matcher(fileName.substring(0, fileName.length() - 3)).replaceAll("");
            val config = new AbilityConfig(name, file);
            extraAbilities.put(name, config);
            clazzAbilities.put(config.getRepresentedClass(), config);
        }
    }
}
