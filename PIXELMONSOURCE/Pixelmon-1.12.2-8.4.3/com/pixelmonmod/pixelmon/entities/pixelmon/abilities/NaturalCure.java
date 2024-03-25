package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class NaturalCure extends AbilityBase {
   public void applySwitchOutEffect(PixelmonWrapper pw) {
      pw.clearStatus();
   }

   public void applyEndOfBattleEffect(PixelmonWrapper p) {
      p.clearStatus();
   }
}
