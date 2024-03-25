package com.pixelmonmod.pixelmon.quests.client.ui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.quests.client.ObjectiveDetail;
import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.quests.comm.AbandonQuest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class GuiQuests extends GuiScreen {
   private GuiButton random;
   private GuiButton craft;
   private GuiListQuest questList;
   private GuiListQuest completeList;
   private final boolean DEBUG;
   private boolean showActive;
   private int activeSeperator;
   private boolean init;
   private boolean abandoning;
   private int abandonColor;
   private boolean rising;

   public GuiQuests() {
      this.DEBUG = Pixelmon.devEnvironment;
      this.showActive = true;
      this.activeSeperator = 25;
      this.init = false;
      this.abandoning = false;
      this.abandonColor = -5767168;
      this.rising = true;
   }

   public void func_73866_w_() {
      this.field_146292_n.clear();
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = true;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      this.questList = new GuiListQuest(this.field_146297_k.field_71466_p, x + 7, y + 23, 104, 9);
      this.completeList = new GuiListQuest(this.field_146297_k.field_71466_p, x + 7, y + 27, 104, 9);
      this.refreshQuests();
      this.questList.setFocused(true);
   }

   public void refreshQuests() {
      this.questList.lines.clear();
      this.completeList.lines.clear();
      Iterator var1 = QuestDataClient.getInstance().getQuests().iterator();

      QuestProgressClient qpc;
      while(var1.hasNext()) {
         qpc = (QuestProgressClient)var1.next();
         if (qpc.getName() != null) {
            this.questList.addLine(qpc);
         }
      }

      var1 = QuestDataClient.getInstance().getCompleteQuests().iterator();

      while(var1.hasNext()) {
         qpc = (QuestProgressClient)var1.next();
         this.completeList.addLine(qpc);
      }

      this.questList.selectQuest(-1);
      this.completeList.selectQuest(-1);
   }

   public void func_146276_q_() {
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = 3;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int div = 2 * gap;
      int border = 25;
      int lowerBorder = dispHeight - border - div;
      float v = (float)(x + gap * 3) + 3.0F * (float)dispWidth / 4.0F;
      int sliderSpeed = true;
      QuestProgressClient clickedOnQuest;
      if (mouseX >= (int)((float)(x + gap * 3) + (float)dispWidth / 2.0F) + 1 && mouseX <= (int)((float)x + 3.0F * (float)dispWidth / 4.0F) && mouseY >= y + lowerBorder - div && mouseY <= y + dispHeight - div) {
         clickedOnQuest = null;
         if (this.questList.selectedQuest != null && this.questList.selectedQuest != -1) {
            clickedOnQuest = (QuestProgressClient)this.questList.lines.get(this.questList.selectedQuest);
         }

         if (clickedOnQuest != null) {
            boolean openEye = QuestDataClient.getInstance().getDisplayQuest() != clickedOnQuest;
            if (openEye) {
               QuestDataClient.getInstance().setDisplayQuest(clickedOnQuest);
            } else {
               QuestDataClient.getInstance().setDisplayQuest((QuestProgressClient)null);
            }
         }
      } else {
         if ((float)mouseX >= v && mouseX <= x + dispWidth - 2 && mouseY >= y + lowerBorder - div && mouseY <= y + dispHeight - div) {
            if (this.questList.selectedQuest != null && this.questList.selectedQuest != -1) {
               clickedOnQuest = (QuestProgressClient)this.questList.lines.get(this.questList.selectedQuest);
               if (clickedOnQuest.isAbandonable()) {
                  if (!this.abandoning) {
                     this.abandoning = true;
                  } else {
                     if (QuestDataClient.getInstance().getDisplayQuest() == clickedOnQuest) {
                        QuestDataClient.getInstance().setDisplayQuest((QuestProgressClient)null);
                     }

                     Pixelmon.network.sendToServer(new AbandonQuest(clickedOnQuest));
                  }
               }
            }

            return;
         }

         if (this.activeSeperator == border && mouseX > x - gap && mouseX < x - gap + dispWidth / 2 && mouseY > y && mouseY < y + this.activeSeperator && !this.showActive) {
            this.showActive = true;
            this.questList.setFocused(true);
            this.completeList.setFocused(false);
         } else if (this.activeSeperator == lowerBorder && mouseX > x - gap && mouseX < x - gap + dispWidth / 2 && mouseY > y + this.activeSeperator + div && mouseY < y + dispHeight && this.showActive) {
            this.showActive = false;
            this.completeList.setFocused(true);
            this.questList.setFocused(false);
         } else {
            int lineindex;
            if (this.questList.isFocused) {
               clickedOnQuest = this.questList.mouseClicked(mouseX, mouseY, 0, this.questList.yPos + 8);
               if (clickedOnQuest != null) {
                  lineindex = this.questList.lines.indexOf(clickedOnQuest);
                  this.completeList.selectQuest(-1);
                  this.questList.selectQuest(lineindex);
               }
            } else if (this.completeList.isFocused) {
               clickedOnQuest = this.completeList.mouseClicked(mouseX, mouseY, 0, this.completeList.yPos + 8);
               if (clickedOnQuest != null) {
                  lineindex = this.completeList.lines.indexOf(clickedOnQuest);
                  this.questList.selectQuest(-1);
                  this.completeList.selectQuest(lineindex);
               }
            }
         }
      }

      this.abandoning = false;
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      super.func_73863_a(mouseX, mouseY, f);
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.backgroundTexture);
      int gap = 3;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int div = 2 * gap;
      int border = 25;
      int lowerBorder = dispHeight - border - div;
      if (!this.init) {
         this.activeSeperator = dispHeight - border - div;
      }

      int sliderSpeed = 5;
      if (!this.showActive) {
         if (this.activeSeperator > border) {
            this.activeSeperator -= sliderSpeed;
            if (this.activeSeperator <= border) {
               this.activeSeperator = border;
            }
         }
      } else if (this.activeSeperator < lowerBorder) {
         this.activeSeperator += sliderSpeed;
         if (this.activeSeperator >= lowerBorder) {
            this.activeSeperator = lowerBorder;
         }
      }

      int qlh = this.activeSeperator - border - 12;
      if (qlh <= 2) {
         this.questList.isEnabled = false;
      } else {
         this.questList.isEnabled = true;
         this.questList.height = qlh;
         this.questList.displayLines = (int)(9.0F * ((float)qlh / (float)(lowerBorder - border - 12)));
      }

      float titleBorder = 8.5F;
      int clh = lowerBorder - 12 - this.activeSeperator;
      this.completeList.yPos = y + (int)titleBorder + this.activeSeperator + div + 16;
      if (clh <= 2) {
         this.completeList.isEnabled = false;
      } else {
         this.completeList.isEnabled = true;
         this.completeList.height = clh;
         this.completeList.displayLines = (int)(9.0F * ((float)clh / (float)(lowerBorder - 12 - border)));
      }

      Gui.func_146110_a(x - gap, y, 0.0F, 0.0F, dispWidth / 2, this.activeSeperator, (float)dispWidth / 2.0F, (float)this.activeSeperator);
      Gui.func_146110_a(x - gap, y + this.activeSeperator + div, 0.0F, 0.0F, dispWidth / 2, dispHeight - div - this.activeSeperator, (float)dispWidth / 2.0F, (float)(dispHeight - div - this.activeSeperator));
      Gui.func_146110_a(x + dispWidth / 2 + gap + 1, y, 0.0F, 0.0F, dispWidth / 2, dispHeight, (float)dispWidth / 2.0F, (float)dispHeight);
      String textA = TextFormatting.BOLD + I18n.func_135052_a("quest.ui.quests", new Object[0]);
      this.field_146289_q.func_175065_a(textA, (float)(x - gap + dispWidth / 4 - this.field_146289_q.func_78256_a(textA) / 2), (float)y + titleBorder, 16777215, true);
      String textB = TextFormatting.BOLD + I18n.func_135052_a("quest.ui.history", new Object[0]);
      this.field_146289_q.func_175065_a(textB, (float)(x - gap + dispWidth / 4 - this.field_146289_q.func_78256_a(textB) / 2), (float)y + titleBorder + (float)this.activeSeperator + (float)div, 16777215, true);
      if (this.questList.isEnabled) {
         this.questList.drawBackground();
         this.questList.drawText();
      }

      if (this.completeList.isEnabled) {
         this.completeList.drawBackground();
         this.completeList.drawText();
      }

      QuestProgressClient selectedQuest = null;
      if (this.questList.selectedQuest != null && this.questList.selectedQuest != -1) {
         selectedQuest = (QuestProgressClient)this.questList.lines.get(this.questList.selectedQuest);
      } else if (this.completeList.selectedQuest != null && this.completeList.selectedQuest != -1) {
         selectedQuest = (QuestProgressClient)this.completeList.lines.get(this.completeList.selectedQuest);
      }

      int resting;
      int alerting;
      if (selectedQuest != null) {
         String title = selectedQuest.getName();
         String desc = selectedQuest.getDesc();
         int width = (int)((float)dispWidth / 2.0F - 9.0F);
         resting = this.field_146289_q.func_78267_b(desc, width);
         alerting = this.field_146289_q.func_78256_a(title);
         double wf = 95.0;
         double finalWidth = Math.min((double)alerting, wf - 6.0);
         GuiHelper.drawSquashedString(this.field_146289_q, title, false, wf, (float)((int)((double)((float)(x + gap) + (float)dispWidth / 4.0F * 3.0F) - finalWidth / 2.0 + 6.0)), (float)((int)((float)y + titleBorder)), 16777215, true);
         this.field_146289_q.func_78279_b(desc, (int)((float)(x + gap * 3) + (float)dispWidth / 2.0F), (int)((float)y + titleBorder + 17.0F), width, 16777215);
         Iterator var30 = selectedQuest.getObjectives().iterator();

         while(var30.hasNext()) {
            ObjectiveDetail od = (ObjectiveDetail)var30.next();
            int channel = 5;
            String detail = selectedQuest.format(od);
            if (!detail.isEmpty()) {
               int detailHeight = this.field_146289_q.func_78267_b(detail, width - channel);
               this.field_146289_q.func_78279_b(detail, (int)((float)(x + gap * 3) + (float)dispWidth / 2.0F) + channel, (int)((float)y + titleBorder + 14.0F) + resting + this.field_146289_q.field_78288_b, width - channel, 16777215);
               resting += detailHeight;
            }
         }

         GlStateManager.func_179147_l();
         GlStateManager.func_179141_d();
         GlStateManager.func_179112_b(770, 771);
         this.field_146297_k.field_71446_o.func_110577_a(selectedQuest.isComplete() ? GuiResources.question_mark : selectedQuest.getIcon().getResource());
         if (selectedQuest.isComplete()) {
            GlStateManager.func_179131_c(0.8509804F, 0.8509804F, 0.8509804F, 1.0F);
         } else {
            QuestColor color = selectedQuest.getColor();
            GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
         }

         Gui.func_146110_a((int)((double)((float)(x + gap) + (float)dispWidth / 4.0F * 3.0F) - finalWidth / 2.0) - 11, (int)((float)y + titleBorder) - 4, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
      }

      int inactiveButtonColor = -9079435;
      boolean openEye = selectedQuest == null || QuestDataClient.getInstance().getDisplayQuest() != selectedQuest;
      func_73734_a((int)((float)(x + gap * 3) + (float)dispWidth / 2.0F) + 1, y + lowerBorder - div, (int)((float)x + 3.0F * (float)dispWidth / 4.0F), y + dispHeight - div, selectedQuest != null && !selectedQuest.isComplete() ? (openEye ? -12884481 : -10467329) : inactiveButtonColor);
      float v = (float)(x + gap * 3) + 3.0F * (float)dispWidth / 4.0F;
      resting = -5767168;
      alerting = -1179648;
      int diff = 65536;
      if (this.abandoning) {
         if (this.rising) {
            this.abandonColor += diff;
            if (this.abandonColor >= alerting) {
               this.rising = false;
            }
         } else {
            this.abandonColor -= diff;
            if (this.abandonColor <= resting) {
               this.rising = true;
            }
         }
      } else if (this.abandonColor > resting) {
         this.abandonColor -= diff;
         if (this.abandonColor <= resting) {
            this.abandonColor = resting;
         }
      } else {
         this.abandonColor = resting;
      }

      func_73734_a((int)v, y + lowerBorder - div, x + dispWidth - 2, y + dispHeight - div, selectedQuest != null && !selectedQuest.isComplete() && selectedQuest.isAbandonable() ? this.abandonColor : inactiveButtonColor);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      if (openEye) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.eyeTexture);
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.eyeClosedTexture);
      }

      Gui.func_146110_a((int)((float)(x + gap * 3) + (float)dispWidth / 2.0F) + 11, (int)((float)(y + lowerBorder) - (float)div * 1.55F), 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.crossTexture);
      Gui.func_146110_a((int)v + 11, (int)((float)(y + lowerBorder) - (float)div * 1.55F), 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      String leftButton = openEye ? I18n.func_135052_a("quest.ui.track", new Object[0]) : I18n.func_135052_a("quest.ui.hide", new Object[0]);
      this.field_146289_q.func_175065_a(leftButton, (float)((int)((float)(x + gap * 3) + (float)dispWidth / 2.0F) + 27) - (float)this.field_146289_q.func_78256_a(leftButton) / 2.0F, (float)((int)((float)(y + lowerBorder) - (float)div * 1.55F) + 26), 16777215, false);
      String rightButton = this.abandoning ? I18n.func_135052_a("quest.ui.abandoncheck", new Object[0]) : I18n.func_135052_a("quest.ui.abandon", new Object[0]);
      this.field_146289_q.func_175065_a(rightButton, (float)((int)v + 27) - (float)this.field_146289_q.func_78256_a(rightButton) / 2.0F, (float)((int)((float)(y + lowerBorder) - (float)div * 1.55F) + 26), 16777215, false);
      this.checkMouseWheel();
      if (this.DEBUG) {
         this.field_146289_q.func_78276_b("X: " + mouseX + " Y: " + mouseY, mouseX + 10, mouseY + 10, Color.red.getRGB());
      }

      if (!this.init) {
         this.init = true;
      }

   }

   private void checkMouseWheel() {
      int mousewheelDirection = Mouse.getDWheel();
      if (mousewheelDirection == 120) {
         if (this.questList.isFocused) {
            this.questList.scrollDown();
         } else if (this.completeList.isFocused) {
            this.completeList.scrollDown();
         }
      } else if (mousewheelDirection == -120) {
         if (this.questList.isFocused) {
            this.questList.scrollUp();
         } else if (this.completeList.isFocused) {
            this.completeList.scrollUp();
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_73876_c() {
   }
}
