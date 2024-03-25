package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import java.util.function.Supplier;

public enum EnumEffectType {
   BALL(AttackBall.BallData::new, AttackBall::new),
   BEAM(AttackBeam.BeamData::new, AttackBeam::new),
   BUFF(AttackBuff.BuffData::new, AttackBuff::new),
   TUBE(AttackTube.TubeData::new, AttackTube::new),
   EXPLOSION(AttackExplosion.ExplosionData::new, AttackExplosion::new),
   LIGHTNING(AttackLightning.LightningData::new, AttackLightning::new),
   RADIAL(AttackRadial.RadialData::new, AttackRadial::new),
   RAIN(AttackRain.RainData::new, AttackRain::new),
   RISE(AttackRise.RiseData::new, AttackRise::new),
   STAT_CHANGE(AttackStatChange.StatChangeData::new, AttackStatChange::new);

   public final Supplier dataSupplier;
   public final Supplier systemSupplier;

   private EnumEffectType(Supplier dataSupplier, Supplier systemSupplier) {
      this.dataSupplier = dataSupplier;
      this.systemSupplier = systemSupplier;
   }
}
