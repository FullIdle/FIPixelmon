package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.EnumMovementType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.Movement;
import com.pixelmonmod.pixelmon.enums.EnumMovement;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OldMovementHandler {
   @SubscribeEvent
   public static void tickStart(TickEvent.ClientTickEvent event) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player != null && player.func_184187_bx() != null) {
         handleRidingMovement();
      }

   }

   private static void handleRidingMovement() {
      ArrayList array = new ArrayList();
      if (Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151470_d()) {
         array.add(EnumMovement.Jump);
      }

      if (Descend.Instance.func_151470_d()) {
         array.add(EnumMovement.Descend);
      }

      if (!array.isEmpty()) {
         Pixelmon.network.sendToServer(new Movement((EnumMovement[])array.toArray(new EnumMovement[0]), EnumMovementType.Riding));
      }

   }
}
