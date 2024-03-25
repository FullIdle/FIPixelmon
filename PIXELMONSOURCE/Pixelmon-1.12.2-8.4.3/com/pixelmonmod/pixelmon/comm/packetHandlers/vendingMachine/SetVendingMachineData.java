package com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine;

import com.pixelmonmod.pixelmon.client.gui.npc.ClientShopItem;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopScreen;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetVendingMachineData implements IMessage {
   ArrayList buyList;
   ArrayList clientBuyList;

   public SetVendingMachineData() {
   }

   public SetVendingMachineData(ArrayList buyList) {
      this.buyList = buyList;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.buyList.size());
      Iterator var2 = this.buyList.iterator();

      while(var2.hasNext()) {
         ShopItemWithVariation item = (ShopItemWithVariation)var2.next();
         item.writeToBuffer(buffer);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      int numItems = buffer.readShort();
      this.clientBuyList = new ArrayList();

      for(int i = 0; i < numItems; ++i) {
         this.clientBuyList.add(ClientShopItem.fromBuffer(buffer));
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetVendingMachineData message, MessageContext ctx) {
         GuiShopScreen.buyItems = message.clientBuyList;
         return null;
      }
   }
}
