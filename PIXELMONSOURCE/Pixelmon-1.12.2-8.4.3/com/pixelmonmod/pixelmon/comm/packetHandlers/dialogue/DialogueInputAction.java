package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.dialogue.DialogueInputEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DialogueInputAction implements IMessage {
   protected String text;

   public DialogueInputAction() {
   }

   public DialogueInputAction(String text) {
      this.text = text;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.text = (new PacketBuffer(buf)).func_150789_c(50);
      } else {
         this.text = null;
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.text != null);
      if (this.text != null) {
         (new PacketBuffer(buf)).func_180714_a(this.text);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(DialogueInputAction message, MessageContext ctx) {
         if (message.text == null) {
            Pixelmon.EVENT_BUS.post(new DialogueInputEvent.ClosedScreen(ctx.getServerHandler().field_147369_b));
         } else {
            Pixelmon.EVENT_BUS.post(new DialogueInputEvent.Submitted(ctx.getServerHandler().field_147369_b, message.text));
         }

      }
   }
}
