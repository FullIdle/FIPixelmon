package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetClientTradePair implements IMessage {
   public TradePair tradePair;
   public boolean hasPokemon = false;

   public SetClientTradePair() {
   }

   public SetClientTradePair(TradePair tradePair, boolean hasPokemon) {
      this.tradePair = tradePair;
      this.hasPokemon = hasPokemon;
   }

   public void fromBytes(ByteBuf buf) {
      PokemonSpec offer = (new PokemonSpec(new String[0])).readFromNBT(ByteBufUtils.readTag(buf));
      PokemonSpec exchange = (new PokemonSpec(new String[0])).readFromNBT(ByteBufUtils.readTag(buf));
      String description = ByteBufUtils.readUTF8String(buf);
      if (description.equals("null")) {
         description = null;
      }

      this.tradePair = new TradePair(offer, exchange, description);
      this.hasPokemon = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeTag(buf, this.tradePair.offer.writeToNBT(new NBTTagCompound()));
      ByteBufUtils.writeTag(buf, this.tradePair.exchange.writeToNBT(new NBTTagCompound()));
      ByteBufUtils.writeUTF8String(buf, this.tradePair.description != null && !this.tradePair.description.isEmpty() ? this.tradePair.description : "null");
      buf.writeBoolean(this.hasPokemon);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetClientTradePair message, MessageContext ctx) {
         ClientProxy.currentTradePair = message.tradePair;
         ClientProxy.playerHasTradeRequestPokemon = message.hasPokemon;
         return null;
      }
   }
}
