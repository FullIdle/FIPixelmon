package com.pixelmonmod.pixelmon.items.tools;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import com.pixelmonmod.pixelmon.items.ItemHammer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericHammer extends ItemHammer {
   public GenericHammer(Item.ToolMaterial material, String itemName) {
      super(material, itemName);
   }

   public String func_77658_a() {
      return super.func_77658_a().substring(5);
   }

   public float func_150893_a(ItemStack stack, IBlockState state) {
      if (state.func_177230_c() == PixelmonBlocks.anvil) {
         if (this.field_77862_b != PixelmonItemsTools.RUBY && this.field_77862_b != PixelmonItemsTools.SAPPHIRE && this.field_77862_b != PixelmonItemsTools.AMETHYST && this.field_77862_b != PixelmonItemsTools.CRYSTAL && this.field_77862_b != PixelmonItemsTools.LEAFSTONE) {
            if (this.field_77862_b != PixelmonItemsTools.THUNDERSTONE && this.field_77862_b != PixelmonItemsTools.SUNSTONE && this.field_77862_b != PixelmonItemsTools.MOONSTONE && this.field_77862_b != PixelmonItemsTools.DAWNSTONE && this.field_77862_b != PixelmonItemsTools.FIRESTONE && this.field_77862_b != PixelmonItemsTools.WATERSTONE && this.field_77862_b != PixelmonItemsTools.DUSKSTONE) {
               return 1.0F;
            } else {
               return 6.0F;
            }
         } else {
            return 4.0F;
         }
      } else {
         return 1.0F;
      }
   }

   public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      return ToolEffects.processEffect(playerIn.func_184586_b(hand), playerIn, worldIn, pos.func_177984_a()) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
   }
}
