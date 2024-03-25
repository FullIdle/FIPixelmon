package com.pixelmonmod.tcg.api.registries;

import com.pixelmonmod.tcg.api.card.spec.tcg.CardSpecificationProxy;
import com.pixelmonmod.tcg.api.card.spec.tcg.requirement.impl.RarityRequirement;

public class RequirementRegistry {
   public static void init() {
      CardSpecificationProxy.register(new RarityRequirement());
   }
}
