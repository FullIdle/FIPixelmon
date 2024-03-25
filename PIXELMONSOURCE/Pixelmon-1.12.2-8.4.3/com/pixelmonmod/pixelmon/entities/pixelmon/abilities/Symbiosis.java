package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class Symbiosis extends AbilityBase {
   public void onItemConsumed(PixelmonWrapper pokemon, PixelmonWrapper consumer, ItemHeld heldItem) {
      if (pokemon.hasHeldItem() && !consumer.hasHeldItem() && pokemon.getTeamPokemon().contains(consumer) && pokemon.isItemRemovable(pokemon) && consumer.isItemGivable(pokemon.getHeldItem())) {
         consumer.setNewHeldItem(pokemon.getHeldItem());
         pokemon.removeHeldItem();
      }

   }
}
