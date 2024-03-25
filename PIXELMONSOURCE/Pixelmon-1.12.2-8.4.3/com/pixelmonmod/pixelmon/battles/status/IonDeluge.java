package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class IonDeluge extends GlobalStatusBase {
   public IonDeluge() {
      super(StatusType.IonDeluge);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.status.iondeluge");
         user.bc.globalStatusController.addGlobalStatus(new IonDeluge());
      }

   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Normal && !a.isAttack("Struggle")) {
         a.overrideType(EnumType.Electric);
      }

      return new int[]{power, accuracy};
   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
   }
}
