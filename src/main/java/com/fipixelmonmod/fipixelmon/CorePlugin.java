package com.fipixelmonmod.fipixelmon;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public static final boolean isClient = FMLLaunchHandler.side() == Side.CLIENT;

    public CorePlugin(){
        //文件
        FIPixelmon.fiPixelmonFolder = new File("FIPixelmonData");
        if (!FIPixelmon.fiPixelmonFolder.exists()){
            FIPixelmon.fiPixelmonFolder.mkdirs();
        }
        //pokemon
        FIPixelmon.pokemonFolder = new File(FIPixelmon.fiPixelmonFolder, "pokemon");
        if (!FIPixelmon.pokemonFolder.exists()) {
            FIPixelmon.pokemonFolder.mkdirs();
        }
        //Stats
        FIPixelmon.statsFolder = new File(FIPixelmon.fiPixelmonFolder, "stats");
        if (!FIPixelmon.statsFolder.exists()) {
            FIPixelmon.statsFolder.mkdirs();
        }
        //lang
        FIPixelmon.langFolder = new File(FIPixelmon.fiPixelmonFolder, "lang");
        if (!FIPixelmon.langFolder.exists()) {
            FIPixelmon.langFolder.mkdirs();
        }
        //models
        FIPixelmon.modelsFolder = new File(FIPixelmon.fiPixelmonFolder, "models");
        if (!FIPixelmon.modelsFolder.exists()) {
            FIPixelmon.modelsFolder.mkdirs();
        }
        //textures
        FIPixelmon.texturesFolder = new File(FIPixelmon.fiPixelmonFolder, "textures");
        if (!FIPixelmon.texturesFolder.exists()) {
            FIPixelmon.texturesFolder.mkdirs();
        }
    }

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.fipixelmon_early.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
