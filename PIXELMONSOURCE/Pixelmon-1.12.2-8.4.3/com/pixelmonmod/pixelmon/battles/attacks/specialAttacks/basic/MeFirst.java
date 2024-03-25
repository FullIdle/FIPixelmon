package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MeFirstStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class MeFirst extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.ignore;
      } else {
         Iterator var3 = user.bc.turnList.iterator();

         while(var3.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var3.next();
            if (pw == user) {
               break;
            }

            if (pw == target) {
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               return AttackResult.failed;
            }
         }

         if (target.attack != null && target.attack.getAttackCategory() != AttackCategory.STATUS && !target.attack.isAttack("Focus Punch", "Beak Blast", "Belch", "Counter", "Covet", "Metal Punch", "Mirror Coat", "Shell Trap", "Struggle", "Chatter", "Thief") && !target.attack.isZ) {
            user.addStatus(new MeFirstStatus(), user);
            user.useTempAttack(target.attack);
            return AttackResult.ignore;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList())) {
         ArrayList possibleChoices = MoveChoice.createChoicesFromChoices(pw, bestOpponentChoices, false);
         pw.bc.simulateMode = false;
         pw.addStatus(new MeFirstStatus(), pw);
         pw.bc.simulateMode = true;
         pw.getBattleAI().weightRandomMove(pw, userChoice, possibleChoices);
         pw.bc.simulateMode = false;
         pw.removeStatus(StatusType.MeFirst);
         pw.bc.simulateMode = true;
      }
   }
}
