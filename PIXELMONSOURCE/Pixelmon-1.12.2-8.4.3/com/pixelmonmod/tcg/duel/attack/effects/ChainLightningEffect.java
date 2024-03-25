package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class ChainLightningEffect extends BaseAttackEffect {
   private static final String CODE = "CHAINLIGHTNING";

   public ChainLightningEffect() {
      super("CHAINLIGHTNING");
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      PokemonCardState active = opp.getActiveCard();
      Energy mainEnergy = active.getMainEnergy();
      if (mainEnergy != Energy.COLORLESS) {
         PokemonCardState[] var10 = opp.getBenchCards();
         int var11 = var10.length;

         int var12;
         PokemonCardState bench;
         for(var12 = 0; var12 < var11; ++var12) {
            bench = var10[var12];
            if (bench != null && bench.getMainEnergy() == mainEnergy) {
               bench.addDamage(player.getActiveCard(), 10, server);
            }
         }

         var10 = player.getBenchCards();
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            bench = var10[var12];
            if (bench != null && bench.getMainEnergy() == mainEnergy) {
               bench.addDamage(player.getActiveCard(), 10, server);
            }
         }
      }

   }
}
