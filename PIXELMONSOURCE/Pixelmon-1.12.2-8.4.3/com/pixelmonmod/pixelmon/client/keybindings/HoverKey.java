package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.HoverPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(
   modid = "pixelmon",
   value = {Side.CLIENT}
)
public class HoverKey extends KeyBinding {
   static HoverKey INSTANCE;

   public HoverKey() {
      super("key.hover", 35, "key.categories.pixelmon");
      INSTANCE = this;
   }

   public void onKeyDown() {
      if (this.func_151468_f()) {
         EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
         if (player.func_184187_bx() != null) {
            Entity riddenEntity = player.func_184187_bx();
            if (riddenEntity instanceof EntityPixelmon) {
               Pixelmon.network.sendToServer(new HoverPacket((EntityPixelmon)riddenEntity));
            }
         }
      }

   }

   @EventBusSubscriber(
      modid = "pixelmon",
      value = {Side.CLIENT}
   )
   private static class EventHandler {
      @SubscribeEvent
      public static void onKeyDown(InputEvent.KeyInputEvent event) {
         HoverKey.INSTANCE.onKeyDown();
      }
   }
}
