package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestSpectate implements IMessage {
   UUID uuid;

   public RequestSpectate() {
   }

   public RequestSpectate(UUID playerId) {
      this.uuid = playerId;
   }

   public void fromBytes(ByteBuf buffer) {
      this.uuid = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.uuid.getMostSignificantBits());
      buffer.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RequestSpectate message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            this.processMessage(message, ctx);
         });
         return null;
      }

      private void processMessage(RequestSpectate message, MessageContext ctx) {
         EntityPlayerMP watcher = ctx.getServerHandler().field_147369_b;
         EntityPlayerMP watchedPlayer = (EntityPlayerMP)watcher.field_70170_p.func_152378_a(message.uuid);
         if (watchedPlayer != null) {
            BattleControllerBase base = BattleRegistry.getBattle(watchedPlayer);
            if (base != null && !Pixelmon.EVENT_BUS.post(new SpectateEvent.StartSpectate(watcher, base, watchedPlayer))) {
               PlayerParticipant watchedParticipant = base.getPlayer(watchedPlayer.getDisplayNameString());
               if (watchedParticipant == null) {
                  return;
               }

               Pixelmon.network.sendTo(new StartBattle(base.battleIndex, base.getBattleType(watchedParticipant), base.rules), watcher);
               Pixelmon.network.sendTo(new SetAllBattlingPokemon(PixelmonInGui.convertToGUI(Arrays.asList(watchedParticipant.allPokemon))), watcher);
               ArrayList teamList = watchedParticipant.getTeamPokemonList();
               Pixelmon.network.sendTo(new SetBattlingPokemon(teamList), watcher);
               Pixelmon.network.sendTo(new SetPokemonBattleData(PixelmonInGui.convertToGUI(teamList), false), watcher);
               Pixelmon.network.sendTo(new SetPokemonBattleData(watchedParticipant.getOpponentData(), true), watcher);
               Pixelmon.network.sendTo(new StartSpectate(watchedPlayer.func_110124_au(), base.rules.battleType), watcher);
               if (base.getTeam(watchedParticipant).size() > 1) {
                  Pixelmon.network.sendTo(new SetPokemonTeamData(watchedParticipant.getAllyData()), watcher);
               }

               base.addSpectator(new Spectator(watcher, watchedPlayer.getDisplayNameString()));
            }
         }

      }
   }
}
