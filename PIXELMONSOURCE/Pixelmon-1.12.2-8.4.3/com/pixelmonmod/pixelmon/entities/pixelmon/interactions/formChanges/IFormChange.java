package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public interface IFormChange {
   boolean isValidItem(ItemStack var1);

   boolean isValidPokemon(EntityPixelmon var1);

   boolean execute(EntityPixelmon var1, ItemStack var2, EntityPlayerMP var3);
}
