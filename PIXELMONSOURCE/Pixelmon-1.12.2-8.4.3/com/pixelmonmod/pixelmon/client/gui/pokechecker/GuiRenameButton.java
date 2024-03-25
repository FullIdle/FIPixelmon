package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRenameButton extends GuiButton {
   public GuiRenameButton(int buttonId, int x, int y, String buttonText) {
      super(buttonId, x, y, 49, 25, buttonText);
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146124_l) {
         mc.field_71446_o.func_110577_a(GuiResources.rename);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         if (this.func_146114_a(this.field_146123_n) == 2) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 0, 115, 49, this.field_146121_g);
         }

         int color = 16777215;
         if (!this.field_146124_l) {
            color = -6250336;
         } else if (this.field_146123_n) {
            color = 16777120;
         }

         this.func_73732_a(mc.field_71466_p, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + this.field_146121_g / 2 - 4, color);
      }

   }
}
