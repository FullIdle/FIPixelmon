package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ModifyDenPacket implements IMessage {
   public int entityID;
   public int[] rgb = null;
   public Integer cubeRange = null;
   public Boolean alwaysCube = null;
   public Boolean alwaysAnimate = null;
   public Boolean noBasePlate = null;
   public Boolean invisible = null;
   public Float size = null;

   public static Builder builder(EntityPokestop pokestop) {
      return new Builder(pokestop);
   }

   public void fromBytes(ByteBuf buf) {
      this.entityID = buf.readInt();
      if (buf.readBoolean()) {
         this.rgb = new int[]{buf.readInt(), buf.readInt(), buf.readInt()};
      }

      if (buf.readBoolean()) {
         this.cubeRange = buf.readInt();
      }

      if (buf.readBoolean()) {
         this.alwaysCube = buf.readBoolean();
      }

      if (buf.readBoolean()) {
         this.alwaysAnimate = buf.readBoolean();
      }

      if (buf.readBoolean()) {
         this.noBasePlate = buf.readBoolean();
      }

      if (buf.readBoolean()) {
         this.invisible = buf.readBoolean();
      }

      if (buf.readBoolean()) {
         this.size = buf.readFloat();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeBoolean(this.rgb != null);
      if (this.rgb != null) {
         buf.writeInt(this.rgb[0]);
         buf.writeInt(this.rgb[1]);
         buf.writeInt(this.rgb[2]);
      }

      buf.writeBoolean(this.cubeRange != null);
      if (this.cubeRange != null) {
         buf.writeInt(this.cubeRange);
      }

      buf.writeBoolean(this.alwaysCube != null);
      if (this.alwaysCube != null) {
         buf.writeBoolean(this.alwaysCube);
      }

      buf.writeBoolean(this.alwaysAnimate != null);
      if (this.alwaysAnimate != null) {
         buf.writeBoolean(this.alwaysAnimate);
      }

      buf.writeBoolean(this.noBasePlate != null);
      if (this.noBasePlate != null) {
         buf.writeBoolean(this.noBasePlate);
      }

      buf.writeBoolean(this.invisible != null);
      if (this.invisible != null) {
         buf.writeBoolean(this.invisible);
      }

      buf.writeBoolean(this.size != null);
      if (this.size != null) {
         buf.writeFloat(this.size);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ModifyDenPacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(message.entityID);
            if (entity != null && entity instanceof EntityPokestop) {
               EntityPokestop pokestop = (EntityPokestop)entity;
               if (message.rgb != null) {
                  pokestop.setColor(message.rgb[0], message.rgb[1], message.rgb[2]);
               }

               if (message.cubeRange != null) {
                  pokestop.setCubeRange(message.cubeRange);
               }

               if (message.alwaysCube != null) {
                  pokestop.setAlwaysCube(message.alwaysCube);
               }

               if (message.alwaysAnimate != null) {
                  pokestop.setAlwaysAnimate(message.alwaysAnimate);
               }

               if (message.noBasePlate != null) {
                  pokestop.setNoBasePlate(message.noBasePlate);
               }

               if (message.invisible != null) {
                  pokestop.func_82142_c(message.invisible);
               }

               if (message.size != null) {
                  pokestop.setSize(message.size);
               }
            }

         });
         return null;
      }
   }

   public static class Builder {
      private final ModifyDenPacket packet = new ModifyDenPacket();

      public Builder(EntityPokestop pokestop) {
         this.packet.entityID = pokestop.func_145782_y();
      }

      public Builder setRGB(int r, int g, int b) {
         this.packet.rgb = new int[]{r, g, b};
         return this;
      }

      public Builder setCubeRange(int cubeRange) {
         this.packet.cubeRange = cubeRange;
         return this;
      }

      public Builder setAlwaysAnimate(boolean alwaysAnimate) {
         this.packet.alwaysAnimate = alwaysAnimate;
         return this;
      }

      public Builder setAlwaysCube(boolean alwaysCube) {
         this.packet.alwaysCube = alwaysCube;
         return this;
      }

      public Builder setNoBasePlate(boolean noBasePlate) {
         this.packet.noBasePlate = noBasePlate;
         return this;
      }

      public Builder setInvisible(boolean invisible) {
         this.packet.invisible = invisible;
         return this;
      }

      public Builder setSize(float size) {
         this.packet.size = size;
         return this;
      }

      public ModifyDenPacket build() {
         return this.packet;
      }
   }
}
