package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMachine extends GenericRotatableModelBlock {
   public BlockMachine(String name) {
      super(Material.field_151573_f);
      this.func_149711_c(0.75F);
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.func_149663_c(name);
      this.func_149672_a(SoundType.field_185852_e);
   }
}
