package com.pixelmonmod.pixelmon.quests.client.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDownManager;
import com.pixelmonmod.pixelmon.quests.actions.ExecutorMode;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiSimpleButton;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Mouse;

public class GuiQuestStage extends GuiScreen {
   private List buttons = new ArrayList();
   private Stage stage;
   private CopyOnWriteArrayList objectives;
   private CopyOnWriteArrayList actions;
   private QuestElement selection = null;
   private HashSet actionObjectives = new HashSet();
   private ExecutorMode actionMode;
   private ArrayList selectionElements;
   private GuiDropDown dropDown;
   private final GuiDropDownManager dropDownManager;
   private MouseState mouseState;
   private boolean reload;
   int prevWidth;
   int prevHeight;

   public GuiQuestStage(Stage stage) {
      this.actionMode = ExecutorMode.AND;
      this.selectionElements = new ArrayList();
      this.dropDownManager = new GuiDropDownManager();
      this.mouseState = GuiQuestStage.MouseState.HOVER;
      this.reload = false;
      this.prevWidth = -1;
      this.prevHeight = -1;
      this.stage = stage;
      this.objectives = new CopyOnWriteArrayList(QuestEditorState.get().getObjectivesForStage(stage));
      this.actions = new CopyOnWriteArrayList(QuestEditorState.get().getActionsForStage(stage));
      this.dropDownManager.clearDropDowns();
   }

