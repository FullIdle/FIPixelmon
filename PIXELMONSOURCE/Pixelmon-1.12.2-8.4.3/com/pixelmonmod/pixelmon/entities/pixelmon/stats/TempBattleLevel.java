package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;

public class TempBattleLevel extends Level {
   protected int level;
   protected int exp = 0;

   public TempBattleLevel(DelegateLink p, int level) {
      super(p);
      this.setLevel(level);
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int i) {
      this.level = i;
      this.pixelmon.getStats().setLevelStats(this.pixelmon.getNature(), this.pixelmon.getBaseStats(), this.getLevel());
   }

   public void updateExpToNextLevel() {
      this.expToNextLevel = -1;
   }

   public int getExp() {
      return this.exp;
   }

   public void setExp(int experience) {
      this.exp = experience;
   }

   public boolean canLevelUp() {
      return false;
   }

   protected void onLevelUp(PixelmonStatsData stats) {
   }

   public void awardEXP(int experience, ExperienceGainType type) {
   }

   public void recalculateXP() {
      this.setExp(0);
      this.expToNextLevel = -1;
   }
}
