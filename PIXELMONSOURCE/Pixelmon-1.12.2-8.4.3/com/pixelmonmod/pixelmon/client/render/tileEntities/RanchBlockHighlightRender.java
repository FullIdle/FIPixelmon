package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.ranch.BlockRanchBlock;
import com.pixelmonmod.pixelmon.blocks.ranch.RanchBounds;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.util.Bounds;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RanchBlockHighlightRender {
   private static ResourceLocation ranchOverlay = new ResourceLocation("pixelmon:textures/blocks/ranchOverlay.png");
   private TileEntityRanchBlock lastBlock;
   private int tick = 0;

   @SubscribeEvent
   public void onHighlight(DrawBlockHighlightEvent event) {
      if (event.getTarget().field_72313_a == Type.BLOCK) {
         World world = event.getPlayer().field_70170_p;
         Block block = world.func_180495_p(event.getTarget().func_178782_a()).func_177230_c();
         if (block instanceof BlockRanchBlock) {
            try {
               IBlockState state = world.func_180495_p(event.getTarget().func_178782_a());
               MultiBlock mb = (MultiBlock)state.func_177230_c();
               BlockPos loc = mb.findBaseBlock(world, new BlockPos.MutableBlockPos(event.getTarget().func_178782_a()), state);
               TileEntityRanchBlock rb = (TileEntityRanchBlock)world.func_175625_s(loc);
               if (Objects.equals(event.getPlayer().func_110124_au(), rb.getOwnerUUID())) {
                  if (rb == this.lastBlock) {
                     if (this.tick >= 40) {
                        this.renderBounds(rb, event.getPlayer(), world, event.getPartialTicks());
                     } else {
                        ++this.tick;
                     }
                  } else {
                     ++this.tick;
                  }
               } else {
                  this.tick = 0;
               }

               this.lastBlock = rb;
            } catch (Exception var8) {
            }
         } else {
            this.tick = 0;
         }
      } else {
         this.tick = 0;
      }

   }

   private void renderBounds(TileEntityRanchBlock te, EntityPlayer player, World world, float partialTicks) {
      Bounds b = te.getBounds();
      RanchBounds bounds = null;
      if (b instanceof RanchBounds) {
         bounds = (RanchBounds)b;
      }

      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      GlStateManager.func_179147_l();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179126_j();
      GlStateManager.func_179112_b(770, 771);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ranchOverlay);

      for(int x = b.left; x <= b.right; ++x) {
         for(int z = b.bottom; z <= b.top; ++z) {
            if (x != te.func_174877_v().func_177958_n() || z != te.func_174877_v().func_177952_p()) {
               BlockPos xz = new BlockPos(x, 0, z);
               BlockPos pos;
               if (bounds == null) {
                  pos = world.func_175672_r(xz);
               } else {
                  pos = bounds.getTopBlock(world, xz);
               }

               Block block = world.func_180495_p(pos).func_177230_c();
               float f1 = 0.002F;
               double d0 = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)partialTicks;
               double d1 = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)partialTicks;
               double d2 = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)partialTicks;
               this.drawBlockOverlay(tessellator, block.func_180640_a(world.func_180495_p(pos), world, pos).func_72321_a((double)f1, (double)f1, (double)f1).func_72317_d(-d0, -d1, -d2));
            }
         }
      }

      tessellator.func_78381_a();
      GlStateManager.func_179084_k();
   }

   private void drawBlockOverlay(Tessellator tessellator, AxisAlignedBB box) {
      tessellator.func_178180_c().func_181662_b(box.field_72340_a + 0.15, box.field_72338_b + 0.05000000074505806, box.field_72334_f - 0.15).func_187315_a(0.0, 1.0).func_181675_d();
      tessellator.func_178180_c().func_181662_b(box.field_72336_d - 0.15, box.field_72338_b + 0.05000000074505806, box.field_72334_f - 0.15).func_187315_a(1.0, 1.0).func_181675_d();
      tessellator.func_178180_c().func_181662_b(box.field_72336_d - 0.15, box.field_72338_b + 0.05000000074505806, box.field_72339_c + 0.15).func_187315_a(1.0, 0.0).func_181675_d();
      tessellator.func_178180_c().func_181662_b(box.field_72340_a + 0.15, box.field_72338_b + 0.05000000074505806, box.field_72339_c + 0.15).func_187315_a(0.0, 0.0).func_181675_d();
   }
}
