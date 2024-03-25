package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemItemFinder extends Item {
   private static final double RADIUS = 35.0;

   public ItemItemFinder() {
      this.func_77637_a(PixelmonCreativeTabs.PokeLoot);
      this.func_77655_b("item_finder");
      this.setRegistryName("item_finder");
      this.func_77625_d(1);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack itemStack = player.func_184586_b(hand);
      player.func_184811_cZ().func_185145_a(this, 20);
      if (!world.field_72995_K) {
         TileEntityPokeChest chest = (TileEntityPokeChest)BlockHelper.findClosestTileEntity(TileEntityPokeChest.class, player, 35.0, (p) -> {
            return p.getVisibility() == EnumPokechestVisibility.Hidden;
         });
         if (chest != null) {
            EnumFacing direction = this.getDirection(player, chest.func_174877_v());
            if (direction == EnumFacing.NORTH) {
               itemStack.func_77964_b(1);
            } else if (direction == EnumFacing.SOUTH) {
               itemStack.func_77964_b(2);
            } else if (direction == EnumFacing.EAST) {
               itemStack.func_77964_b(3);
            } else {
               itemStack.func_77964_b(4);
            }

            world.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u + 0.5, player.field_70161_v, SoundEvents.field_187750_dc, SoundCategory.PLAYERS, 0.5F, 1.0F);
         } else {
            itemStack.func_77964_b(0);
         }
      }

      return new ActionResult(EnumActionResult.SUCCESS, itemStack);
   }

   private EnumFacing getDirection(EntityPlayer player, BlockPos pos) {
      int x = (int)(player.field_70165_t - (double)pos.func_177958_n());
      int z = (int)(player.field_70161_v - (double)pos.func_177952_p());
      EnumFacing direction;
      if (Math.abs(x) > Math.abs(z)) {
         if (x > 0) {
            direction = EnumFacing.WEST;
         } else {
            direction = EnumFacing.EAST;
         }
      } else if (z > 0) {
         direction = EnumFacing.NORTH;
      } else {
         direction = EnumFacing.SOUTH;
      }

      EnumFacing facing = player.func_174811_aO();
      if (facing == direction) {
         return EnumFacing.NORTH;
      } else if (facing.func_176734_d() == direction) {
         return EnumFacing.SOUTH;
      } else {
         return facing.func_176746_e() == direction ? EnumFacing.EAST : EnumFacing.WEST;
      }
   }
}
