package com.pixelmonmod.pixelmon.enums.forms;

import java.util.Objects;

public enum EnumOrigin implements IEnumForm {
   NORMAL,
   ORIGIN;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return Objects.equals(this, NORMAL) ? "pixelmon.generic.form.noform" : "pixelmon.giratina.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
