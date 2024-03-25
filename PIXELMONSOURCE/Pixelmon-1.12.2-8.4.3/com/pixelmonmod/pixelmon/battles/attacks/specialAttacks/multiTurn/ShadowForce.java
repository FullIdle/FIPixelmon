package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Vanish;

public class ShadowForce extends MultiTurnCharge {
   public ShadowForce() {
      super("pixelmon.effect.shadowforce", Vanish.class.getSimpleName(), StatusType.Vanish);
   }
}
