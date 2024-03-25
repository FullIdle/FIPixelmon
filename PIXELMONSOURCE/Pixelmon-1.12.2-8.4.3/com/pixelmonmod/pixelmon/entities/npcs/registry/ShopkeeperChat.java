package com.pixelmonmod.pixelmon.entities.npcs.registry;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ShopkeeperChat {
   public String hello;
   public String goodbye;

   public ShopkeeperChat(String hello, String goodbye) {
      this.hello = hello;
      this.goodbye = goodbye;
   }

   public void writeToBuffer(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.hello);
      ByteBufUtils.writeUTF8String(buffer, this.goodbye);
   }

   public static ShopkeeperChat loadFromBuffer(ByteBuf buffer) {
      ShopkeeperChat chat = new ShopkeeperChat(ByteBufUtils.readUTF8String(buffer), ByteBufUtils.readUTF8String(buffer));
      return chat;
   }
}
