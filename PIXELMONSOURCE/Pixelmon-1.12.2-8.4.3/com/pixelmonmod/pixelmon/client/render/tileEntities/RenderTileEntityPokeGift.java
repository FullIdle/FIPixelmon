package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockPokegift;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokegift;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityPokeGift extends TileEntityRenderer {
   private static final ResourceLocation pokegift = new ResourceLocation("pixelmon", "textures/pokeballs/cherishball.png");

   public RenderTileEntityPokeGift() {
      this.scale = 0.1F;
      this.yOffset = 0.03F;
      this.flip = false;
   }

   public void renderTileEntity(TileEntityPokegift te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (te.getVisibility() != EnumPokechestVisibility.Hidden) {
         this.func_147499_a(pokegift);
         GenericModelHolder modelHolder = SharedModels.getPokeballModel(EnumPokeballs.CherishBall);
         ((ModelPokeballs)modelHolder.getModel()).theModel.setAnimation(AnimationType.IDLE);
         ((ModelPokeballs)modelHolder.getModel()).theModel.animate();
         modelHolder.render(0.0625F);
      }
   }

   protected int getRotation(IBlockState state) {
      if (state.func_177230_c() instanceof BlockPokegift) {
         EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         if (facing == EnumFacing.EAST) {
            return 270;
         }

         if (facing == EnumFacing.NORTH) {
            return 0;
         }

         if (facing == EnumFacing.WEST) {
            return 90;
         }

         if (facing == EnumFacing.SOUTH) {
            return 180;
         }
      }

      return 0;
   }
}
