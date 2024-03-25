package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Autotomize extends StatusBase {
   public Autotomize() {
      super(StatusType.Autotomize);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.addStatus(new Autotomize(), user)) {
         user.bc.sendToAll("pixelmon.status.autotomize", user.getNickname());
      }

   }

   public float modifyWeight(float initWeight) {
      return Math.max(0.0F, initWeight - 100.0F);
   }
}
