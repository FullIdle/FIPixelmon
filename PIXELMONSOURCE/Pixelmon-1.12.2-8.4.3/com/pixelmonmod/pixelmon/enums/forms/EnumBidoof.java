package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import java.util.Set;

public enum EnumBidoof implements IEnumForm {
   NORMAL,
   SIRDOOFUSIII;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public Set getFormAttributes() {
      return (Set)(this == SIRDOOFUSIII ? Sets.immutableEnumSet(FormAttributes.COSMETIC, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public String getUnlocalizedName() {
      return "pixelmon.bidoof.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
