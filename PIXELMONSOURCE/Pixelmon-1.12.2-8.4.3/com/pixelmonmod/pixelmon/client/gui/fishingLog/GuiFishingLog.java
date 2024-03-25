package com.pixelmonmod.pixelmon.client.gui.fishingLog;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiFishingLog extends GuiScreen {
   private int centerX;
   private int centerY;
   private float guiHeight;
   private float guiWidth;
   private EnumSpecies species;
   private int[] encountered;
   private LinkedHashMultimap categories = LinkedHashMultimap.create();
   private BiMap buttons = HashBiMap.create();
   private int currentScroll = 0;
   private int waitingScroll = 0;
   private int viewedForm = -1;
   private double percentComplete = 0.0;
   int maxScroll;

   public GuiFishingLog(int species, int... data) {
      this.species = EnumSpecies.getFromDex(species);
      this.encountered = data;
      this.setCategories();
      int[] var3 = this.encountered;
      int var4 = var3.length;

      int i;
      for(i = 0; i < var4; ++i) {
         int i = var3[i];
         if (i > 0) {
            ++this.percentComplete;
         }
      }

      this.percentComplete /= (double)data.length;
      this.maxScroll = 40;
      Iterator var7 = this.categories.keySet().iterator();

      while(var7.hasNext()) {
         Integer category = (Integer)var7.next();
         this.maxScroll += 32;

         for(i = 0; i < this.categories.get(category).size(); ++i) {
            if (i % 3 == 0 || i == this.categories.get(category).size()) {
               this.maxScroll += 65;
            }
         }
      }

   }

   public void setCategories() {
      Multimap c = FormLogRegistry.getFormsForSpecies(this.species);
      Iterator var2 = c.keySet().iterator();

      while(var2.hasNext()) {
         Integer integer = (Integer)var2.next();
         List specs = Lists.newArrayList();
         Iterator var5 = c.get(integer).iterator();

         while(var5.hasNext()) {
            PokemonSpec spec = (PokemonSpec)var5.next();
            int id = FormLogRegistry.getFormId(this.species, spec);
            if (this.encountered[id] > 0) {
               specs.add(spec);
            }
         }

         this.categories.putAll(integer, specs);
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerX = this.field_146294_l / 2;
      this.centerY = this.field_146295_m / 2;
      this.guiHeight = 220.0F;
      this.guiWidth = this.guiHeight * 0.698571F;
      Iterator var1 = this.categories.values().iterator();

      while(var1.hasNext()) {
         PokemonSpec spec = (PokemonSpec)var1.next();
         this.buttons.put(spec, this.func_189646_b(new GuiButton(FormLogRegistry.getFormId(this.species, spec), 0, 0, 40, 46, spec.toString())));
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.bindFontRenderer("pixelmon:textures/gui/pokemonfont.png", true);
      this.drawScreenBackground();
      this.processScroll();
      GL11.glPushMatrix();
      ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
      float x1 = ((float)this.centerX - this.guiWidth / 2.0F) * (float)sr.func_78325_e();
      float x2 = ((float)this.centerX + this.guiWidth / 2.0F) * (float)sr.func_78325_e();
      float y1 = ((float)this.centerY - this.guiHeight / 2.0F + 5.0F) * (float)sr.func_78325_e();
      float y2 = ((float)this.centerY + this.guiHeight / 2.0F - 5.0F) * (float)sr.func_78325_e();
      GL11.glScissor((int)x1, (int)((float)Display.getHeight() - y2), (int)(x2 - x1), (int)(y2 - y1));
      GL11.glEnable(3089);
      this.drawScreenForeground(mouseX, mouseY);
      GL11.glDisable(3089);
      GL11.glPopMatrix();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogTitle);
      int height = 15;
      float width = (float)height * 6.168067F;
      GuiHelper.drawImageQuad((double)((float)this.centerX - width / 2.0F), (double)(this.centerY - 117), (double)width, (float)height, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GuiHelper.drawScaledCenteredString(I18n.func_74837_a("gui.fishinglog.title", new Object[]{this.species.getLocalizedName()}), (float)this.centerX, (float)this.centerY - 112.5F, -7781367, 14.0F);
      GuiHelper.resetFontRenderer();
   }

   public void drawScreenBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(this.viewedForm == -1 ? GuiResources.fishingLogBackground : GuiResources.fishingLogInformationBackground);
      GuiHelper.drawImageQuad((double)((float)this.centerX - this.guiWidth / 2.0F), (double)((float)this.centerY - this.guiHeight / 2.0F), (double)this.guiWidth, this.guiHeight, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   public void drawScreenForeground(int mouseX, int mouseY) {
      float x = (float)this.centerX - this.guiWidth / 2.0F + 14.5F;
      AtomicInteger top = new AtomicInteger((int)((float)(-this.currentScroll + this.centerY) - this.guiHeight / 2.0F - 5.0F));
      DecimalFormat format = new DecimalFormat("###.#");
      GuiHelper.drawScaledString(I18n.func_74837_a("gui.fishinglog.percentage", new Object[]{format.format(100.0 * this.percentComplete)}), x + 5.0F, (float)top.addAndGet(21), -7781367, 10.0F);
      func_73734_a((int)x, top.get(), (int)x + 2, top.addAndGet(8), -1276390);
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
      Iterator var6 = this.categories.keySet().iterator();

      label37:
      while(var6.hasNext()) {
         Integer category = (Integer)var6.next();
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogCategory);
         GuiHelper.drawImageQuad((double)x, (double)top.addAndGet(14), 127.0, 14.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         GuiHelper.drawScaledString(I18n.func_74838_a("gui.fishinglog." + this.species.name.toLowerCase() + ".category" + category + ".name"), x + 4.0F, (float)top.addAndGet(4), -7781367, 10.0F);
         top.addAndGet(14);
         int i = 0;
         Iterator var9 = this.categories.get(category).iterator();

         while(true) {
            do {
               if (!var9.hasNext()) {
                  continue label37;
               }

               PokemonSpec form = (PokemonSpec)var9.next();
               GuiButton button = (GuiButton)this.buttons.get(form);
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogContainer);
               float height = 45.6F;
               float xPos = x + 43.5F * (float)(i % 3);
               GuiHelper.drawImageQuad((double)xPos, (double)top.get(), 40.0, height, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               button.field_146128_h = (int)xPos;
               button.field_146129_i = top.get();
               Pokemon pokemon = Pixelmon.pokemonFactory.create(this.species);
               form.apply(pokemon);
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(pokemon));
               GuiHelper.drawImageQuad((double)xPos, (double)(top.get() - 1), 39.0, 39.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               int id = button.field_146127_k + 1;
               GuiHelper.drawScaledString("No. " + id, xPos + (id < 10 ? 3.0F : 1.6F), (float)(top.get() + 2), -1, 7.0F);
               if (pokemon.isShiny()) {
                  GuiHelper.drawScaledCenteredSplitString(I18n.func_74838_a("gui.trainereditor.shiny"), xPos + 20.0F, (float)top.get() + height + 4.0F, -7845363, 8.0F, 50, false);
               } else {
                  GuiHelper.drawScaledCenteredSplitString(pokemon.getFormEnum().getLocalizedName(), xPos + 20.0F, (float)top.get() + height + 4.0F, -7845363, 8.0F, 50, false);
               }

               ++i;
            } while(i % 3 != 0 && i != this.categories.get(category).size());

            top.addAndGet(65);
         }
      }

   }

   public void processScroll() {
      int mustard = 6;
      if (this.waitingScroll != 0) {
         if (Math.abs(this.waitingScroll) >= mustard) {
            if (this.waitingScroll < 0) {
               this.waitingScroll += mustard;
               this.currentScroll -= mustard;
            } else {
               this.waitingScroll -= mustard;
               this.currentScroll += mustard;
            }
         } else if (this.waitingScroll < 0) {
            ++this.waitingScroll;
            --this.currentScroll;
         } else {
            --this.waitingScroll;
            ++this.currentScroll;
         }
      }

   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      if (this.viewedForm == -1) {
         int i = Mouse.getEventDWheel();
         int speed = 20;
         if (i < 0) {
            this.waitingScroll += speed;
            if (this.currentScroll + this.waitingScroll > this.maxScroll - 240) {
               this.waitingScroll = this.maxScroll - 240 - this.currentScroll;
            }
         } else if (i > 0) {
            this.waitingScroll -= speed;
            if (this.currentScroll + this.waitingScroll < 0) {
               this.waitingScroll = -this.currentScroll;
            }
         }
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      PokemonSpec form = (PokemonSpec)this.buttons.inverse().get(button);
      int category = this.categories.entries().stream().filter((e) -> {
         return ((PokemonSpec)e.getValue()).equals(form);
      }).mapToInt(Map.Entry::getKey).findFirst().orElse(0);
      Minecraft.func_71410_x().func_147108_a(new GuiFishingLogInformation(this.species.getNationalPokedexInteger(), category, form, this.encountered));
   }
}
