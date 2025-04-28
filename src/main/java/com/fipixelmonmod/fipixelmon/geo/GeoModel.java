package com.fipixelmonmod.fipixelmon.geo;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation("fipixelmon", "geo/test.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        return new ResourceLocation("fipixelmon", "textures/test.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation("fipixelmon", "animations/test.animation.json");
    }
}
