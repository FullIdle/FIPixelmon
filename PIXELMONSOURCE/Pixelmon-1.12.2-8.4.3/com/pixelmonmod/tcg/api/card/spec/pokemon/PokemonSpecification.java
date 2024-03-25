package com.pixelmonmod.tcg.api.card.spec.pokemon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.tcg.api.card.spec.AbstractSpecification;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.World;

public class PokemonSpecification extends AbstractSpecification {
   public PokemonSpecification(String originalSpec, List requirements) {
      super(Pokemon.class, EntityPixelmon.class, originalSpec, requirements);
   }

   public PokemonSpecification clone() {
      return PokemonSpecificationProxy.create(this.originalSpec);
   }

   public Pokemon create() {
      return this.create(false);
   }

   public Pokemon create(boolean shallow) {
      Pokemon pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID());
      Iterator var3 = this.requirements.iterator();

      while(var3.hasNext()) {
         Requirement requirement = (Requirement)var3.next();
         requirement.applyData(pokemon);
      }

      if (this.requirements.isEmpty()) {
         pokemon.setSpecies(EnumSpecies.MissingNo, true);
      }

      if (!shallow) {
         pokemon.initialize();
      }

      return pokemon;
   }

   public EntityPixelmon create(World world) {
      EntityPixelmon pixelmon = new EntityPixelmon(world);
      pixelmon.setPokemon(this.create());
      Iterator var3 = this.requirements.iterator();

      while(var3.hasNext()) {
         Requirement requirement = (Requirement)var3.next();
         requirement.applyMinecraft(pixelmon);
      }

      return pixelmon;
   }
}
