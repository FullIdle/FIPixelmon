package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BattleStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SpectralThief extends SpecialAttackBase {
   private static final StatsType[] STATS_TYPES;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      BattleStats userStats = user.getBattleStats();
      BattleStats targetStats = target.getBattleStats();
      boolean steal = false;
      StatsType[] var6 = STATS_TYPES;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         StatsType type = var6[var8];
         if (targetStats.getStage(type) > 0 && userStats.getStage(type) < 6) {
            int userStage = userStats.getStage(type);
            int targetStage = targetStats.getStage(type);
            int newStage = userStage + targetStage;
            if (newStage > 6) {
               newStage = targetStage - newStage % 6;
            }

            if (!steal) {
               user.bc.sendToAll("pixelmon.effect.spectralthief", target.getNickname());
               steal = true;
            }

            userStats.modifyStat(newStage, type, user, true);
            targetStats.decreaseStat(-targetStage, type, user, false);
         }
      }

      return AttackResult.proceed;
   }

   static {
      STATS_TYPES = new StatsType[]{StatsType.Accuracy, StatsType.Evasion, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed};
   }
}
