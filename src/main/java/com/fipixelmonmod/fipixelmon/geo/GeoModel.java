package com.fipixelmonmod.fipixelmon.geo;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;
import java.util.function.Function;

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


    /**
     * TODO 似乎pixelmon本社那就没有头部，可能需要手动实现头部的存在...
     * 灵活头部的实现
     */
    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        extendLivingAnimations((EntityPixelmon) entity);
    }

    private void extendLivingAnimations(EntityPixelmon entity) {
        nonNullExec(this.getAnimationProcessor().getBone("head"), bone -> {
            setHeadRotation(entity, bone);
            return bone;
        });
    }

    private void setHeadRotation(EntityPixelmon entity, IBone head) {
        head.setRotationX(formatPitch(entity.rotationPitch));
        head.setRotationY(formatYaw(entity.renderYawOffset - entity.rotationYawHead));
    }

    private float formatPitch(float pitch) {
        return (float) Math.toRadians(MathHelper.clamp(-pitch, -90, 90));
    }

    private float formatYaw(float yaw) {
        return (float) Math.toRadians(yaw);
    }

    /**
     * 非空调用
     *
     * @param v        被处理对象
     * @param function 非空时调用的函数
     * @return 函数处理后的返回值
     */
    private <T, E> E nonNullExec(T v, Function<T, E> function) {
        if (v == null) return null;
        return function.apply(v);
    }
}
