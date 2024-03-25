package com.pixelmonmod.pixelmon.quests.comm.editor;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.client.editor.QuestEditorState;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PullQuestData implements IMessage {
   private ArrayList quests = new ArrayList(QuestRegistry.getInstance().getQuestCollection());
   private ArrayList elements = QuestRegistry.getInstance().getQuestElements();

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.quests.size());
      Iterator var2 = this.quests.iterator();

      while(var2.hasNext()) {
         Quest quest = (Quest)var2.next();
         quest.writeToByteBuf(buf);
      }

      buf.writeShort(this.elements.size());
      var2 = this.elements.iterator();

      while(var2.hasNext()) {
         QuestElement element = (QuestElement)var2.next();
         element.writeToByteBuf(buf);
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.quests = new ArrayList();
      int questCount = buf.readShort();

      for(int i = 0; i < questCount; ++i) {
         this.quests.add(new Quest(buf));
      }

      this.elements = new ArrayList();
      int elementCount = buf.readShort();

      for(int i = 0; i < elementCount; ++i) {
         this.elements.add(new QuestElement(buf));
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(PullQuestData message, MessageContext ctx) {
         QuestEditorState.get().setQuests(message.quests, message.elements);
      }
   }
}
