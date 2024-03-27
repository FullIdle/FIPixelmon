package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLaggingTail extends ItemHeld {
   public ItemLaggingTail(EnumHeldItems itemType, String itemName) {
      super(itemType, itemName);
   }

   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      return priority - 0.2F;
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (this != PixelmonItemsHeld.fullIncense) {
         return super.func_180614_a(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
      } else {
         IBlockState iblockstate = worldIn.func_180495_p(pos);
         Block block = iblockstate.func_177230_c();
         if (!block.func_176200_f(worldIn, pos)) {
            pos = pos.func_177972_a(facing);
         }

         ItemStack itemstack = player.func_184586_b(hand);
         if (!itemstack.func_190926_b() && player.func_175151_a(pos, facing, itemstack) && worldIn.func_190527_a(this.getBlock(), pos, false, facing, player)) {
            int i = this.func_77647_b(itemstack.func_77960_j());
            IBlockState iblockstate1 = this.getBlock().getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
            if (this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
               iblockstate1 = worldIn.func_180495_p(pos);
               SoundType soundtype = iblockstate1.func_177230_c().getSoundType(iblockstate1, worldIn, pos, player);
               worldIn.func_184133_a(player, pos, soundtype.func_185841_e(), SoundCategory.BLOCKS, (soundtype.func_185843_a() + 1.0F) / 2.0F, soundtype.func_185847_b() * 0.8F);
               itemstack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
      if (!world.func_180501_a(pos, newState, 11)) {
         return false;
      } else {
         IBlockState state = world.func_180495_p(pos);
         if (state.func_177230_c() == this.getBlock()) {
            this.getBlock().func_180633_a(world, pos, state, player, stack);
            if (player instanceof EntityPlayerMP) {
               CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)player, pos, stack);
            }
         }

         return true;
      }
   }

   public Block getBlock() {
      return PixelmonBlocks.getBlockFromItem(this);
   }
}