package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.EnumForm;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.PokemonConfig;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonSmdFactory;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.util.ResourceLocation;
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

    @Shadow
    private static void addModel(EnumSpecies species, IEnumForm form, PixelmonSmdFactory factory) {
    }

    @Shadow
    private static void addFlyingModel(EnumSpecies species, IEnumForm form, PixelmonSmdFactory factory) {
    }

    @Inject(method = "init",remap = false,
            at = @At("TAIL")
    )
    private static void init(CallbackInfo ci){
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : Cache.extraPokemonConfig.entrySet()) {
            PokemonConfig value = entry.getValue();
            EnumPokemonModel model = value.getModel();
            EnumPokemonModel fModel = value.getFlyingModel();
            EnumSpecies es = entry.getKey();
            if (model != null){
                addModel(es,new PixelmonSmdFactory(model));
            }
            if (fModel != null){
                addFlyingModel(es,new PixelmonSmdFactory(fModel));
            }
            for (EnumForm.FormData form : value.getForms()) {
                String formModel = form.getModel();
                String flyingModel = form.getFlyingModel();
                if (formModel != null){
                    addModel(es,form.getEnumForm(),new PixelmonSmdFactory(new ResourceLocation(formModel)));
                }
                if (flyingModel != null){
                    addFlyingModel(es,form.getEnumForm(),new PixelmonSmdFactory(new ResourceLocation(flyingModel)));
                }
            }
        }
    }
}
