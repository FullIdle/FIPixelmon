package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Geomancy extends MultiTurnCharge {
   private static final transient StatsType[] raiseStats;

   public Geomancy() {
      super("pixelmon.effect.geomancy");
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AttackResult baseResult = super.applyEffectDuring(user, target);
      if (baseResult == AttackResult.proceed) {
         user.getBattleStats().modifyStat(2, (StatsType[])raiseStats);
      }

      return baseResult;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      StatsType[] var7 = raiseStats;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         StatsType stat = var7[var9];
         StatsEffect statsEffect = new StatsEffect(stat, 2, true);
         statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }

      super.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }

   static {
      raiseStats = new StatsType[]{StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed};
   }
}
