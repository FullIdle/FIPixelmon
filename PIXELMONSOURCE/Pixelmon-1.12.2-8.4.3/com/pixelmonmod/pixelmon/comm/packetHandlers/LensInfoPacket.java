package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LensInfoPacket implements IMessage {
   public boolean hide;
   public int entityID;
   public IVStore ivs;
   public String abilityLangKey;
   public EnumNature nature;

   public LensInfoPacket() {
      this.hide = false;
   }

   public LensInfoPacket(EntityPixelmon pixelmon) {
      this(pixelmon, false);
   }

   public LensInfoPacket(EntityPixelmon pixelmon, boolean hide) {
      this.hide = false;
      if (hide) {
         this.hide = hide;
      } else {
         this.entityID = pixelmon.func_145782_y();
         this.ivs = pixelmon.getPokemonData().getIVs();
         this.abilityLangKey = pixelmon.getPokemonData().getAbility().getUnlocalizedName();
         this.nature = pixelmon.getPokemonData().getNature();
      }
   }

   public void fromBytes(ByteBuf buf) {
      this.hide = buf.readBoolean();
      if (!this.hide) {
         this.entityID = buf.readInt();
         this.ivs = (new IVStore()).readFromByteBuffer(buf);
         this.abilityLangKey = ByteBufUtils.readUTF8String(buf);
         this.nature = EnumNature.values()[buf.readByte()];
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.hide);
      if (!this.hide) {
         buf.writeInt(this.entityID);
         this.ivs.writeToByteBuffer(buf);
         ByteBufUtils.writeUTF8String(buf, this.abilityLangKey);
         buf.writeByte(this.nature.ordinal());
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(LensInfoPacket message, MessageContext ctx) {
         Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(message.entityID);
         if (entity != null && entity instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            if (message.hide) {
               pixelmon.setClientOnlyInfo((LensInfoPacket)null);
            } else {
               pixelmon.setClientOnlyInfo(message);
            }
         }

         return null;
      }
   }
}
