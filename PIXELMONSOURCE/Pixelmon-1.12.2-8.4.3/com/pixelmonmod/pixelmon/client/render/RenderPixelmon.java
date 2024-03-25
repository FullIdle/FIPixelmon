package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.models.DualModelDualTextureFactory;
import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.LensInfoPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPixelmon extends RenderLiving {
   private static final ResourceLocation DYNAMAX_CLOUDS_TEX = new ResourceLocation("pixelmon:textures/fluff/clouds.png");
   private static final ResourceLocation MATTE_WHITE_TEX = new ResourceLocation("pixelmon:textures/fluff/mattewhite.png");
   private final IPixelmonModel DYNAMAX_CLOUDS;
   private int defaultNameRenderDistance;
   private int defaultBossNameRenderDistanceExtension;
   private int configNameRenderMultiplier;
   private int nameRenderDistanceNormal;
   private int nameRenderDistanceBoss;
   private float distance;
   private static final float[] WHITE = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   String lvlTag;
   String bossTag;

   public RenderPixelmon(RenderManager manager) {
      super(manager, (ModelBase)null, 0.5F);
      this.DYNAMAX_CLOUDS = (IPixelmonModel)EnumCustomModel.DynamaxClouds.getModel();
      this.defaultNameRenderDistance = 8;
      this.defaultBossNameRenderDistanceExtension = 8;
      this.configNameRenderMultiplier = Math.max(1, Math.min(PixelmonConfig.nameplateRangeModifier, 3));
      this.nameRenderDistanceNormal = this.defaultNameRenderDistance * this.configNameRenderMultiplier;
      this.nameRenderDistanceBoss = this.nameRenderDistanceNormal + this.defaultBossNameRenderDistanceExtension;
      this.distance = 0.0F;
   }

   public void doRender(EntityPixelmon pixelmon, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!pixelmon.stopRender) {
         if (!pixelmon.func_82150_aj()) {
            if (!pixelmon.getPokemonName().equals("") && pixelmon.getPokemonData() != null && pixelmon.getPokemonData().getGrowth() != null) {
               if (this.field_76990_c.field_78734_h != null) {
                  this.distance = pixelmon.func_70032_d(this.field_76990_c.field_78734_h);
               }

               ModelBase model = this.field_77045_g;
               if (model instanceof PixelmonModelSmd) {
                  ((PixelmonModelSmd)model).partialTick = partialTicks;
               }

               GlStateManager.func_179092_a(516, 0.1F);
               if (pixelmon.getModel() != null) {
                  this.renderPixelmon(pixelmon, x, y, z, partialTicks, false);
               }

            }
         }
      }
   }

   public void renderPixelmon(EntityPixelmon pixelmon, double x, double y, double z, float partialTicks, boolean fromPokedex) {
      this.renderPixelmon(pixelmon, x, y, z, partialTicks, fromPokedex, 1);
   }

   public void renderPixelmon(EntityPixelmon pixelmon, double x, double y, double z, float partialTicks, float[] color) {
      this.renderPixelmon(pixelmon, x, y, z, partialTicks, false, 1, color);
   }

   public void renderPixelmon(EntityPixelmon pixelmon, double x, double y, double z, float partialTicks, boolean fromPokedex, int pass) {
      this.renderPixelmon(pixelmon, x, y, z, partialTicks, fromPokedex, 1, WHITE);
   }

   public void renderPixelmon(EntityPixelmon pixelmon, double x, double y, double z, float partialTicks, boolean fromPokedex, int pass, float[] color) {
      if (fromPokedex || !pixelmon.func_82150_aj()) {
         boolean dynamax = pixelmon.getDynamaxScale() > 0.0F;
         GlStateManager.func_179094_E();
         GlStateManager.func_179129_p();
         this.field_77045_g = pixelmon.getModel();
         this.field_77045_g.field_78095_p = this.func_77040_d(pixelmon, partialTicks);
         this.field_77045_g.field_78093_q = pixelmon.func_184218_aH();
         this.field_77045_g.field_78091_s = pixelmon.func_70631_g_();
         if (this.field_77045_g instanceof PixelmonModelSmd) {
            PixelmonModelSmd smd = (PixelmonModelSmd)this.field_77045_g;
            if (smd.theModel != null) {
               if (smd.theModel.hasAnimations()) {
                  ((PixelmonModelSmd)this.field_77045_g).setupForRender((Entity2Client)pixelmon);
               }
            } else {
               Pixelmon.LOGGER.error("MISSING MODEL: " + pixelmon.getSpecies() + " " + pixelmon.getFormEnum());
            }
         }

         try {
            float f2 = this.func_77034_a(pixelmon.field_70760_ar, pixelmon.field_70761_aq, partialTicks);
            float f3 = this.func_77034_a(pixelmon.field_70758_at, pixelmon.field_70759_as, partialTicks);
            float f4 = f3 - f2;
            float f9 = pixelmon.field_70127_C + (pixelmon.field_70125_A - pixelmon.field_70127_C) * partialTicks;
            this.func_77039_a(pixelmon, x, y, z);
            float f5 = this.func_77044_a(pixelmon, partialTicks);
            this.func_77043_a(pixelmon, f5, f2, partialTicks);
            GlStateManager.func_179091_B();
            GlStateManager.func_179145_e();
            GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
            this.renderEvolutionLightBeams(pixelmon);
            this.preRenderCallback(pixelmon, partialTicks);
            GlStateManager.func_179109_b(0.0F, -1.5F, 0.0F);
            float f7 = pixelmon.field_184618_aE + (pixelmon.field_70721_aZ - pixelmon.field_184618_aE) * partialTicks;
            float f8 = pixelmon.field_184619_aG - pixelmon.field_70721_aZ * (1.0F - partialTicks);
            if (pixelmon.func_70631_g_()) {
               f8 *= 3.0F;
            }

            if (f7 > 1.0F) {
               f7 = 1.0F;
            }

            GlStateManager.func_179141_d();
            this.field_77045_g.func_78086_a(pixelmon, f8, f7, partialTicks);
            this.field_77045_g.func_78087_a(f8, f7, f5, f4, f9, 0.0625F, pixelmon);
            if (!(this.field_77045_g instanceof PixelmonModelSmd)) {
               GlStateManager.func_179133_A();
               GlStateManager.func_179103_j(7425);
            }

            boolean flag = this.func_177090_c(pixelmon, partialTicks);
            pixelmon.field_70170_p.field_72984_F.func_76320_a("pixelmon_render");
            if (pixelmon.getBossMode().isBossPokemon()) {
               GlStateManager.func_179131_c(pixelmon.getBossMode().r, pixelmon.getBossMode().g, pixelmon.getBossMode().b, 1.0F);
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
            } else if (pixelmon.evoStage != null) {
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
               GlStateManager.func_179147_l();
               GlStateManager.func_179090_x();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, (float)pixelmon.fadeCount / 20.0F);
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
               GlStateManager.func_179098_w();
            } else if (dynamax) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
               this.func_110776_a(MATTE_WHITE_TEX);
               if (this.field_77045_g instanceof DualModelDualTextureFactory.TransparentImpl) {
                  DualModelDualTextureFactory.TransparentImpl impl = (DualModelDualTextureFactory.TransparentImpl)this.field_77045_g;
                  impl.overloadTexture();
               }

               GlStateManager.func_179141_d();
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(770, 1);
               GlStateManager.func_179140_f();
               if (pixelmon.getSpecies() == EnumSpecies.Calyrex) {
                  GlStateManager.func_179131_c(0.0F, 0.1F, 1.0F, 0.35F);
               } else {
                  GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 0.15F);
               }

               GlStateManager.func_179132_a(false);
               GlStateManager.func_179090_x();
               GlStateManager.func_179094_E();
               GlStateManager.func_179139_a(1.05, 1.0, 1.05);
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
               GlStateManager.func_179121_F();
               GlStateManager.func_179098_w();
               GlStateManager.func_179132_a(true);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179145_e();
               GlStateManager.func_179112_b(770, 771);
            } else {
               GlStateManager.func_179131_c(color[0], color[1], color[2], color[3]);
               this.func_77036_a(pixelmon, f8, f7, f5, f4, f9, 0.0625F);
            }

            pixelmon.field_70170_p.field_72984_F.func_76319_b();
            float distance = pixelmon.func_70032_d(this.field_76990_c.field_78734_h);
            if (PixelmonConfig.emissiveTextures && EmissiveTextures.hasEmissive(pixelmon) && distance < (float)PixelmonConfig.emissiveTexturesDistance) {
               try {
                  pixelmon.field_70170_p.field_72984_F.func_76320_a("pixelmon_emissive");
                  EmissiveTextures.startEmissive();
                  this.func_77036_a(pixelmon, f8, f7, f5, 0.0F, 0.0F, 0.0625F);
               } finally {
                  EmissiveTextures.stopEmissive();
                  pixelmon.field_70170_p.field_72984_F.func_76319_b();
               }
            }

            if (flag) {
               this.func_177091_f();
            }

            GlStateManager.func_179132_a(true);
            GlStateManager.func_179101_C();
         } catch (Exception var26) {
            var26.printStackTrace();
         }

         OpenGlHelper.func_77473_a(OpenGlHelper.field_77476_b);
         if (!fromPokedex) {
            GlStateManager.func_179098_w();
         }

         OpenGlHelper.func_77473_a(OpenGlHelper.field_77478_a);
         GlStateManager.func_179089_o();
         GlStateManager.func_179121_F();
         if (dynamax) {
            RenderDynamax.dynamaxRender(this, pixelmon, x, y, z, DYNAMAX_CLOUDS_TEX, this.DYNAMAX_CLOUDS, partialTicks);
         }

         if (!fromPokedex) {
            this.renderName(pixelmon, x, y, z);
         }

      }
   }

   private void renderEvolutionLightBeams(EntityPixelmon pixelmon) {
      if (pixelmon.evoStage == EvolutionStage.PreAnimation || pixelmon.evoStage == EvolutionStage.PostAnimation) {
         float ticks = (float)pixelmon.evoAnimTicks;
         if (pixelmon.evoStage == EvolutionStage.PostAnimation) {
            ticks += (float)EvolutionStage.PreAnimation.ticks;
         }

         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder worldRenderer = tessellator.func_178180_c();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179090_x();
         GlStateManager.func_179140_f();
         GlStateManager.func_179147_l();
         float width = (float)pixelmon.getBaseStats().getBoundsData().getWidth();
         float height = (float)pixelmon.getBaseStats().getBoundsData().getHeight();
         float scale = ticks / ((float)EvolutionStage.PreAnimation.ticks + (float)EvolutionStage.PostAnimation.ticks);
         if (scale > 1.0F) {
            scale = 1.0F;
         }

         float hScale = scale * pixelmon.heightDiff / pixelmon.getBaseStats().getHeight();
         GlStateManager.func_179094_E();
         if (pixelmon.evoStage == EvolutionStage.PreAnimation) {
            GlStateManager.func_179109_b(0.0F, -1.0F * height * (1.0F + hScale) / 2.0F, 0.0F);
         } else {
            GlStateManager.func_179109_b(0.0F, -1.0F * height * (1.0F - hScale) / 2.0F, 0.0F);
         }

         float length = scale * 18.0F + 2.0F;
         if (ticks > 20.0F && pixelmon.fadeCount > 18) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(15.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         if (ticks > 40.0F && pixelmon.fadeCount > 15) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F + 180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(15.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         if (ticks > 60.0F && pixelmon.fadeCount > 12) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F + 270.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(40.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         if (ticks > 80.0F && pixelmon.fadeCount > 9) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F + 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(40.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         if (ticks > 100.0F && pixelmon.fadeCount > 6) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F + 135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(65.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         if (ticks > 120.0F && pixelmon.fadeCount > 3) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(ticks * 3.0F + 315.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(65.0F, 1.0F, 0.0F, 1.0F);
            this.drawBeam(width / 10.0F, length, worldRenderer, tessellator);
            GlStateManager.func_179121_F();
         }

         GlStateManager.func_179121_F();
         GlStateManager.func_179084_k();
         GlStateManager.func_179145_e();
         GlStateManager.func_179098_w();
      }

   }

   private void drawBeam(float width, float length, BufferBuilder worldRenderer, Tessellator tessellator) {
      float alpha = 0.6F;
      worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      worldRenderer.func_181662_b((double)(-width), (double)(0.0F * length), 0.0).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b((double)width, (double)(0.0F * length), 0.0).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b((double)(width * 3.0F), (double)(-length), 0.0).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b((double)(-width * 3.0F), (double)(-length), 0.0).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      tessellator.func_78381_a();
      worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      worldRenderer.func_181662_b(0.0, (double)(0.0F * length), (double)(-width)).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b(0.0, (double)(0.0F * length), (double)width).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b(0.0, (double)(-length), (double)(width * 3.0F)).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      worldRenderer.func_181662_b(0.0, (double)(-length), (double)(-width * 3.0F)).func_181666_a(1.0F, 1.0F, 1.0F, alpha).func_181675_d();
      tessellator.func_78381_a();
   }

   public void drawNameTag(EntityPixelmon entityPixelmon, double x, double y, double z, boolean owned) {
      if (Minecraft.func_71382_s() && !entityPixelmon.func_70093_af() && entityPixelmon.func_184179_bs() != Minecraft.func_71410_x().field_71439_g) {
         this.renderLabel(entityPixelmon, x, y, z, owned);
      }

   }

   protected void renderLabel(EntityPixelmon pixelmon, double x, double y, double z, boolean owned) {
      FontRenderer fontRenderer = this.func_76983_a();
      float var13 = 1.6F;
      float baseScale = 0.016666668F * var13;
      GlStateManager.func_179094_E();
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      float scale = pixelmon.getPixelmonScale() * scaleFactor * pixelmon.getScaleFactor();
      EntityBoundsData boundsData = pixelmon.getBaseStats().getBoundsData();
      double height = y + 0.699999988079071 + boundsData.getHeight() * (double)scale;
      GlStateManager.func_179137_b(x, height, z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179152_a(-baseScale, -baseScale, baseScale);
      GlStateManager.func_179140_f();
      GlStateManager.func_179126_j();
      GlStateManager.func_179118_c();
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      String line3 = null;
      int color1 = 553648127;
      int color2 = 553648127;
      int pokemonLevel = pixelmon.getPokemonData().getLevel();
      String line1;
      String line2;
      int colour;
      if (owned) {
         if (pixelmon.getPokemonData().isShiny()) {
            color2 = -15000;
         }

         line2 = I18n.func_135052_a("ribbon." + pixelmon.getPokemonData().getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{pixelmon.getNickname()});
         line1 = Minecraft.func_71410_x().field_71439_g.func_70005_c_();
         line3 = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemonLevel;
      } else if (pixelmon.func_184753_b() != null) {
         if (pixelmon.getPokemonData().isShiny()) {
            color2 = -15000;
         }

         line2 = I18n.func_135052_a("ribbon." + pixelmon.getPokemonData().getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{pixelmon.getNickname()});
         EntityPlayer player = pixelmon.func_130014_f_().func_152378_a(pixelmon.func_184753_b());
         line1 = player == null ? "Unknown" : player.func_70005_c_();
         line3 = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemonLevel;
      } else {
         EnumBossMode enumBossMode = pixelmon.getBossMode();
         if (enumBossMode == EnumBossMode.NotBoss) {
            if (PixelmonServerConfig.renderWildLevels) {
               if (this.lvlTag == null) {
                  this.lvlTag = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]);
               }

               line1 = this.lvlTag;
               line2 = "" + pokemonLevel;
            } else {
               line1 = "";
               line2 = "";
            }

            if (pixelmon.getPokemonData().isShiny()) {
               colour = -15000;
               color2 = colour;
               color1 = colour;
            }
         } else {
            if (this.bossTag == null) {
               this.bossTag = I18n.func_135052_a("gui.boss.text", new Object[0]);
            }

            line1 = this.bossTag;
            line2 = enumBossMode.getBossText();
            colour = enumBossMode.getColourInt();
            if (PixelmonConfig.showWildNames) {
               color1 = colour;
            } else {
               color2 = colour;
            }
         }

         if (PixelmonConfig.showWildNames) {
            line1 = line1 + " " + line2;
            line2 = I18n.func_135052_a("ribbon." + pixelmon.getPokemonData().getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{pixelmon.getNickname()});
         }
      }

      int line2Width = fontRenderer.func_78256_a(line2);
      colour = line2Width / 2 * -1;
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(-2.5 + (double)colour, -4.5, 0.0);
      GlStateManager.func_179139_a(0.5, 0.5, 0.5);
      fontRenderer.func_78276_b(line1, 0, 0, color1);
      GlStateManager.func_179121_F();
      fontRenderer.func_78276_b(line2, -line2Width / 2, 0, color2);
      if (line3 != null) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(2.5 + (double)colour, 9.0, 0.0);
         GlStateManager.func_179139_a(0.5, 0.5, 0.5);
         fontRenderer.func_78276_b(line3, 0, 0, color1);
         GlStateManager.func_179121_F();
      }

      LensInfoPacket info = pixelmon.getClientOnlyInfo();
      if (info != null) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(30.0, -4.5, 0.0);
         GlStateManager.func_179139_a(0.5, 0.5, 0.5);
         fontRenderer.func_78276_b(I18n.func_135052_a("gui.screenpokechecker.ability", new Object[0]) + ": " + I18n.func_135052_a(info.abilityLangKey, new Object[0]), 0, 0, 16777215);
         fontRenderer.func_78276_b(I18n.func_135052_a("gui.screenpokechecker.nature", new Object[0]) + ": " + info.nature.getLocalizedName(), 0, 10, 16777215);
         fontRenderer.func_78276_b(I18n.func_135052_a("gui.trainereditor.ivs", new Object[0]) + ": (" + info.ivs.getPercentageString(0) + "%) " + info.ivs.getShorthand("/"), 0, 20, 16777215);
         if (pixelmon.getPokemonData().getGender() != Gender.None) {
            fontRenderer.func_78276_b(I18n.func_135052_a("gui.trainereditor.gender", new Object[0]) + ": " + pixelmon.getPokemonData().getGender().getLocalizedName(), 0, 30, 16777215);
         }

         GlStateManager.func_179121_F();
      }

      GlStateManager.func_179126_j();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179090_x();
      int center = 50;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      if (PixelmonConfig.drawHealthBars) {
         this.drawHealthBars(pixelmon, center, owned, vertexBuffer);
      }

      tessellator.func_78381_a();
      GlStateManager.func_179084_k();
      GlStateManager.func_179098_w();
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179141_d();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   private void drawHealthBars(EntityPixelmon pixelmon, int center, boolean owned, BufferBuilder vertexBuffer) {
      float hp;
      float maxHp;
      float healthWidth;
      if (owned) {
         hp = (float)pixelmon.getLvl().getExp();
         maxHp = (float)pixelmon.getLvl().getExpForNextLevelClient();
         if (hp >= maxHp) {
            hp = maxHp;
         }

         healthWidth = 50.0F * (hp / maxHp);
         vertexBuffer.func_181662_b((double)(-25.0F + healthWidth), 16.0, 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b((double)(-25.0F + healthWidth), 17.0, 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b(25.0, 17.0, 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b(25.0, 16.0, 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b(-25.0, 16.0, 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b(-25.0, 17.0, 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b((double)(healthWidth - 25.0F), 17.0, 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
         vertexBuffer.func_181662_b((double)(healthWidth - 25.0F), 16.0, 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
      }

      hp = pixelmon.func_110143_aJ();
      maxHp = pixelmon.func_110138_aP();
      if (hp > maxHp) {
         hp = maxHp;
      }

      healthWidth = 50.0F * (hp / maxHp);
      int healthBarY = owned ? 14 : 10;
      vertexBuffer.func_181662_b((double)(-25.0F + healthWidth), (double)healthBarY, 0.0).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_181675_d();
      vertexBuffer.func_181662_b((double)(-25.0F + healthWidth), (double)(healthBarY + 1), 0.0).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_181675_d();
      vertexBuffer.func_181662_b(25.0, (double)(healthBarY + 1), 0.0).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_181675_d();
      vertexBuffer.func_181662_b(25.0, (double)healthBarY, 0.0).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_181675_d();
      float r = 0.0F;
      float g = 0.0F;
      float b = 0.0F;
      float a = 1.0F;
      if (hp <= maxHp / 5.0F) {
         r = 0.8F;
         g = 0.0F;
         b = 0.0F;
      } else if (hp <= maxHp / 2.0F) {
         r = 1.0F;
         g = 1.0F;
         b = 0.2F;
      } else {
         r = 0.2F;
         g = 1.0F;
         b = 0.2F;
      }

      vertexBuffer.func_181662_b(-25.0, (double)healthBarY, 0.0).func_181666_a(r, g, b, a).func_181675_d();
      vertexBuffer.func_181662_b(-25.0, (double)(healthBarY + 1), 0.0).func_181666_a(r, g, b, a).func_181675_d();
      vertexBuffer.func_181662_b((double)(healthWidth - 25.0F), (double)(healthBarY + 1), 0.0).func_181666_a(r, g, b, a).func_181675_d();
      vertexBuffer.func_181662_b((double)(healthWidth - 25.0F), (double)healthBarY, 0.0).func_181666_a(r, g, b, a).func_181675_d();
   }

   protected void preRenderScale(EntityPixelmon entity, float f) {
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      scaleFactor *= entity.getScaleFactor();
      if (entity.getModel() instanceof PixelmonModelBase) {
         scaleFactor *= ((PixelmonModelBase)entity.getModel()).getScale();
      }

      float dynamaxScale = entity.getDynamaxScale();
      double wScale;
      if (dynamaxScale > 0.0F) {
         EntityBoundsData bounds = entity.getPokemonData().getBaseStats().getBoundsData();
         if (entity.transformed != null) {
            bounds = entity.transformed.getSpecies().getBaseStats().getBoundsData();
         }

         wScale = Math.max(bounds.getHeight(), bounds.getWidth());
         float targetHeight = PixelmonConfig.dynamaxSize / 2.0F;
         double currentHeight = wScale * (double)entity.getPixelmonScale();
         if (currentHeight < (double)targetHeight) {
            double scaling = (double)targetHeight / currentHeight;
            scaleFactor = (float)((double)scaleFactor * Math.max((double)dynamaxScale * scaling, (double)entity.getPixelmonScale()));
         } else {
            scaleFactor *= entity.getPixelmonScale();
         }
      } else {
         scaleFactor *= entity.getPixelmonScale();
      }

      GlStateManager.func_179152_a(scaleFactor, scaleFactor, scaleFactor);
      double lScale;
      float scale;
      double hScale;
      if (entity.evoStage == EvolutionStage.PreAnimation) {
         scale = (float)entity.evoAnimTicks / ((float)EvolutionStage.PreAnimation.ticks + (float)EvolutionStage.PostAnimation.ticks);
         if (scale > 1.0F) {
            scale = 1.0F;
         }

         wScale = (double)(scale * entity.widthDiff) / entity.getBaseStats().getBoundsData().getWidth();
         hScale = (double)(scale * entity.heightDiff) / entity.getBaseStats().getBoundsData().getHeight();
         lScale = (double)(scale * entity.lengthDiff) / entity.getBaseStats().getBoundsData().getWidth();
         GlStateManager.func_179139_a(1.0 + wScale, 1.0 + hScale, 1.0 + lScale);
      }

      if (entity.evoStage == EvolutionStage.PostAnimation) {
         scale = ((float)entity.evoAnimTicks + (float)EvolutionStage.PreAnimation.ticks) / ((float)EvolutionStage.PreAnimation.ticks + (float)EvolutionStage.PostAnimation.ticks);
         if (scale > 1.0F) {
            scale = 1.0F;
         }

         scale = 1.0F - scale;
         wScale = (double)(scale * entity.widthDiff) / entity.getBaseStats().getBoundsData().getWidth();
         hScale = (double)(scale * entity.heightDiff) / entity.getBaseStats().getBoundsData().getHeight();
         lScale = (double)(scale * entity.lengthDiff) / entity.getBaseStats().getBoundsData().getWidth();
         GlStateManager.func_179139_a(1.0 - wScale, 1.0 - hScale, 1.0 - lScale);
      }

   }

   protected void preRenderCallback(EntityPixelmon entityPixelmon, float f) {
      this.preRenderScale(entityPixelmon, f);
   }

   protected ResourceLocation getEntityTexture(EntityPixelmon pixelmon) {
      if (pixelmon.getPokemonData().getFormEnum() == EnumSpecial.Online && pixelmon.func_184753_b() != null) {
         PlayerExtraDataStore.get(pixelmon.func_184753_b()).checkPokemon(pixelmon.getPokemonData());
      }

      return pixelmon.getTexture();
   }

   protected boolean bindEntityTexture(EntityPixelmon entity) {
      ResourceLocation resourcelocation = this.getEntityTexture(entity);
      if (EmissiveTextures.isRendering() && resourcelocation != null) {
         resourcelocation = EmissiveTextures.getEmissive(resourcelocation);
      }

      if (resourcelocation == null) {
         return false;
      } else {
         this.func_110776_a(resourcelocation);
         return true;
      }
   }

   protected float func_77034_a(float par1, float par2, float par3) {
      float f3;
      for(f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F) {
      }

      while(f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return par1 + par3 * f3;
   }

   public void renderName(EntityPixelmon entity, double x, double y, double z) {
      boolean owned = ClientStorageManager.party.findOne((pokemon) -> {
         return pokemon.getUUID().equals(entity.getPokemonData().getUUID());
      }) != null;
      float renderDistance = entity.getBossMode() != EnumBossMode.NotBoss ? (float)this.nameRenderDistanceBoss : (float)this.nameRenderDistanceNormal;
      if (this.distance <= renderDistance || owned) {
         this.drawNameTag(entity, x, y, z, owned);
      }

   }
}
