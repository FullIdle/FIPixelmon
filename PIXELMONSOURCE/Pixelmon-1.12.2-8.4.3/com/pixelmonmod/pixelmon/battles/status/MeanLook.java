package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MeanLook extends StatusBase {
   transient PixelmonWrapper locker;

   public MeanLook() {
      super(StatusType.MeanLook);
   }

   public MeanLook(PixelmonWrapper locker) {
      super(StatusType.MeanLook);
      this.locker = locker;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.addStatus(new MeanLook(user), user)) {
         user.bc.sendToAll("pixelmon.effect.noescape", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public boolean stopsSwitching() {
      return true;
   }

   public void onEndOfTurn(PixelmonWrapper user) {
      if (this.locker.isFainted()) {
         user.removeStatus((StatusBase)this);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.bc.isInBattle(this.locker)) {
         pw.removeStatus((StatusBase)this);
      }

   }
}
