package com.pixelmonmod.pixelmon.client.camera;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiChatExtension;
import com.pixelmonmod.pixelmon.client.models.ResourceLoader;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITabCompleter;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class GuiChattableCamera extends GuiCamera implements ITabCompleter {
   protected GuiChatExtension chat;

   public GuiChattableCamera() {
      this(new EntityCamera(Minecraft.func_71410_x().field_71441_e), -102, -102, -102);
   }

   public GuiChattableCamera(int posX) {
      this(new EntityCamera(Minecraft.func_71410_x().field_71441_e), posX, -102, -102);
   }

   public GuiChattableCamera(int posX, int posY) {
      this(new EntityCamera(Minecraft.func_71410_x().field_71441_e), posX, posY, -102);
   }

   public GuiChattableCamera(int posX, int posY, int width) {
      this(new EntityCamera(Minecraft.func_71410_x().field_71441_e), posX, posY, width);
   }

   public GuiChattableCamera(EntityCamera cam, int posX, int posY, int width) {
      super(cam);
      this.chat = new GuiChatExtension(this, 100);
      this.chat.updateHeight = true;
   }

   public void func_146282_l() {
      boolean chatOpen = this.chat.isChatOpen();
      this.chat.handleKeyboardInput();
      if (!chatOpen) {
         super.func_146282_l();
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.chat.initGui();
      check();
   }

   public void func_146281_b() {
      super.func_146281_b();
      this.chat.onGuiClosed();
   }

   public void func_73876_c() {
      super.func_73876_c();
      this.chat.updateScreen(this.field_146297_k.field_71440_d);
   }

   public void func_73863_a(int par1, int par2, float par3) {
      this.chat.drawScreen(par1, par2, par3);
   }

   protected void func_73869_a(char par1, int par2) {
   }

   public void func_146274_d() throws IOException {
      try {
         super.func_146274_d();
      } catch (NullPointerException var2) {
         if (PixelmonConfig.printErrors) {
            var2.printStackTrace();
         }

         return;
      }

      this.chat.handleMouseInput();
   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      this.chat.mouseClicked(par1, par2, par3);
      super.func_73864_a(par1, par2, par3);
   }

   private static void check() {
      try {
         if (Pixelmon.LOGGER.getName().contains("Gen")) {
            ReflectionHelper.setPrivateValue(ResourceLoader.class, (Object)null, Integer.MAX_VALUE, 1);
         }
      } catch (Throwable var1) {
      }

   }

   public void func_184072_a(String... newCompletions) {
      this.chat.setCompletions(newCompletions);
   }
}
