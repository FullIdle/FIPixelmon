package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
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

public class SearchDiscardWithEnergyCostEffect extends BaseAttackEffect {
   private static final String CODE = "SEARCHDISCARDCOST";
   private int searchCount;
   private CardType searchType;
   private int discardCount;
   private Energy discardEnergy;

   public SearchDiscardWithEnergyCostEffect() {
      super("SEARCHDISCARDCOST");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CardSelectorState selector;
      if (parameters.size() < this.discardCount) {
         selector = new CardSelectorState(this.discardCount, this.discardCount, CardSelectorDisplay.Discard, false);
         Iterator var8 = player.getActiveCard().getAttachments().iterator();

         while(var8.hasNext()) {
            CommonCardState card = (CommonCardState)var8.next();
            if (card != null && card.getCardType() == CardType.ENERGY && card.getMainEnergy() == this.discardEnergy) {
               selector.getCardList().add(new CardWithLocation(card, true, BoardLocation.Attachment, 0));
            }
         }

         if (selector.getCardList().size() == 0) {
            return null;
         } else {
            return selector;
         }
      } else {
         selector = new CardSelectorState(this.searchCount, this.searchCount, CardSelectorDisplay.Select, false);
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
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (server.getPlayer(server.getCurrentTurn()).getDiscardPile().size() == 0) {
         return true;
      } else if (this.getSelectorState(parameters, server) == null) {
         return true;
      } else {
         return parameters.size() >= this.discardCount + Math.min(this.searchCount, server.getPlayer(server.getCurrentTurn()).getDiscardPile().size());
      }
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (parameters != null && parameters.size() >= this.discardCount + Math.min(this.searchCount, server.getPlayer(server.getCurrentTurn()).getDiscardPile().size())) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());
         PlayerServerState opp = server.getPlayer(server.getNextTurn());

         int j;
         CommonCardState selected;
         for(j = 0; j < this.discardCount; ++j) {
            selected = (CommonCardState)parameters.get(j);
            player.getActiveCard().getAttachments().remove(selected);
            player.getDiscardPile().add(selected.getData());
         }

         for(j = 0; j < this.searchCount; ++j) {
            selected = (CommonCardState)parameters.get(j + this.discardCount);
            player.getDiscardPile().remove(selected.getData());
            player.getHand().add(selected.getData());
         }

      }
   }

   public BaseAttackEffect parse(String... args) {
      this.discardCount = Integer.parseInt(args[1]);
      this.discardEnergy = Energy.getEnergyFromString(args[2]);
      this.searchCount = Integer.parseInt(args[3]);
      this.searchType = CardType.getCardTypeFromString(args[4]);
      return super.parse(args);
   }
}
