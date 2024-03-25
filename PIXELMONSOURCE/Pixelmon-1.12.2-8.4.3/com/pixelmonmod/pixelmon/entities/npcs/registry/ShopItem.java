package com.pixelmonmod.pixelmon.entities.npcs.registry;

import net.minecraft.item.ItemStack;

public class ShopItem {
   private BaseShopItem item;
   private float rarity;
   private float buyMultiplier;
   private boolean canPriceVary;

   public ShopItem(BaseShopItem item, float buyMultiplier, float rarity, boolean canPriceVary) {
      this.item = item;
      this.rarity = rarity;
      this.buyMultiplier = buyMultiplier;
      this.canPriceVary = canPriceVary;
   }

   public float getRarity() {
      return this.rarity;
   }

   public float getBuyMultiplier() {
      return this.buyMultiplier;
   }

   public boolean canPriceVary() {
      return this.canPriceVary;
   }

   public ItemStack getItemStack() {
      return this.item.itemStack;
   }

   public BaseShopItem getBaseItem() {
      return this.item;
   }
}
