package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumGigantamax implements IEnumForm {
   Normal(0, ""),
   Gigantamax(1, "gmax");

   private final byte form;
   private final String suffix;

   private EnumGigantamax(int form, String suffix) {
      this.form = (byte)form;
      this.suffix = suffix;
   }

   public String getFormSuffix() {
      return this == Normal ? "" : "-" + this.suffix;
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isTemporary() {
      return this != Normal;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return Normal;
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public Set getFormAttributes() {
      return (Set)(this == Gigantamax ? Sets.immutableEnumSet(FormAttributes.GIGANTAMAX, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
