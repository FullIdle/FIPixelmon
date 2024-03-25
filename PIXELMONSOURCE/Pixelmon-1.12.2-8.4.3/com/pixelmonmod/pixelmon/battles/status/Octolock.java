package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Octolock extends MeanLook {
   public Octolock() {
   }

   public Octolock(PixelmonWrapper locker) {
      super(locker);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.addStatus(new Octolock(user), user)) {
         user.bc.sendToAll("pixelmon.effect.noescape", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      super.applyRepeatedEffect(pw);
      if (pw.bc.isInBattle(this.locker)) {
         StatsType[] var2 = new StatsType[]{StatsType.Defence, StatsType.SpecialDefence};
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            StatsType type = var2[var4];
            pw.getBattleStats().modifyStat(-1, type, pw, true);
         }
      }

   }
}
