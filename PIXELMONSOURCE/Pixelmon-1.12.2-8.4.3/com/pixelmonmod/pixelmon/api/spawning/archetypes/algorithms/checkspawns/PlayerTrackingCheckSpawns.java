package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICheckSpawns;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

public class PlayerTrackingCheckSpawns implements ICheckSpawns {
   public void checkSpawns(AbstractSpawner spawner, ICommandSender sender, List arguments) {
      if (!(spawner instanceof PlayerTrackingSpawner)) {
         ICheckSpawns.getDefault().checkSpawns(spawner, sender, arguments);
      } else {
         PlayerTrackingSpawner pSpawner = (PlayerTrackingSpawner)spawner;
         EntityPlayerMP player = pSpawner.getTrackedPlayer();
         if (player == null) {
            sender.func_145747_a(this.translate(TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]));
            return;
         }

         ArrayList spawnLocations = pSpawner.spawnLocationCalculator.calculateSpawnableLocations(spawner.getTrackedBlockCollection(player, 0.0F, 0.0F, pSpawner.horizontalSliceRadius, pSpawner.verticalSliceRadius, 0, 0));
         Iterator var7 = arguments.iterator();

         while(var7.hasNext()) {
            String argument = (String)var7.next();
            if (argument.equalsIgnoreCase("specific")) {
               spawnLocations.removeIf((spawnLocationx) -> {
                  return !spawnLocationx.location.pos.equals(player.func_180425_c());
               });
               if (spawnLocations.isEmpty()) {
                  sender.func_145747_a(this.translate(TextFormatting.RED, "spawning.error.midairorinvalid", new Object[0]));
                  return;
               }
               break;
            }
         }

         Map possibleSpawns = new HashMap();
         Iterator var12 = spawnLocations.iterator();

         while(var12.hasNext()) {
            SpawnLocation spawnLocation = (SpawnLocation)var12.next();
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
}
