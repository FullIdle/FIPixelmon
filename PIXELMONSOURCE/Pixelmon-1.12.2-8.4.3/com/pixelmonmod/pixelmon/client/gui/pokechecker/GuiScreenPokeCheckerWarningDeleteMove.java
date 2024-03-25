package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiWarning;
import com.pixelmonmod.pixelmon.comm.packetHandlers.DeleteMove;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

class GuiScreenPokeCheckerWarningDeleteMove extends GuiWarning {
   private Pokemon pokemon;
   private int moveToDelete;

   GuiScreenPokeCheckerWarningDeleteMove(GuiScreen previousScreen, Pokemon pokemon, int moveToDelete) {
      super(previousScreen);
      this.pokemon = pokemon;
      this.moveToDelete = moveToDelete;
   }

   protected void confirmAction() {
      Pixelmon.network.sendToServer(new DeleteMove(this.pokemon.getUUID(), this.moveToDelete));
   }

   protected void drawWarningText() {
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.forget1", new Object[0]), 60, 68, 16777215);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.forget2", new Object[]{this.pokemon.getDisplayName()}), 60, 78, 16777215);
      this.func_73732_a(this.field_146297_k.field_71466_p, this.pokemon.getMoveset().get(this.moveToDelete).getActualMove().getLocalizedName() + "?", 60, 88, 16777215);
   }
}
