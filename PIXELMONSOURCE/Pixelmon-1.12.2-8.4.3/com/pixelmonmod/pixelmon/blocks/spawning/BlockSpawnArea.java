package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.World;

public class BlockSpawnArea {
   public String name;
   public EnumBattleStartTypes type;
   public HashMap blockSpawns = new HashMap();

   public BlockSpawnArea(String name, EnumBattleStartTypes type, HashMap blockSpawns) {
      this.name = name;
      this.type = type;
      this.blockSpawns = blockSpawns;
   }

   public String getRandomPokemon(World world) {
      return this.getPokemonFromList(world);
   }

   public String getPokemonFromList(World world) {
      List times = WorldTime.getCurrent(world);
      int totalRarity = 0;

      BlockSpawnData spawnList;
      for(Iterator var4 = this.blockSpawns.values().iterator(); var4.hasNext(); totalRarity += spawnList.getRarity(times)) {
         spawnList = (BlockSpawnData)var4.next();
      }

      if (totalRarity > 0) {
         int num = RandomHelper.getRandomNumberBetween(0, totalRarity - 1);
         int sum = 0;

         int rarity;
         for(Iterator var6 = this.blockSpawns.values().iterator(); var6.hasNext(); sum += rarity) {
            BlockSpawnData spawnList = (BlockSpawnData)var6.next();
            rarity = 0;
            rarity += spawnList.getRarity(times);
            if (num < sum + rarity) {
               return spawnList.name;
            }
         }
      }

      return null;
   }
}
