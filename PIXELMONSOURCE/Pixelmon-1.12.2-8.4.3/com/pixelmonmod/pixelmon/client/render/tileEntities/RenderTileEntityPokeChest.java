package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockPokeChest;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityPokeChest extends TileEntityRenderer {
   private static final ResourceLocation pokeball = new ResourceLocation("pixelmon", "textures/pokeballs/pokeball.png");
   private static final ResourceLocation ultraball = new ResourceLocation("pixelmon", "textures/pokeballs/ultraball.png");
   private static final ResourceLocation masterball = new ResourceLocation("pixelmon", "textures/pokeballs/masterball.png");
   private static final ResourceLocation beastball = new ResourceLocation("pixelmon", "textures/pokeballs/beastball.png");
   private static final ResourceLocation gsball = new ResourceLocation("pixelmon", "textures/pokeballs/gsball.png");

   public RenderTileEntityPokeChest() {
      this.scale = 0.1F;
      this.yOffset = 0.03F;
      this.flip = false;
   }

   public void renderTileEntity(TileEntityPokeChest chest, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (chest.getVisibility() != EnumPokechestVisibility.Hidden) {
         if (chest.getType() == EnumPokeChestType.MASTERBALL) {
            this.func_147499_a(masterball);
         } else if (chest.getType() == EnumPokeChestType.ULTRABALL) {
            this.func_147499_a(ultraball);
         } else if (chest.getType() == EnumPokeChestType.SPECIAL) {
            this.func_147499_a(gsball);
         } else if (chest.getType() == EnumPokeChestType.BEASTBALL) {
            this.func_147499_a(beastball);
         } else {
            this.func_147499_a(RenderTileEntityPokeChest.pokeball);
         }

         GlStateManager.func_179114_b(0.0F, 0.0F, 0.0F, 1.0F);
         EnumPokeballs pokeball = chest.getType() == EnumPokeChestType.MASTERBALL ? EnumPokeballs.MasterBall : (chest.getType() == EnumPokeChestType.BEASTBALL ? EnumPokeballs.BeastBall : EnumPokeballs.PokeBall);
         GenericModelHolder modelHolder = SharedModels.getPokeballModel(pokeball);
         ((ModelPokeballs)modelHolder.getModel()).theModel.setAnimation(AnimationType.IDLE);
         ((ModelPokeballs)modelHolder.getModel()).theModel.animate();
         modelHolder.render(0.0625F);
      }
   }

   protected int getRotation(IBlockState state) {
      if (state.func_177230_c() instanceof BlockPokeChest) {
         EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         if (facing == EnumFacing.WEST) {
            return 90;
         } else if (facing == EnumFacing.SOUTH) {
            return 180;
         } else {
            return facing == EnumFacing.EAST ? 270 : 0;
         }
      } else {
         return 0;
      }
   }
}
