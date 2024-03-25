package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class GorillaTactics extends AbilityBase {
   public void preProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
      if (!pokemon.bc.simulateMode && pokemon.isDynamax <= 0) {
         pokemon.choiceLocked = a;
      }

   }

   public void applySwitchInEffect(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode) {
         pw.choiceLocked = null;
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode) {
         pw.choiceLocked = null;
      }

   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.isDynamax <= 0) {
         int var10001 = StatsType.Attack.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      Moveset moveset = pokemon.getMoveset();

      for(int i = 0; i < moveset.size(); ++i) {
         Attack currentMove = moveset.get(i);
         if (pokemon.choiceLocked != null && !currentMove.equals(pokemon.choiceLocked)) {
            currentMove.setDisabled(true, pokemon);
         }
      }

   }
}
