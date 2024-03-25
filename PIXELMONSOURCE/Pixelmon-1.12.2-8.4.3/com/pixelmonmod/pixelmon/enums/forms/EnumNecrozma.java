package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumNecrozma implements IEnumForm {
   NORMAL,
   DUSK,
   DAWN,
   ULTRA;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == ULTRA;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return pokemon.getPersistentData().func_74764_b("SrcForm") ? values()[pokemon.getPersistentData().func_74762_e("SrcForm")] : this;
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.necrozma.form." + this.name().toLowerCase();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getName() {
      return this.name();
   }
}
