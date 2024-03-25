package com.pixelmonmod.pixelmon.quests.comm;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HideDisplayQuest implements IMessage {
   public void toBytes(ByteBuf buf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(HideDisplayQuest message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            QuestDataClient.getInstance().setDisplayQuest((QuestProgressClient)null);
         });
      }
   }
}
