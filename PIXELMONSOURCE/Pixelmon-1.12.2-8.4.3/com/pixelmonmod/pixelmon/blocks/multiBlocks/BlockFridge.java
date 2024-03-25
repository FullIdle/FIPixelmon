package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFridge;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFridge extends BlockGenericModelMultiblock {
   public BlockFridge() {
      super(Material.field_151576_e, 1, 2.0, 1);
      this.func_149711_c(0.5F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149663_c("Fridge");
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K || hand == EnumHand.OFF_HAND) {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityFridge fridgeEntity = (TileEntityFridge)BlockHelper.getTileEntity(TileEntityFridge.class, world, pos);
         if (fridgeEntity != null) {
            if (fridgeEntity.isOpenDoor()) {
               if (player.func_70093_af()) {
                  fridgeEntity.closeFridge();
               } else {
                  player.func_71007_a(fridgeEntity);
               }
            } else {
               fridgeEntity.openFridge();
            }
         }
      }

      return true;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonBlocks.fridgeBlock);
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List drops = super.getDrops(world, pos, state, fortune);
      drops.add(new ItemStack(PixelmonBlocks.fridgeBlock, 1));
      return drops;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityFridge());
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityFridge fridgeEntity = (TileEntityFridge)BlockHelper.getTileEntity(TileEntityFridge.class, world, base);
      if (fridgeEntity != null) {
         InventoryHelper.func_180175_a(world, pos, fridgeEntity);
      }

      super.func_180663_b(world, pos, state);
   }
}
