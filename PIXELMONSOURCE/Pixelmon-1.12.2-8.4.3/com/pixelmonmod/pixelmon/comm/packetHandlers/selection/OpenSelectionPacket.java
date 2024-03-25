package com.pixelmonmod.pixelmon.comm.packetHandlers.selection;

import com.pixelmonmod.pixelmon.client.gui.custom.selection.GuiSelection;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenSelectionPacket implements IMessage {
   private ITextComponent title;
   private ITextComponent text;
   private int selections;
   private boolean allowExit;

   public OpenSelectionPacket() {
   }

   public OpenSelectionPacket(ITextComponent title, ITextComponent text, int selections, boolean allowExit) {
      this.title = title;
      this.text = text;
      this.selections = selections;
      this.allowExit = allowExit;
   }

   public void fromBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);

      try {
         this.title = pb.func_179258_d();
         this.text = pb.func_179258_d();
         this.selections = pb.readInt();
         this.allowExit = pb.readBoolean();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public void toBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);
      pb.func_179256_a(this.title);
      pb.func_179256_a(this.text);
      pb.writeInt(this.selections);
      pb.writeBoolean(this.allowExit);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenSelectionPacket message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(OpenSelectionPacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_147108_a(new GuiSelection(message.title, message.text, message.selections, message.allowExit));
      }
   }
}
