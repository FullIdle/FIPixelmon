package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumWishiwashi;

public class Schooling extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.tryFormChange(pw);
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.tryFormChange(newPokemon);
   }

   private void tryFormChange(PixelmonWrapper pw) {
      if (pw.getSpecies() == EnumSpecies.Wishiwashi && pw.getLevelNum() >= 20) {
         if (!(pw.pokemon.getFormEnum() instanceof EnumWishiwashi)) {
            return;
         }

         if (pw.getForm() == EnumWishiwashi.SCHOOL.ordinal()) {
            if (pw.getHealthPercent() < 25.0F) {
               pw.setForm(EnumWishiwashi.SOLO);
               pw.bc.sendToAll("pixelmon.abilities.schooling.stop", pw.getNickname());
            }
         } else if (pw.getForm() == EnumWishiwashi.SOLO.ordinal() && pw.getHealthPercent() > 25.0F) {
            pw.setForm(EnumWishiwashi.SCHOOL);
            pw.bc.sendToAll("pixelmon.abilities.schooling.start", pw.getNickname());
         }
      }

   }
}
