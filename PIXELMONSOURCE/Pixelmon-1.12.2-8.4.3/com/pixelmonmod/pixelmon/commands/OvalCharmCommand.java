package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class OvalCharmCommand extends PixelmonCommand {
   public OvalCharmCommand() {
      super("ovalcharm", "/ovalcharm [player]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length <= 2) {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = args.length == 1 ? requireEntityPlayer(args[0]) : requireEntityPlayer(sender);
         PlayerPartyStorage party = getPlayerStorage(player);
         EnumFeatureState newState = args.length == 2 && args[1].equalsIgnoreCase("remove") ? EnumFeatureState.Disabled : EnumFeatureState.Active;
         party.setOvalCharm(newState);
         if (sender != player) {
            this.sendMessage(sender, "pixelmon.command.ovalcharm.success", new Object[]{player.func_70005_c_()});
         }

         this.sendMessage(player, "pixelmon.command.ovalcharm.received", new Object[0]);
         func_152374_a(sender, this, 0, "pixelmon.command.ovalcharm.notify", new Object[]{sender.func_70005_c_(), player.func_70005_c_()});
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         return tabCompleteUsernames(args);
      } else {
         return args.length == 2 ? tabComplete(args, new String[]{"remove", ""}) : tabComplete(args, new String[0]);
      }
   }
}
