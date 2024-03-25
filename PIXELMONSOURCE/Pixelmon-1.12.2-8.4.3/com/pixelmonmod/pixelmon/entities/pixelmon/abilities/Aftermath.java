package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class Aftermath extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hasDamp = false;
      Iterator var4 = user.bc.getActiveUnfaintedPokemon().iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         if (pw.getBattleAbility() instanceof Damp) {
            hasDamp = true;
         }
      }

      if (!hasDamp && target.isFainted() && !(user.getBattleAbility() instanceof MagicGuard)) {
         user.doBattleDamage(target, (float)user.getPercentMaxHealth(25.0F), DamageTypeEnum.ABILITY);
         user.bc.sendToAll("pixelmon.abilities.aftermath", target.getNickname(), user.getNickname());
      }

   }
}
