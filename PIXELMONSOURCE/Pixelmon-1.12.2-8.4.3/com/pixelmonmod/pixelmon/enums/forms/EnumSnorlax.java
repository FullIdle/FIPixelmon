package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import java.util.Set;

public enum EnumSnorlax implements IEnumForm {
   Normal(0, "normal"),
   Snow(1, "snow"),
   Gigantamax(2, "gmax");

   private final byte form;
   private final String suffix;

   private EnumSnorlax(int form, String suffix) {
      this.form = (byte)form;
      this.suffix = suffix;
   }

   public String getFormSuffix() {
      return this == Normal ? "" : "-" + this.suffix;
   }

   public String getFormSuffix(boolean shiny) {
      return shiny && this != Gigantamax ? "" : this.getFormSuffix();
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isTemporary() {
      return this == Gigantamax;
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public Set getFormAttributes() {
      switch (this) {
         case Snow:
            return Sets.immutableEnumSet(FormAttributes.COSMETIC, new FormAttributes[0]);
         case Gigantamax:
            return Sets.immutableEnumSet(FormAttributes.GIGANTAMAX, new FormAttributes[0]);
         default:
            return IEnumForm.super.getFormAttributes();
      }
   }

   public String getUnlocalizedName() {
      return (this == Snow ? "pixelmon.snorlax.form." : "pixelmon.generic.form.") + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
