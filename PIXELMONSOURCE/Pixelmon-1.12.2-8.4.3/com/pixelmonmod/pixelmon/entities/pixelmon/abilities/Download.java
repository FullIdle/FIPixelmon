package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Iterator;

public class Download extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      int totalDef = 0;
      int totalSpDef = 0;

      PixelmonWrapper opponent;
      for(Iterator var4 = newPokemon.bc.getOpponentPokemon(newPokemon.getParticipant()).iterator(); var4.hasNext(); totalSpDef += opponent.getBattleStats().specialDefenceStat) {
         opponent = (PixelmonWrapper)var4.next();
         totalDef += opponent.getBattleStats().defenceStat;
      }

      this.sendActivatedMessage(newPokemon);
      if (totalDef < totalSpDef) {
         newPokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
      } else {
         newPokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
      }

   }
}
