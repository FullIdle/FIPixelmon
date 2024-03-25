package com.pixelmonmod.pixelmon.battles.status;

public class MultiTurn extends StatusBase {
   public transient int numTurns;

   public MultiTurn() {
      super(StatusType.MultiTurn);
   }
}
