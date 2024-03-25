package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class LockOn extends StatusBase {
   private transient PixelmonWrapper target;

   public LockOn() {
      super(StatusType.LockOn);
   }

   public LockOn(PixelmonWrapper target) {
      super(StatusType.LockOn);
      this.target = target;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.lockon", user.getNickname(), target.getNickname());
      user.addStatus(new LockOn(target), user);
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user != target) {
         if (target == this.target) {
            user.removeStatus((StatusBase)this);
            return new int[]{power, -2};
         }

         if (!user.targets.contains(this.target)) {
            user.removeStatus((StatusBase)this);
         }
      }

      return new int[]{power, accuracy};
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         MoveChoice maxAccuracyChoice = null;
         Iterator var8 = bestUserChoices.iterator();

         while(true) {
            MoveChoice choice;
            do {
               if (!var8.hasNext()) {
                  if (maxAccuracyChoice != null) {
                     int accuracy = maxAccuracyChoice.result.accuracy;
                     if (accuracy < 100 && accuracy > 0) {
                        userChoice.raiseWeight((float)(100 - maxAccuracyChoice.result.accuracy));
                        userChoice.raiseTier(maxAccuracyChoice.tier);
                     }
                  }

                  return;
               }

               choice = (MoveChoice)var8.next();
            } while(maxAccuracyChoice != null && maxAccuracyChoice.result.accuracy >= choice.result.accuracy);

            maxAccuracyChoice = choice;
         }
      }
   }
}
