package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.HealerGuiClose;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiDoctor extends GuiScreen {
   private EnumGuiDoctorMode mode;
   private int flashCount;

   public GuiDoctor() {
      this.mode = GuiDoctor.EnumGuiDoctorMode.Before;
      this.flashCount = 0;
   }

   public void func_73863_a(int i, int j, float f) {
      int guiWidth = 300;
      int guiHeight = 60;
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.battleGui3);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - guiWidth / 2), (double)(this.field_146295_m - guiHeight), (double)guiWidth, (float)guiHeight, 0.0, 0.0, 1.0, 0.30416667461395264, this.field_73735_i);
      String s;
      if (this.mode == GuiDoctor.EnumGuiDoctorMode.Before) {
         s = I18n.func_135052_a("gui.guiDoctor.healPokemon", new Object[0]);
         this.func_73732_a(this.field_146297_k.field_71466_p, s, this.field_146294_l / 2, this.field_146295_m - 35, 16777215);
         ++this.flashCount;
         if (this.flashCount > 30) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.battleGui3);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 130), (double)(this.field_146295_m - 15), 10.0, 6.0F, 0.9546874761581421, 0.31041666865348816, 0.981249988079071, 0.33125001192092896, this.field_73735_i);
            if (this.flashCount > 60) {
               this.flashCount = 0;
            }
         }
      } else {
         s = I18n.func_135052_a("gui.guiDoctor.waiting", new Object[0]);
         ++this.flashCount;
         if (this.flashCount >= 160) {
            this.flashCount = 0;
         }

         if (this.flashCount < 40) {
            this.func_73732_a(this.field_146297_k.field_71466_p, s, this.field_146294_l / 2, this.field_146295_m - 35, 16777215);
         } else if (this.flashCount < 80) {
            this.func_73732_a(this.field_146297_k.field_71466_p, s + ".", this.field_146294_l / 2, this.field_146295_m - 35, 16777215);
         } else if (this.flashCount < 120) {
            this.func_73732_a(this.field_146297_k.field_71466_p, s + "..", this.field_146294_l / 2, this.field_146295_m - 35, 16777215);
         } else if (this.flashCount < 160) {
            this.func_73732_a(this.field_146297_k.field_71466_p, s + "...", this.field_146294_l / 2, this.field_146295_m - 35, 16777215);
         }
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      Pixelmon.network.sendToServer(new HealerGuiClose(I18n.func_135052_a("npc.model.doctor", new Object[0])));
   }

   public boolean func_73868_f() {
      return false;
   }

   static enum EnumGuiDoctorMode {
      Before,
      Healing,
      After;
   }
}
