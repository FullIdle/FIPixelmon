package com.pixelmonmod.tcg.block;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.gui.enums.EnumGui;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPrinter extends GenericRotatableModelBlock {
   public BlockPrinter() {
      super(Material.field_151573_f);
      this.func_149663_c(getName());
      this.func_149711_c(1.5F);
      this.func_149752_b(10.0F);
      this.setRegistryName("tcg", getName());
      this.func_149647_a(TCG.tabTCG);
   }

   public static String getName() {
      return "card_printer";
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return TCG.itemPrinter;
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityPrinter();
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(TCG.itemPrinter);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
      return false;
   }

   public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K) {
         TileEntityPrinter tileEntityPrinter = (TileEntityPrinter)world.func_175625_s(pos);
         if (tileEntityPrinter != null) {
            player.openGui(TCG.instance, EnumGui.Printer.getIndex(), world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         }
      }

      return true;
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      TileEntityPrinter printer = (TileEntityPrinter)BlockHelper.getTileEntity(TileEntityPrinter.class, world, pos);
      if (printer != null) {
         InventoryHelper.func_180175_a(world, pos, printer);
      }

      world.func_175666_e(pos, this);
      super.func_180663_b(world, pos, state);
   }
}
