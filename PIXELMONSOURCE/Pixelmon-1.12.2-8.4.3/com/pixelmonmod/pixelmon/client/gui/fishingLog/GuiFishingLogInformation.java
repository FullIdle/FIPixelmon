package com.pixelmonmod.pixelmon.client.gui.fishingLog;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiFishingLogInformation extends GuiScreen {
   private Pokemon pokemon;
   private int[] encountered;
   private int category;
   private double scalar = 10900.0;
   private EntityPixelmon displayedPokemon;
   private int centerX;
   private int centerY;
   private float guiHeight;
   private float guiWidth;
   private GuiButton okButton;
   private List bubbles = Lists.newArrayList();

   public GuiFishingLogInformation(int dex, int category, int form, int... data) {
      this.category = category;
      EnumSpecies species = EnumSpecies.getFromDex(dex);
      PokemonSpec spec = FormLogRegistry.getFormFromId(species, form);
      this.pokemon = Pixelmon.pokemonFactory.create(species);
      spec.apply(this.pokemon);
      this.encountered = Arrays.copyOfRange(data, 3, data.length);
   }

   public GuiFishingLogInformation(int dex, int category, PokemonSpec form, int[] data) {
      this.category = category;
      EnumSpecies species = EnumSpecies.getFromDex(dex);
      this.pokemon = Pixelmon.pokemonFactory.create(species);
      form.apply(this.pokemon);
      this.encountered = data;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerX = this.field_146294_l / 2;
      this.centerY = this.field_146295_m / 2;
      this.guiHeight = 220.0F;
      this.guiWidth = this.guiHeight * 0.698571F;
      int height = 20;
      int width = (int)((double)height * 5.338235294117647);
      this.okButton = this.func_189646_b(new GuiButton(0, this.centerX - width / 2, this.centerY + 70, width, height, ""));
      this.bubbles = Lists.newArrayList();
      this.setupDisplayedPokemon();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.bindFontRenderer("pixelmon:textures/gui/pokemonfont.png", true);
      if (this.bubbles.size() < 8 && RandomHelper.getRandomChance(0.05)) {
         int size = RandomHelper.getRandomNumberBetween(5, 10);
         this.bubbles.add(new GuiBubbleObject((float)(this.centerX + RandomHelper.getRandomNumberBetween(-65, 65 - size)), (float)this.centerY, (float)size));
      }

      this.drawScreenBackground();
      this.drawScreenForeground(mouseX, mouseY, partialTicks);
      GuiHelper.resetFontRenderer();
   }

   public void drawScreenBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogWater);
      float borderWidth = 1.0F;
      float width = this.guiWidth - borderWidth * 2.0F;
      GuiHelper.drawImageQuad((double)((float)this.centerX - this.guiWidth / 2.0F + borderWidth), (double)((float)this.centerY - this.guiHeight / 2.0F + borderWidth), (double)width, width * 0.73F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GL11.glPushMatrix();
      ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
      float x1 = ((float)this.centerX - this.guiWidth / 2.0F) * (float)sr.func_78325_e();
      float x2 = ((float)this.centerX + this.guiWidth / 2.0F) * (float)sr.func_78325_e();
      float y1 = ((float)this.centerY - this.guiHeight / 2.0F + 1.0F) * (float)sr.func_78325_e();
      float y2 = (float)(this.centerY * sr.func_78325_e());
      GL11.glScissor((int)x1, (int)((float)Display.getHeight() - y2), (int)(x2 - x1), (int)(y2 - y1));
      GL11.glEnable(3089);

      for(int i = 0; i < this.bubbles.size(); ++i) {
         GuiBubbleObject bubble = (GuiBubbleObject)this.bubbles.get(i);
         bubble.render();
         if (bubble.getY() < (float)this.centerY - this.guiHeight / 2.0F - 20.0F) {
            this.bubbles.remove(i--);
         }
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogInformationBackground);
      GuiHelper.drawImageQuad((double)((float)this.centerX - this.guiWidth / 2.0F), (double)((float)this.centerY - this.guiHeight / 2.0F), (double)this.guiWidth, this.guiHeight, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   public void drawScreenForeground(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogConfirmButton);
      GuiHelper.drawImageQuad((double)this.okButton.field_146128_h, (double)this.okButton.field_146129_i, (double)this.okButton.field_146120_f, (float)this.okButton.field_146121_g, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GuiHelper.drawCenteredString("Ok", (float)this.centerX, (float)(this.okButton.field_146129_i + 6), this.okButton.func_146116_c(this.field_146297_k, mouseX, mouseY) ? -12687348 : -1);
      GuiHelper.drawScaledCenteredString(this.pokemon.getFormEnum().getLocalizedName(), (float)this.centerX, (float)(this.centerY + 1), -7781367, 14.0F);
      GuiHelper.drawScaledCenteredSplitString(I18n.func_74838_a(this.pokemon.getFormEnum().getUnlocalizedName() + ".description"), (float)this.centerX, (float)(this.centerY + 30), -9945576, 12.0F, 140, false);
      this.drawEntityToScreen(this.centerX, this.centerY - 30, 70, this.displayedPokemon, mouseX, partialTicks);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      Minecraft.func_71410_x().func_147108_a(new GuiFishingLog(this.pokemon.getSpecies().getNationalPokedexInteger(), this.encountered));
   }

   private void setupDisplayedPokemon() {
      if (this.field_146297_k != null) {
         this.displayedPokemon = new EntityPixelmon(this.field_146297_k.field_71441_e);
         this.displayedPokemon.func_70107_b(this.field_146297_k.field_71439_g.field_70165_t, this.field_146297_k.field_71439_g.field_70163_u, this.field_146297_k.field_71439_g.field_70161_v);
         this.displayedPokemon.setPokemon(this.pokemon);
         this.displayedPokemon.getPokemonData().setGrowth(EnumGrowth.Ordinary);
         this.displayedPokemon.checkAnimation();
         this.displayedPokemon.setAnimation(AnimationType.SWIM);
         this.displayedPokemon.initAnimation();
         double referenceW = 50.0;
         double referenceH = 50.0;
         double dh = (double)this.displayedPokemon.field_70131_O - referenceH;
         double dw = (double)this.displayedPokemon.field_70130_N - referenceW;
         if (dh > dw) {
            this.scalar = referenceH / (double)this.displayedPokemon.field_70131_O;
         } else {
            this.scalar = referenceW / (double)this.displayedPokemon.field_70130_N;
         }

      }
   }

   public void func_73876_c() {
      if (this.displayedPokemon != null && this.displayedPokemon.getAnimationVariables() != null) {
         this.displayedPokemon.getAnimationVariables().tick();
      }

   }

   private void drawEntityToScreen(int x, int y, int l, EntityPixelmon e, int mouseX, float pt) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179142_g();
      GlStateManager.func_179126_j();
      GlStateManager.func_179109_b((float)x, (float)y, 100.0F);
      GlStateManager.func_179139_a(this.scalar, this.scalar, this.scalar);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179114_b((float)(x - mouseX) / 2.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();

      try {
         RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
         Render entityClassRenderObject = renderManager.func_78715_a(EntityPixelmon.class);
         RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
         rp.renderPixelmon(e, 0.0, 0.0, 0.0, pt, true);
         renderManager.field_78735_i = 180.0F;
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      GlStateManager.func_179114_b(360.0F - (float)(x - mouseX) / 2.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179139_a(1.0 / this.scalar, 1.0 / this.scalar, 1.0 / this.scalar);
      GlStateManager.func_179121_F();
   }
}
