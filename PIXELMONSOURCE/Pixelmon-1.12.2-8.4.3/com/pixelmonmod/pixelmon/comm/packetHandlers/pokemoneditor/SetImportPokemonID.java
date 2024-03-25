package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPartyEditorBase;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetImportPokemonID implements IMessage {
   int partySlot;
   UUID pokemonUUID;

   public SetImportPokemonID() {
   }

   public SetImportPokemonID(int partySlot, UUID pokemonUUID) {
      this.partySlot = partySlot;
      this.pokemonUUID = pokemonUUID;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.partySlot);
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
   }

   public void fromBytes(ByteBuf buf) {
      this.partySlot = buf.readInt();
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetImportPokemonID message, MessageContext ctx) {
         Pokemon pokemon = null;
         GuiScreen currentScreen = Minecraft.func_71410_x().field_71462_r;
         List pokemonList = null;
         if (currentScreen instanceof GuiPartyEditorBase) {
            GuiPartyEditorBase partyEditor = (GuiPartyEditorBase)currentScreen;
            pokemonList = partyEditor.pokemonList;
         } else {
            pokemonList = ServerStorageDisplay.editedPokemon;
         }

         if (pokemonList != null && pokemonList.size() > message.partySlot) {
            pokemon = (Pokemon)pokemonList.get(message.partySlot);
         }

         if (pokemon != null) {
            pokemon.setUUID(message.pokemonUUID);
         }

         return null;
      }
   }
}
