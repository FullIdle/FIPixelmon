package com.fipixelmonmod.fipixelmon.test;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelTest extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object o) {
        return new ResourceLocation("fipixelmon", "geo/test.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return new ResourceLocation("fipixelmon", "textures/test.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return new ResourceLocation("fipixelmon", "animations/test.animation.json");
    }
}
