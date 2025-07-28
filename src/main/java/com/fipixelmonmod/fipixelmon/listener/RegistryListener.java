package com.fipixelmonmod.fipixelmon.listener;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.ItemHelper;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(
        modid = FIPixelmon.MODID
)
public class RegistryListener {
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        ItemHelper.registerTeraShardItems(event);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        ItemHelper.registerTeraShardRenderers();
    }
}
