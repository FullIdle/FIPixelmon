package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.ElectricTerrain;

public class RisingVoltage extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.getTerrain() instanceof ElectricTerrain) {
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      return AttackResult.proceed;
   }
}
