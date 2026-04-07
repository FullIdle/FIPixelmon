package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import lombok.Getter;
import lombok.val;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EffectTypeConfig {
    //clinit
    public static final Map<String, ExtendClassConfig> extraEffectTypes;

    static {
        val packageName = "com.pixelmonmod.pixelmon.battles.attacks.fipixelmon";
        extraEffectTypes = new HashMap<>();
        val files = FIPixelmon.effectsFolder.listFiles();
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val config = new ExtendClassConfig(file, packageName, EffectBase.class);
            extraEffectTypes.put(config.getName(), config);
        }
    }
}
