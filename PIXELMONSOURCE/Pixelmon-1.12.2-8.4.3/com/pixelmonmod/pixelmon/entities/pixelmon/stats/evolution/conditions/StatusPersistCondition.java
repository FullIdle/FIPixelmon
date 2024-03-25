package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class StatusPersistCondition extends EvoCondition {
   StatusType type;

   public StatusPersistCondition() {
      super("status");
      this.type = StatusType.Burn;
   }

   public StatusPersistCondition(StatusType type) {
      this();
      this.type = type;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.getPokemonData().getStatus() != null && pixelmon.getPokemonData().getStatus().type == this.type;
   }

   public StatusType getType() {
      return this.type;
   }
}
