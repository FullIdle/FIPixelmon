package com.fipixelmonmod.fipixelmon.test;

import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderTest extends GeoEntityRenderer{
    public RenderTest(RenderManager renderManager) {
        super(renderManager, new ModelTest());
    }
}
