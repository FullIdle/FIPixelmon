package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.exceptions.ShowdownImportException;
import com.pixelmonmod.pixelmon.api.pokemon.ImportExportConverter;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonHoverDisable;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public abstract class GuiIndividualEditorBase extends GuiScreenDropDown implements IImportableContainer {
   public Pokemon p;
   public String titleText;
   private GuiTextField tbName;
   private GuiTextField tbNickname;
   private GuiTextField tbLvl;
   private GuiTabCompleteTextField[] tbMoves = new GuiTabCompleteTextField[4];
   private String origName;
   private Attack[] attacks = new Attack[4];
   private static final int BUTTON_OKAY = 1;
   private static final int BUTTON_GENDER = 3;
   private static final int BUTTON_CHANGE = 4;
   private static final int BUTTON_ADVANCED = 6;
   private static final int BUTTON_DELETE = 14;
   private static final int BUTTON_IMPORT_EXPORT = 15;
   protected List textFields = Lists.newArrayList();

   public GuiIndividualEditorBase(Pokemon p, String titleText) {
      this.p = p;
      this.origName = p.getSpecies().getLocalizedName();
      this.titleText = titleText;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      List shinyOptions = Lists.newArrayList(new EnumTextureType[]{EnumTextureType.Normal, EnumTextureType.Shiny});
      EnumSpecies species = this.p.getSpecies();
      EnumTextureType currentType = EnumTextureType.Normal;
      if (this.p.isShiny()) {
         currentType = EnumTextureType.Shiny;
      }

      this.addDropDown((new GuiDropDown(shinyOptions, currentType, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 14, 80, 50)).setOnSelected(this::setTextureType).setGetOptionString(EnumTextureType::getLocalizedName));
      double malePercent = this.p.getSpecies().getBaseStats().getMalePercent();
      if (malePercent > 0.0 && malePercent < 100.0) {
         this.field_146292_n.add(new GuiButtonHoverDisable(3, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 30, 80, 20, this.p.getGender() == Gender.Male ? I18n.func_135052_a("gui.trainereditor.male", new Object[0]) : I18n.func_135052_a("gui.trainereditor.female", new Object[0])));
      }

      this.addDropDown((new GuiDropDown(EnumGrowth.orderedList, this.p.getGrowth(), this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 28, 80, 100)).setOnSelected((growth) -> {
         this.p.setGrowth(growth);
      }).setGetOptionString(ITranslatable::getLocalizedName).setInactiveTop(this.field_146295_m / 2 + 54));
      List forms = species.getPossibleForms(true);
      if (forms.contains(EnumSpecial.Online) && !PlayerExtraDataStore.get((EntityPlayer)this.field_146297_k.field_71439_g).canSeeTexture(species)) {
         forms.remove(EnumSpecial.Online);
      }

      if (forms.contains(EnumDeoxys.Sus) && !PlayerExtraDataStore.get((EntityPlayer)this.field_146297_k.field_71439_g).canSeeTexture(species)) {
         forms.remove(EnumDeoxys.Sus);
      }

      int formWidth = this.getLongestString(forms);
      GuiDropDown formGuiDropDown = this.addDropDown((new GuiDropDown(forms, this.p.getFormEnum(), this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 92 + Math.max(-16, Math.min(6, forms.size()) - 1) * -10, 80, 80, Math.max(80, formWidth))).setOnSelected((enumForm) -> {
         this.p.setForm(enumForm);
         this.func_73866_w_();
      }).setGetOptionString(ITranslatable::getLocalizedName).setInactiveTop(this.field_146295_m / 2 + 94));
      if (!Entity3HasStats.hasForms(this.p.getSpecies())) {
         formGuiDropDown.setVisible(false);
      }

      this.tbName = (new GuiTabCompleteTextField(6, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 50, 90, 17)).setCompletions(EnumSpecies.getNameList());
      this.tbName.func_146180_a(this.p.getSpecies().getLocalizedName());
      this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 20, this.field_146295_m / 2 - 52, 100, 20, I18n.func_135052_a("gui.trainereditor.changepokemon", new Object[0])));
      this.tbLvl = new GuiTextField(7, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 30, 60, 17);
      this.tbLvl.func_146180_a(this.p.getLevel() + "");
      this.tbNickname = new GuiTextField(8, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 10, 90, 17);
      this.tbNickname.func_146203_f(16);
      this.tbNickname.func_146180_a(this.p.getNickname() == null ? "" : this.p.getNickname());

      int i;
      for(i = 0; i < this.tbMoves.length; ++i) {
         this.tbMoves[i] = (new GuiTabCompleteTextField(9 + i, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 60, this.field_146295_m / 2 + 20 * i, 120, 17)).setCompletions((Collection)AttackBase.ATTACKS.stream().map(ITranslatable::getLocalizedName).collect(Collectors.toSet()));
      }

      for(i = 0; i < this.p.getMoveset().size(); ++i) {
         this.attacks[i] = this.p.getMoveset().get(i);
         this.tbMoves[i].func_146180_a(this.attacks[i].getMove().getLocalizedName());
      }

      this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 + 105, this.field_146295_m / 2 - 110, 80, 20, I18n.func_135052_a("gui.trainereditor.advanced", new Object[0])));
      this.textFields.clear();
      this.textFields.addAll(Arrays.asList(this.tbName, this.tbLvl, this.tbNickname, this.tbMoves[0], this.tbMoves[1], this.tbMoves[2], this.tbMoves[3]));
      if (this.showDeleteButton()) {
         this.field_146292_n.add(new GuiButton(14, this.field_146294_l / 2 - 185, this.field_146295_m / 2 - 110, 90, 20, I18n.func_135052_a("gui.trainereditor.deletepoke", new Object[0])));
      }

      this.field_146292_n.add(new GuiButton(15, this.field_146294_l / 2 + 20, this.field_146295_m / 2 + 90, 100, 20, I18n.func_135052_a("gui.pokemoneditor.importexport", new Object[0])));
   }

   private int getLongestString(List items) {
      return GuiHelper.getLongestStringWidth((Collection)items.stream().filter(Objects::nonNull).map(ITranslatable::getLocalizedName).collect(Collectors.toList())) + 2;
   }

   protected GuiTextField createExtraTextField(int id) {
      return new GuiTextField(id, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 72, 60, 17);
   }

   private void setTextureType(EnumTextureType textureType) {
      switch (textureType) {
         case Shiny:
            this.p.setShiny(true);
            break;
         default:
            this.p.setShiny(false);
      }

   }

   protected boolean showDeleteButton() {
      return true;
   }

   protected void drawBackgroundUnderMenus(float f, int i, int j) {
      if (this.tbName == null) {
         this.func_73866_w_();
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      this.field_146297_k.field_71466_p.func_78276_b(this.titleText, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(this.titleText) / 2, this.field_146295_m / 2 - 90, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.name", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 45, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.lvl", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 25, 0);
      this.tbName.func_146194_f();
      this.tbLvl.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.nickname", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 5, 0);
      this.tbNickname.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.shiny", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 15, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.gender", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 35, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.growth", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 55, 0);
      if (this.p.getSpecies().getNumForms(true) > 0) {
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.form", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 95, 0);
      }

      GuiHelper.bindPokemonSprite(this.p, this.field_146297_k);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 157), (double)(this.field_146295_m / 2 - 73), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, 1.0F);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.screenpokechecker.moves", new Object[0]), this.field_146294_l / 2 + 100, this.field_146295_m / 2 - 15, 0);
      if (EnumGigantamaxPokemon.hasGigantamaxForm(this.p, true)) {
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         float color = this.p.hasGigantamaxFactor() ? 1.0F : 0.3F;
         GlStateManager.func_179124_c(color, color, color);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.gmaxFactor);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 28), (double)(this.field_146295_m / 2 + 92), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 1.0F);
      }

      GuiTabCompleteTextField[] var8 = this.tbMoves;
      int var5 = var8.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         GuiTextField tbMove = var8[var6];
         if (tbMove != null) {
            tbMove.func_146194_f();
         }
      }

   }

   protected void drawExtraText(GuiTextField textField, String langKey) {
      if (textField != null) {
         textField.func_146194_f();
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a(langKey, new Object[0]), this.field_146294_l / 2 - 180, textField.field_146210_g + 3, 0);
      }

   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      Iterator var3 = this.textFields.iterator();

      while(var3.hasNext()) {
         GuiTextField tbMove = (GuiTextField)var3.next();
         tbMove.func_146201_a(key, keyCode);
      }

      this.p.setNickname(this.tbNickname.func_146179_b());

      try {
         int lvl = Integer.parseInt(this.tbLvl.func_146179_b());
         if (lvl > 0 && lvl <= PixelmonServerConfig.maxLevel) {
            this.p.setLevel(lvl);
         }
      } catch (NumberFormatException var5) {
      }

      if (keyCode == 1 || keyCode == 28) {
         this.saveFields();
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int button) throws IOException {
      this.tbLvl.func_146192_a(x, y, button);
      this.tbNickname.func_146192_a(x, y, button);
      this.tbName.func_146192_a(x, y, button);
      GuiTabCompleteTextField[] var4 = this.tbMoves;
      int gY = var4.length;

      for(int var6 = 0; var6 < gY; ++var6) {
         GuiTextField tbMove = var4[var6];
         tbMove.func_146192_a(x, y, button);
      }

      if (EnumGigantamaxPokemon.hasGigantamaxForm(this.p, true)) {
         int gX = this.field_146294_l / 2 - 28;
         gY = this.field_146295_m / 2 + 92;
         boolean flagA = x >= gX && x < gX + 16 && y >= gY && y < gY + 16;
         if (flagA) {
            this.p.setGigantamaxFactor(!this.p.hasGigantamaxFactor());
         }
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 1) {
            this.saveFields();
         } else if (button.field_146127_k == 3 && this.p.getSpecies().getBaseStats().getMalePercent() > 0.0) {
            if (this.p.getGender() == Gender.Male) {
               this.p.setGender(Gender.Female);
            } else {
               this.p.setGender(Gender.Male);
            }

            this.func_73866_w_();
            button.field_146126_j = this.p.getGender() == Gender.Male ? I18n.func_135052_a("gui.trainereditor.male", new Object[0]) : I18n.func_135052_a("gui.trainereditor.female", new Object[0]);
         } else if (button.field_146127_k == 4) {
            Optional pokemon = EnumSpecies.getFromName(this.tbName.func_146179_b());
            if (pokemon.isPresent()) {
               this.origName = this.tbName.func_146179_b();
               this.changePokemon((EnumSpecies)pokemon.get());
            } else {
               this.tbName.func_146180_a(this.origName);
            }
         } else if (button.field_146127_k == 6) {
            if (this.checkFields()) {
               this.field_146297_k.func_147108_a(new GuiPokemonEditorAdvanced(this));
            }
         } else if (button.field_146127_k == 14) {
            this.deletePokemon();
         } else if (button.field_146127_k == 15 && this.checkFields()) {
            this.field_146297_k.func_147108_a(new GuiImportExport(this, this.titleText));
         }
      }

   }

   protected abstract void changePokemon(EnumSpecies var1);

   protected abstract void deletePokemon();

   private void saveFields() {
      if (this.checkFields()) {
         this.saveAndClose();
      }

   }

   protected abstract void saveAndClose();

   protected boolean checkFields() {
      this.p.getMoveset().clear();
      int numMoves = 0;
      GuiTabCompleteTextField[] var2 = this.tbMoves;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         GuiTextField tbMove = var2[var4];
         String moveText = tbMove.func_146179_b();
         if (!moveText.isEmpty()) {
            if (!Attack.hasAttack(moveText)) {
               tbMove.func_146180_a("");
               return false;
            }

            this.p.getMoveset().set(this.getNextAvailablePosition(), new Attack(moveText));
         }
      }

      for(int i = 0; i < 4; ++i) {
         if (this.p.getMoveset().get(i) != null) {
            ++numMoves;
         }
      }

      if (numMoves == 0) {
         return false;
      } else {
         return true;
      }
   }

   public int getNextAvailablePosition() {
      int size = this.p.getMoveset().size();
      return size == 4 ? -1 : size;
   }

   public abstract List getPokemonList();

   public String getExportText() {
      return ImportExportConverter.getExportText(this.p);
   }

   public String importText(String importText) {
      try {
         Pokemon importedPokemon = ImportExportConverter.importText(importText);
         if (importedPokemon != null) {
            importedPokemon.setUUID(this.p.getUUID());
            this.p = importedPokemon;
            return null;
         } else {
            return "Error";
         }
      } catch (ShowdownImportException var3) {
         return var3.field.errorCode;
      }
   }

   public GuiScreen getScreen() {
      return this;
   }
}
