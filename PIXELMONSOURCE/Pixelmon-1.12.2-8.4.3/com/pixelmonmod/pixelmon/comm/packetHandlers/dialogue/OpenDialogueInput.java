package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.client.gui.custom.dialogue.GuiDialogueInput;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.io.UncheckedIOException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenDialogueInput implements IMessage {
   protected ITextComponent title;
   protected ITextComponent text;
   protected String defaultText;

   public OpenDialogueInput() {
   }

   public OpenDialogueInput(ITextComponent title, ITextComponent text, String defaultText) {
      this.title = title;
      this.text = text;
      this.defaultText = defaultText;
   }

   public void fromBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);

      try {
         this.title = pb.func_179258_d();
         this.text = pb.func_179258_d();
         this.defaultText = pb.func_150789_c(50);
      } catch (IOException var4) {
         throw new UncheckedIOException(var4);
      }
   }

   public void toBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);
      pb.func_179256_a(this.title);
      pb.func_179256_a(this.text);
      pb.func_180714_a(this.defaultText);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenDialogueInput message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(OpenDialogueInput message, MessageContext ctx) {
         Minecraft.func_71410_x().func_147108_a(new GuiDialogueInput(message.title, message.text, message.defaultText));
      }
   }
}
