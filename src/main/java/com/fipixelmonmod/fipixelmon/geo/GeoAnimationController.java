package com.fipixelmonmod.fipixelmon.geo;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

import static com.fipixelmonmod.fipixelmon.geo.GeoAnimations.*;

public class GeoAnimationController extends AnimationController {
    public GeoAnimationController(IAnimatable animatable, String name, float transitionLengthTicks) {
        super(animatable, name, transitionLengthTicks, event -> {
            //控制器
            event.getController().setAnimation(judgeAnimation((EntityPixelmon) event.getAnimatable()));
            //不会用，不管了就直接CONTINUE
            return PlayState.CONTINUE;
        });
    }

    private static AnimationBuilder judgeAnimation(EntityPixelmon ep) {
        //空中处理
        if (ep.world.getBlockState(
                new BlockPos(ep.posX, ep.posY - 0.5, ep.posZ)
        ).getBlock() == Blocks.AIR) {
            // 检查是否静止在空中
            return ep.prevPosX == ep.posX
                    && ep.prevPosY == ep.posY
                    && ep.prevPosZ == ep.posZ ?
                    IDLE : JUMP;
        }

        // 地面或水中行为判断
        return getGroundAnimation(ep.isInWater(), ep.isSprinting());
    }

    private static AnimationBuilder getGroundAnimation(boolean isInWater, boolean isMoving) {
        return isInWater ? SWIM : isMoving ? WALK : IDLE;
    }
}
