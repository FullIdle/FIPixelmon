package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.blocks.spawning.BlockPixelmonSpawner;
import com.pixelmonmod.pixelmon.comm.PixelmonSpawnerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateSpawner implements IMessage {
   PixelmonSpawnerData data;

   public UpdateSpawner() {
   }

   public UpdateSpawner(PixelmonSpawnerData data) {
      this.data = data;
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = new PixelmonSpawnerData();
      this.data.readPacketData(buffer);
   }

   public void toBytes(ByteBuf buffer) {
      this.data.writePacketData(buffer);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateSpawner message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (BlockPixelmonSpawner.checkPermission(player)) {
            message.data.updateTileEntity(ctx.getServerHandler().field_147369_b.field_70170_p);
         }

         return null;
      }
   }
}
