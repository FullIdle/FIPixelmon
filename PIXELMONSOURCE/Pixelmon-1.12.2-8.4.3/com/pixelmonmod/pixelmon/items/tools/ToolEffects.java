package com.pixelmonmod.pixelmon.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolEffects {
   public static boolean processEffect(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
      String toolName = stack.func_77977_a().toLowerCase();
      IBlockState state = world.func_180495_p(pos);
      Block blockAtPosition = state.func_177230_c();
      if (toolName.contains("fire")) {
         if (blockAtPosition.equals(Blocks.field_150355_j)) {
            if ((Integer)state.func_177229_b(BlockLiquid.field_176367_b) == 0) {
               world.func_175656_a(pos, Blocks.field_150343_Z.func_176223_P());
            } else {
               world.func_175656_a(pos, Blocks.field_150347_e.func_176223_P());
            }

            stack.func_77972_a(1, player);
            return true;
         }
      } else if (toolName.contains("water")) {
         if (blockAtPosition.equals(Blocks.field_150353_l)) {
            if ((Integer)state.func_177229_b(BlockLiquid.field_176367_b) == 0) {
               world.func_175656_a(pos, Blocks.field_150343_Z.func_176223_P());
            } else {
               world.func_175656_a(pos, Blocks.field_150347_e.func_176223_P());
            }

            stack.func_77972_a(1, player);
            return true;
         }
      } else if (toolName.contains("leaf")) {
         if (ItemDye.applyBonemeal(new ItemStack(stack.func_77973_b()), world, pos.func_177977_b(), player, (EnumHand)null)) {
            if (!world.field_72995_K) {
               world.func_175718_b(2005, pos.func_177977_b(), 0);
            }

            for(int i = 0; i < 12; ++i) {
               stack.func_77972_a(1, player);
            }

            return true;
         }
      } else if (toolName.contains("thunder")) {
         NBTTagList list = stack.func_77986_q();
         boolean hasSameEfficiency = false;
         int efficiency = 3;

         for(int i = 0; list.func_74745_c() > i; ++i) {
            NBTTagCompound tag = (NBTTagCompound)list.func_179238_g(i);
            if (tag.func_74765_d("id") == Enchantment.func_185258_b(Enchantments.field_185305_q)) {
               if (tag.func_74765_d("lvl") >= efficiency) {
                  hasSameEfficiency = true;
               } else {
                  list.func_74744_a(i);
               }
               break;
            }
         }

         if (!hasSameEfficiency) {
            stack.func_77966_a(Enchantments.field_185305_q, efficiency);
            stack.func_77972_a(1, player);
            return true;
         }
      } else if (toolName.contains("sun")) {
         if (Blocks.field_150346_d.func_176196_c(world, pos) && Blocks.field_150478_aa.func_176196_c(world, pos)) {
            world.func_175656_a(pos, Blocks.field_150478_aa.func_176223_P());
            stack.func_77972_a(1, player);
            return true;
         }
      } else {
         if (toolName.contains("moon")) {
            player.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 6000, 0, true, true));
            stack.func_77972_a(1, player);
            return true;
         }

         if (toolName.contains("dawn")) {
            player.func_70690_d(new PotionEffect(MobEffects.field_180152_w, 6000, 0, true, true));
            stack.func_77972_a(1, player);
            return true;
         }

         if (toolName.contains("dusk")) {
            player.func_70690_d(new PotionEffect(MobEffects.field_76441_p, 6000, 0, true, true));
            stack.func_77972_a(1, player);
            return true;
         }
      }

      return false;
   }
}