   public void func_73866_w_() {
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
         Gui.func_146110_a(x - gap, y, 0.0F, 0.0F, dispWidth / 2, dispHeight, (float)dispWidth / 2.0F, (float)dispHeight);
         Gui.func_146110_a(x + dispWidth / 2 + gap + 1, y, 0.0F, 0.0F, dispWidth / 2, dispHeight, (float)dispWidth / 2.0F, (float)dispHeight);
         GlStateManager.func_179147_l();
         GlStateManager.func_179141_d();
         GlStateManager.func_179112_b(770, 771);
         this.drawElements(x, y, mouseX, mouseY);
         GlStateManager.func_179084_k();
         GlStateManager.func_179118_c();
         Iterator var11 = this.buttons.iterator();

         while(var11.hasNext()) {
            GuiSimpleButton button = (GuiSimpleButton)var11.next();
            button.draw();
         }

         if (this.selection != null) {
            this.selection.draw(this.field_146297_k, x, y, dispWidth, dispHeight);
         }

         this.dropDownManager.drawDropDowns(0.0F, mouseX, mouseY);
         this.checkMouseWheel(mouseX, mouseY);
      }
   }

   private void drawElements(int x, int y, int mouseX, int mouseY) {
      boolean mouseDown = Mouse.isButtonDown(0);
      if (mouseDown) {
         if (this.mouseState == GuiQuestStage.MouseState.HOVER) {
            this.mouseState = GuiQuestStage.MouseState.PRESS;
         } else if (this.mouseState == GuiQuestStage.MouseState.PRESS) {
            this.mouseState = GuiQuestStage.MouseState.DRAG;
         }
      } else if (this.mouseState != GuiQuestStage.MouseState.PRESS && this.mouseState != GuiQuestStage.MouseState.DRAG) {
         if (this.mouseState == GuiQuestStage.MouseState.RELEASE) {
            this.mouseState = GuiQuestStage.MouseState.HOVER;
         }
      } else {
         this.mouseState = GuiQuestStage.MouseState.RELEASE;
      }

      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int left = x + 4;
      int top = y + 24;
      int right = x + dispWidth / 2 - 10;
      int size = 13;
      boolean showActionOptions = this.selection != null && this.selection.getType() == QuestElementType.ACTION;
      if (showActionOptions) {
         this.field_146289_q.func_78264_a(true);
         GuiHelper.drawCenteredString(TextFormatting.BOLD + this.actionMode.name(), (float)right - 6.0F, (float)(top - 8), 0);
         this.field_146289_q.func_78264_a(false);
      }

      int i = 0;
      Iterator var16 = this.objectives.iterator();

      QuestElement element;
      while(var16.hasNext()) {
         element = (QuestElement)var16.next();
         this.drawElement(element, x, y, dispWidth, dispHeight, i++, size, left, right, top, mouseX, mouseY, -17664);
      }

      var16 = this.actions.iterator();

      while(var16.hasNext()) {
         element = (QuestElement)var16.next();
         this.drawElement(element, x, y, dispWidth, dispHeight, i++, size, left, right, top, mouseX, mouseY, -5479425);
      }

      int addY = top + (size - 1) * i + 5;
      func_73734_a(left + 20, addY, left + 40, addY + 10, -17664);
      GuiHelper.drawCenteredString("+", (float)left + 30.5F, (float)addY + 1.75F, -16777216);
      func_73734_a(right - 40, addY, right - 20, addY + 10, -5479425);
      GuiHelper.drawCenteredString("+", (float)(right - 40) + 10.5F, (float)addY + 1.75F, -16777216);
      if (this.mouseState == GuiQuestStage.MouseState.PRESS) {
         if (showActionOptions && mouseX >= right - 12 && mouseX <= right && mouseY >= top - 8 && mouseY <= top - 2) {
            this.actionMode = this.actionMode == ExecutorMode.AND ? ExecutorMode.OR : ExecutorMode.AND;
         } else if (mouseY >= addY && mouseY <= addY + 10) {
            if (mouseX >= left + 20 && mouseX <= left + 40) {
               this.objectives.add(((QuestElement)QuestEditorState.get().getObjectives().get(0)).copy());
            } else if (mouseX >= right - 40 && mouseX <= right - 20) {
               this.actions.add(((QuestElement)QuestEditorState.get().getActions().get(0)).copy());
            }
         }
      }

      QuestColor color = QuestEditorState.get().getSelectedQuest().getColor();
      GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.stage.getIcon().getResource());
      Gui.func_146110_a(x + 60, y + 4, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
   }

   private void drawElement(QuestElement element, int x, int y, int w, int h, int i, int size, int left, int right, int top, int mouseX, int mouseY, int color) {
      int offset = (size - 1) * i;
      boolean selected = this.selection == element;
      if (selected) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(0.0F, 0.0F, 2.0F);
      }

      boolean showActionOptions = this.selection != null && this.selection.getType() == QuestElementType.ACTION && element.getType() == QuestElementType.OBJECTIVE;
      int nudge = 0;
      if (showActionOptions) {
         nudge = 12;
      }

      int x2 = right - nudge;
      int y1 = top + offset;
      int y2 = top + size + offset;
      func_73734_a(left, y1, x2, y2, selected ? -1 : -16777216);
      func_73734_a(left + 1, y1 + 1, x2 - 1, y2 - 1, color);
      GuiHelper.drawSquashedString(this.field_146289_q, I18n.func_188566_a(element.getName()) ? I18n.func_135052_a(element.getName(), new Object[0]) : element.getIdentifier(), false, showActionOptions ? 84.0 : 95.0, (float)(left + 3), (float)(y1 + size / 2 - 3), -16777216, false);
      func_73734_a(x2 - 11, y1 + 2, x2 - 2, y1 + 11, -2210481);
      this.field_146289_q.func_78276_b("-", x2 - 9, y1 + 3, -1);
      if (showActionOptions) {
         func_73734_a(right - 11, y1 + 2, right - 2, y1 + 3, -16777216);
         func_73734_a(right - 11, y1 + 10, right - 2, y1 + 11, -16777216);
         func_73734_a(right - 11, y1 + 2, right - 10, y1 + 11, -16777216);
         func_73734_a(right - 3, y1 + 2, right - 2, y1 + 11, -16777216);
         if (this.actionObjectives.contains(i)) {
            func_73734_a(right - 10, y1 + 3, right - 3, y1 + 10, -5479425);
         }
      }

      if (this.mouseState == GuiQuestStage.MouseState.PRESS) {
         if (showActionOptions && mouseX >= right - 11 && mouseY >= y1 + 2 && mouseX <= right - 2 && mouseY <= y1 + 11) {
            if (!this.actionObjectives.remove(i)) {
               this.actionObjectives.add(i);
            }
         } else if (mouseX >= x2 - 11 && mouseY >= y1 + 2 && mouseX <= x2 - 2 && mouseY <= y1 + 11) {
            this.deleteElement(element);
         } else if (mouseX >= left && mouseY >= y1 && mouseX <= x2 && mouseY <= y2) {
            this.selectElement(element, x, y, w, h);
         }
      }

      if (selected) {
         GlStateManager.func_179121_F();
      }

   }

   private void writeExtraString(QuestElement element) {
      if (element != null && element.getType() == QuestElementType.ACTION) {
         StringBuilder extra = new StringBuilder(this.actionMode == ExecutorMode.AND ? "+" : "");
         boolean first = true;

         for(Iterator var4 = this.actionObjectives.iterator(); var4.hasNext(); first = false) {
            int i = (Integer)var4.next();
            if (!first) {
               extra.append(",");
            }

            extra.append(i);
         }

         String result = extra.toString();
         if (!result.isEmpty() && !result.equalsIgnoreCase("+")) {
            element.setExtra(result);
         } else {
            element.setExtra("0");
         }
      }

   }

   private void selectElement(QuestElement element, int x, int y, int w, int h) {
      this.writeExtraString(this.selection);
      this.selection = element;
      if (this.selection.getType() == QuestElementType.ACTION) {
         String objArgs = this.selection.getExtra();
         this.actionMode = objArgs.startsWith("+") ? ExecutorMode.AND : ExecutorMode.OR;
         if (this.actionMode == ExecutorMode.AND) {
            objArgs = objArgs.substring(1);
         }

         String[] split = objArgs.split(",");
         this.actionObjectives.clear();
         String[] var8 = split;
         int var9 = split.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String arg = var8[var10];
            if (NumberUtils.isParsable(arg)) {
               int i = Integer.parseInt(arg);
               if (this.objectives.size() > i) {
                  this.actionObjectives.add(i);
               }
            }
         }
      }

      this.selection.initialize(this.field_146297_k, x, y, w, h);
      this.setDropDowns(x, y, w, h);
   }

   private void setDropDowns(int x, int y, int w, int h) {
      int dropDownX = x + w / 2 + 6;
      int dropDownY = y + 2;
      int dropDownW = 111;
      int dropDownH = 140;
      this.dropDownManager.clearDropDowns();
      if (this.selection.getType() == QuestElementType.OBJECTIVE) {
         this.addDropDown((new GuiDropDown(QuestEditorState.get().getObjectives(), QuestEditorState.get().getObjectives().indexOf(QuestEditorState.get().getObjective(this.selection.getIdentifier())), dropDownX, dropDownY, dropDownW, dropDownH, dropDownW - 6)).setGetOptionString((qe) -> {
            return I18n.func_188566_a(qe.getName()) ? I18n.func_135052_a(qe.getName(), new Object[0]) : qe.getIdentifier();
         }).setOrdered().setOnSelected((e) -> {
            this.selection.duplicate(e);
         }));
      } else if (this.selection.getType() == QuestElementType.ACTION) {
         this.addDropDown((new GuiDropDown(QuestEditorState.get().getActions(), QuestEditorState.get().getActions().indexOf(QuestEditorState.get().getAction(this.selection.getIdentifier())), dropDownX, dropDownY, dropDownW, dropDownH, dropDownW - 6)).setGetOptionString((qe) -> {
            return I18n.func_188566_a(qe.getName()) ? I18n.func_135052_a(qe.getName(), new Object[0]) : qe.getIdentifier();
         }).setOrdered().setOnSelected((e) -> {
            this.selection.duplicate(e);
         }));
      }

   }

   private void deleteElement(QuestElement element) {
      if (this.objectives.remove(element)) {
         Quest quest = QuestEditorState.get().getSelectedQuest();
         quest.clearDefaultStrings(this.stage.getStage(), this.objectives.size());
         this.actionObjectives.remove(this.objectives.size());
      }

      this.actions.remove(element);
      if (this.selection == element) {
         this.selection = null;
      }

   }

   private int bound(int value, int min, int max) {
      return Math.min(Math.max(value, min), max);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (!this.dropDownManager.mouseClicked(mouseX, mouseY, mouseButton)) {
         int x = this.field_146294_l / 2 - 125;
         int y = this.field_146295_m / 2 - 100;
         Gui.func_146110_a(x + 104, y + 4, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
         if (mouseX >= x + 60 && mouseX <= x + 60 + 16 && mouseY >= y + 4 && mouseY <= y + 4 + 16) {
            this.stage.changeIcon();
         } else {
            int id = -1;
            Iterator var7 = this.buttons.iterator();

            while(var7.hasNext()) {
               GuiSimpleButton button = (GuiSimpleButton)var7.next();
               if (button.isEnabled() && button.isWithin(mouseX, mouseY)) {
                  id = button.id;
                  break;
               }
            }

            if (id != -1) {
               switch (id) {
                  case 0:
                     this.writeToStage();
                     this.field_146297_k.func_147108_a(new GuiQuestOverview());
                     break;
                  case 1:
                     this.writeToStage();
                     break;
                  case 2:
                     this.field_146297_k.func_147108_a(new GuiQuestOverview());
               }
            }

            if (this.selection != null) {
               this.selection.onClick(mouseX, mouseY, mouseButton);
            }

         }
      }
   }

   private void writeToStage() {
      this.writeExtraString(this.selection);
      Quest quest = QuestEditorState.get().getSelectedQuest();
      this.stage.getRawObjectives().clear();
      int oi = 0;
      Iterator var3 = this.objectives.iterator();

      while(var3.hasNext()) {
         QuestElement element = (QuestElement)var3.next();
         this.stage.getRawObjectives().add(element.build(quest, this.stage, oi++));
      }

      this.stage.getRawActions().clear();
      int ai = 0;
      Iterator var7 = this.actions.iterator();

      while(var7.hasNext()) {
         QuestElement element = (QuestElement)var7.next();
         this.stage.getRawActions().add(element.build(quest, this.stage, ai++));
      }

      quest.setDefaultStrings(this.stage.getStage(), this.stage.getRawObjectives().size());
   }

   public void func_73869_a(char c, int i) {
      try {
         super.func_73869_a(c, i);
      } catch (IOException var4) {
         if (Pixelmon.devEnvironment) {
            Pixelmon.LOGGER.error(var4.getLocalizedMessage());
         }
      }

      if (this.selection != null) {
         this.selection.onType(c, i);
      }

   }

   private void checkMouseWheel(int mouseX, int mouseY) {
      int mousewheelDirection = Mouse.getDWheel();
      if (mousewheelDirection == 120) {
         this.onScroll(GuiQuestStage.Direction.DOWN, mouseX, mouseY);
      } else if (mousewheelDirection == -120) {
         this.onScroll(GuiQuestStage.Direction.UP, mouseX, mouseY);
      }

   }

   private void onScroll(Direction direction, int mouseX, int mouseY) {
   }

   public void func_73876_c() {
      if (!QuestEditorState.get().hasQuestSelected()) {
         this.field_146297_k.func_147108_a(new GuiQuestEditor());
      }

      if (this.prevWidth != -1 && this.prevHeight != -1 && (this.field_146294_l != this.prevWidth || this.field_146295_m != this.prevHeight)) {
         this.addButtons();
         if (this.selection != null) {
            int x = this.field_146294_l / 2 - 125;
            int y = this.field_146295_m / 2 - 100;
            int xOffset = 20;
            int yOffset = 20;
            int dispWidth = xOffset + 230;
            int dispHeight = yOffset + 180;
            this.selection.initialize(this.field_146297_k, x, y, dispWidth, dispHeight);
            this.setDropDowns(x, y, dispWidth, dispHeight);
         }
      }

      if (QuestEditorState.get().isDirty() && this.reload) {
         this.reload = false;
      }

      this.prevWidth = this.field_146294_l;
      this.prevHeight = this.field_146295_m;
   }

   public GuiDropDown addDropDown(GuiDropDown dropDown) {
      this.dropDownManager.addDropDown(dropDown);
      return dropDown;
   }

   public void removeDropDown(GuiDropDown dropDown) {
      this.dropDownManager.removeDropDown(dropDown);
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
