package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.items.ItemPixelmonBoots;
import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public class SpeedModifier implements IItemAttributeModifier {
   private static final UUID elementalBootsUUID = UUID.fromString("10ae6bcc-5b15-41b1-ba51-b6101e178401");
   private final AttributeModifier elementalBootsModifier;

   public SpeedModifier(float amount) {
      this.elementalBootsModifier = (new AttributeModifier(elementalBootsUUID, SharedMonsterAttributes.field_111263_d.func_111108_a(), (double)amount, 1)).func_111168_a(false);
   }

   public Multimap getAttributeModifiers(ItemStack stack, GenericArmor armor) {
      Multimap map = HashMultimap.create();
      if (stack != null && stack != ItemStack.field_190927_a) {
         if (!(stack.func_77973_b() instanceof GenericArmor) && !(stack.func_77973_b() instanceof ItemPixelmonBoots)) {
            return map;
         } else {
            map.put(SharedMonsterAttributes.field_111263_d.func_111108_a(), this.elementalBootsModifier);
            return map;
         }
      } else {
         return map;
      }
   }

   public Multimap getNormalAttribute() {
      Multimap o = HashMultimap.create();
      o.put(SharedMonsterAttributes.field_111263_d.func_111108_a(), this.elementalBootsModifier);
      return o;
   }
}
