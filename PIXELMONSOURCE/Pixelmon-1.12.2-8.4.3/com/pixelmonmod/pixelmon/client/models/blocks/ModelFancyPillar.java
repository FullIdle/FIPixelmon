package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.blocks.decorative.BlockContainerPlus;
import com.pixelmonmod.pixelmon.blocks.decorative.BlockFancyPillar;
import com.pixelmonmod.pixelmon.blocks.enums.EnumAxis;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModelFancyPillar extends ModelEntityBlock {
   private static final ResourceLocation pillar = new ResourceLocation("pixelmon:textures/blocks/pillar.png");
   private static final ResourceLocation pillarDamaged = new ResourceLocation("pixelmon:textures/blocks/pillar_fractured.png");
   PixelmonModelRenderer platform = new PixelmonModelRenderer(this);
   PixelmonModelRenderer column = new PixelmonModelRenderer(this);
   PixelmonModelRenderer fracturedBottom = new PixelmonModelRenderer(this);
   PixelmonModelRenderer fracturedTop = new PixelmonModelRenderer(this);

   public ModelFancyPillar() {
      this.platform.addCustomModel(new ModelCustomWrapper((IPixelmonModel)EnumCustomModel.PillarPlatform.getModel()));
      this.column.addCustomModel(new ModelCustomWrapper((IPixelmonModel)EnumCustomModel.PillarColumn.getModel()));
      this.fracturedBottom.addCustomModel(new ModelCustomWrapper((IPixelmonModel)EnumCustomModel.PillarColumnFracturedBottom.getModel()));
      this.fracturedTop.addCustomModel(new ModelCustomWrapper((IPixelmonModel)EnumCustomModel.PillarColumnFracturedTop.getModel()));
   }

   public void renderTileEntity(TileEntityDecorativeBase tileEnt, float scale) {
      BlockFancyPillar theBlock = (BlockFancyPillar)tileEnt.func_145838_q();
      IBlockState state = tileEnt.func_145831_w().func_180495_p(tileEnt.func_174877_v());
      BlockFancyPillar.Connections connections = theBlock.getConnections(tileEnt.func_145831_w(), tileEnt.func_174877_v(), state);
      GlStateManager.func_179094_E();
      GlStateManager.func_179091_B();
      if (!theBlock.getIsDamaged(state)) {
         this.renderStandardPillar(connections, scale);
      } else {
         this.renderDamagedPillar(connections, scale, tileEnt);
      }

      GlStateManager.func_179121_F();
   }

   public void renderStandardPillar(BlockFancyPillar.Connections connections, float scale) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(pillar);
      this.column.func_78785_a(scale);
      if (!connections.top) {
         this.platform.func_78793_a(0.0F, 6.5F, 0.0F);
         this.platform.func_78785_a(scale);
      }

      if (!connections.bottom) {
         this.platform.func_78793_a(0.0F, -6.5F, 0.0F);
         this.platform.func_78785_a(scale);
      }

   }

   public void renderDamagedPillarInv(float scale) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(pillarDamaged);
      this.platform.func_78793_a(0.0F, 6.5F, 0.0F);
      this.platform.func_78785_a(scale);
      this.platform.func_78793_a(0.0F, -6.5F, 0.0F);
      this.platform.func_78785_a(scale);
      this.fracturedBottom.func_78785_a(scale);
      this.fracturedTop.func_78785_a(scale);
   }

   public void renderDamagedPillar(BlockFancyPillar.Connections connections, float scale, TileEntityDecorativeBase tileEnt) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(pillarDamaged);
      BlockFancyPillar theBlock = (BlockFancyPillar)tileEnt.func_145838_q();
      IBlockState state = tileEnt.func_145831_w().func_180495_p(tileEnt.func_174877_v());
      boolean renderTopPlatform = false;
      boolean renderBotPlatform = false;
      boolean renderTopDamaged = false;
      boolean renderBotDamaged = false;
      EnumAxis axis = (EnumAxis)state.func_177229_b(BlockContainerPlus.AXIS);
      int index = axis.ordinal() - 1;
      if (index < 0) {
         index = 0;
      }

      Boolean bottomState = theBlock.isEnd(tileEnt.func_145831_w(), tileEnt.func_174877_v(), BlockFancyPillar.DOWN_VALS[index]);
      if (bottomState != null) {
         if (!bottomState) {
            renderBotPlatform = true;
         } else if (bottomState) {
            renderBotDamaged = true;
         }
      }

      Boolean topState = theBlock.isEnd(tileEnt.func_145831_w(), tileEnt.func_174877_v(), BlockFancyPillar.UP_VALS[index]);
      if (topState != null) {
         if (!topState) {
            renderTopPlatform = true;
         } else if (topState) {
            renderTopDamaged = true;
         }
      }

      if (renderBotDamaged) {
         this.fracturedTop.func_78785_a(scale);
      }

      if (renderTopDamaged) {
         this.fracturedBottom.func_78785_a(scale);
      }

      if (renderTopPlatform) {
         this.platform.func_78793_a(0.0F, -6.5F, 0.0F);
         this.platform.func_78785_a(scale);
         if (!renderTopDamaged && !renderBotDamaged) {
            this.column.func_78785_a(scale);
         }
      }

      if (renderBotPlatform) {
         this.platform.func_78793_a(0.0F, 6.5F, 0.0F);
         this.platform.func_78785_a(scale);
         if (!renderTopDamaged && !renderBotDamaged) {
            this.column.func_78785_a(scale);
         }
      }

      if (!renderTopDamaged && !renderBotDamaged && !renderTopPlatform && !renderBotPlatform) {
         this.column.func_78785_a(scale);
      }

   }
}
