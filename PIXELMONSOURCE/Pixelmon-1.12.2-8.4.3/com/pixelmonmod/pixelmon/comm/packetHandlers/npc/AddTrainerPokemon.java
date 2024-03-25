package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.ChangePokemonOpenGUI;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AddTrainerPokemon implements IMessage {
   int trainerID;

   public AddTrainerPokemon() {
   }

   public AddTrainerPokemon(int trainerId) {
      this.trainerID = trainerId;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.trainerID);
   }

   public void fromBytes(ByteBuf buffer) {
      this.trainerID = buffer.readInt();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(AddTrainerPokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(player)) {
            return null;
         } else {
            Optional entityNPCOptional = EntityNPC.locateNPCServer(player.field_70170_p, message.trainerID, NPCTrainer.class);
            if (!entityNPCOptional.isPresent()) {
               return null;
            } else {
               NPCTrainer t = (NPCTrainer)entityNPCOptional.get();
               if (t != null) {
                  TrainerPartyStorage storage = t.getPokemonStorage();
                  if (storage.hasSpace()) {
                     storage.add(Pixelmon.pokemonFactory.create(EnumSpecies.Bulbasaur));
                  }

                  t.updateLvl();
                  t.getPokemonStorage().sendCacheToPlayer(player);
                  Pixelmon.network.sendTo(new ChangePokemonOpenGUI(storage.getTeam().size() - 1), player);
               }

               return null;
            }
         }
      }
   }
}
