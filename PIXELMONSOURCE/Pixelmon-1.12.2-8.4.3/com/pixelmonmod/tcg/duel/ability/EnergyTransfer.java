package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import java.util.Iterator;

public class EnergyTransfer extends BaseAbilityEffect {
   public EnergyTransfer() {
      super("EnergyTransfer");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && client.getMe().getActiveAndBenchCards().size() > 1 && pokemon.getAttachments().stream().anyMatch((c) -> {
         return c.getCardType() == CardType.ENERGY && c.getMainEnergy() == Energy.GRASS && c.getSecondaryEnergy() == null;
      });
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      CardSelectorState selector;
      switch (pokemon.getParameters().size()) {
         case 0:
            selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.energytransfer.selector.from");
            Iterator var4 = server.getPlayer(server.getCurrentTurn()).getActiveAndBenchCards(true).iterator();

            while(var4.hasNext()) {
               CardWithLocation location = (CardWithLocation)var4.next();
               PokemonCardState card = (PokemonCardState)location.getCard();
               if (card.getAttachments().stream().anyMatch((c) -> {
                  return c.getCardType() == CardType.ENERGY && c.getMainEnergy() == Energy.GRASS && c.getSecondaryEnergy() == null;
               })) {
                  selector.getCardList().add(location);
               }
            }

            return selector;
         case 1:
            selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.energytransfer.selector.to");
            server.getPlayer(server.getCurrentTurn()).getActiveAndBenchCards(true).stream().filter((locationx) -> {
               return locationx.getCard() != pokemon.getParameters().get(0);
            }).forEach((locationx) -> {
               selector.getCardList().add(locationx);
            });
            return selector;
         default:
            return null;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return pokemon.getParameters().size() == 2;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState from = (PokemonCardState)pokemon.getParameters().get(0);
      PokemonCardState to = (PokemonCardState)pokemon.getParameters().get(1);
      CommonCardState energy = (CommonCardState)from.getAttachments().stream().filter((c) -> {
         return c.getCardType() == CardType.ENERGY && c.getMainEnergy() == Energy.GRASS && c.getSecondaryEnergy() == null;
      }).findFirst().get();
      from.getAttachments().remove(energy);
      to.getAttachments().add(energy);
   }
}
