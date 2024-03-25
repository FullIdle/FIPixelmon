package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Electrify extends StatusBase {
   public Electrify() {
      super(StatusType.Electrify);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasMoved() && target.addStatus(new Electrify(), user)) {
         user.bc.sendToAll("pixelmon.status.electrify", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!a.isAttack("Struggle")) {
         a.overrideType(EnumType.Electric);
      }

      return new int[]{power, accuracy};
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }
}
