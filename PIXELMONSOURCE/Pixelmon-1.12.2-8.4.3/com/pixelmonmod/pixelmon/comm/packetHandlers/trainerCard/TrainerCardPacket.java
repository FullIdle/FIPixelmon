package com.pixelmonmod.pixelmon.comm.packetHandlers.trainerCard;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.trainerCard.GuiTrainerCard;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import io.netty.buffer.ByteBuf;
import java.text.NumberFormat;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainerCardPacket implements IMessage {
   private UUID playerId;
   private EnumTrainerCardColor color;
   private int money;
   private int caughtCount;
   private Pokemon[] party = new Pokemon[6];

   public TrainerCardPacket() {
   }

   public TrainerCardPacket(UUID playerId, EnumTrainerCardColor color, int money, int caughtCount, Pokemon[] party) {
      this.playerId = playerId;
      this.color = color;
      this.money = money;
      this.caughtCount = caughtCount;
      this.party = party;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.playerId.getMostSignificantBits());
      buf.writeLong(this.playerId.getLeastSignificantBits());
      buf.writeShort(this.color.ordinal());
      buf.writeInt(this.money);
      buf.writeInt(this.caughtCount);
      buf.writeShort(this.party.length);
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon poke = var2[var4];
         boolean isPresent = poke != null;
         buf.writeBoolean(isPresent);
         if (isPresent) {
            poke.writeToByteBuffer(buf, EnumUpdateType.Appearance);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.playerId = new UUID(buf.readLong(), buf.readLong());
      this.color = EnumTrainerCardColor.values()[buf.readShort()];
      this.money = buf.readInt();
      this.caughtCount = buf.readInt();
      int length = buf.readShort();

      for(int i = 0; i < length; ++i) {
         if (buf.readBoolean()) {
            this.party[i] = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buf, EnumUpdateType.Appearance);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(TrainerCardPacket message, MessageContext ctx) {
         if (Minecraft.func_71410_x().field_71462_r instanceof GuiTrainerCard) {
            GuiTrainerCard gui = (GuiTrainerCard)Minecraft.func_71410_x().field_71462_r;
            if (gui.player.func_110124_au().equals(message.playerId)) {
               gui.color = message.color;
               gui.money = NumberFormat.getInstance().format((long)message.money);
               gui.caughtCount = message.caughtCount;
               gui.party = message.party;
            }
         }

         return null;
      }
   }
}
