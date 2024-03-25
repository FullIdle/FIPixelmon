package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SolarPower extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeather() instanceof Sunny) {
         pokemon.bc.sendToAll("pixelmon.abilities.solarpower", pokemon.getNickname());
         pokemon.doBattleDamage(pokemon, (float)pokemon.getPercentMaxHealth(12.5F), DamageTypeEnum.ABILITY);
      }

   }

   public int[] modifyStats(PixelmonWrapper pokemon, int[] stats) {
      if (pokemon.bc.globalStatusController.getWeather() instanceof Sunny) {
         int var10001 = StatsType.SpecialAttack.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }
}
