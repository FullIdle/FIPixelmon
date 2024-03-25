package com.pixelmonmod.pixelmon.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class GenericHoe extends ItemHoe {
   public GenericHoe(Item.ToolMaterial material, String itemName) {
      super(material);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public String func_77658_a() {
      return super.func_77658_a().substring(5);
   }

   public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack stack = playerIn.func_184586_b(hand);
      if (!playerIn.func_175151_a(pos.func_177972_a(facing), facing, stack)) {
         return EnumActionResult.FAIL;
      } else {
         int hook = ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos);
         if (hook != 0) {
            return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
         } else {
            IBlockState iblockstate = worldIn.func_180495_p(pos);
            Block block = iblockstate.func_177230_c();
            if (facing != EnumFacing.DOWN && worldIn.func_175623_d(pos.func_177984_a())) {
               if (block == Blocks.field_150349_c) {
                  this.func_185071_a(stack, playerIn, worldIn, pos, Blocks.field_150458_ak.func_176223_P());
                  return EnumActionResult.SUCCESS;
               }

               if (block == Blocks.field_150346_d) {
                  switch ((BlockDirt.DirtType)iblockstate.func_177229_b(BlockDirt.field_176386_a)) {
                     case DIRT:
                        this.func_185071_a(stack, playerIn, worldIn, pos, Blocks.field_150458_ak.func_176223_P());
                        return EnumActionResult.SUCCESS;
                     case COARSE_DIRT:
                        this.func_185071_a(stack, playerIn, worldIn, pos, Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, DirtType.DIRT));
                        return EnumActionResult.SUCCESS;
                  }
               }
            }

            return ToolEffects.processEffect(stack, playerIn, worldIn, pos.func_177984_a()) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
         }
      }
   }
}
