package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Immunity extends PreventStatus {
   public Immunity() {
      super("pixelmon.abilities.immunity", "pixelmon.abilities.immunitycure", StatusType.Poison, StatusType.PoisonBadly);
   }
}
