package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class StickyWeb extends EntryHazard {
   public StickyWeb() {
      super(StatusType.StickyWeb, 1);
   }

   protected void doEffect(PixelmonWrapper pw) {
      PixelmonWrapper opponent = pw;
      ArrayList opponents = pw.getOpponentPokemon();
      if (!opponents.isEmpty()) {
         opponent = (PixelmonWrapper)opponents.get(0);
      }

      pw.getBattleStats().modifyStat(-1, StatsType.Speed, opponent, false);
   }

   public boolean isUnharmed(PixelmonWrapper pw) {
      return super.isUnharmed(pw) || this.isAirborne(pw);
   }

   public EntryHazard getNewInstance() {
      return new StickyWeb();
   }

   protected String getFirstLayerMessage() {
      return "pixelmon.status.stickyweb";
   }

   protected String getAffectedMessage() {
      return "pixelmon.status.stickywebcaught";
   }

   public int getAIWeight() {
      return 20;
   }
}
