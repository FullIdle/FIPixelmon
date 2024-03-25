package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import net.minecraft.item.ItemStack;

public interface IItemAttributeModifier {
   Multimap getAttributeModifiers(ItemStack var1, GenericArmor var2);
}
