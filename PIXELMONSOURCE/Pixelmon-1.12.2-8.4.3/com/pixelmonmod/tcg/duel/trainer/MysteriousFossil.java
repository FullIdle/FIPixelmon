package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class MysteriousFossil extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PokemonCardState[] benchCards = client.getMe().getBenchCards();
      PokemonCardState[] var3 = benchCards;
      int var4 = benchCards.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PokemonCardState card = var3[var5];
         if (card == null) {
            return true;
         }
      }

      return false;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PokemonCardState[] bench = player.getBenchCards();

      for(int i = 0; i < bench.length; ++i) {
         if (bench[i] == null) {
            bench[i] = new PokemonCardState(this, trainer, server.getCurrentTurn());
            bench[i].setCardType(CardType.BASIC);
            break;
         }
      }

   }

   public boolean preventDiscard() {
      return true;
   }

   public boolean showDiscardButton() {
      return true;
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() == null && card.isMine() && (card.getLocation() == BoardLocation.Active || card.getLocation() == BoardLocation.Bench);
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      PokemonCardState card = new PokemonCardState(this, trainer, server.getCurrentTurn());
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      if (b == BoardLocation.Active) {
         player.setActiveCard(card);
         player.getActiveCard().setCardType(CardType.BASIC);
      } else if (b == BoardLocation.Bench) {
         player.getBenchCards()[p] = card;
         player.getBenchCards()[p].setCardType(CardType.BASIC);
      }

   }
}
