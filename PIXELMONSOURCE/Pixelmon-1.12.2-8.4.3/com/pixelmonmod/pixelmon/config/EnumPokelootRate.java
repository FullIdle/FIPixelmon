package com.pixelmonmod.pixelmon.config;

public enum EnumPokelootRate {
   MINIMAL(508, 64, 4),
   NORMAL(254, 32, 2),
   MORE(143, 18, 3),
   EXTREME(76, 9, 3);

   private int minDistance;
   private int minChunk;
   private int hidden_frequency;

   private EnumPokelootRate(int minDistance, int minChunk, int hidden_frequency) {
      this.minDistance = minDistance;
      this.minChunk = minChunk;
      this.hidden_frequency = hidden_frequency;
   }

   public int getMinDistance() {
      return this.minDistance;
   }

   public int getMinChunk() {
      return this.minChunk;
   }

   public int getHidden_frequency() {
      return this.hidden_frequency;
   }
}
