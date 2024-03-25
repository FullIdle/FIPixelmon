package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.customStarters.SelectPokemonController;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectPokemon implements IMessage {
   int starterIndex;

   public SelectPokemon() {
   }

   public SelectPokemon(int starterIndex) {
      this.starterIndex = starterIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.starterIndex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.starterIndex);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SelectPokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (SelectPokemonController.isOnList(player)) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            EnumSpecies[] list = SelectPokemonController.getPokemonList(player);
            if (list != null && message.starterIndex > 0 && message.starterIndex < list.length) {
               Pokemon pokemon = (new PokemonSpec(new String[]{list[message.starterIndex].name, "lvl:" + PixelmonConfig.starterLevel})).create();
               Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(player, ReceiveType.SelectPokemon, pokemon));
               party.add(pokemon);
               party.starterPicked = true;
               SelectPokemonController.removePlayer(player);
            }

         }
      }
   }
}
