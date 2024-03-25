package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;

public class Charge extends StatusBase {
   transient int duration = 2;

   public Charge() {
      super(StatusType.Charge);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.charge", user.getNickname());
      Charge charge = (Charge)user.getStatus(StatusType.Charge);
      if (charge == null) {
         user.addStatus(new Charge(), user);
      } else {
         charge.duration = 2;
      }

   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Electric) {
         power *= 2;
      }

      return new int[]{power, accuracy};
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.duration <= 0) {
         pw.removeStatus((StatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!pw.hasStatus(StatusType.Charge) && MoveChoice.hasOffensiveAttackType(bestUserChoices, EnumType.Electric)) {
         userChoice.raiseWeight(50.0F);
      }

   }
}
