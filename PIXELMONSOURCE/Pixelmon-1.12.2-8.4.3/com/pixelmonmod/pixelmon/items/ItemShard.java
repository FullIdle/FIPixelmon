package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityOrb;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShard extends PixelmonItem {
   public final EnumOrbShard shardType;
   public final Block block;

   public ItemShard(EnumOrbShard shardType) {
      super(shardType.name().toLowerCase() + "_shard");
      this.shardType = shardType;
      this.func_77637_a(PixelmonCreativeTabs.natural);
      switch (shardType) {
         case RED:
            this.block = PixelmonBlocks.redOrb;
            break;
         case BLUE:
            this.block = PixelmonBlocks.blueOrb;
            break;
         default:
            this.block = Blocks.field_150350_a;
      }

   }

   public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && this.block != Blocks.field_150350_a) {
         ItemStack stack = player.func_184586_b(hand);
         Block groundBlock = world.func_180495_p(pos).func_177230_c();
         if (groundBlock == this.block) {
            try {
               TileEntityOrb tile = (TileEntityOrb)world.func_175625_s(pos);
               if (tile.getPieces() <= 9 && player.func_175151_a(pos, facing, stack)) {
                  tile.addPiece();
                  world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187567_bP, SoundCategory.BLOCKS, 1.0F, 10.0F);
                  if (tile.getPieces() > 9) {
                     world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187649_bu, SoundCategory.BLOCKS, 1.0F, 2.0F);
                  }

                  if (!player.func_184812_l_()) {
                     stack.func_190918_g(1);
                  }

                  return EnumActionResult.SUCCESS;
               } else {
                  return EnumActionResult.FAIL;
               }
            } catch (ClassCastException var12) {
               var12.printStackTrace();
               world.func_175698_g(pos);
               return EnumActionResult.PASS;
            }
         } else if (facing == EnumFacing.UP && world.func_180495_p(pos.func_177984_a()).func_185904_a().func_76222_j() && player.func_175151_a(pos.func_177984_a(), facing, stack)) {
            world.func_175656_a(pos.func_177984_a(), this.block.func_176223_P().func_177226_a(BlockProperties.FACING, player.func_184172_bi()));
            if (!player.func_184812_l_()) {
               stack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.FAIL;
         }
      } else {
         return EnumActionResult.SUCCESS;
      }
   }
}
