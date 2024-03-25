package com.pixelmonmod.pixelmon.comm;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandChatHandler {
   public static void sendChat(ICommandSender commandSender, String string, Object... data) {
      TextComponentTranslation chatTranslation = new TextComponentTranslation(string, data);
      chatTranslation.func_150256_b().func_150238_a(TextFormatting.GRAY);
      commandSender.func_145747_a(chatTranslation);
   }

   public static void sendFormattedChat(ICommandSender commandSender, TextFormatting format, String string, Object... data) {
      TextComponentTranslation chatTranslation = new TextComponentTranslation(string, data);
      chatTranslation.func_150256_b().func_150238_a(format);
      commandSender.func_145747_a(chatTranslation);
   }
}
