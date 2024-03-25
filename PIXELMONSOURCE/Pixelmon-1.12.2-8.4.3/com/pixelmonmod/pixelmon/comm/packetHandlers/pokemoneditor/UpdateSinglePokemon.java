package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorIndividual;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateSinglePokemon implements IMessage {
   int slot;
   Pokemon pokemon;

   public UpdateSinglePokemon() {
   }

   public UpdateSinglePokemon(int slot, Pokemon pokemon) {
      this.slot = slot;
      this.pokemon = pokemon;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
      boolean hasPokemon = this.pokemon != null;
      buf.writeBoolean(hasPokemon);
      if (hasPokemon) {
         this.pokemon.writeToByteBuffer(buf, EnumUpdateType.ALL);
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
      if (buf.readBoolean()) {
         this.pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buf, EnumUpdateType.ALL);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateSinglePokemon message, MessageContext ctx) {
         ServerStorageDisplay.editedPokemon.set(message.slot, message.pokemon);
         if (Minecraft.func_71410_x().field_71462_r instanceof GuiPokemonEditorIndividual) {
            GuiPokemonEditorIndividual gui = (GuiPokemonEditorIndividual)Minecraft.func_71410_x().field_71462_r;
            if (message.pokemon != null && gui.p.getUUID().equals(message.pokemon.getUUID())) {
               gui.p = message.pokemon;
               gui.func_73866_w_();
            }
         }

         return null;
      }
   }
}
