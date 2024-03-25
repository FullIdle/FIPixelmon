package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiItemDrops;
import com.pixelmonmod.tcg.proxy.TCGClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GuiTCGOverlay extends Gui {
   public FontRenderer f;
   public static boolean isVisible = true;

   public GuiTCGOverlay() {
      this.f = Minecraft.func_71410_x().field_71466_p;
   }

   @SubscribeEvent
   public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
      if (event.getType() == ElementType.ALL) {
         if (Minecraft.func_71410_x().field_71462_r == null) {
            isVisible = true;
         }

         if ((!(Minecraft.func_71410_x().field_71462_r instanceof GuiInventory) || event == null) && isVisible && !Minecraft.func_71410_x().field_71474_y.field_74319_N && !(Minecraft.func_71410_x().field_71462_r instanceof GuiItemDrops)) {
            Minecraft mc = Minecraft.func_71410_x();
            GL11.glPushMatrix();
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            this.f.func_78264_a(true);
            mc.field_71460_t.func_78478_c();
            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg", "textures/items/card.png"));
            Gui.func_146110_a(event.getResolution().func_78326_a() - 32, event.getResolution().func_78328_b() - 90, 0.0F, 0.0F, 22, 22, 22.0F, 22.0F);
            String keyCode = GameSettings.func_74298_c(TCGClientProxy.openTCGCraftingMenu.func_151463_i());
            this.f.func_78276_b(keyCode, event.getResolution().func_78326_a() - 10 - Minecraft.func_71410_x().field_71466_p.func_78256_a(keyCode), event.getResolution().func_78328_b() - 100, 16777215);
            this.f.func_78264_a(false);
            RenderHelper.func_74518_a();
            GlStateManager.func_179140_f();
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179126_j();
            GL11.glPopMatrix();
         }
      }
   }
}
