package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class Stats extends PixelmonCommand {
   public String func_71517_b() {
      return "pokestats";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/pokestats <me:player>";
   }

   public int func_82362_a() {
      return 0;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP target = null;
      if (args.length != 0 && !args[0].equalsIgnoreCase("me")) {
         target = getPlayer(args[0]);
         if (target == null) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]);
            return;
         }

         this.sendMessage(sender, target.getDisplayNameString() + I18n.func_74838_a("pixelmon.command.stats.playerstats"), new Object[0]);
      } else if (sender instanceof EntityPlayerMP) {
         target = (EntityPlayerMP)sender;
         this.sendMessage(sender, "pixelmon.command.stats.yourstats", new Object[0]);
      } else {
         this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]);
      }

      PlayerPartyStorage party = Pixelmon.storageManager.getParty(target);
      PlayerStats stats = party.stats;
      this.sendMessage(sender, TextFormatting.GREEN, I18n.func_74838_a("pixelmon.command.stats.wins") + " " + stats.getWins(), new Object[0]);
      this.sendMessage(sender, TextFormatting.RED, I18n.func_74838_a("pixelmon.command.stats.losses") + " " + stats.getLosses(), new Object[0]);
   }
}
