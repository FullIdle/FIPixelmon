package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectPokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class TeamSelectPokemonIcon {
   private TeamSelectPokemon pokemon;
   private String disabled;
   private int x;
   private int y;
   private int tickOffset;
   public int selectIndex = -1;
   private static final int SIZE = 16;

   public TeamSelectPokemonIcon() {
   }

   public TeamSelectPokemonIcon(TeamSelectPokemon pokemon, String disabled) {
      this.pokemon = pokemon;
      this.disabled = disabled;
   }

   public TeamSelectPokemonIcon setPosition(int x, int y) {
      this.x = x;
      this.y = y;
      return this;
   }

   public void setTickOffset(int tickOffset) {
      this.tickOffset = tickOffset;
   }

   public void drawIcon(int mouseX, int mouseY, float zLevel, boolean fullSelected) {
      if (this.pokemon != null) {
         Minecraft mc = Minecraft.func_71410_x();
         int currentX = this.x + this.tickOffset;
         int currentY = this.y;
         boolean allowed = this.disabled.isEmpty();
         boolean mouseOver = this.isMouseOver(mouseX, mouseY);
         boolean isOpponent = "n".equals(this.disabled);
         boolean unselected = this.selectIndex == -1;
         ResourceLocation circle = null;
         if (!allowed) {
            if (!isOpponent) {
               circle = GuiResources.fainted;
            }
         } else if (mouseOver) {
            circle = GuiResources.selected;
         } else if (!unselected) {
            circle = GuiResources.released;
         }

         if (circle != null) {
            mc.field_71446_o.func_110577_a(circle);
            int circleDiff = 4;
            int circleSize = 16 + circleDiff;
            GuiHelper.drawImageQuad((double)(currentX - circleDiff / 2), (double)(this.y - circleDiff / 2), (double)circleSize, (float)circleSize, 0.0, 0.0, 1.0, 1.0, zLevel);
         }

         GuiHelper.bindPokemonSprite(this.pokemon.pokemon.pokemon, this.pokemon.pokemon.form, this.pokemon.pokemon.gender, this.pokemon.customTexture, this.pokemon.isShiny, this.pokemon.eggCycles, mc);
         double us = isOpponent ? 0.0 : 1.0;
         double ue = isOpponent ? 1.0 : 0.0;
         GuiHelper.drawImageQuad((double)currentX, (double)currentY, 16.0, 16.0F, us, 0.0, ue, 1.0, zLevel);
         if (!unselected) {
            mc.field_71466_p.func_78276_b(Integer.toString(this.selectIndex + 1), currentX + 8 - 3, this.y + 16 + 1, 0);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         }

         if (mouseOver && (!allowed || unselected && fullSelected)) {
            String reason = "";
            if (isOpponent) {
               reason = this.pokemon.pokemon.toBase().getLocalizedName();
            } else if (this.disabled.equals("e")) {
               reason = I18n.func_135052_a("gui.battlerules.egg", new Object[0]);
            } else if (this.disabled.equals("f")) {
               reason = I18n.func_135052_a("gui.battlerules.fainted", new Object[0]);
            } else if (!this.disabled.isEmpty() && !this.disabled.equals("n")) {
               reason = I18n.func_135052_a("gui.battlerules.clauseviolated", new Object[0]) + " " + BattleClause.getLocalizedName(this.disabled);
            } else if (fullSelected) {
               reason = I18n.func_135052_a("gui.battlerules.fullselect", new Object[0]);
            }

            if (!reason.isEmpty()) {
               GuiHelper.renderTooltip(mouseX + 10, mouseY, mc.field_71466_p.func_78271_c(reason, 100), Color.BLUE.getRGB(), Color.BLACK.getRGB(), 100, false, false);
            }
         }

      }
   }

   public boolean isMouseOver(int mouseX, int mouseY) {
      if (this.tickOffset == 0 && this.pokemon != null) {
         int selectOffset = 2;
         return mouseX >= this.x + selectOffset + 1 && mouseX <= this.x + 12 + selectOffset && mouseY >= this.y && mouseY <= this.y + 16;
      } else {
         return false;
      }
   }

   public boolean isDisabled() {
      return !this.disabled.isEmpty();
   }
}
