package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import java.util.ArrayList;

public class GenderCondition extends EvoCondition {
   public ArrayList genders = Lists.newArrayList();

   public GenderCondition(Gender... genders) {
      super("gender");
      this.genders = Lists.newArrayList(genders);
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return this.genders.contains(pixelmon.getPokemonData().getGender());
   }
}
