package com.pixelmonmod.pixelmon.comm;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.BattleMessageEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.tasks.BattleMessageTask;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ChatHandler {
   public static void sendChat(Entity entityLiving, String string, Object... data) {
      sendChat(entityLiving, getMessage(string, data));
   }

   public static void sendChat(ICommandSender receiver, TextComponentTranslation message) {
      if (receiver != null && receiver instanceof EntityPlayerMP) {
         receiver.func_145747_a(message);
      }

   }

   public static void sendChat(ICommandSender owner, ICommandSender owner2, String string, Object... data) {
      TextComponentTranslation textComponentTranslation = getMessage(string, data);
      sendChat(owner, textComponentTranslation);
      sendChat(owner2, textComponentTranslation);
   }

   public static void sendFormattedChat(ICommandSender receiver, TextFormatting chatFormat, String string, Object... data) {
      TextComponentTranslation textComponentTranslation = new TextComponentTranslation(string, data);
      textComponentTranslation.func_150256_b().func_150238_a(chatFormat);
      sendChat(receiver, textComponentTranslation);
   }

   public static void sendBattleMessage(Entity user, String string, Object... data) {
      TextComponentTranslation textComponentTranslation = new TextComponentTranslation(string, data);
      textComponentTranslation.func_150256_b().func_150238_a(TextFormatting.GRAY);
      sendBattleMessage(user, textComponentTranslation);
   }

   public static void sendBattleMessage(Entity user, TextComponentTranslation chat) {
      if (chat.func_150268_i() != null && !chat.func_150268_i().isEmpty()) {
         if (user instanceof EntityPlayerMP) {
            Pixelmon.network.sendTo(new BattleMessageTask(chat), (EntityPlayerMP)user);
         }

         Pixelmon.EVENT_BUS.post(new BattleMessageEvent(user, chat));
      }

   }

   public static void sendBattleMessage(ArrayList participants, String string, Object... data) {
      sendBattleMessage(participants, getMessage(string, data));
   }

   public static void sendBattleMessage(ArrayList participants, TextComponentTranslation message) {
      Iterator var2 = participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         sendBattleMessage((Entity)p.getEntity(), message);
      }

      ArrayList spectators = ((BattleParticipant)participants.get(0)).bc.spectators;
      spectators.forEach((spectator) -> {
         spectator.sendBattleMessage(message);
      });
   }

   public static TextComponentTranslation getMessage(String string, Object... data) {
      TextComponentTranslation message = new TextComponentTranslation(string, data);
      message.func_150256_b().func_150238_a(TextFormatting.GRAY);
      return message;
   }

   public static void sendMessageToAllPlayers(MinecraftServer minecraftServer, String string) {
      TextComponentTranslation translation = new TextComponentTranslation(string, new Object[0]);
      minecraftServer.func_184103_al().func_148539_a(translation);
   }
}
