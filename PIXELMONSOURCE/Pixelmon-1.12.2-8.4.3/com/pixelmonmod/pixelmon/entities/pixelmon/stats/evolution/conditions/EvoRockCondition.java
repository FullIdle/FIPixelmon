package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.blocks.BlockEvolutionRock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityEvolutionRock;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionRock;

public class EvoRockCondition extends EvoCondition {
   public EnumEvolutionRock evolutionRock;
   public int maxRangeSquared = 100;

   public EvoRockCondition(EnumEvolutionRock evoRock, int maxRangeSquared) {
      super("evolutionRock");
      this.evolutionRock = evoRock;
      this.maxRangeSquared = maxRangeSquared;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.func_70902_q() != null ? pixelmon.func_70902_q().func_130014_f_().field_147482_g.stream().anyMatch((te) -> {
         return te instanceof TileEntityEvolutionRock && te.func_174877_v().func_177951_i(pixelmon.func_70902_q().func_180425_c()) < (double)this.maxRangeSquared && ((BlockEvolutionRock)te.func_145838_q()).rockType == this.evolutionRock;
      }) : pixelmon.func_130014_f_().field_147482_g.stream().anyMatch((te) -> {
         return te instanceof TileEntityEvolutionRock && te.func_174877_v().func_177951_i(pixelmon.func_180425_c()) < (double)this.maxRangeSquared && ((BlockEvolutionRock)te.func_145838_q()).rockType == this.evolutionRock;
      });
   }
}
