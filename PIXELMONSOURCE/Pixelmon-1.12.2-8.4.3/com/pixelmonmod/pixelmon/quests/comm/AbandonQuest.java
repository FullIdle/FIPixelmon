package com.pixelmonmod.pixelmon.quests.comm;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AbandonQuest implements IMessage {
   private String filename;
   private UUID identifier;

   public AbandonQuest() {
   }

   public AbandonQuest(QuestProgressClient qpc) {
      this.filename = qpc.getFilename();
      this.identifier = qpc.getIdentifier();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.filename);
      UUIDHelper.writeUUID(this.identifier, buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.filename = ByteBufUtils.readUTF8String(buf);
      this.identifier = UUIDHelper.readUUID(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(AbandonQuest message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player != null) {
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
            pps.getQuestData(true).abandonQuest(message.filename, message.identifier);
         }

      }
   }
}
