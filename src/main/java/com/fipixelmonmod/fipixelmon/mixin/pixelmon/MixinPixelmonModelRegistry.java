package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.PokemonConfig;
import com.pixelmonmod.pixelmon.client.models.DualModelFactory;
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
public abstract class MixinPixelmonModelRegistry {
    @Shadow
    private static void addModel(EnumSpecies species, PixelmonSmdFactory factory) {
    }

    @Shadow
    private static void addFlyingModel(EnumSpecies species, PixelmonSmdFactory factory) {
    }

    @Inject(method = "init",remap = false,
            at = @At("TAIL")
    )
    private static void init(CallbackInfo ci){
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : Cache.extraPokemonConfig.entrySet()) {
            EnumPokemonModel model = entry.getValue().getModel();
            EnumPokemonModel fModel = entry.getValue().getFlyingModel();
            if (model != null){
                addModel(entry.getKey(),new PixelmonSmdFactory(model));
            }
            if (fModel != null){
                addFlyingModel(entry.getKey(),new PixelmonSmdFactory(fModel));
            }
        }
    }
}
