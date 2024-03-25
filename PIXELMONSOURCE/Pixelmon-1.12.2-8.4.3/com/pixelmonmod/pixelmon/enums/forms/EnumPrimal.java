package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumPrimal implements IEnumForm {
   NORMAL,
   PRIMAL;

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
      return (Set)(this == PRIMAL ? Sets.immutableEnumSet(FormAttributes.PRIMAL_EVOLUTION, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   public boolean isTemporary() {
      return this == PRIMAL;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
