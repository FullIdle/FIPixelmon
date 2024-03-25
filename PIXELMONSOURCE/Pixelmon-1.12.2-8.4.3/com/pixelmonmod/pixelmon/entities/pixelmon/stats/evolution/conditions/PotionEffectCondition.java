package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.List;
import java.util.stream.Collectors;

public class PotionEffectCondition extends EvoCondition {
   private List potions;

   public PotionEffectCondition() {
      super("potionEffect");
      this.potions = Lists.newArrayList();
   }

   public PotionEffectCondition(List potions) {
      this();
      this.potions = potions;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return ((List)pixelmon.func_193076_bZ().keySet().stream().map((p) -> {
         return p.getRegistryName().func_110624_b().equals("minecraft") ? p.getRegistryName().func_110623_a() : p.getRegistryName().toString();
      }).collect(Collectors.toList())).containsAll(this.potions);
   }

   public List getPotions() {
      return this.potions;
   }
}
