package com.pixelmonmod.pixelmon.client.gui.elements;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChatExtension {
   private int bPosX = 0;
   private int bPosY = 0;
   private int bWidth = 0;
   private int yOffset;
   public static boolean chatOpen = false;
   public boolean updateHeight = false;
   private int lastHeight = -1;
   private int sentHistoryCursor = -1;
   protected GuiTextField inputField;
   protected GuiChat.ChatTabCompleter tabCompleter;
   private boolean field_73897_d;
   private int field_73903_n;
   private ArrayList onlineNames = Lists.newArrayList();
   private String field_73898_b;
   private Minecraft mc = Minecraft.func_71410_x();
   private GuiScreen screen;

   public GuiChatExtension(GuiScreen screen, int yOffset) {
      this.screen = screen;
      this.yOffset = yOffset;
   }

   public void handleKeyboardInput() {
      if (Keyboard.getEventKeyState()) {
         int i = Keyboard.getEventKey();
         char c0 = Keyboard.getEventCharacter();
         this.keyTyped(c0, i);
      }

   }

   public void initGui() {
      this.bPosX = 2;
      this.bPosY = this.screen.field_146295_m - this.yOffset;
      this.bWidth = this.screen.field_146294_l - 124;
      chatOpen = false;
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.field_71456_v.func_146158_b().func_146238_c().size();
      this.inputField = new GuiTextField(0, this.mc.field_71466_p, this.bPosX + 2, this.bPosY + 2, this.bWidth - 2, this.screen.field_146295_m - 64);
      this.inputField.func_146203_f(100);
      this.inputField.func_146185_a(false);
      this.inputField.func_146195_b(true);
      this.inputField.func_146180_a("");
      this.inputField.func_146205_d(false);
      this.inputField.func_146189_e(false);
      this.tabCompleter = new GuiChat.ChatTabCompleter(this.inputField);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.field_71456_v.func_146158_b().func_146240_d();
      chatOpen = false;
   }

   public void updateScreen(int height) {
      if (this.inputField != null) {
         this.inputField.func_146178_a();
      }

      if (this.updateHeight) {
         if (this.lastHeight != height) {
            this.yOffset = height > 900 ? 140 : 125;
            this.initGui();
         }

         this.lastHeight = height;
      }

   }

   public boolean isChatOpen() {
      return chatOpen;
   }

   public void drawScreen(int par1, int par2, float par3) {
      if (chatOpen) {
         Gui.func_73734_a(2, this.screen.field_146295_m - this.yOffset, this.screen.field_146294_l - 122, this.screen.field_146295_m - this.yOffset + 12, Integer.MIN_VALUE);
         this.inputField.func_146194_f();
      }

      if (this.mc != null && this.mc.field_71456_v != null) {
         GuiNewChat guiNewChat = this.mc.field_71456_v.func_146158_b();
         int j = this.getLineCount();
         int k = 0;
         int l = guiNewChat.field_146253_i.size();
         float f = this.mc.field_71474_y.field_74357_r * 0.9F + 0.1F;
         int updateCounter = this.mc.field_71456_v.func_73834_c();
         if (l > 0) {
            float f1 = this.getChatScale();
            int i1 = MathHelper.func_76123_f((float)this.getChatWidth() / f1);
            GlStateManager.func_179094_E();
            if (chatOpen) {
               GlStateManager.func_179109_b(2.0F, (float)(this.bPosY - 3), 0.0F);
            } else {
               GlStateManager.func_179109_b(2.0F, (float)(this.bPosY + 10), 0.0F);
            }

            GlStateManager.func_179152_a(f1, f1, 1.0F);

            int j1;
            int k1;
            int l1;
            int b0;
            for(j1 = 0; j1 + guiNewChat.field_146250_j < guiNewChat.field_146253_i.size() && j1 < j; ++j1) {
               ChatLine chatline = (ChatLine)guiNewChat.field_146253_i.get(j1 + guiNewChat.field_146250_j);
               if (chatline != null) {
                  k1 = updateCounter - chatline.func_74540_b();
                  if (k1 < 200 || chatOpen) {
                     double d0 = (double)k1 / 200.0;
                     d0 = 1.0 - d0;
                     d0 *= 10.0;
                     if (d0 < 0.0) {
                        d0 = 0.0;
                     }

                     if (d0 > 1.0) {
                        d0 = 1.0;
                     }

                     d0 *= d0;
                     l1 = (int)(255.0 * d0);
                     if (chatOpen) {
                        l1 = 255;
                     }

                     l1 = (int)((float)l1 * f);
                     ++k;
                     if (l1 > 3) {
                        b0 = 0;
                        int i2 = -j1 * 9;
                        Gui.func_73734_a(b0, i2 - 9, b0 + i1 + 4, i2, l1 / 2 << 24);
                        String s = chatline.func_151461_a().func_150254_d();
                        if (!this.mc.field_71474_y.field_74344_o) {
                           s = StringUtils.func_76338_a(s);
                        }

                        GlStateManager.func_179147_l();
                        this.mc.field_71466_p.func_175063_a(s, (float)b0, (float)(i2 - 8), 16777215 + (l1 << 24));
                        GlStateManager.func_179118_c();
                        GlStateManager.func_179084_k();
                     }
                  }
               }
            }

            if (chatOpen) {
               j1 = this.mc.field_71466_p.field_78288_b;
               GlStateManager.func_179109_b(-3.0F, 0.0F, 0.0F);
               int j2 = l * j1 + l;
               k1 = k * j1 + k;
               int k2 = guiNewChat.field_146250_j * k1 / l;
               int l2 = k1 * k1 / j2;
               if (j2 != k1) {
                  l1 = k2 > 0 ? 170 : 96;
                  b0 = guiNewChat.field_146251_k ? 13382451 : 3355562;
                  Gui.func_73734_a(0, -k2, 2, -k2 - l2, b0 + (l1 << 24));
                  Gui.func_73734_a(2, -k2, 1, -k2 - l2, 13421772 + (l1 << 24));
               }
            }

            GlStateManager.func_179121_F();
         }

      }
   }

   private void chatOpened(String start) {
      if (this.mc.field_71474_y.field_74343_n != EnumChatVisibility.HIDDEN && this.inputField != null) {
         if (start != null) {
            this.inputField.func_146180_a(start);
         } else {
            this.inputField.func_146180_a("");
         }

         chatOpen = true;
         this.inputField.func_146189_e(true);
      }
   }

   private void chatClosed() {
      chatOpen = false;
      this.inputField.func_146189_e(false);
      this.inputField.func_146180_a("");
   }

   public void keyTyped(char par1, int par2) {
      if ((par2 == this.mc.field_71474_y.field_74310_D.func_151463_i() || par2 == 53) && !chatOpen) {
         this.chatOpened(par2 == 53 ? "/" : null);
      } else if (par2 == 1 && chatOpen) {
         this.chatClosed();
      } else if (chatOpen) {
         if (par2 != 28 && par2 != 156) {
            if (par2 == 200) {
               this.getSentHistory(-1);
            } else if (par2 == 208) {
               this.getSentHistory(1);
            } else if (par2 == 201) {
               this.mc.field_71456_v.func_146158_b().func_146229_b(1);
            } else if (par2 == 209) {
               this.mc.field_71456_v.func_146158_b().func_146229_b(-1);
            } else {
               this.inputField.func_146201_a(par1, par2);
            }
         } else {
            String s = this.inputField.func_146179_b().trim();
            if (!s.isEmpty()) {
               this.mc.field_71456_v.func_146158_b().func_146239_a(s);
               if (ClientCommandHandler.instance.func_71556_a(this.mc.field_71439_g, s) != 1) {
                  this.mc.field_71439_g.func_71165_d(s);
               }
            }

            this.inputField.func_146180_a("");
            this.chatClosed();
         }

         this.tabCompleter.func_186843_d();
         if (par2 == 15) {
            this.tabCompleter.func_186841_a();
         } else {
            this.tabCompleter.func_186842_c();
         }

      }
   }

   public void handleMouseInput() throws IOException {
      int i = Mouse.getEventDWheel();
      if (i != 0) {
         if (i > 1) {
            i = 1;
         }

         if (i < -1) {
            i = -1;
         }

         if (!GuiScreen.func_146272_n()) {
            i *= 7;
         }

         this.mc.field_71456_v.func_146158_b().func_146229_b(i);
      }

   }

   private void completePlayerName() {
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
         s = this.inputField.func_146179_b().substring(0, this.inputField.func_146198_h());
         this.autoComplete(s);
         if (this.onlineNames.isEmpty()) {
            return;
         }

         this.field_73897_d = true;
         this.inputField.func_146175_b(i - this.inputField.func_146198_h());
      }

      if (this.onlineNames.size() > 1) {
         StringBuilder stringBuilder = new StringBuilder();

         for(Iterator iterator = this.onlineNames.iterator(); iterator.hasNext(); stringBuilder.append(s)) {
            s = (String)iterator.next();
            if (stringBuilder.length() > 0) {
               stringBuilder.append(", ");
            }
         }

         this.mc.field_71456_v.func_146158_b().func_146234_a(new TextComponentString(stringBuilder.toString()), 1);
      }

      this.inputField.func_146191_b(TextFormatting.func_110646_a((String)this.onlineNames.get(this.field_73903_n++)));
   }

   private void autoComplete(String par1Str) {
      if (par1Str.length() >= 1) {
         ClientCommandHandler.instance.autoComplete(par1Str);
         this.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketTabComplete(par1Str, (BlockPos)null, false));
      }

   }

   private void getSentHistory(int par1) {
      int j = this.sentHistoryCursor + par1;
      int k = this.mc.field_71456_v.func_146158_b().func_146238_c().size();
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

               this.inputField.func_146180_a((String)this.mc.field_71456_v.func_146158_b().func_146238_c().get(j));
               this.sentHistoryCursor = j;
            }
         } catch (NullPointerException var5) {
         }
      }

   }

   public void mouseClicked(int par1, int par2, int par3) throws IOException {
      this.inputField.func_146192_a(par1, par2, par3);
   }

   private int getChatWidth() {
      return calculateChatboxWidth(this.mc.field_71474_y.field_96692_F);
   }

   private int getChatHeight() {
      return calculateChatboxHeight(this.mc.field_71474_y.field_96694_H);
   }

   private float getChatScale() {
      return this.mc.field_71474_y.field_96691_E;
   }

   private static int calculateChatboxWidth(float p_146233_0_) {
      short short1 = 320;
      byte b0 = 40;
      return MathHelper.func_76141_d(p_146233_0_ * (float)(short1 - b0) + (float)b0);
   }

   private static int calculateChatboxHeight(float p_146243_0_) {
      short short1 = 180;
      byte b0 = 20;
      return MathHelper.func_76141_d(p_146243_0_ * (float)(short1 - b0) + (float)b0);
   }

   private int getLineCount() {
      return this.getChatHeight() / 9;
   }

   public void setCompletions(String... newCompletions) {
      this.tabCompleter.func_186840_a(newCompletions);
   }
}
