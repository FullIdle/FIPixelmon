package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class WaterVeil extends PreventStatus {
   public WaterVeil() {
      super("pixelmon.abilities.waterveil", "pixelmon.abilities.waterveilcure", StatusType.Burn);
   }
}
