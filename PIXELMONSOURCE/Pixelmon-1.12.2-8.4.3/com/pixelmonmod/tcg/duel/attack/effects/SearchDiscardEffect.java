package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;
import java.util.List;

public class SearchDiscardEffect extends BaseAttackEffect {
   private static final String CODE = "SEARCHDISCARD";
   private int searchCount;
   private CardType searchType;

   public SearchDiscardEffect() {
      super("SEARCHDISCARD");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CardSelectorState selector = new CardSelectorState(this.searchCount, this.searchCount, CardSelectorDisplay.Select, false);
      int pos = 0;
      Iterator var6 = player.getDiscardPile().iterator();

      while(var6.hasNext()) {
         ImmutableCard card = (ImmutableCard)var6.next();
         if (card != null && card.getCardType() == this.searchType) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState(card), true, BoardLocation.DiscardPile, pos));
            ++pos;
         }
      }

      if (selector.getCardList().size() == 0) {
         return null;
      } else {
         return selector;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (server.getPlayer(server.getCurrentTurn()).getDiscardPile().size() == 0) {
         return true;
      } else if (this.getSelectorState(parameters, server) == null) {
         return true;
      } else {
         return parameters.size() == Math.min(this.searchCount, server.getPlayer(server.getCurrentTurn()).getDiscardPile().size());
      }
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (parameters != null && parameters.size() >= Math.min(this.searchCount, server.getPlayer(server.getCurrentTurn()).getDiscardPile().size())) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());
         PlayerServerState opp = server.getPlayer(server.getNextTurn());

         for(int j = 0; j < this.searchCount; ++j) {
            CommonCardState selected = (CommonCardState)parameters.get(j);
            player.getDiscardPile().remove(selected.getData());
            player.getHand().add(selected.getData());
         }

      }
   }

   public BaseAttackEffect parse(String... args) {
      this.searchCount = Integer.parseInt(args[1]);
      this.searchType = CardType.getCardTypeFromString(args[2]);
      return super.parse(args);
   }
}
