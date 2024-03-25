package com.pixelmonmod.pixelmon.comm.packetHandlers.evolution;

public enum EvolutionStage {
   PreChoice(40),
   Choice,
   PreAnimation(140),
   PostAnimation(60),
   End;

   public int ticks;

   private EvolutionStage(int ticks) {
      this.ticks = ticks;
   }

   private EvolutionStage() {
   }
}
