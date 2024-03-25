package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Instruct extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.lastAttack != null && !target.isDynamax() && target.getMoveset().hasAttack(target.lastAttack) && target.getMoveset().get(target.getMoveset().indexOf(target.lastAttack)).pp > 0 && !target.lastAttack.isAttack("Assist", "Beak Blast", "Bide", "Celebrate", "Copycat", "Dynamax Cannon", "Focus Punch", "Ice Ball", "Instruct", "King's Shield", "Me First", "Metronome", "Mimic", "Mirror Move", "Nature Power", "Outrage", "Petal Dance", "Rollout", "Shell Trap", "Sketch", "Sleep Talk", "Thrash", "Transform")) {
         target.useTempAttack(target.lastAttack, target.lastTargets, true);
         return AttackResult.proceed;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
