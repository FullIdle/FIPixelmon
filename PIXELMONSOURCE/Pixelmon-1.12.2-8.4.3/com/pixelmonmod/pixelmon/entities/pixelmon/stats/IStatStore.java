package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

public interface IStatStore {
   int getStat(StatsType var1);

   void setStat(StatsType var1, int var2);

   int getTotal();

   default void addStat(StatsType type, int value) {
      this.setStat(type, this.getStat(type) + value);
   }
}
