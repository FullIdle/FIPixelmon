package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class Drive extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      EnumType type = EnumType.Normal;
      if (user.getHeldItem() != null && user.getHeldItem().getHeldItemType() == EnumHeldItems.drive) {
         if (user.getHeldItem() == PixelmonItemsHeld.burnDrive) {
            type = EnumType.Fire;
         } else if (user.getHeldItem() == PixelmonItemsHeld.chillDrive) {
            type = EnumType.Ice;
         } else if (user.getHeldItem() == PixelmonItemsHeld.douseDrive) {
            type = EnumType.Water;
         } else if (user.getHeldItem() == PixelmonItemsHeld.shockDrive) {
            type = EnumType.Electric;
         }
      }

      user.attack.overrideType(type);
      return AttackResult.proceed;
   }
}
