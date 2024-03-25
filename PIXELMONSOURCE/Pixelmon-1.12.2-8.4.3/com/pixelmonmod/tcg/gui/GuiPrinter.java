package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.item.containers.ContainerPrinter;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.PrinterStartPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPrinter extends GuiContainer {
   private static final int PRINT_BUTTON_ID = 1;
   public static final ResourceLocation guiTextures = new ResourceLocation("tcg:gui/printer.png");
   private final TileEntityPrinter tileEntityPrinter;
   private int posX;
   private int posY;
   private GuiButton printButton;

   public GuiPrinter(InventoryPlayer inventoryPlayer, TileEntityPrinter tileEntityPrinter) {
      super(new ContainerPrinter(inventoryPlayer, tileEntityPrinter));
      this.tileEntityPrinter = tileEntityPrinter;
      this.field_147000_g = 185;
   }

   public void func_73866_w_() {
      this.posX = (this.field_146294_l - this.field_146999_f) / 2;
      this.posY = (this.field_146295_m - this.field_147000_g) / 2;
      this.printButton = new GuiButton(1, this.posX + 29, this.posY + 30, 40, 20, "Print");
      super.func_73866_w_();
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.tileEntityPrinter.canPrint()) {
         if (!this.field_146292_n.contains(this.printButton)) {
            this.field_146292_n.add(this.printButton);
         }
      } else if (this.field_146292_n.contains(this.printButton)) {
         this.field_146292_n.remove(this.printButton);
      }

      this.printButton.field_146124_l = this.tileEntityPrinter.getPrintTime() == 0;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      switch (button.field_146127_k) {
         case 1:
            this.startPrinting();
         default:
            super.func_146284_a(button);
      }
   }

   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(guiTextures);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      short printTime = this.tileEntityPrinter.getPrintTime();
      if (printTime > 0) {
         float printRatio = (float)printTime / 200.0F;
         int height = (int)Math.ceil(16.0 * (double)printRatio);
         this.func_73729_b(k + 58, l + 59, 182, 16 - height, 16, height);
      }

   }

   protected void func_146979_b(int mouseX, int mouseY) {
      if (!this.tileEntityPrinter.canPrint()) {
         this.field_146289_q.func_78276_b("TCG", this.field_146999_f / 2 - this.field_146289_q.func_78256_a("TCG") / 2 - 38, 37, 4210752);
      }

      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.inventory", new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
   }

   private void startPrinting() {
      PacketHandler.net.sendToServer(new PrinterStartPacket(this.tileEntityPrinter.func_174877_v()));
   }

   public void func_73863_a(int par1, int par2, float par3) {
      super.func_73863_a(par1, par2, par3);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      GlStateManager.func_179141_d();
      this.func_191948_b(par1, par2);
      GlStateManager.func_179118_c();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }
}
