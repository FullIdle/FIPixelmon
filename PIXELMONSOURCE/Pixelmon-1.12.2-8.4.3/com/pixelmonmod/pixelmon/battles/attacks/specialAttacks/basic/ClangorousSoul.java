package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class ClangorousSoul extends SpecialAttackBase {
   private static final transient float ONE_THIRD = 33.333332F;
   private static final transient StatsType[] raiseStats;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean canUse = true;
      StatsType[] var4 = raiseStats;
      int var5 = var4.length;

      int var6;
      StatsType statsType;
      for(var6 = 0; var6 < var5; ++var6) {
         statsType = var4[var6];
         if (user.getBattleStats().getStage(statsType) >= 6) {
            canUse = false;
         }
      }

      if (user.getHealthPercent() > 33.333332F && canUse) {
         user.doBattleDamage(user, (float)user.getPercentMaxHealth(33.333332F), DamageTypeEnum.SELF);
         var4 = raiseStats;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            statsType = var4[var6];
            user.getBattleStats().modifyStat(1, (StatsType)statsType);
         }

         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!(MoveChoice.getMaxDamagePercent(pw, bestOpponentChoices) > pw.getHealthPercent() - 33.333332F)) {
         StatsType[] var7 = raiseStats;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            StatsType statsType = var7[var9];
            StatsEffect effect = new StatsEffect(statsType, 1, true);
            effect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
         }

      }
   }

   static {
      raiseStats = new StatsType[]{StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed};
   }
}
