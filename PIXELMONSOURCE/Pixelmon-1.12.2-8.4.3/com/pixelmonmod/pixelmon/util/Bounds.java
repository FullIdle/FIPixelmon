package com.pixelmonmod.pixelmon.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Bounds {
   public int top;
   public int left;
   public int bottom;
   public int right;
   protected World world;

   public Bounds() {
      this.top = this.left = this.bottom = this.right = 0;
   }

   public Bounds(int top, int left, int bottom, int right) {
      this.top = top;
      this.left = left;
      this.bottom = bottom;
      this.right = right;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("boundsTop", this.top);
      nbt.func_74768_a("boundsLeft", this.left);
      nbt.func_74768_a("boundsBottom", this.bottom);
      nbt.func_74768_a("boundsRight", this.right);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.top = nbt.func_74762_e("boundsTop");
      this.left = nbt.func_74762_e("boundsLeft");
      this.bottom = nbt.func_74762_e("boundsBottom");
      this.right = nbt.func_74762_e("boundsRight");
   }

   public boolean isIn(Vec3d vec3) {
      return vec3 == null ? false : this.isIn(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c);
   }

   public boolean isIn(double x, double y, double z) {
      return x >= (double)this.left && x <= (double)this.right && z >= (double)this.bottom && z <= (double)this.top;
   }

   public void setWorldObj(World world) {
      this.world = world;
   }

   public boolean canExtend() {
      return false;
   }

   public boolean canExtend(int i, int j, int k, int l) {
      return false;
   }

   public void extend(EntityPlayerMP playerEntity, int i, int j, int k, int l) {
   }
}
