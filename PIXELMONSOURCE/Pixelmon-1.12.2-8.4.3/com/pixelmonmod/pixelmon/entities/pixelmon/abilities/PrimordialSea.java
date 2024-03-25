package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import java.util.Iterator;

public class PrimordialSea extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.bc.globalStatusController.canWeatherChange(new Rainy(true))) {
         newPokemon.bc.globalStatusController.addGlobalStatus(new Rainy(true));
         newPokemon.bc.sendToAll("pixelmon.abilities.primordialsea", newPokemon.getNickname());
      }

   }

   private void checkForRemoval(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Rainy && pokemon.bc.globalStatusController.getWeatherIgnoreAbility().extreme) {
         Iterator var2 = pokemon.bc.getActivePokemon().iterator();

         while(var2.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var2.next();
            if (pw != pokemon && pw.getBattleAbility() instanceof PrimordialSea) {
               return;
            }
         }

         pokemon.bc.globalStatusController.removeGlobalStatus(StatusType.Rainy);
         pokemon.bc.globalStatusController.triggerWeatherChange((Weather)null);
         pokemon.bc.sendToAll("pixelmon.status.rainstopped");
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      this.checkForRemoval(oldPokemon);
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
      this.checkForRemoval(pokemon);
   }

   public void onDamageReceived(PixelmonWrapper user, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damagetype) {
      if (pokemon.isFainted()) {
         this.checkForRemoval(pokemon);
      }

   }

   public boolean canBeIgnored() {
      return false;
   }
}
