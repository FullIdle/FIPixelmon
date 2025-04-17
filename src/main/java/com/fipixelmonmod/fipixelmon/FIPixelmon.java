package com.fipixelmonmod.fipixelmon;

import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.fipixelmonmod.fipixelmon.mixin.pixelmon.MixinEntityPixelmon;
import com.fipixelmonmod.fipixelmon.test.RenderTest;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLogInformation;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Map;
import java.util.zip.ZipFile;

@Mod(
        modid = FIPixelmon.MODID,
        name = FIPixelmon.MODNAME,
        version = FIPixelmon.VERSION,
        dependencies = "required-after:pixelmon@[1.12.2-8.4.3,)"
)
@Mod.EventBusSubscriber(modid = "fipixelmon")
public class FIPixelmon {
    public static final String MODID = "fipixelmon";
    public static final String MODNAME = "FIPixelmon Mod";
    public static final String VERSION = "1.0";
    public static final Logger logger = LogManager.getLogger("FIPixelmon");
    public static Gson GSON;
    public static File pokemonFolder;
    public static File pokeballFolder;
    public static File megastoneFolder;
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
            if (file.getName().endsWith(".lang")) {
                BufferedInputStream bins = new BufferedInputStream(Files.newInputStream(file.toPath()));
                inject.invoke(fallback, bins);
                bins.close();
            }
        }
        //load zip lang config
        for (File file : FileHelper.loadedZipFile) {
            ZipFile zipFile = new ZipFile(file);
            for (String lang : FileHelper.getZipFileList(zipFile, "lang")) {
                if (lang.endsWith(".lang")) {
                    BufferedInputStream bins = new BufferedInputStream(zipFile.getInputStream(zipFile.getEntry(lang)));
                    inject.invoke(fallback, bins);
                    bins.close();
                }
            }
        }

        for (Map.Entry<ResourceLocation, GeoModel> entry : GeckoLibCache.getInstance().getGeoModels().entrySet()) {
            System.out.println("====");
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        //初始化
        logger.info("Complete initialization!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void bindEntityRenderer(ModelRegistryEvent event) {
        // 自然也可以用 method reference。这里写成 lambda 只是为了更加明显。

        if (!GeckoLib.hasInitialized) {
            GeckoLib.initialize();
        }

        FIPixelmon.logger.info("TESTETESTSETSETSETESTESTESTSE");
/*
        RenderingRegistry.registerEntityRenderingHandler(EntityPixelmon.class, RenderTest::new);
*/
    }
}
