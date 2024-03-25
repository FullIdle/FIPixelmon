package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.machines.BlockHealer;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityHealer;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RenderTileEntityHealer extends TileEntityRenderer {
   private static final GenericModelHolder modelHealer = new GenericModelHolder("blocks/healer/healer.pqc");

   public RenderTileEntityHealer() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityHealer healer, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(healer.getTexture());
      modelHealer.render(1.0F);
      if (healer.stayDark) {
         healer.rotation = 0.0F;
      }

      if (healer.beingUsed) {
         for(int slot = 0; slot < healer.pokeballType.length; ++slot) {
            if (healer.pokeballType[slot] != null) {
               this.renderPokeball(slot, healer);
            }
         }
      }

   }

   protected int getRotation(IBlockState state) {
      if (state.func_177230_c() instanceof BlockHealer) {
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

   private void renderPokeball(int slot, TileEntityHealer tile) {
      GlStateManager.func_179094_E();
      float y = -0.8F;
      switch (slot) {
         case 0:
            GlStateManager.func_179109_b(-0.12F, -0.8F, 0.25F);
            break;
         case 1:
            GlStateManager.func_179109_b(0.12F, -0.8F, 0.25F);
            break;
         case 2:
            GlStateManager.func_179109_b(-0.12F, -0.8F, 0.0F);
            break;
         case 3:
            GlStateManager.func_179109_b(0.12F, -0.8F, 0.0F);
            break;
         case 4:
            GlStateManager.func_179109_b(-0.12F, -0.8F, -0.25F);
            break;
         case 5:
            GlStateManager.func_179109_b(0.12F, -0.8F, -0.25F);
      }

      GlStateManager.func_179114_b(tile.rotation + 180.0F, 0.0F, 1.0F, 0.0F);
      if (tile.allPlaced) {
         if (tile.flashTimer >= 6 && !tile.stayDark) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         } else {
            GlStateManager.func_179131_c(0.3F, 0.3F, 0.3F, 1.0F);
         }

         if (tile.flashTimer >= 10) {
            tile.flashTimer = 0;
         }
      }

      this.func_147499_a(tile.pokeballType[slot].getTextureLocation());
      GenericModelHolder pokeball = SharedModels.getPokeballModel(tile.pokeballType[slot]);
      GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 60.6F);
      ModelPokeballs model = (ModelPokeballs)pokeball.getModel();
      model.theModel.setAnimation(AnimationType.IDLE);
      model.theModel.animate();
      model.render((Entity)null, EntityPokeBall.scale);
      GlStateManager.func_179121_F();
   }
}
