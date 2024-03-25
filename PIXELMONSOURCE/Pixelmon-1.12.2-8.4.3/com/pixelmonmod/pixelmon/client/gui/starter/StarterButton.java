package com.pixelmonmod.pixelmon.client.gui.starter;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class StarterButton extends GuiButton {
   int starterIndex;
   boolean clicked = false;

   public StarterButton(int buttonID, int x, int y, int starterIndex) {
      super(buttonID, x, y, 30, 30, "");
      this.starterIndex = starterIndex;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float random) {
      if (this.field_146125_m) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = GuiHelper.isMouseOverButton(this, mouseX, mouseY);
         if (this.clicked) {
            mc.func_110434_K().func_110577_a(GuiResources.roundedButtonOver);
            GuiHelper.drawImageQuad((double)(this.field_146128_h + 4), (double)(this.field_146129_i - 1), 36.0, 32.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         this.func_146119_b(mc, mouseX, mouseY);
         if (this.starterIndex < 0) {
            mc.field_71446_o.func_110577_a(GuiResources.questionMark);
            GuiHelper.drawImageQuad((double)(this.field_146128_h + this.field_146120_f / 2 - 12), (double)(this.field_146129_i + 8), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         } else {
            PokemonForm pokemon = ServerStorageDisplay.starterListPacket.pokemonList[this.starterIndex];
            EnumSpecies species = EnumSpecies.getFromDex(ServerStorageDisplay.starterListPacket.pokemonListIndex[this.starterIndex]);
            GuiHelper.bindPokemonSprite(species, pokemon.form, pokemon.gender, "", pokemon.shiny, 0, mc);
            GuiHelper.drawImageQuad((double)(this.field_146128_h + 3), (double)(this.field_146129_i - 9), 36.0, 36.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }
      }

   }

   public boolean func_146116_c(Minecraft par1Minecraft, int mouseX, int mouseY) {
      return this.starterIndex < 0 ? false : super.func_146116_c(par1Minecraft, mouseX, mouseY);
   }
}
