package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class EnergyRemoval extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientOpponentState opp = client.getOpponent();
      PokemonCardState card = opp.getActiveCard();
      if (card != null && card.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
         return true;
      } else {
         PokemonCardState[] var4 = opp.getBenchCards();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PokemonCardState bench = var4[var6];
            if (bench != null && bench.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && !card.isMine() && (card.getLocation() == BoardLocation.Active || card.getLocation() == BoardLocation.Bench) && ((PokemonCardState)card.getCard()).getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence);
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      if (trainer.getParameters().size() == 0) {
         PlayerServerState opp = server.getPlayer(server.getNextTurn());
         return SelectorHelper.generateSelectorForPokemonWithEnergy(opp, false, (String)null);
      } else if (trainer.getParameters().size() == 1) {
         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Discard, false);
         PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
         pokemon.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).forEach((attachment) -> {
            selector.getCardList().add(new CardWithLocation(attachment, false, BoardLocation.Attachment, 0));
         });
         return selector;
      } else {
         return null;
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      CommonCardState energy = (CommonCardState)trainer.getParameters().get(1);
      pokemon.getAttachments().remove(energy);
      server.getPlayer(server.getNextTurn()).getDiscardPile().add(energy.getData());
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
