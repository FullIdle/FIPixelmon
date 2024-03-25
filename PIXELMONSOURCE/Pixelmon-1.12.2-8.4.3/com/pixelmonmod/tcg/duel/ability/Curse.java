package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import java.util.Iterator;
import java.util.Objects;

public class Curse extends BaseAbilityEffect {
   public Curse() {
      super("Curse");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && this.isAnyOpponentCardDamaged(client);
   }

   private boolean isAnyOpponentCardDamaged(GameClientState client) {
      if (client.getOpponent().getActiveAndBenchCards().size() == 1) {
         return false;
      } else {
         Iterator var2 = client.getOpponent().getActiveAndBenchCards().iterator();

         PokemonCardState activeAndBenchCard;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            activeAndBenchCard = (PokemonCardState)var2.next();
         } while(activeAndBenchCard.getStatus().getDamage() <= 0);

         return true;
      }
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getCurrentOpponent();
      switch (pokemon.getParameters().size()) {
         case 0:
            CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, true, "ability.damageswap.selector.from");
            Iterator var9 = player.getActiveAndBenchCards(false).iterator();

            while(var9.hasNext()) {
               CardWithLocation activeAndBenchCard = (CardWithLocation)var9.next();
               if (activeAndBenchCard.getCard() instanceof PokemonCardState && ((PokemonCardState)activeAndBenchCard.getCard()).getStatus().getDamage() > 0) {
                  selector.addCard(activeAndBenchCard);
               }
            }

            return selector;
         case 1:
            PokemonCardState fromPokemon = (PokemonCardState)pokemon.getParameters().get(0);
            CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, true, "ability.damageswap.selector.to");
            Iterator var6 = player.getActiveAndBenchCards(false).iterator();

            while(var6.hasNext()) {
               CardWithLocation activeAndBenchCard = (CardWithLocation)var6.next();
               if (!Objects.equals(fromPokemon, activeAndBenchCard.getCard())) {
                  selector.addCard(activeAndBenchCard);
               }
            }

            return selector;
         default:
            return null;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState server) {
      return pokemon.getParameters().size() == 2;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState fromPokemon = (PokemonCardState)pokemon.getParameters().get(0);
      PokemonCardState toPokemon = (PokemonCardState)pokemon.getParameters().get(1);
      fromPokemon.getStatus().healDamage(10);
      toPokemon.getStatus().setDamage(toPokemon.getStatus().getDamage() + 10);
   }
}
