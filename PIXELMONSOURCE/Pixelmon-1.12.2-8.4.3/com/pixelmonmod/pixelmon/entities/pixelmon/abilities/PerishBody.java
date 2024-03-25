package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Perish;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class PerishBody extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.Perish)) {
         user.addStatus(new Perish(), user);
         target.addStatus(new Perish(), target);
         user.bc.sendToAll("applyperish.body", target.getNickname(), user.getNickname());
      }

   }
}
