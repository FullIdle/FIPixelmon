package com.fipixelmonmod.fipixelmon.geo;

import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeoRender extends GeoEntityRenderer{
    public GeoRender(RenderManager renderManager) {
        super(renderManager, new GeoModel());
    }
}
