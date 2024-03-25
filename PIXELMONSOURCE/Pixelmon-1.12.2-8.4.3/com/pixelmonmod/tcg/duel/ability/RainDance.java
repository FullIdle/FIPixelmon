package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.SelectorHelper;

public class RainDance extends BaseAbilityEffect {
   public RainDance() {
      super("RainDance");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && client.getMe().getHand().stream().anyMatch((c) -> {
         return c.getCardType() == CardType.ENERGY && c.getMainEnergy() == Energy.WATER;
      });
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      return SelectorHelper.generateSelectorForActiveAndBench(server.getPlayer(server.getCurrentTurn()), "ability.raindance.selector.chooseattach");
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return !pokemon.getParameters().isEmpty();
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState attachTo = (PokemonCardState)pokemon.getParameters().get(0);
      ImmutableCard water = (ImmutableCard)player.getHand().stream().filter((c) -> {
         return c.getCardType() == CardType.ENERGY && c.getMainEnergy() == Energy.WATER;
      }).findFirst().get();
      player.getHand().remove(water);
      attachTo.getAttachments().add(new CommonCardState(water));
   }
}
