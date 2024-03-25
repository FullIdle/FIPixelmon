package com.pixelmonmod.pixelmon.client.render.blockReveal;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BlockRevealRenderer {
   public static ConcurrentHashMap revealedBlocks = new ConcurrentHashMap();
   private static boolean increasing = true;
   private static short ticker = 0;

   private BlockRevealRenderer() {
   }

   public static void update() {
      Iterator iterator = revealedBlocks.values().iterator();

      while(iterator.hasNext()) {
         BlockReveal reveal = (BlockReveal)iterator.next();
         reveal.decreaseTicksRemaining(1);
         if (reveal.getTicksRemaining() <= 0) {
            iterator.remove();
         }
      }

   }

   public static void doRender() {
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.field_71441_e == null) {
         revealedBlocks.clear();
      } else if (!revealedBlocks.isEmpty()) {
         if (increasing) {
            ++ticker;
            if (ticker >= 100) {
               increasing = false;
            }
         } else {
            --ticker;
            if (ticker <= 0) {
               increasing = true;
            }
         }

         mc.field_71424_I.func_76320_a("pixelmon:block_reveal");
         preRender();
         Iterator var1 = revealedBlocks.values().iterator();

         while(var1.hasNext()) {
            BlockReveal value = (BlockReveal)var1.next();
            render(value);
         }

         postRender();
         mc.field_71424_I.func_76319_b();
      }

   }

   private static void render(BlockReveal block) {
      Minecraft mc = Minecraft.func_71410_x();
      block.drawDown = true;
      block.drawUp = true;
      block.drawNorth = true;
      block.drawSouth = true;
      block.drawEast = true;
      block.drawWest = true;
      Iterator var2 = revealedBlocks.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         BlockPos revealPos = (BlockPos)entry.getKey();
         BlockPos blockPos = block.getPos();
         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.DOWN))) {
            block.drawDown = false;
         }

         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.UP))) {
            block.drawUp = false;
         }

         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.NORTH))) {
            block.drawNorth = false;
         }

         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.SOUTH))) {
            block.drawSouth = false;
         }

         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.EAST))) {
            block.drawEast = false;
         }

         if (revealPos.equals(blockPos.func_177972_a(EnumFacing.WEST))) {
            block.drawWest = false;
         }
      }

      int pattern = block.getPattern();
      boolean draw = true;
      float r = 0.0F;
      float g = 0.0F;
      float b = 0.0F;
      if (block.getPattern() == 7) {
         int time = (int)(mc.field_71441_e.func_72820_D() % 24000L);
         if (WorldTime.DAWN.tickCondition.test(time)) {
            r = 0.54509807F;
            g = 0.75686276F;
            b = 0.7882353F;
            pattern = 12;
         } else if (WorldTime.DUSK.tickCondition.test(time)) {
            r = 0.4627451F;
            g = 0.44313726F;
            b = 0.7411765F;
            pattern = 13;
         } else {
            draw = false;
         }
      } else {
         r = (float)block.getColor().getRed() / 255.0F;
         g = (float)block.getColor().getGreen() / 255.0F;
         b = (float)block.getColor().getBlue() / 255.0F;
      }

      if (draw) {
         GlStateManager.func_179131_c(r, g, b, 0.7F - 0.4F * ((float)ticker / 100.0F) - (block.getTicksRemaining() <= 30 ? 0.7F - 0.7F * ((float)block.getTicksRemaining() / 30.0F) : 0.0F));
         patternProvider(block, pattern);
      }

   }

   private static void patternProvider(BlockReveal block, int pattern) {
      switch (pattern) {
         case 0:
            pattern0(block);
            break;
         case 1:
            pattern1(block);
            break;
         case 2:
            pattern2(block);
            break;
         case 3:
            pattern3(block);
            break;
         case 4:
            pattern4(block);
            break;
         case 5:
            pattern5(block);
            break;
         case 6:
            pattern6(block);
            break;
         case 7:
            pattern7(block);
            break;
         case 8:
            pattern8(block);
            break;
         case 9:
            pattern9(block);
            break;
         case 10:
            pattern10(block);
            break;
         case 11:
            pattern11(block);
            break;
         case 12:
            pattern12(block);
            break;
         case 13:
            pattern13(block);
      }

   }

   private static void pattern0(BlockReveal block) {
      drawQuad(block, 4, 13, 5, 14);
      drawQuad(block, 7, 12, 9, 13);
      drawQuad(block, 12, 13, 14, 14);
      drawQuad(block, 10, 9, 12, 11);
      drawQuad(block, 8, 8, 10, 7);
      drawQuad(block, 7, 7, 11, 6);
      drawQuad(block, 5, 11, 7, 10);
      drawQuad(block, 3, 10, 8, 9);
      drawQuad(block, 1, 8, 3, 7);
      drawQuad(block, 4, 6, 5, 5);
      drawQuad(block, 3, 3, 5, 2);
      drawQuad(block, 11, 5, 13, 4);
      drawQuad(block, 8, 4, 14, 3);
      drawQuad(block, 9, 3, 11, 2);
   }

   private static void pattern1(BlockReveal block) {
      drawQuad(block, 8, 15, 6, 13);
      drawQuad(block, 14, 13, 12, 11);
      drawQuad(block, 6, 12, 4, 10);
      drawQuad(block, 8, 11, 6, 9);
      drawQuad(block, 8, 8, 6, 6);
      drawQuad(block, 12, 7, 10, 5);
      drawQuad(block, 15, 6, 13, 4);
      drawQuad(block, 9, 5, 7, 3);
      drawQuad(block, 3, 3, 1, 1);
   }

   private static void pattern2(BlockReveal block) {
      drawQuad(block, 14, 14, 12, 13);
      drawQuad(block, 9, 13, 7, 12);
      drawQuad(block, 6, 14, 2, 13);
      drawQuad(block, 6, 13, 5, 12);
      drawQuad(block, 12, 11, 10, 9);
      drawQuad(block, 7, 11, 5, 10);
      drawQuad(block, 8, 10, 3, 9);
      drawQuad(block, 3, 9, 1, 7);
      drawQuad(block, 12, 8, 11, 6);
      drawQuad(block, 11, 8, 9, 5);
      drawQuad(block, 9, 7, 8, 5);
      drawQuad(block, 8, 8, 7, 6);
      drawQuad(block, 15, 7, 13, 3);
      drawQuad(block, 11, 4, 8, 2);
      drawQuad(block, 5, 6, 4, 5);
      drawQuad(block, 5, 5, 2, 4);
      drawQuad(block, 6, 4, 5, 3);
      drawQuad(block, 7, 3, 4, 1);
   }

   private static void pattern3(BlockReveal block) {
      drawQuad(block, 11, 2, 13, 3);
      drawQuad(block, 13, 3, 14, 4);
      drawQuad(block, 6, 2, 8, 3);
      drawQuad(block, 7, 3, 9, 4);
      drawQuad(block, 8, 4, 10, 5);
      drawQuad(block, 9, 5, 10, 6);
      drawQuad(block, 11, 6, 12, 7);
      drawQuad(block, 12, 5, 13, 8);
      drawQuad(block, 13, 6, 14, 9);
      drawQuad(block, 14, 7, 15, 10);
      drawQuad(block, 2, 4, 3, 5);
      drawQuad(block, 3, 4, 4, 6);
      drawQuad(block, 4, 5, 5, 7);
      drawQuad(block, 5, 6, 7, 9);
      drawQuad(block, 7, 8, 8, 10);
      drawQuad(block, 8, 8, 9, 11);
      drawQuad(block, 9, 9, 10, 11);
      drawQuad(block, 10, 9, 11, 12);
      drawQuad(block, 11, 10, 12, 12);
      drawQuad(block, 2, 8, 3, 11);
      drawQuad(block, 3, 9, 4, 10);
      drawQuad(block, 4, 10, 6, 13);
      drawQuad(block, 6, 11, 7, 14);
      drawQuad(block, 7, 12, 8, 13);
      drawQuad(block, 8, 12, 9, 14);
      drawQuad(block, 9, 13, 10, 15);
   }

   private static void pattern4(BlockReveal block) {
      drawQuad(block, 3, 2, 4, 4);
      drawQuad(block, 2, 3, 3, 4);
      drawQuad(block, 13, 1, 14, 2);
      drawQuad(block, 11, 4, 13, 5);
      drawQuad(block, 9, 7, 11, 8);
      drawQuad(block, 5, 5, 6, 7);
      drawQuad(block, 6, 4, 7, 7);
      drawQuad(block, 7, 5, 8, 6);
      drawQuad(block, 1, 7, 2, 8);
      drawQuad(block, 3, 8, 5, 9);
      drawQuad(block, 5, 9, 8, 10);
      drawQuad(block, 10, 12, 11, 13);
      drawQuad(block, 11, 9, 12, 10);
      drawQuad(block, 12, 8, 13, 11);
      drawQuad(block, 13, 9, 14, 12);
      drawQuad(block, 1, 12, 3, 13);
      drawQuad(block, 3, 11, 4, 14);
      drawQuad(block, 4, 12, 5, 13);
   }

   private static void pattern5(BlockReveal block) {
      drawQuad(block, 5, 6, 12, 8);
      drawQuad(block, 7, 8, 10, 10);
      drawQuad(block, 6, 8, 7, 9);
      drawQuad(block, 8, 10, 9, 11);
      drawQuad(block, 6, 5, 11, 6);
      drawQuad(block, 8, 4, 10, 5);
      drawQuad(block, 8, 3, 9, 4);
   }

   private static void pattern6(BlockReveal block) {
      drawSizedQuad(block, 1, 8, 1, 1);
      drawSizedQuad(block, 2, 5, 1, 5);
      drawSizedQuad(block, 3, 4, 1, 7);
      drawSizedQuad(block, 4, 3, 6, 9);
      drawSizedQuad(block, 5, 12, 3, 1);
      drawSizedQuad(block, 10, 4, 1, 6);
      drawSizedQuad(block, 11, 4, 1, 5);
      drawSizedQuad(block, 12, 5, 1, 4);
      drawSizedQuad(block, 13, 5, 1, 2);
      drawSizedQuad(block, 13, 8, 1, 1);
      drawSizedQuad(block, 14, 5, 1, 1);
   }

   private static void pattern7(BlockReveal block) {
   }

   private static void pattern8(BlockReveal block) {
      drawSizedQuad(block, 7, 3, 1, 2);
      drawSizedQuad(block, 8, 3, 2, 3);
      drawSizedQuad(block, 10, 3, 1, 4);
      drawSizedQuad(block, 11, 4, 1, 6);
   }

   private static void pattern9(BlockReveal block) {
      drawSizedQuad(block, 3, 11, 1, 1);
      drawSizedQuad(block, 4, 11, 1, 2);
      drawSizedQuad(block, 5, 10, 2, 3);
      drawSizedQuad(block, 7, 9, 1, 3);
      drawSizedQuad(block, 8, 8, 1, 4);
      drawSizedQuad(block, 9, 10, 1, 1);
      drawSizedQuad(block, 9, 7, 1, 1);
      drawSizedQuad(block, 9, 5, 1, 1);
   }

   private static void pattern10(BlockReveal block) {
      drawSizedQuad(block, 5, 2, 1, 1);
      drawSizedQuad(block, 5, 4, 1, 1);
      drawSizedQuad(block, 6, 3, 3, 3);
      drawSizedQuad(block, 9, 4, 3, 2);
      drawSizedQuad(block, 7, 6, 4, 1);
      drawSizedQuad(block, 8, 7, 2, 1);
      drawSizedQuad(block, 10, 8, 1, 1);
   }

   private static void pattern11(BlockReveal block) {
      drawSizedQuad(block, 11, 4, 1, 1);
      drawSizedQuad(block, 7, 5, 6, 2);
      drawSizedQuad(block, 8, 7, 4, 1);
      drawSizedQuad(block, 10, 8, 2, 1);
   }

   private static void pattern12(BlockReveal block) {
      drawSizedQuad(block, 4, 5, 1, 1);
      drawSizedQuad(block, 5, 4, 1, 3);
      drawSizedQuad(block, 6, 4, 5, 4);
      drawSizedQuad(block, 8, 3, 2, 1);
      drawSizedQuad(block, 11, 4, 1, 2);
      drawSizedQuad(block, 7, 8, 3, 1);
      drawSizedQuad(block, 8, 9, 1, 1);
   }

   private static void pattern13(BlockReveal block) {
      drawSizedQuad(block, 6, 8, 1, 2);
      drawSizedQuad(block, 7, 7, 1, 3);
      drawSizedQuad(block, 8, 6, 2, 3);
      drawSizedQuad(block, 10, 3, 1, 5);
      drawSizedQuad(block, 11, 5, 1, 3);
   }

   private static void drawSizedQuad(BlockReveal block, int x, int z, int w, int h) {
      drawQuad(block, x, z, x + w, z + h);
   }

   private static void drawQuad(BlockReveal block, int x1, int z1, int x2, int z2) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;

      for(int side = 0; side < 6; ++side) {
         double yaw = 0.0;
         double pitch = 0.0;
         boolean inverse = false;
         switch (side) {
            case 0:
               if (player.field_70163_u + (double)player.eyeHeight > (double)block.getPos().func_177956_o() || !block.drawDown) {
                  continue;
               }
               break;
            case 1:
               if (player.field_70163_u + (double)player.eyeHeight < (double)(block.getPos().func_177956_o() + 1) || !block.drawUp) {
                  continue;
               }

               pitch = Math.PI;
               break;
            case 2:
               if (player.field_70161_v < (double)(block.getPos().func_177952_p() + 1) || !block.drawSouth) {
                  continue;
               }

               pitch = 1.5707963267948966;
               break;
            case 3:
               if (player.field_70161_v > (double)block.getPos().func_177952_p() || !block.drawNorth) {
                  continue;
               }

               yaw = Math.PI;
               pitch = -1.5707963267948966;
               break;
            case 4:
               if (player.field_70165_t > (double)block.getPos().func_177958_n() || !block.drawWest) {
                  continue;
               }

               inverse = true;
               yaw = 1.5707963267948966;
               pitch = 1.5707963267948966;
               break;
            case 5:
               if (player.field_70165_t < (double)(block.getPos().func_177958_n() + 1) || !block.drawEast) {
                  continue;
               }

               inverse = true;
               yaw = -1.5707963267948966;
               pitch = 1.5707963267948966;
         }

         GL11.glBegin(7);
         rotatePointAndDraw((double)((float)block.getPos().func_177958_n() + (float)x1 / 16.0F), (double)block.getPos().func_177956_o(), (double)((float)block.getPos().func_177952_p() + (float)z1 / 16.0F), (double)((float)block.getPos().func_177958_n() + 0.5F), (double)((float)block.getPos().func_177956_o() + 0.5F), (double)((float)block.getPos().func_177952_p() + 0.5F), yaw, pitch, inverse);
         rotatePointAndDraw((double)((float)block.getPos().func_177958_n() + (float)x2 / 16.0F), (double)block.getPos().func_177956_o(), (double)((float)block.getPos().func_177952_p() + (float)z1 / 16.0F), (double)((float)block.getPos().func_177958_n() + 0.5F), (double)((float)block.getPos().func_177956_o() + 0.5F), (double)((float)block.getPos().func_177952_p() + 0.5F), yaw, pitch, inverse);
         rotatePointAndDraw((double)((float)block.getPos().func_177958_n() + (float)x2 / 16.0F), (double)block.getPos().func_177956_o(), (double)((float)block.getPos().func_177952_p() + (float)z2 / 16.0F), (double)((float)block.getPos().func_177958_n() + 0.5F), (double)((float)block.getPos().func_177956_o() + 0.5F), (double)((float)block.getPos().func_177952_p() + 0.5F), yaw, pitch, inverse);
         rotatePointAndDraw((double)((float)block.getPos().func_177958_n() + (float)x1 / 16.0F), (double)block.getPos().func_177956_o(), (double)((float)block.getPos().func_177952_p() + (float)z2 / 16.0F), (double)((float)block.getPos().func_177958_n() + 0.5F), (double)((float)block.getPos().func_177956_o() + 0.5F), (double)((float)block.getPos().func_177952_p() + 0.5F), yaw, pitch, inverse);
         GL11.glEnd();
      }

   }

   private static void preRender() {
      Minecraft mc = Minecraft.func_71410_x();
      GlStateManager.func_179094_E();
      Entity viewpoint = mc.func_175606_aa() == null ? mc.field_71439_g : mc.func_175606_aa();
      float tx = (float)(((Entity)viewpoint).field_70169_q + (((Entity)viewpoint).field_70165_t - ((Entity)viewpoint).field_70169_q) * (double)mc.func_184121_ak());
      float ty = (float)(((Entity)viewpoint).field_70167_r + (((Entity)viewpoint).field_70163_u - ((Entity)viewpoint).field_70167_r) * (double)mc.func_184121_ak());
      float tz = (float)(((Entity)viewpoint).field_70166_s + (((Entity)viewpoint).field_70161_v - ((Entity)viewpoint).field_70166_s) * (double)mc.func_184121_ak());
      GlStateManager.func_179090_x();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179140_f();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179097_i();
      GlStateManager.func_179129_p();
      GlStateManager.func_179109_b(-tx, -ty, -tz);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glHint(3155, 4354);
   }

   private static void postRender() {
      GlStateManager.func_179089_o();
      GlStateManager.func_179084_k();
      GlStateManager.func_179098_w();
      GlStateManager.func_179145_e();
      GlStateManager.func_179126_j();
      GlStateManager.func_179141_d();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   private static double[] rotatePoint(double px, double py, double pz, double cx, double cy, double cz, double yaw, double pitch, boolean inverse) {
      double rx;
      double rz;
      double ry;
      if (inverse) {
         rx = Math.sin(pitch) * (pz - cz) + Math.cos(pitch) * (py - cy) + cy;
         rz = Math.cos(pitch) * (pz - cz) - Math.sin(pitch) * (py - cy) + cz;
         ry = Math.cos(yaw) * (px - cx) - Math.sin(yaw) * (rz - cz) + cx;
         rz = Math.sin(yaw) * (px - cx) + Math.cos(yaw) * (rz - cz) + cz;
         return new double[]{ry, rx, rz};
      } else {
         rx = Math.cos(yaw) * (px - cx) - Math.sin(yaw) * (pz - cz) + cx;
         rz = Math.sin(yaw) * (px - cx) + Math.cos(yaw) * (pz - cz) + cz;
         ry = Math.sin(pitch) * (rz - cz) + Math.cos(pitch) * (py - cy) + cy;
         rz = Math.cos(pitch) * (rz - cz) - Math.sin(pitch) * (py - cy) + cz;
         return new double[]{rx, ry, rz};
      }
   }

   private static void rotatePointAndDraw(double px, double py, double pz, double cx, double cy, double cz, double yaw, double pitch, boolean inverse) {
      double[] points = rotatePoint(px, py, pz, cx, cy, cz, yaw, pitch, inverse);
      GL11.glVertex3d(points[0], points[1], points[2]);
   }

   @SubscribeEvent
   public static void onBlockBreak(BlockEvent.BreakEvent event) {
      revealedBlocks.remove(event.getPos());
   }

   @SubscribeEvent
   public static void onBlockBreak(WorldEvent.Load event) {
      if (event.getWorld().field_72995_K) {
         revealedBlocks.clear();
      }

   }
}
