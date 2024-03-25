package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.util.math.MathHelper;

public enum EnumZygarde implements IEnumForm {
   FIFTY_PERCENT("50%"),
   TEN_PERCENT("10%"),
   COMPLETE("complete");

   private final String form;

   private EnumZygarde(String form) {
      this.form = form;
   }

   public String getFormSuffix() {
      return this == FIFTY_PERCENT ? "" : "-" + this.form;
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == COMPLETE;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      if (pokemon != null && pokemon.getPersistentData().func_74764_b("SrcForm")) {
         int form = MathHelper.func_76125_a(pokemon.getPersistentData().func_74762_e("SrcForm"), 0, 2);
         return values()[form];
      } else {
         return TEN_PERCENT;
      }
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return FIFTY_PERCENT;
   }

   public boolean isDefaultForm() {
      return this != COMPLETE;
   }

   public String getUnlocalizedName() {
      return "pixelmon.zygarde.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
