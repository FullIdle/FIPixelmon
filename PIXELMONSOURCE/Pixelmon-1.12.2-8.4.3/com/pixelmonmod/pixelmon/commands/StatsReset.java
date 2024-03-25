package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class StatsReset extends PixelmonCommand {
   public String func_71517_b() {
      return "resetpokestats";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/resetpokestats <player>";
   }

   public int func_82362_a() {
      return 2;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 1) {
         this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
      } else {
         EntityPlayerMP player = getPlayer(args[0]);
         if (player == null) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]);
         } else {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            PlayerStats stats = party.stats;
            stats.resetWinLoss();
            this.sendMessage(sender, player.func_145748_c_().func_150260_c() + I18n.func_74838_a("pixelmon.command.statsreset.reset"), new Object[0]);
         }

      }
   }
}
