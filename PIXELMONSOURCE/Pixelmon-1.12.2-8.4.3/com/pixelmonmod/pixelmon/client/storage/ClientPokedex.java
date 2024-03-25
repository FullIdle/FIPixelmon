package com.pixelmonmod.pixelmon.client.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex.RequestMovesetData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex.RequestSpawnData;
import com.pixelmonmod.pixelmon.enums.EnumMovesetGroup;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;

@SideOnly(Side.CLIENT)
public class ClientPokedex extends Pokedex {
   Map data = Maps.newHashMap();
   Map movesets = Maps.newHashMap();

   public void update(Map seenMap, Table data2) {
      this.setSeenMap(seenMap);
      this.formDex = data2;
   }

   public void update(int npn, List spawnData) {
      this.data.put(npn, spawnData);
   }

   public void updateMoveset(EnumSpecies species, byte form, String texture, Map moveset) {
      this.movesets.put(Triple.of(species, form, texture), moveset);
   }

   public List getSpawnData(int npn) {
      if (!this.data.containsKey(npn)) {
         Pixelmon.network.sendToServer(new RequestSpawnData(npn));
         this.data.put(npn, Lists.newArrayList());
      }

      return (List)this.data.get(npn);
   }

   public Map getMoveset(EnumSpecies species, byte form, String texture) {
      Triple p = Triple.of(species, form, texture);
      if (!this.movesets.containsKey(p)) {
         Pixelmon.network.sendToServer(new RequestMovesetData(species, form, texture));
         HashMap m = new HashMap();
         EnumMovesetGroup[] var6 = EnumMovesetGroup.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EnumMovesetGroup group = var6[var8];
            m.put(group, new ArrayList());
         }

         this.movesets.put(p, m);
      }

      return (Map)this.movesets.get(p);
   }
}
