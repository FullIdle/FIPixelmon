package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckPlayerBattle implements IMessage {
   UUID uuid;

   public CheckPlayerBattle() {
   }

   public CheckPlayerBattle(EntityPlayer player) {
      this.uuid = player.func_110124_au();
   }

   public void fromBytes(ByteBuf buffer) {
      this.uuid = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.uuid.getMostSignificantBits());
      buffer.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CheckPlayerBattle message, MessageContext ctx) {
         if (ctx.getServerHandler().field_147369_b != null && ctx.getServerHandler().field_147369_b.field_70170_p != null && message.uuid != null) {
            EntityPlayer player = ctx.getServerHandler().field_147369_b.field_70170_p.func_152378_a(message.uuid);
            return player != null && BattleRegistry.getBattleExcludeSpectate(player) != null ? new ShowSpectateMessage(message.uuid) : null;
         } else {
            return null;
         }
      }
   }
}
