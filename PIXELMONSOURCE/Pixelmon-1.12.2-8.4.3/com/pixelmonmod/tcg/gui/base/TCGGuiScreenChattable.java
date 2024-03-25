package com.pixelmonmod.tcg.gui.base;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class TCGGuiScreenChattable extends TCGGuiScreen {
   private int bPosX = 0;
   private int bPosY = 0;
   private int bWidth = 0;
   public static boolean chatOpen = false;
   private int sentHistoryCursor = -1;
   protected GuiTextField inputField;
   private boolean field_73897_d;
   private int field_73903_n;
   private ArrayList onlineNames = Lists.newArrayList();
   private String field_73898_b;

   public TCGGuiScreenChattable() {
      chatOpen = false;
      this.inputField = new GuiTextField(0, this.field_146297_k.field_71466_p, 0, 0, 0, 0);
      this.inputField.func_146203_f(100);
      this.inputField.func_146185_a(false);
      this.inputField.func_146195_b(true);
      this.inputField.func_146180_a("");
      this.inputField.func_146205_d(false);
      this.inputField.func_146189_e(false);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.field_146297_k.field_71456_v.func_146158_b().func_146238_c().size();
      this.handleResize(this.field_146297_k);
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.inputField != null) {
         this.inputField.func_146178_a();
      }

   }

   private void handleResize(Minecraft mc) {
      ScaledResolution res = new ScaledResolution(mc);
      float guiScaling = (float)mc.field_71440_d / 240.0F / (float)res.func_78325_e();
      this.bPosX = 42;
      this.bPosY = this.field_146295_m - (int)(56.0F * guiScaling);
      this.bWidth = this.field_146294_l - 124;
      this.inputField.field_146209_f = this.bPosX + 2;
      this.inputField.field_146210_g = this.bPosY + 2;
      this.inputField.field_146218_h = this.bWidth - 2;
      this.inputField.field_146219_i = this.bPosY + 10;
   }

   public void func_73863_a(int par1, int par2, float par3) {
      super.func_73863_a(par1, par2, par3);
   }

   public void func_146281_b() {
      super.func_146281_b();
      Keyboard.enableRepeatEvents(false);
      this.field_146297_k.field_71456_v.func_146158_b().func_146240_d();
      chatOpen = false;
   }

   public void chatOpened(String start) {
      if (this.field_146297_k.field_71474_y.field_74343_n != EnumChatVisibility.HIDDEN && this.inputField != null) {
         if (start != null) {
            this.inputField.func_146180_a(start);
         } else {
            this.inputField.func_146180_a("");
         }

         chatOpen = true;
         this.inputField.func_146189_e(true);
      }
   }

   public void chatClosed() {
      chatOpen = false;
      this.inputField.func_146189_e(false);
      this.inputField.func_146180_a("");
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if ((keyCode == Minecraft.func_71410_x().field_71474_y.field_74310_D.func_151463_i() || keyCode == 53) && !chatOpen) {
         this.chatOpened(keyCode == 53 ? "/" : null);
      } else if (keyCode == 1 && chatOpen) {
         this.chatClosed();
      } else {
         if (chatOpen) {
            if (keyCode == 15) {
               this.completePlayerName();
            } else {
               this.field_73897_d = false;
            }

            if (keyCode != 28 && keyCode != 156) {
               if (keyCode == 200) {
                  this.getSentHistory(-1);
               } else if (keyCode == 208) {
                  this.getSentHistory(1);
               } else if (keyCode == 201) {
                  this.field_146297_k.field_71456_v.func_146158_b().func_146229_b(1);
               } else if (keyCode == 209) {
                  this.field_146297_k.field_71456_v.func_146158_b().func_146229_b(-1);
               } else {
                  this.inputField.func_146201_a(typedChar, keyCode);
               }
            } else {
               String s = this.inputField.func_146179_b().trim();
               if (!s.isEmpty()) {
                  this.field_146297_k.field_71456_v.func_146158_b().func_146239_a(s);
                  if (ClientCommandHandler.instance.func_71556_a(this.field_146297_k.field_71439_g, s) != 1) {
                     this.field_146297_k.field_71439_g.func_71165_d(s);
                  }
               }

               this.inputField.func_146180_a("");
               this.chatClosed();
            }
         }

         super.func_73869_a(typedChar, keyCode);
      }
   }

   public void func_146274_d() throws IOException {
      try {
         super.func_146274_d();
      } catch (NullPointerException var2) {
         if (PixelmonConfig.printErrors) {
            var2.printStackTrace();
         }

         return;
      }

      int i = Mouse.getEventDWheel();
      if (i != 0) {
         if (i > 1) {
            i = 1;
         }

         if (i < -1) {
            i = -1;
         }

         if (!func_146272_n()) {
            i *= 7;
         }

         this.field_146297_k.field_71456_v.func_146158_b().func_146229_b(i);
      }

   }

   public void completePlayerName() {
      String s;
      if (this.field_73897_d) {
         this.inputField.func_146175_b(this.inputField.func_146197_a(-1, this.inputField.func_146198_h(), false) - this.inputField.func_146198_h());
         if (this.field_73903_n >= this.onlineNames.size()) {
            this.field_73903_n = 0;
         }
      } else {
         int i = this.inputField.func_146197_a(-1, this.inputField.func_146198_h(), false);
         this.onlineNames.clear();
         this.field_73903_n = 0;
         this.inputField.func_146179_b().substring(i).toLowerCase();
         s = this.inputField.func_146179_b().substring(0, this.inputField.func_146198_h());
         if (this.onlineNames.isEmpty()) {
            return;
         }

         this.field_73897_d = true;
         this.inputField.func_146175_b(i - this.inputField.func_146198_h());
      }

      if (this.onlineNames.size() > 1) {
         StringBuilder stringbuilder = new StringBuilder();

         for(Iterator iterator = this.onlineNames.iterator(); iterator.hasNext(); stringbuilder.append(s)) {
            s = (String)iterator.next();
            if (stringbuilder.length() > 0) {
               stringbuilder.append(", ");
            }
         }

         this.field_146297_k.field_71456_v.func_146158_b().func_146234_a(new TextComponentString(stringbuilder.toString()), 1);
      }

      this.inputField.func_146191_b(TextFormatting.func_110646_a((String)this.onlineNames.get(this.field_73903_n++)));
   }

   public void getSentHistory(int par1) {
      int j = this.sentHistoryCursor + par1;
      int k = this.field_146297_k.field_71456_v.func_146158_b().func_146238_c().size();
      if (j < 0) {
         j = 0;
      }

      if (j > k) {
         j = k;
      }

      if (j != this.sentHistoryCursor) {
         try {
            if (j == k) {
               this.sentHistoryCursor = k;
               this.inputField.func_146180_a(this.field_73898_b);
            } else {
               if (this.sentHistoryCursor == k) {
                  this.field_73898_b = this.inputField.func_146179_b();
               }

               this.inputField.func_146180_a((String)this.field_146297_k.field_71456_v.func_146158_b().func_146238_c().get(j));
               this.sentHistoryCursor = j;
            }
         } catch (NullPointerException var5) {
         }
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      this.inputField.func_146192_a(par1, par2, par3);
      super.func_73864_a(par1, par2, par3);
   }

   public int getChatWidth() {
      return calculateChatboxWidth(this.field_146297_k.field_71474_y.field_96692_F);
   }

   public int getChatHeight() {
      return calculateChatboxHeight(this.field_146297_k.field_71474_y.field_96694_H);
   }

   public float getChatScale() {
      return this.field_146297_k.field_71474_y.field_96691_E;
   }

   public static int calculateChatboxWidth(float p_146233_0_) {
      short short1 = 320;
      byte b0 = 40;
      return MathHelper.func_76141_d(p_146233_0_ * (float)(short1 - b0) + (float)b0);
   }

   public static int calculateChatboxHeight(float p_146243_0_) {
      short short1 = 180;
      byte b0 = 20;
      return MathHelper.func_76141_d(p_146243_0_ * (float)(short1 - b0) + (float)b0);
   }

   public int getLineCount() {
      return this.getChatHeight() / 9;
   }
}
