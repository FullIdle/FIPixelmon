package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Moody extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      int randomStatToIncrease = -1;
      int randomStatToDecrease = -1;
      this.sendActivatedMessage(pokemon);
      int[] statStages = pokemon.getBattleStats().getStages();
      if (pokemon.getBattleStats().statCanBeRaised()) {
         while(randomStatToIncrease == -1) {
            randomStatToIncrease = RandomHelper.getRandomNumberBetween(2, 6);
            if (statStages[randomStatToIncrease] < 6) {
               pokemon.getBattleStats().modifyStat(2, (StatsType)pokemon.getBattleStats().getStageEnum(randomStatToIncrease));
            } else {
               randomStatToIncrease = -1;
            }
         }
      }

      boolean canDecrease = false;

      for(int i = 0; i < statStages.length; ++i) {
         if (i != randomStatToIncrease && statStages[i] < 6) {
            canDecrease = true;
            break;
         }
      }

      if (canDecrease) {
         while(randomStatToDecrease == -1) {
            do {
               randomStatToDecrease = RandomHelper.getRandomNumberBetween(2, 6);
            } while(randomStatToIncrease == randomStatToDecrease);

            if (statStages[randomStatToDecrease] > -6) {
               pokemon.getBattleStats().modifyStat(-1, (StatsType)pokemon.getBattleStats().getStageEnum(randomStatToDecrease));
            } else {
               randomStatToDecrease = -1;
            }
         }
      }

   }
}
