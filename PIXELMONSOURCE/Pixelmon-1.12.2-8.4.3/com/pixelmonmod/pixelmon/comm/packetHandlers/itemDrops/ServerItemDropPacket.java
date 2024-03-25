package com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops;

import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerItemDropPacket implements IMessage {
   int itemID;
   PacketMode mode;

   public ServerItemDropPacket() {
   }

   public ServerItemDropPacket(PacketMode mode) {
      this.mode = mode;
   }

   public ServerItemDropPacket(int itemID) {
      this.mode = ServerItemDropPacket.PacketMode.TakeItem;
      this.itemID = itemID;
   }

   public void fromBytes(ByteBuf buffer) {
      this.mode = ServerItemDropPacket.PacketMode.values()[buffer.readInt()];
      if (this.mode == ServerItemDropPacket.PacketMode.TakeItem) {
         this.itemID = buffer.readInt();
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.mode.ordinal());
      if (this.mode == ServerItemDropPacket.PacketMode.TakeItem) {
         buffer.writeInt(this.itemID);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ServerItemDropPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_184102_h().func_152344_a(() -> {
            switch (message.mode) {
               case DropAllItems:
                  DropItemQueryList.dropAllItems(player);
                  break;
               case TakeAllItems:
                  DropItemQueryList.takeAllItems(player);
                  break;
               case TakeItem:
                  DropItemQueryList.takeItem(player, message.itemID);
            }

         });
         return null;
      }
   }

   public static enum PacketMode {
      DropAllItems,
      TakeAllItems,
      TakeItem;
   }
}
