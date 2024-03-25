package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.battles.raids.RaidPokemon;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RandomizeRaidAllyEvent extends Event {
   private final RaidData raid;
   private RaidPokemon ally;

   public RandomizeRaidAllyEvent(RaidData raid, RaidPokemon ally) {
      this.raid = raid;
      this.ally = ally;
   }

   public RaidData getRaid() {
      return this.raid;
   }

   public RaidPokemon getAlly() {
      return this.ally;
   }

   public void setAlly(RaidPokemon ally) {
      this.ally = ally;
   }
}
