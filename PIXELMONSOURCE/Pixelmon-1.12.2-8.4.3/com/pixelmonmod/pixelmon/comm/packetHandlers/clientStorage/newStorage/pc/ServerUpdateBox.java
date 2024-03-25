package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerUpdateBox implements IMessage {
   private int boxNumber;
   private String name;
   private String wallpaper;

   public ServerUpdateBox() {
   }

   public ServerUpdateBox(int boxNumber, String name, String wallpaper) {
      this.boxNumber = boxNumber;
      this.name = name;
      this.wallpaper = wallpaper;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.boxNumber);
      ByteBufUtils.writeUTF8String(buf, this.name == null ? "" : this.name);
      ByteBufUtils.writeUTF8String(buf, this.wallpaper == null ? "" : this.wallpaper);
   }

   public void fromBytes(ByteBuf buf) {
      this.boxNumber = buf.readShort();
      this.name = ByteBufUtils.readUTF8String(buf);
      this.wallpaper = ByteBufUtils.readUTF8String(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerUpdateBox message, MessageContext ctx) {
         PCStorage pc = Pixelmon.storageManager.getPCForPlayer(ctx.getServerHandler().field_147369_b);
         PCBox box = pc.getBox(message.boxNumber);
         box.setName(message.name);
         box.setWallpaper(message.wallpaper);
         box.setNeedsSaving();
      }
   }
}
