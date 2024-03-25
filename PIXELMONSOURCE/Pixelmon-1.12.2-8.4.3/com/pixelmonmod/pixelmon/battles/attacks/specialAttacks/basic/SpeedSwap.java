package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.SpeedSwapStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SpeedSwap extends Swap {
   public SpeedSwap() {
      super("pixelmon.effect.speedswap", StatsType.Speed);
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll(this.langString, user.getNickname(), target.getNickname());
      target.removeStatus(StatusType.SpeedSwap);
      user.removeStatus(StatusType.SpeedSwap);
      int targetStat = target.getBattleStats().speedStat;
      target.addStatus(new SpeedSwapStatus(user.getBattleStats().speedStat), user);
      user.addStatus(new SpeedSwapStatus(targetStat), target);
      return AttackResult.succeeded;
   }
}
