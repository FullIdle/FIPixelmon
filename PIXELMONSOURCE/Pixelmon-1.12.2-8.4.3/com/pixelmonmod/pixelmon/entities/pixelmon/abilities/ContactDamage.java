package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class ContactDamage extends AbilityBase {
   private String langString;

   protected ContactDamage(String langString) {
      this.langString = langString;
   }

   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.isAlive() && !(user.getBattleAbility() instanceof MagicGuard)) {
         user.bc.sendToAll(this.langString, target.getNickname(), user.getNickname());
         user.doBattleDamage(target, (float)user.getPercentMaxHealth(12.5F), DamageTypeEnum.ABILITY);
      }

   }
}
