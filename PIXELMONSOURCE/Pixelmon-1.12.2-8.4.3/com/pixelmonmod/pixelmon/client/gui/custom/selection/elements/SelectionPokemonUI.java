package com.pixelmonmod.pixelmon.client.gui.custom.selection.elements;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiPokemonUI;

public class SelectionPokemonUI extends GuiPokemonUI {
   private final Pokemon pokemon;
   private boolean selected = false;

   public SelectionPokemonUI(int leftOffset, int topOffset, Pokemon pokemon) {
      super(leftOffset, topOffset);
      this.pokemon = pokemon;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }
}
