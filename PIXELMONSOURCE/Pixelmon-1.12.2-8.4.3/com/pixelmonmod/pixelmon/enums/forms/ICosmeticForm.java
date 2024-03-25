package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import javax.annotation.Nonnull;

public interface ICosmeticForm {
   boolean isCosmetic();

   @Nonnull
   IEnumForm getBaseFromCosmetic(Pokemon var1);

   default boolean hasShiny(EnumSpecies species) {
      return false;
   }
}
