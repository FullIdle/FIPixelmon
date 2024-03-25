package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;
import java.util.List;

public class ChangeResistanceEffect extends BaseAttackEffect {
   private static final String CODE = "CHANGE_RESISTANCE";
   private boolean onMe;

   public ChangeResistanceEffect() {
      super("CHANGE_RESISTANCE");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      if (!this.hasResistance(server)) {
         return null;
      } else {
         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "attack.effect.changeresistance.selector");
         Iterator var4 = CardRegistry.getEnergyCards().iterator();

         while(var4.hasNext()) {
            ImmutableCard energyCard = (ImmutableCard)var4.next();
            selector.getCardList().add(new CardWithLocation(new CommonCardState(energyCard), false, (BoardLocation)null, 0));
         }

         return selector;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (!this.hasResistance(server)) {
         return true;
      } else {
         return parameters.size() == 1;
      }
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (this.hasResistance(server)) {
         CommonCardState energy = (CommonCardState)parameters.get(0);
         PokemonCardState applyOn = server.getPlayer(this.onMe ? server.getCurrentTurn() : server.getNextTurn()).getActiveCard();
         applyOn.setResistance(energy.getMainEnergy());
      }
   }

   protected boolean hasResistance(GameServerState server) {
      return server.getPlayer(this.onMe ? server.getCurrentTurn() : server.getNextTurn()).getActiveCard().getResistance() != null;
   }

   public BaseAttackEffect parse(String... args) {
      this.onMe = args[1].equalsIgnoreCase("SELF");
      return super.parse(args);
   }
}
