package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NPCTeachMove implements IMessage {
   UUID pokemonUUID;
   int attackId;
   int npcId;

   /** @deprecated */
   @Deprecated
   public NPCTeachMove() {
   }

   public NPCTeachMove(UUID pokemonUUID, int attackId, int npcId) {
      this.pokemonUUID = pokemonUUID;
      this.attackId = attackId;
      this.npcId = npcId;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.attackId = buffer.readInt();
      this.npcId = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeInt(this.attackId);
      buffer.writeInt(this.npcId);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(NPCTeachMove message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         Attack attack = new Attack(message.attackId);
         Pokemon pokemon = Pixelmon.storageManager.getParty(player).find(message.pokemonUUID);
         if (pokemon != null) {
            List costs = new ArrayList();
            Optional npc = EntityNPC.locateNPCServer(player.field_70170_p, message.npcId, EntityNPC.class);
            if (npc.isPresent() && (double)((EntityNPC)npc.get()).func_70032_d(player) <= 64.0) {
               if (npc.get() instanceof NPCRelearner) {
                  List attackList = NPCRelearner.getRelearnableMoves(pokemon);
                  if (!attackList.contains(attack)) {
                     return;
                  }

                  NPCRelearner relearner = (NPCRelearner)npc.get();
                  ItemStack cost = relearner.getCost();
                  if (!cost.func_190926_b()) {
                     ((List)costs).add(cost);
                  }
               } else {
                  if (!(npc.get() instanceof NPCTutor)) {
                     return;
                  }

                  NPCTutor tutor = (NPCTutor)npc.get();
                  if (tutor.moveList.stream().noneMatch((it) -> {
                     return it.attack() == attack.getMove();
                  })) {
                     return;
                  }

                  if (!pokemon.getBaseStats().canLearn(attack.getMove().getAttackName())) {
                     return;
                  }

                  for(int i = 0; i < tutor.moveList.size(); ++i) {
                     if (((NPCTutor.LearnableMove)tutor.moveList.get(i)).attack().isAttack(attack)) {
                        List array = ((NPCTutor.LearnableMove)tutor.moveList.get(i)).costs();
                        if (array.size() >= 1) {
                           costs = array;
                        }
                     }
                  }
               }

               if (!((List)costs).isEmpty() && !player.field_71075_bZ.field_75098_d) {
                  LearnMoveController.addLearnMove(player, pokemon.getUUID(), attack.getActualMove(), LearnMoveController.itemCostCondition((Collection)costs));
               } else {
                  LearnMoveController.addLearnMove(player, pokemon.getUUID(), attack.getActualMove());
               }

               if (pokemon.getMoveset().size() >= 4) {
                  Pixelmon.network.sendTo(new OpenReplaceMoveScreen(pokemon.getUUID(), attack.getActualMove()), player);
               } else if (LearnMoveController.canLearnMove(player, pokemon.getUUID(), attack.getActualMove())) {
                  pokemon.getMoveset().add(attack);
                  ChatHandler.sendChat(player, "pixelmon.stats.learnedmove", pokemon.getDisplayName(), attack.getMove().getTranslatedName());
               } else {
                  ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.npc.cantpay");
               }

            }
         }
      }
   }
}
