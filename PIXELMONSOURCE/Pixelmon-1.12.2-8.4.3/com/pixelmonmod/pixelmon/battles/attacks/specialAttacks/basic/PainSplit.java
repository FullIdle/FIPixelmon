package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class PainSplit extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      float newHPBase = (float)(user.getHealth(true) + target.getHealth(true)) / 2.0F;
      if (user.getHealth(true) != target.getHealth(true)) {
         user.bc.sendToAll("pixelmon.effect.painsplit");
         if ((float)user.getHealth(true) < newHPBase) {
            user.healEntityBy((int)(newHPBase - (float)user.getHealth(true)));
         } else if ((float)user.getHealth(true) > newHPBase) {
            user.doBattleDamage(user, (float)user.getHealth(true) - newHPBase, DamageTypeEnum.SELF);
         }

         if ((float)target.getHealth(true) < newHPBase) {
            target.healEntityBy((int)(newHPBase - (float)target.getHealth(true)));
            return AttackResult.succeeded;
         }

         if ((float)target.getHealth(true) > newHPBase) {
            target.doBattleDamage(user, (float)target.getHealth(true) - newHPBase, DamageTypeEnum.SELF);
            return AttackResult.hit;
         }
      }

      return AttackResult.failed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            float newHPBase = (float)(pw.getHealth(true) + target.getHealth(true)) / 2.0F;
            if (newHPBase < (float)pw.getHealth(true)) {
               return;
            }

            userChoice.raiseWeight(pw.getHealthPercent(Math.min(newHPBase - (float)pw.getHealth(true), (float)pw.getHealthDeficit())));
            userChoice.raiseWeight(target.getHealthPercent((float)target.getHealth(true) - newHPBase));
         }

      }
   }
}
