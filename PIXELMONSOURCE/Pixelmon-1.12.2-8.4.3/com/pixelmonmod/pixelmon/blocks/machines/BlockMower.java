package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileMower;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMower extends BlockMachine {
   public BlockMower() {
      super("mower");
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
      TileMower mower = (TileMower)world.func_175625_s(pos);
      player.func_71007_a(mower);
      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileMower();
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      TileEntity tileEntity = world.func_175625_s(pos);
      if (tileEntity instanceof TileMower) {
         TileMower mower = (TileMower)tileEntity;

         for(int i = 0; i < mower.func_70302_i_(); ++i) {
            EntityItem item = new EntityItem(world);
            item.func_92058_a(mower.func_70301_a(i));
            item.func_70107_b((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5);
            world.func_72838_d(item);
         }
      }

      super.func_180663_b(world, pos, state);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
