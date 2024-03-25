package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;

public class ScoopUp extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);
      selector.getCardList().addAll(server.getPlayer(server.getCurrentTurn()).getActiveAndBenchCards(true));
      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return !trainer.getParameters().isEmpty();
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CommonCardState basic = null;
      if (pokemon.getData().getCardType() == CardType.BASIC) {
         basic = pokemon;
      }

      Iterator var6 = pokemon.getAttachments().iterator();

      while(true) {
         while(var6.hasNext()) {
            CommonCardState attachment = (CommonCardState)var6.next();
            if (basic == null && attachment.getData().getCardType() == CardType.BASIC) {
               basic = attachment;
            } else {
               player.getDiscardPile().add(attachment.getData());
            }
         }

         if (basic == null) {
            basic = pokemon;
         }

         if (basic != pokemon) {
            player.getDiscardPile().add(pokemon.getData());
         }

         if (player.getActiveCard() == pokemon) {
            player.setActiveCard((PokemonCardState)null);
         }

         for(int i = 0; i < player.getBenchCards().length; ++i) {
            if (player.getBenchCards()[i] == pokemon) {
               player.getBenchCards()[i] = null;
            }
         }

         player.getHand().add(((CommonCardState)basic).getData());
         return;
      }
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && (card.getLocation() == BoardLocation.Bench || card.getLocation() == BoardLocation.Active);
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CommonCardState basic = null;
      if (pokemon.getData().getCardType() == CardType.BASIC) {
         basic = pokemon;
      }

      Iterator var8 = pokemon.getAttachments().iterator();

      while(true) {
         while(var8.hasNext()) {
            CommonCardState attachment = (CommonCardState)var8.next();
            if (basic == null && attachment.getData().getCardType() == CardType.BASIC) {
               basic = attachment;
            } else {
               player.getDiscardPile().add(attachment.getData());
            }
         }

         if (basic == null) {
            basic = pokemon;
         }

         if (basic != pokemon) {
            player.getDiscardPile().add(pokemon.getData());
         }

         if (player.getActiveCard() == pokemon) {
            player.setActiveCard((PokemonCardState)null);
         }

         for(int i = 0; i < player.getBenchCards().length; ++i) {
            if (player.getBenchCards()[i] == pokemon) {
               player.getBenchCards()[i] = null;
            }
         }

         player.getHand().add(((CommonCardState)basic).getData());
         return;
      }
   }
}
