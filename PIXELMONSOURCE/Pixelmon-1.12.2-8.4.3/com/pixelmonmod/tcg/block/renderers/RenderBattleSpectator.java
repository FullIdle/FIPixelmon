package com.pixelmonmod.tcg.block.renderers;

import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.tileEntities.GenericRenderer;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import net.minecraft.util.ResourceLocation;

public class RenderBattleSpectator extends GenericRenderer {
   public RenderBattleSpectator() {
      super("tcg:textures/blocks/battlespectator/Texture_red.png", new GenericSmdModel("models/blocks/computer", "computer.pqc"), 180);
   }

   public void render(TileEntityBattleSpectator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      if (te.getPlayerIndex() == 0) {
         super.setTexture(new ResourceLocation("tcg:textures/blocks/battlespectator/Texture_red.png"));
      } else {
         super.setTexture(new ResourceLocation("tcg:textures/blocks/battlespectator/Texture_light_blue.png"));
      }

      super.func_192841_a(te, x, y, z, partialTicks, destroyStage, alpha);
   }
}
