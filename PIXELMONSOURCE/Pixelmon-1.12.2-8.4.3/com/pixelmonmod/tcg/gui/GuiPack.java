package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.helper.CardHelper;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPack extends GuiScreen {
   private int set;
   private List cards;
   private List viewed;
   private Cursor blankCursor;
   private List animationTicks;

   public GuiPack() {
   }

   public GuiPack(int set, ImmutableCard[] cards) {
      this.animationTicks = new ArrayList();

      for(int i = 0; i < 10; ++i) {
         this.animationTicks.add(0);
      }

      this.set = set;
      this.cards = Arrays.asList(cards);
      Collections.shuffle(this.cards);
      this.viewed = new ArrayList();

      try {
         this.blankCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), (IntBuffer)null);
      } catch (LWJGLException var4) {
      }

   }

   public void func_73866_w_() {
      GuiHelper.disableMinecraftUI();

      try {
         Mouse.setNativeCursor(this.blankCursor);
      } catch (LWJGLException var2) {
      }

   }

   public void func_146281_b() {
      GuiHelper.enableMinecraftUI();

      try {
         Mouse.setNativeCursor((Cursor)null);
      } catch (LWJGLException var2) {
      }

      Minecraft.func_71410_x().field_71474_y.field_74335_Z = TCGConfig.getInstance().savedUIScale;
      super.func_146281_b();
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int w = this.field_146294_l;
      int h = this.field_146295_m;
      float c = 1.5F;
      if (this.viewed.size() == this.cards.size()) {
         Minecraft.func_71410_x().field_71474_y.field_74335_Z = TCGConfig.getInstance().savedUIScale;
         this.field_146297_k.field_71439_g.func_71053_j();
      }

      for(int ii = 0; ii < this.cards.size(); ++ii) {
         float wWidth = 216.0F / (c * 2.0F);
         float hWidth = 335.0F / (c * 2.0F);
         float wStart = (float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(ii * 75);
         float hStart = (float)(h / 2) - 335.0F / (4.0F * c) - 57.5F;
         float wStart2 = (float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((ii - 5) * 75);
         float hStart2 = (float)(h / 2) - 335.0F / (4.0F * c) + 57.5F;
         if (ii < 5 && (float)mouseX > wStart && (float)mouseX < wStart + wWidth && (float)mouseY > hStart && (float)mouseY < hStart + hWidth && !this.viewed.contains(ii)) {
            this.viewed.add(ii);
            this.animationTicks.set(ii, 1);
         }

         if (ii >= 5 && (float)mouseX > wStart2 && (float)mouseX < wStart2 + wWidth && (float)mouseY > hStart2 && (float)mouseY < hStart2 + hWidth && !this.viewed.contains(ii)) {
            this.viewed.add(ii);
            this.animationTicks.set(ii, 1);
         }
      }

   }

   public void func_73863_a(int par1, int par2, float par3) {
      if (this.field_146297_k != null && this.field_146297_k.field_71446_o != null) {
         int w = this.field_146294_l;
         int h = this.field_146295_m;
         float z = this.field_73735_i;
         Item[] var10000 = new Item[]{TCG.itemPack};
         int animationTime = 120;
         float c = 1.5F;

         for(int i = 0; i < this.cards.size(); ++i) {
            CommonCardState card = new CommonCardState((ImmutableCard)this.cards.get(i));
            if (i < 5) {
               if (this.viewed.contains(i) && (Integer)this.animationTicks.get(i) > animationTime / 2) {
                  if ((Integer)this.animationTicks.get(i) < animationTime && (Integer)this.animationTicks.get(i) > 0) {
                     this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 1);
                     if ((Integer)this.animationTicks.get(i) > animationTime / 4 && (Integer)this.animationTicks.get(i) < animationTime / 4 * 3) {
                        this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 4);
                     }
                  }

                  GL11.glPushMatrix();
                  if ((Integer)this.animationTicks.get(i) > 0 && (Integer)this.animationTicks.get(i) < animationTime) {
                     GL11.glTranslated((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(i * 75) + 216.0F / (c * 2.0F) / 2.0F), (double)((float)(h / 2) - 335.0F / (4.0F * c)) - 57.5 + (double)(335.0F / (c * 2.0F)), 0.0);
                     GL11.glRotated((double)((Integer)this.animationTicks.get(i) * 3), 0.0, 1.0, 0.0);
                     GL11.glTranslated((double)(-((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(i * 75) + 216.0F / (c * 2.0F) / 2.0F)), -((double)((float)(h / 2) - 335.0F / (4.0F * c)) - 57.5 + (double)(335.0F / (c * 2.0F))), 0.0);
                  }

                  GL11.glTranslated((double)(w / 2 - 150 + i * 75), (double)(h / 2) - 57.5, 0.0);
                  GL11.glScaled(0.66666666, 0.66666666, 0.66666666);
                  CardHelper.drawCard(card, this.field_146297_k, 0, 0, this.field_73735_i, 1.0F, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
                  GL11.glScaled(-0.66666666, -0.66666666, -0.66666666);
                  GL11.glPopMatrix();
               } else {
                  if ((Integer)this.animationTicks.get(i) < animationTime && (Integer)this.animationTicks.get(i) > 0) {
                     this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 1);
                     if ((Integer)this.animationTicks.get(i) > animationTime / 4 && (Integer)this.animationTicks.get(i) < animationTime / 4 * 3) {
                        this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 4);
                     }
                  }

                  GL11.glPushMatrix();
                  if ((Integer)this.animationTicks.get(i) > 0 && (Integer)this.animationTicks.get(i) < animationTime) {
                     GL11.glTranslated((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(i * 75) + 216.0F / (c * 2.0F) / 2.0F), (double)((float)(h / 2) - 335.0F / (4.0F * c)) - 57.5 + (double)(335.0F / (c * 2.0F)), 0.0);
                     GL11.glRotated((double)((Integer)this.animationTicks.get(i) * 3), 0.0, 1.0, 0.0);
                     GL11.glTranslated((double)(-((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(i * 75) + 216.0F / (c * 2.0F) / 2.0F)), -((double)((float)(h / 2) - 335.0F / (4.0F * c)) - 57.5 + (double)(335.0F / (c * 2.0F))), 0.0);
                  }

                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  GlStateManager.func_179147_l();
                  GlStateManager.func_179141_d();
                  GlStateManager.func_179140_f();
                  this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/backs/default.png"));
                  GuiHelper.drawImageQuad((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)(i * 75)), (double)((float)(h / 2) - 335.0F / (4.0F * c)) - 57.5, (double)(216.0F / (c * 2.0F)), 335.0F / (c * 2.0F), 0.0, 0.0, 1.0, 1.0, z);
                  GL11.glPopMatrix();
                  GlStateManager.func_179145_e();
               }
            } else if (this.viewed.contains(i) && (Integer)this.animationTicks.get(i) > animationTime / 2) {
               if ((Integer)this.animationTicks.get(i) < animationTime && (Integer)this.animationTicks.get(i) > 0) {
                  this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 1);
                  if ((Integer)this.animationTicks.get(i) > animationTime / 4 && (Integer)this.animationTicks.get(i) < animationTime / 4 * 3) {
                     this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 4);
                  }
               }

               GL11.glPushMatrix();
               if ((Integer)this.animationTicks.get(i) > 0 && (Integer)this.animationTicks.get(i) < animationTime) {
                  GL11.glTranslated((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((i - 5) * 75) + 216.0F / (c * 2.0F) / 2.0F), (double)((float)(h / 2) - 335.0F / (4.0F * c)) + 57.5 + (double)(335.0F / (c * 2.0F)), 0.0);
                  GL11.glRotated((double)((Integer)this.animationTicks.get(i) * 3), 0.0, 1.0, 0.0);
                  GL11.glTranslated((double)(-((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((i - 5) * 75)) - 216.0F / (c * 2.0F) / 2.0F), -((double)((float)(h / 2) - 335.0F / (4.0F * c)) + 57.5) - (double)(335.0F / (c * 2.0F)), 0.0);
               }

               GL11.glTranslated((double)(w / 2 - 150 + (i - 5) * 75), (double)(h / 2) + 57.5, 0.0);
               GL11.glScaled(0.66666666, 0.66666666, 0.66666666);
               CardHelper.drawCard(card, this.field_146297_k, 0, 0, this.field_73735_i, 1.0F, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
               GL11.glScaled(-0.66666666, -0.66666666, -0.66666666);
               GL11.glPopMatrix();
            } else {
               if ((Integer)this.animationTicks.get(i) < animationTime && (Integer)this.animationTicks.get(i) > 0) {
                  this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 1);
                  if ((Integer)this.animationTicks.get(i) > animationTime / 4 && (Integer)this.animationTicks.get(i) < animationTime / 4 * 3) {
                     this.animationTicks.set(i, (Integer)this.animationTicks.get(i) + 4);
                  }
               }

               GL11.glPushMatrix();
               if ((Integer)this.animationTicks.get(i) > 0 && (Integer)this.animationTicks.get(i) < animationTime) {
                  GL11.glTranslated((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((i - 5) * 75) + 216.0F / (c * 2.0F) / 2.0F), (double)((float)(h / 2) - 335.0F / (4.0F * c)) + 57.5 + (double)(335.0F / (c * 2.0F)), 0.0);
                  GL11.glRotated((double)((Integer)this.animationTicks.get(i) * 3), 0.0, 1.0, 0.0);
                  GL11.glTranslated((double)(-((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((i - 5) * 75)) - 216.0F / (c * 2.0F) / 2.0F), -((double)((float)(h / 2) - 335.0F / (4.0F * c)) + 57.5) - (double)(335.0F / (c * 2.0F)), 0.0);
               }

               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179147_l();
               GlStateManager.func_179141_d();
               GlStateManager.func_179140_f();
               this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/backs/default.png"));
               GuiHelper.drawImageQuad((double)((float)(w / 2) - 216.0F / (4.0F * c) - 150.0F + (float)((i - 5) * 75)), (double)((float)(h / 2) - 335.0F / (4.0F * c)) + 57.5, (double)(216.0F / (c * 2.0F)), 335.0F / (c * 2.0F), 0.0, 0.0, 1.0, 1.0, z);
               GL11.glPopMatrix();
               GlStateManager.func_179145_e();
            }
         }

         GlStateManager.func_179140_f();
         if (Mouse.isButtonDown(0)) {
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursorPressed.png"));
         } else if (Mouse.isButtonDown(1)) {
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursorInspect.png"));
         } else {
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursor.png"));
         }

         GlStateManager.func_179141_d();
         GuiHelper.drawImageQuad((double)(par1 - 8), (double)(par2 - 8), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
         GlStateManager.func_179118_c();
      }

      GlStateManager.func_179101_C();
      RenderHelper.func_74518_a();
      GlStateManager.func_179140_f();
      GlStateManager.func_179097_i();
      GlStateManager.func_179140_f();
      GlStateManager.func_179145_e();
   }

   public boolean func_73868_f() {
      return false;
   }
}
