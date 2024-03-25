package com.pixelmonmod.pixelmon.client.keybindings;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class OptionsKey extends TargetKeyBinding {
   public OptionsKey() {
      super("key.pixelmonoptions", 25, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Iterator var2 = Loader.instance().getModList().iterator();

         while(var2.hasNext()) {
            ModContainer mod = (ModContainer)var2.next();
            if (mod.getModId().equals("pixelmon")) {
               try {
                  IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(mod);
                  GuiScreen newScreen = guiFactory.createConfigGui((GuiScreen)null);
                  Minecraft.func_71410_x().func_147108_a(newScreen);
               } catch (Exception var6) {
                  var6.printStackTrace();
               }

               return;
            }
         }
      }

   }
}
