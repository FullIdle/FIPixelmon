package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumCastform;

public class Forecast extends AbilityBase {
   public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
      if (pw.getSpecies() == EnumSpecies.Castform) {
         if (pw.bc.simulateMode) {
            return;
         }

         StatusType weatherType = weather == null ? StatusType.None : weather.type;
         EnumCastform form = EnumCastform.Normal;
         switch (weatherType) {
            case Rainy:
               form = EnumCastform.Rain;
               break;
            case Sunny:
               form = EnumCastform.Sun;
               break;
            case Hail:
               form = EnumCastform.Ice;
         }

         if (pw.getForm() != form.ordinal()) {
            pw.setForm(form);
            pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
         }
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.resetForm(newPokemon);
      this.onWeatherChange(newPokemon, newPokemon.bc.globalStatusController.getWeather());
   }

   public void applySwitchOutEffect(PixelmonWrapper pw) {
      this.resetForm(pw);
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
      this.onWeatherChange(pokemon, (Weather)null);
   }

   private void resetForm(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode) {
         if (pw.getForm() != EnumCastform.Normal.getForm()) {
            pw.setForm(EnumCastform.Normal);
         }

      }
   }
}
