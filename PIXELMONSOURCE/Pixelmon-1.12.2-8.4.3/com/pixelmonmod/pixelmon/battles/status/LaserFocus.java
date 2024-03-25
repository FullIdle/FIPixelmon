package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class LaserFocus extends StatusBase {
   public LaserFocus() {
      super(StatusType.LaserFocus);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.getBattleStats().increaseCritStage(3, false)) {
         user.bc.sendToAll("pixelmon.battletext.movefailed");
      } else {
         user.bc.sendToAll("pixelmon.effect.laserfocus", user.getNickname());
         user.addStatus(new LaserFocus(), user);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper user) {
      if (user.hasStatus(StatusType.LaserFocus) && !user.attack.isAttack("Laser Focus")) {
         user.getBattleStats().decreaseCritStage(3);
         user.removeStatus(StatusType.LaserFocus);
      }

   }
}
