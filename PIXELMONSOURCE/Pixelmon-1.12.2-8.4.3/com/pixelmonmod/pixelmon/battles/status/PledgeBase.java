package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class PledgeBase extends StatusBase {
   transient int remainingTurns = 4;

   public PledgeBase(StatusType type) {
      super(type);
   }

   public boolean isTeamStatus() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.remainingTurns <= 0) {
         pw.bc.sendToAll("pixelmon.status." + this.type.toString().toLowerCase() + "end", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public StatusBase copy() {
      try {
         return (StatusBase)this.getClass().getConstructor().newInstance();
      } catch (Exception var2) {
         return this;
      }
   }
}
