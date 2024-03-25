package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RandomiseTrainerPokemon implements IMessage {
   int trainerId;

   public RandomiseTrainerPokemon() {
   }

   public RandomiseTrainerPokemon(int trainerId) {
      this.trainerId = trainerId;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.trainerId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.trainerId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RandomiseTrainerPokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(player)) {
            return null;
         } else {
            Optional trainer = EntityNPC.locateNPCServer(player.field_70170_p, message.trainerId, NPCTrainer.class);
            if (trainer.isPresent()) {
               ((NPCTrainer)trainer.get()).randomisePokemon(player);
            }

            return null;
         }
      }
   }
}
