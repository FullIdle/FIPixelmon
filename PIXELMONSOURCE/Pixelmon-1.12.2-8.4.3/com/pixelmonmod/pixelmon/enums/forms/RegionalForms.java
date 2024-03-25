package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import java.util.Set;

public enum RegionalForms implements IEnumForm {
   NORMAL(0, "normal"),
   ALOLAN(1, "alola"),
   GALARIAN(2, "galar"),
   HISUIAN(3, "hisuian");

   private final byte form;
   private final String suffix;

   private RegionalForms(int form, String suffix) {
      this.form = (byte)form;
      this.suffix = suffix;
   }

   public String getFormSuffix() {
      return "-" + this.suffix;
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public Set getFormAttributes() {
      switch (this) {
         case ALOLAN:
            return Sets.immutableEnumSet(FormAttributes.ALOLAN, new FormAttributes[0]);
         case GALARIAN:
            return Sets.immutableEnumSet(FormAttributes.GALARIAN, new FormAttributes[0]);
         case HISUIAN:
            return Sets.immutableEnumSet(FormAttributes.HISUIAN, new FormAttributes[0]);
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
