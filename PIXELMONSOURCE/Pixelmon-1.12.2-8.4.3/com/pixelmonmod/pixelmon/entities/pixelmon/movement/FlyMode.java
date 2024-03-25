package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

public enum FlyMode {
   Fly(70),
   Swoop(10),
   Land(20),
   FlyClimb,
   SwoopEnd,
   Attack,
   AttackBackOff,
   Retreat,
   Flee,
   CircleOwner,
   FlyToOwner,
   LandOwner,
   FlyAwayDespawn,
   FlyToFriend,
   LandFriend,
   Hover;

   public int probability;

   private FlyMode() {
      this(0);
   }

   private FlyMode(int probability) {
      this.probability = probability;
   }

   public boolean ignoreCollision() {
      return this == Land || this == FlyToOwner || this == LandOwner;
   }
}
