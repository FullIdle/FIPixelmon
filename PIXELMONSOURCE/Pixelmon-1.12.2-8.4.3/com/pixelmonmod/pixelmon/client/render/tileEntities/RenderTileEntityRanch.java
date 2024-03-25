package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityRanch extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder("blocks/ranch/ranch.pqc");
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/ranchblock.png");
   private static final GenericModelHolder Egg = new GenericModelHolder("blocks/ranch/ranch_egg.pqc");
   private static final ResourceLocation togepiEgg = new ResourceLocation("pixelmon", "textures/eggs/eggTogepi.png");
   private static final ResourceLocation greenEgg = new ResourceLocation("pixelmon", "textures/eggs/eggGreen.png");

   public RenderTileEntityRanch() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityRanchBlock ranch, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(texture);
      int ranchCurrentFrame = ranch.percentAbove / 5;
      ((GenericSmdModel)model.getModel()).setFrame(ranchCurrentFrame);
      model.render();
      if (ranch.hasEgg()) {
         EnumSpecies eggSpecies = ranch.getEggSpecies();
         if (eggSpecies != null) {
            if (eggSpecies == EnumSpecies.Togepi) {
               this.func_147499_a(togepiEgg);
            } else {
               this.func_147499_a(greenEgg);
            }

            ((GenericSmdModel)Egg.getModel()).setFrame(ranchCurrentFrame);
            Egg.render();
         }
      }

   }
}
