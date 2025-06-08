package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.geo.GeoAnimationController;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Mixin(
        value = EntityPixelmon.class
)
/*
让EntityPixelmon实现GEO提供的IAnimatable接口
 */
public abstract class MixinEntityPixelmon extends EntityLiving implements IAnimatable {
    /*animations*/
    @Unique
    private final AnimationFactory fIPixelmon$factory = new AnimationFactory(this);

    public MixinEntityPixelmon(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new GeoAnimationController(this, "controller", 0));
    }

    @Override
    public AnimationFactory getFactory() {
        return fIPixelmon$factory;
    }
}
