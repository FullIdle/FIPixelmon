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

public class DynamaxBandCommand extends PixelmonCommand {
   public DynamaxBandCommand() {
      super("dynamaxband", "/dynamaxband [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      this.resendWithMultipleTargets(sender, args, 0);
      EntityPlayerMP player = args.length == 1 ? requireEntityPlayer(args[0]) : requireEntityPlayer(sender);
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      if (!party.getMegaItemsUnlocked().canDynamax()) {
         party.setMegaItem(EnumMegaItem.DynamaxBand, false);
         party.unlockDynamax();
         if (sender != player) {
            this.sendMessage(sender, "pixelmon.command.dynamaxband.success", new Object[]{player.func_70005_c_()});
         }

         this.sendMessage(player, "pixelmon.command.dynamaxband.received", new Object[0]);
         func_152374_a(sender, this, 0, "pixelmon.command.dynamaxband.notify", new Object[]{sender.func_70005_c_(), player.func_70005_c_()});
      } else {
         this.sendMessage(sender, "pixelmon.command.dynamaxband.alreadyhas", new Object[]{player.func_70005_c_()});
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
