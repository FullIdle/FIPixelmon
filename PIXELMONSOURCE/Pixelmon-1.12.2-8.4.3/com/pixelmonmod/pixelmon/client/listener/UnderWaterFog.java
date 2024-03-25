package com.pixelmonmod.pixelmon.client.listener;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnderWaterFog {
   @SubscribeEvent
   public void checkFog(EntityViewRenderEvent.FogColors event) {
      Entity entity = event.getEntity();
      if (entity instanceof EntityPlayer && entity.func_184187_bx() != null && entity.func_184187_bx() instanceof EntityPixelmon) {
         EntityPixelmon p = (EntityPixelmon)entity.func_184187_bx();
         IBlockState blockstate = ActiveRenderInfo.func_186703_a(entity.field_70170_p, entity, (float)event.getRenderPartialTicks());
         if (p.getBaseStats() != null && p.getBaseStats().canSurf() && blockstate != null && blockstate.func_185904_a() == Material.field_151586_h) {
            event.setBlue(event.getBlue() * 6.0F + 0.3F);
            event.setRed(event.getRed() * 6.0F + 0.3F);
            event.setGreen(event.getGreen() * 6.0F + 0.3F);
         }
      }

   }

   @SubscribeEvent
   public void checkFogDensity(EntityViewRenderEvent.FogDensity event) {
      Entity entity = event.getEntity();
      if (entity instanceof EntityPlayer && entity.func_184187_bx() != null && entity.func_184187_bx() instanceof EntityPixelmon) {
         EntityPixelmon p = (EntityPixelmon)entity.func_184187_bx();
         IBlockState blockstate = ActiveRenderInfo.func_186703_a(entity.field_70170_p, entity, (float)event.getRenderPartialTicks());
         if (p.getBaseStats() != null && p.getBaseStats().canSurf() && blockstate != null && blockstate.func_185904_a() == Material.field_151586_h) {
            GlStateManager.func_187430_a(FogMode.EXP);
            event.setDensity(0.01F);
            event.setCanceled(true);
         }
      }

   }
}
