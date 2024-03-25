package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Arrays;
import java.util.HashSet;

public class StompingTantrum extends SpecialAttackBase {
   private static final HashSet results;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.lastAttack != null && results.contains(user.lastAttack.moveResult.result)) {
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      return AttackResult.proceed;
   }

   static {
      results = new HashSet(Arrays.asList(AttackResult.charging, AttackResult.unable, AttackResult.failed, AttackResult.missed, AttackResult.notarget));
   }
}
