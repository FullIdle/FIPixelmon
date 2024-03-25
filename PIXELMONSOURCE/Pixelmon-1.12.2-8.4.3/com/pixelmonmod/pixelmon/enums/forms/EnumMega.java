package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumMega implements IEnumForm {
   Normal(0, ""),
   Mega(1, "mega"),
   MegaX(1, "mega-x"),
   MegaY(2, "mega-y");

   private final byte form;
   private final String suffix;

   private EnumMega(int form, String suffix) {
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
      return (Set)(this != Normal ? Sets.immutableEnumSet(FormAttributes.MEGA_EVOLUTION, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
