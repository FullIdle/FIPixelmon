package com.pixelmonmod.pixelmon.comm.packetHandlers.trainerCard;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestTrainerCardPacket implements IMessage {
   private UUID playerId;

   public RequestTrainerCardPacket() {
   }

   public RequestTrainerCardPacket(UUID playerId) {
      this.playerId = playerId;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.playerId.getMostSignificantBits());
      buf.writeLong(this.playerId.getLeastSignificantBits());
   }

   public void fromBytes(ByteBuf buf) {
      this.playerId = new UUID(buf.readLong(), buf.readLong());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RequestTrainerCardPacket message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b.func_184102_h().func_184103_al().func_177451_a(message.playerId);
            if (player != null) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               Pixelmon.network.sendTo(new TrainerCardPacket(player.func_110124_au(), party.trainerCardColor, ((IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(player).get()).getMoney(), party.pokedex.countCaught(), Pixelmon.storageManager.getParty(player).getAll()), ctx.getServerHandler().field_147369_b);
            }

         });
         return null;
      }
   }
}
