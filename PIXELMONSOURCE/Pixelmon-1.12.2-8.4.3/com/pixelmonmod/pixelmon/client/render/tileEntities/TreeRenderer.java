package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTree;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class TreeRenderer extends TileEntityRenderer {
   private static final GenericModelHolder tree1 = new GenericModelHolder("pixelutilities/Tree/tree1.pqc");
   private static final GenericModelHolder tree2 = new GenericModelHolder("pixelutilities/Tree/tree2.pqc");
   private static final GenericModelHolder tree3 = new GenericModelHolder("pixelutilities/Tree/tree3.pqc");
   private static final GenericModelHolder tree4 = new GenericModelHolder("pixelutilities/Tree/tree4.pqc");
   private static final ResourceLocation texture1 = new ResourceLocation("pixelmon", "textures/blocks/trees/tree1.png");
   private static final ResourceLocation texture2 = new ResourceLocation("pixelmon", "textures/blocks/trees/tree2.png");
   private static final ResourceLocation texture3 = new ResourceLocation("pixelmon", "textures/blocks/trees/tree3.png");
   private static final ResourceLocation texture4 = new ResourceLocation("pixelmon", "textures/blocks/trees/tree4.png");

   public TreeRenderer() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityTree tree, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      int treeType = tree.getTreeType();
      switch (treeType) {
         case 2:
            this.func_147499_a(texture2);
            tree2.render();
            break;
         case 3:
            this.func_147499_a(texture3);
            tree3.render();
            break;
         case 4:
            this.func_147499_a(texture4);
            tree4.render();
            break;
         default:
            System.out.println("Tree type not saved. Listed number: " + treeType);
         case 0:
            tree.setTreeType(1);
         case 1:
            this.func_147499_a(texture1);
            tree1.render();
      }

   }
}
