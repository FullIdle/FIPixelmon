package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.TickHandler;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.StarterList;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChooseStarter implements IMessage {
   int starterIndex;

   public ChooseStarter() {
   }

   public ChooseStarter(int starterIndex) {
      this.starterIndex = starterIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.starterIndex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.starterIndex);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ChooseStarter message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         if (!party.starterPicked) {
            PokemonForm[] list = StarterList.getStarterList();
            if (list != null && message.starterIndex >= 0 && message.starterIndex < list.length) {
               PokemonForm pokemonForm = StarterList.getStarterList()[message.starterIndex];
               PokemonSpec spec = new PokemonSpec(pokemonForm.pokemon.name);
               spec.level = PixelmonConfig.starterLevel;
               spec.shiny = pokemonForm.shiny;
               spec.gender = (byte)pokemonForm.gender.ordinal();
               if (pokemonForm.pokemon.getNumForms(false) > 1) {
                  spec.form = (byte)pokemonForm.form;
               }

               Pokemon pokemon = spec.create();
               party.starterPicked = true;
               if (PixelmonConfig.starterMarks) {
                  pokemon.addRibbon(EnumRibbonType.DESTINY);
               }

               Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(player, ReceiveType.Starter, pokemon));
               party.add(pokemon);
               TickHandler.deregisterStarterList(player);
            }

         }
      }
   }
}
