package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemMemory;

public class MultiAttack extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getHeldItem().getHeldItemType() == EnumHeldItems.memory) {
         user.attack.overrideType(((ItemMemory)user.getHeldItem()).type);
      }

      return AttackResult.proceed;
   }
}
