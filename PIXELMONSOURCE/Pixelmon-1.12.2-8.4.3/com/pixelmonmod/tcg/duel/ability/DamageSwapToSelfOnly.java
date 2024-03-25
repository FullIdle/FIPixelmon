package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import java.util.List;
import java.util.stream.Collectors;

public class DamageSwapToSelfOnly extends BaseAbilityEffect {
   public DamageSwapToSelfOnly() {
      super("DamageSwapToSelfOnly");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      if (!super.isEnabled(pokemon, client)) {
         return false;
      } else {
         List hasDamage = (List)client.getMe().getActiveAndBenchCards().stream().filter((c) -> {
            return c.getStatus().getDamage() > 0;
         }).collect(Collectors.toList());
         if (hasDamage.size() == 0) {
            return false;
         } else {
            if (hasDamage.contains(pokemon)) {
               hasDamage.remove(pokemon);
            }

            if (hasDamage.size() == 0) {
               return false;
            } else {
               return pokemon.getStatus().getDamage() + 10 < pokemon.getHP();
            }
         }
      }
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.damageswap.selector.from");
      player.getActiveAndBenchCards(true).stream().filter((location) -> {
         return ((PokemonCardState)location.getCard()).getStatus().getDamage() > 0 && location.getCard() != pokemon;
      }).forEach((location) -> {
         selector.getCardList().add(location);
      });
      return selector;
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return pokemon.getParameters().size() == 1;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState fromPokemon = (PokemonCardState)pokemon.getParameters().get(0);
      fromPokemon.getStatus().healDamage(10);
      pokemon.getStatus().setDamage(pokemon.getStatus().getDamage() + 10);
   }
}
