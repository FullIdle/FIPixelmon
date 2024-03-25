package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.TempCategory;

public class ItemGracidea extends PixelmonItem {
   public ItemGracidea() {
      super("gracidea");
      this.func_77637_a(PixelmonCreativeTabs.natural);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         IBlockState targetBlock = world.func_180495_p(pos);
         ItemStack heldStack = player.func_184586_b(hand);
         pos = pos.func_177982_a(0, 1, 0);
         if (facing == EnumFacing.UP && targetBlock.func_177230_c() == Blocks.field_150349_c && world.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a && world.func_175678_i(pos) && world.func_180494_b(pos).func_150561_m() == TempCategory.MEDIUM) {
            IBlockState state = PixelmonBlocks.gracideaBlock.func_176223_P();
            world.func_175656_a(pos, state);
            state.func_177230_c().func_180633_a(world, pos, state, player, heldStack);
            heldStack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.PASS;
         }
      } else {
         return EnumActionResult.PASS;
      }
   }
}
