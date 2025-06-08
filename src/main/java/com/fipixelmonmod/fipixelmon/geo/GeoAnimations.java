package com.fipixelmonmod.fipixelmon.geo;

import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;

/**
 * 动作名
 */
public final class GeoAnimations {
    public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    public static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP);
    /*这个实际应该看成飞行*/
    public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("jump", ILoopType.EDefaultLoopTypes.LOOP);
    public static final AnimationBuilder SWIM = new AnimationBuilder().addAnimation("swim", ILoopType.EDefaultLoopTypes.LOOP);
}
