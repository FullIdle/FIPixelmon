package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FirePledge extends PledgeBase {
   public FirePledge() {
      super(StatusType.FirePledge);
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      super.applyRepeatedEffect(pw);
      if (this.remainingTurns > 0 && !pw.bc.globalStatusController.hasStatus(StatusType.Rainy)) {
         pw.bc.sendToAll("pixelmon.status.firepledgeeffect", pw.getNickname());
         pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(12.5F), DamageTypeEnum.STATUS);
      }

   }
}
