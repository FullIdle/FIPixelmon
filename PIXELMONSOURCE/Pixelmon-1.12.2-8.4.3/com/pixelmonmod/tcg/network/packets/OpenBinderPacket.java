package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.gui.enums.EnumGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenBinderPacket implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(OpenBinderPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_71121_q().func_152344_a(() -> {
            player.openGui(TCG.instance, EnumGui.Binder.getIndex(), player.func_130014_f_(), 0, 0, 0);
         });
         return null;
      }
   }
}
