package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IArmorEffect {
   void onArmorTick(World var1, EntityPlayer var2, ItemStack var3, GenericArmor var4);

   static boolean isWearingFullSet(EntityPlayer player, ItemArmor.ArmorMaterial material) {
      ItemStack legs = player.func_184582_a(EntityEquipmentSlot.LEGS);
      ItemStack chest = player.func_184582_a(EntityEquipmentSlot.CHEST);
      ItemStack head = player.func_184582_a(EntityEquipmentSlot.HEAD);
      ItemStack feet = player.func_184582_a(EntityEquipmentSlot.FEET);
      if (!legs.func_190926_b() && legs.func_77973_b() instanceof GenericArmor && ((GenericArmor)legs.func_77973_b()).field_77878_bZ == material && !chest.func_190926_b() && chest.func_77973_b() instanceof GenericArmor && ((GenericArmor)chest.func_77973_b()).field_77878_bZ == material && !head.func_190926_b() && head.func_77973_b() instanceof GenericArmor && ((GenericArmor)head.func_77973_b()).field_77878_bZ == material) {
         return !feet.func_190926_b() && feet.func_77973_b() instanceof GenericArmor && ((GenericArmor)feet.func_77973_b()).field_77878_bZ == material;
      } else {
         return false;
      }
   }
}
