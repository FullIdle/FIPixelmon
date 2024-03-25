package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
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
import java.util.stream.Collectors;

public class DiscardAttachmentEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_DISCARD", "OPP_DISCARD"};
   public static final int ALL_COUNT = -1;
   private boolean onMe;
   private Energy energy;
   private int count;

   public DiscardAttachmentEffect() {
      super(CODES);
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      if (this.energy == null && this.count != -1) {
         List energyCards = this.getEnergyCards(server);
         int realCount = Math.min(this.count, energyCards.size());
         if (realCount == 0) {
            return null;
         } else {
            CardSelectorState selector = new CardSelectorState(realCount, realCount, CardSelectorDisplay.Discard, false);
            Iterator var6 = energyCards.iterator();

            while(var6.hasNext()) {
               CommonCardState card = (CommonCardState)var6.next();
               selector.getCardList().add(new CardWithLocation(card, this.onMe, BoardLocation.Attachment, 0));
            }

            return selector;
         }
      } else {
         return null;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (this.energy == null && this.count != -1) {
         List energyCards = this.getEnergyCards(server);
         if (energyCards.isEmpty()) {
            return true;
         } else {
            return parameters.size() == Math.min(energyCards.size(), this.count);
         }
      } else {
         return true;
      }
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState applyingPlayer = this.onMe ? server.getPlayer(server.getCurrentTurn()) : server.getPlayer(server.getNextTurn());
      PokemonCardState applyingCard = applyingPlayer.getActiveCard();
      List energyCards = this.getEnergyCards(server);
      if (!energyCards.isEmpty()) {
         int totalCount = Math.min(this.count, energyCards.size());
         int discardCount = 0;
         int i = 0;
         if (this.energy == null && this.count != -1) {
            Iterator var14 = parameters.iterator();

            while(var14.hasNext()) {
               Object parameter = var14.next();
               CommonCardState attachment = (CommonCardState)parameter;
               applyingCard.getAttachments().remove(attachment);
               applyingPlayer.getDiscardPile().add(attachment.getData());
            }
         } else {
            CommonCardState attachment;
            for(; i < applyingCard.getAttachments().size(); ++i) {
               attachment = (CommonCardState)applyingCard.getAttachments().get(i);
               if (attachment.getCardType() == CardType.ENERGY && attachment.getMainEnergy() == this.energy) {
                  applyingCard.getAttachments().remove(i);
                  applyingPlayer.getDiscardPile().add(attachment.getData());
                  ++discardCount;
                  --i;
                  if (discardCount == totalCount) {
                     break;
                  }
               }
            }

            if (discardCount < totalCount) {
               for(i = 0; i < applyingCard.getAttachments().size(); ++i) {
                  attachment = (CommonCardState)applyingCard.getAttachments().get(i);
                  if (attachment.getAbility() != null && attachment.getAbility().getEffect() != null) {
                     List equivalence = attachment.getAbility().getEffect().getEnergyEquivalence(attachment);
                     List matched = (List)equivalence.stream().filter((c) -> {
                        return c.getMainEnergy() == this.energy;
                     }).collect(Collectors.toList());
                     if (!matched.isEmpty()) {
                        applyingCard.getAttachments().remove(i);
                        applyingPlayer.getDiscardPile().add(attachment.getData());
                        --i;
                        discardCount += matched.size();
                     }

                     if (discardCount >= totalCount) {
                        break;
                     }
                  }
               }
            }
         }

      }
   }

   private List getEnergyCards(GameServerState server) {
      PlayerServerState player = this.onMe ? server.getPlayer(server.getCurrentTurn()) : server.getPlayer(server.getNextTurn());
      return (List)player.getActiveCard().getAttachments().stream().filter((c) -> {
         return c.isEnergyEquivalence();
      }).collect(Collectors.toList());
   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.onMe = types[0].equalsIgnoreCase("SELF");
      this.count = -1;
      this.energy = Energy.getEnergyFromString(args[1]);
      if (!args[2].equalsIgnoreCase("ALL") && !args[2].equalsIgnoreCase("*")) {
         this.count = Integer.parseInt(args[2]);
      }

      return super.parse(args);
   }
}
