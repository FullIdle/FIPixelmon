package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Judgment extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.overrideType(this.getOverrideType(user));
      return AttackResult.proceed;
   }

   private EnumType getOverrideType(PixelmonWrapper user) {
      EnumType type = (EnumType)user.type.get(0);
      if (user.attack.getType() == EnumType.Mystery) {
         type = EnumType.Normal;
      }

      return type;
   }
}
