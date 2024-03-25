package com.pixelmonmod.pixelmon.commands.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class QuestDataCommand extends PixelmonCommand {
   public QuestDataCommand() {
      super("questdata", "/questdata <quest> {numeric,literal} <key> [value]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 3 && args.length != 4) {
         this.sendMessage(sender, "pixelmon.command.questdata.invalidargs", new Object[0]);
      } else {
         EntityPlayerMP player = requireEntityPlayer(sender.func_70005_c_());
         boolean set = args.length == 4;
         if (!args[1].equalsIgnoreCase("literal") && !args[1].equalsIgnoreCase("numeric")) {
            this.sendMessage(sender, "pixelmon.command.questdata.invalidargs", new Object[0]);
            return;
         }

         boolean numeric = args[1].equalsIgnoreCase("numeric");
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         QuestData data = party.getQuestData();
         QuestProgress progress = (QuestProgress)require(data.getProgressForQuest(args[0], true), "pixelmon.command.invalidquest", new Object[0]);
         if (set) {
            if (numeric) {
               progress.setData(args[2], (long)requireInt(args[3], "commands.generic.num.invalid"));
            } else {
               progress.setData(args[2], args[3]);
            }

            progress.sendTo(player);
            func_152374_a(sender, this, 0, "pixelmon.command.questdata.set.notify", new Object[]{sender.func_70005_c_(), args[1], args[2], args[3], args[0].replace("_", " ")});
            this.sendMessage(sender, "pixelmon.command.questdata.set", new Object[]{args[1], args[2], args[3], args[0].replace("_", " ")});
         } else {
            Object result;
            if (numeric) {
               result = progress.getDataLong(args[2]);
            } else {
               result = progress.getDataString(args[2]);
            }

            func_152374_a(sender, this, 0, "pixelmon.command.questdata.get.notify", new Object[]{sender.func_70005_c_(), args[1], args[2], args[0].replace("_", " ")});
            this.sendMessage(sender, "pixelmon.command.questdata.get", new Object[]{args[1], args[2], result, args[0].replace("_", " ")});
         }
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      switch (args.length) {
         case 1:
            return tabComplete(args, QuestRegistry.getInstance().getQuestFilepaths(true));
         case 2:
            return Arrays.asList("numeric", "literal");
         default:
            return tabComplete(args, new String[0]);
      }
   }
}
