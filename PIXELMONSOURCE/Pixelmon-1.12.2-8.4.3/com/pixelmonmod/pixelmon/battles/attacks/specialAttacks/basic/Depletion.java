package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;
import java.util.Iterator;

public class Depletion extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.proceed;
      } else {
         if (target.lastAttack != null && target.lastAttack.pp > 0) {
            user.bc.sendToAll("pixelmon.effect.ppdrop", target.getNickname(), target.lastAttack.getMove().getTranslatedName());
            Moveset targetMoveset = target.getMoveset();
            Iterator var4 = targetMoveset.iterator();

            while(var4.hasNext()) {
               Attack attack = (Attack)var4.next();
               if (attack.isAttack(target.lastAttack.toString())) {
                  attack.pp = Math.max(attack.pp - 2, 0);
                  break;
               }
            }
         }

         return AttackResult.proceed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(true) {
            PixelmonWrapper target;
            do {
               if (!var7.hasNext()) {
                  return;
               }

               target = (PixelmonWrapper)var7.next();
            } while(target.lastAttack == null && pw.bc.getFirstMover(pw, target) != target);

            userChoice.raiseWeight(15.0F);
         }
      }
   }
}
