package com.pixelmonmod.pixelmon.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IEquippable {
   EntityEquipmentSlot getEquipmentSlot(ItemStack var1);

   boolean isValidArmor(ItemStack var1, EntityEquipmentSlot var2, Entity var3);

   String getEquippableModelKey();

   ResourceLocation getEquippableTexture();

   Item getEquippableItem();
}
