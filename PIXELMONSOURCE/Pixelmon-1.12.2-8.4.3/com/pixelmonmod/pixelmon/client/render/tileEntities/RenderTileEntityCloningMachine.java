package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelBrokenCloningMachine;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelCloningMachine;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelEntityBlock;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTileEntityCloningMachine extends TileEntityRenderer {
   private static final ResourceLocation cloningMachineTex = new ResourceLocation("pixelmon:textures/blocks/cloner.png");
   private static final ResourceLocation cloningMachineBlocks = new ResourceLocation("pixelmon:textures/gui/clonerBlocks.png");
   private static final GenericModelHolder model = new GenericModelHolder(ModelCloningMachine.class);
   private static final GenericModelHolder brokenModel = new GenericModelHolder(ModelBrokenCloningMachine.class);
   private EntityPixelmon pokemon;

   public RenderTileEntityCloningMachine() {
      this.flip = false;
   }

   public void renderTileEntity(TileEntityCloningMachine machine, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(cloningMachineTex);
      ((ModelEntityBlock)(machine.isBroken ? brokenModel : model).getModel()).renderTileEntity(machine, 0.0625F);
      GlStateManager.func_179121_F();
      EnumFacing facing = EnumFacing.NORTH;
      int rotation = super.getRotation(state);
      if (state.func_177230_c() instanceof MultiBlock) {
         facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
      }

      GlStateManager.func_179094_E();
      if (machine.hasMew() && !machine.isBroken && !machine.isFinished) {
         if (this.pokemon == null) {
            this.pokemon = PokemonSpec.from(EnumSpecies.Mew.name).create(this.func_178459_a());
            this.pokemon.getPokemonData().setGrowth(EnumGrowth.Runt);
            this.pokemon.getLvl().setLevel(1);
            this.pokemon.getPokemonData().setShiny(machine.mew.isShiny());
            this.pokemon.initAnimation();
         }

         GlStateManager.func_179094_E();
         if (this.pokemon != null) {
            GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.45F, (float)z + 0.5F);
            GlStateManager.func_179114_b((float)rotation, 0.0F, 1.0F, 0.0F);
            this.pokemon.func_70012_b(x, y, z, 0.0F, 0.0F);
            this.pokemon.setAnimation(AnimationType.IDLE);
            if (this.pokemon.getAnimationVariables().hasInt(-1)) {
               this.pokemon.getAnimationVariables().getCounter(-1).value = 30.0F;
            }

            Minecraft.func_71410_x().func_175598_ae().func_188391_a(this.pokemon, 0.0, 0.0, 0.0, 0.0F, partialTicks, false);
         }

         GlStateManager.func_179121_F();
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)x + 0.5F, (float)y, (float)z + 0.5F);
      GlStateManager.func_179114_b((float)rotation, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179152_a(1.0F, -1.0F, -1.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_147499_a(cloningMachineTex);
      if (machine.isBroken) {
         ((ModelBrokenCloningMachine)brokenModel.getModel()).renderGlass(machine, 0.0625F);
      } else {
         ((ModelCloningMachine)model.getModel()).renderGlass(machine, 0.0625F);
      }

      GlStateManager.func_179132_a(true);
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
      if (machine.isBroken) {
         this.renderLeftDisplay(machine, I18n.func_135052_a("pixelmon.blocks.cloningmachine.error", new Object[0]), facing, x, y, z, 64, false, -1);
      } else {
         if (machine.isFinished) {
            this.renderLeftDisplay(machine, I18n.func_135052_a("pixelmon.blocks.cloningmachine.finished", new Object[0]), facing, x, y, z, 64, false, -1);
         } else if (machine.hasMew()) {
            this.renderLeftDisplay(machine, I18n.func_135052_a("pixelmon.blocks.cloningmachine.scanning", new Object[0]), facing, x, y, z, 64, false, -1);
         } else {
            this.renderLeftDisplay(machine, I18n.func_135052_a("pixelmon.blocks.cloningmachine.mew", new Object[0]), facing, x, y, z, 64, false, -1);
         }

         if (machine.hasMew()) {
            this.renderRightDisplay(machine, I18n.func_135052_a("pixelmon.blocks.cloningmachine.catalyst", new Object[0]), facing, x, y, z, 64, false, -1);
         }

         if (!machine.pokemonName.equals("")) {
            if (machine.pixelmon == null) {
               machine.pixelmon = (new PokemonSpec(new String[]{machine.pokemonName, machine.isShiny ? "s" : "!s", "gr:Runt", "lvl:1"})).create(this.func_178459_a());
               machine.pixelmon.initAnimation();
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.5F + machine.xboost, (float)y + 0.8F, (float)z + 0.5F + machine.zboost);
            machine.pixelmon.func_70029_a(machine.func_145831_w());
            GlStateManager.func_179114_b((float)rotation, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179152_a(0.0F + (float)machine.pokemonProgress / 200.0F, 0.0F + (float)machine.pokemonProgress / 200.0F, 0.0F + (float)machine.pokemonProgress / 200.0F);
            Minecraft.func_71410_x().func_175598_ae().func_188391_a(machine.pixelmon, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);
            GlStateManager.func_179121_F();
         }
      }

   }

   private void renderLeftDisplay(TileEntityCloningMachine tile, String par2Str, EnumFacing facing, double par3, double par5, double par7, int par9, boolean par10, int par11) {
      int j = 0;
      if (facing == EnumFacing.EAST) {
         j = 270;
      } else if (facing == EnumFacing.NORTH) {
         j = 0;
      } else if (facing == EnumFacing.WEST) {
         j = 90;
      } else if (facing == EnumFacing.SOUTH) {
         j = 180;
      }

      FontRenderer var12 = this.func_147498_b();
      float var13 = 1.6F;
      float var14 = 0.011666667F * var13;
      GlStateManager.func_179094_E();
      if (facing == EnumFacing.NORTH) {
         GlStateManager.func_179109_b((float)par3 + 0.5F, (float)par5 + 0.9F, (float)par7 + 1.22F);
      } else if (facing == EnumFacing.WEST) {
         GlStateManager.func_179109_b((float)par3 + 1.23F, (float)par5 + 0.9F, (float)par7 + 0.5F);
      } else if (facing == EnumFacing.SOUTH) {
         GlStateManager.func_179109_b((float)par3 + 0.5F, (float)par5 + 0.9F, (float)par7 - 0.23F);
      } else {
         GlStateManager.func_179109_b((float)par3 - 0.23F, (float)par5 + 0.9F, (float)par7 + 0.51F);
      }

      GlStateManager.func_179114_b((float)(j + 180), 0.0F, 1.0F, 0.0F);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      if (!par10) {
         GlStateManager.func_179152_a(-var14, -var14, var14);
      } else {
         GlStateManager.func_179152_a(-var14 + 0.012F, -var14 + 0.012F, var14 + 0.012F);
      }

      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      byte var16 = 0;
      GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
      GlStateManager.func_179090_x();
      GlStateManager.func_179098_w();
      var12.func_78276_b(par2Str, -var12.func_78256_a(par2Str) / 2, var16, par11);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
   }

   private void renderRightDisplay(TileEntityCloningMachine tile, String par2Str, EnumFacing facing, double par3, double par5, double par7, int par9, boolean par10, int par11) {
      int j = 0;
      if (facing == EnumFacing.EAST) {
         j = 270;
      } else if (facing == EnumFacing.NORTH) {
         j = 0;
      } else if (facing == EnumFacing.WEST) {
         j = 90;
      } else if (facing == EnumFacing.SOUTH) {
         j = 180;
      }

      FontRenderer var12 = this.func_147498_b();
      float var13 = 1.6F;
      float var14 = 0.011666667F * var13;
      GlStateManager.func_179094_E();
      if (facing == EnumFacing.NORTH) {
         GlStateManager.func_179109_b((float)par3 + 3.87F, (float)par5 + 0.9F, (float)par7 + 1.22F);
      } else if (facing == EnumFacing.WEST) {
         GlStateManager.func_179109_b((float)par3 + 1.23F, (float)par5 + 0.9F, (float)par7 - 2.87F);
      } else if (facing == EnumFacing.SOUTH) {
         GlStateManager.func_179109_b((float)par3 - 2.87F, (float)par5 + 0.9F, (float)par7 - 0.23F);
      } else {
         GlStateManager.func_179109_b((float)par3 - 0.23F, (float)par5 + 0.9F, (float)par7 + 3.87F);
      }

      GlStateManager.func_179114_b((float)(j + 180), 0.0F, 1.0F, 0.0F);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      if (!par10) {
         GlStateManager.func_179152_a(-var14, -var14, var14);
      } else {
         GlStateManager.func_179152_a(-var14 + 0.012F, -var14 + 0.012F, var14 + 0.012F);
      }

      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      byte var16 = 0;
      GlStateManager.func_179152_a(0.3F, 0.3F, 0.3F);
      GlStateManager.func_179090_x();
      var12.func_78256_a(par2Str);
      GlStateManager.func_179098_w();
      if (tile.isFinished) {
         par2Str = I18n.func_135052_a("pixelmon.blocks.cloningmachine.retrieve", new Object[0]);
      } else if (tile.processingClone || tile.growingPokemon) {
         par2Str = I18n.func_135052_a("pixelmon.blocks.cloningmachine.processing", new Object[0]);
      }

      var12.func_78276_b(par2Str, -var12.func_78256_a(par2Str) / 2, var16, par11);
      if (!tile.processingClone && !tile.growingPokemon && !tile.isFinished) {
         this.func_147499_a(cloningMachineBlocks);
         GuiHelper.drawImageQuad(-30.0, 10.0, 60.0, 20.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
         par2Str = I18n.func_135052_a("pixelmon.blocks.cloningmachine.boost", new Object[0]) + " " + tile.boostCount + "/3";
         var12.func_78276_b(par2Str, -var12.func_78256_a(par2Str) / 2, var16 + 40, par11);
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder worldRenderer = tessellator.func_178180_c();
         int x = -30;
         int w = 60;
         int y = 35;
         int h = 17;
         int zLevel = 0;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179090_x();
         float boostPercent = (float)tile.boostLevel / 40.0F;
         worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
         worldRenderer.func_181662_b((double)x, (double)(y + h), (double)zLevel + 0.01).func_181666_a(0.2F + 0.3F * (1.0F - boostPercent), 0.2F + 0.3F * boostPercent, 0.2F, 1.0F).func_181675_d();
         worldRenderer.func_181662_b((double)(x + w), (double)(y + h), (double)zLevel + 0.01).func_181666_a(0.2F + 0.3F * (1.0F - boostPercent), 0.2F + 0.3F * boostPercent, 0.2F, 1.0F).func_181675_d();
         worldRenderer.func_181662_b((double)(x + w), (double)y, (double)zLevel + 0.01).func_181666_a(0.2F + 0.3F * (1.0F - boostPercent), 0.2F + 0.3F * boostPercent, 0.2F, 1.0F).func_181675_d();
         worldRenderer.func_181662_b((double)x, (double)y, (double)zLevel + 0.01).func_181666_a(0.2F + 0.3F * (1.0F - boostPercent), 0.2F + 0.3F * boostPercent, 0.2F, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GlStateManager.func_179098_w();
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
   }
}
