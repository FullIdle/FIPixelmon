package com.pixelmonmod.pixelmon.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import java.util.UUID;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemPixelmonBoots extends ItemArmor {
   private static UUID runningShoesUUID = UUID.fromString("B7060ADF-8FAF-4C0F-B816-87CB5721979F");
   private static AttributeModifier oldRunningShoesModifier;
   private static AttributeModifier newRunningShoesModifier;

   public ItemPixelmonBoots(ItemArmor.ArmorMaterial enumArmorMaterial, int k, EntityEquipmentSlot l, String itemName) {
      super(enumArmorMaterial, k, l);
      this.func_77656_e(1000);
      this.func_77637_a(CreativeTabs.field_78037_j);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public Multimap getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
      Multimap o = HashMultimap.create();
      if (slot == EntityEquipmentSlot.FEET) {
         AttributeModifier modifier = this == PixelmonItemsTools.oldRunningShoes ? oldRunningShoesModifier : newRunningShoesModifier;
         o.put(SharedMonsterAttributes.field_111263_d.func_111108_a(), modifier);
      }

      return o;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
      if (!world.field_72995_K && itemStack != null && player.func_184582_a(EntityEquipmentSlot.FEET) == itemStack) {
         Item item = itemStack.func_77973_b();
         if (item == PixelmonItemsTools.newRunningShoes) {
            if (!itemStack.func_77984_f()) {
               if (itemStack.func_77951_h()) {
                  itemStack.func_77964_b(0);
               }

               return;
            }

            if (player.func_184812_l_() || player.func_175149_v() || player.func_184218_aH() || player.func_184613_cA()) {
               return;
            }

            int maxDamage = PixelmonItemsTools.newRunningShoes.getMaxDamage(itemStack);
            if (itemStack.func_77952_i() >= maxDamage) {
               player.func_184201_a(EntityEquipmentSlot.FEET, new ItemStack(PixelmonItemsTools.oldRunningShoes));
            } else {
               double currentX = player.field_70165_t;
               double currentZ = player.field_70161_v;
               NBTTagCompound compound = itemStack.func_190925_c("pos");
               if (!compound.func_74764_b("x")) {
                  compound.func_74780_a("x", currentX);
                  compound.func_74780_a("z", currentZ);
                  return;
               }

               double bootLastX = compound.func_74769_h("x");
               double bootLastZ = compound.func_74769_h("z");
               double changeX = Math.abs(bootLastX - currentX);
               double changeZ = Math.abs(bootLastZ - currentZ);
               if (changeX >= 2.0 || changeZ >= 2.0) {
                  itemStack.func_77964_b(itemStack.func_77952_i() + 1);
                  compound.func_74780_a("x", currentX);
                  compound.func_74780_a("z", currentZ);
               }
            }
         } else if (item == PixelmonItemsTools.oldRunningShoes && itemStack.func_77951_h()) {
            itemStack.func_77964_b(0);
         }
      }

   }

   public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
      return stack.func_77973_b() == PixelmonItemsTools.newRunningShoes ? "pixelmon:textures/models/armor/running_1.png" : "pixelmon:textures/models/armor/oldrunning_1.png";
   }

   static {
      oldRunningShoesModifier = new AttributeModifier(runningShoesUUID, SharedMonsterAttributes.field_111263_d.func_111108_a(), 0.5, 1);
      newRunningShoesModifier = new AttributeModifier(runningShoesUUID, SharedMonsterAttributes.field_111263_d.func_111108_a(), 0.75, 1);
   }
}
