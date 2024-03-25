package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemWailmerPail extends Item {
   public ItemWailmerPail(String itemName) {
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
      this.func_77656_e(32);
      this.canRepair = false;
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!player.func_175151_a(pos, side, player.func_184586_b(hand))) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState state = world.func_180495_p(pos);
         IGrowable tree;
         if (state.func_177230_c() instanceof BlockApricornTree) {
            tree = (IGrowable)state.func_177230_c();
            if (tree.func_176473_a(world, pos, state, world.field_72995_K)) {
               BlockPos loc = pos;
               if (state.func_177229_b(BlockApricornTree.BLOCKPOS) == EnumBlockPos.TOP) {
                  loc = pos.func_177977_b();
               }

               TileEntityApricornTree apricornTree = (TileEntityApricornTree)world.func_175625_s(loc);
               if (apricornTree != null) {
                  ApricornEvent.ApricornWatered apricornWatered = new ApricornEvent.ApricornWatered(apricornTree.tree.apricorn, loc, player, apricornTree);
                  if (!Pixelmon.EVENT_BUS.post(apricornWatered)) {
                     if (!apricornTree.wasWateredToday()) {
                        apricornTree.updateWatering();
                        tree.func_176474_b(world, world.field_73012_v, pos, state);
                        spawnBonemealParticles(world, pos, 15);
                     } else {
                        ChatHandler.sendChat(player, "pixelmon.blocks.bonemealwatered");
                     }
                  }
               }
            }

            return EnumActionResult.SUCCESS;
         } else if (!(state.func_177230_c() instanceof BlockBerryTree)) {
            return EnumActionResult.PASS;
         } else {
            tree = (IGrowable)state.func_177230_c();
            BerryEvent.BerryWatered berryWatered = new BerryEvent.BerryWatered(pos, player, ((BlockBerryTree)state.func_177230_c()).getType());
            if (tree.func_176473_a(world, pos, state, world.field_72995_K) && !Pixelmon.EVENT_BUS.post(berryWatered)) {
               tree.func_176474_b(world, world.field_73012_v, pos, state);
               spawnBonemealParticles(world, pos, 15);
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.bonemealwatered");
            }

            return EnumActionResult.SUCCESS;
         }
      }
   }

   public static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount) {
      if (!worldIn.field_72995_K) {
         WorldServer world = (WorldServer)worldIn;
         if (amount == 0) {
            amount = 15;
         }

         IBlockState iblockstate = worldIn.func_180495_p(pos);
         int i;
         double d0;
         double d1;
         double d2;
         if (iblockstate.func_185904_a() != Material.field_151579_a) {
            for(i = 0; i < amount; ++i) {
               d0 = field_77697_d.nextGaussian() * 0.02;
               d1 = field_77697_d.nextGaussian() * 0.02;
               d2 = field_77697_d.nextGaussian() * 0.02;
               world.func_180505_a(EnumParticleTypes.VILLAGER_HAPPY, false, (double)((float)pos.func_177958_n() + field_77697_d.nextFloat()), (double)pos.func_177956_o() + (double)field_77697_d.nextFloat() * iblockstate.func_185900_c(worldIn, pos).field_72337_e, (double)((float)pos.func_177952_p() + field_77697_d.nextFloat()), 1, d0, d1, d2, 0.0, new int[0]);
            }
         } else {
            for(i = 0; i < amount; ++i) {
               d0 = field_77697_d.nextGaussian() * 0.02;
               d1 = field_77697_d.nextGaussian() * 0.02;
               d2 = field_77697_d.nextGaussian() * 0.02;
               world.func_180505_a(EnumParticleTypes.VILLAGER_HAPPY, false, (double)((float)pos.func_177958_n() + field_77697_d.nextFloat()), (double)pos.func_177956_o() + (double)field_77697_d.nextFloat() * 1.0, (double)((float)pos.func_177952_p() + field_77697_d.nextFloat()), 1, d0, d1, d2, 0.0, new int[0]);
            }
         }
      }

   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
