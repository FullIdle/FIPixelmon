package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiChattingNPCEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiQuestGiverNPCEditor;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetChattingNPCTextures implements IMessage {
   private List data = new ArrayList();

   public SetChattingNPCTextures() {
      Iterator var1 = ServerNPCRegistry.getEnglishNPCs().iterator();

      while(var1.hasNext()) {
         GeneralNPCData generalData = (GeneralNPCData)var1.next();
         Iterator var3 = generalData.getTextures().iterator();

         while(var3.hasNext()) {
            String texture = (String)var3.next();
            this.data.add(new ClientNPCData(generalData.id, texture));
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ArrayHelper.encodeList(buf, this.data);
   }

   public void fromBytes(ByteBuf buf) {
      this.data.clear();
      int listSize = buf.readInt();

      for(int i = 0; i < listSize; ++i) {
         this.data.add(new ClientNPCData(buf));
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetChattingNPCTextures message, MessageContext ctx) {
         GuiChattingNPCEditor.npcData = GuiQuestGiverNPCEditor.npcData = message.data;
         return null;
      }
   }
}
