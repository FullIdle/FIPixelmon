package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import lombok.val;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @see AbilityBase
 */
public class AbilityConfig {
    public static final Map<String, ExtendClassConfig> extraAbilities;

    public static final Map<Class<? extends AbilityBase>, ExtendClassConfig> clazzAbilities;

    static {
        extraAbilities = new HashMap<>();
        clazzAbilities = new HashMap<>();

        val logger = FIPixelmon.logger;

        val files = FIPixelmon.abilitiesFolder.listFiles();
        val packageName = "com.pixelmonmod.pixelmon.entities.pixelmon.abilities";
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val config = new ExtendClassConfig(file, packageName, AbilityBase.class);
            val name = config.getName();
            extraAbilities.put(name, config);
            clazzAbilities.put(((Class<? extends AbilityBase>) config.getRepresentedClass()), config);
            logger.info("Loaded ability {}", name);
        }
    }
}
