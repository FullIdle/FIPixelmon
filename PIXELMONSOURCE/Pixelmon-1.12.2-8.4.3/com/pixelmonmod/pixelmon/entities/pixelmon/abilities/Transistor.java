package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Transistor extends AbilityBase {
   public int modifyDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Electric) {
         damage = (int)((double)damage * 1.5);
      }

      return damage;
   }

   public boolean needNewInstance() {
      return true;
   }
}
