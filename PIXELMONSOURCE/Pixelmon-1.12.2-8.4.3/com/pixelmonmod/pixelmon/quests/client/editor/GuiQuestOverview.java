package com.pixelmonmod.pixelmon.quests.client.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiSimpleButton;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiTextField;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

public class GuiQuestOverview extends GuiScreen {
   private List buttons = new ArrayList();
   private GuiTextField questName;
   private String oldName;
   private int globalQuestOption = -1;
   private MouseState mouseState;
   private Stage draggedStage;
   private Stage hoverStage;
   private int ticker;
   private final int[] addStageButtonLoc;
   private boolean changingHue;
   private boolean changingColor;
   private float[] hsb;
   private int hoveringColor;
   private boolean reload;

   public GuiQuestOverview() {
      this.mouseState = GuiQuestOverview.MouseState.HOVER;
      this.draggedStage = null;
      this.hoverStage = null;
      this.ticker = 0;
      this.addStageButtonLoc = new int[]{0, 0, 0, 0};
      this.changingHue = false;
      this.changingColor = false;
      this.hsb = null;
      this.hoveringColor = -1;
      this.reload = false;
      this.oldName = QuestEditorState.get().getSelectedQuest().getFilename();
   }

   public void func_73866_w_() {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = 3;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      float titleBorder = 5.5F;
      this.questName = new GuiTextField(this.field_146297_k.field_71466_p, (int)((float)(x + gap) + (float)dispWidth / 2.0F) - 23, (int)((float)y + titleBorder), 115, 14, -6908266, -7895161);
      this.questName.setTextColor(Color.WHITE.getRGB());
      Quest quest = QuestEditorState.get().getSelectedQuest();
      if (quest != null) {
         this.questName.setText(quest.getPrintableName());
      }

      this.addButtons();
   }

