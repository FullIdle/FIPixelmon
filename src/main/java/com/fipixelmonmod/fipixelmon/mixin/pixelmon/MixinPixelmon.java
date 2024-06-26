package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.commands.BattleFixesCommand;
import com.fipixelmonmod.fipixelmon.common.adapter.EnumFormAdapter;
import com.fipixelmonmod.fipixelmon.common.adapter.EnumSpeciesAdapter;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.EnumForm;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.commands.BattleLogCommand;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Pixelmon.class, remap = false)
public class MixinPixelmon {
    @Inject(method = "<clinit>", at = @At("HEAD"), remap = false)
    private static void cli(CallbackInfo ci) {
        FIPixelmon.GSON = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(com.pixelmonmod.pixelmon.enums.EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
                .registerTypeAdapter(EnumForm.class, EnumFormAdapter.INSTANCE)
                .create();
    }

    @Inject(method = "onServerStart"
            , at = @At("HEAD"), remap = false
    )
    private void onServerStart(FMLServerStartingEvent event, CallbackInfo ci) {
        event.registerServerCommand(new BattleFixesCommand());
    }

    @Inject(method = "preInit", at = @At("HEAD"), remap = false)
    private void perInit(FMLPreInitializationEvent event, CallbackInfo ci) {
    }
}
