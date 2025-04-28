package com.fipixelmonmod.fipixelmon.geo;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation("pixelmon",
                PokemonConfig.extraPokemonConfig.get(((EntityPixelmon) entity).getSpecies()).getGeoModel()
        );
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        return new ResourceLocation("pixelmon",
                PokemonConfig.extraPokemonConfig.get(((EntityPixelmon) entity).getSpecies()).getGeoTexture()
        );
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation("pixelmon",
                PokemonConfig.extraPokemonConfig.get(((EntityPixelmon) entity).getSpecies()).getGeoAnimation()
        );
    }
}
