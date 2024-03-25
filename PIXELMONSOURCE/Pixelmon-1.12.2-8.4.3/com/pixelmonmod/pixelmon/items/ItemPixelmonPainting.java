package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.custom.EntityPixelmonPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPixelmonPainting extends Item {
   public ItemPixelmonPainting(String itemName) {
      this.func_77637_a(PixelmonCreativeTabs.decoration);
      this.func_77655_b("pixelmon_painting");
      this.setRegistryName(itemName);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!worldIn.field_72995_K) {
         if (side == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
         } else if (side == EnumFacing.UP) {
            return EnumActionResult.FAIL;
         } else {
            BlockPos blockpos1 = pos.func_177972_a(side);
            ItemStack stack = player.func_184586_b(hand);
            if (!player.func_175151_a(blockpos1, side, stack)) {
               return EnumActionResult.FAIL;
            } else {
               EntityPixelmonPainting entityhanging = this.createEntity(worldIn, blockpos1, side);
               if (entityhanging.func_70518_d()) {
                  worldIn.func_72838_d(entityhanging);
                  stack.func_77979_a(1);
               }

               return EnumActionResult.SUCCESS;
            }
         }
      } else {
         return EnumActionResult.PASS;
      }
   }

   private EntityPixelmonPainting createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
      return new EntityPixelmonPainting(worldIn, pos, clickedSide);
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
