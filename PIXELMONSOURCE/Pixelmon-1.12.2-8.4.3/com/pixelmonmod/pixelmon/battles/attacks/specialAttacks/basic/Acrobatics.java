package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;

public class Acrobatics extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasHeldItem() || user.getUsableHeldItem() == PixelmonItemsHeld.flyingGem) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      return AttackResult.proceed;
   }
}
