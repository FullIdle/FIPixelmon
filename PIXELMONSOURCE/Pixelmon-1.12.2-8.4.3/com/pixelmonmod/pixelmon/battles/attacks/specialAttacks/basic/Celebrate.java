package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Celebrate extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.effect.celebrate", user.getParticipant().getDisplayName());
      return AttackResult.succeeded;
   }
}
