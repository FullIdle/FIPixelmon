package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class EnergyBurn extends BaseAbilityEffect {
   public EnergyBurn() {
      super("EnergyBurn");
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return true;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      pokemon.getAttachments().stream().filter((attachment) -> {
         return attachment.getCardType() == CardType.ENERGY;
      }).forEach((attachment) -> {
         attachment.setOverwriteEnergy(Energy.FIRE);
      });
   }
}
