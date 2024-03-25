package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.enums.EnumBike;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBike extends Item {
   private EnumBike type;

   public ItemBike(EnumBike type) {
      this.func_77637_a(CreativeTabs.field_78029_e);
      this.func_77655_b(type.name().toLowerCase() + "_bike");
      this.setRegistryName(type.name().toLowerCase() + "_bike");
      this.type = type;
      this.func_77625_d(1);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      ItemStack itemstack = player.func_184586_b(hand);
      if (!world.field_72995_K) {
         double x = (double)pos.func_177958_n();
         double y = (double)pos.func_177956_o();
         double z = (double)pos.func_177952_p();
         if (side == EnumFacing.UP) {
            x += (double)hitX;
            ++y;
            z += (double)hitZ;
         } else if (side == EnumFacing.DOWN) {
            x += (double)hitX;
            --y;
            z += (double)hitZ;
            if (!world.func_175623_d(pos.func_177967_a(EnumFacing.DOWN, 2))) {
               return EnumActionResult.FAIL;
            }
         } else {
            x += (double)side.func_82601_c() + 0.5;
            y += (double)hitY;
            z += (double)side.func_82599_e() + 0.5;
         }

         if (!world.func_175623_d(pos.func_177972_a(side)) || world.func_72872_a(EntityBike.class, (new AxisAlignedBB(new BlockPos(x, y, z))).func_72321_a(-0.5, -0.5, -0.5)).size() > 0) {
            return EnumActionResult.FAIL;
         }

         EntityBike bike = new EntityBike(world);
         bike.setType(this.type);
         if (itemstack.func_77978_p() != null && itemstack.func_77978_p().func_74764_b("color")) {
            bike.setColor(EnumDyeColor.values()[itemstack.func_77978_p().func_74771_c("color")]);
         }

         bike.func_70080_a(x, y, z, player.field_70177_z, player.field_70125_A);
         world.func_72838_d(bike);
         if (!player.func_184812_l_()) {
            player.func_184611_a(hand, ItemStack.field_190927_a);
         }

         world.func_184133_a((EntityPlayer)null, player.func_180425_c(), SoundEvents.field_187604_bf, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }

      return EnumActionResult.SUCCESS;
   }

   public EnumRarity func_77613_e(ItemStack par1ItemStack) {
      return EnumRarity.RARE;
   }
}
