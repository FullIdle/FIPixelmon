package com.pixelmonmod.pixelmon.client.gui.selectPokemon;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class SelectPokemonButton extends GuiButton {
   protected static final ResourceLocation buttonTexture = new ResourceLocation("pixelmon:textures/gui/starter/starterHolder.png");
   protected static final ResourceLocation mouseOverTexture = new ResourceLocation("pixelmon:textures/gui/starter/moStarter.png");
   protected static final ResourceLocation questionMark = new ResourceLocation("pixelmon:textures/gui/starter/questionmark.png");
   int starterIndex;

   public SelectPokemonButton(int buttonId, int x, int y, int starterIndex) {
      super(buttonId, x, y, 80, 33, "");
      this.starterIndex = starterIndex;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float random) {
      if (this.field_146125_m) {
         FontRenderer fontrenderer = mc.field_71466_p;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         if (this.field_146123_n && this.starterIndex >= 0) {
            mc.func_110434_K().func_110577_a(mouseOverTexture);
         } else {
            mc.func_110434_K().func_110577_a(buttonTexture);
         }

         GuiHelper.drawImageQuad((double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (float)this.field_146121_g, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         this.func_146119_b(mc, mouseX, mouseY);
         int l = 0;
         if (this.starterIndex < 0) {
            l = 14737632;
            mc.field_71446_o.func_110577_a(questionMark);
            GuiHelper.drawImageQuad((double)(this.field_146128_h + this.field_146120_f / 2 - 12), (double)(this.field_146129_i + 5), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         } else {
            if (!this.field_146124_l) {
               l = -6250336;
            } else if (this.field_146123_n) {
               l = 16777215;
            }

            int npn = ServerStorageDisplay.selectPokemonListPacket.pokemonListIndex[this.starterIndex];
            String numString = String.format("%03d", npn);
            mc.field_71446_o.func_110577_a(GuiResources.sprite(numString));
            GuiHelper.drawImageQuad((double)(this.field_146128_h + 3), (double)(this.field_146129_i + 3), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         GlStateManager.func_179152_a(0.8F, 0.8F, 0.8F);
         String name;
         if (this.starterIndex == -1) {
            name = I18n.func_74838_a("gui.starter.comingsoon");
            fontrenderer.func_78276_b(name, (int)((float)(this.field_146128_h + this.field_146120_f / 2 - 25) / 0.8F), (int)((float)(this.field_146129_i + (this.field_146121_g - 3) / 2) / 0.8F), l);
         } else if (this.starterIndex == -2) {
            name = I18n.func_74838_a("gui.starter.disabled");
            fontrenderer.func_78276_b(name, (int)((float)(this.field_146128_h + this.field_146120_f / 2 - 15) / 0.8F), (int)((float)(this.field_146129_i + (this.field_146121_g - 3) / 2) / 0.8F), l);
         } else {
            name = ServerStorageDisplay.selectPokemonListPacket.pokemonList[this.starterIndex].pokemon.getLocalizedName();
            fontrenderer.func_78276_b(name, (int)((float)(this.field_146128_h + this.field_146120_f / 2 - 15) / 0.8F), (int)((float)(this.field_146129_i + (this.field_146121_g - 3) / 2) / 0.8F), l);
         }

         GlStateManager.func_179152_a(1.25F, 1.25F, 1.25F);
      }

   }

   public boolean func_146116_c(Minecraft par1Minecraft, int mouseX, int mouseY) {
      return this.starterIndex < 0 ? false : super.func_146116_c(par1Minecraft, mouseX, mouseY);
   }
}
