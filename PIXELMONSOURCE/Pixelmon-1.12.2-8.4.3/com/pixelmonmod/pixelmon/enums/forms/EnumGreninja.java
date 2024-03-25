package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;

public enum EnumGreninja implements IEnumForm, ICosmeticForm {
   BASE,
   BATTLE_BOND,
   ASH,
   ZOMBIE_BATTLE_BOND,
   ZOMBIE,
   ASH_ZOMBIE,
   ALTER_BATTLE_BOND,
   ALTER,
   ASH_ALTER;

   public String getFormSuffix() {
      if (this == ZOMBIE_BATTLE_BOND) {
         return "-zombie";
      } else if (this == ALTER_BATTLE_BOND) {
         return "-alter";
      } else {
         return this.ordinal() > BATTLE_BOND.ordinal() ? "-" + this.name().toLowerCase() : "";
      }
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == ASH || this == ASH_ZOMBIE || this == ASH_ALTER;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      if (this != ASH && this != BATTLE_BOND) {
         if (this == ASH_ZOMBIE) {
            return ZOMBIE_BATTLE_BOND;
         } else {
            return this == ASH_ALTER ? ALTER_BATTLE_BOND : BASE;
         }
      } else {
         return BATTLE_BOND;
      }
   }

   public String getUnlocalizedName() {
      return "pixelmon.greninja.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != BASE && this != BATTLE_BOND && this != ASH;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      if (this == ZOMBIE) {
         return BASE;
      } else if (this == ZOMBIE_BATTLE_BOND) {
         return BATTLE_BOND;
      } else if (this == ASH_ZOMBIE) {
         return ASH;
      } else if (this == ALTER) {
         return BASE;
      } else if (this == ALTER_BATTLE_BOND) {
         return BATTLE_BOND;
      } else {
         return this == ASH_ALTER ? ASH : this;
      }
   }

   public boolean isDefaultForm() {
      return this == BASE;
   }

   public EnumGreninja getAshForm() {
      if (this == BATTLE_BOND) {
         return ASH;
      } else if (this == ZOMBIE_BATTLE_BOND) {
         return ASH_ZOMBIE;
      } else {
         return this == ALTER_BATTLE_BOND ? ASH_ALTER : null;
      }
   }

   public String getName() {
      return this.name();
   }
}
