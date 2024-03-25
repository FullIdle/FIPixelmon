package com.pixelmonmod.pixelmon.client.camera;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;

public class GuiCamera extends GuiScreen {
   boolean mouseDown;

   public GuiCamera() {
      this(CameraMode.Battle);
   }

   public GuiCamera(CameraMode mode) {
      this(new EntityCamera(Minecraft.func_71410_x().field_71441_e, mode));
   }

   public GuiCamera(EntityCamera cam) {
      this.mouseDown = false;
      if (PixelmonConfig.useBattleCamera) {
         Minecraft minecraft = Minecraft.func_71410_x();
         minecraft.func_152344_a(() -> {
            ClientProxy.camera = cam;
            Entity renderEntity = minecraft.func_175606_aa();
            this.getCamera().func_70107_b(renderEntity.field_70165_t, renderEntity.field_70163_u, renderEntity.field_70161_v);
            minecraft.field_71441_e.func_73027_a(ClientProxy.camera.func_145782_y(), ClientProxy.camera);
            cam.getMovement().generatePositions();
         });
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_146282_l() {
      if (ClientProxy.camera != null) {
         ClientProxy.camera.getMovement().handleKeyboardInput();
      }

   }

   public void func_146274_d() throws IOException {
      if (ClientProxy.camera != null && ClientProxy.camera.getTarget() != null && ClientProxy.camera.getTarget().isValidTarget()) {
         int dx = 0;
         int dy = 0;
         if (Mouse.isButtonDown(1)) {
            if (!this.mouseDown) {
               Mouse.getDX();
               Mouse.getDY();
            }

            if (this.mouseDown) {
               dx = Mouse.getDX();
               dy = Mouse.getDY();
            }

            this.mouseDown = true;
         } else if (this.mouseDown) {
            this.mouseDown = false;
         }

         ClientProxy.camera.getMovement().handleMouseMovement(dx, dy, Mouse.getDWheel());
      }

      super.func_146274_d();
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public EntityCamera getCamera() {
      return ClientProxy.camera;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146291_p = true;
   }

   public void func_146281_b() {
      super.func_146281_b();
      this.field_146297_k.func_71381_h();
   }
}
