package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.client.gui.npc.GuiTutor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTutorEditor;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLearnType;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoadTutorData implements IMessage {
   List learnableMoves;
   EnumSet learnTypes = EnumSet.noneOf(BaseStatsLearnType.class);

   public LoadTutorData() {
   }

   public LoadTutorData(List learnableMoves) {
      this.learnableMoves = learnableMoves;
   }

   public LoadTutorData(List learnableMoves, EnumSet learnTypes) {
      this.learnableMoves = learnableMoves;
      this.learnTypes = learnTypes;
   }

   public void fromBytes(ByteBuf buf) {
      int count = buf.readInt();
      this.learnableMoves = new ArrayList();

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
      public IMessage onMessage(LoadTutorData message, MessageContext ctx) {
         GuiTutor.moveList = message.learnableMoves;
         GuiTutorEditor.learnTypes = message.learnTypes;
         return null;
      }
   }
}
