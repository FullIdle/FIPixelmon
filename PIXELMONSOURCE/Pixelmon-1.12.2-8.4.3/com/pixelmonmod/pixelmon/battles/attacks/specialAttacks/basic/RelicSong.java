package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class RelicSong extends SpecialAttackBase {
   public void applyAfterEffect(PixelmonWrapper pw) {
      if (pw.getSpecies() == EnumSpecies.Meloetta) {
         pw.setForm(1 - pw.getForm());
         pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
      }

   }
}
