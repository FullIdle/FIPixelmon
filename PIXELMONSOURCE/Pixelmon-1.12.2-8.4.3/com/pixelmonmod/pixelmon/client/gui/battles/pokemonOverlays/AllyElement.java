package com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.gui.GuiElement;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiParticleEngine;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AllyElement extends GuiElement {
   private static final ResourceLocation ALLY = new ResourceLocation("pixelmon", "textures/gui/battle/ally.png");
   private static final ResourceLocation RAID = new ResourceLocation("pixelmon", "textures/gui/battle/raid.png");
   private static final ResourceLocation BARRIER = new ResourceLocation("pixelmon", "textures/gui/raids/barrier.png");
   private static final ResourceLocation SHINY = new ResourceLocation("pixelmon", "textures/gui/battle/shiny.png");
   private static final ResourceLocation HEALTHY = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_healthy.png");
   private static final ResourceLocation CAUTION = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_caution.png");
   private static final ResourceLocation WARNING = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_warning.png");
   private static final ResourceLocation TARGET = new ResourceLocation("pixelmon", "textures/gui/battle/target_down.png");
   private final PixelmonInGui ally;
   private final GuiScreen parent;
   private final GuiParticleEngine particleEngine;

   public AllyElement(PixelmonInGui ally, GuiScreen parent, GuiParticleEngine particleEngine) {
      this.ally = ally;
      this.parent = parent;
      this.particleEngine = particleEngine;
   }

   public PixelmonInGui getAlly() {
      return this.ally;
   }

   public void drawSelected(int x, int y, int width, int height, float scale) {
      this.setPosition(x, y, width, height);
      GlStateManager.func_179094_E();
      double tX = (double)this.parent.field_146294_l;
      double tY = (double)((float)(y + height) + 2.0F / scale);
      GlStateManager.func_179137_b(tX, tY, 0.0);
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179137_b(-tX, -tY, 0.0);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImage(TARGET, (double)x + 131.5, (double)(y - 16), 26.66666603088379, 11.0, this.zLevel);
      GlStateManager.func_179121_F();
      this.setPosition((int)((float)x * scale), (int)((float)y * scale), (int)((float)width * scale), (int)((float)height * scale));
   }

   public void drawElement(float scale) {
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImage(ALLY, (double)this.x, (double)(this.y + 5), 160.0, 50.0, this.zLevel);
      float healthPercent = this.ally.health / (float)this.ally.maxHealth;
      GuiHelper.drawBar((double)(this.x + 13), (double)(this.y + 28), 109.0, 7.0, healthPercent, this.ally.getHealthColor(), true);
      float barWidth = this.ally.level < PixelmonServerConfig.maxLevel ? this.ally.expFraction : 1.0F;
      GuiHelper.drawBar((double)(this.x + 13), (double)(this.y + 36), 111.0, 2.0, barWidth, new Color(69, 215, 255), true);
      this.parent.field_146297_k.func_110434_K().func_110577_a(healthPercent <= 0.5F ? (healthPercent <= 0.25F ? WARNING : CAUTION) : HEALTHY);
      GuiHelper.drawImage((double)(this.x + 115), (double)(this.y - 2), 60.0, 60.0, this.zLevel);
      GuiHelper.bindPokemonSprite(this.ally, this.parent.field_146297_k);
      GuiHelper.drawImage((double)(this.x + 125), (double)(this.y + 2), 40.0, 40.0, this.zLevel);
      this.particleEngine.drawAtOffset(this.ally.pokemonUUID.toString(), (double)(this.x + 130), (double)(this.y + 7), (double)RandomHelper.rand.nextInt(26), (double)RandomHelper.rand.nextInt(26));
      float offset = 0.0F;
      if (this.ally.getGender() != Gender.None) {
         this.parent.field_146297_k.func_110434_K().func_110577_a(this.ally.getGender() == Gender.Male ? GuiResources.male : GuiResources.female);
         GuiHelper.drawImage((double)(this.x + 12), (double)((float)this.y + 13.5F), 5.0, 8.0, this.zLevel);
         offset += 7.0F;
      }

      GuiHelper.drawScaledString(this.ally.getDisplayName(), (float)(this.x + 12) + offset, (float)this.y + 14.0F, this.ally.shiny ? -7545 : -986896, 16.0F);
      GuiHelper.drawScaledStringRightAligned("Lv." + this.ally.level, (float)(this.x + 114), (float)this.y + 15.0F, -986896, false, 12.0F);
      if (PixelmonConfig.advancedBattleInformation) {
         GuiHelper.drawScaledString((int)this.ally.health + "/" + this.ally.maxHealth, (float)(this.x + 15), (float)this.y + 29.0F, -986896, 11.0F);
      }

      if (this.ally.status != -1 && StatusType.getEffect(this.ally.status) != null) {
         float[] texturePair2 = StatusType.getTexturePos(StatusType.getEffect(this.ally.status));
         this.parent.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
         GuiHelper.drawImageQuad((double)((float)(this.x + 12 + GuiHelper.getStringWidth(this.ally.getDisplayName())) + offset + 2.0F), (double)this.y + 12.5, 10.5, 10.5F, (double)(texturePair2[0] / 768.0F), (double)(texturePair2[1] / 512.0F), (double)((texturePair2[0] + 240.0F) / 768.0F), (double)((texturePair2[1] + 240.0F) / 512.0F), this.zLevel);
      }

      if (this.ally.shiny && RandomHelper.rand.nextInt(80) == 0) {
         int size = 7 + RandomHelper.rand.nextInt(7);
         this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle(this.ally.pokemonUUID.toString(), SHINY, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0F, 0.8F, 0.3F, 0.0F, (float)size, (float)size, 120, (particle) -> {
            int x = particle.age;
            int m = particle.maxAge;
            int h = m / 2;
            particle.a = (float)(x <= h ? x : h - (x - h)) / (float)h;
         }));
      }

   }
}