   public void addButtons() {
      this.buttons.clear();
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int id = 0;
      int buttonS = 14;
      this.buttons.add(new GuiSimpleButton(id++, x + 4 + (buttonS + 6) * 0, y + 6, 12, GuiResources.back, 2, 2, -12407307));
      this.buttons.add(new GuiSimpleButton(id++, x + 4 + (buttonS + 6) * 1, y + 6, 12, GuiResources.save, 2, 2, -14300093));
      this.buttons.add(new GuiSimpleButton(id++, x + 4 + (buttonS + 6) * 2, y + 6, 12, GuiResources.discard, 2, 2, -2210481));
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      if (QuestEditorState.get().hasQuestSelected()) {
         Quest quest = QuestEditorState.get().getSelectedQuest();
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
         Gui.func_146110_a(x - gap, y, 0.0F, 0.0F, dispWidth + gap * 2 + 1, dispHeight, (float)dispWidth, (float)dispHeight);
         this.drawColorPicker(dispWidth, dispHeight, mouseX, mouseY);
         GlStateManager.func_179147_l();
         GlStateManager.func_179141_d();
         GlStateManager.func_179112_b(770, 771);
         float titleBorder = 8.5F;
         this.field_146297_k.field_71446_o.func_110577_a(((Stage)quest.getStages().get(quest.getStages().size() - 1)).getStage() == quest.getActiveStage() ? GuiResources.question_mark : GuiResources.exclamation_mark);
         QuestColor color = quest.getColor();
         GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
         Gui.func_146110_a((int)((float)(x + gap) + (float)dispWidth / 2.0F) - 41, (int)((float)y + titleBorder) - 4, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
         this.drawStages(x, y, mouseX, mouseY);
         float textX = (float)x + (float)dispWidth / 4.0F * 3.0F - 2.0F;
         float textY = (float)(y + 130);
         float spacing = 14.0F;
         int buttonSize = 40;
         int tickerColor = this.ticker % 200 < 100 ? 0 : 16777215;
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.questeditor.weight", new Object[]{String.valueOf(quest.getWeight())}), textX, textY, this.globalQuestOption == 0 ? tickerColor : 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a(quest.isAbandonable() ? "gui.questeditor.abandonable" : "gui.questeditor.notabandonable", new Object[0]), textX, textY + spacing, this.globalQuestOption == 1 ? tickerColor : 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a(quest.isRepeatable() ? "gui.questeditor.repeatable" : "gui.questeditor.notrepeatable", new Object[0]), textX, textY + spacing * 2.0F, this.globalQuestOption == 2 ? tickerColor : 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.questeditor.edittext", new Object[0]), textX, textY + spacing * 3.0F, this.globalQuestOption == 3 ? tickerColor : 0);
         boolean optionHovering = false;
         if ((float)mouseX >= textX - (float)buttonSize && (float)mouseX <= textX + (float)buttonSize) {
            for(int i = 0; i < 4; ++i) {
               if ((float)mouseY >= textY + spacing * (float)i && (float)mouseY <= textY + spacing * (float)i + 6.0F) {
                  optionHovering = true;
                  this.globalQuestOption = i;
               }
            }
         }

         if (!optionHovering) {
            this.globalQuestOption = -1;
         }

         GlStateManager.func_179084_k();
         GlStateManager.func_179118_c();
         Iterator var22 = this.buttons.iterator();

         while(var22.hasNext()) {
            GuiSimpleButton button = (GuiSimpleButton)var22.next();
            button.draw();
         }

         this.questName.drawTextBox();
         this.checkMouseWheel(mouseX, mouseY);
      }
   }

   private void drawStages(int x, int y, int mouseX, int mouseY) {
      boolean mouseDown = Mouse.isButtonDown(0);
      if (mouseDown) {
         if (this.mouseState == GuiQuestOverview.MouseState.HOVER) {
            this.mouseState = GuiQuestOverview.MouseState.PRESS;
         } else if (this.mouseState == GuiQuestOverview.MouseState.PRESS) {
            this.mouseState = GuiQuestOverview.MouseState.DRAG;
         }
      } else if (this.mouseState != GuiQuestOverview.MouseState.PRESS && this.mouseState != GuiQuestOverview.MouseState.DRAG) {
         if (this.mouseState == GuiQuestOverview.MouseState.RELEASE) {
            this.mouseState = GuiQuestOverview.MouseState.HOVER;
            this.draggedStage = null;
         }
      } else {
         this.mouseState = GuiQuestOverview.MouseState.RELEASE;
      }

      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      Quest quest = QuestEditorState.get().getSelectedQuest();
      int[] editDropZone = new int[]{x + dispWidth / 2 - 50, y + 90, x + dispWidth / 2 - 5, y + 110};
      int[] deleteDropZone = new int[]{x + dispWidth / 2 + 5, y + 90, x + dispWidth / 2 + 50, y + 110};
      boolean isHoverEdit = this.draggedStage != null && mouseX >= editDropZone[0] && mouseY >= editDropZone[1] && mouseX <= editDropZone[2] && mouseY <= editDropZone[3];
      boolean isHoverDelete = quest.getStages().size() > 1 && this.draggedStage != null && mouseX >= deleteDropZone[0] && mouseY >= deleteDropZone[1] && mouseX <= deleteDropZone[2] && mouseY <= deleteDropZone[3];
      func_73734_a(editDropZone[0], editDropZone[1], editDropZone[2], editDropZone[3], !isHoverEdit && (this.draggedStage == null || this.ticker % 200 >= 100) ? -10066330 : -16727066);
      func_73734_a(deleteDropZone[0], deleteDropZone[1], deleteDropZone[2], deleteDropZone[3], !isHoverDelete && (quest.getStages().size() <= 1 || this.draggedStage == null || this.ticker % 200 >= 100) ? -10066330 : -2210481);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.questeditor.editstage", new Object[0]), (float)(editDropZone[0] + editDropZone[2]) / 2.0F, (float)(y + 96), 16777215, true);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.questeditor.removestage", new Object[0]), (float)(deleteDropZone[0] + deleteDropZone[2]) / 2.0F, (float)(y + 96), 16777215, true);
      int stageCount = quest.getStages().size();
      int gap = dispWidth / (stageCount + 1);
      if (this.draggedStage != null && this.mouseState == GuiQuestOverview.MouseState.RELEASE) {
         if (isHoverEdit) {
            this.field_146297_k.func_147108_a(new GuiQuestStage(this.draggedStage));
         } else if (isHoverDelete) {
            Iterator var17 = quest.getStages().iterator();

            while(var17.hasNext()) {
               Stage stage = (Stage)var17.next();
               if (stage.getNextStage() == this.draggedStage.getStage()) {
                  stage.setNextStage(this.draggedStage.getNextStage());
               }
            }

            quest.getStages().remove(this.draggedStage);
            quest.clearDefaultStrings(this.draggedStage.getStage(), this.draggedStage.getRawObjectives().size());
         }
      }

      boolean hovering = false;
      int index = 1;

      for(Iterator var19 = quest.getStages().iterator(); var19.hasNext(); ++index) {
         Stage stage = (Stage)var19.next();
         boolean mouseOver = this.drawStage(stage, quest.getNextStage(stage.getStage()), this.draggedStage == stage, x + gap * index, y + 45, gap, mouseX, mouseY);
         if (mouseOver) {
            hovering = true;
            this.hoverStage = stage;
            if (this.mouseState == GuiQuestOverview.MouseState.PRESS) {
               this.draggedStage = stage;
            } else if (this.mouseState == GuiQuestOverview.MouseState.RELEASE) {
               if (this.draggedStage != null) {
                  Stage a = this.draggedStage;
                  ArrayList oa = new ArrayList(a.getRawObjectives());
                  ArrayList aa = new ArrayList(a.getRawActions());
                  ArrayList ob = new ArrayList(stage.getRawObjectives());
                  ArrayList ab = new ArrayList(stage.getRawActions());
                  a.getRawObjectives().clear();
                  a.getRawObjectives().addAll(ob);
                  a.getRawActions().clear();
                  a.getRawActions().addAll(ab);
                  stage.getRawObjectives().clear();
                  stage.getRawObjectives().addAll(oa);
                  stage.getRawActions().clear();
                  stage.getRawActions().addAll(aa);
               }

               this.draggedStage = null;
            }
         }
      }

      if (!hovering) {
         this.hoverStage = null;
      }

      ++this.ticker;
   }

   private boolean drawStage(Stage stage, Stage nextStage, boolean dragged, int x, int y, int gap, int mouseX, int mouseY) {
      int width = 6;
      int height = 30;
      int left = x - width / 2 - 1;
      int top = y - 1;
      int right = x + width + 1;
      int bottom = y + height + 2;
      if (dragged) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(0.0F, 0.0F, 2.0F);
      }

      func_73734_a(left, top, right, bottom, -16777216);
      GuiHelper.drawCenteredString(String.valueOf(stage.getStage()), (float)(x + 2), (float)(y - 10), this.hoverStage == stage && this.draggedStage == null ? (this.ticker % 200 < 100 ? 0 : 16777215) : 0);
      boolean activeStage = QuestEditorState.get().getSelectedQuest().getActiveStage() == stage.getStage();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(activeStage ? GuiResources.eyeTexture : GuiResources.eyeClosedTexture);
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      if (activeStage) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         float grey = 0.4F;
         GlStateManager.func_179131_c(grey, grey, grey, 1.0F);
         if (this.mouseState == GuiQuestOverview.MouseState.PRESS && mouseX >= x - 4 && mouseX <= x + 7 && mouseY >= y + height + 1 && mouseY <= y + height + 15) {
            QuestEditorState.get().getSelectedQuest().setActiveStage(stage.getStage());
         }
      }

      Gui.func_146110_a(x - 4, y + height + 1, 0.0F, 0.0F, 11, 11, 11.0F, 11.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (stage.getStage() == 0) {
         func_73734_a(x - 10, y + height / 2 - 1, x - 4, y + height / 2 + 2, -1);
         this.addStageButtonLoc[0] = x - 19;
         this.addStageButtonLoc[1] = y + height / 2 - 4;
         this.addStageButtonLoc[2] = x - 10;
         this.addStageButtonLoc[3] = y + height / 2 + 5;
         func_73734_a(this.addStageButtonLoc[0], this.addStageButtonLoc[1], this.addStageButtonLoc[2], this.addStageButtonLoc[3], -1);
         func_73734_a(x - 17, y + height / 2, x - 12, y + height / 2 + 1, -16777216);
         func_73734_a(x - 15, y + height / 2 - 2, x - 14, y + height / 2 + 3, -16777216);
      }

      if (nextStage != null) {
         func_73734_a(x + 7, y + height / 2 - 1, x + gap - 4, y + height / 2 + 2, -1);
      } else {
         func_73734_a(x + 7, y + height / 2 - 1, x + 13, y + height / 2 + 2, -1);
         func_73734_a(x + 13, y + height / 2 - 3, x + 15, y + height / 2 + 4, -1);
      }

      if (dragged) {
         x = mouseX;
         y = mouseY - height / 2;
      }

      func_73734_a(x - width / 2 - 1, y - 1, x + width + 1, y + height + 2, -16777216);
      func_73734_a(x - width / 2, y, x + width, y + height / 2, -17664);
      func_73734_a(x - width / 2, y + height / 2 + 1, x + width, y + height + 1, -5479425);
      GuiHelper.drawCenteredString(String.valueOf(stage.getRawObjectives().size()), (float)(x + 2), (float)y + (float)height / 2.0F - 11.0F, 0);
      GuiHelper.drawCenteredString(String.valueOf(stage.getRawActions().size()), (float)(x + 2), (float)(y + height - 10), 0);
      if (dragged) {
         GlStateManager.func_179121_F();
      }

      return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
   }

   private void drawColorPicker(int dispWidth, int dispHeight, int mouseX, int mouseY) {
      Quest quest = QuestEditorState.get().getSelectedQuest();
      if (this.hsb == null) {
         this.hsb = new float[3];
         Color.RGBtoHSB(quest.getColor().getR(), quest.getColor().getG(), quest.getColor().getB(), this.hsb);
      }

      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int left = x + 5;
      int top = y + 117;
      int width = 100;
      int height = 55;

      int newColor;
      for(newColor = 0; newColor < width; ++newColor) {
         float v1 = (float)newColor / (float)width;
         float v2 = (float)((double)newColor + 0.5) / (float)width;
         GuiHelper.drawGradientRect(left + newColor, top, 1.0F, left + newColor + 1, top + height, Color.HSBtoRGB(this.hsb[0], v1, 1.0F), Color.BLACK.getRGB(), false);
         GuiHelper.drawGradientRect(left + newColor, top + height + 4, 1.0F, left + newColor + 1, top + height + 12, Color.HSBtoRGB(v1, 1.0F, 1.0F), Color.HSBtoRGB(v2, 1.0F, 1.0F), true);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      newColor = Color.HSBtoRGB(this.hsb[0], this.hsb[1], this.hsb[2]);
      Color color = new Color(newColor);
      func_73734_a(left + width + 3, top, left + width + 13, top + height + 23, -16777216);
      func_73734_a(left + width + 3 + 1, top + 1, left + width + 13 - 1, top + height + 23 - 1, newColor);
      int tickerColor = this.ticker % 200 < 100 ? 0 : 16777215;
      int rgbSpacing = 37;
      this.field_146289_q.func_78276_b("R", left, top + height + 16, 0);
      this.field_146289_q.func_78276_b(String.valueOf(color.getRed()), left + 8, top + height + 16, this.hoveringColor == 0 ? tickerColor : 0);
      this.field_146289_q.func_78276_b("G", left + rgbSpacing, top + height + 16, 0);
      this.field_146289_q.func_78276_b(String.valueOf(color.getGreen()), left + rgbSpacing + 8, top + height + 16, this.hoveringColor == 1 ? tickerColor : 0);
      this.field_146289_q.func_78276_b("B", left + rgbSpacing * 2, top + height + 16, 0);
      this.field_146289_q.func_78276_b(String.valueOf(color.getBlue()), left + rgbSpacing * 2 + 8, top + height + 16, this.hoveringColor == 2 ? tickerColor : 0);
      if (this.mouseState == GuiQuestOverview.MouseState.PRESS) {
         if (mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + height) {
            this.changingColor = true;
            CursorHelper.setCursor(CursorHelper.TRANSPARENT_CURSOR);
         } else if (mouseX >= left && mouseX <= left + width && mouseY >= top + height + 4 && mouseY <= top + height + 12) {
            this.changingHue = true;
            CursorHelper.setCursor(CursorHelper.TRANSPARENT_CURSOR);
         }
      } else if (this.mouseState == GuiQuestOverview.MouseState.RELEASE) {
         if (this.changingColor || this.changingHue) {
            CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
         }

         this.changingColor = false;
         this.changingHue = false;
      } else {
         int i;
         if (this.mouseState != GuiQuestOverview.MouseState.DRAG || !this.changingColor && !this.changingHue) {
            boolean set = false;

            for(i = 0; i < 3; ++i) {
               if (mouseX >= left + rgbSpacing * i && mouseX <= left + rgbSpacing * i + 24 && mouseY >= top + height + 16 && mouseY <= top + height + 16 + 6) {
                  this.hoveringColor = i;
                  set = true;
                  break;
               }
            }

            if (!set) {
               this.hoveringColor = -1;
            }
         } else {
            int boundedMouseX = this.bound(mouseX, left, left + width);
            i = this.bound(mouseY, top, top + height);
            float colorX = (float)(boundedMouseX - left) / (float)width;
            float colorY = (float)(i - top) / (float)height;
            if (this.changingColor) {
               this.hsb[1] = colorX;
               this.hsb[2] = 1.0F - colorY;
            } else if (this.changingHue) {
               this.hsb[0] = colorX;
               i = top + height + 8;
            }

            int fullbrightColor = Color.HSBtoRGB(this.hsb[0], 1.0F, 1.0F);
            QuestColor newQuestColor = new QuestColor(newColor);
            quest.setColor(newQuestColor);
            int size = 12;
            int border = 1;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 0.0F, 2.0F);
            func_73734_a(boundedMouseX - border - size / 2, i - border - size / 2, boundedMouseX + size / 2 + border, i + size / 2 + border, -16777216);
            func_73734_a(boundedMouseX - size / 2, i - size / 2, boundedMouseX + size / 2, i + size / 2, this.changingColor ? newColor : fullbrightColor);
            GlStateManager.func_179121_F();
         }
      }

   }

   private int bound(int value, int min, int max) {
      return Math.min(Math.max(value, min), max);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int id = -1;
      Iterator var11 = this.buttons.iterator();

      while(var11.hasNext()) {
         GuiSimpleButton button = (GuiSimpleButton)var11.next();
         if (button.isEnabled() && button.isWithin(mouseX, mouseY)) {
            id = button.id;
            break;
         }
      }

      if (id != -1) {
         switch (id) {
            case 0:
               QuestEditorState.get().push(this.oldName, QuestEditorState.get().getSelectedQuest(), true);
               this.oldName = QuestEditorState.get().getSelectedQuest().getFilename();
               break;
            case 1:
               QuestEditorState.get().push(this.oldName, QuestEditorState.get().getSelectedQuest(), false);
               this.oldName = QuestEditorState.get().getSelectedQuest().getFilename();
               break;
            case 2:
               QuestEditorState.get().fetch(false);
               this.reload = true;
         }
      }

      if (mouseX >= this.addStageButtonLoc[0] && mouseY >= this.addStageButtonLoc[1] && mouseX <= this.addStageButtonLoc[2] && mouseY <= this.addStageButtonLoc[3]) {
         QuestEditorState.get().addNewStage();
      }

      if (this.globalQuestOption != -1) {
         switch (this.globalQuestOption) {
            case 1:
               QuestEditorState.get().getSelectedQuest().toggleAbandonability();
               break;
            case 2:
               QuestEditorState.get().getSelectedQuest().toggleRepeatability();
               break;
            case 3:
               this.field_146297_k.func_147108_a(new GuiQuestStrings());
         }
      }

      this.questName.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void func_73869_a(char c, int i) {
      try {
         super.func_73869_a(c, i);
      } catch (IOException var4) {
         if (Pixelmon.devEnvironment) {
            Pixelmon.LOGGER.error(var4.getLocalizedMessage());
         }
      }

      if (this.questName.isFocused() && this.questName.textboxKeyTyped(c, i)) {
         QuestEditorState.get().getSelectedQuest().setFilename(this.questName.getText() + ".json");
      }

   }

   private void checkMouseWheel(int mouseX, int mouseY) {
      int mousewheelDirection = Mouse.getDWheel();
      if (mousewheelDirection == 120) {
         this.onScroll(GuiQuestOverview.Direction.DOWN, mouseX, mouseY);
      } else if (mousewheelDirection == -120) {
         this.onScroll(GuiQuestOverview.Direction.UP, mouseX, mouseY);
      }

   }

   private void onScroll(Direction direction, int mouseX, int mouseY) {
      if (!this.changingColor && !this.changingHue) {
         int oldStageValue;
         int newStageValue;
         if (this.hoverStage != null) {
            oldStageValue = this.hoverStage.getStage();
            newStageValue = (short)(oldStageValue + (direction == GuiQuestOverview.Direction.UP ? 1 : -1));
            if (newStageValue >= 0 && newStageValue < 32767 && oldStageValue != newStageValue) {
               Quest quest = QuestEditorState.get().getSelectedQuest();
               Stage replacing = quest.getStage((short)newStageValue);
               if (replacing != null) {
                  short sA = this.hoverStage.getStage();
                  short nsA = this.hoverStage.getNextStage();
                  short sB = replacing.getStage();
                  short nsB = replacing.getNextStage();
                  this.hoverStage.setStage(sB);
                  this.hoverStage.setNextStage(nsB);
                  replacing.setStage(sA);
                  replacing.setNextStage(nsA);
               } else if (oldStageValue != 0 || newStageValue != 1) {
                  this.hoverStage.setStage((short)newStageValue);
                  Iterator var8 = quest.getStages().iterator();

                  while(var8.hasNext()) {
                     Stage stage = (Stage)var8.next();
                     if (stage.getNextStage() == oldStageValue) {
                        stage.setNextStage((short)newStageValue);
                     }
                  }

                  quest.clearDefaultStrings(oldStageValue, this.hoverStage.getRawObjectives().size());
                  quest.setDefaultStrings(newStageValue, this.hoverStage.getRawObjectives().size());
               }

               quest.getStages().sort(Comparator.comparing(Stage::getStage));
            }
         }

         if (this.hoveringColor != -1) {
            oldStageValue = direction == GuiQuestOverview.Direction.DOWN ? -1 : 1;
            newStageValue = Color.HSBtoRGB(this.hsb[0], this.hsb[1], this.hsb[2]);
            Color color = new Color(newStageValue);
            QuestColor newColor = new QuestColor(this.bound(color.getRed() + (this.hoveringColor == 0 ? oldStageValue : 0), 0, 255), this.bound(color.getGreen() + (this.hoveringColor == 1 ? oldStageValue : 0), 0, 255), this.bound(color.getBlue() + (this.hoveringColor == 2 ? oldStageValue : 0), 0, 255));
            QuestEditorState.get().getSelectedQuest().setColor(newColor);
            Color.RGBtoHSB(newColor.getR(), newColor.getG(), newColor.getB(), this.hsb);
         }

         if (this.globalQuestOption == 0) {
            oldStageValue = direction == GuiQuestOverview.Direction.DOWN ? -1 : 1;
            QuestEditorState.get().getSelectedQuest().setWeight(QuestEditorState.get().getSelectedQuest().getWeight() + oldStageValue);
         }

      }
   }

   private void refreshQuest() {
      Iterator var1 = QuestEditorState.get().getAllQuests().iterator();

      while(var1.hasNext()) {
         Quest quest = (Quest)var1.next();
         if (quest.getFilename().equalsIgnoreCase(this.oldName)) {
            QuestEditorState.get().selectQuest(quest);
            this.field_146297_k.func_147108_a(new GuiQuestOverview());
            break;
         }
      }

   }

   public void func_73876_c() {
      if (!QuestEditorState.get().hasQuestSelected()) {
         this.field_146297_k.func_147108_a(new GuiQuestEditor());
      }

      this.addButtons();
      if (QuestEditorState.get().isDirty() && this.reload) {
         this.refreshQuest();
         this.reload = false;
      }

   }

   private static enum Direction {
      UP,
      DOWN;
   }

   private static enum MouseState {
      HOVER,
      PRESS,
      DRAG,
      RELEASE;
   }
}
