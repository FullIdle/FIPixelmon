package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class SkyDropped extends SkyDropBase {
   private transient PixelmonWrapper dropper;

   public SkyDropped(PixelmonWrapper user) {
      super(StatusType.SkyDropped);
      this.dropper = user;
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (this.dropper.isAlive() && this.dropper.hasStatus(StatusType.SkyDropping)) {
         user.bc.setAllReady();
         return false;
      } else {
         user.removeStatus((StatusBase)this);
         return true;
      }
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user == this.dropper ? false : super.stopsIncomingAttack(pokemon, user);
   }
}
