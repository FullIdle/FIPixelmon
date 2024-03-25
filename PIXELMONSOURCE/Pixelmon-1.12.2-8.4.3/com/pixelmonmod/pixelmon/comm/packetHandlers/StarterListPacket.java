package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.starter.GuiChooseStarter;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.StarterList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarterListPacket extends PokemonListPacket {
   public boolean isShiny;

   public StarterListPacket() {
      super(StarterList.getStarterList());
      this.isShiny = PixelmonConfig.shinyStarter;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(StarterListPacket message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(StarterListPacket message) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71439_g != null) {
            ServerStorageDisplay.starterListPacket = message;
            if (mc.field_71462_r == null) {
               mc.func_147108_a(new GuiChooseStarter());
            }
         }

      }
   }
}
