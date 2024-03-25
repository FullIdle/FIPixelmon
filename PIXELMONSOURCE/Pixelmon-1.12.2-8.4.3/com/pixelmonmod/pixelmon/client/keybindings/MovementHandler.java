package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.MovementPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import com.pixelmonmod.pixelmon.enums.EnumKeybinds;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MovementHandler {
   public static ArrayList movements = new ArrayList();

   private static void handleRidingMovement() {
      movements.clear();
      if (Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151470_d()) {
         movements.add(EnumKeybinds.Jump);
      }

      if (Minecraft.func_71410_x().field_71474_y.field_74351_w.func_151470_d()) {
         movements.add(EnumKeybinds.Forward);
      }

      if (Minecraft.func_71410_x().field_71474_y.field_74368_y.func_151470_d()) {
         movements.add(EnumKeybinds.Back);
      }

      if (Minecraft.func_71410_x().field_71474_y.field_74370_x.func_151470_d()) {
         movements.add(EnumKeybinds.Left);
      }

      if (Minecraft.func_71410_x().field_71474_y.field_74366_z.func_151470_d()) {
         movements.add(EnumKeybinds.Right);
      }

      if (Minecraft.func_71410_x().field_71474_y.field_151444_V.func_151470_d()) {
         movements.add(EnumKeybinds.Sprint);
      }

      if (!movements.isEmpty()) {
         Pixelmon.network.sendToServer(new MovementPacket(movements));
      }

      if (Minecraft.func_71410_x().field_71439_g.func_184187_bx() instanceof Entity6Moves) {
         ((Entity6Moves)Minecraft.func_71410_x().field_71439_g.func_184187_bx()).handleMovement(movements);
      }

   }

   @EventBusSubscriber(
      modid = "pixelmon",
      value = {Side.CLIENT}
   )
   private static class EventHandler {
      @SubscribeEvent
      public static void clientTick(TickEvent.ClientTickEvent event) {
         if (Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71439_g.func_184187_bx() != null) {
            MovementHandler.handleRidingMovement();
         }

      }
   }
}
