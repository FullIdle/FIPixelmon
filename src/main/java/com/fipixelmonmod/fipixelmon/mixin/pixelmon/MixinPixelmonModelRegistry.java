package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelHolder;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonSmdFactory;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.file.AnimationFileLoader;
import software.bernie.geckolib3.file.GeoModelLoader;
import software.bernie.geckolib3.resource.GeckoLibCache;

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
        String formPath;
        String formFlyPath;

        //TODO geo
        if (!GeckoLib.hasInitialized) GeckoLib.initialize();
        val geckoLibCache = GeckoLibCache.getInstance();
        val animations = geckoLibCache.getAnimations();
        val geoModels = geckoLibCache.getGeoModels();
        AnimationFileLoader animationLoader = ReflectionHelper.getPrivateValue(GeckoLibCache.class, geckoLibCache, "animationLoader");
        GeoModelLoader modelLoader = ReflectionHelper.getPrivateValue(GeckoLibCache.class, geckoLibCache, "modelLoader");
        val resourceManager = Minecraft.getMinecraft().getResourceManager();
        ResourceLocation location;

        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            es = entry.getKey();
            pokemonConfig = entry.getValue();

            //TODO geo
            //TODO pixelmon
            if (pokemonConfig.getGeoModel() != null) {
                animations.put(
                        location = new ResourceLocation("pixelmon", pokemonConfig.getGeoAnimation()),
                        animationLoader.loadAllAnimations(geckoLibCache.parser, location, resourceManager)
                );

                geoModels.put(
                        location = new ResourceLocation("pixelmon", pokemonConfig.getGeoModel()),
                        modelLoader.loadModel(resourceManager, location)
                );
            } else if (es.getDefaultForms().contains(EnumNoForm.NoForm)) {
                if (pokemonConfig.getModel() != null)
                    addModel(es, EnumNoForm.NoForm, new PixelmonSmdFactory(
                            new ResourceLocation("pixelmon", fIPixelmon$formatPath(pokemonConfig.getModel()))
                    ));
                if (pokemonConfig.getFlyingModel() != null)
                    addFlyingModel(es, EnumNoForm.NoForm, new PixelmonSmdFactory(
                            new ResourceLocation("pixelmon", fIPixelmon$formatPath(pokemonConfig.getFlyingModel()))
                    ));
            }


            /*
            形态
            * */
            for (IEnumForm iForm : pokemonConfig.getEnumForm()) {
                if (!(iForm instanceof EnumForm)) continue;
                val form = (EnumForm) iForm;
                if (form.getData().getGeoModel() != null) {
                    animations.put(
                            location = new ResourceLocation("pixelmon", form.getData().getGeoAnimation()),
                            animationLoader.loadAllAnimations(geckoLibCache.parser, location, resourceManager)
                    );

                    geoModels.put(
                            location = new ResourceLocation("pixelmon", form.getData().getGeoModel()),
                            modelLoader.loadModel(resourceManager, location)
                    );
                } else if ((formPath = form.getData().getModel()) != null) {
                    addModel(es, form, new PixelmonSmdFactory(
                            new ResourceLocation("pixelmon", fIPixelmon$formatPath(formPath))
                    ));
                }
                if ((formFlyPath = form.getData().getFlyingModel()) != null) {
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
