package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class FlashFire extends AbilityBase {
   boolean activated = false;

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() == EnumType.Fire) {
         if (!this.activated) {
            pokemon.bc.sendToAll("pixelmon.abilities.flashfire", pokemon.getNickname());
            MoveResults var10000 = a.moveResult;
            var10000.weightMod -= 25.0F;
            this.activated = true;
         } else {
            pokemon.bc.sendToAll("pixelmon.abilities.flashfire2", pokemon.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return this.activated && a.getType() == EnumType.Fire ? new int[]{(int)((double)power * 1.5), accuracy} : new int[]{power, accuracy};
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.activated = false;
   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return 1.0F;
      } else {
         SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
         if (spawnInfoPokemon.getPokemonSpec() != null && spawnInfoPokemon.getPokemonSpec().name != null) {
            EnumSpecies species = spawnInfoPokemon.getSpecies();
            int form = spawnInfoPokemon.getPokemonSpec().form == null ? -1 : spawnInfoPokemon.getPokemonSpec().form;
            return species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(EnumType.Fire) ? 5.0F : 1.0F;
         } else {
            return 1.0F;
         }
      }
   }
}
