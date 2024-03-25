package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

public class Aggression {
   public int timid;
   public int passive;
   public int aggressive;

   public Aggression(int pcTimid, int pcAgg, String pixelmonName) {
      this.timid = pcTimid;
      this.passive = 100 - pcTimid - pcAgg;
      this.aggressive = pcAgg;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Aggression)) {
         return false;
      } else {
         Aggression other = (Aggression)o;
         return other.timid == this.timid && other.passive == this.passive && other.aggressive == this.aggressive;
      }
   }
}
