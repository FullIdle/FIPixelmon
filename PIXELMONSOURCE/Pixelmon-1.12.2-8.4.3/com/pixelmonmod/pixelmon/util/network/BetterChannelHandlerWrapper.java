package com.pixelmonmod.pixelmon.util.network;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.FMLOutboundHandler.OutboundTarget;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class BetterChannelHandlerWrapper extends SimpleChannelInboundHandler {
   private final IMessageHandler messageHandler;
   private final Side side;
   private static Constructor contextConstructor;

   public BetterChannelHandlerWrapper(Class handler, Side side, Class requestType) {
      this(instantiate(handler), side, requestType);
   }

   public BetterChannelHandlerWrapper(IMessageHandler handler, Side side, Class requestType) {
      super(requestType);
      this.messageHandler = (IMessageHandler)Preconditions.checkNotNull(handler, "IMessageHandler must not be null");
      this.side = side;
   }

   protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) throws Exception {
      ThreadLocal local = (ThreadLocal)ctx.channel().attr(BetterIndexedCodec.FML_SOURCE).get();
      INetHandler iNetHandler = ((FMLProxyPacket)local.get()).handler();
      local.remove();
      MessageContext context = context(iNetHandler, this.side);
      IMessage result = this.messageHandler.onMessage(msg, context);
      if (result != null) {
         ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.REPLY);
         ctx.writeAndFlush(result).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      FMLLog.log.error("BetterChannelHandlerWrapper exception", cause);
      super.exceptionCaught(ctx, cause);
   }

   static IMessageHandler instantiate(Class handler) {
      try {
         return (IMessageHandler)handler.newInstance();
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }

   static MessageContext context(INetHandler netHandler, Side side) {
      try {
         return (MessageContext)contextConstructor.newInstance(netHandler, side);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         var3.printStackTrace();
         throw new RuntimeException("Could not initialize message context!");
      }
   }

   static {
      try {
         contextConstructor = MessageContext.class.getDeclaredConstructor(INetHandler.class, Side.class);
         contextConstructor.setAccessible(true);
      } catch (NoSuchMethodException var1) {
         var1.printStackTrace();
      }

   }
}
