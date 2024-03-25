package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PokemonBreeder extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      if (client.isDisablingEvolution()) {
         return false;
      } else {
         Iterator var2 = ((List)client.getMe().getHand().stream().filter((c) -> {
            return c.getCardType() == CardType.STAGE2;
         }).collect(Collectors.toList())).iterator();

         while(var2.hasNext()) {
            ImmutableCard stage2 = (ImmutableCard)var2.next();
            if (this.isBasicOf(stage2, client.getMe().getActiveCard())) {
               return true;
            }

            for(int i = 0; i < client.getMe().getBenchCards().length; ++i) {
               if (this.isBasicOf(stage2, client.getMe().getBenchCards()[i])) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      CardSelectorState selector;
      PlayerServerState player;
      if (!trainer.getParameters().isEmpty()) {
         selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "card.BS76.effect.selector.basic");
         player = server.getPlayer(server.getCurrentTurn());
         CommonCardState stage2 = (CommonCardState)trainer.getParameters().get(0);
         if (this.isBasicOf(stage2.getData(), player.getActiveCard())) {
            selector.getCardList().add(new CardWithLocation(player.getActiveCard(), true, BoardLocation.Active, 0));
         }

         for(int i = 0; i < player.getBenchCards().length; ++i) {
            if (this.isBasicOf(stage2.getData(), player.getBenchCards()[i])) {
               selector.getCardList().add(new CardWithLocation(player.getBenchCards()[i], true, BoardLocation.Bench, i));
            }
         }

         return selector;
      } else {
         selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "card.BS76.effect.selector.stage2");
         player = server.getPlayer(server.getCurrentTurn());
         Iterator var5 = ((List)player.getHand().stream().filter((c) -> {
            return c.getCardType() == CardType.STAGE2;
         }).collect(Collectors.toList())).iterator();

         while(true) {
            while(var5.hasNext()) {
               ImmutableCard stage2 = (ImmutableCard)var5.next();
               if (this.isBasicOf(stage2, player.getActiveCard())) {
                  selector.getCardList().add(new CardWithLocation(new CommonCardState(stage2), true, BoardLocation.Hand, 0));
               } else {
                  for(int i = 0; i < player.getBenchCards().length; ++i) {
                     if (this.isBasicOf(stage2, player.getBenchCards()[i])) {
                        selector.getCardList().add(new CardWithLocation(new CommonCardState(stage2), true, BoardLocation.Hand, 0));
                        break;
                     }
                  }
               }
            }

            return selector;
         }
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() >= 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      CommonCardState stage2 = (CommonCardState)trainer.getParameters().get(0);
      PokemonCardState basic = (PokemonCardState)trainer.getParameters().get(1);
      PokemonCardState newCard = LogicHelper.evolveCard(stage2.getData(), basic, server.getTurnCount());
      server.getLog().trackEvolve(basic, newCard, server.getCurrentTurn(), server);
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PokemonCardState oldCard = player.getActiveCard();
      if (player.getActiveCard() == basic) {
         player.setActiveCard(newCard);
      } else {
         for(int i = 0; i < player.getBenchCards().length; ++i) {
            if (player.getBenchCards()[i] == basic) {
               player.getBenchCards()[i] = newCard;
            }
         }
      }

      if (newCard != null) {
         PlayerServerState[] var14 = server.getPlayers();
         int var9 = var14.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            PlayerServerState player0 = var14[var10];
            Iterator var12 = player0.getActiveAndBenchCards().iterator();

            while(var12.hasNext()) {
               PokemonCardState pokemon = (PokemonCardState)var12.next();
               if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
                  pokemon.getAbility().getEffect().onSwitchActiveCard(newCard, oldCard, player, pokemon, player0, server);
               }

               if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null) {
                  pokemon.getHiddenAbility().getEffect().onSwitchActiveCard(newCard, oldCard, player, pokemon, player0, server);
               }
            }
         }
      }

      player.getHand().remove(stage2.getData());
   }

   private boolean isBasicOf(ImmutableCard stage2, PokemonCardState card) {
      if (card != null && card.getData().getCardType() == CardType.BASIC) {
         Optional stage1 = CardRegistry.getAll().stream().filter((c) -> {
            return c.getPokemonID() == stage2.getPrevEvoID() && c.getCardType() == CardType.STAGE1;
         }).findFirst();
         if (stage1.isPresent() && ((ImmutableCard)stage1.get()).getPrevEvoID() == card.getData().getPokemonID()) {
            return true;
         }
      }

      return false;
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
