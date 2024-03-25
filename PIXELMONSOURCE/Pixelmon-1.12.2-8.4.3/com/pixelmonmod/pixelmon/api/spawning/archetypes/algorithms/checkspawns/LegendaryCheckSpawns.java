package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.LegendaryCheckSpawnsEvent;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICheckSpawns;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.spawning.LegendarySpawner;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class LegendaryCheckSpawns implements ICheckSpawns {
   public String node;

   public LegendaryCheckSpawns(String node) {
      this.node = node;
   }

   public String getPermissionNode() {
      return this.node;
   }

   public void checkSpawns(AbstractSpawner spawner, ICommandSender sender, List arguments) {
      if (spawner instanceof LegendarySpawner) {
         LegendaryCheckSpawnsEvent event = new LegendaryCheckSpawnsEvent(sender);
         Pixelmon.EVENT_BUS.post(event);
         LegendarySpawner lSpawner = (LegendarySpawner)spawner;
         EntityPlayerMP target = null;
         List targets = new ArrayList();
         if (!(sender instanceof EntityPlayerMP)) {
            PlayerList playerList = sender.func_184102_h().func_184103_al();
            Iterator var9 = arguments.iterator();

            while(var9.hasNext()) {
               String argument = (String)var9.next();
               if (!spawner.name.equals(argument) && (target = playerList.func_152612_a(argument)) != null) {
                  break;
               }
            }
         }

         if (target == null) {
            targets.addAll(sender.func_184102_h().func_184103_al().func_181057_v());
         } else {
            targets.add(target);
         }

         HashMap totalPercentages = new HashMap();
         double percentageSum = 0.0;
         Iterator var11 = targets.iterator();

         Iterator var15;
         while(var11.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP)var11.next();
            ArrayList spawnLocations = lSpawner.spawnLocationCalculator.calculateSpawnableLocations(spawner.getTrackedBlockCollection(player, 0.0F, 0.0F, lSpawner.horizontalSliceRadius, lSpawner.verticalSliceRadius, 0, 0));
            Map possibleSpawns = new HashMap();
            var15 = spawnLocations.iterator();

            while(var15.hasNext()) {
               SpawnLocation spawnLocation = (SpawnLocation)var15.next();
               ArrayList spawns = spawner.getSuitableSpawns(spawnLocation);
               if (!spawns.isEmpty()) {
                  possibleSpawns.put(spawnLocation, spawns);
               }
            }

            Map percentages = spawner.selectionAlgorithm.getPercentages(spawner, possibleSpawns);

            Map.Entry entry;
            for(Iterator var25 = percentages.entrySet().iterator(); var25.hasNext(); percentageSum += (Double)entry.getValue()) {
               entry = (Map.Entry)var25.next();
               totalPercentages.put(entry.getKey(), (Double)entry.getValue() + (Double)totalPercentages.getOrDefault(entry.getKey(), 0.0));
            }
         }

         DecimalFormat df = new DecimalFormat(".####", DecimalFormatSymbols.getInstance(Locale.US));
         DecimalFormat df2 = new DecimalFormat(".##", DecimalFormatSymbols.getInstance(Locale.US));
         if (percentageSum > 100.0) {
            double multiplier = 100.0 / percentageSum;
            var15 = totalPercentages.keySet().iterator();

            label49:
            while(true) {
               while(true) {
                  if (!var15.hasNext()) {
                     break label49;
                  }

                  String key = (String)var15.next();
                  double percentage = (Double)totalPercentages.get(key) * multiplier;
                  if (!(percentage < 0.01) && !(percentage > 99.99)) {
                     totalPercentages.put(key, Double.parseDouble(df2.format(percentage)));
                  } else {
                     totalPercentages.put(key, Double.parseDouble(df.format(percentage)));
                  }
               }
            }
         }

         sender.func_184102_h().func_152344_a(() -> {
            List messages = new ArrayList();
            long timeToGo = lSpawner.nextSpawnTime - System.currentTimeMillis();
            int minutes = (int)Math.ceil((double)((float)timeToGo / 1000.0F / 60.0F));
            if (event.shouldShowTime) {
               messages.add(this.translate(TextFormatting.GOLD, "spawning.checkspawns.timeuntilnextattempt1", new Object[0]).func_150257_a(new TextComponentString(" ")).func_150257_a(this.translate(TextFormatting.DARK_AQUA, "spawning.checkspawns.timeuntilnextattempt2", new Object[]{minutes})));
            }

            if (event.shouldShowChance) {
               messages.add(this.translate(TextFormatting.GOLD, "spawning.checkspawns.chanceofspawning", new Object[0]).func_150257_a(new TextComponentString(" " + TextFormatting.DARK_AQUA + (new DecimalFormat(".##")).format((double)((lSpawner == PixelmonSpawning.legendarySpawner ? PixelmonConfig.getLegendaryRate() : PixelmonConfig.bossSpawnChance) * 100.0F)) + "%")));
            }

            messages.add(this.translate(TextFormatting.AQUA, "spawning.checkspawns.possiblespawns", new Object[0]));
            messages.addAll(this.generateMessages(totalPercentages, arguments));
            messages.forEach(sender::func_145747_a);
         });
      }
   }
}
