package com.pixelmonmod.tcg.api.card.spec.tcg;

import com.pixelmonmod.tcg.api.card.spec.SpecificationFactory;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import net.minecraft.nbt.NBTTagCompound;

public class CardSpecificationProxy {
   public static void register(Requirement requirement) {
      SpecificationFactory.register(CardSpecification.class, requirement);
   }

   public static CardSpecification requirements(String spec) {
      return (CardSpecification)SpecificationFactory.requirements(CardSpecification.class, spec);
   }

   public static CardSpecification create(String... specs) {
      return (CardSpecification)SpecificationFactory.create(CardSpecification.class, specs);
   }

   public static CardSpecification create(Object... args) {
      return (CardSpecification)SpecificationFactory.create(CardSpecification.class, args);
   }

   public static String[] getRequirementNames() {
      return SpecificationFactory.getRequirementNames(CardSpecification.class);
   }

   public static CardSpecification fromNbt(NBTTagCompound nbt) {
      return (CardSpecification)SpecificationFactory.fromNbt(CardSpecification.class, nbt);
   }
}
