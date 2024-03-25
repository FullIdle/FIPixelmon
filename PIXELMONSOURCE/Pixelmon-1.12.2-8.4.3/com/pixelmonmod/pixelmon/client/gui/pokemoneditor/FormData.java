package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class FormData {
   public EnumSpecies species;
   public short form;

   public FormData(EnumSpecies species, short form) {
      this.species = species;
      this.form = form;
   }
}
