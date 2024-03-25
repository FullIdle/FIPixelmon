package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class SuckerPunch extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.proceed;
      } else {
         if (target.attack != null && target.attack.getAttackCategory() != AttackCategory.STATUS) {
            for(int i = user.bc.turn + 1; i < user.bc.turnList.size(); ++i) {
               if (user.bc.turnList.get(i) == target) {
                  return AttackResult.proceed;
               }
            }
         }

         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int numOffensive = 0;
      int numStatus = 0;
      Iterator var9 = userChoice.targets.iterator();

      while(var9.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var9.next();
         Iterator var11 = pw.getBattleAI().getMoveset(target).iterator();

         while(var11.hasNext()) {
            Attack attack = (Attack)var11.next();
            if (attack.getAttackCategory() == AttackCategory.STATUS) {
               ++numStatus;
            } else {
               ++numOffensive;
            }
         }
      }

      if (numStatus != 0) {
         if (RandomHelper.getRandomChance((float)numStatus / ((float)numOffensive + (float)numStatus))) {
            userChoice.lowerTier(1);
         }

      }
   }
}
