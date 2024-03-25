package com.pixelmonmod.pixelmon.commands.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class ResetAllQuestsCommand extends PixelmonCommand {
   static final String OTHER_NODE = "pixelmon.command.admin.resetallquests.other";

   public ResetAllQuestsCommand() {
      super("resetallquests", "/resetallquests [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.execute(sender, new String[]{sender.func_70005_c_()});
      } else if (args.length == 1) {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         if (sender != player && !sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.resetallquests.other")) {
            endCommand("pixelmon.command.resetallquests.permissionother", new Object[0]);
         }

         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         party.resetQuestData();
         func_152374_a(sender, this, 0, "pixelmon.command.resetallquests.notify", new Object[]{sender.func_70005_c_(), player.getDisplayNameString()});
         this.sendMessage(sender, "pixelmon.command.resetallquests", new Object[]{player.getDisplayNameString()});
      } else {
         this.sendMessage(sender, "pixelmon.command.resetallquests.invalidargs", new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
