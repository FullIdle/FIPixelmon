package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumMeowth implements IEnumForm {
   Normal(0, "normal"),
   Alolan(1, "alola"),
   Galarian(2, "galar"),
   Gigantamax(3, "gmax");

   private final byte form;
   private final String suffix;

   private EnumMeowth(int form, String suffix) {
      this.form = (byte)form;
      this.suffix = suffix;
   }

   public String getFormSuffix() {
      return "-" + this.suffix;
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isTemporary() {
      return this == Gigantamax;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return Normal;
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public Set getFormAttributes() {
      switch (this) {
         case Alolan:
            return Sets.immutableEnumSet(FormAttributes.ALOLAN, new FormAttributes[0]);
         case Galarian:
            return Sets.immutableEnumSet(FormAttributes.GALARIAN, new FormAttributes[0]);
         case Gigantamax:
            return Sets.immutableEnumSet(FormAttributes.GIGANTAMAX, new FormAttributes[0]);
         default:
            return IEnumForm.super.getFormAttributes();
      }
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
