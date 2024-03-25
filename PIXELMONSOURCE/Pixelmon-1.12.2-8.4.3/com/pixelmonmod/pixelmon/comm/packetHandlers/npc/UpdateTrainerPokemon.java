package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTrainerEditor;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdateEditedPokemon;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateTrainerPokemon extends UpdateEditedPokemon {
   int trainerID;

   public UpdateTrainerPokemon() {
   }

   public UpdateTrainerPokemon(Pokemon data) {
      super(data);
      this.trainerID = GuiTrainerEditor.currentTrainerID;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.trainerID);
      super.toBytes(buffer);
   }

   public void fromBytes(ByteBuf buffer) {
      this.trainerID = buffer.readInt();
      super.fromBytes(buffer);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateTrainerPokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (ItemNPCEditor.checkPermission(player)) {
            Optional entityNPCOptional = EntityNPC.locateNPCServer(player.field_70170_p, message.trainerID, NPCTrainer.class);
            if (entityNPCOptional.isPresent()) {
               NPCTrainer t = (NPCTrainer)entityNPCOptional.get();
               TrainerPartyStorage storage = t.getPokemonStorage();
               UpdateEditedPokemon.updatePokemon(message, player, storage);
               if (message.data != null) {
                  t.updateLvl();
                  storage.sendCacheToPlayer(player);
               }

            }
         }
      }
   }
}
