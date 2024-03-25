package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.HealerEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HealerGuiClose implements IMessage {
   private String npc;

   public HealerGuiClose() {
   }

   public HealerGuiClose(String npc) {
      this.npc = npc;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.npc);
   }

   public void fromBytes(ByteBuf buffer) {
      this.npc = ByteBufUtils.readUTF8String(buffer);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(HealerGuiClose message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            Pokerus.onHealerClose(ctx.getServerHandler().field_147369_b, message.npc);
            Pixelmon.EVENT_BUS.post(new HealerEvent.Post(ctx.getServerHandler().field_147369_b, message.npc));
         });
         return null;
      }
   }
}
