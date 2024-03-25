package com.pixelmonmod.pixelmon.commands.econ;

import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class GiveMoneyCommand extends PixelmonCommand {
   public GiveMoneyCommand() {
      super("givemoney", "/givemoney <player> <money>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 2) {
         this.resendWithMultipleTargets(sender, args, 0);
         GameProfile target = findProfile(args[0]);
         if (target == null) {
            endCommand("commands.generic.player.notFound", new Object[]{args[0]});
         }

         int amount = requireInt(args[1], -999999, 999999, "commands.generic.num.invalid", new Object[0]);
         IPixelmonBankAccount targetAccount = (IPixelmonBankAccount)require(Pixelmon.moneyManager.getBankAccount(target.getId()), "pixelmon.command.general.invalidplayer", new Object[0]);
         int beforeCurrency = targetAccount.getMoney();
         int currencyDifference = targetAccount.changeMoney(amount) - beforeCurrency;
         if (currencyDifference == 0) {
            if (amount > 0) {
               this.sendMessage(sender, "pixelmon.command.givemoney.moneylimit", new Object[]{target.getName()});
            } else {
               this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.givemoney.nomoney", new Object[]{target.getName()});
            }
         } else {
            String currencyString = Integer.toString(currencyDifference);
            func_152374_a(sender, this, 0, "pixelmon.command.givemoney.notifygive", new Object[]{sender.func_70005_c_(), currencyDifference, target.getName()});
            this.sendMessage(sender, "pixelmon.command.givemoney.given", new Object[]{currencyString, target.getName()});
            EntityPlayerMP player = getEntityPlayer(target.getId());
            if (player != null && player != sender) {
               this.sendMessage(player, TextFormatting.GRAY, "pixelmon.command.givemoney.received", new Object[]{sender.func_145748_c_(), currencyString});
            }
         }
      } else if (args.length == 1) {
         this.execute(sender, new String[]{sender.func_70005_c_(), args[0]});
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length != 1 ? tabComplete(args, new String[0]) : tabCompleteUsernames(args);
   }
}
