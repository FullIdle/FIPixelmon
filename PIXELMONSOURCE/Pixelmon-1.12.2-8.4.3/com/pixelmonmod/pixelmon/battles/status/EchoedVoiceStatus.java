package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;

public class EchoedVoiceStatus extends GlobalStatusBase {
   public transient int power = 80;
   public transient int turnInc;

   public EchoedVoiceStatus() {
      super(StatusType.EchoedVoice);
   }

   public EchoedVoiceStatus(int turnInc) {
      super(StatusType.EchoedVoice);
      this.turnInc = turnInc;
   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      if (globalStatusController.bc.battleTurn != this.turnInc) {
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }
}
