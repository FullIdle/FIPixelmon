package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientChangeOpenPC implements IMessage {
   UUID storageUUID;

   public ClientChangeOpenPC() {
   }

   public ClientChangeOpenPC(UUID storageUUID) {
      this.storageUUID = storageUUID;
   }

   public void fromBytes(ByteBuf buf) {
      this.storageUUID = new UUID(buf.readLong(), buf.readLong());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.storageUUID.getMostSignificantBits());
      buf.writeLong(this.storageUUID.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ClientChangeOpenPC message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientStorageManager.openPC = (PCStorage)ClientStorageManager.pcs.getOrDefault(message.storageUUID, ClientStorageManager.openPC);
         });
         return null;
      }
   }
}
