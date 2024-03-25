package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.GuardSplit;
import com.pixelmonmod.pixelmon.battles.status.PowerSplit;
import com.pixelmonmod.pixelmon.battles.status.PowerTrick;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.WonderRoom;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Iterator;

public class BeastBoost extends AbilityBase {
   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.isFainted()) {
         StatsType highest = StatsType.HP;
         int highestAmount = 0;
         int[] stats = user.getBattleStats().getBaseBattleStats();
         Iterator var8 = user.getStatuses().iterator();

         while(true) {
            StatusBase status;
            do {
               if (!var8.hasNext()) {
                  var8 = user.bc.globalStatusController.getGlobalStatuses().iterator();

                  while(var8.hasNext()) {
                     GlobalStatusBase status = (GlobalStatusBase)var8.next();
                     if (status instanceof WonderRoom) {
                        status.modifyBaseStats(user, stats);
                     }
                  }

                  if (stats[StatsType.Attack.getStatIndex()] > highestAmount) {
                     highestAmount = stats[StatsType.Attack.getStatIndex()];
                     highest = StatsType.Attack;
                  }

                  if (stats[StatsType.Defence.getStatIndex()] > highestAmount) {
                     highestAmount = stats[StatsType.Defence.getStatIndex()];
                     highest = StatsType.Defence;
                  }

                  if (stats[StatsType.SpecialAttack.getStatIndex()] > highestAmount) {
                     highestAmount = stats[StatsType.SpecialAttack.getStatIndex()];
                     highest = StatsType.SpecialAttack;
                  }

                  if (stats[StatsType.SpecialDefence.getStatIndex()] > highestAmount) {
                     highestAmount = stats[StatsType.SpecialDefence.getStatIndex()];
                     highest = StatsType.SpecialDefence;
                  }

                  if (stats[StatsType.Speed.getStatIndex()] > highestAmount) {
                     highest = StatsType.Speed;
                  }

                  if (user.getBattleStats().statCanBeRaised(highest)) {
                     user.getBattleStats().modifyStat(1, (StatsType)highest);
                  }

                  return;
               }

               status = (StatusBase)var8.next();
            } while(!(status instanceof PowerSplit) && !(status instanceof GuardSplit) && !(status instanceof PowerTrick));

            stats = status.modifyStats(user, stats);
         }
      }
   }
}
