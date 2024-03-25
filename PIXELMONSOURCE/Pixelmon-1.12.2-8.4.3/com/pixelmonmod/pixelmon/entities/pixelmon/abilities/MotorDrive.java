package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class MotorDrive extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() == EnumType.Electric) {
         pokemon.bc.sendToAll("pixelmon.abilities.motordrive", pokemon.getNickname());
         if (pokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed)) {
            MoveResults var10000 = a.moveResult;
            var10000.weightMod -= 25.0F;
         }

         return false;
      } else {
         return true;
      }
   }
}
