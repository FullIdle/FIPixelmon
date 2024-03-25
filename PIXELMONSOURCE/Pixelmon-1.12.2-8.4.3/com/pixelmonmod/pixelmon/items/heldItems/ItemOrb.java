package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityOrb;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemOrb extends ItemHeld {
   public final EnumOrbShard shard;
   private final Block block;

   public ItemOrb(EnumOrbShard shard) {
      super(EnumHeldItems.redBlueJadeOrb, shard.name().toLowerCase() + "_orb");
      this.shard = shard;
      switch (shard) {
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
      if (world.field_72995_K) {
         return EnumActionResult.SUCCESS;
      } else if (facing != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else if (!world.func_180495_p(pos.func_177984_a()).func_185904_a().func_76222_j()) {
         return EnumActionResult.FAIL;
      } else {
         world.func_175656_a(pos.func_177984_a(), this.block.func_176223_P().func_177226_a(BlockProperties.FACING, player.func_184172_bi()));
         ((TileEntityOrb)world.func_175625_s(pos.func_177984_a())).setPieces(10);
         player.func_184586_b(hand).func_190918_g(1);
         return EnumActionResult.SUCCESS;
      }
   }
}
