package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.awt.Color;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.GL11;

public class GuiScreenPokeChecker extends GuiScreen {
   private static final Color[] DYNAMAX_LEVELS = new Color[]{new Color(3601150), new Color(6941951), new Color(7984383), new Color(6142719), new Color(11375103), new Color(13274879), new Color(15948030), new Color(16662225), new Color(16721836), new Color(16654730)};
   protected int xSize;
   protected int ySize;
   protected int guiLeft;
   protected int guiTop;
   protected PokemonStorage storage;
   protected StoragePosition position;
   protected Pokemon pokemon;
   @Nullable
   protected GuiScreen parent;
   private GuiButton renameButton;

   public GuiScreenPokeChecker(PokemonStorage storage, StoragePosition position, @Nullable GuiScreen parent) {
      this.xSize = 176;
      this.ySize = 166;
      this.storage = (PokemonStorage)Preconditions.checkNotNull(storage);
      this.position = (StoragePosition)Preconditions.checkNotNull(position);
      this.pokemon = (Pokemon)Preconditions.checkNotNull(storage.get(position));
      this.parent = parent;
      this.renameButton = new GuiButton(4, 0, 0, 50, 20, I18n.func_135052_a("gui.screenpokechecker.rename", new Object[0]));
      this.renameButton.field_146125_m = false;
   }

