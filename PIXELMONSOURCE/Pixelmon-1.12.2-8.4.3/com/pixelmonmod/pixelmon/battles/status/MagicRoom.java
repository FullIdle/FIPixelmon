package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MagicRoom extends GlobalStatusBase {
   private transient int duration = 5;

   public MagicRoom() {
      super(StatusType.MagicRoom);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.removeGlobalStatus(StatusType.MagicRoom)) {
         user.bc.sendToAll("pixelmon.status.magicroomend");
      } else {
         user.bc.sendToAll("pixelmon.status.magicroom", user.getNickname());
         user.bc.globalStatusController.addGlobalStatus(new MagicRoom());
      }

   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      if (--this.duration <= 0) {
         globalStatusController.bc.sendToAll("pixelmon.status.magicroomend");
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }
}
