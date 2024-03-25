package com.pixelmonmod.pixelmon.client.gui.elements;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;

public abstract class GuiScreenDropDown extends GuiScreen {
   protected int xSize = 176;
   protected int ySize = 166;
   protected int guiLeft;
   protected int guiTop;
   private final GuiDropDownManager dropDownManager = new GuiDropDownManager();

   protected GuiScreenDropDown() {
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.guiLeft = (this.field_146294_l - this.xSize) / 2;
      this.guiTop = (this.field_146295_m - this.ySize) / 2;
      this.dropDownManager.clearDropDowns();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiButtonHoverDisable.setHoverDisabledScreen(this.field_146292_n, this.dropDownManager.isMouseOver(mouseX, mouseY));
      int i = this.guiLeft;
      int j = this.guiTop;
      this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
      GlStateManager.func_179101_C();
      RenderHelper.func_74518_a();
      GlStateManager.func_179140_f();
      GlStateManager.func_179097_i();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      RenderHelper.func_74520_c();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)i, (float)j, 0.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179091_B();
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.func_74518_a();
      this.drawGuiContainerForegroundLayer(mouseX, mouseY);
      RenderHelper.func_74520_c();
      GlStateManager.func_179121_F();
      GlStateManager.func_179145_e();
      GlStateManager.func_179126_j();
      RenderHelper.func_74519_b();
   }

   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      if (this.disableMenus()) {
         mouseY = -1;
         mouseX = -1;
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)(-this.guiLeft), (float)(-this.guiTop), 0.0F);
      this.dropDownManager.drawDropDowns(0.0F, mouseX, mouseY);
      GlStateManager.func_179121_F();
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      if (this.dropDownManager.isMouseOver(mouseX, mouseY)) {
         mouseY = -1;
         mouseX = -1;
      }

      this.drawBackgroundUnderMenus(partialTicks, mouseX, mouseY);
   }

   protected abstract void drawBackgroundUnderMenus(float var1, int var2, int var3);

   protected void func_73864_a(int mouseX, int mouseY, int button) throws IOException {
      if (!this.dropDownManager.mouseClicked(mouseX, mouseY, button)) {
         super.func_73864_a(mouseX, mouseY, button);
         this.mouseClickedUnderMenus(mouseX, mouseY, button);
      }
   }

   protected void mouseClickedUnderMenus(int mouseX, int mouseY, int button) throws IOException {
   }

   public GuiDropDown addDropDown(GuiDropDown dropDown) {
      this.dropDownManager.addDropDown(dropDown);
      return dropDown;
   }

   public void removeDropDown(GuiDropDown dropDown) {
      this.dropDownManager.removeDropDown(dropDown);
   }

   protected boolean disableMenus() {
      return false;
   }
}
