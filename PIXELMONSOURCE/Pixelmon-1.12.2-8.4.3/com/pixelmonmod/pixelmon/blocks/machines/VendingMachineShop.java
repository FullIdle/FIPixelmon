package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class VendingMachineShop {
   private ArrayList items = new ArrayList();

   public VendingMachineShop() {
      this.addShopItem(PixelmonItems.freshWater, 200, 100);
      this.addShopItem(PixelmonItems.sodaPop, 300, 150);
      this.addShopItem(PixelmonItems.lemonade, 350, 175);
      this.addShopItem(PixelmonItems.moomooMilk, 500, 250);
      this.addShopItem(PixelmonItemsHeld.berryJuice, 1500, 50);
      this.addShopItem(PixelmonItems.curryCoconutMilk, 1000, 100);
   }

   private void addShopItem(Item item, int buyPrice, int sellPrice) {
      BaseShopItem baseItem = new BaseShopItem(item.func_77658_a(), new ItemStack(item, 1), buyPrice, sellPrice);
      ShopItem shopItem = new ShopItem(baseItem, 1.0F, 1.0F, false);
      ShopItemWithVariation shopItemWithVariation = new ShopItemWithVariation(shopItem, 1.0F);
      this.items.add(shopItemWithVariation);
   }

   public ArrayList getItems() {
      return this.items;
   }
}
