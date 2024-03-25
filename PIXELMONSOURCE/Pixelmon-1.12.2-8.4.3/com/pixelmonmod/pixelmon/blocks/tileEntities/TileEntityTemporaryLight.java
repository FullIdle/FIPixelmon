package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockTemporaryLight;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityTemporaryLight extends TileEntity implements ITickable {
   public int durationTicks = 100;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("duration")) {
         this.durationTicks = nbt.func_74762_e("duration");
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("duration", this.durationTicks);
      return nbt;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         --this.durationTicks;
         if (this.durationTicks <= 0 && this.field_145850_b.func_180495_p(this.field_174879_c).func_177230_c() instanceof BlockTemporaryLight) {
            this.field_145850_b.func_175698_g(this.field_174879_c);
         }

      }
   }
}
