package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICheckSpawns;
import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextFormatting;

public class FishingCheckSpawns implements ICheckSpawns {
   public String getPermissionNode() {
      return "pixelmon.checkspawns.fishing";
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

      if (!(target.field_71104_cf instanceof EntityHook)) {
         sender.func_145747_a(this.translate(TextFormatting.RED, "spawning.error.notfishing", new Object[]{target.func_70005_c_()}));
      } else {
         SpawnLocation spawnLocation = ((EntityHook)target.field_71104_cf).createSpawnLocation();
         sender.func_184102_h().func_152344_a(() -> {
            ArrayList spawns = spawner.getSuitableSpawns(spawnLocation);
            if (spawnLocation.diameter == 0) {
               spawns.clear();
            }

            Map possibleSpawns = new HashMap();
            possibleSpawns.put(spawnLocation, spawns);
            Map percentages = spawner.selectionAlgorithm.getPercentages(spawner, possibleSpawns);
            List messages = this.generateMessages(percentages, arguments);
            messages.add(0, this.translate(TextFormatting.AQUA, "spawning.checkspawns.possiblespawns", new Object[0]));
            messages.forEach(sender::func_145747_a);
         });
      }
   }
}
