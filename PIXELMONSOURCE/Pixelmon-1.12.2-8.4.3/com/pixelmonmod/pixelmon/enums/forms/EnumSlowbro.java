package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumSlowbro implements IEnumForm {
   Normal(0, "normal"),
   Mega(1, "mega"),
   Galarian(2, "galar");

   private final byte form;
   private final String suffix;

   private EnumSlowbro(int form, String suffix) {
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
      return this == Mega;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return Normal;
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public Set getFormAttributes() {
      switch (this) {
         case Mega:
            return Sets.immutableEnumSet(FormAttributes.MEGA_EVOLUTION, new FormAttributes[0]);
         case Galarian:
            return Sets.immutableEnumSet(FormAttributes.GALARIAN, new FormAttributes[0]);
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
