package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.StatTable;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.util.Tuple;

public class SelfBoost extends SpecialAttackBase {
   StatTable boosts = new StatTable();

   public void applyEffectAfterAllTargets(PixelmonWrapper user) {
      if (user.attack.moveResult.result.isSuccess()) {
         Tuple stats = this.boosts.getStatChanges();
         user.getBattleStats().modifyStat((int[])stats.func_76341_a(), (StatsType[])stats.func_76340_b(), user, true);
      }

   }
}
