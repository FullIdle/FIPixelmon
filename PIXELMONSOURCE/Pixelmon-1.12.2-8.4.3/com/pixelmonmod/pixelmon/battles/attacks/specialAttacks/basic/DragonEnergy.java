package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class DragonEnergy extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      int power = (int)Math.max(1.0, 150.0 * (double)user.getHealth() / (double)user.getMaxHealth());
      user.attack.getMove().setBasePower(power);
      return super.applyEffectDuring(user, target);
   }
}
