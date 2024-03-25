package com.pixelmonmod.pixelmon.client.gui.battles.tasks;

import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.TimerTask;

public class AFKTask extends TimerTask {
   private ClientBattleManager bm;

   public AFKTask(ClientBattleManager bm) {
      this.bm = bm;
   }

   public void run() {
      if (this.bm.waitingText) {
         this.bm.waitingText = false;
      } else if (this.bm.mode != BattleMode.LevelUp && this.bm.mode != BattleMode.ReplaceAttack && this.bm.mode != BattleMode.YesNoReplaceMove && this.bm.mode != BattleMode.MegaEvolution && !this.bm.hasMoreMessages()) {
         --this.bm.afkTime;
      }

   }
}
