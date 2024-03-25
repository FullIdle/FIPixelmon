package com.pixelmonmod.pixelmon.client.gui.battles.tasks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import java.util.TimerTask;
import java.util.UUID;

public class SwitchTask extends TimerTask {
   int ticks = 0;
   UUID pix1UUID;
   PixelmonInGui pokemon;
   PixelmonInGui newPokemon;
   String oldName;
   boolean canceled = false;

   public SwitchTask(UUID pokemon, PixelmonInGui newPokemon) {
      this.pix1UUID = pokemon;
      this.newPokemon = newPokemon;
   }

   public void run() {
      if (this.newPokemon == null) {
         if (this.ticks < 120) {
            this.loadPokemon(this.pix1UUID);
            if (this.pokemon != null) {
               --this.pokemon.xPos;
            } else {
               Pixelmon.LOGGER.debug("SwitchTask null pokemon #1");
            }
         } else {
            this.cancel();
         }
      } else {
         if (this.ticks < 120 && this.pix1UUID == null) {
            this.ticks = 120;
         }

         if (this.ticks < 120) {
            this.loadPokemon(this.pix1UUID);
            if (this.pokemon != null) {
               --this.pokemon.xPos;
               if (this.oldName == null) {
                  this.oldName = this.pokemon.nickname;
               }

               this.pokemon.isSwitchingOut = true;
               this.newPokemon.isSwitchingIn = true;
            } else {
               Pixelmon.LOGGER.debug("SwitchTask null pokemon #2 - " + this.newPokemon.toString());
            }
         } else if (this.ticks == 120) {
            if (this.pokemon != null) {
               PixelmonInGui oldPokemon = this.pokemon;
               this.loadPokemon(this.newPokemon.pokemonUUID);
               PixelmonInGui[] array = ClientProxy.battleManager.displayedOurPokemon;
               int i;
               if (ClientProxy.battleManager.isEnemyPokemon(oldPokemon)) {
                  array = ClientProxy.battleManager.displayedEnemyPokemon;
               } else if (ClientProxy.battleManager.displayedAllyPokemon != null) {
                  for(i = 0; i < ClientProxy.battleManager.displayedAllyPokemon.length; ++i) {
                     if (ClientProxy.battleManager.displayedAllyPokemon[i].pokemonUUID.equals(this.pix1UUID)) {
                        ClientProxy.battleManager.displayedAllyPokemon[i] = this.pokemon;
                     }
                  }
               }

               for(i = 0; i < array.length; ++i) {
                  if (array[i].pokemonUUID.equals(this.pix1UUID)) {
                     array[i] = this.pokemon;
                  }
               }

               this.pokemon.status = this.newPokemon.status;
               this.pokemon.health = this.newPokemon.health;
               this.pokemon.maxHealth = this.newPokemon.maxHealth;
               this.pokemon.level = this.newPokemon.level;
               this.pokemon.gender = this.newPokemon.gender;
               if (this.pokemon.nickname.equals(this.oldName)) {
                  this.pokemon.nickname = this.newPokemon.nickname;
               }

               this.pokemon.species = this.newPokemon.species;
               this.pokemon.shiny = this.newPokemon.shiny;
               this.pokemon.gmaxFactor = this.newPokemon.gmaxFactor;
               this.pokemon.expFraction = this.newPokemon.expFraction;
               this.pokemon.form = this.newPokemon.form;
               if (ClientProxy.battleManager.isEnemyPokemon(this.pokemon)) {
                  this.pokemon.xPos = -120;
               } else {
                  this.pokemon.xPos = 0;
               }
            } else {
               Pixelmon.LOGGER.debug("SwitchTask null pokemon #3 - " + this.newPokemon.toString());
            }
         } else if (this.ticks > 120 && this.ticks < 241) {
            this.loadPokemon(this.newPokemon.pokemonUUID);
            if (this.pokemon != null) {
               ++this.pokemon.xPos;
               this.pokemon.isSwitchingOut = false;
               this.newPokemon.isSwitchingIn = false;
            } else {
               Pixelmon.LOGGER.debug("SwitchTask null pokemon #4 - " + this.newPokemon.toString());
            }
         } else if (this.ticks >= 241) {
            this.cancel();
         }
      }

      ++this.ticks;
   }

   private void loadPokemon(UUID pokemonUUID) {
      if (this.pokemon == null || !this.pokemon.pokemonUUID.equals(pokemonUUID)) {
         this.pokemon = ClientProxy.battleManager.getPokemon(pokemonUUID);
         if (this.pokemon == null) {
            this.pokemon = this.newPokemon;
         }
      }

   }

   public boolean cancel() {
      this.canceled = true;
      return super.cancel();
   }

   public boolean isDone() {
      return this.canceled;
   }
}
