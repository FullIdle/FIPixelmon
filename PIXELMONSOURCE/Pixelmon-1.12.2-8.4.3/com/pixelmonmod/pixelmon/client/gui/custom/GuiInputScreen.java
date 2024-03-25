package com.pixelmonmod.pixelmon.client.gui.custom;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.keybindings.Descend;
import com.pixelmonmod.pixelmon.comm.packetHandlers.EnumMovementType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.Movement;
import com.pixelmonmod.pixelmon.enums.EnumMovement;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiInputScreen extends GuiScreen {
   private List movements = Lists.newArrayList();

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146291_p = true;
      Minecraft.func_71410_x().field_71415_G = true;
      Minecraft.func_71410_x().field_71417_B.func_74372_a();
   }

   public void func_146269_k() throws IOException {
      super.func_146269_k();
      if (Descend.Instance.func_151470_d()) {
         this.movements.add(EnumMovement.Descend);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151463_i())) {
         this.movements.add(EnumMovement.Crouch);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151463_i())) {
         this.movements.add(EnumMovement.Jump);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74351_w.func_151463_i())) {
         this.movements.add(EnumMovement.Forward);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74368_y.func_151463_i())) {
         this.movements.add(EnumMovement.Back);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74370_x.func_151463_i())) {
         this.movements.add(EnumMovement.Left);
      }

      if (Keyboard.isKeyDown(Minecraft.func_71410_x().field_71474_y.field_74366_z.func_151463_i())) {
         this.movements.add(EnumMovement.Right);
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.movements.size() > 0) {
         Pixelmon.network.sendToServer(new Movement((EnumMovement[])this.movements.toArray(new EnumMovement[this.movements.size()]), EnumMovementType.Custom));
         this.movements.clear();
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
