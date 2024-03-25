package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.LiquidOoze;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBigRoot;

public class StrengthSap extends EffectBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      int restoration = target.getBattleStats().getStatWithMod(StatsType.Attack);
      if (user.getUsableHeldItem() instanceof ItemBigRoot) {
         restoration = (int)((double)restoration * 1.3);
      }

      if (target.getBattleAbility() instanceof LiquidOoze) {
         user.bc.sendToAll("pixelmon.abilities.liquidooze", user.getNickname());
         user.doBattleDamage(target, (float)restoration, DamageTypeEnum.ABILITY);
      } else {
         if (target.getBattleStats().modifyStat(-1, StatsType.Attack, target, true)) {
            user.healEntityBy(restoration);
         } else if (!target.getBattleAbility(user).allowsStatChange(target, user, new StatsEffect(StatsType.Attack, -1, false))) {
            user.healEntityBy(restoration);
         }

      }
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }
}
