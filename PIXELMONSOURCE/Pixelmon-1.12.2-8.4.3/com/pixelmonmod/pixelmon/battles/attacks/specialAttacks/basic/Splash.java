package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Splash extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         user.bc.sendToAll("pixelmon.effect.splash");
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
