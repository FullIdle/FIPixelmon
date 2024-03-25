package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;

public enum EnumPikachu implements IEnumForm, ICosmeticForm {
   Cosplay("-cosplay", (byte)99),
   Libre("-libre", (byte)98),
   Phd("-phd", (byte)97),
   Popstar("-popstar", (byte)96),
   Rockstar("-rockstar", (byte)95),
   Belle("-belle", (byte)94);

   private String suffix;
   private byte index;

   private EnumPikachu(String suffix, byte index) {
      this.suffix = suffix;
      this.index = index;
   }

   public String getFormSuffix(boolean shiny) {
      return shiny ? "" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return this.suffix;
   }

   public byte getForm() {
      return this.index;
   }

   public boolean isDefaultForm() {
      return false;
   }

   public String getUnlocalizedName() {
      return "pixelmon.pikachu.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != Cosplay;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return Cosplay;
   }

   public String getName() {
      return this.name();
   }
}
