package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class Harvest extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      ItemHeld consumedItem = pokemon.getConsumedItem();
      if (consumedItem.isBerry() && !pokemon.hasHeldItem() && (pokemon.bc.globalStatusController.getWeather() instanceof Sunny || RandomHelper.getRandomChance(0.5F))) {
         pokemon.setHeldItem(consumedItem);
         pokemon.setConsumedItem((ItemHeld)null);
         pokemon.bc.sendToAll("pixelmon.abilities.harvest", pokemon.getNickname(), consumedItem.getLocalizedName());
      }

   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return 1.0F;
      } else {
         SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
         if (spawnInfoPokemon.getPokemonSpec() != null && spawnInfoPokemon.getPokemonSpec().name != null) {
            EnumSpecies species = spawnInfoPokemon.getSpecies();
            int form = spawnInfoPokemon.getPokemonSpec().form == null ? -1 : spawnInfoPokemon.getPokemonSpec().form;
            return species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(EnumType.Grass) ? 5.0F : 1.0F;
         } else {
            return 1.0F;
         }
      }
   }
}
