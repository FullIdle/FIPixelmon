package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.events.spawning.LegendarySpawnEvent;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.spawning.LegendarySpawner;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

public class DoLegendarySpawnCommand extends PixelmonCommand {
   public DoLegendarySpawnCommand() {
      super("dolegendaryspawn", "/dolegendaryspawn [player]", 4);
      this.aliases.add("legendaryspawn");
      this.aliases.add("spawnlegendary");
   }

   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      if (getServer().func_184103_al().func_72394_k() == 0) {
         endCommand("spawning.error.nooneisonline", new Object[0]);
      }

      EntityPlayerMP target = args.length == 1 ? (StringUtils.equalsAnyIgnoreCase(args[0], new CharSequence[]{"any", "random"}) ? (EntityPlayerMP)RandomHelper.getRandomElementFromList(getServer().func_184103_al().func_181057_v()) : requireEntityPlayer(args[0])) : (sender instanceof EntityPlayerMP ? requireEntityPlayer(sender) : (EntityPlayerMP)RandomHelper.getRandomElementFromList(getServer().func_184103_al().func_181057_v()));
      boolean isCoordinatorActive = PixelmonSpawning.coordinator.getActive();
      AbstractSpawner abstrSpawner = PixelmonSpawning.coordinator.getSpawner("legendary");
      if (abstrSpawner == null) {
         abstrSpawner = PixelmonSpawning.legendarySpawner;
      }

      LegendarySpawner spawner = (LegendarySpawner)require(abstrSpawner, "spawning.error.legendaryspawnermissing", new Object[0]);
      sender.func_145747_a(format(TextFormatting.GRAY, "spawning.attemptlegendaryspawn", new Object[0]));
      if (isCoordinatorActive) {
         spawner.forcefullySpawn(target);
      } else {
         this.forcefullySpawn(target, spawner);
      }

      Runnable r = () -> {
         if (spawner.possibleSpawns != null && !spawner.possibleSpawns.isEmpty()) {
            spawner.possibleSpawns.forEach((spawn) -> {
               spawn.spawnInfo.tags.forEach((tag) -> {
                  Long var10000 = (Long)BetterSpawnerConfig.intervalTimes.remove(tag);
               });
               PixelmonCommand.getServer().func_152344_a(() -> {
                  spawn.doSpawn(spawner);
               });
            });
            spawner.possibleSpawns = null;
         } else {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.effect.effectfailed", new Object[0]);
            this.sendMessage(sender, TextFormatting.GRAY, "/checkspawns legendary", new Object[0]);
         }

      };
      if (isCoordinatorActive) {
         SpawnerCoordinator.PROCESSOR.execute(r);
      } else {
         DoLegendarySpawnCommand.Processor.instance.addProcess(r);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }

   private void forcefullySpawn(@Nullable EntityPlayerMP onlyFocus, LegendarySpawner spawner) {
      ArrayList clusters = new ArrayList();
      ArrayList players = new ArrayList(getServer().func_184103_al().func_181057_v());
      if (onlyFocus == null) {
         while(!players.isEmpty()) {
            ArrayList cluster = new ArrayList();
            EntityPlayerMP focus = (EntityPlayerMP)players.remove(0);
            cluster.add(focus);
            LegendarySpawner.fillNearby(players, cluster, focus);
            clusters.add(cluster);
         }
      }

      spawner.isBusy = true;
      DoLegendarySpawnCommand.Processor.instance.addProcess(() -> {
         if (onlyFocus != null) {
            spawner.possibleSpawns = spawner.doLegendarySpawn(onlyFocus);
         } else {
            Collections.shuffle(clusters);

            while(clusters.size() > 0) {
               for(int i = 0; i < clusters.size(); ++i) {
                  EntityPlayerMP player = (EntityPlayerMP)CollectionHelper.getRandomElement((List)clusters.get(i));
                  ((ArrayList)clusters.get(i)).remove(player);
                  if (((ArrayList)clusters.get(i)).isEmpty()) {
                     clusters.remove(i--);
                  }

                  if (spawner.firesChooseEvent) {
                     LegendarySpawnEvent.ChoosePlayer event = new LegendarySpawnEvent.ChoosePlayer(spawner, player, clusters);
                     if (Pixelmon.EVENT_BUS.post(event) || event.player == null) {
                        continue;
                     }

                     player = event.player;
                  }

                  spawner.possibleSpawns = spawner.doLegendarySpawn(player);
                  if (spawner.possibleSpawns != null) {
                     spawner.isBusy = false;
                     return;
                  }
               }
            }
         }

         spawner.isBusy = false;
      });
   }

   public static class Processor {
      public static final Processor instance = new Processor();
      private ThreadFactory namedThreadFactory = new NamedThreadFactory();
      private ThreadPoolExecutor executor;

      public Processor() {
         this.executor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque(), this.namedThreadFactory);
      }

      public void addProcess(Runnable process) {
         this.executor.execute(process);
      }

      private static class NamedThreadFactory implements ThreadFactory {
         private NamedThreadFactory() {
         }

         public Thread newThread(Runnable arg0) {
            Thread t = new Thread(arg0);
            t.setDaemon(true);
            t.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
            t.setName("DoLegendarySpawnCommand-Spawner");
            return t;
         }

         // $FF: synthetic method
         NamedThreadFactory(Object x0) {
            this();
         }
      }

      private static class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
         private UncaughtExceptionHandler() {
         }

         public void uncaughtException(Thread arg0, Throwable arg1) {
            Pixelmon.LOGGER.error("Caught Exception from \"" + arg0.getName() + "\"", arg1);
         }

         // $FF: synthetic method
         UncaughtExceptionHandler(Object x0) {
            this();
         }
      }
   }
}
