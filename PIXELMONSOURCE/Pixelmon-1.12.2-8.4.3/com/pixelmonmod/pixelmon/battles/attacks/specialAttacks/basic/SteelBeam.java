package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;

public class SteelBeam extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.doRecoil(user, target);
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.doRecoil(user, target);
   }

   private void doRecoil(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.getBattleAbility().isAbility(MagicGuard.class)) {
         user.bc.sendToAll("recoil.damage", user.getNickname());
         user.doBattleDamage(user, (float)user.getPercentMaxHealth(50.0F), DamageTypeEnum.RECOIL);
      }

   }
}
