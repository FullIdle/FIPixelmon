package com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.enums.forms.EnumShearable;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ShearableTickHandler extends TickHandlerBase {
   public ShearableTickHandler(Entity1Base pixelmon) {
      super(pixelmon, 400);
   }

   protected void onTick(World world) {
      if (!world.field_72995_K) {
         ShearableStats shearableStats = (ShearableStats)this.pixelmon.getPokemonData().getExtraStats(ShearableStats.class);
         if (shearableStats.growthStage > 0 && world.func_180495_p(this.pixelmon.func_180425_c().func_177977_b()).func_177230_c() == Blocks.field_150349_c) {
            world.func_175656_a(this.pixelmon.func_180425_c().func_177977_b(), Blocks.field_150346_d.func_176223_P());
            --shearableStats.growthStage;
            if (shearableStats.growthStage <= 0) {
               this.pixelmon.getPokemonData().setForm(EnumShearable.NORMAL);
            }
         }

      }
   }
}
