package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientInitializePC implements IMessage {
   private PCStorage storage;

   public ClientInitializePC() {
   }

   public ClientInitializePC(PCStorage storage) {
      Preconditions.checkNotNull(storage);
      this.storage = storage;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.storage.uuid.getMostSignificantBits());
      buf.writeLong(this.storage.uuid.getLeastSignificantBits());
      buf.writeShort(this.storage.getBoxCount());
      buf.writeShort(this.storage.getLastBox());

      for(int i = 0; i < this.storage.getBoxCount(); ++i) {
         PCBox box = this.storage.getBox(i);
         ByteBufUtils.writeUTF8String(buf, box.getName() == null ? "" : box.getName());
         ByteBufUtils.writeUTF8String(buf, box.getWallpaper() == null ? "" : box.getWallpaper());
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.storage = new PCStorage(new UUID(buf.readLong(), buf.readLong()), buf.readShort());
      this.storage.shouldSendUpdates = false;
      this.storage.setLastBox(buf.readShort());

      for(int i = 0; i < this.storage.getBoxCount(); ++i) {
         PCBox box = this.storage.getBox(i);
         box.setName(ByteBufUtils.readUTF8String(buf));
         box.setWallpaper(ByteBufUtils.readUTF8String(buf));
         box.hasChangedClientSide = false;
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ClientInitializePC message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            if (message.storage.uuid.equals(Minecraft.func_71410_x().field_71439_g.func_110124_au())) {
               message.storage.playerUUID = message.storage.uuid;
            }

            ClientStorageManager.pcs.put(message.storage.uuid, message.storage);
            if (ClientStorageManager.openPC == null || ClientStorageManager.openPC.getBoxCount() == 0) {
               ClientStorageManager.openPC = message.storage;
            }

         });
         return null;
      }
   }
}
