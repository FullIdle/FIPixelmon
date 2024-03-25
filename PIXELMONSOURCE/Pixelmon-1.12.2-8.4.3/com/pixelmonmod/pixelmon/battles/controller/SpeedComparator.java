package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.TrickRoom;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Comparator;

public class SpeedComparator implements Comparator {
   public int compare(PixelmonWrapper pw1, PixelmonWrapper pw2) {
      return this.doesGoFirst(pw1, pw2) ? -1 : 1;
   }

   protected boolean doesGoFirst(PixelmonWrapper p, PixelmonWrapper foe) {
      try {
         for(int i = 0; i < p.bc.globalStatusController.getGlobalStatusSize(); ++i) {
            if (p.bc.globalStatusController.getGlobalStatus(i) instanceof TrickRoom) {
               return ((TrickRoom)p.bc.globalStatusController.getGlobalStatus(i)).participantMovesFirst(p, foe);
            }
         }
      } catch (Exception var4) {
         p.bc.battleLog.onCrash(var4, "Caught error in participantMovesFirst, cause in doesGoFirst().");
      }

      if (p.getBattleStats().getStatWithMod(StatsType.Speed) > foe.getBattleStats().getStatWithMod(StatsType.Speed)) {
         return true;
      } else if (foe.getBattleStats().getStatWithMod(StatsType.Speed) > p.getBattleStats().getStatWithMod(StatsType.Speed)) {
         return false;
      } else {
         return RandomHelper.getRandomNumberBetween(0, 1) < 1;
      }
   }
}
