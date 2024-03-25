package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class ScorchingSands extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.removeStatus(StatusType.Freeze)) {
         user.bc.sendToAll("pixelmon.status.breakice", user.getNickname());
      }

      if (target.removeStatus(StatusType.Freeze)) {
         target.bc.sendToAll("pixelmon.status.breakice", target.getNickname());
      }

   }
}
