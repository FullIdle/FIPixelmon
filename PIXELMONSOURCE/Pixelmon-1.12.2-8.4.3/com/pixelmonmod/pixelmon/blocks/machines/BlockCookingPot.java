package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import com.pixelmonmod.pixelmon.enums.EnumGuiContainer;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCookingPot extends MultiBlock {
   public BlockCookingPot() {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149711_c(2.5F);
      this.func_149663_c("cookingpot");
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityCookingPot cookingPot = (TileEntityCookingPot)BlockHelper.getTileEntity(TileEntityCookingPot.class, world, base);
      if (cookingPot != null) {
         InventoryHelper.func_180175_a(world, base, cookingPot);
      }

      world.func_175666_e(base, this);
      super.func_180663_b(world, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityCookingPot cookingPot = (TileEntityCookingPot)BlockHelper.getTileEntity(TileEntityCookingPot.class, world, pos);
         if (cookingPot != null) {
            if (!cookingPot.isCooking()) {
               if (player.func_184586_b(hand).func_77973_b() instanceof ItemFlintAndSteel && cookingPot.canStart()) {
                  cookingPot.startCooking((EntityPlayerMP)player);
               } else {
                  player.openGui(Pixelmon.instance, EnumGuiContainer.CookingPot.getIndex(), world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
               }
            } else {
               cookingPot.processCookingInteract();
            }
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
      return Optional.of(new TileEntityCookingPot());
   }

   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
   }
}
