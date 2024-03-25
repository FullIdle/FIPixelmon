package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.machines.BlockAnvil;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityAnvil;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelAnvil;
import com.pixelmonmod.pixelmon.client.models.discs.ModelDiscFlat;
import com.pixelmonmod.pixelmon.client.models.discs.ModelDiscHemiSphere;
import com.pixelmonmod.pixelmon.client.models.discs.ModelDiscStage1;
import com.pixelmonmod.pixelmon.client.models.discs.ModelDiscStage2;
import com.pixelmonmod.pixelmon.client.models.plates.ModelIngot;
import com.pixelmonmod.pixelmon.client.models.plates.ModelPlate;
import com.pixelmonmod.pixelmon.client.models.plates.ModelPlateStage2;
import com.pixelmonmod.pixelmon.client.models.plates.ModelPlateStage3;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonOres;
import com.pixelmonmod.pixelmon.items.ItemPokeballDisc;
import com.pixelmonmod.pixelmon.items.ItemPokeballLid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityAnvil extends TileEntityRenderer {
   private static final ResourceLocation anvil_texture = new ResourceLocation("pixelmon:textures/blocks/anvil.png");
   private static final ResourceLocation aluminiumIngot_texture = new ResourceLocation("pixelmon:textures/blocks/aluminium/ingot.png");
   private static final ResourceLocation ironDisc_texture = new ResourceLocation("pixelmon:textures/pokeballs/irondisc.png");
   private static final GenericModelHolder model = new GenericModelHolder(ModelAnvil.class);
   private static final GenericModelHolder modelDiscFlat = new GenericModelHolder(ModelDiscFlat.class);
   private static final GenericModelHolder modelDiscHemiSphere = new GenericModelHolder(ModelDiscHemiSphere.class);
   private static final GenericModelHolder modelDiscStage1 = new GenericModelHolder(ModelDiscStage1.class);
   private static final GenericModelHolder modelDiscStage2 = new GenericModelHolder(ModelDiscStage2.class);
   private static final GenericModelHolder modelPlateIngot = new GenericModelHolder(ModelIngot.class);
   private static final GenericModelHolder modelPlate = new GenericModelHolder(ModelPlate.class);
   private static final GenericModelHolder modelPlateStage2 = new GenericModelHolder(ModelPlateStage2.class);
   private static final GenericModelHolder modelPlateStage3 = new GenericModelHolder(ModelPlateStage3.class);

   public RenderTileEntityAnvil() {
      this.yOffset = 1.5F;
      this.correctionAngles = 90;
   }

   public void renderTileEntity(TileEntityAnvil anvil, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(anvil_texture);
      GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
      ((ModelAnvil)model.getModel()).renderModel(anvil, 0.0625F);
      if (!anvil.stack.func_190926_b()) {
         Item item = anvil.stack.func_77973_b();
         if (PixelmonOres.itemMatches(anvil.stack, "ingotAluminum")) {
            this.func_147499_a(aluminiumIngot_texture);
            if (anvil.state == 0) {
               ((ModelIngot)modelPlateIngot.getModel()).renderModel(0.0625F);
            } else if (anvil.state == 1) {
               ((ModelPlateStage2)modelPlateStage2.getModel()).renderModel(0.0625F);
            } else if (anvil.state == 2) {
               ((ModelPlateStage3)modelPlateStage3.getModel()).renderModel(0.0625F);
            }
         } else if (item == PixelmonItems.aluminiumPlate) {
            this.func_147499_a(aluminiumIngot_texture);
            ((ModelPlate)modelPlate.getModel()).renderModel(0.0625F);
         } else if (!(item instanceof ItemPokeballDisc) && item != PixelmonItemsPokeballs.ironDisc && item != PixelmonItemsPokeballs.aluDisc) {
            if (item instanceof ItemPokeballLid || item == PixelmonItemsPokeballs.ironBase || item == PixelmonItemsPokeballs.aluBase) {
               if (item != PixelmonItemsPokeballs.ironBase && item != PixelmonItemsPokeballs.aluBase) {
                  this.func_147499_a(new ResourceLocation("pixelmon:textures/pokeballs/anvil/" + ((ItemPokeballLid)item).pokeball.getTexture()));
               } else {
                  this.func_147499_a(ironDisc_texture);
               }

               ((ModelDiscHemiSphere)modelDiscHemiSphere.getModel()).renderModel(0.0625F);
            }
         } else {
            if (item != PixelmonItemsPokeballs.ironDisc && item != PixelmonItemsPokeballs.aluDisc) {
               this.func_147499_a(new ResourceLocation("pixelmon:textures/pokeballs/anvil/" + ((ItemPokeballDisc)item).pokeball.getTexture()));
            } else {
               this.func_147499_a(ironDisc_texture);
            }

            if (anvil.state == 0) {
               ((ModelDiscFlat)modelDiscFlat.getModel()).renderModel(0.0625F);
            } else if (anvil.state == 1) {
               ((ModelDiscStage1)modelDiscStage1.getModel()).renderModel(0.0625F);
            } else if (anvil.state == 2) {
               ((ModelDiscStage2)modelDiscStage2.getModel()).renderModel(0.0625F);
            }
         }
      }

   }

   protected int getRotation(IBlockState state) {
      if (state.func_177230_c() instanceof BlockAnvil) {
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
