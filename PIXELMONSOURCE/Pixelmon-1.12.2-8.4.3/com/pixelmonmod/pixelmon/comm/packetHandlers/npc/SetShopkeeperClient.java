package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiShopkeeperEditor;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientShopkeeperData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetShopkeeperClient implements IMessage {
   List data = new ArrayList();

   public SetShopkeeperClient() {
   }

   public SetShopkeeperClient(String language) {
      Iterator var2 = ServerNPCRegistry.getEnglishShopkeepers().iterator();

      while(var2.hasNext()) {
         ShopkeeperData shopkeeper = (ShopkeeperData)var2.next();
         List names = new ArrayList();

         for(int i = 0; i < shopkeeper.countNames(); ++i) {
            names.add(ServerNPCRegistry.shopkeepers.getTranslatedName(language, shopkeeper.id, i));
         }

         this.data.add(new ClientShopkeeperData(shopkeeper.id, shopkeeper.getTextures(), names));
      }

   }

   public void toBytes(ByteBuf buf) {
      ArrayHelper.encodeList(buf, this.data);
   }

   public void fromBytes(ByteBuf buf) {
      this.data.clear();
      int listSize = buf.readInt();

      for(int i = 0; i < listSize; ++i) {
         this.data.add(new ClientShopkeeperData(buf));
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetShopkeeperClient message, MessageContext ctx) {
         GuiShopkeeperEditor.shopkeeperData = message.data;
         return null;
      }
   }
}
