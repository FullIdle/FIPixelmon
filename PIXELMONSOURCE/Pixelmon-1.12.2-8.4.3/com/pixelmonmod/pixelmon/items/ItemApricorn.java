package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksApricornTrees;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ItemApricorn extends PixelmonItem {
   public EnumApricorns apricorn;
   private final Block block;

   public ItemApricorn(EnumApricorns apricorn) {
      super(apricorn.toString().toLowerCase() + "_apricorn");
      this.apricorn = apricorn;
      this.func_77625_d(64);
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.natural);
      switch (apricorn) {
         case Black:
            this.block = PixelmonBlocksApricornTrees.apricornTreeBlack;
            break;
         case White:
            this.block = PixelmonBlocksApricornTrees.apricornTreeWhite;
            break;
         case Pink:
            this.block = PixelmonBlocksApricornTrees.apricornTreePink;
            break;
         case Green:
            this.block = PixelmonBlocksApricornTrees.apricornTreeGreen;
            break;
         case Blue:
            this.block = PixelmonBlocksApricornTrees.apricornTreeBlue;
            break;
         case Yellow:
            this.block = PixelmonBlocksApricornTrees.apricornTreeYellow;
            break;
         case Red:
            this.block = PixelmonBlocksApricornTrees.apricornTreeRed;
            break;
         default:
            this.block = null;
      }

      this.canRepair = false;
   }

   public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (worldIn.field_72995_K) {
         return EnumActionResult.SUCCESS;
      } else if (!PixelmonConfig.allowPlanting) {
         return EnumActionResult.FAIL;
      } else {
         ItemStack stack = playerIn.func_184586_b(hand);
         IBlockState groundBlock = worldIn.func_180495_p(pos);
         if (facing == EnumFacing.UP && (groundBlock.func_185904_a() == Material.field_151577_b || groundBlock.func_185904_a() == Material.field_151578_c || groundBlock.func_177230_c() == Blocks.field_150407_cf || groundBlock.func_177230_c() == Blocks.field_150391_bh)) {
            int count = BlockHelper.countTileEntitiesOfType(worldIn, new ChunkPos(pos), TileEntityApricornTree.class);
            count += BlockHelper.countTileEntitiesOfType(worldIn, new ChunkPos(pos), TileEntityBerryTree.class);
            if (count >= PixelmonConfig.maximumPlants) {
               ChatHandler.sendChat(playerIn, "pixelmon.blocks.maxPlants");
               return EnumActionResult.FAIL;
            } else {
               pos = pos.func_177984_a();
               if (!playerIn.func_175151_a(pos, facing, stack)) {
                  return EnumActionResult.FAIL;
               } else if (stack.func_190916_E() <= 0) {
                  return EnumActionResult.FAIL;
               } else if (worldIn.func_190527_a(this.block, pos, false, facing, (Entity)null)) {
                  ApricornEvent.ApricornPlanted plantEvent = new ApricornEvent.ApricornPlanted(this.apricorn, pos, (EntityPlayerMP)playerIn);
                  if (Pixelmon.EVENT_BUS.post(plantEvent)) {
                     return EnumActionResult.FAIL;
                  } else {
                     IBlockState state = this.block.func_176223_P().func_177226_a(BlockApricornTree.BLOCKPOS, EnumBlockPos.BOTTOM);
                     worldIn.func_180501_a(pos, state, 3);
                     state = worldIn.func_180495_p(pos);
                     if (state.func_177230_c() == this.block) {
                        ItemBlock.func_179224_a(worldIn, playerIn, pos, stack);
                        state.func_177230_c().func_180633_a(worldIn, pos, state, playerIn, stack);
                     }

                     worldIn.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, SoundEvents.field_187579_bV, SoundCategory.PLAYERS, 0.5F, 1.0F);
                     if (!playerIn.func_184812_l_()) {
                        if (stack.func_190916_E() <= 1) {
                           playerIn.func_184611_a(hand, ItemStack.field_190927_a);
                        } else {
                           stack.func_190918_g(1);
                        }
                     }

                     return EnumActionResult.SUCCESS;
                  }
               } else {
                  return EnumActionResult.PASS;
               }
            }
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }
}
