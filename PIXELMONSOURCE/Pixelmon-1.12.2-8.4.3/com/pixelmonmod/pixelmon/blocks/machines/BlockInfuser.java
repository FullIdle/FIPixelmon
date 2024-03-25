package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import com.pixelmonmod.pixelmon.enums.EnumGuiContainer;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInfuser extends MultiBlock {
   public BlockInfuser() {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149711_c(2.5F);
      this.func_149663_c("infuser");
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      TileEntityInfuser infuser = (TileEntityInfuser)BlockHelper.getTileEntity(TileEntityInfuser.class, world, pos);
      return infuser != null && infuser.isRunning() ? 15 : 0;
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityInfuser infuser = (TileEntityInfuser)BlockHelper.getTileEntity(TileEntityInfuser.class, world, base);
      if (infuser != null) {
         InventoryHelper.func_180175_a(world, base, infuser);
      }

      world.func_175666_e(base, this);
      super.func_180663_b(world, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityInfuser infuser = (TileEntityInfuser)BlockHelper.getTileEntity(TileEntityInfuser.class, world, pos);
         if (infuser != null) {
            player.openGui(Pixelmon.instance, EnumGuiContainer.Infuser.getIndex(), world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         }

         return true;
      } else {
         return true;
      }
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityInfuser());
   }
}
