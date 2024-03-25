package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class AttackModifierBase extends EffectBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }
}
