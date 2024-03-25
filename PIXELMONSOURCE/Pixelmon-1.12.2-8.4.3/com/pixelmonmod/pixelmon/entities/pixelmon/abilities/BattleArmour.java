package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class BattleArmour extends AbilityBase {
   public boolean preventsCriticalHits(PixelmonWrapper opponent) {
      return true;
   }
}
