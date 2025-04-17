package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Mixin(
        value = EntityPixelmon.class
)
public abstract class MixinEntityPixelmon extends EntityLivingBase implements IAnimatable {
    private final AnimationFactory fIPixelmon$factory = new AnimationFactory(this);

    public MixinEntityPixelmon(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return fIPixelmon$factory;
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("jump", true));
        return PlayState.CONTINUE;
    }
}
