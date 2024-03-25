package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.blocks.BlockRotation;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTimespaceAltar;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.client.particle.particles.SmallRising;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGiratina;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RenderTileEntityTimespaceShrine extends TileEntityRenderer {
   private static final GenericModelHolder main_shrine = new GenericModelHolder("blocks/shrines/timespace/timespacealtar.pqc");
   private static final GenericModelHolder arceus_ring = new GenericModelHolder("blocks/shrines/timespace/arceus_ring.pqc");
   private static final GenericModelHolder palkia_symbol = new GenericModelHolder("blocks/shrines/timespace/palkia_symbol.pqc");
   private static final GenericModelHolder dialga_symbol = new GenericModelHolder("blocks/shrines/timespace/dialga_symbol.pqc");
   private static final GenericModelHolder giratina_symbol = new GenericModelHolder("blocks/shrines/timespace/giratina_symbol.pqc");
   private static final ResourceLocation main_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/timespace/timespacealtar.png");
   private static final GenericModelHolder lustrous_orb = new GenericModelHolder("blocks/shrines/timespace/lustrous_orb.pqc");
   private static final ResourceLocation lustrous_orb_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/timespace/lustrous_orb.png");
   private static final GenericModelHolder adamant_orb = new GenericModelHolder("blocks/shrines/timespace/adamant_orb.pqc");
   private static final ResourceLocation adamant_orb_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/timespace/adamant_orb.png");
   private static final GenericModelHolder griseous_orb = new GenericModelHolder("blocks/shrines/timespace/griseous_orb.pqc");
   private static final ResourceLocation griseous_orb_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/timespace/griseous_orb.png");
   private static final ResourceLocation[] dialga_textures = new ResourceLocation[]{new ResourceLocation("pixelmon", "textures/pokemon/dialga-normal.png"), new ResourceLocation("pixelmon", "textures/pokemon/pokemon-shiny/shinydialga-normal.png")};
   private static final ResourceLocation[] palkia_textures = new ResourceLocation[]{new ResourceLocation("pixelmon", "textures/pokemon/palkia-normal.png"), new ResourceLocation("pixelmon", "textures/pokemon/pokemon-shiny/shinypalkia-normal.png")};
   private static final ResourceLocation[] giratina_textures = new ResourceLocation[]{new ResourceLocation("pixelmon", "textures/pokemon/giratina-origin.png"), new ResourceLocation("pixelmon", "textures/pokemon/pokemon-shiny/shinygiratina-origin.png")};
   private static final ResourceLocation[] arceus_textures = new ResourceLocation[]{new ResourceLocation("pixelmon", "textures/pokemon/arceus-normal.png"), new ResourceLocation("pixelmon", "textures/pokemon/pokemon-shiny/shinyarceus-normal.png")};
   private double theta = 0.0;
   private double theta2 = 0.0;
   private static final double threshold = 2.3561944901923447E9;

   public RenderTileEntityTimespaceShrine() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityTimespaceAltar shrineBlock, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      GlStateManager.func_179094_E();
      switch (shrineBlock.orbIn) {
         case PALKIA:
            this.func_147499_a(lustrous_orb_texture);
            GlStateManager.func_179137_b(0.0, -1.535, 0.0);
            GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179139_a(0.3, 0.3, 0.3);
            lustrous_orb.render();
            break;
         case DIALGA:
            this.func_147499_a(adamant_orb_texture);
            GlStateManager.func_179137_b(0.0, -1.535, 0.0);
            GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179139_a(0.3, 0.3, 0.3);
            adamant_orb.render();
            break;
         case GIRATINA:
            this.func_147499_a(griseous_orb_texture);
            GlStateManager.func_179137_b(0.0, -1.16, 1.165);
            GlStateManager.func_179139_a(0.3, 0.3, 0.3);
            griseous_orb.render();
      }

      GlStateManager.func_179121_F();
      this.func_147499_a(main_texture);
      float rotate = -90.0F;
      float scale = 0.3F;
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rotate, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      main_shrine.render();
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rotate, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      if (shrineBlock.chainIn) {
         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
      }

      arceus_ring.render();
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rotate, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      palkia_symbol.render();
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rotate, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      dialga_symbol.render();
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rotate, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      giratina_symbol.render();
      GlStateManager.func_179145_e();
      GlStateManager.func_179121_F();
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179094_E();
      BlockRotation rot = BlockRotation.getRotationFromMetadata(shrineBlock.func_145832_p());
      double xOffset;
      double zOffset;
      if (rot == BlockRotation.Normal) {
         xOffset = 0.5;
         zOffset = 5.5;
      } else if (rot == BlockRotation.Rotate180) {
         xOffset = 0.5;
         zOffset = -4.5;
      } else if (rot == BlockRotation.CW) {
         xOffset = -4.5;
         zOffset = 0.5;
      } else {
         xOffset = 5.5;
         zOffset = 0.5;
      }

      BlockPos center = new BlockPos((double)shrineBlock.func_174877_v().func_177958_n() + xOffset, (double)shrineBlock.func_174877_v().func_177956_o(), (double)shrineBlock.func_174877_v().func_177952_p() + zOffset);
      int i;
      double pX;
      double pZ;
      if (shrineBlock.timeSpent > 0) {
         GlStateManager.func_179147_l();
         GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
         if (shrineBlock.flutePlayed) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         } else {
            GlStateManager.func_179131_c(1.0F, (float)shrineBlock.timeSpent / 200.0F + 0.02F, (float)shrineBlock.timeSpent / 200.0F + 0.02F, 1.0F);
         }

         GlStateManager.func_179109_b(0.0F, (1.0F - (float)shrineBlock.timeSpent / 200.0F) * 5.0F, 0.0F);
         GlStateManager.func_179109_b(0.0F, (float)(-shrineBlock.timeSpent) / 200.0F * 5.0F, 0.0F);
         if (shrineBlock.flutePlayed) {
            this.func_147499_a(arceus_textures[shrineBlock.summoningShiny ? 1 : 0]);
            GlStateManager.func_179137_b(0.0, -3.5, 5.0);
            GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179152_a(0.35F, 0.35F, 0.35F);
            PixelmonModelRegistry.getModel(EnumSpecies.Arceus, EnumNoForm.NoForm).func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
         } else {
            switch (shrineBlock.orbIn) {
               case PALKIA:
                  this.func_147499_a(palkia_textures[shrineBlock.summoningShiny ? 1 : 0]);
                  GlStateManager.func_179137_b(0.0, -3.5, 5.0);
                  GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179152_a(0.35F, 0.35F, 0.35F);
                  PixelmonModelRegistry.getModel(EnumSpecies.Palkia, EnumNoForm.NoForm).func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
                  break;
               case DIALGA:
                  this.func_147499_a(dialga_textures[shrineBlock.summoningShiny ? 1 : 0]);
                  GlStateManager.func_179137_b(0.0, -7.0, 5.0);
                  GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
                  PixelmonModelRegistry.getModel(EnumSpecies.Dialga, EnumNoForm.NoForm).func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
                  break;
               case GIRATINA:
                  this.func_147499_a(giratina_textures[shrineBlock.summoningShiny ? 1 : 0]);
                  GlStateManager.func_179109_b(0.0F, -35.0F, 5.0F);
                  GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
                  PixelmonModelRegistry.getModel(EnumSpecies.Giratina, EnumGiratina.ORIGIN).func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
            }
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179084_k();
         int totalPoints = (int)((float)shrineBlock.timeSpent / 20.0F);

         for(i = 0; i < totalPoints; ++i) {
            pX = (double)shrineBlock.func_174877_v().func_177958_n() + xOffset + (double)RandomHelper.getRandomNumberBetween(-4.0F, 4.0F);
            pZ = (double)shrineBlock.func_174877_v().func_177952_p() + zOffset + (double)RandomHelper.getRandomNumberBetween(-4.0F, 4.0F);
            if (center.func_177954_c(pX, (double)shrineBlock.func_174877_v().func_177956_o(), pZ) < 16.0) {
               Minecraft.func_71410_x().field_71452_i.func_78873_a(new ParticleArcanery(this.func_178459_a(), pX, (double)shrineBlock.func_174877_v().func_177956_o(), pZ, 0.0, 1.0, 0.0, new SmallRising(1.0F, shrineBlock.flutePlayed ? 1.0F : 0.0F, shrineBlock.flutePlayed ? 1.0F : 0.0F, 0.5F)));
            }
         }

         ParticleSystems.get(1).execute(Minecraft.func_71410_x(), this.func_178459_a(), (double)shrineBlock.func_174877_v().func_177958_n() + xOffset, (double)shrineBlock.func_174877_v().func_177956_o() + 0.5, (double)shrineBlock.func_174877_v().func_177952_p() + zOffset, 0.0F, false, shrineBlock.flutePlayed ? 1.0 : 0.0);
      } else if (shrineBlock.chainIn) {
         int totalPoints = 16;

         for(i = 1; i <= totalPoints; ++i) {
            pX = 6.283185307179586 / (double)totalPoints;
            pZ = pX * (double)i + this.theta;
            this.theta += 7.5E-4;
            if (this.theta > Math.PI) {
               this.theta -= Math.PI;
            }

            double radius = 4.0;
            double dx = radius * Math.sin(pZ);
            double dz = radius * Math.cos(pZ);
            Minecraft.func_71410_x().field_71452_i.func_78873_a(new ParticleArcanery(this.func_178459_a(), (double)shrineBlock.func_174877_v().func_177958_n() + xOffset + dx, (double)shrineBlock.func_174877_v().func_177956_o() + 0.5, (double)shrineBlock.func_174877_v().func_177952_p() + zOffset + dz, 0.0, 0.0, 0.0, new SmallRising(1.0F, 0.0F, 0.0F, 0.5F)));
         }
      }

      GlStateManager.func_179121_F();
   }
}
