package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.DeoxysStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MiniorStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.RecoilStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ExtraStats {
   private static final Map STATS_SUPPLIERS = Maps.newEnumMap(EnumSpecies.class);

   public abstract void writeToNBT(NBTTagCompound var1);

   public abstract void readFromNBT(NBTTagCompound var1);

   public static void init() {
      STATS_SUPPLIERS.put(EnumSpecies.Mew, MewStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Azelf, LakeTrioStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Uxie, LakeTrioStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Mesprit, LakeTrioStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Meltan, MeltanStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Mareep, ShearableStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Minior, MiniorStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Wooloo, ShearableStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Dubwool, ShearableStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Deoxys, DeoxysStats::new);
      STATS_SUPPLIERS.put(EnumSpecies.Basculin, RecoilStats::new);
   }

   public static ExtraStats getExtraStats(EnumSpecies p) {
      Supplier supplier = (Supplier)STATS_SUPPLIERS.get(p);
      return supplier == null ? null : (ExtraStats)supplier.get();
   }

   public boolean hasSpecialSetup() {
      return false;
   }

   public void specialPrep(Pokemon pokemon) {
   }
}
