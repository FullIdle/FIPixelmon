package com.pixelmonmod.pixelmon.comm.packetHandlers.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RaidAction implements IMessage {
   private int den;
   private int action;
   private int slot;

   public RaidAction() {
   }

   public RaidAction(int den, int action) {
      this(den, action, 0);
   }

   public RaidAction(int den, int action, int slot) {
      this.den = den;
      this.action = action;
      this.slot = slot;
   }

   public void fromBytes(ByteBuf buf) {
      this.den = buf.readInt();
      this.action = buf.readInt();
      this.slot = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.den);
      buf.writeInt(this.action);
      buf.writeInt(this.slot);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RaidAction message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         WorldServer world = player.func_71121_q();
         Entity entity = world.func_73045_a(message.den);
         if (entity instanceof EntityDen) {
            EntityDen den = (EntityDen)entity;
            RaidData raid = den.getServerData();
            if (raid == null || raid.getPlayer(player) == null) {
               raid = den.getInUseRaidData();
               if (raid != null && raid.getPlayer(player) == null) {
                  raid = null;
               }
            }

            if (raid != null) {
               if (den.func_70032_d(player) <= 30.0F) {
                  raid.processAction(message.action, message.slot, player);
               } else {
                  raid.removePlayer(player);
               }
            } else {
               Pixelmon.network.sendTo(new CloseRaid(), player);
            }
         }

      }
   }
}
