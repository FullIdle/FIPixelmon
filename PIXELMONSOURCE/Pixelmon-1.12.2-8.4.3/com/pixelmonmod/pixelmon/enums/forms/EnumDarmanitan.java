package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumDarmanitan implements IEnumForm {
   STANDARD,
   ZEN,
   GALAR_STANDARD,
   GALAR_ZEN;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase().replace("_", "-");
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == STANDARD || this == GALAR_STANDARD;
   }

   public boolean isTemporary() {
      return this == ZEN || this == GALAR_ZEN;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      if (pokemon.getFormEnum() == ZEN) {
         return STANDARD;
      } else {
         return pokemon.getFormEnum() == GALAR_ZEN ? GALAR_STANDARD : STANDARD;
      }
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return STANDARD;
   }

   public Set getFormAttributes() {
      switch (this) {
         case GALAR_STANDARD:
         case GALAR_ZEN:
            return Sets.immutableEnumSet(FormAttributes.GALARIAN, new FormAttributes[0]);
         default:
            return IEnumForm.super.getFormAttributes();
      }
   }

   public String getUnlocalizedName() {
      return "pixelmon.darmanitan.form." + this.name().toLowerCase();
   }

   public EnumDarmanitan getZenFromStandard() {
      return this == GALAR_STANDARD ? GALAR_ZEN : ZEN;
   }

   public String getName() {
      return this.name();
   }
}
