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

    public CorePlugin() {
        //文件
        (FIPixelmon.fiPixelmonFolder = new File("FIPixelmonData")).mkdirs();
        //pokemon
        (FIPixelmon.pokemonFolder = new File(FIPixelmon.fiPixelmonFolder, "pokemon")).mkdirs();
        //pokeball
        (FIPixelmon.pokeballFolder = new File(FIPixelmon.fiPixelmonFolder, "pokeball")).mkdirs();
        //Stats
        (FIPixelmon.statsFolder = new File(FIPixelmon.fiPixelmonFolder, "stats")).mkdirs();
        //lang
        (FIPixelmon.langFolder = new File(FIPixelmon.fiPixelmonFolder, "lang")).mkdirs();
        //models
        (FIPixelmon.modelsFolder = new File(FIPixelmon.fiPixelmonFolder, "models")).mkdirs();
        //textures
        (FIPixelmon.texturesFolder = new File(FIPixelmon.fiPixelmonFolder, "textures")).mkdirs();
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
