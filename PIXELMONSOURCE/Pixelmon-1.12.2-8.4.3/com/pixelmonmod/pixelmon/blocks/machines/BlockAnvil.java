package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.AnvilEvent;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityAnvil;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonOres;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemPokeballDisc;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockAnvil extends GenericRotatableModelBlock {
   public BlockAnvil() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149752_b(2000.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("anvil");
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.20000000298023224, 0.0, 0.20000000298023224, 0.800000011920929, 0.699999988079071, 0.800000011920929);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      this.func_176206_d(world, pos, world.func_180495_p(pos));
      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public void func_176206_d(World world, BlockPos pos, IBlockState state) {
      if (!world.field_72995_K) {
         TileEntityAnvil anvil = (TileEntityAnvil)BlockHelper.getTileEntity(TileEntityAnvil.class, world, pos);
         if (anvil != null && !anvil.stack.func_190926_b()) {
            EntityItem var3 = new EntityItem(world, (double)pos.func_177958_n(), (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p(), anvil.stack);
            var3.func_174867_a(10);
            world.func_72838_d(var3);
         }
      }

      super.func_176206_d(world, pos, state);
   }

   public int func_149745_a(Random random) {
      return 1;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return false;
      } else {
         TileEntityAnvil anvil = (TileEntityAnvil)BlockHelper.getTileEntity(TileEntityAnvil.class, world, pos);
         if (anvil == null) {
            return false;
         } else if (!anvil.stack.func_190926_b() && hand == EnumHand.MAIN_HAND) {
            AnvilEvent.MaterialChanged materialChangedEvent = new AnvilEvent.MaterialChanged((EntityPlayerMP)player, anvil, anvil.stack, true);
            if (Pixelmon.EVENT_BUS.post(materialChangedEvent)) {
               return false;
            } else {
               anvil.stack = ItemStack.field_190927_a;
               anvil.state = 0;
               player.field_70170_p.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187883_gR, SoundCategory.BLOCKS, 0.1F, 3.0F);
               DropItemHelper.giveItemStack((EntityPlayerMP)player, materialChangedEvent.material, false);
               ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(pos);
               return true;
            }
         } else {
            ItemStack heldStack = player.func_184586_b(hand);
            if (anvil.stack.func_190926_b() && !heldStack.func_190926_b()) {
               Item heldItem = heldStack.func_77973_b();
               if (heldItem instanceof ItemPokeballDisc || heldItem == PixelmonItemsPokeballs.ironDisc || heldItem == PixelmonItemsPokeballs.aluDisc || PixelmonOres.itemMatches(heldStack, "ingotAluminum")) {
                  AnvilEvent.MaterialChanged materialChangedEvent = new AnvilEvent.MaterialChanged((EntityPlayerMP)player, anvil, heldStack, false);
                  if (Pixelmon.EVENT_BUS.post(materialChangedEvent)) {
                     return false;
                  }

                  anvil.stack = heldStack.func_77979_a(1);
                  world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187689_f, SoundCategory.BLOCKS, 0.15F, 1.0F);
                  ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(pos);
                  return true;
               }
            }

            return false;
         }
      }
   }

   public TileEntity func_149915_a(World var1, int var2) {
      return new TileEntityAnvil();
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(PixelmonBlocks.anvil);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(PixelmonBlocks.anvil));
   }
}
