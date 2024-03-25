package com.pixelmonmod.tcg.helper;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class GuiHelper {
   public static void showTitle(ITextComponent content, EntityPlayerMP player) {
      player.field_71135_a.func_147359_a(new SPacketTitle(Type.RESET, (ITextComponent)null));
      player.field_71135_a.func_147359_a(new SPacketTitle(Type.TITLE, content));
   }

   public static void showTitle(ITextComponent content, EntityPlayerMP player, int fadeInTime, int displayTime, int fadeOutTime) {
      player.field_71135_a.func_147359_a(new SPacketTitle(Type.RESET, (ITextComponent)null));
      player.field_71135_a.func_147359_a(new SPacketTitle(Type.TIMES, (ITextComponent)null, fadeInTime, displayTime, fadeOutTime));
      player.field_71135_a.func_147359_a(new SPacketTitle(Type.TITLE, content));
   }

   public static int getRotationFromFacing(EnumFacing facing) {
      switch (facing) {
         case EAST:
            return 270;
         case NORTH:
            return 0;
         case WEST:
            return 90;
         case SOUTH:
            return 180;
         default:
            return 0;
      }
   }
}
