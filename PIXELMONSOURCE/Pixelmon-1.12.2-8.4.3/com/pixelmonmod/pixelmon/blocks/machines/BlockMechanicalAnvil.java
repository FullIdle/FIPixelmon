package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import com.pixelmonmod.pixelmon.enums.EnumGuiContainer;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMechanicalAnvil extends MultiBlock {
   public BlockMechanicalAnvil() {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149711_c(2.5F);
      this.func_149663_c("mechanicalanvil");
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      TileEntityMechanicalAnvil mechanicalAnvil = (TileEntityMechanicalAnvil)BlockHelper.getTileEntity(TileEntityMechanicalAnvil.class, world, pos);
      return mechanicalAnvil != null && mechanicalAnvil.isRunning() ? 15 : 0;
   }

   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      TileEntityMechanicalAnvil mechanicalAnvil = (TileEntityMechanicalAnvil)BlockHelper.getTileEntity(TileEntityMechanicalAnvil.class, world, pos);
      if (mechanicalAnvil != null && mechanicalAnvil.isRunning()) {
         EnumFacing enumfacing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         double d0 = (double)pos.func_177958_n() + 0.5;
         double d1 = (double)pos.func_177956_o() + rand.nextDouble() * 6.0 / 16.0;
         double d2 = (double)pos.func_177952_p() + 0.5;
         double d3 = 0.52;
         double d4 = rand.nextDouble() * 0.6 - 0.3;
         switch (enumfacing) {
            case EAST:
               world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
               world.func_175688_a(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
               break;
            case WEST:
               world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
               world.func_175688_a(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
               break;
            case SOUTH:
               world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0, 0.0, 0.0, new int[0]);
               world.func_175688_a(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0, 0.0, 0.0, new int[0]);
               break;
            case NORTH:
               world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0, 0.0, 0.0, new int[0]);
               world.func_175688_a(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0, 0.0, 0.0, new int[0]);
         }
      }

   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityMechanicalAnvil mechanicalAnvil = (TileEntityMechanicalAnvil)BlockHelper.getTileEntity(TileEntityMechanicalAnvil.class, world, base);
      if (mechanicalAnvil != null) {
         InventoryHelper.func_180175_a(world, base, mechanicalAnvil);
      }

      world.func_175666_e(base, this);
      super.func_180663_b(world, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityMechanicalAnvil mechanicalAnvil = (TileEntityMechanicalAnvil)BlockHelper.getTileEntity(TileEntityMechanicalAnvil.class, world, pos);
         if (mechanicalAnvil != null) {
            player.openGui(Pixelmon.instance, EnumGuiContainer.MechanicalAnvil.getIndex(), world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
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
      return Optional.of(new TileEntityMechanicalAnvil());
   }
}
