package com.pixelmonmod.pixelmon.quests.comm;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetDisplayQuest implements IMessage {
   private UUID identifier;

   public SetDisplayQuest() {
   }

   public SetDisplayQuest(UUID identifier) {
      this.identifier = identifier;
   }

   public SetDisplayQuest(QuestProgress quest) {
      this.identifier = quest.getIdentifier();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.identifier, buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.identifier = UUIDHelper.readUUID(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetDisplayQuest message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            QuestProgressClient qpc = QuestDataClient.getInstance().getVisible(message.identifier);
            if (qpc != null) {
               QuestDataClient.getInstance().setDisplayQuest(qpc);
            }

         });
      }
   }
}
