package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@EventBusSubscriber(
   value = {Side.CLIENT},
   modid = "pixelmon"
)
public class DebugListener {
   @SubscribeEvent
   public static void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
      if (Pixelmon.devEnvironment && Keyboard.isKeyDown(61)) {
         GuiScreen screen = event.getGui();
         int centerW = screen.field_146294_l / 2;
         int centerH = screen.field_146295_m / 2;
         int mouseX = event.getMouseX();
         int mouseY = event.getMouseY();
         GuiHelper.drawCenteredString("x: " + mouseX + ", y: " + mouseY, (float)mouseX, (float)(mouseY - 29), 1048575);
         GuiHelper.drawCenteredString("xcenter: " + (mouseX - centerW), (float)mouseX, (float)(mouseY - 19), 1048575);
         GuiHelper.drawCenteredString("ycenter: " + (mouseY - centerH), (float)mouseX, (float)(mouseY - 9), 1048575);
      }

   }
}
