package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MiniorStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMinior;

public class ShieldsDown extends AbilityBase {
   public boolean allowsStatus(StatusType status, PixelmonWrapper pw, PixelmonWrapper user) {
      if (status != StatusType.StealthRock && status != StatusType.Spikes && status != StatusType.ToxicSpikes && status != StatusType.StickyWeb && status != StatusType.Steelsurge && status != StatusType.Substitute) {
         return pw.getSpecies() != EnumSpecies.Minior || pw.getForm() != EnumMinior.METEOR.ordinal();
      } else {
         return true;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.tryFormChange(pw);
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.tryFormChange(newPokemon);
   }

   private void tryFormChange(PixelmonWrapper pw) {
      if (pw.getSpecies() == EnumSpecies.Minior) {
         if (pw.getForm() != EnumMinior.METEOR.ordinal()) {
            if (pw.getHealthPercent() > 50.0F) {
               pw.setForm(EnumMinior.METEOR);
               pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
            }
         } else if (pw.getForm() == EnumMinior.METEOR.ordinal() && pw.getHealthPercent() < 50.0F && pw.getInnerLink().getExtraStats(MiniorStats.class) != null) {
            MiniorStats ms = (MiniorStats)pw.getInnerLink().getExtraStats(MiniorStats.class);
            pw.setForm(ms.color + 1);
            pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
         }
      }

   }
}
