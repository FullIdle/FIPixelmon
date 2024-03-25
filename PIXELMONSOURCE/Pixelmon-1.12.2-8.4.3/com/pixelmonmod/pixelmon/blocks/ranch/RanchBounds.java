package com.pixelmonmod.pixelmon.blocks.ranch;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.util.Bounds;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RanchBounds extends Bounds {
   public final TileEntityRanchBlock ranch;
   private int originalTop;
   private int originalLeft;
   private int originalBottom;
   private int originalRight;
   private int height;
   private static final int HEIGHT_OFFSET = 3;

   public RanchBounds(TileEntityRanchBlock tileEntityRanchBlock) {
      this.ranch = tileEntityRanchBlock;
   }

   public RanchBounds(TileEntityRanchBlock ranch, int top, int left, int bottom, int right, int height) {
      super(top, left, bottom, right);
      this.ranch = ranch;
      this.originalTop = top;
      this.originalLeft = left;
      this.originalBottom = bottom;
      this.originalRight = right;
      this.height = height + 3;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      nbt.func_74768_a("origTop", this.originalTop);
      nbt.func_74768_a("origLeft", this.originalLeft);
      nbt.func_74768_a("origBot", this.originalBottom);
      nbt.func_74768_a("origRight", this.originalRight);
      nbt.func_74768_a("height", this.height);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      super.readFromNBT(nbt);
      this.originalTop = nbt.func_74762_e("origTop");
      this.originalLeft = nbt.func_74762_e("origLeft");
      this.originalBottom = nbt.func_74762_e("origBot");
      this.originalRight = nbt.func_74762_e("origRight");
      this.height = nbt.func_74762_e("height");
   }

   public BreedingConditions getContainingBreedingConditions(World world) {
      ArrayList blockList = new ArrayList();

      for(int x = this.left; x <= this.right; ++x) {
         for(int z = this.bottom; z <= this.top; ++z) {
            if (x != (this.left + this.right) / 2 || z != (this.top + this.bottom) / 2) {
               BlockPos pos = this.getTopBlock(world, new BlockPos(x, 0, z));
               if (pos.func_177956_o() > -1) {
                  blockList.add(world.func_180495_p(pos.func_177977_b()).func_177230_c());
                  if (world.func_180495_p(pos.func_177984_a()).func_185904_a() != Material.field_151579_a) {
                     blockList.add(world.func_180495_p(pos).func_177230_c());
                  }
               }
            }
         }
      }

      return new BreedingConditions(blockList);
   }

   public BlockPos getTopBlock(World world, BlockPos pos) {
      Chunk chunk = world.func_175726_f(pos);

      BlockPos blockPos1;
      BlockPos blockPos2;
      for(blockPos1 = new BlockPos(pos.func_177958_n(), this.height, pos.func_177952_p()); blockPos1.func_177956_o() >= 0; blockPos1 = blockPos2) {
         blockPos2 = blockPos1.func_177977_b();
         IBlockState state = chunk.func_177435_g(blockPos2);
         if (state.func_185904_a() != Material.field_151579_a) {
            break;
         }
      }

      return blockPos1;
   }

   public boolean canExtend(int top, int left, int bottom, int right) {
      if (this.top + top - this.originalTop >= 4) {
         return false;
      } else if (this.left - left - this.originalLeft <= -4) {
         return false;
      } else if (this.bottom - bottom - this.originalBottom <= -4) {
         return false;
      } else {
         return this.right + right - this.originalRight < 4;
      }
   }

   public void extend(EntityPlayerMP player, int top, int left, int bottom, int right) {
      if (this.canExtend(top, left, bottom, right)) {
         this.top += top;
         this.left -= left;
         this.bottom -= bottom;
         this.right += right;
         player.field_71135_a.func_147359_a(this.ranch.func_189518_D_());
      }
   }

   public boolean canExtend() {
      return this.canExtend(1, 0, 0, 0) || this.canExtend(0, 1, 0, 0) || this.canExtend(0, 0, 1, 0) || this.canExtend(0, 0, 0, 1);
   }

   public int[] getRandomLocation(Random rand) {
      int[] xz = new int[]{this.left + rand.nextInt(this.right - this.left + 1), this.bottom + rand.nextInt(this.top - this.bottom + 1)};
      return xz;
   }
}
