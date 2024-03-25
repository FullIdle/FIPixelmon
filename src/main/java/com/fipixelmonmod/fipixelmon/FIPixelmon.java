package com.fipixelmonmod.fipixelmon;

import com.google.gson.Gson;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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
    public static File modsFolder;
    public static File fiPixelmonFolder;
    public static File statsFolder;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        //初始化
        logger.info("Complete initialization!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
    }
}
