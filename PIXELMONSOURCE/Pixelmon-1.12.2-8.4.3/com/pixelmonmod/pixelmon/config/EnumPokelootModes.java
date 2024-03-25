package com.pixelmonmod.pixelmon.config;

public enum EnumPokelootModes {
   FCFS(true, true, false),
   PL(false, true, false),
   PU(false, false, false),
   TIMED(false, true, true);

   private boolean oneTimeUse;
   private boolean oncePerPlayer;
   private boolean timeEnabled;

   private EnumPokelootModes(boolean oneTimeUse, boolean oncePerPlayer, boolean timeEnabled) {
      this.oneTimeUse = oneTimeUse;
      this.oncePerPlayer = oncePerPlayer;
      this.timeEnabled = timeEnabled;
   }

   public boolean isOneTimeUse() {
      return this.oneTimeUse;
   }

   public boolean isOncePerPlayer() {
      return this.oncePerPlayer;
   }

   public boolean isTimeEnabled() {
      return this.timeEnabled;
   }
}
