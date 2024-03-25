package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ServerCosmetics;
import com.pixelmonmod.pixelmon.storage.extras.ExtrasContact;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasStorage;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PixelExtrasDisplayPacket implements IMessage {
   private UUID uuid;
   public int dataHash;
   private final EnumSet enabled = EnumSet.noneOf(PixelExtrasData.Category.class);
   private Set serverCosmetics = EnumSet.noneOf(ServerCosmetics.class);
   private PixelExtrasData.HatType hatType;
   private PixelExtrasData.MonocleType monocleType;
   private PixelExtrasData.SashType sashType;
   private PixelExtrasData.RobeType robeType;
   private PixelExtrasData.SphealType sphealType;
   private final int[][] colours;

   public PixelExtrasDisplayPacket() {
      this.hatType = PixelExtrasData.HatType.NONE;
      this.monocleType = PixelExtrasData.MonocleType.NONE;
      this.sashType = PixelExtrasData.SashType.NONE;
      this.robeType = PixelExtrasData.RobeType.NONE;
      this.sphealType = PixelExtrasData.SphealType.DEFAULT;
      this.colours = new int[3][3];
   }

   public PixelExtrasDisplayPacket(PixelExtrasData data) {
      this.hatType = PixelExtrasData.HatType.NONE;
      this.monocleType = PixelExtrasData.MonocleType.NONE;
      this.sashType = PixelExtrasData.SashType.NONE;
      this.robeType = PixelExtrasData.RobeType.NONE;
      this.sphealType = PixelExtrasData.SphealType.DEFAULT;
      this.colours = new int[3][3];
      this.uuid = data.id;
      this.dataHash = data.dataHash();
      PixelExtrasData.Category[] var2 = PixelExtrasData.Category.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelExtrasData.Category category = var2[var4];
         if (data.isEnabled(category)) {
            this.enabled.add(category);
         }
      }

      this.serverCosmetics = data.getServerCosmetics();
      this.hatType = data.getHatType();
      this.monocleType = data.getMonocleType();
      this.sashType = data.getSashType();
      this.robeType = data.getRobeType();
      this.sphealType = data.getSphealType();
      this.colours[0] = data.getColours(PixelExtrasData.Category.SASH);
      this.colours[1] = data.getColours(PixelExtrasData.Category.HAT);
      this.colours[2] = data.getColours(PixelExtrasData.Category.MONOCLE);
   }

   public void fromBytes(ByteBuf buf) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
      this.dataHash = buf.readInt();
      PixelExtrasData.Category[] var2 = PixelExtrasData.Category.values();
      int var3 = var2.length;

      int i;
      for(i = 0; i < var3; ++i) {
         PixelExtrasData.Category category = var2[i];
         if (buf.readBoolean()) {
            this.enabled.add(category);
         }
      }

      byte[] bytes = new byte[buf.readByte()];
      buf.readBytes(bytes);
      BitSet set = BitSet.valueOf(bytes);
      ServerCosmetics[] var10 = ServerCosmetics.values();
      int b = var10.length;

      for(int var6 = 0; var6 < b; ++var6) {
         ServerCosmetics cosmetics = var10[var6];
         if (set.get(cosmetics.ordinal())) {
            this.serverCosmetics.add(cosmetics);
         }
      }

      this.hatType = PixelExtrasData.HatType.getFromId(buf.readByte());
      this.monocleType = PixelExtrasData.MonocleType.getFromId(buf.readByte());
      this.sashType = PixelExtrasData.SashType.values()[buf.readByte()];
      this.robeType = PixelExtrasData.RobeType.values()[buf.readByte()];
      this.sphealType = PixelExtrasData.SphealType.values()[buf.readByte()];

      for(i = 0; i < this.colours.length; ++i) {
         for(b = 0; b < 3; ++b) {
            this.colours[i][b] = buf.readInt();
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.uuid.getMostSignificantBits()).writeLong(this.uuid.getLeastSignificantBits());
      buf.writeInt(this.dataHash);
      PixelExtrasData.Category[] var2 = PixelExtrasData.Category.values();
      int var3 = var2.length;

      int i;
      for(i = 0; i < var3; ++i) {
         PixelExtrasData.Category category = var2[i];
         buf.writeBoolean(this.enabled.contains(category));
      }

      BitSet set = new BitSet();
      Iterator var7 = this.serverCosmetics.iterator();

      while(var7.hasNext()) {
         ServerCosmetics cosmetics = (ServerCosmetics)var7.next();
         set.set(cosmetics.ordinal());
      }

      byte[] bytes = set.toByteArray();
      buf.writeByte(bytes.length).writeBytes(bytes);
      buf.writeByte(this.hatType.id);
      buf.writeByte(this.monocleType.id);
      buf.writeByte(this.sashType.ordinal());
      buf.writeByte(this.robeType.ordinal());
      buf.writeByte(this.sphealType.ordinal());

      for(i = 0; i < this.colours.length; ++i) {
         for(int b = 0; b < 3; ++b) {
            buf.writeInt(this.colours[i][b]);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PixelExtrasDisplayPacket message, MessageContext ctx) {
         Consumer consumer = (datax) -> {
            PixelExtrasData.Category[] var2 = PixelExtrasData.Category.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               PixelExtrasData.Category category = var2[var4];
               datax.setEnabled(category, message.enabled.contains(category));
            }

            datax.setHatType(message.hatType);
            datax.setMonocleType(message.monocleType);
            datax.setSashType(message.sashType);
            datax.setRobeType(message.robeType);
            datax.setSphealType(message.sphealType);
            datax.setColours(PixelExtrasData.Category.SASH, message.colours[0]);
            datax.setColours(PixelExtrasData.Category.HAT, message.colours[1]);
            datax.setColours(PixelExtrasData.Category.MONOCLE, message.colours[2]);
         };
         if (ctx.side == Side.CLIENT) {
            PixelExtrasData data = new PixelExtrasData(message.uuid);
            data.updateServerCosmetics(message.serverCosmetics);
            ExtrasContact.updateData(data, message.dataHash, consumer.andThen(PlayerExtraDataStore::add));
         } else {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            PixelExtrasData data = PixelExtrasStorage.getData(player.func_110124_au());
            data.updateServerCosmetics(Pixelmon.storageManager.getParty(player).getServerCosmetics());
            if (data.isReady() && ExtrasContact.checkCacheHash(message.uuid, message.dataHash)) {
               consumer.accept(data);
               if (data.hasData()) {
                  return new PixelExtrasDisplayPacket(data);
               }
            } else {
               ExtrasContact.updateData(data, message.dataHash, consumer.andThen(PixelExtrasStorage::addAndDistribute));
            }
         }

         return null;
      }
   }
}
