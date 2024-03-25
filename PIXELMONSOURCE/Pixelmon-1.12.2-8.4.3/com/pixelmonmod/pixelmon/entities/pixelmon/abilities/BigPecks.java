package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class BigPecks extends AbilityBase {
   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      if (e.getStatsType() == StatsType.Defence && !e.getUser()) {
         if (!Attack.dealsDamage(user.attack)) {
            user.bc.sendToAll("pixelmon.abilities.bigpecks", pokemon.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }
}
