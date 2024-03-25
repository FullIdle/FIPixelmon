package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayParticleSystem implements IMessage {
   private ParticleSystems system;
   private double x;
   private double y;
   private double z;
   private int dimension;
   private float scale;
   private boolean shiny;
   private double[] args;

   public PlayParticleSystem() {
   }

   public PlayParticleSystem(ParticleSystems system, double x, double y, double z, int dimension, float scale, boolean shiny, double... args) {
      this.system = system;
      this.x = x;
      this.y = y;
      this.z = z;
      this.dimension = dimension;
      this.scale = scale;
      this.shiny = shiny;
      this.args = args;
   }

   public void fromBytes(ByteBuf buf) {
      this.system = ParticleSystems.values()[buf.readInt()];
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.dimension = buf.readInt();
      this.scale = buf.readFloat();
      this.shiny = buf.readBoolean();
      this.args = new double[buf.readInt()];

      for(int i = 0; i < this.args.length; ++i) {
         this.args[i] = buf.readDouble();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.system.ordinal());
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
      buf.writeInt(this.dimension);
      buf.writeFloat(this.scale);
      buf.writeBoolean(this.shiny);
      buf.writeInt(this.args.length);
      double[] var2 = this.args;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double arg = var2[var4];
         buf.writeDouble(arg);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PlayParticleSystem message, MessageContext ctx) {
         handlePacket(message);
         return null;
      }

      @SideOnly(Side.CLIENT)
      private static void handlePacket(PlayParticleSystem message) {
         if (Minecraft.func_71410_x().field_71441_e.field_73011_w.getDimension() == message.dimension) {
            message.system.getSystem().execute(Minecraft.func_71410_x(), Minecraft.func_71410_x().field_71441_e, message.x, message.y, message.z, message.scale, message.shiny, message.args);
         }

      }
   }
}
