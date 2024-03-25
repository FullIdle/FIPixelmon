package com.pixelmonmod.pixelmon.commands.econ;

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

public class BankTransferCommand extends PixelmonCommand {
   public BankTransferCommand() {
      super("transfer", "/transfer <player> <money>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 2) {
         EntityPlayerMP player = requireEntityPlayer(sender);
         EntityPlayerMP target = requireEntityPlayer(args[0]);
         if (player == target) {
            endCommand("pixelmon.command.transfer.sameplayer", new Object[0]);
         }

         int amount = requireInt(args[1], 1, 999999, "commands.generic.num.invalid", new Object[0]);
         IPixelmonBankAccount userAccount = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(player).orElseThrow(() -> {
            return new NullPointerException("bank account");
         });
         IPixelmonBankAccount targetAccount = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(target).orElseThrow(() -> {
            return new NullPointerException("bank account");
         });
         if (userAccount.getMoney() < amount) {
            endCommand("pixelmon.command.transfer.notenoughmoney", new Object[0]);
         }

         int beforeCurrency = targetAccount.getMoney();
         int currencyDifference = targetAccount.changeMoney(amount) - beforeCurrency;
         if (currencyDifference == 0) {
            if (amount > 0) {
               this.sendMessage(sender, "pixelmon.command.givemoney.moneylimit", new Object[]{target.func_145748_c_()});
            } else {
               this.sendMessage(sender, "pixelmon.command.givemoney.nomoney", new Object[]{target.func_145748_c_()});
            }

            targetAccount.setMoney(beforeCurrency);
         } else {
            userAccount.changeMoney(-currencyDifference);
            String currencyString = Integer.toString(currencyDifference);
            func_152374_a(sender, this, 0, "pixelmon.command.transfer.notifytransfer", new Object[]{sender.func_70005_c_(), currencyString, target.func_145748_c_()});
            this.sendMessage(sender, "pixelmon.command.transfer.transferred", new Object[]{currencyString, target.func_145748_c_()});
            this.sendMessage(target, "pixelmon.command.transfer.received", new Object[]{player.func_145748_c_(), currencyString});
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length != 1 ? tabComplete(args, new String[0]) : tabCompleteUsernames(args);
   }
}
