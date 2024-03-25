package com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiElement;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiParticleEngine;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import java.text.DecimalFormat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class OpponentElement extends GuiElement {
   private static final ResourceLocation OPPONENT = new ResourceLocation("pixelmon", "textures/gui/battle/opponent.png");
   private static final ResourceLocation HEALTHY = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_healthy.png");
   private static final ResourceLocation CAUTION = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_caution.png");
   private static final ResourceLocation WARNING = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_warning.png");
   private static final ResourceLocation SHINY = new ResourceLocation("pixelmon", "textures/gui/battle/shiny.png");
   private static final ResourceLocation CAUGHT = new ResourceLocation("pixelmon", "textures/gui/battle/caught.png");
   private static final ResourceLocation TARGET = new ResourceLocation("pixelmon", "textures/gui/battle/target_up.png");
   private final GuiScreen parent;
   private final PixelmonInGui enemy;
   private final GuiParticleEngine particleEngine;

   public OpponentElement(PixelmonInGui enemy, GuiScreen parent, GuiParticleEngine particleEngine) {
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
      GuiHelper.drawImage(TARGET, (double)x + 6.5, (double)(y + 45), 26.66666603088379, 11.0, this.zLevel);
      GlStateManager.func_179121_F();
      this.setPosition((int)((float)x * scale), (int)((float)y * scale), (int)((float)width * scale), (int)((float)height * scale));
   }

   public void drawElement(float scale) {
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImage(OPPONENT, (double)this.x, (double)(this.y - 3), 160.0, 50.0, this.zLevel);
      float healthPercent = this.enemy.health / (float)this.enemy.maxHealth;
      GuiHelper.drawBar((double)(this.x + 44), (double)(this.y + 20), 109.0, 10.0, healthPercent, this.enemy.getHealthColor());
      this.parent.field_146297_k.func_110434_K().func_110577_a(healthPercent <= 0.5F ? (healthPercent <= 0.25F ? WARNING : CAUTION) : HEALTHY);
      GuiHelper.drawImage((double)(this.x - 10), (double)(this.y - 18), 60.0, 60.0, this.zLevel);
      GuiHelper.bindPokemonSprite(this.enemy, this.parent.field_146297_k);
      GuiHelper.drawImage((double)(this.x + 1), (double)(this.y - 3), 40.0, 40.0, this.zLevel);
      this.particleEngine.drawAtOffset(this.enemy.pokemonUUID.toString(), (double)(this.x + 5), (double)(this.y + 1), (double)RandomHelper.rand.nextInt(26), (double)RandomHelper.rand.nextInt(26));
      float offset = 0.0F;
      if (ClientStorageManager.pokedex.hasCaught(this.enemy.species)) {
         GuiHelper.drawImage(CAUGHT, (double)(this.x + 52), (double)(this.y + 5), 8.0, 8.0, this.zLevel);
         offset += 9.0F;
      }

      if (this.enemy.getGender() != Gender.None) {
         this.parent.field_146297_k.func_110434_K().func_110577_a(this.enemy.getGender() == Gender.Male ? GuiResources.male : GuiResources.female);
         GuiHelper.drawImage((double)((float)(this.x + 52) + offset), (double)(this.y + 5), 5.0, 8.0, this.zLevel);
         offset += 7.0F;
      }

      GuiHelper.drawScaledString(this.enemy.getDisplayName(), (float)(this.x + 52) + offset, (float)this.y + 5.75F, this.enemy.shiny ? -7545 : -986896, 16.0F);
      GuiHelper.drawScaledStringRightAligned("Lv." + this.enemy.level, (float)(this.x + 149), (float)this.y + 7.0F, -986896, false, 12.0F);
      if (PixelmonConfig.advancedBattleInformation) {
         DecimalFormat df = new DecimalFormat(".#");
         String percentage = df.format((double)healthPercent * 100.0).replace(".0", "");
         if (percentage.isEmpty()) {
            percentage = "0";
         }

         GuiHelper.drawScaledStringRightAligned(percentage + "%", (float)(this.x + 145), (float)this.y + 22.0F, -986896, false, 14.0F);
      }

      if (this.enemy.status != -1 && StatusType.getEffect(this.enemy.status) != null) {
         float[] texturePair2 = StatusType.getTexturePos(StatusType.getEffect(this.enemy.status));
         this.parent.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
         GuiHelper.drawImageQuad((double)((float)(this.x + 54 + GuiHelper.getStringWidth(this.enemy.getDisplayName())) + offset), (double)(this.y + 4), 10.5, 10.5F, (double)(texturePair2[0] / 768.0F), (double)(texturePair2[1] / 512.0F), (double)((texturePair2[0] + 240.0F) / 768.0F), (double)((texturePair2[1] + 240.0F) / 512.0F), this.zLevel);
      }

      if (ClientProxy.battleManager.catchCombo != 0) {
         GuiHelper.drawScaledString(I18n.func_135052_a("gui.battle.catch_combo", new Object[]{ClientProxy.battleManager.catchCombo}), (float)(this.x + 22) + offset, (float)this.y + 38.75F, -986896, 12.0F);
      }

      if (this.enemy.shiny && RandomHelper.rand.nextInt(80) == 0) {
         int size = 7 + RandomHelper.rand.nextInt(7);
         this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle(this.enemy.pokemonUUID.toString(), SHINY, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0F, 0.8F, 0.3F, 0.0F, (float)size, (float)size, 120, (particle) -> {
            int x = particle.age;
            int m = particle.maxAge;
            int h = m / 2;
            particle.a = (float)(x <= h ? x : h - (x - h)) / (float)h;
         }));
      }

   }
}
