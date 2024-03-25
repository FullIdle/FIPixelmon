package com.pixelmonmod.pixelmon.commands.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CompleteAllQuestsCommand extends PixelmonCommand {
   static final String OTHER_NODE = "pixelmon.command.admin.completeallquests.other";

   public CompleteAllQuestsCommand() {
      super("completeallquests", "/completeallquests [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.execute(sender, new String[]{sender.func_70005_c_()});
      } else if (args.length == 1) {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         if (sender != player && !sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.completeallquests.other")) {
            endCommand("pixelmon.command.completeallquests.permissionother", new Object[0]);
         }

         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         QuestData data = party.getQuestData();
         Iterator var6 = data.getProgress().iterator();

         while(var6.hasNext()) {
            QuestProgress progress = (QuestProgress)var6.next();
            progress.reopen();
            progress.setStage((short)-1);
            progress.complete(player);
            progress.sendTo(player);
         }

         func_152374_a(sender, this, 0, "pixelmon.command.completeallquests.notify", new Object[]{sender.func_70005_c_(), player.getDisplayNameString()});
         this.sendMessage(sender, "pixelmon.command.completeallquests", new Object[]{player.getDisplayNameString()});
      } else {
         this.sendMessage(sender, "pixelmon.command.completeallquests.invalidargs", new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
