package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumMorpeko;

public class AuraWheel extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getFormEnum() == EnumMorpeko.HANGRY) {
         user.attack.overrideType(EnumType.Dark);
         return AttackResult.proceed;
      } else if (user.getFormEnum() == EnumMorpeko.FULLBELLY) {
         user.attack.overrideType(EnumType.Electric);
         return AttackResult.proceed;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
