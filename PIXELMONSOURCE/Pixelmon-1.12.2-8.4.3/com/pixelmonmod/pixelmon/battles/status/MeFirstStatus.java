package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MeFirstStatus extends StatusBase {
   public MeFirstStatus() {
      super(StatusType.MeFirst);
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      user.removeStatus((StatusBase)this);
      return new int[]{(int)((double)power * 1.5), accuracy};
   }
}
