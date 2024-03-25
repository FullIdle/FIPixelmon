package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SpawningCommand extends PixelmonCommand {
   public SpawningCommand() {
      super("spawning", "/spawning {off|on|diagnose]", 4);
   }

   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         if (PixelmonSpawning.coordinator != null && PixelmonSpawning.coordinator.getActive()) {
            this.sendMessage(sender, "Spawner: On.", new Object[0]);
         } else {
            this.sendMessage(sender, "Spawner: Off", new Object[0]);
         }
      } else if (args[0].equalsIgnoreCase("off")) {
         if (PixelmonSpawning.coordinator != null) {
            PixelmonSpawning.coordinator.deactivate();
         }

         this.sendMessage(sender, "All spawning has been turned off.", new Object[0]);
      } else if (args[0].equalsIgnoreCase("on")) {
         if (PixelmonSpawning.coordinator != null) {
            PixelmonSpawning.coordinator.deactivate();
         }

         PixelmonSpawning.startTrackingSpawner();
         this.sendMessage(sender, "Better Spawning system has been engaged.", new Object[0]);
      } else if (args[0].equalsIgnoreCase("diagnose")) {
         boolean spawnerActive = PixelmonSpawning.coordinator != null && PixelmonSpawning.coordinator.getActive();
         this.sendMessage(sender, TextFormatting.GRAY + "Better Spawner: " + (spawnerActive ? TextFormatting.GREEN + "Active." : TextFormatting.RED + "Inactive."), new Object[0]);
         if (spawnerActive) {
            int spawnerCount = PixelmonSpawning.coordinator.spawners.size();
            this.sendMessage(sender, TextFormatting.GRAY + "Spawner count: " + TextFormatting.YELLOW + spawnerCount, new Object[0]);
            if (spawnerCount > 0) {
               ArrayList summaryMessages = new ArrayList();
               Iterator var6 = PixelmonSpawning.coordinator.spawners.iterator();

               label85:
               while(true) {
                  AbstractSpawner spawner;
                  do {
                     if (!var6.hasNext()) {
                        var6 = summaryMessages.iterator();

                        while(var6.hasNext()) {
                           String message = (String)var6.next();
                           sender.func_145747_a(new TextComponentString(message));
                        }
                        break label85;
                     }

                     spawner = (AbstractSpawner)var6.next();
                  } while(!(spawner instanceof PlayerTrackingSpawner));

                  PlayerTrackingSpawner pts = (PlayerTrackingSpawner)spawner;
                  EntityPlayerMP player = pts.getTrackedPlayer();
                  String message;
                  if (player == null) {
                     message = TextFormatting.RED + "OFFLINE PLAYER " + TextFormatting.GRAY + "(";
                  } else {
                     message = TextFormatting.YELLOW + player.func_70005_c_() + " (";
                  }

                  double percentage = (double)pts.spawnedTracker.count() / (double)pts.capacity * 100.0;
                  String strPercentage = String.format("%.1f", percentage) + "%";
                  if (percentage != 100.0 && percentage != 0.0) {
                     strPercentage = TextFormatting.YELLOW + strPercentage;
                  } else {
                     strPercentage = TextFormatting.RED + strPercentage;
                  }

                  message = message + strPercentage + TextFormatting.GRAY + ") - Last Cycle: ";
                  long timeSinceLastCycle = System.currentTimeMillis() - pts.lastCycleTime;
                  double seconds = (double)timeSinceLastCycle / 1000.0;
                  double expectedSeconds = (double)(pts.spawnFrequency / 60.0F);
                  if (seconds > expectedSeconds * 60.0) {
                     message = message + TextFormatting.RED + String.format("%.1f", seconds) + "s";
                  } else if (seconds > expectedSeconds * 10.0) {
                     message = message + TextFormatting.YELLOW + String.format("%.1f", seconds) + "s";
                  } else {
                     message = message + TextFormatting.GREEN + String.format("%.1f", seconds) + "s";
                  }

                  message = message + TextFormatting.GRAY + " - Last Spawn: ";
                  long timeSinceLastSpawn = System.currentTimeMillis() - pts.lastSpawnTime;
                  seconds = (double)timeSinceLastSpawn / 1000.0;
                  String time = String.format("%.1f", seconds) + "s";
                  if (seconds > expectedSeconds * 60.0) {
                     message = message + TextFormatting.RED + time;
                  } else if (seconds > expectedSeconds * 10.0) {
                     message = message + TextFormatting.YELLOW + time;
                  } else {
                     message = message + TextFormatting.GREEN + time;
                  }

                  summaryMessages.add(message);
               }
            }
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabComplete(args, new String[]{"on", "off", "diagnose"}) : tabComplete(args, new String[0]);
   }
}
