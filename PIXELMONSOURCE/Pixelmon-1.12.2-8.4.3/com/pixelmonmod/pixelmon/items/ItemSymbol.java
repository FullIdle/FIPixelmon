package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumSymbols;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class ItemSymbol extends PixelmonItem implements IEquippable {
   public EnumSymbols symbol;

   public ItemSymbol(EnumSymbols symbol) {
      super(symbol.getFileName());
      this.symbol = symbol;
      this.func_77625_d(1);
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.badges);
      this.canRepair = false;
   }

   public String getTooltipText() {
      return I18n.func_74838_a("item.symbol.tooltip");
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.CHEST;
   }

   public String getEquippableModelKey() {
      return "symbol";
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
