package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
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
import java.util.Iterator;
import java.util.List;

public class DevolutionSpray extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return !this.getCardSelectorStateForEvolutionCard(client).getCardList().isEmpty();
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      if (trainer.getParameters().isEmpty()) {
         return this.getCardSelectorStateForEvolutionCard(new GameClientState(server));
      } else {
         PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
         CardSelectorState selectorState = new CardSelectorState(1, 1, CardSelectorDisplay.Discard, false, "card.BS72.effect.selector.chooseevolution");
         selectorState.getCardList().add(new CardWithLocation(pokemon, true, BoardLocation.Attachment, 0));
         pokemon.getAttachments().stream().filter((card) -> {
            return card.getData().isEvolution() && this.hasLowerEvolution(card.getData().getPrevEvoID(), pokemon.getAttachments());
         }).forEach((card) -> {
            selectorState.getCardList().add(new CardWithLocation(card, true, BoardLocation.Attachment, 0));
         });
         return selectorState;
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      CommonCardState card = (CommonCardState)trainer.getParameters().get(1);
      CommonCardState retain = this.getLowerEvolution(card.getData().getPrevEvoID(), pokemon.getAttachments());
      pokemon.getAttachments().remove(retain);
      PokemonCardState newPokemon;
      if (card != pokemon) {
         for(newPokemon = this.getLowerEvolution(pokemon.getData().getPrevEvoID(), pokemon.getAttachments()); newPokemon != card && newPokemon != null; newPokemon = this.getLowerEvolution(newPokemon.getData().getPrevEvoID(), pokemon.getAttachments())) {
            pokemon.getAttachments().remove(newPokemon);
            player.getDiscardPile().add(newPokemon.getData());
         }

         pokemon.getAttachments().remove(card);
         player.getDiscardPile().add(card.getData());
      }

      player.getDiscardPile().add(pokemon.getData());
      newPokemon = new PokemonCardState(retain.getData(), server.getCurrentTurn());
      newPokemon.getStatus().setDamage(pokemon.getStatus().getDamage());
      newPokemon.getAttachments().addAll(pokemon.getAttachments());
      if (pokemon == player.getActiveCard()) {
         player.setActiveCard(newPokemon);
      } else {
         for(int i = 0; i < player.getBenchCards().length; ++i) {
            if (player.getBenchCards()[i] == pokemon) {
               player.getBenchCards()[i] = newPokemon;
            }
         }
      }

   }

   private CardSelectorState getCardSelectorStateForEvolutionCard(GameClientState client) {
      CardSelectorState selectorState = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "card.BS72.effect.selector.choosepokemon");
      PlayerClientMyState player = client.getMe();
      if (player.getActiveCard().getData().isEvolution() && this.hasLowerEvolution(player.getActiveCard().getData().getPrevEvoID(), player.getActiveCard().getAttachments())) {
         selectorState.getCardList().add(new CardWithLocation(player.getActiveCard(), true, BoardLocation.Active, 0));
      }

      for(int i = 0; i < player.getBenchCards().length; ++i) {
         if (player.getBenchCards()[i] != null && player.getBenchCards()[i].getData().isEvolution() && this.hasLowerEvolution(player.getBenchCards()[i].getData().getPrevEvoID(), player.getBenchCards()[i].getAttachments())) {
            selectorState.getCardList().add(new CardWithLocation(player.getBenchCards()[i], true, BoardLocation.Bench, 0));
         }
      }

      return selectorState;
   }

   private boolean hasLowerEvolution(int pokemonId, List attachments) {
      return this.getLowerEvolution(pokemonId, attachments) != null;
   }

   private PokemonCardState getLowerEvolution(int pokemonId, List attachments) {
      label27:
      while(true) {
         if (pokemonId != 0) {
            Iterator var3 = attachments.iterator();

            CommonCardState attachment;
            do {
               if (!var3.hasNext()) {
                  ImmutableCard card = CardRegistry.fromId(pokemonId);
                  if (card == null) {
                     return null;
                  }

                  pokemonId = card.getPrevEvoID();
                  continue label27;
               }

               attachment = (CommonCardState)var3.next();
            } while(!(attachment instanceof PokemonCardState) || attachment.isEnergyEquivalence() || attachment.getData().getPokemonID() != pokemonId);

            return (PokemonCardState)attachment;
         }

         return null;
      }
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
