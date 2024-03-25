package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RibbonRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"ribbon", "ribbons"});
   private static final List NONE;
   private List ribbons;

   public RibbonRequirement() {
      super(KEYS);
   }

   public RibbonRequirement(List ribbons) {
      this();
      this.ribbons = ribbons;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         if (args.length < 2) {
            return Collections.singletonList(this.createInstance(NONE));
         } else {
            List ribbons = Lists.newArrayList();
            String[] var5 = args[1].split(",");
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String s = var5[var7];
               ribbons.add(EnumRibbonType.valueOf(s));
            }

            return Collections.singletonList(this.createInstance((List)ribbons));
         }
      }
   }

   public Requirement createInstance(List value) {
      return new RibbonRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return this.ribbons.containsAll(pixelmon.getRibbons());
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.getRibbons().clear();
      pixelmon.getRibbons().addAll(this.ribbons);
   }

   public List getValue() {
      return this.ribbons;
   }

   static {
      NONE = Collections.singletonList(EnumRibbonType.NONE);
   }
}
