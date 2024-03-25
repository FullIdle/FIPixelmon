package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class MegaRingCommand extends PixelmonCommand {
   public MegaRingCommand() {
      super("megaring", "/megaring [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      this.resendWithMultipleTargets(sender, args, 0);
      EntityPlayerMP player = args.length == 1 ? requireEntityPlayer(args[0]) : requireEntityPlayer(sender);
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      if (!party.getMegaItemsUnlocked().canMega()) {
         party.setMegaItem(EnumMegaItem.BraceletORAS, false);
         party.unlockMega();
         if (sender != player) {
            this.sendMessage(sender, "pixelmon.command.megaring.success", new Object[]{player.func_70005_c_()});
         }

         this.sendMessage(player, "pixelmon.command.megaring.received", new Object[0]);
         func_152374_a(sender, this, 0, "pixelmon.command.megaring.notify", new Object[]{sender.func_70005_c_(), player.func_70005_c_()});
      } else {
         this.sendMessage(sender, "pixelmon.command.megaring.alreadyhas", new Object[]{player.func_70005_c_()});
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
