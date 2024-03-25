package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityEvolutionRock;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityEvolutionRock extends TileEntityRenderer {
   private static final GenericModelHolder IcyRock = new GenericModelHolder("blocks/evoRock/icyrock.pqc");
   private static final ResourceLocation iceTexture = new ResourceLocation("pixelmon", "textures/blocks/icyRockTex.png");
   private static final GenericModelHolder MossyRock = new GenericModelHolder("blocks/evoRock/mossyrock.pqc");
   private static final ResourceLocation mossyTexture = new ResourceLocation("pixelmon", "textures/blocks/mossyRockTex.png");

   public RenderTileEntityEvolutionRock() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityEvolutionRock rock, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (rock.func_145838_q() == PixelmonBlocks.icyRock) {
         this.func_147499_a(iceTexture);
         IcyRock.render();
      } else {
         this.func_147499_a(mossyTexture);
         MossyRock.render();
      }

   }
}
