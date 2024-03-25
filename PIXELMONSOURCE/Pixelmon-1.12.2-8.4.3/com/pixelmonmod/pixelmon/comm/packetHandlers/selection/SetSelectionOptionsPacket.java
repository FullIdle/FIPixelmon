package com.pixelmonmod.pixelmon.comm.packetHandlers.selection;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.custom.selection.GuiSelection;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SetSelectionOptionsPacket implements IMessage {
   private int slot;
   private Pokemon pokemon;

   public SetSelectionOptionsPacket() {
   }

   public SetSelectionOptionsPacket(int slot, Pokemon pokemon) {
      this.slot = slot;
      this.pokemon = pokemon;
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
      this.pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buf, EnumUpdateType.ALL);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
      this.pokemon.writeToByteBuffer(buf, EnumUpdateType.ALL);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetSelectionOptionsPacket message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(SetSelectionOptionsPacket message, MessageContext ctx) {
         GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
         if (screen instanceof GuiSelection) {
            ((GuiSelection)screen).setPokemon(message.slot, message.pokemon);
            screen.func_73866_w_();
         }
      }
   }
}
