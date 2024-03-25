package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityCookingPot extends TileEntityRenderer {
   private static final GenericModelHolder potModel = new GenericModelHolder("blocks/cookingpot/cookingpot.pqc");
   private static final GenericModelHolder stewModel = new GenericModelHolder("blocks/cookingpot/stew.pqc");
   private static final GenericModelHolder spoonModel = new GenericModelHolder("blocks/cookingpot/spoon.pqc");
   private static final ResourceLocation textures = new ResourceLocation("pixelmon", "textures/blocks/cookingpot.png");

   public RenderTileEntityCookingPot() {
      this.correctionAngles = 90;
   }

   public void renderTileEntity(TileEntityCookingPot machine, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(textures);
      potModel.render(1.0F);
      if (machine.isCooking()) {
         stewModel.render(1.0F);
         ((GenericSmdModel)spoonModel.getModel()).getModel().setAnimation(AnimationType.SPECIAL);
         ((GenericSmdModel)spoonModel.getModel()).setFrame((int)machine.frame);
      } else {
         ((GenericSmdModel)spoonModel.getModel()).getModel().setAnimation(AnimationType.IDLE);
         ((GenericSmdModel)spoonModel.getModel()).setFrame(0);
      }

      spoonModel.render(1.0F);
   }
}
