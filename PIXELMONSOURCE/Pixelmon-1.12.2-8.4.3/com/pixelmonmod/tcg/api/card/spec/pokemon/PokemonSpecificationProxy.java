package com.pixelmonmod.tcg.api.card.spec.pokemon;

import com.pixelmonmod.tcg.api.card.spec.SpecificationFactory;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import net.minecraft.nbt.NBTTagCompound;

public class PokemonSpecificationProxy {
   public static void register(Requirement requirement) {
      SpecificationFactory.register(PokemonSpecification.class, requirement);
   }

   public static PokemonSpecification requirements(String spec) {
      return (PokemonSpecification)SpecificationFactory.requirements(PokemonSpecification.class, spec);
   }

   public static PokemonSpecification create(String... specs) {
      return (PokemonSpecification)SpecificationFactory.create(PokemonSpecification.class, specs);
   }

   public static PokemonSpecification create(Object... args) {
      return (PokemonSpecification)SpecificationFactory.create(PokemonSpecification.class, args);
   }

   public static String[] getRequirementNames() {
      return SpecificationFactory.getRequirementNames(PokemonSpecification.class);
   }

   public static PokemonSpecification fromNbt(NBTTagCompound nbt) {
      return (PokemonSpecification)SpecificationFactory.fromNbt(PokemonSpecification.class, nbt);
   }
}
