package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class HeldItemRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"helditem", "hi"});
   private HeldItem heldItem;

   public HeldItemRequirement() {
      super(KEYS);
   }

   public HeldItemRequirement(HeldItem heldItem) {
      this();
      this.heldItem = heldItem;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         if (args.length != 0 && args.length != 1) {
            String rs = "pixelmon:" + args[1];
            Item item = (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(rs));
            if (item == null) {
               return Collections.emptyList();
            } else if (!(item instanceof HeldItem)) {
               return Collections.emptyList();
            } else {
               HeldItem heldItem = (HeldItem)item;
               return Collections.singletonList(this.createInstance(heldItem));
            }
         } else {
            return Collections.emptyList();
         }
      }
   }

   public Requirement createInstance(HeldItem value) {
      return new HeldItemRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return Objects.equals(this.heldItem, pokemon.getHeldItem().func_77973_b());
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setHeldItem(new ItemStack(this.heldItem));
   }

   public HeldItem getValue() {
      return this.heldItem;
   }
}
