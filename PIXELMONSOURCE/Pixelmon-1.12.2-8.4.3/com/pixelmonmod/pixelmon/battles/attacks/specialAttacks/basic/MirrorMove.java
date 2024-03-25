package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class MirrorMove extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.lastAttack != null && !target.lastAttack.isAttack("Mirror Move", "Sketch")) {
         if (user.attack.isZ) {
            user.zMove = null;
         }

         user.useTempAttack(target.lastAttack);
         return AttackResult.ignore;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         bestOpponentChoices = MoveChoice.getAffectedChoices(userChoice, bestOpponentChoices);

         ArrayList possibleChoices;
         for(Iterator var7 = userChoice.targets.iterator(); var7.hasNext(); pw.getBattleAI().weightRandomMove(pw, userChoice, possibleChoices)) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList())) {
               possibleChoices = MoveChoice.createChoicesFromChoices(pw, bestOpponentChoices, false);
            } else {
               if (target.lastAttack == null) {
                  return;
               }

               possibleChoices = target.lastAttack.createMoveChoices(pw, false);
            }
         }

      }
   }
}
