package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Hex extends SpecialAttackBase {
   public int basePower = 65;
   public int statusPower = 130;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.getMove().setBasePower(target.hasPrimaryStatus() ? this.statusPower : this.basePower);
      return AttackResult.proceed;
   }
}
