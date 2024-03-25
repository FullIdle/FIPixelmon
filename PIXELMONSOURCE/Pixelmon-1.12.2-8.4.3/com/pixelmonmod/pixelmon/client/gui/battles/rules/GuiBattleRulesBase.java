package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.EnumTier;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.Tier;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.EnumTextAlign;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonOnOff;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTextDescriptive;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiImportExport;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.IImportableContainer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public abstract class GuiBattleRulesBase extends GuiScreenDropDown implements IImportableContainer {
   protected BattleRules rules = new BattleRules();
   private String titleText;
   protected List textFields = new ArrayList();
   private GuiTextField levelCapField;
   private GuiTextField numPokemonField;
   private GuiTextField turnTimeField;
   private GuiTextField teamSelectTimeField;
   protected boolean editingEnabled = true;
   protected GuiButton importExportButton;
   protected GuiButtonOnOff raiseToCapButton;
   protected GuiButton battleTypeButton;
   protected GuiButtonOnOff fullHealButton;
   protected GuiButtonOnOff teamPreviewButton;
   protected GuiButton[] editButtons;
   protected List ruleLabels;
   protected List selectedClauses = new ArrayList();
   private GuiClauseList allClauseList;
   private GuiClauseList selectedClauseList;
   protected int centerX;
   protected int centerY;
   protected int rectBottom;
   protected int yChange;
   protected int clauseListHeight = 70;
   protected GuiDropDown tierMenu;

   public GuiBattleRulesBase() {
      Keyboard.enableRepeatEvents(true);
      this.field_146297_k = Minecraft.func_71410_x();
      this.titleText = I18n.func_135052_a("gui.battlerules.title", new Object[0]);
   }

   public void func_73866_w_() {
      if (!this.textFields.isEmpty()) {
         this.registerRules();
      }

      this.textFields.clear();
      super.func_73866_w_();
      this.centerX = this.field_146294_l / 2;
      this.centerY = this.field_146295_m / 2;
      this.rectBottom = this.centerY - 120 + this.getBackgroundHeight();
      int textID = 0;
      int textHeight = 20;
      int leftFieldX = this.centerX - 130;
      int centerFieldX = this.centerX - 20;
      int rightFieldX = this.centerX + 120;
      int topY = this.centerY - 76 + this.yChange;
      int yOffset = 25;
      int centerY = topY + yOffset;
      int bottomY = topY + yOffset * 2;
      int numberFieldWidth = 30;
      this.levelCapField = this.createRuleField(textID++, leftFieldX, topY, numberFieldWidth, textHeight);
      this.numPokemonField = this.createRuleField(textID++, centerFieldX, centerY, numberFieldWidth, textHeight);
      this.turnTimeField = this.createRuleField(textID++, rightFieldX, topY, numberFieldWidth, textHeight);
      this.teamSelectTimeField = this.createRuleField(textID++, rightFieldX, centerY, numberFieldWidth, textHeight);
      this.textFields.addAll(Arrays.asList(this.levelCapField, this.numPokemonField, this.turnTimeField, this.teamSelectTimeField));
      int buttonID = 0;
      int onOffButtonWidth = 30;
      int buttonHeight = 20;
      int leftButton = leftFieldX - 1;
      this.importExportButton = new GuiButton(buttonID++, this.centerX + 90, this.centerY - 120, 100, buttonHeight, I18n.func_135052_a("gui.pokemoneditor.importexport", new Object[0]));
      this.raiseToCapButton = new GuiButtonOnOff(buttonID, leftButton, centerY, onOffButtonWidth, buttonHeight, this.rules.raiseToCap);
      this.fullHealButton = new GuiButtonOnOff(buttonID, leftButton, bottomY, onOffButtonWidth, buttonHeight, this.rules.fullHeal);
      this.teamPreviewButton = new GuiButtonOnOff(buttonID, rightFieldX - 1, bottomY, onOffButtonWidth, buttonHeight, this.rules.teamPreview);
      this.battleTypeButton = new GuiButton(buttonID++, centerFieldX - 1, topY, 40, buttonHeight, this.rules.battleType.getLocalizedName());
      this.editButtons = new GuiButton[]{this.importExportButton, this.battleTypeButton, this.raiseToCapButton, this.fullHealButton, this.teamPreviewButton};
      this.field_146292_n.addAll(Arrays.asList(this.editButtons));
      this.ruleLabels = new ArrayList();
      int textOffset = 5;
      int leftTextX = leftFieldX - textOffset;
      int centerTextX = centerFieldX - textOffset;
      int rightTextX = rightFieldX - textOffset;
      int topTextY = topY + textOffset;
      int centerTextY = topTextY + yOffset;
      int bottomTextY = topTextY + yOffset * 2;
      int tierTextX = this.centerX - 62;
      this.ruleLabels.add(this.createRuleLabel("levelcap", leftTextX, topTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("raisetocap", leftTextX, centerTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("fullheal", leftTextX, bottomTextY, EnumTextAlign.Right));
      this.ruleLabels.add(new GuiTextDescriptive(I18n.func_135052_a("gui.trainereditor.battletype", new Object[0]), I18n.func_135052_a("gui.battlerules.description.battletype", new Object[0]), centerTextX, topTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("numpokemon", centerTextX, centerTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("tier", tierTextX, bottomTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("turntime", rightTextX, topTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("teamselecttime", rightTextX, centerTextY, EnumTextAlign.Right));
      this.ruleLabels.add(this.createRuleLabel("teampreview", rightTextX, bottomTextY, EnumTextAlign.Right));
      int clauseListWidth = 120;
      int clauseListY = topTextY + yOffset * 2 + 30;
      int clauseListX = centerTextX - clauseListWidth / 2 + 10;
      this.allClauseList = new GuiClauseList(this, BattleClauseRegistry.getClauseRegistry().getClauseList(), clauseListY, clauseListX, clauseListWidth, this.clauseListHeight);
      this.selectedClauseList = new GuiClauseList(this, this.selectedClauses, clauseListY, clauseListX + clauseListWidth + 10, clauseListWidth, this.clauseListHeight);
      BattleClauseRegistry tierRegistry = BattleClauseRegistry.getTierRegistry();
      List tiers = new ArrayList();
      EnumTier[] var28 = EnumTier.values();
      int var29 = var28.length;

      for(int var30 = 0; var30 < var29; ++var30) {
         EnumTier tier = var28[var30];
         tiers.add(tierRegistry.getClause(tier.getTierID()));
      }

      List customTiers = tierRegistry.getCustomClauses();
      Collections.sort(customTiers);
      tiers.addAll(customTiers);
      this.tierMenu = new GuiDropDown(tiers, this.rules.tier, tierTextX + 10, bottomTextY, 70, 100);
      this.addDropDown(this.tierMenu);
      this.setRules(this.rules);
   }

   private GuiTextField createRuleField(int componentID, int x, int y, int width, int height) {
      return new GuiTextField(componentID, this.field_146297_k.field_71466_p, x, y, width, height);
   }

   private GuiTextDescriptive createRuleLabel(String langKey, int x, int y, EnumTextAlign align) {
      return new GuiTextDescriptive(I18n.func_135052_a("gui.battlerules." + langKey, new Object[0]), I18n.func_135052_a("gui.battlerules.description." + langKey, new Object[0]), x, y, align);
   }

   public void setRules(BattleRules rules) {
      this.rules = rules;
      this.setText(this.levelCapField, this.rules.levelCap);
      this.setText(this.numPokemonField, this.rules.numPokemon);
      this.setBlankableText(this.turnTimeField, this.rules.turnTime);
      this.setBlankableText(this.teamSelectTimeField, this.rules.teamSelectTime);
      this.raiseToCapButton.setOn(this.rules.raiseToCap);
      this.fullHealButton.setOn(this.rules.fullHeal);
      this.teamPreviewButton.setOn(this.rules.teamPreview);
      this.battleTypeButton.field_146126_j = this.rules.battleType.getLocalizedName();
      this.selectedClauses.clear();
      this.selectedClauses.addAll(this.rules.getClauseList());
      this.tierMenu.setSelected(this.rules.tier);
   }

   private void setText(GuiTextField field, int number) {
      field.func_146180_a(Integer.toString(number));
   }

   private void setBlankableText(GuiTextField field, int number) {
      String fieldText = "";
      if (number > 0) {
         fieldText = Integer.toString(number);
      }

      field.func_146180_a(fieldText);
   }

   protected void drawBackgroundUnderMenus(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      int textColor = 0;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.centerX - 200), (double)(this.centerY - 120), 400.0, (float)this.getBackgroundHeight(), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      Iterator var5 = this.textFields.iterator();

      while(var5.hasNext()) {
         GuiTextField textField = (GuiTextField)var5.next();
         textField.func_146194_f();
      }

      GuiButton[] var12 = this.editButtons;
      int var14 = var12.length;

      int descX;
      for(descX = 0; descX < var14; ++descX) {
         GuiButton button = var12[descX];
         button.field_146124_l = this.editingEnabled;
      }

      this.importExportButton.field_146125_m = this.editingEnabled;
      GuiHelper.drawCenteredString(this.titleText, (float)this.centerX, (float)(this.centerY - 90 + this.yChange), textColor);
      if (this.editingEnabled) {
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.battlerules.clauses", new Object[0]), (float)this.allClauseList.getCenterX(), (float)(this.centerY + this.yChange), textColor);
      }

      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.battlerules.selectedclauses", new Object[0]), (float)this.selectedClauseList.getCenterX(), (float)(this.centerY + this.yChange), textColor);
      String description = "";
      Iterator var15 = this.ruleLabels.iterator();

      while(var15.hasNext()) {
         GuiTextDescriptive text = (GuiTextDescriptive)var15.next();
         text.draw();
         if (text.isHovering(mouseX, mouseY)) {
            description = text.getDescription();
         }
      }

      GuiClauseList[] renderLists;
      if (this.editingEnabled) {
         renderLists = new GuiClauseList[]{this.allClauseList, this.selectedClauseList};
      } else {
         renderLists = new GuiClauseList[]{this.selectedClauseList};
      }

      GuiClauseList[] var18 = renderLists;
      int descY = renderLists.length;

      for(int var9 = 0; var9 < descY; ++var9) {
         GuiClauseList list = var18[var9];
         list.drawScreen(mouseX, mouseY, partialTicks);
         if (description.isEmpty()) {
            int hoverIndex = list.getMouseOverIndex(mouseX, mouseY);
            if (hoverIndex > -1) {
               description = list.getElement(hoverIndex).getDescription();
            }
         }
      }

      if (!description.isEmpty()) {
         descX = this.centerX - 190;
         descY = this.centerY + 10 + this.yChange;
         int textWidth = 100;
         int boxOffset = 5;
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
         GlStateManager.func_179124_c(0.65F, 0.65F, 0.65F);
         GuiHelper.drawImageQuad((double)(descX - boxOffset), (double)(descY - boxOffset), (double)(textWidth + boxOffset * 2), 70.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         this.field_146297_k.field_71466_p.func_78279_b(description, descX, descY, textWidth, textColor);
      }

   }

   protected int getBackgroundHeight() {
      return 240;
   }

   protected void func_73869_a(char key, int keyCode) {
      if (this.editingEnabled) {
         Iterator var3 = this.textFields.iterator();

         while(var3.hasNext()) {
            GuiTextField textField = (GuiTextField)var3.next();
            textField.func_146201_a(key, keyCode);
         }

         GuiHelper.switchFocus(keyCode, this.textFields);
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      if (this.editingEnabled) {
         Iterator var4 = this.textFields.iterator();

         while(var4.hasNext()) {
            GuiTextField textField = (GuiTextField)var4.next();
            textField.func_146192_a(x, y, mouseButton);
         }
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button == this.importExportButton) {
         this.registerRules();
         this.field_146297_k.func_147108_a(new GuiImportExport(this, this.titleText));
      } else if (button == this.battleTypeButton) {
         this.rules.battleType = this.rules.battleType.next();
         this.battleTypeButton.field_146126_j = this.rules.battleType.getLocalizedName();
      } else if (button instanceof GuiButtonOnOff) {
         ((GuiButtonOnOff)button).toggle();
      }

   }

   protected void registerRules() {
      try {
         this.rules.levelCap = Integer.parseInt(this.levelCapField.func_146179_b());
      } catch (NumberFormatException var5) {
      }

      this.rules.raiseToCap = this.raiseToCapButton.isOn();

      try {
         this.rules.numPokemon = Integer.parseInt(this.numPokemonField.func_146179_b());
      } catch (NumberFormatException var4) {
      }

      try {
         this.rules.turnTime = this.parseBlankableText(this.turnTimeField.func_146179_b());
      } catch (NumberFormatException var3) {
      }

      try {
         this.rules.teamSelectTime = this.parseBlankableText(this.teamSelectTimeField.func_146179_b());
      } catch (NumberFormatException var2) {
      }

      this.rules.fullHeal = this.fullHealButton.isOn();
      this.rules.teamPreview = this.teamPreviewButton.isOn();
      this.rules.setNewClauses(this.selectedClauses);
      this.rules.tier = (Tier)this.tierMenu.getSelected();
      this.rules.validateRules();
      this.setRules(this.rules);
   }

   private int parseBlankableText(String text) {
      return text.trim().isEmpty() ? 0 : Integer.parseInt(text);
   }

   public String getExportText() {
      return this.rules.exportText();
   }

   public String importText(String importText) {
      String result = this.rules.importText(importText);
      this.setRules(this.rules);
      return result;
   }

   public void func_146281_b() {
      super.func_146281_b();
      Keyboard.enableRepeatEvents(false);
   }

   void clauseListSelected(List clauses, int index) {
      if (this.editingEnabled) {
         BattleClause clause = (BattleClause)clauses.get(index);
         if (this.selectedClauses.contains(clause)) {
            this.selectedClauses.remove(clause);
         } else {
            this.selectedClauses.add(clause);
            Collections.sort(this.selectedClauses);
         }

      }
   }

   boolean isClauseSelected(List clauses, int index) {
      return clauses == this.selectedClauses ? false : this.selectedClauses.contains(clauses.get(index));
   }

   protected void dimScreen() {
      Gui.func_73734_a(this.centerX - 200, this.centerY - 120, this.centerX + 200, this.rectBottom, 1593835520);
   }

   protected void highlightButtons(int highlightOffsetX, int bottomOffset) {
      Gui.func_73734_a(this.centerX - highlightOffsetX, this.rectBottom - bottomOffset, this.centerX + highlightOffsetX, this.rectBottom - bottomOffset + 35, -1);
   }

   public GuiScreen getScreen() {
      return this;
   }

   protected boolean disableMenus() {
      return !this.editingEnabled;
   }
}
