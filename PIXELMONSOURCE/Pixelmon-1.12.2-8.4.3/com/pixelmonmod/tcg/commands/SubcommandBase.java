package com.pixelmonmod.tcg.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

abstract class SubcommandBase {
   abstract String getHelp(ICommandSender var1);

   abstract String getPermissionNode();

   abstract int getPermissionLevel();

   protected abstract boolean execute(ICommandSender var1, String[] var2) throws CommandException;

   boolean canUse(ICommandSender sender) {
      String node = this.getPermissionNode();
      if (node == null) {
         node = "tcg.base";
      }

      return sender.func_70003_b(this.getPermissionLevel(), node);
   }

   final void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayer && !this.canUse(sender)) {
         sendMessage(sender, TextFormatting.RED, "You are not allowed to use this command!");
      } else {
         boolean result = this.execute(sender, args);
         if (!result) {
            String help = this.getHelp(sender);
            if (StringUtils.isNotBlank(help)) {
               sendMessage(sender, TextFormatting.RED, help);
            }
         }

      }
   }

   static final void sendMessage(ICommandSender receiver, TextFormatting format, String message) {
      ITextComponent chat = new TextComponentString(message);
      chat.func_150256_b().func_150238_a(format);
      receiver.func_145747_a(chat);
   }
}
