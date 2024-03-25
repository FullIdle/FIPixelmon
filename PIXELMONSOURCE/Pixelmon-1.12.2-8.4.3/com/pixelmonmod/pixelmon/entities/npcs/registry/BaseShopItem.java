package com.pixelmonmod.pixelmon.entities.npcs.registry;

import net.minecraft.item.ItemStack;

public class BaseShopItem {
   public String id;
   ItemStack itemStack;
   int buy;
   int sell;

   public BaseShopItem(String id, ItemStack itemStack, int buy, int sell) {
      this.id = id;
      this.itemStack = itemStack;
      this.buy = buy;
      this.sell = sell;
   }

   public ItemStack getItem() {
      return this.itemStack;
   }
}
