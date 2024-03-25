package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class TerrainPulse extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.getTerrain().type == StatusType.ElectricTerrain) {
         user.attack.overrideType(EnumType.Electric);
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      if (user.bc.globalStatusController.getTerrain().type == StatusType.PsychicTerrain) {
         user.attack.overrideType(EnumType.Psychic);
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      if (user.bc.globalStatusController.getTerrain().type == StatusType.MistyTerrain) {
         user.attack.overrideType(EnumType.Fairy);
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      if (user.bc.globalStatusController.getTerrain().type == StatusType.GrassyTerrain) {
         user.attack.overrideType(EnumType.Grass);
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      return AttackResult.proceed;
   }
}
