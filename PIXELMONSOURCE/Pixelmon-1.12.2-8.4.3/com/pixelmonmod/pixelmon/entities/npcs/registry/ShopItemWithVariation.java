package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.RandomHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ShopItemWithVariation {
   private float variation;
   private ShopItem shopItem;

   public ShopItemWithVariation(ShopItem shopItem, float variation) {
      this.shopItem = shopItem;
      this.variation = variation;
   }

   public ShopItemWithVariation(ShopItem item) {
      this(item, item.canPriceVary() ? getVariation() : 1.0F);
   }

   private static float getVariation() {
      float rand = RandomHelper.getRandomNumberBetween(0.0F, 1.0F);
      float variation = 1.0F;
      if ((double)rand > 0.75) {
         variation = 1.1F;
      } else if ((double)rand < 0.25) {
         variation = 0.9F;
      }

      return variation;
   }

   public void writeToNBT(NBTTagList list) {
      NBTTagCompound tag = new NBTTagCompound();
      tag.func_74778_a("ItemName", this.shopItem.getBaseItem().id);
      tag.func_74776_a("ItemVar", this.variation);
      list.func_74742_a(tag);
   }

   public static ShopItemWithVariation getFromNBT(String npcIndex, NBTTagCompound tag) {
      ShopItem item = ServerNPCRegistry.shopkeepers.getItem(npcIndex, tag.func_74779_i("ItemName"));
      return item == null ? null : new ShopItemWithVariation(item, tag.func_74760_g("ItemVar"));
   }

   public void writeToBuffer(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.shopItem.getBaseItem().id);
      ByteBufUtils.writeItemStack(buffer, this.shopItem.getBaseItem().itemStack);
      buffer.writeInt((int)((float)this.getBaseShopItem().buy * this.shopItem.getBuyMultiplier() * this.variation));
      buffer.writeInt((int)((float)this.getBaseShopItem().sell * this.variation));
   }

   public ItemStack getItemStack() {
      return this.shopItem.getItemStack();
   }

   public BaseShopItem getBaseShopItem() {
      return this.shopItem.getBaseItem();
   }

   public boolean canSell() {
      return this.shopItem.getBaseItem().sell != -1;
   }

   public int getBuyCost() {
      return (int)Math.max(0.0F, (float)this.getBaseShopItem().buy * this.shopItem.getBuyMultiplier() * this.variation);
   }

   public int getSellCost() {
      return (int)((float)this.getBaseShopItem().sell * this.variation);
   }
}
