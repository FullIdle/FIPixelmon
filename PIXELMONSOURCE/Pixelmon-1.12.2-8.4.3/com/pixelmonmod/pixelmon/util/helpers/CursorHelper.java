package com.pixelmonmod.pixelmon.util.helpers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.IGuiHideMouse;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

@EventBusSubscriber(
   value = {Side.CLIENT},
   modid = "pixelmon"
)
public class CursorHelper {
   public static final Cursor DEFAULT_CURSOR = Mouse.getNativeCursor();
   public static final Cursor TRANSPARENT_CURSOR = createTransparentCursor();

   private static Cursor createTransparentCursor() {
      try {
         BufferedImage image = new BufferedImage(16, 16, 2);
         return new Cursor(16, 16, 0, 0, 1, IntBuffer.wrap(image.getRGB(0, 0, 16, 16, (int[])null, 0, 16)), (IntBuffer)null);
      } catch (Exception var1) {
         Pixelmon.LOGGER.error("Couldn't load the transparent cursor.", var1);
         return DEFAULT_CURSOR;
      }
   }

   public static void setCursor(Cursor cursor) {
      try {
         if (Mouse.getNativeCursor() != cursor) {
            Mouse.setNativeCursor(cursor);
         }
      } catch (LWJGLException var2) {
         if (cursor == DEFAULT_CURSOR) {
            Pixelmon.LOGGER.error("Panic! Couldn't set the default cursor!", var2);
         } else {
            Pixelmon.LOGGER.error("Couldn't set the cursor to a default one.", var2);
            if (Mouse.getNativeCursor() != DEFAULT_CURSOR) {
               setCursor(DEFAULT_CURSOR);
            }
         }
      }

   }

   @SubscribeEvent
   public static void onInitGui(GuiScreenEvent.InitGuiEvent event) {
      if (event.getGui() instanceof IGuiHideMouse) {
         setCursor(TRANSPARENT_CURSOR);
      }

   }

   @SubscribeEvent
   public static void onGuiOpen(GuiOpenEvent event) {
      if (Minecraft.func_71410_x().field_71462_r instanceof IGuiHideMouse) {
         setCursor(DEFAULT_CURSOR);
      }

   }
}
