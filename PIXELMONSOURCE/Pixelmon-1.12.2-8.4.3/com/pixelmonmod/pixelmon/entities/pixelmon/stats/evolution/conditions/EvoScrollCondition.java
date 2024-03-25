package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityScroll;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class EvoScrollCondition extends EvoCondition {
   public BlockScroll.Type evolutionScroll;
   public int maxRangeSquared = 100;

   public EvoScrollCondition(BlockScroll.Type evolutionScroll, int maxRangeSquared) {
      super("evolutionScroll");
      this.evolutionScroll = evolutionScroll;
      this.maxRangeSquared = maxRangeSquared;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.func_70902_q() != null ? pixelmon.func_70902_q().func_130014_f_().field_147482_g.stream().anyMatch((te) -> {
         if (!(te instanceof TileEntityScroll)) {
            return false;
         } else {
            TileEntityScroll scroll = (TileEntityScroll)te;
            return !scroll.isDisplayOnly() && scroll.getType() == this.evolutionScroll && te.func_174877_v().func_177951_i(pixelmon.func_70902_q().func_180425_c()) < (double)this.maxRangeSquared;
         }
      }) : pixelmon.func_130014_f_().field_147482_g.stream().anyMatch((te) -> {
         if (!(te instanceof TileEntityScroll)) {
            return false;
         } else {
            TileEntityScroll scroll = (TileEntityScroll)te;
            return !scroll.isDisplayOnly() && scroll.getType() == this.evolutionScroll && te.func_174877_v().func_177951_i(pixelmon.func_70902_q().func_180425_c()) < (double)this.maxRangeSquared;
         }
      });
   }
}
