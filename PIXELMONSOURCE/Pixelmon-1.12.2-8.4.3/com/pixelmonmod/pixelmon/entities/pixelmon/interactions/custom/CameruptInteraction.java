package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class CameruptInteraction extends PixelmonInteraction {
   public CameruptInteraction() {
      super(3);
   }

   public boolean processInteract(Entity1Base pixelmon, EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (stack != null && stack.func_77973_b() == Items.field_151133_ar) {
         if (!player.field_70170_p.field_72995_K) {
            stack.func_190918_g(1);
            if (stack.func_190916_E() <= 0) {
               player.field_71071_by.func_70299_a(player.field_71071_by.field_70461_c, new ItemStack(Items.field_151129_at));
            } else {
               DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(Items.field_151129_at), false);
            }
         }

         return super.processInteract(pixelmon, player, hand, stack);
      } else {
         return false;
      }
   }

   public void resetCounter(Entity1Base pixelmon) {
      if (!pixelmon.field_70170_p.field_72995_K) {
         this.counter = pixelmon.func_70681_au().nextInt(600) + 6000;
      }

   }
}