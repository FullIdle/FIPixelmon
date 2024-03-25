package com.pixelmonmod.pixelmon.util.network;

import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Method;
import java.util.EnumMap;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BetterNetworkWrapper extends SimpleNetworkWrapper {
   EnumMap channels;
   BetterIndexedCodec codec;
   private static Class defaultChannelPipeline;
   private static Method generateName;

   public BetterNetworkWrapper(String channelName) {
      super(channelName);
      SimpleIndexedCodec packetCodec = (SimpleIndexedCodec)ReflectionHelper.getPrivateValue(SimpleNetworkWrapper.class, this, "packetCodec");
      this.channels = (EnumMap)ReflectionHelper.getPrivateValue(SimpleNetworkWrapper.class, this, "channels");
      this.codec = new BetterIndexedCodec();
      Side[] var3 = Side.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Side side = var3[var5];
         ((FMLEmbeddedChannel)this.channels.get(side)).pipeline().replace(packetCodec, (String)null, this.codec);
      }

      ReflectionHelper.setPrivateValue(SimpleNetworkWrapper.class, this, (Object)null, "packetCodec");
      defaultChannelPipeline = (Class)ReflectionHelper.getPrivateValue(SimpleNetworkWrapper.class, (Object)null, "defaultChannelPipeline");
      generateName = (Method)ReflectionHelper.getPrivateValue(SimpleNetworkWrapper.class, (Object)null, "generateName");
   }

   public void registerMessage(IMessageHandler messageHandler, Class requestMessageType, int discriminator, Side side) {
      this.codec.addDiscriminator(discriminator, requestMessageType);
      FMLEmbeddedChannel channel = (FMLEmbeddedChannel)this.channels.get(side);
      String type = channel.findChannelHandlerNameForType(BetterIndexedCodec.class);
      BetterChannelHandlerWrapper handler = new BetterChannelHandlerWrapper(messageHandler, side, requestMessageType);
      channel.pipeline().addAfter(type, this.generateName(channel.pipeline(), handler), handler);
   }

   private String generateName(ChannelPipeline pipeline, ChannelHandler handler) {
      try {
         return (String)generateName.invoke(defaultChannelPipeline.cast(pipeline), handler);
      } catch (Exception var4) {
         throw new RuntimeException("It appears we somehow have a not-standard pipeline. Huh", var4);
      }
   }
}
