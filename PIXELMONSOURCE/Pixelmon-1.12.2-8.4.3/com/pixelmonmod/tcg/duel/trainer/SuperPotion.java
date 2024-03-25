package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class SuperPotion extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientMyState me = client.getMe();
      PokemonCardState card = me.getActiveCard();
      if (card != null && card.getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
         return true;
      } else {
         PokemonCardState[] var4 = me.getBenchCards();
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
      return card != null && card.isMine() && (card.getLocation() == BoardLocation.Active || card.getLocation() == BoardLocation.Bench) && ((PokemonCardState)card.getCard()).getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence);
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);
      if (trainer.getParameters().size() == 0) {
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         if (me.getActiveCard() != null && me.getActiveCard().getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
            selector.getCardList().add(new CardWithLocation(me.getActiveCard(), true, BoardLocation.Active, 0));
         }

         for(int i = 0; i < me.getBenchCards().length; ++i) {
            if (me.getBenchCards()[i] != null && me.getBenchCards()[i].getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
               selector.getCardList().add(new CardWithLocation(me.getBenchCards()[i], true, BoardLocation.Bench, i));
            }
         }
      } else {
         PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
         pokemon.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).forEach((c) -> {
            selector.getCardList().add(new CardWithLocation(c, true, BoardLocation.Attachment, 0));
         });
      }

      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      CommonCardState energy = (CommonCardState)trainer.getParameters().get(1);
      server.getPlayer(server.getCurrentTurn()).getDiscardPile().add(energy.getData());
      pokemon.getAttachments().remove(energy);
      pokemon.getStatus().healDamage(40);
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
