package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPokeCheckerTabs extends GuiButton {
   private int type;
   private Pokemon pokemon;

   public GuiPokeCheckerTabs(int type, int buttonId, int x, int y, int width, int height, String text) {
      super(buttonId, x, y, width, height, text);
      this.type = type;
   }

   public GuiPokeCheckerTabs(int type, int buttonId, int x, int y, int width, int height, String text, Pokemon pokemon) {
      super(buttonId, x, y, width, height, text);
      this.type = type;
      this.pokemon = pokemon;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.type == 5) {
            mc.field_71446_o.func_110577_a(GuiResources.yesNo);
         } else if (this.type == 6) {
            mc.field_71446_o.func_110577_a(GuiResources.pokecheckerPopup);
         } else {
            mc.field_71446_o.func_110577_a(GuiResources.summarySummary);
         }

         this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         if (this.field_146123_n) {
            if (this.type == 4) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 88, 228, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 7) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 79, 229, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 3) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 235, 205, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 2) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 164, 205, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 1) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 94, 205, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 0) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 2, 205, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 5) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 154, 102, this.field_146120_f, this.field_146121_g);
            } else if (this.type == 6) {
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 1, 76, this.field_146120_f, this.field_146121_g);
            }
         } else if (this.type == 4 && !this.pokemon.doesLevel()) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 88, 239, this.field_146120_f, this.field_146121_g);
         }

         if (this.type == 8) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 0, 221, this.field_146120_f, this.field_146121_g);
         } else if (this.type == 9) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 14, 221, this.field_146120_f, this.field_146121_g);
         }

         this.func_73732_a(mc.field_71466_p, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, !this.field_146124_l ? -6250336 : (this.field_146123_n ? 16777120 : 16777215));
      }

   }

   public void func_146113_a(SoundHandler soundHandlerIn) {
      SoundHelper.playButtonPressSound();
   }
}
