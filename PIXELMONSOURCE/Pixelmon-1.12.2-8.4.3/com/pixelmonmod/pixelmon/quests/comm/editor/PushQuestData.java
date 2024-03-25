package com.pixelmonmod.pixelmon.quests.comm.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.ItemQuestEditor;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PushQuestData implements IMessage {
   private String oldName;
   private Quest quest;
   private boolean delete;
   private boolean openUI;

   public PushQuestData() {
   }

   public PushQuestData(String oldName, Quest quest, boolean openUI) {
      this(oldName, quest, false, openUI);
   }

   public PushQuestData(String oldName, Quest quest, boolean delete, boolean openUI) {
      this.oldName = oldName;
      this.quest = quest;
      this.delete = delete;
      this.openUI = openUI;
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.oldName);
      this.quest.writeToByteBuf(buf);
      buf.writeBoolean(this.delete);
      buf.writeBoolean(this.openUI);
   }

   public void fromBytes(ByteBuf buf) {
      this.oldName = ByteBufUtils.readUTF8String(buf);
      this.quest = new Quest(buf);
      this.delete = buf.readBoolean();
      this.openUI = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(PushQuestData message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player != null && ItemQuestEditor.checkPermission(player) && PixelmonConfig.useExternalJSONFilesQuests) {
            QuestRegistry.getInstance().deleteQuest(message.quest);
            Quest oldQuest = QuestRegistry.getInstance().getQuest(message.oldName);
            if (oldQuest != null) {
               QuestRegistry.getInstance().deleteQuest(oldQuest);
            }

            if (!message.delete) {
               QuestRegistry.getInstance().createOrUpdateQuest(message.quest);
            }

            Pixelmon.network.sendTo(new PullQuestData(), player);
            if (message.openUI) {
               OpenScreen.open(player, EnumGuiScreen.QuestEditor);
            }
         }

      }
   }
}
