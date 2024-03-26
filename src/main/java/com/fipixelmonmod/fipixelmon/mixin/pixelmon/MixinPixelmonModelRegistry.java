package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonSmdFactory;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = PixelmonModelRegistry.class,remap = false)
public class MixinPixelmonModelRegistry {
    @Shadow
    private static void addModel(EnumSpecies species, PixelmonSmdFactory factory) {
    }

    @Inject(method = "init",remap = false,
            at = @At("TAIL")
    )
    private static void init(CallbackInfo ci){
        for (Map.Entry<EnumSpecies, EnumPokemonModel> entry : Cache.extraPokemonModels.entrySet()) {
            addModel(entry.getKey(),new PixelmonSmdFactory(entry.getValue()));
        }
    }

    @Inject(method = "addModel(Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;Lcom/pixelmonmod/pixelmon/client/models/PixelmonSmdFactory;)V",
            remap = false,
            at = @At("HEAD")
    )
    private static void am(EnumSpecies species, PixelmonSmdFactory factory, CallbackInfo ci){
        if (species.name.equalsIgnoreCase("npc4")) {
            System.out.println("TRUE");
        }
    }
}
