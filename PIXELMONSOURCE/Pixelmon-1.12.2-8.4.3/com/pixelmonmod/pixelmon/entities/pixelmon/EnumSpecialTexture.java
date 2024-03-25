package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.util.ITranslatable;

/** @deprecated */
@Deprecated
public enum EnumSpecialTexture implements ITranslatable {
   None(false),
   Roasted(false),
   Zombie(false),
   Online(false),
   Drowned(false),
   Valentine(true),
   Rainbow(true),
   Alien(false),
   Real(false),
   Alternate(false),
   Pink(false),
   Summer(false),
   Crystal(false);

   /** @deprecated */
   @Deprecated
   public int id = this.ordinal();
   public boolean hasShiny;

   private EnumSpecialTexture(boolean hasShiny) {
      this.hasShiny = hasShiny;
   }

   public String toString() {
      return this == None ? "" : this.name();
   }

   public String getUnlocalizedName() {
      return "pixelmon.enum.specialtexture." + this.name().toLowerCase();
   }

   public String getFolderPath() {
      return "pokemon-" + this.name().toLowerCase() + "/";
   }

   public String getTexturePrefix(EnumSpecies species, IEnumForm form, boolean shiny) {
      return shiny && this.hasShiny ? "shiny" : "";
   }

   public String getSpriteFolder(EnumSpecies species, IEnumForm form, boolean shiny) {
      return shiny && this.hasShiny ? "shinypokemon/" : "pokemon/";
   }

   public static EnumSpecialTexture fromIndex(int index) {
      return index > 0 && index < values().length ? values()[index] : None;
   }

   public static EnumSpecialTexture fromName(String name) {
      EnumSpecialTexture[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumSpecialTexture value = var1[var3];
         if (value.name().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return None;
   }
}
