package com.fipixelmonmod.fipixelmon.geo;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import lombok.val;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeoRender extends GeoEntityRenderer {
    private final RenderPixelmon oldRender;

    public GeoRender(RenderManager renderManager) {
        super(renderManager, new GeoModel());
        oldRender = new RenderPixelmon(renderManager);
    }

    @Override
    public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
        val pixelmon = (EntityPixelmon) entity;
        val config = PokemonConfig.extraPokemonConfig.get(pixelmon.getSpecies());

        if (config == null || !hasGeoModel(pixelmon, config)) {
            oldRender.doRender(pixelmon, x, y, z, entityYaw, partialTicks);
            return;
        }

        super.doRender(pixelmon, x, y, z, entityYaw, partialTicks);


        //TODO geo不会渲染的东西调用原版 (可能不全)
        oldRender.renderName(pixelmon, x, y, z);
    }


    public static boolean hasGeoModel(EntityPixelmon pixelmon, PokemonConfig config) {
        if (pixelmon.getFormEnum() == EnumNoForm.NoForm) return config.getGeoModel() != null;
        if (pixelmon.getFormEnum() instanceof EnumForm)
            return ((EnumForm) pixelmon.getFormEnum()).getData().getGeoModel() != null;
        return false;
    }
}
