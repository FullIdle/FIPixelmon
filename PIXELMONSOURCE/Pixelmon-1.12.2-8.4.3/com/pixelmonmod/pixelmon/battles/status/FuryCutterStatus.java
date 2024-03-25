package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FuryCutterStatus extends StatusBase {
   public transient int power = 40;
   public transient int turnInc;

   public FuryCutterStatus() {
      super(StatusType.FuryCutter);
   }

   public FuryCutterStatus(int turnInc) {
      super(StatusType.FuryCutter);
      this.turnInc = turnInc;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.turnInc != pw.bc.battleTurn) {
         pw.removeStatus((StatusBase)this);
      }

   }
}
