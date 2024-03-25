package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity8HoldsItems;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class PokemonDropInformation {
   PokemonSpec pokemon;
   ItemStack mainDrop;
   ItemStack rareDrop;
   ItemStack optDrop1;
   ItemStack optDrop2;
   int mainDropMin = 0;
   int mainDropMax = 1;
   int rareDropMin = 0;
   int rareDropMax = 1;
   int optDrop1Min = 0;
   int optDrop1Max = 1;
   int optDrop2Min = 0;
   int optDrop2Max = 1;

   public PokemonDropInformation(PokemonSpec pokemon, JsonObject jsonObject) {
      this.pokemon = pokemon;
      String rareDropString;
      if (jsonObject.has("maindropdata")) {
         rareDropString = jsonObject.get("maindropdata").getAsString();
         this.mainDrop = DropItemRegistry.parseItem(rareDropString, "pokedrops.json");
         if (jsonObject.has("maindropmin")) {
            this.mainDropMin = jsonObject.get("maindropmin").getAsInt();
         }

         if (jsonObject.has("maindropmax")) {
            this.mainDropMax = jsonObject.get("maindropmax").getAsInt();
         }
      }

      if (jsonObject.has("optdrop1data")) {
         rareDropString = jsonObject.get("optdrop1data").getAsString();
         this.optDrop1 = DropItemRegistry.parseItem(rareDropString, "pokedrops.json");
         if (jsonObject.has("optdrop1min")) {
            this.optDrop1Min = jsonObject.get("optdrop1min").getAsInt();
         }

         if (jsonObject.has("optdrop1max")) {
            this.optDrop1Max = jsonObject.get("optdrop1max").getAsInt();
         }
      }

      if (jsonObject.has("optdrop2data")) {
         rareDropString = jsonObject.get("optdrop2data").getAsString();
         this.optDrop2 = DropItemRegistry.parseItem(rareDropString, "pokedrops.json");
         if (jsonObject.has("optdrop2min")) {
            this.optDrop2Min = jsonObject.get("optdrop2min").getAsInt();
         }

         if (jsonObject.has("optdrop2max")) {
            this.optDrop2Max = jsonObject.get("optdrop2max").getAsInt();
         }
      }

      if (jsonObject.has("raredropdata")) {
         rareDropString = jsonObject.get("raredropdata").getAsString();
         this.rareDrop = DropItemRegistry.parseItem(rareDropString, "pokedrops.json");
         if (jsonObject.has("raredropmin")) {
            this.rareDropMin = jsonObject.get("raredropmin").getAsInt();
         }

         if (jsonObject.has("raredropmax")) {
            this.rareDropMax = jsonObject.get("raredropmax").getAsInt();
         }
      }

   }

   public ArrayList getDrops(Entity8HoldsItems pixelmon) {
      ArrayList drops = new ArrayList();
      int numDrops;
      int i;
      if (this.mainDrop != null) {
         numDrops = RandomHelper.getRandomNumberBetween(this.mainDropMin, this.mainDropMax);

         for(i = 0; i < numDrops; ++i) {
            drops.add(this.mainDrop.func_77946_l());
         }
      }

      if (this.optDrop1 != null) {
         numDrops = RandomHelper.getRandomNumberBetween(this.optDrop1Min, this.optDrop1Max);

         for(i = 0; i < numDrops; ++i) {
            drops.add(this.optDrop1.func_77946_l());
         }
      }

      if (this.optDrop2 != null) {
         numDrops = RandomHelper.getRandomNumberBetween(this.optDrop2Min, this.optDrop2Max);

         for(i = 0; i < numDrops; ++i) {
            drops.add(this.optDrop2.func_77946_l());
         }
      }

      if (this.rareDrop != null && RandomHelper.getRandomChance(0.1F)) {
         numDrops = RandomHelper.getRandomNumberBetween(this.rareDropMin, this.rareDropMax);

         for(i = 0; i < numDrops; ++i) {
            drops.add(this.rareDrop.func_77946_l());
         }
      }

      return drops;
   }
}
