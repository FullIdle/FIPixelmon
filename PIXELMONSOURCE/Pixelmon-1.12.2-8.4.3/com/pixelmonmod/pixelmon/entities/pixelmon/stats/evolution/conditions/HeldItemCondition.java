package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.api.item.JsonItemStack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class HeldItemCondition extends EvoCondition {
   public JsonItemStack item;

   public HeldItemCondition() {
      super("heldItem");
   }

   public HeldItemCondition(ItemHeld item) {
      this();
      this.item = new JsonItemStack(item);
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.getPokemonData().getHeldItemAsItemHeld().getRegistryName().toString().equals(this.item.itemID);
   }
}
