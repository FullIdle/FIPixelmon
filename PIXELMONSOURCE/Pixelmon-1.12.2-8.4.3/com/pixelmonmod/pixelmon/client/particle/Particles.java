package com.pixelmonmod.pixelmon.client.particle;

import com.pixelmonmod.pixelmon.client.particle.particles.Blob;
import com.pixelmonmod.pixelmon.client.particle.particles.BlueMagic;
import com.pixelmonmod.pixelmon.client.particle.particles.CycloneBlob;
import com.pixelmonmod.pixelmon.client.particle.particles.Dragon;
import com.pixelmonmod.pixelmon.client.particle.particles.Electric;
import com.pixelmonmod.pixelmon.client.particle.particles.Leaf;
import com.pixelmonmod.pixelmon.client.particle.particles.RedOrbShrinking;
import com.pixelmonmod.pixelmon.client.particle.particles.ShadowBlob;
import com.pixelmonmod.pixelmon.client.particle.particles.Shiny;
import com.pixelmonmod.pixelmon.client.particle.particles.SlingRing;
import com.pixelmonmod.pixelmon.client.particle.particles.SmallRising;
import io.netty.buffer.ByteBuf;

public enum Particles {
   Blob,
   BlueMagic,
   CycloneBlob,
   Dragon,
   Electric,
   Leaf,
   RedOrbShrinking,
   ShadowBlob,
   Shiny,
   SlingRing,
   SmallRising;

   public static void write(ByteBuf buf, Particles particle, Object... args) {
      buf.writeByte(particle.ordinal());
      int i = 0;
      switch (particle) {
         case Blob:
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeBoolean((Boolean)args[i++]);
            buf.writeInt((Integer)args[i++]);
            buf.writeInt((Integer)args[i++]);
            buf.writeFloat((Float)args[i++]);
         case BlueMagic:
         default:
            break;
         case CycloneBlob:
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            break;
         case Dragon:
         case SmallRising:
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            break;
         case Electric:
            buf.writeInt((Integer)args[i++]);
            buf.writeBoolean((Boolean)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeFloat((Float)args[i++]);
            break;
         case Leaf:
            buf.writeDouble((Double)args[i++]);
            buf.writeFloat((Float)args[i++]);
            buf.writeBoolean((Boolean)args[i++]);
            break;
         case RedOrbShrinking:
         case Shiny:
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            buf.writeDouble((Double)args[i++]);
            break;
         case ShadowBlob:
            buf.writeDouble((Double)args[i++]);
            break;
         case SlingRing:
            buf.writeBoolean((Boolean)args[i++]);
            buf.writeInt((Integer)args[i++]);
      }

   }

   public static ParticleEffect read(ByteBuf buf) {
      Particles particle = values()[buf.readByte()];
      switch (particle) {
         case Blob:
            return new Blob(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readFloat());
         case BlueMagic:
            return new BlueMagic();
         case CycloneBlob:
            return new CycloneBlob(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
         case Dragon:
            return new Dragon(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
         case SmallRising:
            return new SmallRising(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
         case Electric:
            return new Electric(buf.readInt(), buf.readBoolean(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
         case Leaf:
            return new Leaf(buf.readDouble(), buf.readFloat(), buf.readBoolean());
         case RedOrbShrinking:
            return new RedOrbShrinking(buf.readDouble(), buf.readDouble(), buf.readDouble());
         case Shiny:
            return new Shiny(buf.readDouble(), buf.readDouble(), buf.readDouble());
         case ShadowBlob:
            return new ShadowBlob(buf.readDouble());
         case SlingRing:
            return new SlingRing(buf.readBoolean(), buf.readInt());
         default:
            return null;
      }
   }
}
