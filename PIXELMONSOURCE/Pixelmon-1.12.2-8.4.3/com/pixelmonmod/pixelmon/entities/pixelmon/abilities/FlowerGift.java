package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumCherrim;

public class FlowerGift extends AbilityBase {
   public int[] modifyStatsTeammate(PixelmonWrapper pokemon, int[] stats) {
      if (pokemon.bc.globalStatusController.hasStatus(StatusType.Sunny)) {
         int var10001 = StatsType.Attack.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }

   public int[] modifyStatsCancellableTeammate(PixelmonWrapper pokemon, int[] stats) {
      if (pokemon.bc.globalStatusController.hasStatus(StatusType.Sunny)) {
         int var10001 = StatsType.SpecialDefence.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }

   public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
      if (pw.getSpecies() == EnumSpecies.Cherrim && !pw.bc.simulateMode) {
         StatusType weatherType = weather == null ? StatusType.None : weather.type;
         if (weatherType == StatusType.Sunny) {
            if (pw.getForm() == EnumCherrim.OVERCAST.getForm()) {
               pw.setForm(EnumCherrim.SUNSHINE);
               pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
            }
         } else if (pw.getForm() == EnumCherrim.SUNSHINE.getForm()) {
            pw.setForm(EnumCherrim.OVERCAST);
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
         if (pw.getForm() != EnumCherrim.OVERCAST.getForm()) {
            pw.setForm(EnumCherrim.OVERCAST);
         }

      }
   }
}
