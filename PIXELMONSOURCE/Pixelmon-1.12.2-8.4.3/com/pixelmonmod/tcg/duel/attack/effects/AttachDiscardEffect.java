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

public class AttachDiscardEffect extends BaseAttackEffect {
   private static final String CODE = "ATTACHDISCARD";
   private CardType type;
   private int count;
   private Energy energy;

   public AttachDiscardEffect() {
      super("ATTACHDISCARD");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CardSelectorState selector = new CardSelectorState(1, this.count, CardSelectorDisplay.Select, false);
      int pos = 0;
      Iterator var6 = player.getDiscardPile().iterator();

      while(true) {
         ImmutableCard card;
         do {
            do {
               do {
                  if (!var6.hasNext()) {
                     if (selector.getCardList().size() == 0) {
                        return null;
                     }

                     return selector;
                  }

                  card = (ImmutableCard)var6.next();
               } while(card == null);
            } while(card.getCardType() != this.type);
         } while(this.energy != null && (this.energy == null || this.energy != card.getMainEnergy()));

         selector.getCardList().add(new CardWithLocation(new CommonCardState(card), true, BoardLocation.DiscardPile, pos));
         ++pos;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (server.getPlayer(server.getCurrentTurn()).getDiscardPile().size() == 0) {
         return true;
      } else if (this.getSelectorState(parameters, server) == null) {
         return true;
      } else {
         return parameters.size() > 0;
      }
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (parameters != null && !parameters.isEmpty()) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());

         for(int j = 0; j < parameters.size(); ++j) {
            CommonCardState selected = (CommonCardState)parameters.get(j);
            player.getDiscardPile().remove(selected.getData());
            player.getActiveCard().getAttachments().add(selected);
         }

      }
   }

   public BaseAttackEffect parse(String... args) {
      if (args.length == 4) {
         this.type = CardType.getCardTypeFromString(args[2]);
         this.count = Integer.parseInt(args[1]);
         this.energy = Energy.getEnergyFromString(args[3]);
      } else {
         this.type = CardType.getCardTypeFromString(args[2]);
         this.count = Integer.parseInt(args[1]);
         this.energy = null;
      }

      return super.parse(args);
   }
}
