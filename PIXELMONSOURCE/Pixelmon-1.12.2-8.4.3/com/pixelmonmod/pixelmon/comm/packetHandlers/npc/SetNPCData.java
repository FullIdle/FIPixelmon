package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.client.gui.npc.ClientShopItem;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiChattingNPC;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiQuestGiverNPC;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopScreen;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopkeeper;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperChat;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetNPCData implements IMessage {
   SetTrainerData data;
   EnumNPCType npcType;
   ArrayList chatPages;
   String name;
   ArrayList buyList;
   ArrayList sellList;
   ArrayList clientBuyList;
   ArrayList clientSellList;
   ShopkeeperChat skChat;

   public SetNPCData() {
   }

   public SetNPCData(String name, ArrayList chatPages) {
      this(name, chatPages, EnumNPCType.ChattingNPC);
   }

   public SetNPCData(String name, ArrayList chatPages, EnumNPCType type) {
      this.name = name;
      this.npcType = type;
      this.chatPages = chatPages;
   }

   public SetNPCData(String name, ShopkeeperChat chat, ArrayList buyList, ArrayList sellList2) {
      this.name = name;
      this.npcType = EnumNPCType.Shopkeeper;
      this.skChat = chat;
      this.buyList = buyList;
      this.sellList = sellList2;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.npcType.ordinal());
      Iterator var2;
      switch (this.npcType) {
         case ChattingNPC:
         case QuestGiver:
            ByteBufUtils.writeUTF8String(buffer, this.name);
            buffer.writeShort(this.chatPages.size());
            var2 = this.chatPages.iterator();

            while(var2.hasNext()) {
               String page = (String)var2.next();
               ByteBufUtils.writeUTF8String(buffer, page);
            }

            return;
         case Shopkeeper:
            ByteBufUtils.writeUTF8String(buffer, this.name);
            this.skChat.writeToBuffer(buffer);
            buffer.writeShort(this.buyList.size());
            var2 = this.buyList.iterator();

            ShopItemWithVariation item;
            while(var2.hasNext()) {
               item = (ShopItemWithVariation)var2.next();
               item.writeToBuffer(buffer);
            }

            buffer.writeShort(this.sellList.size());
            var2 = this.sellList.iterator();

            while(var2.hasNext()) {
               item = (ShopItemWithVariation)var2.next();
               item.writeToBuffer(buffer);
            }
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.npcType = EnumNPCType.getFromOrdinal(buffer.readShort());
      int numItems;
      switch (this.npcType) {
         case ChattingNPC:
         case QuestGiver:
            this.name = ByteBufUtils.readUTF8String(buffer);
            this.chatPages = new ArrayList();
            int numPages = buffer.readShort();

            for(numItems = 0; numItems < numPages; ++numItems) {
               this.chatPages.add(ByteBufUtils.readUTF8String(buffer));
            }

            return;
         case Shopkeeper:
            this.name = ByteBufUtils.readUTF8String(buffer);
            this.skChat = ShopkeeperChat.loadFromBuffer(buffer);
            numItems = buffer.readShort();
            this.clientBuyList = new ArrayList();

            int i;
            for(i = 0; i < numItems; ++i) {
               this.clientBuyList.add(ClientShopItem.fromBuffer(buffer));
            }

            numItems = buffer.readShort();
            this.clientSellList = new ArrayList();

            for(i = 0; i < numItems; ++i) {
               this.clientSellList.add(ClientShopItem.fromBuffer(buffer));
            }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetNPCData message, MessageContext ctx) {
         switch (message.npcType) {
            case ChattingNPC:
               GuiChattingNPC.name = message.name;
               GuiChattingNPC.chatPages = message.chatPages;
               break;
            case QuestGiver:
               GuiQuestGiverNPC.name = message.name;
               GuiQuestGiverNPC.chatPages = message.chatPages;
               break;
            case Shopkeeper:
               GuiShopkeeper.name = message.name;
               GuiShopkeeper.chat = message.skChat;
               GuiShopScreen.buyItems = message.clientBuyList;
               GuiShopScreen.sellItems = message.clientSellList;
         }

         return null;
      }
   }
}
