package com.pixelmonmod.pixelmon.quests.comm;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClientData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendQuestData implements IMessage {
   private QuestProgress serverQP;
   private QuestProgressClientData data;
   private QuestProgressClient clientQP;

   public SendQuestData() {
   }

   public SendQuestData(QuestProgress serverQP) {
      this.serverQP = serverQP;
      this.data = new QuestProgressClientData();
   }

   public void toBytes(ByteBuf buf) {
      this.data.write(this.serverQP, buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.clientQP = new QuestProgressClient(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SendQuestData message, MessageContext ctx) {
         message.clientQP.update();
      }
   }
}
