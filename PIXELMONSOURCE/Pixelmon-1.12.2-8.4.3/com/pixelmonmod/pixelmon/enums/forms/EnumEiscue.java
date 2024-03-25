package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumEiscue implements IEnumForm {
   ICE_FACE,
   NOICE_FACE;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == NOICE_FACE;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return ICE_FACE;
   }

   public boolean isDefaultForm() {
      return this == ICE_FACE;
   }

   public String getUnlocalizedName() {
      return "pixelmon.eiscue.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
