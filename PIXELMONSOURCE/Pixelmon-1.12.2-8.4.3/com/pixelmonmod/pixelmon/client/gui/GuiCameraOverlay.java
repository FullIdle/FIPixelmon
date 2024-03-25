package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.items.ItemCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiCameraOverlay {
   public static boolean isCameraGuiOn = false;

   public static void checkCameraOn() {
      ItemStack heldItemStack = Minecraft.func_71410_x().field_71439_g.func_184586_b(EnumHand.MAIN_HAND);
      if (heldItemStack.func_190926_b() || !(heldItemStack.func_77973_b() instanceof ItemCamera)) {
         isCameraGuiOn = false;
      }

      if (isCameraGuiOn) {
         GuiIngameForge.renderHotbar = false;
         GuiIngameForge.renderCrosshairs = false;
         GuiIngameForge.renderExperiance = false;
         GuiIngameForge.renderArmor = false;
      } else {
         GuiIngameForge.renderHotbar = true;
         GuiIngameForge.renderCrosshairs = true;
         GuiIngameForge.renderExperiance = true;
         GuiIngameForge.renderArmor = true;
      }

   }

   public static void renderCamera(int screenWidth, int screenHeight, Minecraft mc) {
      float zLevel = -90.0F;
      Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.cameraOverlay);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)screenWidth, (float)screenHeight, 0.0, 0.0, 1.0, 1.0, zLevel);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.cameraControls);
      int controlsWidth = 57;
      int controlsHeight = 80;
      GuiHelper.drawImageQuad((double)(screenWidth / 2 - controlsWidth / 2), (double)(screenHeight / 2 - controlsHeight / 2), (double)controlsWidth, (float)controlsHeight, 0.0, 0.0, 1.0, 1.0, zLevel);
      EntityPlayer player = mc.field_71439_g;
      if (player.field_71071_by.func_70431_c(new ItemStack(PixelmonItems.filmItem))) {
         int filmInInv = 0;

         for(int i = 0; i < player.field_71071_by.func_70302_i_(); ++i) {
            ItemStack stackInSlot = player.field_71071_by.func_70301_a(i);
            if (!stackInSlot.func_190926_b() && stackInSlot.func_77973_b() == PixelmonItems.filmItem) {
               filmInInv += stackInSlot.func_190916_E();
            }
         }

         ScaledResolution scaledResolution = new ScaledResolution(mc);
         int x = scaledResolution.func_78326_a() - scaledResolution.func_78326_a() / 4;
         int y = scaledResolution.func_78328_b() / 4;
         mc.func_175599_af().func_180450_b(new ItemStack(PixelmonItems.filmItem), x, y);
         mc.field_71466_p.func_175065_a("" + filmInInv, (float)(x + 16), (float)y, 16777215, false);
      }

   }

   @SubscribeEvent
   public static void renderHandEvent(RenderHandEvent event) {
      if (isCameraGuiOn) {
         event.setCanceled(true);
      }

   }
}
