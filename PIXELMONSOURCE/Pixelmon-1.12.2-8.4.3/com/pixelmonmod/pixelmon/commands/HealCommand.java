package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class HealCommand extends PixelmonCommand {
   static final String OTHER_NODE = "pixelmon.command.admin.healother";

   public HealCommand() {
      super("pokeheal", "/pokeheal <player..>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.execute(sender, new String[]{sender.func_70005_c_()});
      } else if (args.length > 1) {
         String[] var3 = args;
         int var4 = args.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String player = var3[var5];
            this.execute(sender, new String[]{player});
         }
      } else {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         if (sender != player && !sender.func_70003_b(this.func_82362_a(), "pixelmon.command.admin.healother")) {
            endCommand("pixelmon.command.heal.permissionother", new Object[0]);
         }

         if (BattleRegistry.getBattle(player) != null) {
            endCommand("pixelmon.command.heal.cantheal", new Object[]{player.getDisplayNameString()});
         }

         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         party.heal();
         func_152374_a(sender, this, 0, "pixelmon.command.heal.notifyhealed", new Object[]{sender.func_70005_c_(), player.getDisplayNameString()});
         this.sendMessage(sender, "pixelmon.command.heal.healed", new Object[]{player.getDisplayNameString()});
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}
