package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BattleLogCommand extends PixelmonCommand {
   public BattleLogCommand() {
      super("battlelog", "/battlelog [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         if (!(sender instanceof EntityPlayerMP)) {
            this.sendMessage(sender, "pixelmon.command.battlelog.not.player", new Object[0]);
         } else {
            BattleControllerBase battle = BattleRegistry.getBattle((EntityPlayerMP)sender);
            if (battle == null) {
               this.sendMessage(sender, "pixelmon.command.battlelog.not.battling", new Object[0]);
            } else {
               try {
                  battle.battleLog.exportLogFile();
                  this.sendMessage(sender, "pixelmon.command.battlelog.logged", new Object[0]);
               } catch (Exception var6) {
                  this.sendMessage(sender, "pixelmon.command.battlelog.failed", new Object[0]);
                  var6.printStackTrace();
               }

            }
         }
      } else {
         EntityPlayerMP target = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_152612_a(args[0]);
         if (target == null) {
            this.sendMessage(sender, "pixelmon.command.battlelog.player.not.found", new Object[0]);
         } else {
            BattleControllerBase battle = BattleRegistry.getBattle(target);
            if (battle == null) {
               this.sendMessage(sender, "pixelmon.command.battlelog.not.battling", new Object[0]);
            } else {
               try {
                  battle.battleLog.exportLogFile();
                  this.sendMessage(sender, "pixelmon.command.battlelog.logged", new Object[0]);
               } catch (Exception var7) {
                  this.sendMessage(sender, "pixelmon.command.battlelog.failed", new Object[0]);
                  var7.printStackTrace();
               }

            }
         }
      }
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return tabCompleteUsernames(args);
   }
}
