package com.pixelmonmod.pixelmon.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import java.lang.ref.WeakReference;
import java.util.List;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

@Sharable
public class BetterIndexedCodec extends MessageToMessageCodec {
   private final Byte2ObjectMap discriminators = new Byte2ObjectOpenHashMap();
   private final Object2ByteMap types = new Object2ByteOpenHashMap();
   public static final AttributeKey INBOUNDPACKETTRACKER = AttributeKey.valueOf("fml:inboundpacket");
   public static final AttributeKey FML_SOURCE = AttributeKey.valueOf("pixelmon:fml_source");

   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      super.handlerAdded(ctx);
      ctx.channel().attr(INBOUNDPACKETTRACKER).set(new ThreadLocal());
      ctx.channel().attr(FML_SOURCE).set(new ThreadLocal());
   }

   public BetterIndexedCodec addDiscriminator(int discriminator, Class type) {
      this.discriminators.put((byte)discriminator, type);
      this.types.put(type, (byte)discriminator);
      return this;
   }

   protected final void encode(ChannelHandlerContext ctx, IMessage msg, List out) throws Exception {
      String channel = (String)ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get();
      Class clazz = msg.getClass();
      if (!this.types.containsKey(clazz)) {
         throw new RuntimeException("Undefined discriminator for message type " + clazz.getName() + " in channel " + channel);
      } else {
         byte discriminator = this.types.getByte(clazz);
         PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
         buffer.writeByte(discriminator);
         msg.toBytes(buffer);
         FMLProxyPacket proxy = new FMLProxyPacket(buffer, channel);
         WeakReference ref = (WeakReference)((ThreadLocal)ctx.channel().attr(INBOUNDPACKETTRACKER).get()).get();
         FMLProxyPacket old = ref == null ? null : (FMLProxyPacket)ref.get();
         if (old != null) {
            proxy.setDispatcher(old.getDispatcher());
         }

         out.add(proxy);
      }
   }

   protected final void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List out) throws Exception {
      ByteBuf payload = msg.payload().duplicate();
      if (payload.readableBytes() < 1) {
         FMLLog.log.error("The FMLIndexedCodec has received an empty buffer on channel {}, likely a result of a LAN server issue. Pipeline parts : {}", ctx.channel().attr(NetworkRegistry.FML_CHANNEL), ctx.pipeline().toString());
      }

      byte discriminator = payload.readByte();
      Class clazz = (Class)this.discriminators.get(discriminator);
      if (clazz == null) {
         throw new NullPointerException("Undefined message for discriminator " + discriminator + " in channel " + msg.channel());
      } else {
         IMessage newMsg = (IMessage)clazz.newInstance();
         ((ThreadLocal)ctx.channel().attr(INBOUNDPACKETTRACKER).get()).set(new WeakReference(msg));
         ((ThreadLocal)ctx.channel().attr(FML_SOURCE).get()).set(msg);
         newMsg.fromBytes(payload.slice());
         out.add(newMsg);
         payload.release();
      }
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      FMLLog.log.error("FMLIndexedMessageCodec exception caught", cause);
      super.exceptionCaught(ctx, cause);
   }
}
