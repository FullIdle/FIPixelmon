package com.pixelmonmod.pixelmon.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemRelicCrown extends PixelmonItemBlock implements IEquippable {
   public ItemRelicCrown(Block block) {
      super(block);
   }

   public ItemRelicCrown(Block block, String name) {
      super(block);
      this.func_77655_b(name);
      this.setRegistryName(name);
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.HEAD;
   }

   public String getEquippableModelKey() {
      return "relic_crown";
   }

   public ResourceLocation getEquippableTexture() {
      return null;
   }

   public Item getEquippableItem() {
      return this;
   }

   public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
      return (!stack.func_77942_o() || !stack.func_77978_p().func_74767_n("Unequippable")) && this.getEquipmentSlot(stack) == armorType;
   }
}
