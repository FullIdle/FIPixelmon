package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.util.SpatialData;
import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;

public class RayTraceCheckSpawns implements ICheckSpawns {
   public String node;
   public LocationType locationType;
   public Predicate isValidBlock;

   public RayTraceCheckSpawns(LocationType locationType, Predicate isValidBlock, String node) {
      this.locationType = locationType;
      this.isValidBlock = isValidBlock;
      this.node = node;
   }

   public String getPermissionNode() {
      return this.node;
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

      RayTraceResult result = ForgeHooks.rayTraceEyes(target, 5.0);
      if (result != null && result.field_72313_a == Type.BLOCK) {
         if (this.isValidBlock != null && !this.isValidBlock.test(target.func_71121_q().func_180495_p(result.func_178782_a()))) {
            sender.func_145747_a(this.translate(TextFormatting.RED, "spawning.error.invalidblock", new Object[0]));
         } else {
            SpatialData data = spawner.calculateSpatialData(target.func_71121_q(), result.func_178782_a(), 10, true, (b) -> {
               return true;
            });
            SpawnLocation spawnLocation = new SpawnLocation(target, new MutableLocation(target.func_71121_q(), result.func_178782_a()), Sets.newHashSet(new LocationType[]{this.locationType}), data.baseBlock, data.uniqueSurroundingBlocks, target.func_71121_q().func_180494_b(result.func_178782_a()), BetterSpawnerConfig.doesBlockSeeSky(target.func_71121_q().func_180495_p(result.func_178782_a())), 6, 0);
            sender.func_184102_h().func_152344_a(() -> {
               ArrayList spawns = spawner.getSuitableSpawns(spawnLocation);
               Map possibleSpawns = new HashMap();
               possibleSpawns.put(spawnLocation, spawns);
               Map percentages = spawner.selectionAlgorithm.getPercentages(spawner, possibleSpawns);
               List messages = this.generateMessages(percentages, arguments);
               messages.add(0, this.translate(TextFormatting.AQUA, "spawning.checkspawns.possiblespawns", new Object[0]));
               messages.forEach(sender::func_145747_a);
            });
         }
      } else {
         sender.func_145747_a(this.translate(TextFormatting.RED, "spawning.error.notlookingatblock", new Object[]{target.func_70005_c_()}));
      }
   }
}
