package com.pixelmonmod.pixelmon.api.spawning.calculators;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.util.SpatialData;
import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextFormatting;

public class CurryCheckSpawns implements ICheckSpawns {
   public String getPermissionNode() {
      return "pixelmon.checkspawns.curry";
   }

   public void checkSpawns(AbstractSpawner spawner, ICommandSender sender, List arguments) {
      EntityPlayerMP target = null;
      if (!(sender instanceof EntityPlayerMP)) {
         PlayerList playerList = sender.func_184102_h().func_184103_al();
         Iterator var6 = arguments.iterator();

         while(var6.hasNext()) {
            String argument = (String)var6.next();
            if (!spawner.name.equals(argument) && (target = playerList.func_152612_a(argument)) != null) {
               break;
            }
         }

         if (target == null) {
            sender.func_145747_a(this.translate(TextFormatting.RED, "spawning.error.mustbeplayer", new Object[0]));
            return;
         }
      }

      if (target == null) {
         target = (EntityPlayerMP)sender;
      }

      ArrayList spawnLocations = new ArrayList();
      SpatialData data = spawner.calculateSpatialData(target.func_71121_q(), target.func_180425_c(), 6, true, (s) -> {
         return true;
      });
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_BITTER}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_DRY}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_NONE}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_SOUR}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_SPICY}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      spawnLocations.add(new SpawnLocation(target, new MutableLocation(target.func_71121_q(), target.func_180425_c()), Sets.newHashSet(new LocationType[]{LocationType.CURRY_SWEET}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(target.func_180425_c()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(target.func_180425_c())), 6, 1));
      Map possibleSpawns = new HashMap();
      Iterator var8 = spawnLocations.iterator();

      while(var8.hasNext()) {
         SpawnLocation spawnLocation = (SpawnLocation)var8.next();
         ArrayList spawns = spawner.getSuitableSpawns(spawnLocation);
         if (!spawns.isEmpty()) {
            possibleSpawns.put(spawnLocation, spawns);
         }
      }

      sender.func_184102_h().func_152344_a(() -> {
         Map percentages = spawner.selectionAlgorithm.getPercentages(spawner, possibleSpawns);
         List messages = this.generateMessages(percentages, arguments);
         messages.add(0, this.translate(TextFormatting.AQUA, "spawning.checkspawns.possiblespawns", new Object[0]));
         messages.forEach(sender::func_145747_a);
      });
   }
}
