package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Burn;

public class BurningJealousy extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.getBattleStats().isRaisedThisTurn()) {
         Burn.burn(user, target, user.attack, true);
      }

   }
}
