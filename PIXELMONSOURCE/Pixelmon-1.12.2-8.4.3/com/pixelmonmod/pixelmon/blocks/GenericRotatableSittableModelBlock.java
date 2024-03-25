package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.entities.EntityChairMount;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GenericRotatableSittableModelBlock extends GenericRotatableModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);

   protected GenericRotatableSittableModelBlock(Material materialIn) {
      super(materialIn);
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess access, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (hand == EnumHand.OFF_HAND) {
         return false;
      } else {
         return player.func_70093_af() ? false : this.mountBlock(world, pos, player);
      }
   }

   public void func_176206_d(World worldIn, BlockPos pos, IBlockState state) {
      this.unMountBlock(worldIn, pos);
   }

   public void func_180652_a(World worldIn, BlockPos pos, Explosion explosionIn) {
      this.unMountBlock(worldIn, pos);
   }

   private void unMountBlock(World world, BlockPos pos) {
      List list = world.func_72872_a(EntityChairMount.class, new AxisAlignedBB((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), (double)(pos.func_177958_n() + 1), (double)(pos.func_177956_o() + 1), (double)(pos.func_177952_p() + 1)));
      Iterator var4 = list.iterator();
      if (var4.hasNext()) {
         Entity entity = (Entity)var4.next();
         entity.func_184226_ay();
      }
   }

   public boolean mountBlock(World world, BlockPos pos, EntityPlayer player) {
      if (world.field_72995_K) {
         return true;
      } else {
         List list = world.func_72872_a(Entity.class, new AxisAlignedBB((double)pos.func_177958_n(), (double)(pos.func_177956_o() - 1), (double)pos.func_177952_p(), (double)(pos.func_177958_n() + 1), (double)(pos.func_177956_o() + 2), (double)(pos.func_177952_p() + 1)));
         Iterator var5 = list.iterator();

         Entity entity;
         do {
            if (!var5.hasNext()) {
               EntityChairMount mount = new EntityChairMount(world, pos);
               mount.func_70107_b((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() - 0.65F + this.getSittingHeight()), (double)pos.func_177952_p() + 0.5);
               world.func_72838_d(mount);
               player.func_184220_m(mount);
               return true;
            }

            entity = (Entity)var5.next();
         } while(!(entity instanceof EntityChairMount));

         return false;
      }
   }

   public float getSittingHeight() {
      return 0.5F;
   }
}
