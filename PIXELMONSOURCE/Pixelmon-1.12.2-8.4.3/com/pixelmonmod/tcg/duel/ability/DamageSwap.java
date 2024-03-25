package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.List;
import java.util.stream.Collectors;

public class DamageSwap extends BaseAbilityEffect {
   public DamageSwap() {
      super("DamageSwap");
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
            List canMove = (List)client.getMe().getActiveAndBenchCards().stream().filter((c) -> {
               return c.getStatus().getDamage() + 10 < c.getHP();
            }).collect(Collectors.toList());
            return canMove.size() > 1 || canMove.size() == 1 && (hasDamage.size() > 1 || canMove.get(0) != hasDamage.get(0));
         }
      }
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      switch (pokemon.getParameters().size()) {
         case 0:
            CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.damageswap.selector.from");
            List canMove = (List)player.getActiveAndBenchCards().stream().filter((c) -> {
               return c.getStatus().getDamage() + 10 < c.getHP();
            }).collect(Collectors.toList());
            player.getActiveAndBenchCards(true).stream().filter((location) -> {
               return ((PokemonCardState)location.getCard()).getStatus().getDamage() > 0 && (canMove.size() > 1 || canMove.get(0) != location.getCard());
            }).forEach((location) -> {
               selector.getCardList().add(location);
            });
            return selector;
         case 1:
            PokemonCardState fromPokemon = (PokemonCardState)pokemon.getParameters().get(0);
            CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.damageswap.selector.to");
            PokemonCardState active = player.getActiveCard();
            if (active != null && active != fromPokemon && active.getStatus().getDamage() + 10 < active.getHP()) {
               selector.getCardList().add(new CardWithLocation(player.getActiveCard(), true, BoardLocation.Active, 0));
            }

            for(int i = 0; i < player.getBenchCards().length; ++i) {
               PokemonCardState bench = player.getBenchCards()[i];
               if (bench != null && bench != fromPokemon && bench.getStatus().getDamage() + 10 < bench.getHP()) {
                  selector.getCardList().add(new CardWithLocation(player.getBenchCards()[i], true, BoardLocation.Bench, i));
               }
            }

            return selector;
         default:
            return null;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return pokemon.getParameters().size() == 2;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState fromPokemon = (PokemonCardState)pokemon.getParameters().get(0);
      PokemonCardState toPokemon = (PokemonCardState)pokemon.getParameters().get(1);
      fromPokemon.getStatus().healDamage(10);
      toPokemon.getStatus().setDamage(toPokemon.getStatus().getDamage() + 10);
   }
}
