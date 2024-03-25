package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;

public class Regenerator extends AbilityBase {
   public void applySwitchOutEffect(PixelmonWrapper pw) {
      if (!pw.isFainted()) {
         pw.animateHP = false;
         pw.healByPercent(33.333332F);
         pw.update(EnumUpdateType.HP);
         pw.animateHP = true;
      }
   }
}
