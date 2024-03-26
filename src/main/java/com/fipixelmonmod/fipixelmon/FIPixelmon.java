package com.fipixelmonmod.fipixelmon;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;

@Mod(
        modid = FIPixelmon.MODID,
        name = FIPixelmon.MODNAME,
        version = FIPixelmon.VERSION,
        dependencies = "required-after:pixelmon@[1.12.2-8.4.3,)"
)
public class FIPixelmon {
    public static final String MODID = "fipixelmon";
    public static final String MODNAME = "FIPixelmon Mod";
    public static final String VERSION = "1.0";
    public static final Logger logger = LogManager.getLogger("FIPixelmon");
    public static final Gson GSON = new Gson();
    public static File fiPixelmonFolder;
    public static File statsFolder;
    public static File langFolder;
    public static File modelsFolder;
    public static File texturesFolder;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    }

    @SneakyThrows
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        //load lang config
        LanguageMap fallback = ReflectionHelper.getPrivateValue(I18n.class, null, "field_150828_b");
        Method inject = ReflectionHelper.findMethod(LanguageMap.class, "inject", "inject", InputStream.class);
        File[] files = langFolder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            inject.invoke(fallback,new BufferedInputStream(Files.newInputStream(file.toPath())));
        }

        //初始化
        logger.info("Complete initialization!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
    }
}
