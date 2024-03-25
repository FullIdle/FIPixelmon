package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.FossilCleanerEvent;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilCleaner;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFossilCleaner extends MultiBlock {
   public BlockFossilCleaner() {
      super(Material.field_151573_f, 1, 1.0, 1);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c("fossilcleaner");
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
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
      return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityFossilCleaner());
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityFossilCleaner fossilCleaner = (TileEntityFossilCleaner)BlockHelper.getTileEntity(TileEntityFossilCleaner.class, world, pos);
         if (fossilCleaner != null) {
            if (!fossilCleaner.func_191420_l()) {
               Item item = fossilCleaner.getItemInCleaner();
               FossilCleanerEvent.ObtainingCleanFossil event = new FossilCleanerEvent.ObtainingCleanFossil((EntityPlayerMP)player, fossilCleaner, item);
               if (!Pixelmon.EVENT_BUS.post(event)) {
                  fossilCleaner.setItemInCleaner((Item)null);
                  DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(event.getFossil()), false);
               }
            } else {
               ItemStack heldItem = player.func_184586_b(hand);
               if (!heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemCoveredFossil) {
                  FossilCleanerEvent.PutFossil event = new FossilCleanerEvent.PutFossil((EntityPlayerMP)player, fossilCleaner, heldItem.func_77973_b());
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     fossilCleaner.setItemInCleaner(event.getFossil());
                     heldItem.func_190918_g(1);
                     if (heldItem.func_190916_E() <= 0) {
                        player.func_184611_a(hand, ItemStack.field_190927_a);
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if (!player.field_71075_bZ.field_75098_d) {
         this.func_176206_d(world, pos, world.func_180495_p(pos));
      }

      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public void func_176206_d(World world, BlockPos pos, IBlockState state) {
      if (!world.field_72995_K) {
         TileEntityFossilCleaner fossilCleaner = (TileEntityFossilCleaner)BlockHelper.getTileEntity(TileEntityFossilCleaner.class, world, pos);
         if (fossilCleaner != null && fossilCleaner.getItemInCleaner() != null) {
            EntityItem entityItem = new EntityItem(world, (double)pos.func_177958_n(), (double)pos.func_177956_o() + 1.3, (double)pos.func_177952_p(), new ItemStack(fossilCleaner.getItemInCleaner()));
            entityItem.func_174867_a(10);
            world.func_72838_d(entityItem);
         }
      }

      super.func_176206_d(world, pos, state);
   }

   public int func_149738_a(World world) {
      return 2;
   }

   public int func_180656_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      TileEntityFossilCleaner tile = (TileEntityFossilCleaner)BlockHelper.getTileEntity(TileEntityFossilCleaner.class, blockAccess, pos);
      if (tile != null && tile.isOn()) {
         return tile.timer == 360 ? 15 : tile.timer / 24 + 1;
      } else {
         return 0;
      }
   }

   public boolean func_149744_f(IBlockState state) {
      return true;
   }
}
