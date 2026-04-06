package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.adapter.EnumFormAdapter;
import com.fipixelmonmod.fipixelmon.adapter.EnumSpeciesAdapter;
import com.fipixelmonmod.fipixelmon.commands.Commands;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Pixelmon.class, remap = false)
public class MixinPixelmon {
    @Inject(method = "<clinit>", at = @At("HEAD"), remap = false)
    private static void cli(CallbackInfo ci) {
        //我也忘记玩什么写再这里了，我不敢改动了，怕加载顺序问题啥的
        FIPixelmon.GSON = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(com.pixelmonmod.pixelmon.enums.EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
                .registerTypeAdapter(EnumForm.class, EnumFormAdapter.INSTANCE)
                .create();
    }

    @Inject(method = "preInit", at = @At("HEAD"), remap = false)
    private void perInit(FMLPreInitializationEvent event, CallbackInfo ci) {
    }

    @Inject(method = "onServerStart", at = @At("HEAD"), remap = false)
    private void onServerStart(FMLServerStartingEvent event, CallbackInfo ci) {
        Commands.register(event);
    }
}
