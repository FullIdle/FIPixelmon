package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class QueryResourceLocationResult implements IMessage {
   private UUID query;
   private boolean found;

   public QueryResourceLocationResult() {
   }

   public QueryResourceLocationResult(UUID query, boolean found) {
      this.query = query;
      this.found = found;
   }

   public void fromBytes(ByteBuf buf) {
      this.query = UUIDHelper.readUUID(buf);
      this.found = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.query, buf);
      buf.writeBoolean(this.found);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(QueryResourceLocationResult message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            if (ctx.getServerHandler().field_147369_b != null) {
               PlayerPartyStorage pps = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);
               pps.receiveResourceLocationQueryResult(message.query, message.found);
            }

         });
         return null;
      }
   }
}
