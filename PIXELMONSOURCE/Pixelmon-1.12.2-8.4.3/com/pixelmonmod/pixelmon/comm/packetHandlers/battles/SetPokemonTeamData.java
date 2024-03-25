package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetPokemonTeamData implements IMessage {
   PixelmonInGui[] data;

   public SetPokemonTeamData() {
   }

   public SetPokemonTeamData(ArrayList otherTeamPokemon) {
      this.data = new PixelmonInGui[otherTeamPokemon.size()];

      for(int i = 0; i < otherTeamPokemon.size(); ++i) {
         this.data[i] = new PixelmonInGui((PixelmonWrapper)otherTeamPokemon.get(i));
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.data.length);
      PixelmonInGui[] var2 = this.data;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonInGui pg = var2[var4];
         pg.encodeInto(buffer);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.data = new PixelmonInGui[buffer.readShort()];

      for(int i = 0; i < this.data.length; ++i) {
         this.data[i] = new PixelmonInGui();
         this.data[i].decodeFrom(buffer);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetPokemonTeamData message, MessageContext ctx) {
         ClientProxy.battleManager.setTeamData(message.data);
         return null;
      }
   }
}
