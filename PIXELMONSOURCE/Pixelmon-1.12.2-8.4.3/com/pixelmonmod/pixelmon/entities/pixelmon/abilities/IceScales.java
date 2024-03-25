package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class IceScales extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return a.getAttackCategory() == AttackCategory.SPECIAL ? damage / 2 : damage;
   }
}
