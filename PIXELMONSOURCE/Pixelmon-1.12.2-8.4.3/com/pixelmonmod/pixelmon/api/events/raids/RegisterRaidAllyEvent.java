package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.battles.raids.RaidPokemon;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterRaidAllyEvent extends Event {
   private final ArrayList possibleAllies;
   private boolean defaults = true;

   public RegisterRaidAllyEvent(ArrayList possibleAllies) {
      this.possibleAllies = possibleAllies;
   }

   public void enableDefaults() {
      this.defaults = true;
   }

   public void disableDefaults() {
      this.defaults = false;
   }

   public boolean shouldUseDefaults() {
      return this.defaults;
   }

   public void clearAllies() {
      this.possibleAllies.clear();
   }

   public void addAllies(RaidPokemon... pokemon) {
      this.possibleAllies.addAll(Arrays.asList(pokemon));
   }
}
