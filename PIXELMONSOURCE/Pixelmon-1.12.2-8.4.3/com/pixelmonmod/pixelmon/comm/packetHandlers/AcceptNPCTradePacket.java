package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.trading.NPCTrades;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.TradeEvolution;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AcceptNPCTradePacket implements IMessage {
   private int id = -1;

   public AcceptNPCTradePacket() {
   }

   public AcceptNPCTradePacket(int id) {
      this.id = id;
   }

   public void fromBytes(ByteBuf buffer) {
      this.id = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.id);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(AcceptNPCTradePacket message, MessageContext ctx) {
         EntityPlayerMP playerMP = ctx.getServerHandler().field_147369_b;
         TradePair tradePair = NPCTrades.getTradePair(playerMP.func_110124_au());
         if (tradePair != null) {
            Pokemon pokemon = tradePair.offer.create();
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);

            for(int i = 0; i < party.getAll().length; ++i) {
               Pokemon partyPokemon = party.get(i);
               if (partyPokemon != null && !partyPokemon.isEgg() && tradePair.exchange.matches(partyPokemon) && !NPCTrades.UNTRADEABLE.matches(partyPokemon)) {
                  if (!pokemon.isEgg() && !Pixelmon.EVENT_BUS.post(new PokedexEvent(ctx.getServerHandler().field_147369_b.func_110124_au(), pokemon, EnumPokedexRegisterStatus.caught, "tradeNPC"))) {
                     party.pokedex.set(pokemon, EnumPokedexRegisterStatus.caught);
                     party.pokedex.update();
                  }

                  if (!PixelmonConfig.reuseTraders) {
                     Optional entityNPC = EntityNPC.locateNPCServer(playerMP.field_70170_p, message.id, EntityNPC.class);
                     if (entityNPC.isPresent()) {
                        ((EntityNPC)entityNPC.get()).func_70106_y();
                     }
                  }

                  playerMP.func_71053_j();
                  party.set(i, pokemon);
                  EntityPixelmon pixelmon = pokemon.getOrSpawnPixelmon((World)null, 0.0, 0.0, 0.0, 0.0F, 0.0F);
                  pokemon.getEvolutions(TradeEvolution.class).forEach((evo) -> {
                     if (evo.canEvolve(pixelmon, pokemon.getSpecies())) {
                        evo.doEvolution(pixelmon);
                     }
                  });
                  return;
               }
            }

         }
      }
   }
}
