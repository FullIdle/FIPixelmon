package com.pixelmonmod.pixelmon.client.gui.npc;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ClientShopItem {
   private ItemStack itemStack;
   private int buy;
   private int sell;
   public int amount = 0;
   private String itemID;

   public static ClientShopItem fromBuffer(ByteBuf buffer) {
      ClientShopItem csi = new ClientShopItem();
      csi.itemID = ByteBufUtils.readUTF8String(buffer);
      csi.itemStack = ByteBufUtils.readItemStack(buffer);
      csi.buy = buffer.readInt();
      csi.sell = buffer.readInt();
      return csi;
   }

   public String getName() {
      return this.itemStack.func_82833_r();
   }

   public String getItemID() {
      return this.itemID;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public int getBuy() {
      return this.buy;
   }

   public int getSell() {
      return this.sell;
   }
}
