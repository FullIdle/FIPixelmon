package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

public class GuiKeybindIcon {
   private KeyBinding keybind;
   private ResourceLocation icon;
   private boolean enabled = true;

   public GuiKeybindIcon(KeyBinding keybind, ResourceLocation icon) {
      this.keybind = keybind;
      this.icon = icon;
   }

   public void draw(int x, int y, float zLevel) {
      if (this.enabled && this.keybind.func_151463_i() != 0) {
         boolean flag = Minecraft.func_71410_x().field_71466_p.func_82883_a();
         Minecraft.func_71410_x().field_71466_p.func_78264_a(true);
         Minecraft.func_71410_x().field_71466_p.func_78276_b(GameSettings.func_74298_c(this.keybind.func_151463_i()), x + 20, y + 17, 16777215);
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.icon);
         GuiHelper.drawImageQuad((double)x, (double)y, 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         Minecraft.func_71410_x().field_71466_p.func_78264_a(flag);
      }

   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
}
