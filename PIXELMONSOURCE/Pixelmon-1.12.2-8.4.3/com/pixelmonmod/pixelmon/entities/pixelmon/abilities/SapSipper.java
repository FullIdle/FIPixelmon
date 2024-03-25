package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnSpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class SapSipper extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() != EnumType.Grass) {
         return true;
      } else {
         if (a.isAttack("SolarBeam")) {
            for(int l = 0; l < a.getMove().effects.size(); ++l) {
               if (a.getMove().effects.get(l) instanceof MultiTurnSpecialAttackBase && ((MultiTurnSpecialAttackBase)a.getMove().effects.get(l)).isCharging(user, pokemon)) {
                  return true;
               }
            }
         }

         pokemon.bc.sendToAll("pixelmon.abilities.sapsipper", pokemon.getNickname());
         pokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
         return false;
      }
   }
}
