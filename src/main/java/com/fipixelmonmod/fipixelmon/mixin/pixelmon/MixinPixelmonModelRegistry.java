package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelHolder;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonSmdFactory;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Map;

@Mixin(value = PixelmonModelRegistry.class, remap = false)
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

    @Shadow
    @Final
    private static EnumMap<EnumSpecies, PixelmonModelHolder<?>> modelRegistry;

    @Shadow
    @Final
    private static Map<EnumSpecies, Map<IEnumForm, PixelmonModelHolder<?>>> flyingModelRegistry;

    @Inject(method = "init", remap = false,
            at = @At("TAIL")
    )
    private static void init(CallbackInfo ci) {
        EnumSpecies es;
        PokemonConfig pokemonConfig;
        String formFlyPath;
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            es = entry.getKey();
            pokemonConfig = entry.getValue();
            if (es.getDefaultForms().contains(EnumNoForm.NoForm)) {
                if (pokemonConfig.getModel() != null)
                    addModel(es, EnumNoForm.NoForm, new PixelmonSmdFactory(
                            new ResourceLocation("pixelmon", fIPixelmon$formatPath(pokemonConfig.getModel()))
                    ));
                if (pokemonConfig.getFlyingModel() != null)
                    addFlyingModel(es, EnumNoForm.NoForm, new PixelmonSmdFactory(
                            new ResourceLocation("pixelmon", fIPixelmon$formatPath(pokemonConfig.getFlyingModel()))
                    ));
            }

            for (IEnumForm form : pokemonConfig.getEnumForm()) {
                addModel(es, form, new PixelmonSmdFactory(
                        new ResourceLocation("pixelmon", fIPixelmon$formatPath(((EnumForm) form).getData().getModel()))
                ));
                if ((formFlyPath = ((EnumForm) form).getData().getFlyingModel()) != null) {
                    addFlyingModel(es, form, new PixelmonSmdFactory(new ResourceLocation("pixelmon",
                            fIPixelmon$formatPath(formFlyPath))));
                }
            }
        }
    }

    @Unique
    private static String fIPixelmon$formatPath(String path) {
        return "models/" + (path.startsWith("/") ? path.substring(1) : path);
    }
}
