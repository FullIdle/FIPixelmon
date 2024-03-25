package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.OpenPackPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiPackConfirm extends GuiScreen {
   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 128), (double)(this.field_146295_m / 2 - 50), 256.0, 100.0F, 0.0, 0.0, 1.0, 0.78125, this.field_73735_i);
      String s = "Are you sure you want";
      String s2 = "to open this pack?";
      String s3 = "Be sure to have enough";
      String s4 = "inventory space!";
      int wS = this.field_146297_k.field_71466_p.func_78256_a(s);
      int wS2 = this.field_146297_k.field_71466_p.func_78256_a(s2);
      int ws3 = this.field_146297_k.field_71466_p.func_78256_a(s3);
      int ws4 = this.field_146297_k.field_71466_p.func_78256_a(s4);
      this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - 31 - wS / 2, this.field_146295_m / 2 + 1 - 30, 0);
      this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - 32 - wS / 2, this.field_146295_m / 2 - 30, 16777215);
      this.field_146297_k.field_71466_p.func_78276_b(s2, this.field_146294_l / 2 - 31 - wS2 / 2, this.field_146295_m / 2 + 11 - 30, 0);
      this.field_146297_k.field_71466_p.func_78276_b(s2, this.field_146294_l / 2 - 32 - wS2 / 2, this.field_146295_m / 2 + 10 - 30, 16777215);
      this.field_146297_k.field_71466_p.func_78276_b(s3, this.field_146294_l / 2 - 31 - ws3 / 2, this.field_146295_m / 2 + 20 - 12, 0);
      this.field_146297_k.field_71466_p.func_78276_b(s3, this.field_146294_l / 2 - 32 - ws3 / 2, this.field_146295_m / 2 + 19 - 12, 16777215);
      this.field_146297_k.field_71466_p.func_78276_b(s4, this.field_146294_l / 2 - 31 - ws4 / 2, this.field_146295_m / 2 + 29 - 12, 0);
      this.field_146297_k.field_71466_p.func_78276_b(s4, this.field_146294_l / 2 - 32 - ws4 / 2, this.field_146295_m / 2 + 28 - 12, 16777215);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 - 33 && mouseY < this.field_146295_m / 2 - 7) {
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 - 33), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 + 5 && mouseY < this.field_146295_m / 2 + 31) {
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 + 5), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      String yes = I18n.func_135052_a("Yes", new Object[0]);
      String no = I18n.func_135052_a("No", new Object[0]);
      int wY = this.field_146297_k.field_71466_p.func_78256_a(yes);
      int wN = this.field_146297_k.field_71466_p.func_78256_a(no);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73731_b(this.field_146297_k.field_71466_p, yes, this.field_146294_l / 2 + 85 - wY / 2, this.field_146295_m / 2 - 23, 16777215);
      this.func_73731_b(this.field_146297_k.field_71466_p, no, this.field_146294_l / 2 + 86 - wN / 2, this.field_146295_m / 2 + 15, 16777215);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 - 33 && mouseY < this.field_146295_m / 2 - 7) {
         PacketHandler.net.sendToServer(new OpenPackPacket(0));
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      }

      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 + 5 && mouseY < this.field_146295_m / 2 + 31) {
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
