package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MysteriousAirCurrent;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import java.util.Iterator;

public class DeltaStream extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof MysteriousAirCurrent)) {
         newPokemon.bc.globalStatusController.addGlobalStatus(new MysteriousAirCurrent());
         newPokemon.bc.sendToAll("pixelmon.effect.mysteriousaircurrent", newPokemon.getNickname());
      }

   }

   private void checkForRemoval(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof MysteriousAirCurrent) {
         Iterator var2 = pokemon.bc.getActivePokemon().iterator();

         while(var2.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var2.next();
            if (pw != pokemon && pw.getBattleAbility() instanceof DeltaStream) {
               return;
            }
         }

         pokemon.bc.globalStatusController.removeGlobalStatus(StatusType.MysteriousAirCurrent);
         pokemon.bc.globalStatusController.triggerWeatherChange((Weather)null);
         pokemon.bc.sendToAll("pixelmon.effect.mysteriousaircurrentfaded");
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
