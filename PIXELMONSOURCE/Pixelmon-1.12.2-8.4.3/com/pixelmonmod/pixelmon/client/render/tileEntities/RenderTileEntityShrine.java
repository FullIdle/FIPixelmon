package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.enums.EnumUsed;
import com.pixelmonmod.pixelmon.blocks.machines.BlockShrine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityShrine;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityShrine extends TileEntityRenderer {
   private static final GenericModelHolder ArticunoShrine = new GenericModelHolder("blocks/shrines/articunoshrine.pqc");
   private static final ResourceLocation articuno_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/articuno.png");
   private static final GenericModelHolder MoltresShrine = new GenericModelHolder("blocks/shrines/moltresshrine.pqc");
   private static final ResourceLocation moltres_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/moltres.png");
   private static final GenericModelHolder ZapdosShrine = new GenericModelHolder("blocks/shrines/zapdosshrine.pqc");
   private static final ResourceLocation zapdos_texture = new ResourceLocation("pixelmon", "textures/blocks/shrines/zapdos.png");
   private static final GenericModelHolder Orb = new GenericModelHolder("blocks/shrines/orb.pqc");

   public RenderTileEntityShrine() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityShrine shrineBlock, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      EnumUsed used = EnumUsed.NO;
      if (state.func_177230_c() instanceof BlockShrine) {
         used = (EnumUsed)state.func_177229_b(BlockShrine.USED);
      }

      ResourceLocation texture;
      GenericModelHolder shrineModel;
      if (state.func_177230_c() == PixelmonBlocks.shrineUno) {
         texture = articuno_texture;
         shrineModel = ArticunoShrine;
      } else if (state.func_177230_c() == PixelmonBlocks.shrineTres) {
         texture = moltres_texture;
         shrineModel = MoltresShrine;
      } else {
         texture = zapdos_texture;
         shrineModel = ZapdosShrine;
      }

      this.func_147499_a(texture);
      if (used == EnumUsed.YES) {
         Orb.render();
      }

      shrineModel.render();
   }
}
