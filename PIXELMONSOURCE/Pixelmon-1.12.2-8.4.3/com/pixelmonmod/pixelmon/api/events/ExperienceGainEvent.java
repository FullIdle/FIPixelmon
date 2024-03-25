package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ExperienceGainEvent extends Event {
   public final PokemonLink pokemon;
   private int experience;
   private ExperienceGainType type;

   public ExperienceGainEvent(PokemonLink pokemon, int experience, ExperienceGainType type) {
      this.pokemon = pokemon;
      this.experience = experience;
      this.type = type;
   }

   public int getExperience() {
      return this.experience;
   }

   public void setExperience(int experience) {
      this.experience = experience < 0 ? 0 : experience;
   }

   public ExperienceGainType getType() {
      return this.type;
   }
}
