package com.fipixelmonmod.fipixelmon.geo;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import software.bernie.geckolib3.GeckoLib;

@Mod.EventBusSubscriber(modid = FIPixelmon.MODID)
public class RenderListener {
    /*
     * 注册模型渲染
     * */
    @SubscribeEvent
    public static void bindEntityRenderer(ModelRegistryEvent event) {
        if (!GeckoLib.hasInitialized) GeckoLib.initialize();
        RenderingRegistry.registerEntityRenderingHandler(EntityPixelmon.class, GeoRender::new);
    }
}
