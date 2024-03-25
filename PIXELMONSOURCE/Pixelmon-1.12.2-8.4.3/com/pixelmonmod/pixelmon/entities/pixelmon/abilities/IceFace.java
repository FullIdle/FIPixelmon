package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.forms.EnumEiscue;

public class IceFace extends AbilityBase {
   boolean busted = false;

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.getSpecies() != EnumSpecies.Eiscue) {
         return damage;
      } else if (target.getFormEnum() == EnumEiscue.ICE_FACE && a.getAttackCategory() == AttackCategory.PHYSICAL && !this.busted) {
         target.bc.sendToAll("pixelmon.abilities.iceface.protected", target.getNickname());
         if (!target.bc.simulateMode) {
            this.busted = true;
         }

         return 0;
      } else {
         return damage;
      }
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.getSpecies() == EnumSpecies.Eiscue) {
         if (newPokemon.getFormEnum() == EnumEiscue.NOICE_FACE && newPokemon.bc.globalStatusController.getWeather() instanceof Hail && !newPokemon.bc.simulateMode) {
            newPokemon.bc.sendToAll("pixelmon.abilities.iceface.restore", newPokemon.getNickname());
            newPokemon.setForm(EnumEiscue.ICE_FACE);
            this.busted = false;
         }

      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.getSpecies() == EnumSpecies.Eiscue) {
         if (this.busted && pokemon.getFormEnum() == EnumEiscue.ICE_FACE) {
            pokemon.setForm(EnumEiscue.NOICE_FACE);
         }

      }
   }

   public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
      if (pw.getSpecies() == EnumSpecies.Eiscue) {
         if (pw.getFormEnum() == EnumEiscue.NOICE_FACE && weather instanceof Hail && !pw.bc.simulateMode) {
            pw.bc.sendToAll("pixelmon.abilities.iceface.restore", pw.getNickname());
            pw.setForm(EnumEiscue.ICE_FACE);
            this.busted = false;
         }

      }
   }

   public boolean needNewInstance() {
      return true;
   }
}
