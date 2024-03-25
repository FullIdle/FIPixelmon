package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class AfterYou extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.turn < user.bc.turnList.size() - 2) {
         for(int i = user.bc.turn + 2; i < user.bc.turnList.size(); ++i) {
            if (user.bc.turnList.get(i) == target) {
               user.bc.sendToAll("pixelmon.effect.afteryou", user.getNickname(), target.getNickname());
               user.bc.turnList.add(user.bc.turn + 1, user.bc.turnList.remove(i));
               return AttackResult.succeeded;
            }
         }
      }

      user.bc.sendToAll("pixelmon.effect.effectfailed");
      return AttackResult.failed;
   }
}
