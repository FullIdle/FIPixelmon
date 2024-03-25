package com.pixelmonmod.pixelmon.client.gui.battles.tasks;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import java.util.Objects;
import java.util.TimerTask;
import java.util.UUID;

public abstract class HPTask extends TimerTask {
   float healthDifference;
   float originalHealth;
   float currentHealth;
   float interval;
   UUID uuid;
   PixelmonInGui pokemon;
   boolean canceled = false;

   public HPTask(float healthDifference, UUID uuid) {
      this.healthDifference = healthDifference;
      this.uuid = uuid;
      this.pokemon = ClientProxy.battleManager.getPokemon(uuid);
      if (this.pokemon != null) {
         this.originalHealth = this.pokemon.health;
         this.currentHealth = this.originalHealth;
         this.boundsCheck();
      }

      this.interval = this.healthDifference / 100.0F;
   }

   protected abstract void boundsCheck();

   public void run() {
      if (this.pokemon == null) {
         this.cancel();
      } else {
         this.currentHealth += this.interval;
         if (this.isDone()) {
            this.pokemon.health = Math.max(0.0F, this.originalHealth + this.healthDifference);
            if (ClientProxy.battleManager.fullOurPokemon != null) {
               ClientProxy.battleManager.fullOurPokemon.stream().filter((pig) -> {
                  return Objects.equals(pig.pokemonUUID, this.pokemon.pokemonUUID);
               }).forEach((pig) -> {
                  pig.health = this.pokemon.health;
               });
            }

            this.cancel();
         } else {
            this.pokemon.health = this.currentHealth;
         }
      }
   }

   public boolean cancel() {
      this.canceled = true;
      return super.cancel();
   }

   public abstract boolean isDone();
}
