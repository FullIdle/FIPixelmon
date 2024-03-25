package com.pixelmonmod.pixelmon.client.models.smd;

public enum AnimationType {
   IDLE,
   WALK,
   FLY,
   SWIM,
   IDLE_SWIM,
   SPECIAL,
   SPECIAL2,
   SPECIAL3,
   BOUNCEOPEN(false),
   BOUNCECLOSED(false),
   BREAK(false),
   SHAKELEFT(false),
   SHAKERIGHT(false);

   public boolean isLooped = true;

   private AnimationType() {
   }

   private AnimationType(boolean isLooped) {
      this.isLooped = isLooped;
   }

   public String toString() {
      return this.name().toLowerCase().replace("_", "");
   }

   public static AnimationType getTypeFor(String animation) {
      AnimationType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         AnimationType type = var1[var3];
         if (animation.equalsIgnoreCase(type.toString())) {
            return type;
         }
      }

      return IDLE;
   }
}
