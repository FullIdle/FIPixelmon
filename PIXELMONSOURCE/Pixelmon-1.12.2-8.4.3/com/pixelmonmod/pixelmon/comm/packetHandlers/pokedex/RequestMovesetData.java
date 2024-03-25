package com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestMovesetData implements IMessage {
   private EnumSpecies species;
   private byte form;
   private String texture;

   public RequestMovesetData() {
   }

   public RequestMovesetData(EnumSpecies species, byte form, String texture) {
      this.species = species;
      this.form = form;
      this.texture = texture;
   }

   public void toBytes(ByteBuf byteBuf) {
      byteBuf.writeInt(this.species.getNationalPokedexInteger());
      byteBuf.writeByte(this.form);
      ByteBufUtils.writeUTF8String(byteBuf, this.texture);
   }

   public void fromBytes(ByteBuf byteBuf) {
      this.species = EnumSpecies.getFromDex(byteBuf.readInt());
      this.form = byteBuf.readByte();
      this.texture = ByteBufUtils.readUTF8String(byteBuf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RequestMovesetData message, MessageContext ctx) {
         Pixelmon.network.sendTo(new SendMovesetData(message.species, message.form, message.texture), ctx.getServerHandler().field_147369_b);
      }
   }
}
