package com.pixelmonmod.pixelmon.comm.packetHandlers.trainerCard;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainerCardColorPacket implements IMessage {
   private EnumTrainerCardColor color;

   public TrainerCardColorPacket() {
   }

   public TrainerCardColorPacket(EnumTrainerCardColor color) {
      this.color = color;
   }

   public void fromBytes(ByteBuf buf) {
      this.color = EnumTrainerCardColor.values()[buf.readShort()];
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.color.ordinal());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(TrainerCardColorPacket message, MessageContext ctx) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);
         party.trainerCardColor = message.color == null ? EnumTrainerCardColor.WHITE : message.color;
      }
   }
}
