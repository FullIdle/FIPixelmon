package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerMoves;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiButtonPokeChecker extends GuiButton {
   private GuiScreen parent;
   private PokemonStorage storage;
   private StoragePosition position;
   private Pokemon pokemon;
   private MiniButton summary = new MiniButton(0, I18n.func_135052_a("gui.screenpokechecker.summary", new Object[0]), 10, 15);
   private MiniButton moves = new MiniButton(1, I18n.func_135052_a("gui.screenpokechecker.moves", new Object[0]), 10, 34);
   private MiniButton stats = new MiniButton(2, I18n.func_135052_a("gui.screenpokechecker.stats", new Object[0]), 10, 53);

   public GuiButtonPokeChecker(GuiScreen parent) {
      super(-1, 0, 0, 67, 75, "");
      this.parent = parent;
      this.field_146124_l = false;
   }

   public void setPokemon(PokemonStorage storage, StoragePosition position, Pokemon pokemon, int mouseX, int mouseY) {
      this.storage = storage;
      this.position = position;
      this.pokemon = pokemon;
      this.field_146124_l = this.storage != null && this.position != null && this.pokemon != null;
      this.field_146128_h = mouseX - 73;
      this.field_146129_i = mouseY - 10;
      if (this.field_146129_i + this.field_146121_g > this.parent.field_146295_m) {
         this.field_146129_i = this.parent.field_146295_m - this.field_146121_g;
      }

   }

   public boolean func_146115_a() {
      return this.field_146124_l && this.field_146123_n;
   }

   private boolean isHovered(int mouseX, int mouseY) {
      return mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
   }

   public void mouseClicked(int mouseX, int mouseY) {
      InventoryPlayer inventory = this.parent.field_146297_k.field_71439_g.field_71071_by;
      ItemStack currentItem = inventory.func_70445_o();
      if (currentItem.func_190926_b()) {
         if (this.summary.hovered) {
            SoundHelper.playButtonPressSound();
            Minecraft.func_71410_x().func_147108_a(new GuiScreenPokeChecker(this.storage, this.position, this.parent));
         } else if (this.moves.hovered) {
            SoundHelper.playButtonPressSound();
            Minecraft.func_71410_x().func_147108_a(new GuiScreenPokeCheckerMoves(this.storage, this.position, this.parent));
         } else if (this.stats.hovered) {
            SoundHelper.playButtonPressSound();
            Minecraft.func_71410_x().func_147108_a(new GuiScreenPokeCheckerStats(this.storage, this.position, this.parent));
         }

      }
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146124_l) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = this.isHovered(mouseX, mouseY);
         mc.field_71446_o.func_110577_a(GuiResources.pokecheckerPopup);
         this.func_73729_b(this.field_146128_h, this.field_146129_i, 0, 0, this.field_146120_f, this.field_146121_g);
         this.summary.drawButton(mouseX, mouseY);
         this.moves.drawButton(mouseX, mouseY);
         this.stats.drawButton(mouseX, mouseY);
         this.func_73732_a(mc.field_71466_p, this.pokemon.getDisplayName(), this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + 2, 16777215);
         this.summary.drawText(mc);
         this.moves.drawText(mc);
         this.stats.drawText(mc);
      }

   }

   private class MiniButton {
      private static final int HOVER_WIDTH = 47;
      private static final int HOVER_HEIGHT = 13;
      public final int id;
      private int offsetX;
      private int offsetY;
      private String text;
      private int tempX;
      private int tempY;
      public boolean hovered;

      private MiniButton(int id, String text, int offsetX, int offsetY) {
         this.id = id;
         this.offsetX = offsetX;
         this.offsetY = offsetY;
         this.text = text;
      }

      public boolean isHovered(int mouseX, int mouseY) {
         return mouseX >= this.tempX && mouseY >= this.tempY && mouseX < this.tempX + 47 && mouseY < this.tempY + 13;
      }

      public void drawButton(int mouseX, int mouseY) {
         this.tempX = GuiButtonPokeChecker.this.field_146128_h + this.offsetX;
         this.tempY = GuiButtonPokeChecker.this.field_146129_i + this.offsetY;
         this.hovered = this.isHovered(mouseX, mouseY);
         if (this.hovered) {
            GuiButtonPokeChecker.this.func_73729_b(this.tempX, this.tempY, 1, 76, 47, 13);
         }

      }

      public void drawText(Minecraft mc) {
         GuiButtonPokeChecker.this.func_73732_a(mc.field_71466_p, this.text, this.tempX + 23, this.tempY + 6 - 4, this.hovered ? 16777120 : 16777215);
      }

      // $FF: synthetic method
      MiniButton(int x1, String x2, int x3, int x4, Object x5) {
         this(x1, x2, x3, x4);
      }
   }
}
