package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Switch extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientMyState me = client.getMe();
      if (me.getActiveCard() == null) {
         return false;
      } else {
         PokemonCardState[] var3 = me.getBenchCards();
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
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] != null) {
            selector.getCardList().add(new CardWithLocation(me.getBenchCards()[i], true, BoardLocation.Bench, i));
         }
      }

      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 1;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState benchCard = (PokemonCardState)trainer.getParameters().get(0);
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.switchActive(benchCard, server);
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && card.getLocation() == BoardLocation.Bench;
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.switchActive(pokemon, server);
   }
}
