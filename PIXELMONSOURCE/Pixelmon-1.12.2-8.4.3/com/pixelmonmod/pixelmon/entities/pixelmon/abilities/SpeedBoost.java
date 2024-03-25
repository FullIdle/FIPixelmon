package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SpeedBoost extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (!pokemon.switchedThisTurn) {
         this.sendActivatedMessage(pokemon);
         pokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed);
      }

   }
}
