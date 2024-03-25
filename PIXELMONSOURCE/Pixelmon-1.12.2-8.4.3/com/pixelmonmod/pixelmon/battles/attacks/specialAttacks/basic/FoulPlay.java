package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class FoulPlay extends SpecialAttackBase {
   private transient boolean inProgress = false;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.inProgress) {
         this.inProgress = false;
         return AttackResult.proceed;
      } else {
         this.inProgress = true;
         double oldAttackModifier = user.getBattleStats().getAttackModifier();
         int oldAttack = user.getBattleStats().attackStat;
         int[] userBaseStats;
         if (user.attack.isAttack("Foul Play")) {
            user.getBattleStats().changeStat(StatsType.Attack, (int)target.getBattleStats().getAttackModifier());
            userBaseStats = target.getBattleStats().getBaseBattleStats();
            user.getBattleStats().attackStat = userBaseStats[StatsType.Attack.getStatIndex()];
         } else if (user.attack.isAttack("Body Press")) {
            user.getBattleStats().changeStat(StatsType.Attack, user.getBattleStats().getDefenceModifier());
            userBaseStats = user.getBattleStats().getBaseBattleStats();
            user.getBattleStats().attackStat = userBaseStats[StatsType.Defence.getStatIndex()];
         }

         userBaseStats = user.getBattleStats().getBattleStats();
         user.bc.modifyStats(user, userBaseStats);
         MoveResults[] results = user.useAttackOnly();
         MoveResults[] var8 = results;
         int var9 = results.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            MoveResults result = var8[var10];
            MoveResults var10000 = user.attack.moveResult;
            var10000.damage += result.damage;
            var10000 = user.attack.moveResult;
            var10000.fullDamage += result.fullDamage;
            user.attack.moveResult.accuracy = result.accuracy;
         }

         this.inProgress = false;
         user.getBattleStats().changeStat(StatsType.Attack, (int)oldAttackModifier);
         user.getBattleStats().attackStat = oldAttack;
         return AttackResult.hit;
      }
   }
}
