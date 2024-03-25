package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import com.pixelmonmod.pixelmon.client.particle.Particles;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SummonParticleArcanery implements IMessage {
   private double posX;
   private double posY;
   private double posZ;
   private double motionX;
   private double motionY;
   private double motionZ;
   private float size;
   private Particles particle;
   private Object[] args;
   private ParticleEffect effect;

   public SummonParticleArcanery() {
   }

   public SummonParticleArcanery(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float size, Particles particle, Object... args) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.motionX = motionX;
      this.motionY = motionY;
      this.motionZ = motionZ;
      this.size = size;
      this.particle = particle;
      this.args = args;
   }

   public void fromBytes(ByteBuf buf) {
      this.posX = buf.readDouble();
      this.posY = buf.readDouble();
      this.posZ = buf.readDouble();
      this.motionX = buf.readDouble();
      this.motionY = buf.readDouble();
      this.motionZ = buf.readDouble();
      this.size = buf.readFloat();
      this.effect = Particles.read(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeDouble(this.posX);
      buf.writeDouble(this.posY);
      buf.writeDouble(this.posZ);
      buf.writeDouble(this.motionX);
      buf.writeDouble(this.motionY);
      buf.writeDouble(this.motionZ);
      buf.writeFloat(this.size);
      Particles.write(buf, this.particle, this.args);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SummonParticleArcanery message, MessageContext ctx) {
         handlePacket(message);
         return null;
      }

      @SideOnly(Side.CLIENT)
      private static void handlePacket(SummonParticleArcanery message) {
         Minecraft mc = Minecraft.func_71410_x();
         if (message.effect != null) {
            mc.func_152344_a(() -> {
               mc.field_71452_i.func_78873_a(new ParticleArcanery(mc.field_71441_e, message.posX, message.posY, message.posZ, message.motionX, message.motionY, message.motionZ, message.effect));
            });
         }

      }
   }
}
