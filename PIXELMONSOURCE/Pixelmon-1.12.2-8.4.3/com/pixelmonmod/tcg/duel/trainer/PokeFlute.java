package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
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

public class PokeFlute extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      boolean hasBench = false;
      PokemonCardState[] var3 = client.getOpponent().getBenchCards();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PokemonCardState card = var3[var5];
         if (card == null) {
            hasBench = true;
            break;
         }
      }

      if (!hasBench) {
         return false;
      } else {
         Iterator var7 = client.getOpponent().getDiscardPile().iterator();

         ImmutableCard card;
         do {
            if (!var7.hasNext()) {
               return false;
            }

            card = (ImmutableCard)var7.next();
         } while(card.getCardType() != CardType.BASIC);

         return true;
      }
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);
      PlayerServerState opp = server.getPlayer(server.getNextTurn());

      for(int i = 0; i < opp.getDiscardPile().size(); ++i) {
         ImmutableCard card = (ImmutableCard)opp.getDiscardPile().get(i);
         if (card.getCardType() == CardType.BASIC) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState(card), false, BoardLocation.DiscardPile, i));
         }
      }

      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 1;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      CommonCardState card = (CommonCardState)trainer.getParameters().get(0);
      PlayerServerState opp = server.getPlayer(server.getNextTurn());

      for(int i = 0; i < opp.getBenchCards().length; ++i) {
         if (opp.getBenchCards()[i] == null) {
            opp.getBenchCards()[i] = new PokemonCardState(card.getData(), server.getTurnCount());
            opp.getDiscardPile().remove(card.getData());
            break;
         }
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
