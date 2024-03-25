package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class GustOfWind extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientOpponentState opp = client.getOpponent();
      if (opp.getActiveCard() == null) {
         return false;
      } else {
         PokemonCardState[] var3 = opp.getBenchCards();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PokemonCardState card = var3[var5];
            if (card != null) {
               return true;
            }
         }

         return false;
      }
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);
      PlayerServerState opp = server.getPlayer(server.getNextTurn());

      for(int i = 0; i < opp.getBenchCards().length; ++i) {
         if (opp.getBenchCards()[i] != null) {
            selector.getCardList().add(new CardWithLocation(opp.getBenchCards()[i], false, BoardLocation.Bench, i));
         }
      }

      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 1;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      opp.switchActive(pokemon, server);
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
