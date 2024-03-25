package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class NoRetreat extends StatusBase {
   transient PixelmonWrapper locker;

   public NoRetreat() {
      super(StatusType.NoRetreat);
   }

   public NoRetreat(PixelmonWrapper locker) {
      super(StatusType.NoRetreat);
      this.locker = locker;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasStatus(StatusType.NoRetreat)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         StatsType[] var3 = new StatsType[]{StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed};
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            StatsType type = var3[var5];
            user.getBattleStats().modifyStat(1, type, user, true);
         }

         if (!user.hasStatus(StatusType.MeanLook) && user.addStatus(new NoRetreat(user), user)) {
            user.bc.sendToAll("pixelmon.effect.noescape", user.getNickname());
         }

      }
   }

   public boolean stopsSwitching() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.bc.isInBattle(this.locker)) {
         pw.removeStatus((StatusBase)this);
      }

   }
}
