package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Frustration extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int friendship = user.getFriendship();
      user.attack.getMove().setBasePower(Math.max(1, (int)((double)(255 - friendship) / 2.5)));
      return AttackResult.proceed;
   }
}
