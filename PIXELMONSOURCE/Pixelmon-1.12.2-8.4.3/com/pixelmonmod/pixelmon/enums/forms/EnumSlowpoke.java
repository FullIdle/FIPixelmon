package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import java.util.Set;

public enum EnumSlowpoke implements IEnumForm {
   Normal(0, "normal"),
   Galarian(1, "galar");

   private final byte form;
   private final String suffix;

   private EnumSlowpoke(int form, String suffix) {
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
      return this == Normal;
   }

   public Set getFormAttributes() {
      return (Set)(this == Galarian ? Sets.immutableEnumSet(FormAttributes.GALARIAN, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
