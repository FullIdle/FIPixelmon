package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeleteTrainerPokemon implements IMessage {
   int trainerID;
   int position;

   public DeleteTrainerPokemon() {
   }

   public DeleteTrainerPokemon(int trainerID, int position) {
      this.trainerID = trainerID;
      this.position = position;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.trainerID);
      buffer.writeInt(this.position);
   }

   public void fromBytes(ByteBuf buffer) {
      this.trainerID = buffer.readInt();
      this.position = buffer.readInt();
   }

   public static void deletePokemon(int trainerID, int position, MessageContext ctx, boolean updateClient) {
      EntityPlayerMP p = ctx.getServerHandler().field_147369_b;
      Optional entityNPCOptional = EntityNPC.locateNPCServer(p.field_70170_p, trainerID, NPCTrainer.class);
      if (entityNPCOptional.isPresent()) {
         NPCTrainer t = (NPCTrainer)entityNPCOptional.get();
         TrainerPartyStorage storage = t.getPokemonStorage();
         if (storage.getTeam().size() > position) {
            Pokemon pokemon = (Pokemon)storage.getTeam().get(position);

            try {
               if (storage.countAll() > 1) {
                  storage.set(pokemon.getPosition().order, (Pokemon)null);
               }

               t.updateLvl();
               if (updateClient) {
                  storage.sendCacheToPlayer(ctx.getServerHandler().field_147369_b);
               }
            } catch (Exception var10) {
               var10.printStackTrace();
            }

         }
      }
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DeleteTrainerPokemon message, MessageContext ctx) {
         if (!ItemNPCEditor.checkPermission(ctx.getServerHandler().field_147369_b)) {
            return null;
         } else {
            DeleteTrainerPokemon.deletePokemon(message.trainerID, message.position, ctx, true);
            return null;
         }
      }
   }
}
