package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Flee implements IMessage {
   UUID fleeingPokemonUUID;

   public Flee() {
   }

   public Flee(UUID fleeingPokemonUUID) {
      this.fleeingPokemonUUID = fleeingPokemonUUID;
   }

   public void fromBytes(ByteBuf buffer) {
      this.fleeingPokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.fleeingPokemonUUID);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(Flee message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         BattleControllerBase bc = BattleRegistry.getBattle(player);
         if (bc != null) {
            bc.participants.stream().filter((p) -> {
               return p != null && p instanceof PlayerParticipant;
            }).filter((p) -> {
               return ((PlayerParticipant)p).player == player;
            }).forEach((p) -> {
               bc.setFlee(message.fleeingPokemonUUID);
            });
         }
      }
   }
}
