package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Submerged;

public class Dive extends MultiTurnCharge {
   public Dive() {
      super("pixelmon.effect.dive", Submerged.class.getSimpleName(), StatusType.Submerged);
   }
}
