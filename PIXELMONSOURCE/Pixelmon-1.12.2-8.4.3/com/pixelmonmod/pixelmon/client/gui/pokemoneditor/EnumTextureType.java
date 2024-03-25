package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import net.minecraft.client.resources.I18n;

enum EnumTextureType {
   Normal,
   Shiny;

   String getLocalizedName() {
      return I18n.func_135052_a("gui.trainereditor." + this.name().toLowerCase(), new Object[0]);
   }
}
