package com.pixelmonmod.pixelmon.quests.comm.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.ItemQuestEditor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FetchQuestData implements IMessage {
   private boolean openGui;

   public FetchQuestData() {
   }

   public FetchQuestData(boolean openGui) {
      this.openGui = openGui;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.openGui);
   }

   public void fromBytes(ByteBuf buf) {
      this.openGui = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(FetchQuestData message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player != null && ItemQuestEditor.checkPermission(player)) {
            if (PixelmonConfig.useExternalJSONFilesQuests) {
               Pixelmon.network.sendTo(new PullQuestData(), player);
               if (message.openGui) {
                  OpenScreen.open(player, EnumGuiScreen.QuestEditor);
               }
            } else {
               player.func_145747_a(new TextComponentString(TextFormatting.RED + I18n.func_74838_a("item.quest_editor.cantuse")));
            }
         }

      }
   }
}
