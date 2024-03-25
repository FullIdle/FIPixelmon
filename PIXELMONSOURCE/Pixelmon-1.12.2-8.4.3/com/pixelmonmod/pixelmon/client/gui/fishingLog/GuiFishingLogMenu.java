package com.pixelmonmod.pixelmon.client.gui.fishingLog;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiFishingLogMenu extends GuiScreen {
   private int centerX;
   private int centerY;
   private float guiHeight;
   private float guiWidth;
   private int currentScroll = 0;
   private int waitingScroll = 0;
   int maxScroll;
   private BiMap buttons = HashBiMap.create();
   HashMap map = new HashMap();
   HashMap percent = new HashMap();

   public GuiFishingLogMenu(int[] data) {
      int currentSpecies = -1;
      int total = -1;
      int count = 0;
      int[] arr = new int[0];
      double seen = 0.0;

      for(int i = 1; i < data.length; ++i) {
         if (currentSpecies == -1) {
            currentSpecies = data[i];
         } else if (total == -1) {
            total = data[i];
            arr = new int[data[i]];
         } else {
            if (data[i] > 0) {
               ++seen;
            }

            arr[count] = data[i];
            ++count;
         }

         if (count == total) {
            this.percent.put(currentSpecies, BigDecimal.valueOf(seen * 100.0 / (double)total).setScale(2, RoundingMode.HALF_UP).doubleValue());
            seen = 0.0;
            this.map.put(currentSpecies, arr);
            currentSpecies = -1;
            total = -1;
            count = 0;
         }
      }

      this.maxScroll = 40 + 65 * (this.map.values().size() / 3);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerX = this.field_146294_l / 2;
      this.centerY = this.field_146295_m / 2;
      this.guiHeight = 220.0F;
      this.guiWidth = this.guiHeight * 0.698571F;
      this.map.forEach((id, data) -> {
         EnumSpecies species = EnumSpecies.getFromDex(id);
         this.buttons.put(species, this.func_189646_b(new GuiButton(species.getNationalPokedexInteger(), 0, 0, 40, 46, species.toString())));
      });
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
      GuiHelper.drawScaledCenteredString(I18n.func_74838_a("gui.fishinglog.menu.title"), (float)this.centerX, (float)this.centerY - 112.5F, -7781367, 14.0F);
      GuiHelper.resetFontRenderer();
   }

   private void drawScreenForeground(int mouseX, int mouseY) {
      float x = (float)this.centerX - this.guiWidth / 2.0F + 14.5F;
      AtomicInteger top = new AtomicInteger((int)((float)(-this.currentScroll + this.centerY) - this.guiHeight / 2.0F - 5.0F) + 45);
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
      int i = 0;
      Iterator var6 = this.buttons.values().iterator();

      while(var6.hasNext()) {
         GuiButton button = (GuiButton)var6.next();
         EnumSpecies form = (EnumSpecies)this.buttons.inverse().get(button);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingMenuContainer);
         float height = 45.6F;
         float xPos = x + 63.5F * (float)(i % 2);
         GuiHelper.drawImageQuad((double)xPos, (double)top.get(), 60.0, height, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         button.field_146128_h = (int)xPos;
         button.field_146129_i = top.get();
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(form, 0, Gender.Male, false, false));
         if ((Double)this.percent.get(form.getNationalPokedexInteger()) == 0.0) {
            GL11.glColor3f(0.0F, 0.0F, 0.0F);
         }

         GuiHelper.drawImageQuad((double)(xPos + 10.0F), (double)(top.get() - 1), 39.0, 39.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GuiHelper.drawScaledString(this.percent.get(form.getNationalPokedexInteger()) + "%", xPos + 3.0F, (float)(top.get() + 2), -1, 7.0F);
         GuiHelper.drawScaledCenteredSplitString((Double)this.percent.get(form.getNationalPokedexInteger()) == 0.0 ? "???" : form.getLocalizedName(), xPos + 30.0F, (float)top.get() + height + 4.0F, -7845363, 10.0F, 50, false);
         ++i;
         if (i % 2 == 0) {
            top.addAndGet(65);
         }
      }

   }

   public void drawScreenBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.fishingLogBackground);
      GuiHelper.drawImageQuad((double)((float)this.centerX - this.guiWidth / 2.0F), (double)((float)this.centerY - this.guiHeight / 2.0F), (double)this.guiWidth, this.guiHeight, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
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
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      EnumSpecies form = (EnumSpecies)this.buttons.inverse().get(button);
      if (((int[])this.map.get(form.getNationalPokedexInteger())).length != 0) {
         Minecraft.func_71410_x().func_147108_a(new GuiFishingLog(form.getNationalPokedexInteger(), (int[])this.map.get(form.getNationalPokedexInteger())));
      }

   }
}
