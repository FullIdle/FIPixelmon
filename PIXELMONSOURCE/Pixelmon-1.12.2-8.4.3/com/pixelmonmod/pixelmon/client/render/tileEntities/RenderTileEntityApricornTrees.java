package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.client.models.obj.ObjLoader;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityApricornTrees extends TileEntitySpecialRenderer {
   private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornblack.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornwhite.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornpink.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricorngreen.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornblue.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornyellow.png"), new ResourceLocation("pixelmon:textures/blocks/apricorn_trees/apricornred.png")};

   public void render(TileEntityApricornTree tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      IBlockState state = Minecraft.func_71410_x().field_71441_e.func_180495_p(tile.func_174877_v());
      if (!(state.func_177230_c() instanceof BlockApricornTree)) {
         Pixelmon.LOGGER.error("Apricorn tile entity without a block at " + tile.func_174877_v().func_177958_n() + ", " + tile.func_174877_v().func_177956_o() + ", " + tile.func_174877_v().func_177952_p());
      } else {
         try {
            this.func_147499_a(TEXTURES[tile.tree.ordinal()]);
         } catch (NullPointerException var13) {
            Pixelmon.LOGGER.error("Can't find texture: " + TEXTURES[tile.tree.ordinal()].func_110623_a());
            return;
         } catch (ArrayIndexOutOfBoundsException var14) {
            Pixelmon.LOGGER.error("Bad apricorn tree ordinal: " + tile.tree.ordinal());
            return;
         }

         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)x + 0.5F, (float)y, (float)z + 0.5F);
         GlStateManager.func_179152_a(-0.25F, 0.28F, -0.25F);
         GlStateManager.func_179133_A();
         GlStateManager.func_179103_j(7425);
         RenderTileEntityApricornTrees.EnumApricornModel.values()[tile.getStage()].getModel().render();
         GlStateManager.func_179121_F();
      }
   }

   public static enum EnumApricornModel {
      SEED("pixelmon:models/blocks/apricorn_trees/seed.obj"),
      STAGE1("pixelmon:models/blocks/apricorn_trees/stage1.obj"),
      STAGE2("pixelmon:models/blocks/apricorn_trees/stage2.obj"),
      MIDDLE("pixelmon:models/blocks/apricorn_trees/middle.obj"),
      STAGE3("pixelmon:models/blocks/apricorn_trees/stage3.obj"),
      FINAL("pixelmon:models/blocks/apricorn_trees/final.obj");

      public WavefrontObject obj;
      public final ResourceLocation path;

      private EnumApricornModel(String path) {
         this.path = new ResourceLocation(path);
      }

      public WavefrontObject getModel() {
         if (this.obj == null) {
            try {
               if (ObjLoader.accepts(this.path)) {
                  this.obj = ObjLoader.loadModel(this.path);
               }
            } catch (Exception var2) {
               System.out.println("Could not load the model: " + this.path.func_110623_a());
               var2.printStackTrace();
            }
         }

         return this.obj;
      }
   }
}
