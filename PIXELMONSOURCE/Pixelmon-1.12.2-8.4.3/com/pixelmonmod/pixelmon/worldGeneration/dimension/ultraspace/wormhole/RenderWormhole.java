package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.wormhole;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWormhole extends Render {
   public RenderWormhole(RenderManager renderManagerIn) {
      super(renderManagerIn);
   }

   @Nullable
   protected ResourceLocation func_110775_a(Entity entity) {
      return null;
   }

   public void func_76986_a(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
   }
}
