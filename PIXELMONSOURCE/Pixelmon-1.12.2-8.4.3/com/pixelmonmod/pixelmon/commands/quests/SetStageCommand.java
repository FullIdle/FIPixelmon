package com.pixelmonmod.pixelmon.commands.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class SetStageCommand extends PixelmonCommand {
   static final String OTHER_NODE = "pixelmon.command.admin.setstage.other";

   public SetStageCommand() {
      super("setstage", "/setstage <quest> <stage> [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 2) {
         this.execute(sender, new String[]{args[0], args[1], sender.func_70005_c_()});
      } else if (args.length == 3) {
         this.resendWithMultipleTargets(sender, args, 2);
         EntityPlayerMP player = requireEntityPlayer(args[2]);
         short stage = (short)requireInt(args[1], -32768, 32767, "commands.generic.num.invalid", new Object[0]);
         if (sender != player && !sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.setstage.other")) {
            endCommand("pixelmon.command.setstage.permissionother", new Object[0]);
         }

         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         QuestData data = party.getQuestData();
         QuestProgress progress = (QuestProgress)require(data.getProgressForQuest(args[0], true), "pixelmon.command.invalidquest", new Object[0]);
         if (stage == -1) {
            progress.complete(player);
         } else {
            if (!progress.setStage(stage)) {
               this.sendMessage(sender, "pixelmon.command.setstage.invalidstage", new Object[]{args[1], args[0].replace("_", " ")});
               return;
            }

            progress.reopen();
         }

         progress.sendTo(player);
         func_152374_a(sender, this, 0, "pixelmon.command.setstage.notify", new Object[]{sender.func_70005_c_(), player.getDisplayNameString(), args[0].replace("_", " "), args[1]});
         this.sendMessage(sender, "pixelmon.command.setstage.set", new Object[]{player.getDisplayNameString(), args[0].replace("_", " "), args[1]});
      } else {
         this.sendMessage(sender, "pixelmon.command.setstage.invalidargs", new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      switch (args.length) {
         case 1:
            return tabComplete(args, QuestRegistry.getInstance().getQuestFilepaths(true));
         case 3:
            return tabCompleteUsernames(args);
         default:
            return tabComplete(args, new String[0]);
      }
   }
}
