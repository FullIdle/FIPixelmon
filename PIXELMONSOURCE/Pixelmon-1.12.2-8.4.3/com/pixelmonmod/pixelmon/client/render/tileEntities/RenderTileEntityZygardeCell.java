package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockZygardeCell;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeCell;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.spawning.ZygardeCellsSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;

public class RenderTileEntityZygardeCell extends TileEntityRenderer {
   private static final GenericModelHolder MODEL = new GenericModelHolder("blocks/cell/cell.pqc");
   private static final GenericModelHolder CORE = new GenericModelHolder("blocks/cell/core.pqc");
   private static final ResourceLocation ZYGARDE_CELL = new ResourceLocation("pixelmon", "textures/blocks/cell/zygarde_cell.png");
   private static final ResourceLocation ZYGARDE_CORE = new ResourceLocation("pixelmon", "textures/blocks/cell/zygarde_core.png");

   public void renderTileEntity(TileEntityZygardeCell te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player.func_175149_v() || player.func_184812_l_() || ZygardeCellsSpawner.clientHasCube()) {
         if (state.func_177230_c() instanceof BlockZygardeCell) {
            if (te.func_145838_q() == PixelmonBlocks.zygardeCell) {
               this.func_147499_a(ZYGARDE_CELL);
            } else {
               this.func_147499_a(ZYGARDE_CORE);
            }

            ((GenericSmdModel)CORE.getModel()).modelRenderer.setTransparent(0.8F);
            ((GenericSmdModel)MODEL.getModel()).modelRenderer.setTransparent(0.8F);
            EnumFacing orientation = (EnumFacing)state.func_177229_b(BlockZygardeCell.ORIENTATION_PROPERTY);
            if (orientation.func_176740_k() != Axis.Y) {
               EnumFacing rotation = (EnumFacing)state.func_177229_b(BlockZygardeCell.ROTATION_PROPERTY);
               GlStateManager.func_179137_b(0.0, -0.5, 0.0);
               GlStateManager.func_179114_b(270.0F, rotation == EnumFacing.UP ? 1.0F : -1.0F, 0.0F, 0.0F);
               if (rotation == EnumFacing.DOWN) {
                  GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
               }

               GlStateManager.func_179137_b(0.0, 0.5, 0.0);
            } else if (orientation == EnumFacing.UP) {
               GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179109_b(0.0F, 1.0F, 0.0F);
            }

            MODEL.render(0.03F);
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b(0.0, -0.11, 0.0);
            CORE.render(0.03F);
            GlStateManager.func_179121_F();
         }
      }
   }

   protected int getRotation(IBlockState state) {
      if (this.hasProperty(state, BlockZygardeCell.ORIENTATION_PROPERTY) && this.hasProperty(state, BlockZygardeCell.ROTATION_PROPERTY)) {
         EnumFacing orientation = (EnumFacing)state.func_177229_b(BlockZygardeCell.ORIENTATION_PROPERTY);
         EnumFacing rotation = (EnumFacing)state.func_177229_b(BlockZygardeCell.ROTATION_PROPERTY);
         if (orientation.func_176740_k() == Axis.Y) {
            if (rotation == EnumFacing.EAST) {
               return 270;
            }

            if (rotation == EnumFacing.NORTH) {
               return 0;
            }

            if (rotation == EnumFacing.WEST) {
               return 90;
            }

            if (rotation == EnumFacing.SOUTH) {
               return 180;
            }
         } else {
            if (orientation == EnumFacing.EAST) {
               return 270;
            }

            if (orientation == EnumFacing.NORTH) {
               return 0;
            }

            if (orientation == EnumFacing.WEST) {
               return 90;
            }

            if (orientation == EnumFacing.SOUTH) {
               return 180;
            }
         }
      }

      return 0;
   }
}
