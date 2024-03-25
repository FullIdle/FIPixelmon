package com.pixelmonmod.pixelmon.pokedex;

public enum EnumPokedexRegisterStatus {
   unknown,
   seen,
   caught;

   public static EnumPokedexRegisterStatus get(int i) {
      return values()[i];
   }
}
