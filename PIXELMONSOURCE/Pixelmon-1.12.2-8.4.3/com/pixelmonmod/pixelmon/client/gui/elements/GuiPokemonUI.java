package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiPokemonUI {
   private final Minecraft mc = Minecraft.func_71410_x();
   private final FontRenderer fontRenderer;
   private int leftOffset;
   private int topOffset;

   public GuiPokemonUI(int leftOffset, int topOffset) {
      this.fontRenderer = this.mc.field_71466_p;
      this.leftOffset = leftOffset;
      this.topOffset = topOffset;
   }

   public void drawPokemon(Pokemon pokemon, int leftX, int topY, int mouseX, int mouseY, float zLevel) {
      this.drawPokemon(pokemon, leftX, topY, mouseX, mouseY, zLevel, false);
   }

   public void drawPokemon(Pokemon pokemon, int leftX, int topY, int mouseX, int mouseY, float zLevel, boolean highlight) {
      int offset = false;
      boolean priorUniFlag = this.fontRenderer.func_82883_a();
      this.fontRenderer.func_78264_a(true);
      if (pokemon != null) {
         if (highlight) {
            GlStateManager.func_179131_c(0.5F, 0.5F, 0.5F, 1.0F);
         }

         this.mc.field_71446_o.func_110577_a(GuiResources.textbox);
         GuiHelper.drawImageQuad((double)leftX, (double)(topY - 10), 123.0, 34.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         String displayName = pokemon.getDisplayName();
         this.fontRenderer.func_78276_b(displayName, leftX + 28, topY, 16777215);
         if (!pokemon.isEgg()) {
            String levelString = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemon.getLevel();
            this.fontRenderer.func_78276_b(levelString, leftX + 29, topY + 1 + this.fontRenderer.field_78288_b, 16777215);
            if (pokemon.getHealth() <= 0) {
               this.fontRenderer.func_78276_b(I18n.func_135052_a("gui.creativeinv.fainted", new Object[0]), leftX + 30 + this.fontRenderer.func_78256_a(levelString), topY + 1 + this.fontRenderer.field_78288_b, 16777215);
            } else {
               this.fontRenderer.func_78276_b(I18n.func_135052_a("nbt.hp.name", new Object[0]) + " " + pokemon.getHealth() + "/" + pokemon.getMaxHealth(), leftX + 31 + this.fontRenderer.func_78256_a(levelString), topY + 1 + this.fontRenderer.field_78288_b, 16777215);
            }
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (pokemon.getGender() == Gender.Male && !pokemon.isEgg()) {
            this.mc.field_71446_o.func_110577_a(GuiResources.male);
            GuiHelper.drawImageQuad((double)(this.fontRenderer.func_78256_a(displayName) + leftX + 29), (double)topY, 5.0, 8.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         } else if (pokemon.getGender() == Gender.Female && !pokemon.isEgg()) {
            this.mc.field_71446_o.func_110577_a(GuiResources.female);
            GuiHelper.drawImageQuad((double)(this.fontRenderer.func_78256_a(displayName) + leftX + 29), (double)topY, 5.0, 8.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         }

         GuiHelper.bindPokeballTexture(pokemon.getCaughtBall());
         GuiHelper.drawImageQuad((double)(leftX - 3), (double)(topY - 7), 32.0, 32.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         if (pokemon.getHealth() <= 0) {
            this.mc.field_71446_o.func_110577_a(GuiResources.fainted);
         } else {
            this.mc.field_71446_o.func_110577_a(GuiResources.normal);
         }

         GuiHelper.drawImageQuad((double)(leftX - 3), (double)(topY - 7), 32.0, 32.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         GuiHelper.bindPokemonSprite(pokemon, this.mc);
         GuiHelper.drawImageQuad((double)(leftX + 1), (double)(topY - 6), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      }

      this.fontRenderer.func_78264_a(priorUniFlag);
   }

   public boolean isMouseOver(int leftX, int topY, int mouseX, int mouseY) {
      int left = leftX + this.leftOffset;
      int top = topY + this.topOffset - 5;
      return mouseX > left && mouseX < left + 110 && mouseY > top && mouseY < top + 25;
   }
}
