package com.pixelmonmod.tcg.block.renderers;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.tcg.duel.RenderDuel;
import com.pixelmonmod.tcg.helper.GuiHelper;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderBattleController extends TileEntitySpecialRenderer {
   private ResourceLocation texture;
   private int rotateDegrees;

   public boolean isGlobalRenderer(TileEntityBattleController te) {
      return true;
   }

   public void render(TileEntityBattleController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      EnumFacing facing = getFacing(te);
      this.rotateDegrees = GuiHelper.getRotationFromFacing(facing);
      GlStateManager.func_179091_B();
      GlStateManager.func_179094_E();
      GlStateManager.func_179129_p();
      GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.0F, (float)z + 0.5F);
      GlStateManager.func_179114_b((float)this.rotateDegrees, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179089_o();
      GlStateManager.func_179101_C();
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b((double)((float)x + (facing != EnumFacing.WEST && facing != EnumFacing.NORTH ? 0.0F : 1.0F)), y + 0.10000000149011612, (double)((float)z + (facing != EnumFacing.SOUTH && facing != EnumFacing.WEST ? 1.0F : 0.0F)));
      GlStateManager.func_179114_b((float)(this.rotateDegrees + 180), 0.0F, 1.0F, 0.0F);
      RenderDuel.drawBoardOutline(te, 0.0, 0.0, 0.0, partialTicks, this.func_147498_b());
      GlStateManager.func_179121_F();
   }

   public static EnumFacing getFacing(TileEntityBattleController te) {
      IBlockState state = Minecraft.func_71410_x().field_71441_e.func_180495_p(te.func_174877_v());
      Block block = state.func_177230_c();
      EnumFacing facing = EnumFacing.NORTH;
      if (block != null) {
         if (block instanceof MultiBlock) {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         } else if (block instanceof GenericRotatableModelBlock) {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         }
      }

      return facing;
   }
}
