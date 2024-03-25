package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ParticipantReady implements IMessage {
   public String uuid;
   public int battleIndex;

   public ParticipantReady() {
   }

   public ParticipantReady(int battleControllerIndex, String uuid) {
      this.uuid = uuid;
      this.battleIndex = battleControllerIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.battleIndex = buf.readInt();
      this.uuid = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.battleIndex);
      ByteBufUtils.writeUTF8String(buf, this.uuid);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ParticipantReady message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_143004_u();
         BattleControllerBase bc = BattleRegistry.getBattle(message.battleIndex);
         if (bc != null) {
            bc.participants.stream().filter((bp) -> {
               return bp instanceof PlayerParticipant;
            }).filter((bp) -> {
               return ((PlayerParticipant)bp).player.func_110124_au().toString().equals(message.uuid);
            }).forEach((bp) -> {
               bc.participantReady((PlayerParticipant)bp);
            });
         }
      }
   }
}
