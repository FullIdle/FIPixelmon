package com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.WorldHelper;
import com.pixelmonmod.pixelmon.api.events.StatueEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumStatueTextureType;
import com.pixelmonmod.pixelmon.items.ItemStatueMaker;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StatuePacketServer implements IMessage {
   EnumStatuePacketMode mode;
   UUID id;
   String name;
   boolean data;
   int intData;

   public StatuePacketServer() {
   }

   public StatuePacketServer(EnumStatuePacketMode mode, UUID id, String name) {
      this.mode = mode;
      this.id = id;
      this.name = name;
   }

   public StatuePacketServer(EnumStatuePacketMode mode, UUID id, boolean data) {
      this.mode = mode;
      this.id = id;
      this.data = data;
   }

   public StatuePacketServer(EnumStatuePacketMode mode, UUID id, int intData) {
      this.mode = mode;
      this.id = id;
      this.intData = intData;
   }

   public void fromBytes(ByteBuf buf) {
      this.mode = EnumStatuePacketMode.getFromOrdinal(buf.readShort());
      this.id = new UUID(buf.readLong(), buf.readLong());
      switch (this.mode) {
         case SetGrowth:
         case SetLabel:
         case SetTextureType:
         case SetAnimation:
         case SetName:
            this.name = ByteBufUtils.readUTF8String(buf);
            break;
         case SetShouldAnimate:
         case SetModelStanding:
            this.data = buf.readBoolean();
            break;
         case SetAnimationFrame:
         case SetForm:
         case SetGender:
            this.intData = buf.readInt();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort((short)this.mode.ordinal());
      buf.writeLong(this.id.getMostSignificantBits());
      buf.writeLong(this.id.getLeastSignificantBits());
      switch (this.mode) {
         case SetGrowth:
         case SetLabel:
         case SetTextureType:
         case SetAnimation:
         case SetName:
            ByteBufUtils.writeUTF8String(buf, this.name);
            break;
         case SetShouldAnimate:
         case SetModelStanding:
            buf.writeBoolean(this.data);
            break;
         case SetAnimationFrame:
         case SetForm:
         case SetGender:
            buf.writeInt(this.intData);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(StatuePacketServer message, MessageContext ctx) {
         EntityPlayerMP sender = ctx.getServerHandler().field_147369_b;
         if (ItemStatueMaker.checkPermission(sender)) {
            EntityStatue statue = (EntityStatue)WorldHelper.getEntityByUUID(ctx.getServerHandler().field_147369_b.field_70170_p, message.id, EntityStatue.class);
            if (statue != null) {
               Object value = message.mode.chooseValueForMode(message.name, message.data, message.intData);
               StatueEvent.ModifyStatue modifyEvent = new StatueEvent.ModifyStatue(ctx.getServerHandler().field_147369_b, statue, message.mode, value);
               if (Pixelmon.EVENT_BUS.post(modifyEvent)) {
                  ctx.getServerHandler().field_147369_b.func_71053_j();
               } else {
                  value = modifyEvent.getValue();
                  switch (message.mode) {
                     case SetGrowth:
                        statue.setGrowth((EnumGrowth)value);
                        break;
                     case SetLabel:
                        statue.setLabel((String)value);
                        break;
                     case SetTextureType:
                        statue.setTextureType(EnumStatueTextureType.getFromString((String)value));
                        break;
                     case SetAnimation:
                        statue.setAnimation(AnimationType.getTypeFor((String)value));
                        break;
                     case SetName:
                        statue.setPokemon(new PokemonBase((EnumSpecies)value));
                        break;
                     case SetShouldAnimate:
                        statue.setAnimate((Boolean)value);
                        break;
                     case SetModelStanding:
                        statue.setIsFlying((Boolean)value);
                        break;
                     case SetAnimationFrame:
                        statue.setAnimationFrame((Integer)value);
                        break;
                     case SetForm:
                        statue.setForm((Integer)value);
                        break;
                     case SetGender:
                        statue.setGender(Gender.values()[(Integer)value]);
                  }

               }
            }
         }
      }
   }
}
