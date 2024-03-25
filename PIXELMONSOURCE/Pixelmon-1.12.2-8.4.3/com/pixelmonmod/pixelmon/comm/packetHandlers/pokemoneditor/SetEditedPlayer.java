package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetEditedPlayer implements IMessage {
   UUID playerID;
   String playerName;
   Pokemon[] pokemon;

   public SetEditedPlayer() {
   }

   public SetEditedPlayer(UUID playerID, Pokemon[] pokemon) {
      this(playerID, "", pokemon);
   }

   public SetEditedPlayer(UUID playerID, String playerName, Pokemon[] pokemon) {
      this.playerID = playerID;
      this.playerName = playerName;
      this.pokemon = pokemon;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.playerID);
      ByteBufUtils.writeUTF8String(buf, this.playerName);
      Pokemon[] var2 = this.pokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon data = var2[var4];
         if (data == null) {
            buf.writeBoolean(false);
         } else {
            buf.writeBoolean(true);
            data.writeToByteBuffer(buf, EnumUpdateType.ALL);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.playerID = PixelmonMethods.fromBytesUUID(buf);
      this.playerName = ByteBufUtils.readUTF8String(buf);
      this.pokemon = new Pokemon[6];

      for(int i = 0; i < this.pokemon.length; ++i) {
         if (buf.readBoolean()) {
            this.pokemon[i] = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buf, EnumUpdateType.ALL);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetEditedPlayer message, MessageContext ctx) {
         GuiPokemonEditorParty.editedPlayerUUID = message.playerID;
         if (!message.playerName.isEmpty()) {
            GuiPokemonEditorParty.editedPlayerName = message.playerName;
         }

         ServerStorageDisplay.editedPokemon = Arrays.asList(message.pokemon);
         return null;
      }
   }
}
