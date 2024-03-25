package com.pixelmonmod.pixelmon.blocks.tileEntities;

import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityWarpPlate extends TileEntity {
   private String warpX = null;
   private String warpY = null;
   private String warpZ = null;
   private BlockPos warpPosition = null;

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.warpX != null) {
         nbt.func_74778_a("warp_x", this.warpX);
         nbt.func_74778_a("warp_y", this.warpY);
         nbt.func_74778_a("warp_z", this.warpZ);
      }

      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("warp_x")) {
         this.warpX = nbt.func_74779_i("warp_x");
         this.warpY = nbt.func_74779_i("warp_y");
         this.warpZ = nbt.func_74779_i("warp_z");
         this.warpPosition = this.calculatePosition(this.warpX, this.warpY, this.warpZ);
      } else if (nbt.func_74764_b("warpPosition")) {
         this.warpPosition = BlockPos.func_177969_a(nbt.func_74763_f("warpPosition"));
         this.warpX = String.valueOf(this.warpPosition.func_177958_n());
         this.warpY = String.valueOf(this.warpPosition.func_177956_o());
         this.warpZ = String.valueOf(this.warpPosition.func_177952_p());
      }

   }

   public BlockPos getWarpPosition() {
      return this.warpPosition;
   }

   public void setWarpPosition(String x, String y, String z) {
      this.warpX = x;
      this.warpY = y;
      this.warpZ = z;
      this.warpPosition = this.calculatePosition(this.warpX, this.warpY, this.warpZ);
      this.func_70296_d();
   }

   public BlockPos calculatePosition(String warpX, String warpY, String warpZ) {
      String[] wPos = new String[]{warpX, warpY, warpZ};
      int[] pos = new int[]{this.func_174877_v().func_177958_n(), this.func_174877_v().func_177956_o(), this.func_174877_v().func_177952_p()};
      double[] nPos = new double[3];

      for(int i = 0; i < wPos.length; ++i) {
         try {
            nPos[i] = CommandBase.func_175761_b((double)pos[i], wPos[i], false);
         } catch (NullPointerException | NumberInvalidException var9) {
            return null;
         }
      }

      return new BlockPos(nPos[0], nPos[1], nPos[2]);
   }
}
