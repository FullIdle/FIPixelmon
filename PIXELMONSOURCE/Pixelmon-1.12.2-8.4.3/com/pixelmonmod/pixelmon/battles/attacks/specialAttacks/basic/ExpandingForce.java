package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.PsychicTerrain;

public class ExpandingForce extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.getTerrain() instanceof PsychicTerrain) {
         user.attack.overridePower = user.attack.getMove().getBasePower() * 3 / 2;
         if (user.targets.size() > 1) {
            user.bc.sendToAll("attack.expanding_force.onpsychicterrain");
         }
      }

      return AttackResult.proceed;
   }
}
