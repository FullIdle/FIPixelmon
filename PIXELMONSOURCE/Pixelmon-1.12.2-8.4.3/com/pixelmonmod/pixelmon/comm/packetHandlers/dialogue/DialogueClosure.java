package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.dialogue.DialogueEndedEvent;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DialogueClosure implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DialogueClosure message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            Pixelmon.EVENT_BUS.post(new DialogueEndedEvent(ctx.getServerHandler().field_147369_b));
         });
         return null;
      }
   }
}
