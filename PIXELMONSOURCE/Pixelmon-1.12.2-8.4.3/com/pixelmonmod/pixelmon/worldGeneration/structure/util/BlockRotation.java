package com.pixelmonmod.pixelmon.worldGeneration.structure.util;

import com.pixelmonmod.pixelmon.blocks.IBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class BlockRotation {
   public static int setBlockRotation(EnumFacing coordBaseMode, Block block, int par2) {
      if (block instanceof IBlockRotator) {
         IBlockRotator ibr = (IBlockRotator)block;
         return ibr.rotate(coordBaseMode, block, par2);
      } else if (block instanceof BlockStairs) {
         return rotateStairs(coordBaseMode, block, par2);
      } else {
         return block == Blocks.field_150442_at ? rotateLever(coordBaseMode, block, par2) : par2;
      }
   }

   private static int rotateLever(EnumFacing coordBaseMode, Block block, int par2) {
      int othermeta = par2 & 8;
      int side = par2 & 7;
      if (coordBaseMode == EnumFacing.EAST) {
         if (side == 4) {
            return 1 + othermeta;
         }

         if (side == 1) {
            return 3 + othermeta;
         }

         if (side == 3) {
            return 2 + othermeta;
         }

         if (side == 2) {
            return 4 + othermeta;
         }
      } else if (coordBaseMode == EnumFacing.WEST) {
         if (side == 4) {
            return 2 + othermeta;
         }

         if (side == 1) {
            return 4 + othermeta;
         }

         if (side == 3) {
            return 1 + othermeta;
         }

         if (side == 2) {
            return 3 + othermeta;
         }
      } else if (coordBaseMode == EnumFacing.NORTH) {
         if (side == 1) {
            return 2 + othermeta;
         }

         if (side == 2) {
            return 1 + othermeta;
         }

         if (side == 4) {
            return 3 + othermeta;
         }

         if (side == 3) {
            return 4 + othermeta;
         }
      }

      return par2;
   }

   private static int rotateStairs(EnumFacing coordBaseMode, Block block, int meta) {
      int flag = meta & 4;
      meta &= 3;
      switch (coordBaseMode) {
         case EAST:
            meta = meta < 2 ? meta + 2 : (meta == 2 ? 1 : 0);
            break;
         case WEST:
            meta = meta == 2 ? 3 : (meta == 3 ? 2 : meta);
            break;
         case NORTH:
            meta = meta < 2 ? meta + 2 : meta - 2;
      }

      return meta | flag;
   }

   private static int rotatePixelmonBlock(int coordBaseMode, Block block, int par2) {
      if (coordBaseMode == 2) {
         if (par2 == 2) {
            return 0;
         }

         if (par2 == 0) {
            return 2;
         }
      } else if (coordBaseMode == 1) {
         if (par2 == 0) {
            return 3;
         }

         if (par2 == 1) {
            return 0;
         }

         if (par2 == 2) {
            return 1;
         }

         if (par2 == 3) {
            return 2;
         }
      } else if (coordBaseMode == 3) {
         if (par2 == 2) {
            return 3;
         }

         if (par2 == 1) {
            return 0;
         }

         if (par2 == 0) {
            return 1;
         }

         if (par2 == 3) {
            return 2;
         }
      }

      return par2;
   }

   public static int setPixelmonBlockRotation(int coordBaseMode, Block block, int par2) {
      return rotatePixelmonBlock(coordBaseMode, block, par2);
   }
}
