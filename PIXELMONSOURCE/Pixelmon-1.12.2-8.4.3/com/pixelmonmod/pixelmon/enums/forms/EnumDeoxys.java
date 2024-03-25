package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.DeoxysStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Arrays;
import javax.annotation.Nonnull;

public enum EnumDeoxys implements IEnumForm, ICosmeticForm {
   Normal,
   Attack,
   Defense,
   Speed,
   Sus;

   private static final EnumDeoxys[] VALUES = values();
   private static final EnumDeoxys[] NO_COSMETIC = (EnumDeoxys[])Arrays.copyOfRange(VALUES, 0, Speed.ordinal() + 1);

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public String getUnlocalizedName() {
      return "pixelmon.deoxys.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this == Sus;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return (IEnumForm)(pokemon.getFormEnum() == Sus ? Normal : pokemon.getFormEnum());
   }

   public boolean hasShiny(EnumSpecies species) {
      return true;
   }

   public static EnumDeoxys getFromIndex(int index) {
      try {
         return VALUES[index];
      } catch (IndexOutOfBoundsException var2) {
         return Normal;
      }
   }

   public static int getNextForm(int current, Pokemon pokemon) {
      EnumDeoxys[] forms = VALUES;
      if (pokemon.getExtraStats(DeoxysStats.class) == null || !((DeoxysStats)pokemon.getExtraStats(DeoxysStats.class)).isSus()) {
         forms = NO_COSMETIC;
      }

      return current >= 0 && current + 1 < forms.length ? current + 1 : 0;
   }

   public String getName() {
      return this.name();
   }
}
