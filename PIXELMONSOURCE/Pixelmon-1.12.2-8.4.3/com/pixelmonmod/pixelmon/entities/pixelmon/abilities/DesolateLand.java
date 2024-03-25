package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import java.util.Iterator;

public class DesolateLand extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.bc.globalStatusController.canWeatherChange(new Sunny(true))) {
         newPokemon.bc.globalStatusController.addGlobalStatus(new Sunny(true));
         newPokemon.bc.sendToAll("pixelmon.abilities.desolateland", newPokemon.getNickname());
      }

   }

   private void checkForRemoval(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sunny && pokemon.bc.globalStatusController.getWeatherIgnoreAbility().extreme) {
         Iterator var2 = pokemon.bc.getActivePokemon().iterator();

         while(var2.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var2.next();
            if (pw != pokemon && pw.getBattleAbility() instanceof DesolateLand) {
               return;
            }
         }

         pokemon.bc.globalStatusController.removeGlobalStatus(StatusType.Sunny);
         pokemon.bc.globalStatusController.triggerWeatherChange((Weather)null);
         pokemon.bc.sendToAll("pixelmon.status.sunlightfaded");
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
