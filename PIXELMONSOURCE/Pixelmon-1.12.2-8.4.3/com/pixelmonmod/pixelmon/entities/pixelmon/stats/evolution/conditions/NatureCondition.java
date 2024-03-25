package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.List;

public class NatureCondition extends EvoCondition {
   private List natures;

   public NatureCondition() {
      super("nature");
      this.natures = Lists.newArrayList();
   }

   public NatureCondition(List natures) {
      this();
      this.natures = natures;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return this.natures.contains(pixelmon.getPokemonData().getBaseNature());
   }

   public List getNatures() {
      return this.natures;
   }
}
