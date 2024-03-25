package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SlowStart extends AbilityBase {
   private int turnsRemaining = 5;

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.abilities.slowstart", newPokemon.getNickname());
   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      this.turnsRemaining = 5;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (this.turnsRemaining > 0) {
         int var10001 = StatsType.Attack.getStatIndex();
         stats[var10001] /= 2;
         var10001 = StatsType.Speed.getStatIndex();
         stats[var10001] /= 2;
      }

      return stats;
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (--this.turnsRemaining == 0) {
         pokemon.bc.sendToAll("pixelmon.abilities.slowstartend", pokemon.getNickname());
      }

   }

   public boolean needNewInstance() {
      return true;
   }
}
