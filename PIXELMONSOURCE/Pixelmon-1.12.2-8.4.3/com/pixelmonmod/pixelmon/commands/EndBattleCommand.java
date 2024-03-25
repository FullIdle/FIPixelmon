package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.events.battles.ForceEndBattleEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EndSpectate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ExitBattle;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleForceEndCause;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class EndBattleCommand extends PixelmonCommand {
   public EndBattleCommand() {
      super("endbattle", "/endbattle [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = args.length == 1 ? getEntityPlayer(args[0]) : requireEntityPlayer(sender);
      if (player != null && player != sender && !sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.endbattle.ordinary")) {
         endCommand("pixelmon.command.endbattle.permissionother", new Object[0]);
      }

      if (player == null) {
         endCommand("commands.generic.player.notFound", new Object[]{args[0]});
      }

      BattleControllerBase bc = BattleRegistry.getBattle(player);
      if (bc != null) {
         boolean forceful = sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.endbattle.forceful");
         ForceEndBattleEvent event = new ForceEndBattleEvent(bc, forceful ? EnumBattleForceEndCause.ENDBATTLE_FORCEFUL : EnumBattleForceEndCause.ENDBATTLE);
         if (Pixelmon.EVENT_BUS.post(event) && !forceful) {
            sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.endbattle.failed", new Object[0]));
         } else if (bc.removeSpectator(player)) {
            Pixelmon.network.sendTo(new EndSpectate(), player);
         } else {
            bc.endBattle(EnumBattleEndCause.FORCE);
            sender.func_145747_a(format(TextFormatting.GREEN, "pixelmon.command.endbattle.endbattle", new Object[0]));
            BattleRegistry.deRegisterBattle(bc);
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.endbattle.notinbattle", new Object[]{player.func_70005_c_()}));
         Pixelmon.network.sendTo(new ExitBattle(), player);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
