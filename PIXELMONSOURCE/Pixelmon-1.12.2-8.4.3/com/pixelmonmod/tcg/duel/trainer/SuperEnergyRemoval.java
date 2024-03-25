package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class SuperEnergyRemoval extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientMyState me = client.getMe();
      boolean hasEnergy = me.getActiveCard().getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence);
      if (!hasEnergy) {
         PokemonCardState[] var4 = me.getBenchCards();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PokemonCardState card = var4[var6];
            if (card != null && card.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
               hasEnergy = true;
               break;
            }
         }
      }

      if (!hasEnergy) {
         return false;
      } else {
         PlayerClientOpponentState opp = client.getOpponent();
         PokemonCardState card = opp.getActiveCard();
         if (card != null && card.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
            return true;
         } else {
            PokemonCardState[] var12 = opp.getBenchCards();
            int var13 = var12.length;

            for(int var8 = 0; var8 < var13; ++var8) {
               PokemonCardState bench = var12[var8];
               if (bench != null && bench.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState opp;
      if (trainer.getParameters().size() == 0) {
         opp = server.getPlayer(server.getCurrentTurn());
         return SelectorHelper.generateSelectorForPokemonWithEnergy(opp, true, (String)null);
      } else {
         CardSelectorState selector;
         PokemonCardState pokemon;
         if (trainer.getParameters().size() == 1) {
            selector = new CardSelectorState(1, 1, CardSelectorDisplay.Discard, false);
            pokemon = (PokemonCardState)trainer.getParameters().get(0);
            pokemon.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).forEach((attachment) -> {
               selector.getCardList().add(new CardWithLocation(attachment, true, BoardLocation.Attachment, 0));
            });
            return selector;
         } else if (trainer.getParameters().size() == 2) {
            opp = server.getPlayer(server.getNextTurn());
            return SelectorHelper.generateSelectorForPokemonWithEnergy(opp, false, (String)null);
         } else if (trainer.getParameters().size() == 3) {
            selector = new CardSelectorState(1, 2, CardSelectorDisplay.Discard, false);
            pokemon = (PokemonCardState)trainer.getParameters().get(2);
            pokemon.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).forEach((attachment) -> {
               selector.getCardList().add(new CardWithLocation(attachment, false, BoardLocation.Attachment, 0));
            });
            return selector;
         } else {
            return null;
         }
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() >= 4;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState myPokemon = (PokemonCardState)trainer.getParameters().get(0);
      CommonCardState myEnergy = (CommonCardState)trainer.getParameters().get(1);
      PokemonCardState oppPokemon = (PokemonCardState)trainer.getParameters().get(2);
      CommonCardState oppEnergy1 = (CommonCardState)trainer.getParameters().get(3);
      CommonCardState oppEnergy2 = null;
      if (trainer.getParameters().size() > 4) {
         oppEnergy2 = (CommonCardState)trainer.getParameters().get(4);
      }

      myPokemon.getAttachments().remove(myEnergy);
      server.getPlayer(server.getCurrentTurn()).getDiscardPile().add(myEnergy.getData());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      oppPokemon.getAttachments().remove(oppEnergy1);
      opp.getDiscardPile().add(oppEnergy1.getData());
      if (oppEnergy2 != null) {
         oppPokemon.getAttachments().remove(oppEnergy2);
         opp.getDiscardPile().add(oppEnergy2.getData());
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
