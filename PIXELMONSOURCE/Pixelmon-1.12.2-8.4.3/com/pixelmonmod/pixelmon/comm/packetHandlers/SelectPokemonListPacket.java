package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.selectPokemon.GuiSelectPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SelectPokemonListPacket extends PokemonListPacket {
   public SelectPokemonListPacket() {
   }

   public SelectPokemonListPacket(EnumSpecies... pokemon) {
      super(pokemon);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SelectPokemonListPacket message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(SelectPokemonListPacket message) {
         ServerStorageDisplay.selectPokemonListPacket = message;
         Minecraft.func_71410_x().func_147108_a(new GuiSelectPokemon());
      }
   }
}
