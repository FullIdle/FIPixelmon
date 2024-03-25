package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class DauntlessShield extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.sendActivatedMessage(newPokemon);
      newPokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Defence);
   }
}
