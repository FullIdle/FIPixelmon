package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectPokemonResponse implements IMessage {
   Mode mode;
   int npcId;
   UUID pokemon;

   public SelectPokemonResponse() {
   }

   public SelectPokemonResponse(Mode mode, int npcId, UUID pokemon) {
      this.mode = mode;
      this.npcId = npcId;
      this.pokemon = pokemon;
   }

   public void fromBytes(ByteBuf buf) {
      this.mode = SelectPokemonResponse.Mode.fromOrdinal(buf.readByte());
      this.npcId = buf.readInt();
      this.pokemon = UUIDHelper.readUUID(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.mode.ordinal());
      buf.writeInt(this.npcId);
      UUIDHelper.writeUUID(this.pokemon, buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SelectPokemonResponse message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         Pokemon pokemon = storage.find(message.pokemon);
         if (pokemon != null) {
            Optional relearner;
            if (message.mode != SelectPokemonResponse.Mode.Tutor && message.mode != SelectPokemonResponse.Mode.Transfer_Tutor) {
               if (message.mode == SelectPokemonResponse.Mode.Relearner) {
                  relearner = EntityNPC.locateNPCServer(player.field_70170_p, message.npcId, NPCRelearner.class);
                  relearner.ifPresent((it) -> {
                     it.handlePickedPokemon(player, pokemon);
                  });
               }
            } else {
               relearner = EntityNPC.locateNPCServer(player.field_70170_p, message.npcId, NPCTutor.class);
               relearner.ifPresent((it) -> {
                  it.handlePickedPokemon(player, pokemon);
               });
            }

         }
      }
   }

   public static enum Mode {
      Tutor,
      Transfer_Tutor,
      Relearner,
      Revive,
      Custom;

      private static final Mode[] VALUES = values();

      public static Mode fromOrdinal(int index) {
         return VALUES[index];
      }
   }
}
