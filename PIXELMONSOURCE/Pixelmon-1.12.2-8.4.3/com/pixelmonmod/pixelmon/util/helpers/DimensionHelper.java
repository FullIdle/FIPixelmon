package com.pixelmonmod.pixelmon.util.helpers;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.DrownedWorld;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.DrownedWorldTeleporter;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceTeleporter;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DimensionHelper {
   public static void teleport(EntityPlayerMP entity, int dimension, double x, double y, double z) {
      entity.field_70159_w = entity.field_70181_x = entity.field_70179_y = 0.0;
      entity.field_70143_R = 0.0F;
      entity.field_71135_a.func_147364_a(x, y, z, entity.field_70177_z, entity.field_70125_A);
      if (entity.field_70170_p.field_73011_w.getDimension() != dimension && entity.func_184102_h() != null) {
         WorldServer destination = entity.func_184102_h().func_71218_a(dimension);
         Object teleporter;
         if (dimension == DrownedWorld.DIM_ID) {
            entity.getEntityData().func_74780_a("PortalX", entity.field_70165_t);
            entity.getEntityData().func_74780_a("PortalY", entity.field_70163_u);
            entity.getEntityData().func_74780_a("PortalZ", entity.field_70161_v);
            entity.getEntityData().func_74768_a("PortalD", entity.field_71093_bK);
            teleporter = new DrownedWorldTeleporter(destination);
         } else {
            teleporter = new UltraSpaceTeleporter(destination);
         }

         entity.field_71133_b.func_184103_al().transferPlayerToDimension(entity, dimension, (Teleporter)teleporter);
         findSafeTeleportLocation(entity, entity.field_70170_p.field_73012_v, entity.func_71121_q());
         destination.func_184133_a((EntityPlayer)null, entity.func_180425_c(), SoundEvents.field_187812_eh, SoundCategory.MASTER, 0.5F, 1.0F);
      }

   }

   public static void forceTeleport(EntityPlayerMP entity, int dimension, double x, double y, double z, float yaw, float pitch) {
      entity.field_70159_w = entity.field_70181_x = entity.field_70179_y = 0.0;
      entity.field_70143_R = 0.0F;
      if (dimension == DrownedWorld.DIM_ID) {
         entity.getEntityData().func_74780_a("PortalX", entity.field_70165_t);
         entity.getEntityData().func_74780_a("PortalY", entity.field_70163_u);
         entity.getEntityData().func_74780_a("PortalZ", entity.field_70161_v);
         entity.getEntityData().func_74768_a("PortalD", entity.field_71093_bK);
      }

      entity.field_71135_a.func_147364_a(x, y, z, yaw, pitch);
      if (entity.field_70170_p.field_73011_w.getDimension() != dimension && entity.func_184102_h() != null) {
         WorldServer destination = entity.func_184102_h().func_71218_a(dimension);
         entity.field_71133_b.func_184103_al().transferPlayerToDimension(entity, dimension, new UltraSpaceTeleporter(destination));
      }

   }

   public static boolean findSafeTeleportLocation(EntityPlayerMP entityIn, Random random, WorldServer world) {
      int i = true;
      double d0 = -1.0;
      int j = MathHelper.func_76128_c(entityIn.field_70165_t);
      int k = MathHelper.func_76128_c(entityIn.field_70163_u);
      int l = MathHelper.func_76128_c(entityIn.field_70161_v);
      int i1 = j;
      int j1 = k;
      int k1 = l;
      int l1 = false;
      int i2 = random.nextInt(4);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      int l5;
      double d3;
      int j6;
      double d4;
      int i7;
      int k7;
      int j8;
      int j9;
      int j10;
      int j11;
      int j12;
      int i13;
      int j13;
      int l1;
      double d6;
      double d8;
      for(l5 = j - 16; l5 <= j + 16; ++l5) {
         d3 = (double)l5 + 0.5 - entityIn.field_70165_t;

         for(j6 = l - 16; j6 <= l + 16; ++j6) {
            d4 = (double)j6 + 0.5 - entityIn.field_70161_v;

            label182:
            for(i7 = world.func_72940_L() - 1; i7 >= 0; --i7) {
               if (world.func_175623_d(blockpos$mutableblockpos.func_181079_c(l5, i7, j6))) {
                  while(i7 > 0 && world.func_175623_d(blockpos$mutableblockpos.func_181079_c(l5, i7 - 1, j6))) {
                     --i7;
                  }

                  for(k7 = i2; k7 < i2 + 4; ++k7) {
                     j8 = k7 % 2;
                     j9 = 1 - j8;
                     if (k7 % 4 >= 2) {
                        j8 = -j8;
                        j9 = -j9;
                     }

                     for(j10 = 0; j10 < 3; ++j10) {
                        for(j11 = 0; j11 < 4; ++j11) {
                           for(j12 = -1; j12 < 4; ++j12) {
                              i13 = l5 + (j11 - 1) * j8 + j10 * j9;
                              j13 = i7 + j12;
                              int k5 = j6 + (j11 - 1) * j9 - j10 * j8;
                              blockpos$mutableblockpos.func_181079_c(i13, j13, k5);
                              if (j12 < 0 && !world.func_180495_p(blockpos$mutableblockpos).func_185904_a().func_76220_a() || j12 >= 0 && !world.func_175623_d(blockpos$mutableblockpos)) {
                                 continue label182;
                              }
                           }
                        }
                     }

                     d6 = (double)i7 + 0.5 - entityIn.field_70163_u;
                     d8 = d3 * d3 + d6 * d6 + d4 * d4;
                     if (d0 < 0.0 || d8 < d0) {
                        d0 = d8;
                        i1 = l5;
                        j1 = i7;
                        k1 = j6;
                        l1 = k7 % 4;
                     }
                  }
               }
            }
         }
      }

      if (d0 < 0.0) {
         for(l5 = j - 16; l5 <= j + 16; ++l5) {
            d3 = (double)l5 + 0.5 - entityIn.field_70165_t;

            for(j6 = l - 16; j6 <= l + 16; ++j6) {
               d4 = (double)j6 + 0.5 - entityIn.field_70161_v;

               label120:
               for(i7 = world.func_72940_L() - 1; i7 >= 0; --i7) {
                  if (world.func_175623_d(blockpos$mutableblockpos.func_181079_c(l5, i7, j6))) {
                     while(i7 > 0 && world.func_175623_d(blockpos$mutableblockpos.func_181079_c(l5, i7 - 1, j6))) {
                        --i7;
                     }

                     for(k7 = i2; k7 < i2 + 2; ++k7) {
                        j8 = k7 % 2;
                        j9 = 1 - j8;

                        for(j10 = 0; j10 < 4; ++j10) {
                           for(j11 = -1; j11 < 4; ++j11) {
                              j12 = l5 + (j10 - 1) * j8;
                              i13 = i7 + j11;
                              j13 = j6 + (j10 - 1) * j9;
                              blockpos$mutableblockpos.func_181079_c(j12, i13, j13);
                              if (j11 < 0 && !world.func_180495_p(blockpos$mutableblockpos).func_185904_a().func_76220_a() || j11 >= 0 && !world.func_175623_d(blockpos$mutableblockpos)) {
                                 continue label120;
                              }
                           }
                        }

                        d6 = (double)i7 + 0.5 - entityIn.field_70163_u;
                        d8 = d3 * d3 + d6 * d6 + d4 * d4;
                        if (d0 < 0.0 || d8 < d0) {
                           d0 = d8;
                           i1 = l5;
                           j1 = i7;
                           k1 = j6;
                           l1 = k7 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      entityIn.field_71135_a.func_147364_a((double)i1, (double)(j1 + 2), (double)k1, random.nextFloat() * 2.0F - 1.0F, random.nextFloat() * 2.0F - 1.0F);
      return true;
   }
}
