package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex.ServerPokedex;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class PokedexKey extends KeyBinding {
   public static final String pixelmonWiki = "http://pixelmonmod.com/wiki/index.php?title=";

   public PokedexKey() {
      super("key.pokedex", 23, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71441_e == null) {
            return;
         }

         try {
            RayTraceResult objectMouseOver = TargetKeyBinding.getTarget(false);
            if (objectMouseOver.field_72313_a == Type.ENTITY && objectMouseOver.field_72308_g instanceof EntityPixelmon) {
               EntityPixelmon pokemon = (EntityPixelmon)objectMouseOver.field_72308_g;
               Pixelmon.network.sendToServer(new ServerPokedex(true, pokemon.func_145782_y()));
            } else {
               Pixelmon.network.sendToServer(new ServerPokedex(true));
            }
         } catch (UnsupportedOperationException var5) {
         }
      }

   }
}
