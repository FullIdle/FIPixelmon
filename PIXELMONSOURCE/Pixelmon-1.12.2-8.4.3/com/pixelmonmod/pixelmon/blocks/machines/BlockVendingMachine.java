package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityVendingMachine;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.SetVendingMachineData;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import java.util.Optional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockVendingMachine extends MultiBlock {
   private final ColorEnum color;
   VendingMachineShop shop;

   public BlockVendingMachine(ColorEnum color) {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149711_c(2.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c(color + "vendingmachine");
      this.color = color;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityVendingMachine());
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this.getDroppedItem(world, pos));
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         Pixelmon.network.sendTo(new SetVendingMachineData(this.getShop().getItems()), (EntityPlayerMP)player);
         OpenScreen.open(player, EnumGuiScreen.VendingMachine, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      } else {
         return true;
      }
   }

   public VendingMachineShop getShop() {
      if (this.shop == null) {
         this.shop = new VendingMachineShop();
      }

      return this.shop;
   }

   public ColorEnum getColor() {
      return this.color;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }
}
