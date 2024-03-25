package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.BeakBlastStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class BeakBlast extends SpecialAttackBase {
   public void applyEarlyEffect(PixelmonWrapper user) {
      user.addStatus(new BeakBlastStatus(), user);
      user.bc.sendToAll("pixelmon.effect.beakblast.start", user.getNickname());
   }

   public void applyAfterEffect(PixelmonWrapper user) {
      user.removeStatus(StatusType.BeakBlast);
   }
}
