package com.pixelmonmod.pixelmon.commands.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.Iterator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class ReloadQuestsCommand extends PixelmonCommand {
   public ReloadQuestsCommand() {
      super("reloadquests", "/reloadquests", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      try {
         QuestRegistry.registerQuests(false);
         Iterator var3 = getServer().func_184103_al().func_181057_v().iterator();

         while(var3.hasNext()) {
            EntityPlayerMP playerMP = (EntityPlayerMP)var3.next();
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(playerMP);
            QuestData data = pps.getQuestData();
            Iterator var7 = data.getProgress().iterator();

            while(var7.hasNext()) {
               QuestProgress progress = (QuestProgress)var7.next();
               progress.invalidate();
               progress.getQuest();
               progress.sendTo(playerMP);
            }
         }

         func_152374_a(sender, this, 0, "pixelmon.command.reloadquests.notify", new Object[]{sender.func_70005_c_()});
         this.sendMessage(sender, "pixelmon.command.reloadquests", new Object[0]);
      } catch (Exception var9) {
         Pixelmon.LOGGER.error(var9.toString());
         this.sendMessage(sender, "pixelmon.command.reloadquests.failed", new Object[0]);
      }

   }
}