   GuiScreenPokeChecker(GuiScreenPokeChecker tab) {
      this(tab.storage, tab.position, tab.parent);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.guiLeft = (this.field_146294_l - this.xSize) / 2;
      this.guiTop = (this.field_146295_m - this.ySize) / 2;
      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiPokeCheckerTabs(3, 0, this.field_146294_l / 2 + 107, this.field_146295_m / 2 + 80, 17, 15, ""));
      this.field_146292_n.add(new GuiPokeCheckerTabs(0, 1, this.field_146294_l / 2 - 34 - 89 - 4, this.field_146295_m / 2 + 80, 89, 15, I18n.func_135052_a("gui.screenpokechecker.summary", new Object[0])));
      this.field_146292_n.add(new GuiPokeCheckerTabs(1, 2, this.field_146294_l / 2 - 34, this.field_146295_m / 2 + 80, 69, 15, I18n.func_135052_a("gui.screenpokechecker.moves", new Object[0])));
      this.field_146292_n.add(new GuiPokeCheckerTabs(2, 3, this.field_146294_l / 2 + 36, this.field_146295_m / 2 + 80, 69, 15, I18n.func_135052_a("gui.screenpokechecker.stats", new Object[0])));
      this.field_146292_n.add(new GuiPokeCheckerTabs(4, 5, this.field_146294_l / 2 - 44, this.field_146295_m / 2 - 107, 9, 9, "", this.pokemon));
      this.field_146292_n.add(new GuiPokeCheckerTabs(7, 6, this.field_146294_l / 2 - 44, this.field_146295_m / 2 - 1, 9, 8, "", this.pokemon));
      this.field_146292_n.add(this.renameButton);
      this.field_146292_n.add(new GuiPokeCheckerTabs(8, 7, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 80, 14, 14, ""));
      this.field_146292_n.add(new GuiPokeCheckerTabs(9, 8, this.field_146294_l / 2 - 56, this.field_146295_m / 2 - 80, 16, 14, ""));
   }

   public void func_146284_a(GuiButton button) {
      switch (button.field_146127_k) {
         case 0:
            this.closeScreen();
            break;
         case 1:
            this.field_146297_k.func_147108_a(new GuiScreenPokeChecker(this));
            break;
         case 2:
            this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerMoves(this));
            break;
         case 3:
            this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerStats(this));
            break;
         case 4:
            if (PixelmonConfig.allowNicknames && !this.pokemon.isEgg()) {
               this.field_146297_k.func_147108_a(new GuiRenamePokemon(this));
            }
            break;
         case 5:
            this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerWarningLevel(this));
            break;
         case 6:
            if (PixelmonConfig.allowNicknames && !this.pokemon.isEgg()) {
               this.field_146297_k.func_147108_a(new GuiRenamePokemon(this));
            }
            break;
         case 7:
            this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerTMs(this));
            break;
         case 8:
            this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerRibbons(this));
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.closeScreen();
      }

      super.func_73869_a(typedChar, keyCode);
   }

   private void closeScreen() {
      if (this.parent == null) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         this.field_146297_k.func_147108_a(this.parent);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      if (mouseX > this.field_146294_l / 2 - 125 && mouseX < this.field_146294_l / 2 - 40 && mouseY > this.field_146295_m / 2 - 15 && mouseY < this.field_146295_m / 2 + 5) {
         if (mouseButton == 1) {
            this.renameButton.field_146125_m = true;
            this.renameButton.field_146128_h = mouseX;
            this.renameButton.field_146129_i = mouseY;
         } else {
            this.renameButton.field_146125_m = false;
         }
      }

      this.arrowsMouseClicked(mouseX, mouseY);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      int i = this.guiLeft;
      int j = this.guiTop;
      this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
      GlStateManager.func_179101_C();
      RenderHelper.func_74518_a();
      GlStateManager.func_179140_f();
      GlStateManager.func_179097_i();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      RenderHelper.func_74520_c();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)i, (float)j, 0.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179091_B();
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.func_74518_a();
      this.drawGuiContainerForegroundLayer(mouseX, mouseY);
      RenderHelper.func_74520_c();
      GlStateManager.func_179121_F();
      GlStateManager.func_179145_e();
      GlStateManager.func_179126_j();
      RenderHelper.func_74519_b();
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      if (!this.pokemon.isEgg()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + this.pokemon.getLevel(), 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, 16777215);
         if (this.pokemon.getHealth() <= 0) {
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.fainted", new Object[0]), 117, -13, 16777215);
         } else {
            this.func_73732_a(this.field_146297_k.field_71466_p, this.pokemon.getHealth() + "/" + this.pokemon.getMaxHealth(), 140, -14, 14540253);
         }
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " ???", 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " ???", -30, -14, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, "???/???", 140, -13, 14540253);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.status", new Object[0]), -9, 111, 16777215);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.texp", new Object[0]), 107, 32, 16777215);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.levelup", new Object[0]), 134, 56, 16777215);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.dynamaxlevel", new Object[0]), 134, 80, 16777215);
      if (!this.pokemon.isEgg()) {
         String xp = String.valueOf(this.pokemon.getExperience());
         String xptl = String.valueOf(this.pokemon.getExperienceToLevelUp());
         this.field_146297_k.field_71466_p.func_78276_b(xp, 135 - this.field_146297_k.field_71466_p.func_78256_a(xp) / 2, 44, 16777215);
         this.field_146297_k.field_71466_p.func_78276_b(xptl, 135 - this.field_146297_k.field_71466_p.func_78256_a(xptl) / 2, 68, 16777215);
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.parallelogram);

         for(int i = 0; i < 10; ++i) {
            if (this.pokemon.getDynamaxLevel() > i) {
               Color c = DYNAMAX_LEVELS[i];
               GlStateManager.func_179131_c((float)c.getRed() / 256.0F, (float)c.getGreen() / 256.0F, (float)c.getBlue() / 256.0F, 1.0F);
            } else {
               GlStateManager.func_179131_c(0.5F, 0.5F, 0.5F, 1.0F);
            }

            GuiHelper.drawImageQuad((double)(82 + i * 10), 91.0, 13.0, 13.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         GlStateManager.func_179084_k();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 135, 44, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 135, 68, 16777215);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.ability", new Object[0]), 75, 116, 16777215);
      if (!this.pokemon.isEgg()) {
         try {
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("ability." + this.pokemon.getAbility().getName() + ".name", new Object[0]), 130, 116, 16777215);
            this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("ability." + this.pokemon.getAbility().getName() + ".description", new Object[0]), 62, 131, 145, 16777215);
         } catch (Exception var7) {
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("ability.ComingSoon.name", new Object[0]), 130, 117, 16777215);
            this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("ability.ComingSoon.description", new Object[0]), 62, 131, 145, 16777215);
         }
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("ability.Egg.name", new Object[0]), 130, 116, 16777215);
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a(this.pokemon.getEggDescription(), new Object[0]), 62, 131, 145, 16777215);
      }

      this.drawBasePokemonInfo();
   }

   protected void drawBasePokemonInfo() {
      GuiHelper.bindPokemonSprite(this.pokemon, this.field_146297_k);
      GuiHelper.drawImageQuad(-27.0, 14.0, 68.0, 68.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (!this.pokemon.isEgg()) {
         EnumType type1 = this.pokemon.getBaseStats().getType1();
         EnumType type2 = this.pokemon.getBaseStats().getType2();
         float x = type1.textureX;
         float y = type1.textureY;
         float x1 = 0.0F;
         float y1 = 0.0F;
         if (type2 != null) {
            x1 = type2.textureX;
            y1 = type2.textureY;
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
         if (type2 != EnumType.Mystery && type2 != null) {
            GuiHelper.drawImageQuad(8.0, 0.0, 21.0, 21.0F, (double)(x1 / 1792.0F), (double)(y1 / 768.0F), (double)((x1 + 240.0F) / 1792.0F), (double)((y1 + 240.0F) / 768.0F), this.field_73735_i);
            GuiHelper.drawImageQuad(-14.0, 0.0, 21.0, 21.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
         } else {
            GuiHelper.drawImageQuad(-2.0, 0.0, 21.0, 21.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
         }
      }

      if (this.pokemon.isShiny()) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shiny);
         GuiHelper.drawImageQuad(-35.0, 73.0, 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

      if (this.pokemon.getPokerus() != null) {
         this.field_146297_k.field_71446_o.func_110577_a(this.pokemon.getPokerus().canInfect() ? GuiResources.pokerusInfectious : GuiResources.pokerusCured);
         GuiHelper.drawImageQuad(40.0, 72.0, 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

      GlStateManager.func_179084_k();
   }

   public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summarySummary);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, (this.field_146295_m - this.ySize) / 2 - 25, 0, 0, 256, 205);
      this.drawHealthBar((this.field_146294_l - this.xSize) / 2 + 59, (this.field_146295_m - this.ySize) / 2 - 18, 154, 14, this.pokemon);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 + 59, (this.field_146295_m - this.ySize) / 2 - 18, 103, 222, 150, 16);
      this.drawExpBar((this.field_146294_l - this.xSize) / 2 + 86, (this.field_146295_m - this.ySize) / 2, 122, 14, this.pokemon);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 + 59, (this.field_146295_m - this.ySize) / 2, 104, 239, 150, 16);
      float[] texturePair = StatusType.getTexturePos(this.pokemon.getStatus().type);
      float textureX1 = texturePair[0];
      float textureY1 = texturePair[1];
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
      if (textureX1 != -1.0F) {
         GuiHelper.drawImageQuad((double)((float)(this.field_146294_l - this.xSize) / 2.0F - 6.0F), (double)((float)(this.field_146295_m - this.ySize) / 2.0F + 130.0F), 24.0, 24.0F, (double)(textureX1 / 768.0F), (double)(textureY1 / 512.0F), (double)((textureX1 + 240.0F) / 768.0F), (double)((textureY1 + 240.0F) / 512.0F), this.field_73735_i);
      } else {
         GuiHelper.drawImageQuad((double)((float)(this.field_146294_l - this.xSize) / 2.0F - 6.0F), (double)((float)(this.field_146295_m - this.ySize) / 2.0F + 130.0F), 24.0, 24.0F, 0.34375, 0.515625, 0.65625, 0.984375, this.field_73735_i);
      }

      this.drawPokemonName();
      this.drawArrows(mouseX, mouseY);
      GlStateManager.func_179084_k();
   }

   protected void drawPokemonName() {
      if (this.pokemon.isEgg()) {
         this.drawCenteredStringWithoutShadow(Entity1Base.getLocalizedName("Egg"), (this.field_146294_l - this.xSize) / 2 + 7, (this.field_146295_m - this.ySize) / 2 + 89, 16777215);
      } else {
         int textSize = false;
         int offset = this.pokemon.hasGigantamaxFactor() ? 9 : 0;
         int textSize;
         if (this.pokemon.getNickname() != null) {
            String ogName = "(" + this.pokemon.getSpecies().getLocalizedName() + ")";
            this.drawCenteredStringWithoutShadow(ogName, (this.field_146294_l - this.xSize) / 2 + 7 + offset, (this.field_146295_m - this.ySize) / 2 + 93, 16777215);
            this.drawCenteredStringWithoutShadow(this.pokemon.getNickname(), (this.field_146294_l - this.xSize) / 2 + 7 + offset, (this.field_146295_m - this.ySize) / 2 + 84, 16777215);
            textSize = Math.max(this.field_146289_q.func_78256_a(this.pokemon.getNickname()), this.field_146289_q.func_78256_a(ogName));
         } else {
            this.drawCenteredStringWithoutShadow(this.pokemon.getDisplayName(), (this.field_146294_l - this.xSize) / 2 + 7 + offset, (this.field_146295_m - this.ySize) / 2 + 89, 16777215);
            textSize = this.field_146289_q.func_78256_a(this.pokemon.getDisplayName());
         }

         if (this.pokemon.hasGigantamaxFactor()) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.gmaxFactor);
            GlStateManager.func_179141_d();
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GuiHelper.drawImageQuad((double)((float)(this.field_146294_l - this.xSize) / 2.0F - (float)textSize / 2.0F - 3.0F), (double)((float)(this.field_146295_m - this.ySize) / 2.0F + 84.0F), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }
      }

   }

   public void arrowsMouseClicked(int mouseX, int mouseY) {
      int l1 = (this.field_146294_l - this.xSize) / 2 + 220;
      int l2 = (this.field_146294_l - this.xSize) / 2 - 62;
      int w = 16;
      int t = (this.field_146295_m - this.ySize) / 2;
      int h = 21;
      if (mouseY > t && mouseY < t + h) {
         if (mouseX > l1 && mouseX < l1 + w) {
            SoundHelper.playButtonPressSound();
            this.setNextPokemon();
         }

         if (mouseX > l2 && mouseX < l2 + w) {
            SoundHelper.playButtonPressSound();
            this.setPrevPokemon();
         }
      }

   }

   private void setPrevPokemon() {
      Tuple previous = ClientStorageManager.getPrevious(this.storage, this.position);
      if (previous != null && previous.func_76341_a() != null) {
         this.pokemon = (Pokemon)previous.func_76341_a();
         this.position = (StoragePosition)previous.func_76340_b();
      }

   }

   private void setNextPokemon() {
      Tuple previous = ClientStorageManager.getNext(this.storage, this.position);
      if (previous != null && previous.func_76341_a() != null) {
         this.pokemon = (Pokemon)previous.func_76341_a();
         this.position = (StoragePosition)previous.func_76340_b();
      }

   }

   public void drawArrows(int mouseX, int mouseY) {
      if (this.position.box == -1) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryMoves);
         int l1 = (this.field_146294_l - this.xSize) / 2 + 220;
         int l2 = (this.field_146294_l - this.xSize) / 2 - 62;
         int w = 16;
         int t = (this.field_146295_m - this.ySize) / 2;
         int h = 21;
         this.func_73729_b(l1, t, 24, 207, w, h);
         this.func_73729_b(l2, t, 42, 207, w, h);
         if (mouseY > t && mouseY < t + h) {
            if (mouseX > l1 && mouseX < l1 + w) {
               this.func_73729_b(l1, t, 60, 207, w, h);
            }

            if (mouseX > l2 && mouseX < l2 + w) {
               this.func_73729_b(l2, t, 78, 207, w, h);
            }
         }

      }
   }

   private void drawExpBar(int x, int y, int width, int height, Pokemon pokemon) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      int barWidth;
      if (!pokemon.isEgg()) {
         barWidth = (int)(pokemon.getExperienceFraction() * ((float)width - 6.0F));
      } else {
         barWidth = (int)(0.0F * ((float)width - 6.0F));
      }

      if (pokemon.getLevel() == PixelmonServerConfig.maxLevel) {
         barWidth = 0;
      }

      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(0.0F, 0.0F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(0.0F, 0.0F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181666_a(0.0F, 0.0F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + width), (double)y, 0.0).func_181666_a(0.0F, 0.0F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(0.3F, 1.0F, 1.0F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(0.3F, 1.0F, 1.0F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(0.3F, 1.0F, 1.0F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(0.3F, 1.0F, 1.0F, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }

   public void drawHealthBar(int x, int y, int width, int height, Pokemon pokemon) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      int barWidth = width - 6;
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      float healthFraction = pokemon.isEgg() ? 1.0F : (float)pokemon.getHealth() / (float)pokemon.getMaxHealth();
      barWidth = (int)(healthFraction * ((float)width - 24.0F)) + 18;
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }

   public void drawCenteredStringWithoutShadow(String text, int x, int y, int color) {
      this.field_146297_k.field_71466_p.func_78276_b(text, x - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, y, color);
   }
}
