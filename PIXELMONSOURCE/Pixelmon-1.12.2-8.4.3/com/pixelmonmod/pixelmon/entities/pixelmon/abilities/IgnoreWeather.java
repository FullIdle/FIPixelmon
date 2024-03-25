package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import java.util.Iterator;

public abstract class IgnoreWeather extends AbilityBase {
   private String switchInMessage;

   public IgnoreWeather(String switchInMessage) {
      this.switchInMessage = switchInMessage;
   }

   public boolean ignoreWeather() {
      return true;
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.bc.globalStatusController.canWeatherChange((Weather)null)) {
         newPokemon.bc.sendToAll(this.switchInMessage, newPokemon.getNickname());
         newPokemon.bc.globalStatusController.triggerWeatherChange((Weather)null);
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      Iterator var2 = oldPokemon.bc.getActiveUnfaintedPokemon().iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            if (oldPokemon.bc.globalStatusController.canWeatherChange(oldPokemon.bc.globalStatusController.getWeather())) {
               oldPokemon.bc.globalStatusController.triggerWeatherChange(oldPokemon.bc.globalStatusController.getWeather());
            }

            return;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(pw == oldPokemon || !pw.getBattleAbility().ignoreWeather());

   }
}
