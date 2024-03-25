package com.fipixelmonmod.fipixelmon;

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
        URL url = FIPixelmon.class.getClassLoader().getResource(FIPixelmon.class.getName().replace(".", "/") + ".class");
        String path = url.getPath();
        path = path.substring(5,path.indexOf("!"));
        FIPixelmon.modsFolder = new File(path).getParentFile();
        FIPixelmon.fiPixelmonFolder = new File(FIPixelmon.modsFolder,"fiPixelmonData");
        if (!FIPixelmon.fiPixelmonFolder.exists()){
            FIPixelmon.fiPixelmonFolder.mkdirs();
        }
        //Stats
        FIPixelmon.statsFolder = new File(FIPixelmon.fiPixelmonFolder, "stats");
        if (!FIPixelmon.statsFolder.exists()) {
            FIPixelmon.statsFolder.mkdirs();
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
