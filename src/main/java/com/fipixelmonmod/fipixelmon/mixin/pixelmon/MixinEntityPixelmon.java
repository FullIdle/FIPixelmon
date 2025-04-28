package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.val;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
/*
让EntityPixelmon实现GEO提供的IAnimatable接口
 */
public abstract class MixinEntityPixelmon extends EntityLivingBase implements IAnimatable {
    @Unique
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
        //控制器
        val e = (AnimationEvent<MixinEntityPixelmon>) event;
        e.getController().setAnimation(new AnimationBuilder().addAnimation(
                fIPixelmon$judgeAnimationName(e)
                , true));
        //不会用，不管了就直接CONTINUE
        return PlayState.CONTINUE;
    }

    @Unique
    private static String fIPixelmon$judgeAnimationName(AnimationEvent<MixinEntityPixelmon> e) {
        val animatable = e.getAnimatable();
        if (animatable.world.getBlockState(
                new BlockPos(animatable.posX, animatable.posY - 0.5, animatable.posZ)
        ).getBlock() == Blocks.AIR) {
            // 检查是否静止在空中
            return animatable.prevPosX == animatable.posX
                    && animatable.prevPosY == animatable.posY
                    && animatable.prevPosZ == animatable.posZ ?
                    "idle" : "jump";
        }
        // 地面或水中行为判断
        val isInWater = animatable.isInWater();

        if (e.isMoving()) {
            return isInWater ? "swim_stand" : "walk";
        }
        return isInWater ? "swim_stand" : "idle";
    }
}
