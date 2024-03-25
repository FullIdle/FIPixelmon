package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Shift extends BaseAbilityEffect {
   public Shift() {
      super("Shift");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      List energies = this.getEnergies(pokemon, client.getMe(), client.getOpponent());
      return super.isEnabled(pokemon, client) && pokemon.getParameters().isEmpty() && !energies.isEmpty();
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.shift.selector.change");
      this.getEnergies(pokemon, server.getPlayer(server.getCurrentTurn()), server.getPlayer(server.getNextTurn())).forEach((e) -> {
         selector.getCardList().add(new CardWithLocation(new CommonCardState(CardRegistry.getEnergyCard(e)), true, (BoardLocation)null, 0));
      });
      return selector;
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return !pokemon.getParameters().isEmpty();
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      pokemon.setOverwriteEnergy(((CommonCardState)pokemon.getParameters().get(0)).getMainEnergy());
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
   }

   private List getEnergies(PokemonCardState pokemon, PlayerCommonState me, PlayerCommonState opp) {
      List energies = (List)me.getActiveAndBenchCards().stream().map(CommonCardState::getMainEnergy).collect(Collectors.toList());
      energies.addAll((Collection)opp.getActiveAndBenchCards().stream().map(CommonCardState::getMainEnergy).collect(Collectors.toList()));
      energies = (List)energies.stream().distinct().collect(Collectors.toList());
      energies.remove(pokemon.getMainEnergy());
      energies.remove(Energy.COLORLESS);
      return energies;
   }
}
