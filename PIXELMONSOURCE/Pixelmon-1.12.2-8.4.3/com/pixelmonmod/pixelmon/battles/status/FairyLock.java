package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FairyLock extends GlobalStatusBase {
   private transient int duration = 2;

   public FairyLock() {
      super(StatusType.FairyLock);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.bc.globalStatusController.addGlobalStatus(new FairyLock());
         user.bc.sendToAll("pixelmon.status.fairylock");
      }

   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      if (--this.duration <= 0) {
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }

   public boolean stopsSwitching() {
      return true;
   }
}
