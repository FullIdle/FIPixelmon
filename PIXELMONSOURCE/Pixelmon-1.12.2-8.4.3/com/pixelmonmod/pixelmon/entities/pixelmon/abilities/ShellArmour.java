package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class ShellArmour extends AbilityBase {
   public boolean preventsCriticalHits(PixelmonWrapper opponent) {
      return true;
   }
}
