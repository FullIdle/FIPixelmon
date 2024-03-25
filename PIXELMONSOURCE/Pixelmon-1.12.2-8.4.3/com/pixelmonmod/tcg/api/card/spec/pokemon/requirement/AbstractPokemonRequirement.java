package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.tcg.api.card.spec.requirement.AbstractRequirement;
import java.util.List;
import java.util.Set;

public abstract class AbstractPokemonRequirement extends AbstractRequirement {
   public AbstractPokemonRequirement(Set keys) {
      super(keys);
   }

   public abstract List createSimple(String var1, String var2);

   public void applyMinecraft(EntityPixelmon entityPixelmon) {
      this.applyData(entityPixelmon.getPokemonData());
   }

   public boolean isMinecraftMatch(EntityPixelmon entityPixelmon) {
      return this.isDataMatch(entityPixelmon.getPokemonData());
   }
}
