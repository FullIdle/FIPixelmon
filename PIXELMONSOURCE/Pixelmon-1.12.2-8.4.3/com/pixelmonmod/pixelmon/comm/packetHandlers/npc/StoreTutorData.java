package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLearnType;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreTutorData implements IMessage {
   int npcId;
   List learnableMoves;
   EnumSet learnTypes = EnumSet.noneOf(BaseStatsLearnType.class);

   public StoreTutorData() {
   }

   public StoreTutorData(int npcId, List learnableMoves, EnumSet learnTypes) {
      this.npcId = npcId;
      this.learnableMoves = learnableMoves;
      this.learnTypes = learnTypes;
   }

   public void fromBytes(ByteBuf buf) {
      this.npcId = buf.readInt();
      int count = buf.readInt();
      this.learnableMoves = new ArrayList(count);

      int i;
      for(i = 0; i < count; ++i) {
         NPCTutor.LearnableMove move = new NPCTutor.LearnableMove();
         move.fromBytes(buf);
         this.learnableMoves.add(move);
      }

      count = buf.readInt();
      this.learnTypes.clear();

      for(i = 0; i < count; ++i) {
         BaseStatsLearnType type = BaseStatsLearnType.fromOrdinal(buf.readByte());
         if (type != null) {
            this.learnTypes.add(type);
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.npcId);
      buf.writeInt(this.learnableMoves.size());
      Iterator var2 = this.learnableMoves.iterator();

      while(var2.hasNext()) {
         NPCTutor.LearnableMove learnableMove = (NPCTutor.LearnableMove)var2.next();
         learnableMove.toBytes(buf);
      }

      buf.writeInt(this.learnTypes.size());
      var2 = this.learnTypes.iterator();

      while(var2.hasNext()) {
         BaseStatsLearnType type = (BaseStatsLearnType)var2.next();
         buf.writeByte(type.ordinal());
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StoreTutorData message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(player)) {
            return null;
         } else {
            Optional optional = EntityNPC.locateNPCServer(player.field_70170_p, message.npcId, NPCTutor.class);
            if (optional.isPresent()) {
               NPCTutor tutor = (NPCTutor)optional.get();
               tutor.moveList.clear();
               tutor.moveList.addAll(message.learnableMoves);
               tutor.learnTypes.clear();
               tutor.learnTypes.addAll(message.learnTypes);
            }

            return null;
         }
      }
   }
}
