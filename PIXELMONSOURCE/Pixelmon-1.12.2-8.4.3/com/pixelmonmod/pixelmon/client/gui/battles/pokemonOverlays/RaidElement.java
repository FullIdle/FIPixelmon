package com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiParticleEngine;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import java.text.DecimalFormat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class RaidElement extends OpponentElement {
   private static final ResourceLocation OPPONENT = new ResourceLocation("pixelmon", "textures/gui/battle/opponent.png");
   private static final ResourceLocation HEALTHY = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_healthy.png");
   private static final ResourceLocation CAUTION = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_caution.png");
   private static final ResourceLocation WARNING = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_warning.png");
   private static final ResourceLocation SHINY = new ResourceLocation("pixelmon", "textures/gui/battle/shiny.png");
   private static final ResourceLocation STAR = new ResourceLocation("pixelmon", "textures/gui/battle/star.png");
   private static final ResourceLocation CAUGHT = new ResourceLocation("pixelmon", "textures/gui/battle/caught.png");
   private static final ResourceLocation TARGET = new ResourceLocation("pixelmon", "textures/gui/battle/target_up.png");
   private static final ResourceLocation RAID = new ResourceLocation("pixelmon", "textures/gui/battle/raid.png");
   private static final ResourceLocation BARRIER = new ResourceLocation("pixelmon", "textures/gui/raids/barrier.png");
   private final GuiScreen parent;
   private final PixelmonInGui enemy;
   private final GuiParticleEngine particleEngine;

   public RaidElement(PixelmonInGui enemy, GuiScreen parent, GuiParticleEngine particleEngine) {
      super(enemy, parent, particleEngine);
      this.parent = parent;
      this.enemy = enemy;
      this.particleEngine = particleEngine;
   }

   public PixelmonInGui getEnemy() {
      return this.enemy;
   }

   public void drawSelected(int x, int y, int width, int height, float scale) {
      this.setPosition(x, y, width, height);
      GlStateManager.func_179094_E();
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImage(TARGET, (double)(x + 105), (double)(y + 75), 40.0, 16.5, this.zLevel);
      GlStateManager.func_179121_F();
      this.setPosition((int)((float)x * scale), (int)((float)y * scale), (int)((float)width * scale), (int)((float)height * scale));
   }

   public void drawElement(float scale) {
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImage(RAID, (double)this.x, (double)(this.y - 3), 250.0, 70.0, this.zLevel);
      float healthPercent = this.enemy.health / (float)this.enemy.maxHealth;
      GuiHelper.drawBar((double)(this.x + 6), (double)(this.y + 45), 244.0, 11.5, healthPercent, this.enemy.getHealthColor());
      if (this.enemy.status != -1 && StatusType.getEffect(this.enemy.status) != null) {
         float[] texturePair2 = StatusType.getTexturePos(StatusType.getEffect(this.enemy.status));
         this.parent.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
         GuiHelper.drawImageQuad((double)(this.x + 222), (double)(this.y + 12), 20.0, 20.0F, (double)(texturePair2[0] / 768.0F), (double)(texturePair2[1] / 512.0F), (double)((texturePair2[0] + 240.0F) / 768.0F), (double)((texturePair2[1] + 240.0F) / 512.0F), this.zLevel);
      }

      float offset = 0.0F;
      int size;
      if (this.enemy.shields > 0) {
         offset = 6.0F;

         for(size = 0; size < this.enemy.maxShields; ++size) {
            if (this.enemy.shields <= size) {
               GlStateManager.func_179131_c(0.4F, 0.4F, 0.4F, 1.0F);
               if (this.enemy.lostShield) {
                  this.enemy.lostShield = false;
                  float diffScale = 1.0F - scale;
                  float oX = 22.0F * diffScale * 0.5F;
                  float oY = 7.0F * diffScale * 0.5F;
                  SoundHelper.playSound(SoundEvents.field_187767_eL, 0.25F, 0.8F + this.parent.field_146297_k.field_71441_e.field_73012_v.nextFloat() * 0.4F);
                  this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle("", BARRIER, (double)(((float)(this.x + 128 + 18 * size) - (float)(18 * this.enemy.maxShields) / 2.0F - 2.0F) * scale - oX), ((double)this.y + 30.5) * (double)scale - (double)oY, 2.0, 0.0, 0.0, 0.0, 0.843F, 0.0314F, 0.478F, 0.0F, 22.0F, 7.0F, 40, (particle) -> {
                     particle.a = 1.0F - (float)particle.age / (float)particle.maxAge;
                     float growth = 0.5F * scale;
                     float yMult = 0.5F;
                     particle.x -= (double)growth;
                     particle.w += growth * 2.0F;
                     particle.y -= (double)(growth * yMult);
                     particle.h += growth * 2.0F * yMult;
                  }));
               }
            } else {
               GlStateManager.func_179131_c(0.843F, 0.0314F, 0.478F, 1.0F);
            }

            GuiHelper.drawImage(BARRIER, (double)((float)(this.x + 128 + 18 * size) - (float)(18 * this.enemy.maxShields) / 2.0F - 2.0F), (double)this.y + 30.5, 22.0, 7.0, 1.0F);
         }
      } else if (this.enemy.lostShield) {
         this.enemy.lostShield = false;
         SoundHelper.playSound(SoundEvents.field_187769_eM, 0.8F, 0.8F + this.parent.field_146297_k.field_71441_e.field_73012_v.nextFloat() * 0.4F);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.enemy.getGender() != Gender.None) {
         this.parent.field_146297_k.func_110434_K().func_110577_a(this.enemy.getGender() == Gender.Male ? GuiResources.male : GuiResources.female);
         GuiHelper.drawImage((double)(this.x + 8), (double)(this.y + 13), 10.0, 16.0, this.zLevel);
      }

      GuiHelper.drawScaledCenteredString(this.enemy.getDisplayName(), (float)(this.x + 128), (float)this.y + 14.0F - offset, this.enemy.shiny ? -7545 : -986896, 34.0F);
      if (PixelmonConfig.advancedBattleInformation) {
         DecimalFormat df = new DecimalFormat(".#");
         String percentage = df.format((double)healthPercent * 100.0).replace(".0", "");
         if (percentage.isEmpty()) {
            percentage = "0";
         }

         GuiHelper.drawScaledCenteredString(percentage + "%", (float)(this.x + 128), (float)this.y + 46.0F, -986896, 20.0F);
      }

      if (this.enemy.shiny && RandomHelper.rand.nextInt(80) == 0) {
         size = 7 + RandomHelper.rand.nextInt(7);
         this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle("", SHINY, (double)((float)this.parent.field_146294_l / 2.0F + (float)RandomHelper.rand.nextInt(80) - 40.0F), (double)this.parent.field_146295_m * 0.04 + (double)RandomHelper.rand.nextInt(16) - 10.0, 1.0, 0.0, 0.0, 0.0, 1.0F, 0.8F, 0.3F, 0.0F, (float)size, (float)size, 120, (particle) -> {
            int x = particle.age;
            int m = particle.maxAge;
            int h = m / 2;
            particle.a = (float)(x <= h ? x : h - (x - h)) / (float)h;
         }));
      }

   }
}
