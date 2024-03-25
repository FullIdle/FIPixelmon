package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class HyperTrainEvent extends Event {
   private StatsType stat;
   private boolean hyperTrained;
   private final Pokemon pokemon;

   public HyperTrainEvent(@Nullable StatsType stat, boolean hyperTrained, Pokemon pokemon) {
      this.stat = stat;
      this.hyperTrained = hyperTrained;
      this.pokemon = pokemon;
   }

   public StatsType getStat() {
      return this.stat;
   }

   public void setStat(StatsType stat) {
      this.stat = stat;
   }

   public boolean isHyperTrained() {
      return this.hyperTrained;
   }

   public void setHyperTrained(boolean hyperTrained) {
      this.hyperTrained = hyperTrained;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }
}
