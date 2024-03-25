package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CheckSpawnsCommand extends PixelmonCommand {
   public CheckSpawnsCommand() {
      super("checkspawns", "/checkspawns [specific] [pokemon...]", 2);
   }

   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      List arguments = new ArrayList();
      AbstractSpawner spawner = null;
      String[] var5 = args;
      int var6 = args.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String arg = var5[var7];
         arguments.add(arg);
         if (spawner == null) {
            spawner = PixelmonSpawning.coordinator.getSpawner(arg);
         }
      }

      if (spawner == null) {
         if (sender instanceof EntityPlayerMP) {
            spawner = PixelmonSpawning.coordinator.getSpawner(sender.func_70005_c_());
         } else {
            endCommand("spawning.error.mustbeplayer", new Object[0]);
         }
      }

      AbstractSpawner fSpawner = (AbstractSpawner)require(spawner, "pixelmon.general.error", new Object[0]);
      if (!sender.func_70003_b(2, fSpawner.checkSpawns.getPermissionNode())) {
         endCommand("pixelmon.general.needop", new Object[0]);
      }

      SpawnerCoordinator.PROCESSOR.execute(() -> {
         fSpawner.checkSpawns.checkSpawns(fSpawner, sender, arguments);
      });
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (PixelmonSpawning.coordinator != null) {
         List list = new ArrayList();
         PixelmonSpawning.coordinator.spawners.forEach((s) -> {
            list.add(s.name);
         });
         return args.length == 1 ? tabComplete(args, list) : tabComplete(args, new String[0]);
      } else {
         return tabComplete(args, new String[0]);
      }
   }
}
