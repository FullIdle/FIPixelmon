package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class IntrepidSword extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.sendActivatedMessage(newPokemon);
      newPokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
   }
}
