package com.fipixelmonmod.fipixelmon;

import com.fipixelmonmod.fipixelmon.common.FIPResourcePack;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public static final boolean isClient = FMLLaunchHandler.side() == Side.CLIENT;

    public CorePlugin(){
        //文件
        File fiPixelmonFolder = FIPixelmon.fiPixelmonFolder = new File("FIPixelmonData");
        if (!fiPixelmonFolder.exists()){
            fiPixelmonFolder.mkdirs();
        }
        //Stats
        File statsFolder = FIPixelmon.statsFolder = new File(fiPixelmonFolder, "stats");
        if (!statsFolder.exists()) {
            statsFolder.mkdirs();
        }
        //lang
        File langFolder = FIPixelmon.langFolder = new File(fiPixelmonFolder, "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }
        //models
        File modelsFolder = FIPixelmon.modelsFolder = new File(fiPixelmonFolder, "models");
        if (!modelsFolder.exists()) {
            modelsFolder.mkdirs();
        }
        //textures
        File texturesFolder = FIPixelmon.texturesFolder = new File(fiPixelmonFolder, "textures");
        if (!texturesFolder.exists()) {
            texturesFolder.mkdirs();
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
