package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum EnumToxtricity implements IEnumForm {
   AMPED("amped", new EnumNature[]{EnumNature.Hardy, EnumNature.Brave, EnumNature.Adamant, EnumNature.Naughty, EnumNature.Docile, EnumNature.Impish, EnumNature.Lax, EnumNature.Hasty, EnumNature.Jolly, EnumNature.Naive, EnumNature.Rash, EnumNature.Sassy, EnumNature.Quirky}),
   LOWKEY("lowkey", new EnumNature[]{EnumNature.Lonely, EnumNature.Bold, EnumNature.Relaxed, EnumNature.Timid, EnumNature.Serious, EnumNature.Modest, EnumNature.Mild, EnumNature.Quiet, EnumNature.Bashful, EnumNature.Calm, EnumNature.Gentle, EnumNature.Careful}),
   GIGANTAMAX("gmax", new EnumNature[0]);

   private static final EnumToxtricity[] VALUES = values();
   private final byte form = (byte)this.ordinal();
   private final String suffix;
   private final EnumSet natures = EnumSet.noneOf(EnumNature.class);

   private EnumToxtricity(String suffix, EnumNature... natures) {
      this.suffix = suffix;
      this.natures.addAll(Arrays.asList(natures));
   }

   public String getFormSuffix() {
      return "-" + this.suffix;
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isTemporary() {
      return this == GIGANTAMAX;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      EnumToxtricity[] var2 = VALUES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumToxtricity form = var2[var4];
         if (form.natures.contains(pokemon.getBaseNature())) {
            return form;
         }
      }

      return AMPED;
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return RandomHelper.rand.nextBoolean() ? AMPED : LOWKEY;
   }

   public boolean isDefaultForm() {
      return this == AMPED || this == LOWKEY;
   }

   public Set getFormAttributes() {
      return (Set)(this == GIGANTAMAX ? Sets.immutableEnumSet(FormAttributes.GIGANTAMAX, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public String getUnlocalizedName() {
      return this == GIGANTAMAX ? "pixelmon.generic.form.gigantamax" : "pixelmon.toxtricity.form." + this.suffix;
   }

   public Set getNatures() {
      return ImmutableSet.copyOf(this.natures);
   }

   public String getName() {
      return this.name();
   }
}
