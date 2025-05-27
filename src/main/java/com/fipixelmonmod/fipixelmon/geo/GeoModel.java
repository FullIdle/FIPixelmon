package com.fipixelmonmod.fipixelmon.geo;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return getModelLocation(((EntityPixelmon) entity));
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        return getTextureLocation(((EntityPixelmon) entity));
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return getAnimationFileLocation(((EntityPixelmon) entity));
    }

    public ResourceLocation getModelLocation(EntityPixelmon entity) {
        return new ResourceLocation(Pixelmon.MODID,
                getModelOrFormModel(entity, PokemonConfig.extraPokemonConfig.get(entity.getSpecies()))
        );
    }

    public ResourceLocation getTextureLocation(EntityPixelmon entity) {
        return entity.getTexture();
    }

    public ResourceLocation getAnimationFileLocation(EntityPixelmon entity) {
        return new ResourceLocation(Pixelmon.MODID,
                getAnimationOrFormAnimation(entity, PokemonConfig.extraPokemonConfig.get(entity.getSpecies()))
        );
    }

    public String getModelOrFormModel(EntityPixelmon entity, PokemonConfig config) {
        if (entity.getFormEnum() instanceof EnumForm)
            return ((EnumForm) entity.getFormEnum()).getData().getGeoModel();
        return config.getGeoModel();
    }

    public String getAnimationOrFormAnimation(EntityPixelmon entity, PokemonConfig config) {
        if (entity.getFormEnum() instanceof EnumForm)
            return ((EnumForm) entity.getFormEnum()).getData().getGeoAnimation();
        return config.getGeoAnimation();
    }
}
