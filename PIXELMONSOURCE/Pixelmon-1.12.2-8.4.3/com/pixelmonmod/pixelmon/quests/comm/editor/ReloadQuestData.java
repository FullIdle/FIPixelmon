package com.pixelmonmod.pixelmon.quests.comm.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.items.ItemQuestEditor;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.comm.ResetQuests;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReloadQuestData implements IMessage {
   public void toBytes(ByteBuf buf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ReloadQuestData message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player != null && ItemQuestEditor.checkPermission(player) && PixelmonConfig.useExternalJSONFilesQuests) {
            try {
               QuestRegistry.registerQuests(false);
               Iterator var4 = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_181057_v().iterator();

               while(var4.hasNext()) {
                  EntityPlayerMP playerMP = (EntityPlayerMP)var4.next();
                  Pixelmon.network.sendTo(new ResetQuests(), playerMP);
                  PlayerPartyStorage pps = Pixelmon.storageManager.getParty(playerMP);
                  QuestData data = pps.getQuestData();
                  Iterator var8 = data.getProgress().iterator();

                  while(var8.hasNext()) {
                     QuestProgress progress = (QuestProgress)var8.next();
                     progress.invalidate();
                     progress.getQuest();
                     progress.sendTo(playerMP);
                  }
               }
            } catch (Exception var10) {
               Pixelmon.LOGGER.error(var10.toString());
            }
         }

      }
   }
}
