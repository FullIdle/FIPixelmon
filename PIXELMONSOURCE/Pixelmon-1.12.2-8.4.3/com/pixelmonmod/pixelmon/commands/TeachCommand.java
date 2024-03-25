package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

public class TeachCommand extends PixelmonCommand {
   public TeachCommand() {
      super("teach", "/teach <player> <slot> <move>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         this.sendMessage(sender, TextFormatting.RED, this.func_71518_a(sender), new Object[0]);
      } else if (args.length == 1) {
         this.execute(sender, new String[]{sender.func_70005_c_(), "1", args[0]});
      } else if (args.length == 2) {
         this.execute(sender, new String[]{sender.func_70005_c_(), args[0], args[1]});
      } else {
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         if (BattleRegistry.getBattle(player) != null) {
            endCommand("pixelmon.command.general.inbattle", new Object[0]);
         }

         if (party.guiOpened) {
            endCommand("pixelmon.command.teach.busy", new Object[]{player.getDisplayNameString()});
         }

         int slot = requireInt(args[1], 1, 6, "pixelmon.command.teach.slot", new Object[0]);
         Pokemon pokemon = (Pokemon)require(party.get(slot - 1), "pixelmon.command.teach.nothing", new Object[]{player.getDisplayNameString()});
         String attackName = StringUtils.join(args, " ", 2, args.length);
         attackName = attackName.replace('_', ' ');
         Attack attack;
         if (Attack.hasAttack(attackName)) {
            attack = new Attack(attackName);
         } else {
            try {
               int attackIndex = requireInt(attackName, "");
               if (!Attack.hasAttack(attackIndex)) {
                  endCommand("pixelmon.command.teach.nomove", new Object[]{attackName});
                  return;
               }

               attack = new Attack(attackIndex);
            } catch (CommandException var10) {
               endCommand("pixelmon.command.teach.nomove", new Object[]{attackName});
               return;
            }
         }

         if (attack.getMove().getAttackId() >= 10000 || attack.getMove().getAttackId() < 0) {
            ChatHandler.sendChat(player, "pixelmon.interaction.tmcantlearn", pokemon.getDisplayName(), attack.getMove().getTranslatedName());
            return;
         }

         if (!pokemon.getMoveset().hasAttack(attack)) {
            if (pokemon.getMoveset().size() >= 4) {
               LearnMoveController.sendLearnMove(player, pokemon.getUUID(), attack.getActualMove());
               this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.teach.sentmove", new Object[]{player.getDisplayNameString(), pokemon.getDisplayName(), attack.getActualMove().getTranslatedName()});
            } else {
               pokemon.getMoveset().add(attack);
               this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.stats.learnedmove", new Object[]{pokemon.getDisplayName(), attack.getActualMove().getTranslatedName()});
            }
         } else {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.teach.knowsmove", new Object[]{player.getDisplayNameString(), pokemon.getDisplayName(), attack.getActualMove().getTranslatedName()});
         }
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         return tabCompleteUsernames(args);
      } else if (args.length == 2) {
         return tabComplete(args, new String[]{"1", "2", "3", "4", "5", "6"});
      } else if (args.length == 3) {
         List list = new ArrayList(AttackBase.getAllAttackNames());
         list.replaceAll((s) -> {
            return s.replace(" ", "_");
         });
         return tabComplete(args, list);
      } else {
         return tabComplete(args, new String[0]);
      }
   }
}
