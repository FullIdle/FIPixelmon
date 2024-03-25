package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Quash extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.turnList.indexOf(target) > user.bc.turn) {
         user.bc.sendToAll("pixelmon.effect.quash", user.getNickname(), target.getNickname());
         if (!user.bc.simulateMode) {
            user.bc.turnList.remove(target);
            user.bc.turnList.add(target);
         }

         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
