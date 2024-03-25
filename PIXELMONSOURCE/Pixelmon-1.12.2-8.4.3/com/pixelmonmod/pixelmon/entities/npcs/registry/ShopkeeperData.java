package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumShopKeeperType;
import java.util.ArrayList;
import java.util.Iterator;

public class ShopkeeperData {
   public String id;
   public EnumShopKeeperType type;
   ArrayList textures = new ArrayList();
   ArrayList names = new ArrayList();
   ArrayList chat = new ArrayList();
   ArrayList items = new ArrayList();
   ArrayList biomes = new ArrayList();

   public ShopkeeperData(String name) {
      this.id = name;
   }

   public void addTexture(String texture) {
      this.textures.add(texture);
   }

   void addName(String name) {
      this.names.add(name);
   }

   public void addChat(String hello, String goodbye) {
      this.chat.add(new ShopkeeperChat(hello, goodbye));
   }

   public void addItem(ShopItem item) {
      this.items.add(item);
   }

   public int getRandomChatIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.chat.size() - 1);
   }

   public int getRandomNameIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.names.size() - 1);
   }

   public String getRandomTexture() {
      return (String)RandomHelper.getRandomElementFromList(this.textures);
   }

   public ArrayList getItemList() {
      ArrayList itemList = new ArrayList();
      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         ShopItem item = (ShopItem)var2.next();
         if (item.getRarity() == 1.0F) {
            itemList.add(new ShopItemWithVariation(item));
         } else if (RandomHelper.getRandomChance(item.getRarity())) {
            itemList.add(new ShopItemWithVariation(item));
         }
      }

      return itemList;
   }

   public ShopItem getItem(String itemID) {
      Iterator var2 = this.items.iterator();

      ShopItem item;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         item = (ShopItem)var2.next();
      } while(!item.getBaseItem().id.equals(itemID));

      return item;
   }

   public int getNextNameIndex(int nameIndex) {
      return nameIndex < this.names.size() - 1 ? nameIndex + 1 : 0;
   }

   public String getNextTexture(String texture) {
      for(int i = 0; i < this.textures.size(); ++i) {
         if (((String)this.textures.get(i)).equals(texture)) {
            if (i < this.textures.size() - 1) {
               return (String)this.textures.get(i + 1);
            }

            return (String)this.textures.get(0);
         }
      }

      return (String)this.textures.get(0);
   }

   public int countNames() {
      return this.names.size();
   }

   public ArrayList getTextures() {
      return this.textures;
   }

   public void addBiome(String biome) {
      this.biomes.add(biome);
   }

   public ArrayList getBiomes() {
      return this.biomes;
   }
}
