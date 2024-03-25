package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types;

import com.pixelmonmod.pixelmon.api.item.JsonItemStack;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InteractEvolution extends Evolution {
   public JsonItemStack item = null;
   public Boolean emptyHand = false;

   public InteractEvolution() {
      super("interact");
   }

   public InteractEvolution(PokemonSpec to, Item item, Boolean emptyHand, EvoCondition... conditions) {
      super("interact", to, conditions);
      this.item = item == null ? null : new JsonItemStack(item);
      this.emptyHand = emptyHand != null && emptyHand;
   }

   public boolean canEvolve(EntityPixelmon pokemon, ItemStack stack) {
      return (this.emptyHand || this.item == null || stack.func_77973_b().getRegistryName().toString().equals(this.item.itemID)) && super.canEvolve(pokemon);
   }
}
