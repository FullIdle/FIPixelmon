package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class FriendshipCondition extends EvoCondition {
   public int friendship;

   public FriendshipCondition() {
      super("friendship");
      this.friendship = 220;
   }

   public FriendshipCondition(int friendship) {
      this();
      this.friendship = friendship;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return this.friendship == -1 ? pixelmon.getPokemonData().isFriendshipHighEnoughToEvolve() : pixelmon.getPokemonData().getFriendship() >= this.friendship;
   }
}
