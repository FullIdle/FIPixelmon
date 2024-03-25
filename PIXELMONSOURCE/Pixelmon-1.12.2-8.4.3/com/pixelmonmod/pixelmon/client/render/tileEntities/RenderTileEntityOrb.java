package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockOrb;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityOrb;
import com.pixelmonmod.pixelmon.client.models.obj.ObjLoader;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityOrb extends TileEntitySpecialRenderer {
   public static final ResourceLocation RED_ORB = new ResourceLocation("pixelmon", "textures/blocks/orbs/red_orb.png");
   public static final ResourceLocation BLUE_ORB = new ResourceLocation("pixelmon", "textures/blocks/orbs/blue_orb.png");
   public static final ResourceLocation JADE_ORB = new ResourceLocation("pixelmon", "textures/blocks/orbs/jade_orb.png");

   public boolean hasProperty(IBlockState state, IProperty property) {
      return state.func_177227_a().contains(property);
   }

   public void render(TileEntityOrb tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      int pieces = tile.getPieces();
      if (tile.func_145838_q() instanceof BlockOrb) {
         if (this.hasProperty(tile.func_145838_q().func_176194_O().func_177621_b(), BlockProperties.FACING)) {
            EnumOrbShard shard = ((BlockOrb)tile.func_145838_q()).shardType;
            double scale = 1.0;
            switch (shard) {
               case RED:
                  this.func_147499_a(RED_ORB);
                  scale = 0.12999999523162842;
                  break;
               case BLUE:
                  this.func_147499_a(BLUE_ORB);
                  scale = 0.05999999865889549;
                  break;
               case JADE:
                  this.func_147499_a(JADE_ORB);
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.5F, (float)y, (float)z + 0.5F);
            EnumFacing facing = EnumFacing.func_176731_b(tile.func_145832_p());
            float rotAngle = facing.func_185119_l() + 90.0F;
            GlStateManager.func_179114_b(rotAngle, 0.0F, 360.0F, 0.0F);
            GlStateManager.func_179139_a(scale, scale, scale);
            GlStateManager.func_179140_f();
            GlStateManager.func_179133_A();
            GlStateManager.func_179103_j(7425);

            try {
               RenderTileEntityOrb.EnumOrbModels.getModel(shard, pieces).renderAll(0.0F);
            } finally {
               GlStateManager.func_179145_e();
               GlStateManager.func_179114_b(-((EnumFacing)tile.func_145838_q().func_176194_O().func_177621_b().func_177229_b(BlockProperties.FACING)).func_185119_l(), 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179121_F();
            }

            if (tile.getPieces() > 9) {
               ++tile.clientTicker;
               Tessellator tessellator = Tessellator.func_178181_a();
               BufferBuilder bufferbuilder = tessellator.func_178180_c();
               RenderHelper.func_74518_a();
               float f = ((float)tile.clientTicker + partialTicks) / 300.0F;
               if (tile.clientTicker == Integer.MAX_VALUE) {
                  tile.clientTicker = 0;
               }

               Random random = new Random(432L);
               GlStateManager.func_179090_x();
               GlStateManager.func_179108_z();
               GlStateManager.func_179103_j(7425);
               GlStateManager.func_179147_l();
               GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
               GlStateManager.func_179118_c();
               GlStateManager.func_179089_o();
               GlStateManager.func_179132_a(false);
               GlStateManager.func_179094_E();
               GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
               scale = 3.499999875202775E-4;
               int red = shard == EnumOrbShard.RED ? 255 : 0;
               int blue = shard == EnumOrbShard.BLUE ? 255 : 0;
               int green = shard == EnumOrbShard.JADE ? 255 : 0;
               GlStateManager.func_179139_a(scale, scale, scale);

               for(int i = 0; i < 3; ++i) {
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179114_b(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
                  float f2 = random.nextFloat() * 20.0F + 1.0F + 2000.0F;
                  float f3 = random.nextFloat() * 2.0F + 1.0F + 400.0F;
                  bufferbuilder.func_181668_a(6, DefaultVertexFormats.field_181706_f);
                  bufferbuilder.func_181662_b(3.0, 0.0, 0.0).func_181669_b(red, green, blue, 191).func_181675_d();
                  bufferbuilder.func_181662_b(-3.0 * (double)f3, (double)f2, (double)(-3.0F * f3)).func_181669_b(red, green, blue, 0).func_181675_d();
                  bufferbuilder.func_181662_b(3.0 * (double)f3, (double)f2, (double)(-3.0F * f3)).func_181669_b(red, green, blue, 0).func_181675_d();
                  bufferbuilder.func_181662_b(0.0, (double)f2, (double)(1.0F * f3)).func_181669_b(red, green, blue, 1).func_181675_d();
                  bufferbuilder.func_181662_b(-3.0 * (double)f3, (double)f2, (double)(-3.0F * f3)).func_181669_b(red, green, blue, 0).func_181675_d();
                  tessellator.func_78381_a();
               }

               GlStateManager.func_179121_F();
               GlStateManager.func_179132_a(true);
               GlStateManager.func_179129_p();
               GlStateManager.func_179084_k();
               GlStateManager.func_179103_j(7424);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179098_w();
               GlStateManager.func_179141_d();
               RenderHelper.func_74519_b();
            }

         }
      }
   }

   public static enum EnumOrbModels {
      RED_ONE("pixelmon:models/blocks/orbs/red/1.obj"),
      RED_TWO("pixelmon:models/blocks/orbs/red/2.obj"),
      RED_THREE("pixelmon:models/blocks/orbs/red/3.obj"),
      RED_FOUR("pixelmon:models/blocks/orbs/red/4.obj"),
      RED_FIVE("pixelmon:models/blocks/orbs/red/5.obj"),
      RED_SIX("pixelmon:models/blocks/orbs/red/6.obj"),
      RED_SEVEN("pixelmon:models/blocks/orbs/red/7.obj"),
      RED_EIGHT("pixelmon:models/blocks/orbs/red/8.obj"),
      RED_NINE("pixelmon:models/blocks/orbs/red/9.obj"),
      RED_TEN("pixelmon:models/blocks/orbs/red/10.obj"),
      BLUE_ONE("pixelmon:models/blocks/orbs/blue/1.obj"),
      BLUE_TWO("pixelmon:models/blocks/orbs/blue/2.obj"),
      BLUE_THREE("pixelmon:models/blocks/orbs/blue/3.obj"),
      BLUE_FOUR("pixelmon:models/blocks/orbs/blue/4.obj"),
      BLUE_FIVE("pixelmon:models/blocks/orbs/blue/5.obj"),
      BLUE_SIX("pixelmon:models/blocks/orbs/blue/6.obj"),
      BLUE_SEVEN("pixelmon:models/blocks/orbs/blue/7.obj"),
      BLUE_EIGHT("pixelmon:models/blocks/orbs/blue/8.obj"),
      BLUE_NINE("pixelmon:models/blocks/orbs/blue/9.obj"),
      BLUE_TEN("pixelmon:models/blocks/orbs/blue/10.obj"),
      JADE_ONE("pixelmon:models/blocks/orbs/jade/1.obj"),
      JADE_TWO("pixelmon:models/blocks/orbs/jade/2.obj"),
      JADE_THREE("pixelmon:models/blocks/orbs/jade/3.obj"),
      JADE_FOUR("pixelmon:models/blocks/orbs/jade/4.obj"),
      JADE_FIVE("pixelmon:models/blocks/orbs/jade/5.obj"),
      JADE_SIX("pixelmon:models/blocks/orbs/jade/6.obj"),
      JADE_SEVEN("pixelmon:models/blocks/orbs/jade/7.obj"),
      JADE_EIGHT("pixelmon:models/blocks/orbs/jade/8.obj"),
      JADE_NINE("pixelmon:models/blocks/orbs/jade/9.obj"),
      JADE_TEN("pixelmon:models/blocks/orbs/jade/10.obj");

      public WavefrontObject obj;
      public final ResourceLocation path;

      private EnumOrbModels(String path) {
         this.path = new ResourceLocation(path);
      }

      private WavefrontObject getModel() {
         if (this.obj == null) {
            try {
               if (ObjLoader.accepts(this.path)) {
                  this.obj = ObjLoader.loadModel(this.path);
               }
            } catch (Exception var2) {
               System.out.println("Could not load the model: " + this.path.func_110623_a());
               var2.printStackTrace();
            }
         }

         return this.obj;
      }

      public static WavefrontObject getModel(EnumOrbShard shard, int pieces) {
         return values()[shard.ordinal() * 10 + pieces - 1].getModel();
      }
   }
}
