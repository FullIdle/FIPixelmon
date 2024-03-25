package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class SpecialAttackBase extends EffectBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyAfterEffect(PixelmonWrapper user) {
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }

   public float modifyPriority(PixelmonWrapper pw) {
      return pw.priority;
   }
}
