package com.pixelmonmod.pixelmon.entities;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityChairMount extends Entity {
   private Block block;
   private BlockPos pos;

   public EntityChairMount(World world, @Nullable BlockPos pos) {
      super(world);
      this.pos = pos;
      this.block = pos != null && world != null ? world.func_180495_p(pos).func_177230_c() : null;
      this.func_70105_a(0.0F, 0.0F);
   }

   /** @deprecated */
   @Deprecated
   public EntityChairMount(World world) {
      this(world, (BlockPos)null);
   }

   public double func_70042_X() {
      return 0.5;
   }

   protected void func_70088_a() {
   }

   public void func_70030_z() {
      if (this.field_70163_u < -1.0 || this.field_70170_p != null && !this.field_70170_p.field_72995_K && (this.func_184188_bt().size() != 1 || this.block != null && !this.block.equals(this.field_70170_p.func_180495_p(this.pos).func_177230_c()))) {
         this.field_70128_L = true;
      }

   }

   public boolean func_180431_b(DamageSource source) {
      return source != DamageSource.field_76380_i;
   }

   public boolean func_82150_aj() {
      return true;
   }

   public void func_70091_d(MoverType type, double p_70091_1_, double p_70091_3_, double p_70091_5_) {
   }

   protected void func_70037_a(NBTTagCompound p_70037_1_) {
   }

   protected void func_70014_b(NBTTagCompound p_70014_1_) {
   }

   public boolean func_70067_L() {
      return false;
   }

   public boolean func_70104_M() {
      return false;
   }

   public void func_180430_e(float p_70069_1_, float mod) {
   }

   protected void func_184225_p(Entity passenger) {
      super.func_184225_p(passenger);
      if (!passenger.field_70128_L && passenger.func_180425_c().func_177954_c(this.field_70165_t, this.field_70163_u, this.field_70161_v) < 5.0) {
         passenger.func_70091_d(MoverType.SELF, 0.0, 1.0, 0.0);
      }

   }
}
