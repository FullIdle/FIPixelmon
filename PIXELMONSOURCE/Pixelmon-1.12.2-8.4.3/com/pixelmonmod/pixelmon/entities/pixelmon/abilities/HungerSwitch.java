package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.forms.EnumMorpeko;

public class HungerSwitch extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.getFormEnum() == EnumMorpeko.FULLBELLY) {
         pokemon.setForm(EnumMorpeko.HANGRY);
         pokemon.bc.sendToAll("pixelmon.abilities.hungerswitch.hangry", pokemon.getNickname());
      } else if (pokemon.getFormEnum() == EnumMorpeko.HANGRY) {
         pokemon.setForm(EnumMorpeko.FULLBELLY);
         pokemon.bc.sendToAll("pixelmon.abilities.hungerswitch.full", pokemon.getNickname());
      }

   }
}
