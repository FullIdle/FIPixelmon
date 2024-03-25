package com.pixelmonmod.pixelmon.blocks;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.pixelmonmod.pixelmon.api.events.blocks.WarpPlateEvent;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityWarpPlate;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.concurrent.Executors;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWarpPlate extends GenericBlockContainer {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);

   public BlockWarpPlate() {
      super(Material.field_151573_f);
      this.func_149722_s();
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityWarpPlate();
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
      if (!worldIn.field_72995_K) {
         TileEntityWarpPlate warpPlate = (TileEntityWarpPlate)BlockHelper.getTileEntity(TileEntityWarpPlate.class, worldIn, pos);
         if (warpPlate != null && warpPlate.getWarpPosition() != null) {
            WarpPlateEvent event = new WarpPlateEvent(entity, warpPlate.getWarpPosition());
            BlockPos warp = event.getWarpPosition();
            worldIn.func_73046_m().field_175589_i.add(ListenableFutureTask.create(Executors.callable(() -> {
               entity.func_70634_a((double)warp.func_177958_n() + 0.5, (double)warp.func_177956_o(), (double)warp.func_177952_p() + 0.5);
            })));
         }
      }

   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }
}
