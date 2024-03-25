package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class WringOut extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      float health = (float)target.getHealth();
      float maxHealth = (float)target.getMaxHealth();
      float power = Math.max(1.0F, 120.0F * (health / maxHealth));
      user.attack.getMove().setBasePower((int)power);
      return AttackResult.proceed;
   }
}
