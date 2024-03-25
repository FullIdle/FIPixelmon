package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class ProtectVariationTeam extends ProtectVariation {
   public ProtectVariationTeam(StatusType type) {
      super(type);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addTeamStatus(this.getNewInstance(), user);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         super.applyEffect(user, target);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeTeamStatus((StatusBase)this);
   }

   protected boolean canFail() {
      return false;
   }

   public abstract ProtectVariationTeam getNewInstance();
}
