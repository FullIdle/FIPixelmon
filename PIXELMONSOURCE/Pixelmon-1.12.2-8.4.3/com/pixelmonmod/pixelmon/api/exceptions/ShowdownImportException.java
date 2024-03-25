package com.pixelmonmod.pixelmon.api.exceptions;

public class ShowdownImportException extends Exception {
   public final ShowdownFieldType field;
   public final String value;

   public ShowdownImportException(ShowdownFieldType field, String value) {
      this.field = field;
      this.value = value;
   }

   public static enum ShowdownFieldType {
      SPECIES("Pokémon"),
      IVs("IVs"),
      EVs("EVs"),
      ATTACKS("Moves"),
      ABILITY("Ability"),
      CAUGHT_BALL("Poke Ball"),
      NATURE("Nature"),
      GROWTH("Growth"),
      GENDER("Gender"),
      FRIENDSHIP("Happiness"),
      LEVEL("Level"),
      SHINY("Shiny"),
      CLONES("Clones"),
      RUBY("Rubies"),
      SMELTS("Smelts");

      public final String errorCode;

      private ShowdownFieldType(String errorCode) {
         this.errorCode = errorCode;
      }
   }
}
