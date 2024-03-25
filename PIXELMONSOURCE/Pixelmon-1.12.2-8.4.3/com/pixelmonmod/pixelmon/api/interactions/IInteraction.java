package com.pixelmonmod.pixelmon.api.interactions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public interface IInteraction {
   boolean processInteract(EntityPixelmon var1, EntityPlayer var2, EnumHand var3, ItemStack var4);

   default boolean processOnEmptyHand(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      return false;
   }
}
