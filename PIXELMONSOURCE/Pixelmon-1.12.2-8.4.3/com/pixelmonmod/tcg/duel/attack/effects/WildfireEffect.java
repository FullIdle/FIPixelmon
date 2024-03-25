package com.pixelmonmod.tcg.duel.attack.effects;

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
import java.util.stream.Collectors;

public class WildfireEffect extends BaseAttackEffect {
   private static final String CODE = "WILDFIRE";
   private Energy energy;

   public WildfireEffect() {
      super("WILDFIRE");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      if (parameters.size() == 0) {
         List energyCards = this.getEnergyCards(server);
         Iterator var4 = energyCards.iterator();

         while(var4.hasNext()) {
            CommonCardState card = (CommonCardState)var4.next();
            if (card.getMainEnergy() != this.energy) {
               energyCards.remove(card);
            }
         }

         CardSelectorState selector = new CardSelectorState(1, Math.min(energyCards.size(), server.getPlayer(server.getNextTurn()).getDeck().size()), CardSelectorDisplay.Discard, false, "Discard Energy");
         int count = 0;
         Iterator var6 = energyCards.iterator();

         do {
            if (!var6.hasNext()) {
               return selector;
            }

            CommonCardState card = (CommonCardState)var6.next();
            selector.getCardList().add(new CardWithLocation(card, true, BoardLocation.Attachment, 0));
            ++count;
         } while(server.getPlayer(server.getNextTurn()).getDeck().size() != count);

         return selector;
      } else {
         return null;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return parameters.size() > 0 || server.getPlayer(server.getNextTurn()).getDeck().size() < 1;
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState applyingPlayer = server.getPlayer(server.getCurrentTurn());
      PlayerServerState oppPlayer = server.getPlayer(server.getNextTurn());
      Iterator var7 = parameters.iterator();

      while(var7.hasNext()) {
         Object parameter = var7.next();
         CommonCardState attachment = (CommonCardState)parameter;
         card.getAttachments().remove(attachment);
         applyingPlayer.getDiscardPile().add(attachment.getData());
         if (!oppPlayer.getDeck().isEmpty()) {
            ImmutableCard toDiscard = (ImmutableCard)oppPlayer.getDeck().get(0);
            oppPlayer.getDeck().remove(0);
            oppPlayer.getDiscardPile().add(toDiscard);
         }
      }

   }

   private List getEnergyCards(GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      return (List)player.getActiveCard().getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).collect(Collectors.toList());
   }

   public BaseAttackEffect parse(String... args) {
      this.energy = Energy.getEnergyFromString(args[1]);
      return super.parse(args);
   }
}
