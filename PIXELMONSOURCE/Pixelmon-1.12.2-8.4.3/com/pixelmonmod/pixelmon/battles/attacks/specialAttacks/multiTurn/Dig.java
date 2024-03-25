package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.UnderGround;

public class Dig extends MultiTurnCharge {
   public Dig() {
      super("pixelmon.effect.dighole", UnderGround.class.getSimpleName(), StatusType.UnderGround);
   }
}
