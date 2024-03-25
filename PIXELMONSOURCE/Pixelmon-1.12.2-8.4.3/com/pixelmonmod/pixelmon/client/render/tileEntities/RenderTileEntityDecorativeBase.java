package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.decorative.BlockContainerPlus;
import com.pixelmonmod.pixelmon.blocks.enums.EnumAxis;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelEntityBlock;
import java.util.HashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderTileEntityDecorativeBase extends TileEntitySpecialRenderer {
   static HashMap modelMap = new HashMap();

   public void render(TileEntityDecorativeBase tileentity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      GlStateManager.func_179094_E();

      try {
         GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
         GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
         IBlockState state = tileentity.func_145831_w().func_180495_p(tileentity.func_174877_v());
         if (state.func_177230_c() instanceof BlockContainerPlus) {
            this.doRotation(state);
            ModelEntityBlock theModel = getModelFor((BlockContainerPlus)state.func_177230_c());
            theModel.renderTileEntity(tileentity, 0.0625F);
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      GlStateManager.func_179121_F();
   }

   public void doRotation(IBlockState state) {
      EnumAxis axis;
      try {
         axis = (EnumAxis)state.func_177229_b(BlockContainerPlus.AXIS);
      } catch (IllegalArgumentException var4) {
         return;
      }

      switch (axis) {
         case X:
            GlStateManager.func_179114_b(-90.0F, 0.0F, 0.0F, 1.0F);
            break;
         case Z:
            GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
      }

   }

   public static ModelEntityBlock addModelFor(BlockContainerPlus block) {
      ModelEntityBlock model = null;

      try {
         model = (ModelEntityBlock)block.modelClass.newInstance();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      modelMap.put(block, model);
      return model;
   }

   public static ModelEntityBlock getModelFor(BlockContainerPlus block) {
      return modelMap.containsKey(block) ? (ModelEntityBlock)modelMap.get(block) : addModelFor(block);
   }
}
