package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class DragonsMaw extends AbilityBase {
   boolean usedOnce = false;

   public int modifyDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Dragon) {
         this.usedOnce = true;
         return (int)((double)damage * 1.5);
      } else {
         return damage;
      }
   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
      this.usedOnce = false;
   }

   public boolean needNewInstance() {
      return true;
   }
}
