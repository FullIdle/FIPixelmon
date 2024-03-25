package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.api.enums.ServerCosmetics;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerCosmeticsUpdatePacket implements IMessage {
   private Set serverCosmetics = EnumSet.noneOf(ServerCosmetics.class);

   /** @deprecated */
   @Deprecated
   public ServerCosmeticsUpdatePacket() {
   }

   public ServerCosmeticsUpdatePacket(Set serverCosmetics) {
      this.serverCosmetics = serverCosmetics;
   }

   public void fromBytes(ByteBuf buf) {
      byte[] bytes = new byte[buf.readByte()];
      buf.readBytes(bytes);
      BitSet set = BitSet.valueOf(bytes);
      ServerCosmetics[] var4 = ServerCosmetics.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ServerCosmetics cosmetics = var4[var6];
         if (set.get(cosmetics.ordinal())) {
            this.serverCosmetics.add(cosmetics);
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      BitSet set = new BitSet();
      Iterator var3 = this.serverCosmetics.iterator();

      while(var3.hasNext()) {
         ServerCosmetics cosmetics = (ServerCosmetics)var3.next();
         set.set(cosmetics.ordinal());
      }

      byte[] bytes = set.toByteArray();
      buf.writeByte(bytes.length).writeBytes(bytes);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ServerCosmeticsUpdatePacket message, MessageContext ctx) {
         this.onClient(message);
         return null;
      }

      @SideOnly(Side.CLIENT)
      public void onClient(ServerCosmeticsUpdatePacket message) {
         UUID uuid = Minecraft.func_71410_x().func_110432_I().func_148256_e().getId();
         PixelExtrasData data = new PixelExtrasData(uuid);
         data.updateServerCosmetics(message.serverCosmetics);
         PlayerExtraDataStore.add(data);
      }
   }
}
