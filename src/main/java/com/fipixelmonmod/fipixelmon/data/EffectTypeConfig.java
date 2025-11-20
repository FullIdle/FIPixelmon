package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import lombok.Getter;
import lombok.val;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EffectTypeConfig extends NeedExtendClassScriptConfig<EffectBase>{
    // 文件名去除 .js 后缀
    private final String name;

    private EffectTypeConfig(File file) {
        super(file);
        this.name = file.getName().substring(0, file.getName().length() - 3);
    }

    //clinit
    public static final Map<String, EffectTypeConfig> extraEffectTypes;

    static {
        extraEffectTypes = new HashMap<>();
        val files = FIPixelmon.effectsFolder.listFiles();
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val config = new EffectTypeConfig(file);
            extraEffectTypes.put(config.getName(), config);
        }
    }
}
