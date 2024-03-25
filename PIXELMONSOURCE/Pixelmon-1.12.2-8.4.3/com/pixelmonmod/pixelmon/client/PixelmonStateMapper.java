package com.pixelmonmod.pixelmon.client;

import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.ResourceLocation;

public class PixelmonStateMapper extends DefaultStateMapper {
   protected ModelResourceLocation func_178132_a(IBlockState state) {
      return state.func_177230_c() instanceof BlockScroll ? new ModelResourceLocation("oak_planks", "normal") : new ModelResourceLocation((ResourceLocation)Block.field_149771_c.func_177774_c(state.func_177230_c()), "normal");
   }
}
