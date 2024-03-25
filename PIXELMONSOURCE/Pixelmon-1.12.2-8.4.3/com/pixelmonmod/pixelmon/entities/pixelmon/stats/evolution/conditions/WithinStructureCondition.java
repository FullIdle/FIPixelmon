package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.world.WorldServer;

public class WithinStructureCondition extends EvoCondition {
   private String structure;

   public WithinStructureCondition() {
      super("withinStructure");
   }

   public boolean passes(EntityPixelmon pixelmon) {
      if (!pixelmon.field_70170_p.field_72995_K && pixelmon.field_70170_p instanceof WorldServer) {
         WorldServer world = (WorldServer)pixelmon.field_70170_p;
         return world.func_72863_F().func_193413_a(world, this.structure, pixelmon.func_180425_c());
      } else {
         return false;
      }
   }

   public String getStructure() {
      return this.structure;
   }
}
