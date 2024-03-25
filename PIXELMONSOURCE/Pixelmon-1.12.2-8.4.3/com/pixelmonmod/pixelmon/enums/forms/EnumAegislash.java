package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;

public enum EnumAegislash implements IEnumForm, ICosmeticForm {
   SHIELD,
   BLADE,
   SHIELD_ALTER,
   BLADE_ALTER;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this != SHIELD && this != SHIELD_ALTER;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return pokemon.getFormEnum() == BLADE ? SHIELD : (pokemon.getFormEnum() == BLADE_ALTER ? SHIELD_ALTER : SHIELD);
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return SHIELD;
   }

   public boolean isDefaultForm() {
      return this == SHIELD;
   }

   public String getUnlocalizedName() {
      return "pixelmon.aegislash.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != SHIELD && this != BLADE;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return (IEnumForm)(pokemon.getFormEnum() == SHIELD_ALTER ? SHIELD : (pokemon.getFormEnum() == BLADE_ALTER ? BLADE : pokemon.getFormEnum()));
   }

   public String getName() {
      return this.name();
   }
}
