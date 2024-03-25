package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Struggle extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("recoil.damage", user.getNickname());
      user.doBattleDamage(user, (float)user.getPercentMaxHealth(25.0F), DamageTypeEnum.STRUGGLE);
   }
}
