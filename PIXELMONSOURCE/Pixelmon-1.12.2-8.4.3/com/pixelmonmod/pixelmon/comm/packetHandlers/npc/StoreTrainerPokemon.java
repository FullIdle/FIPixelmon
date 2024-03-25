package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTrainerEditor;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreTrainerPokemon implements IMessage {
   Pokemon pokemon;

   public StoreTrainerPokemon() {
   }

   public StoreTrainerPokemon(Pokemon pokemon) {
      this.pokemon = pokemon;
   }

   public void toBytes(ByteBuf buffer) {
      this.pokemon.writeToByteBuffer(buffer, EnumUpdateType.ALL);
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buffer, EnumUpdateType.ALL);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StoreTrainerPokemon message, MessageContext ctx) {
         GuiTrainerEditor.pokemonList.add(message.pokemon);
         return null;
      }
   }
}
