package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.HealerGuiClose;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiHealer extends GuiScreen {
   boolean isNurse;
   int textureIndex;

   public GuiHealer() {
      this.isNurse = false;
      this.textureIndex = 0;
   }

   public GuiHealer(int textureIndex) {
      this();
      this.isNurse = true;
      this.textureIndex = textureIndex;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public void func_73876_c() {
   }

   public void func_146284_a(GuiButton b) {
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_146282_l() {
   }

   public void func_73863_a(int i, int i1, float f) {
      if (this.isNurse) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.evo);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 120), (double)(this.field_146295_m / 4 - 40), 240.0, 80.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         String name;
         if (this.textureIndex == 1) {
            name = I18n.func_135052_a("gui.nursejoy.name", new Object[0]);
         } else {
            name = I18n.func_135052_a("gui.nursejohn.name", new Object[0]);
         }

         String chat = I18n.func_135052_a("gui.nursejoy.healing", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(name, this.field_146294_l / 2 - 107, this.field_146295_m / 4 - 32, 16777215);
         ArrayList chatText = new ArrayList();
         chatText.add(chat);

         for(int index = 0; index < chatText.size(); ++index) {
            String page;
            int li;
            for(; this.field_146297_k.field_71466_p.func_78256_a((String)chatText.get(index)) > 200; chatText.set(index, page.substring(0, li))) {
               page = (String)chatText.get(index);
               li = page.lastIndexOf(" ");
               if (index + 1 == chatText.size()) {
                  chatText.add(page.substring(li + 1));
               } else {
                  chatText.set(index + 1, page.substring(li + 1) + " " + (String)chatText.get(index + 1));
               }
            }

            this.field_146297_k.field_71466_p.func_78276_b((String)chatText.get(index), this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a((String)chatText.get(index)) / 2, this.field_146295_m / 4 - 20 + index * 14, 16777215);
         }
      }

      super.func_73863_a(i, i1, f);
   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_146281_b() {
      super.func_146281_b();
      Pixelmon.network.sendToServer(new HealerGuiClose(I18n.func_135052_a(this.isNurse ? "gui.nursejoy.name" : "tile.healer.name", new Object[0])));
   }
}
