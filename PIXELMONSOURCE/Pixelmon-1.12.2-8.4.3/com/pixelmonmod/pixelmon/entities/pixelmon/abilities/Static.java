package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Static extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (RandomHelper.getRandomChance(30) && !user.hasStatus(StatusType.Paralysis) && Paralysis.paralyze(target, user, (Attack)null, false)) {
         target.bc.sendToAll("pixelmon.abilities.static", target.getNickname(), user.getNickname());
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
            return species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(EnumType.Electric) ? 5.0F : 1.0F;
         } else {
            return 1.0F;
         }
      }
   }
}
