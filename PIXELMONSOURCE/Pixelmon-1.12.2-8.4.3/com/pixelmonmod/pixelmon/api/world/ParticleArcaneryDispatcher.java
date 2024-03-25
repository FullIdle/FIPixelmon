package com.pixelmonmod.pixelmon.api.world;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.particle.Particles;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SummonParticleArcanery;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ParticleArcaneryDispatcher {
   public static void dispatchToPlayer(EntityPlayerMP player, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float size, Particles particle, Object... args) {
      Pixelmon.network.sendTo(new SummonParticleArcanery(posX, posY, posZ, motionX, motionY, motionZ, size, particle, args), player);
   }

   public static void dispatchToDimension(int dimension, double range, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float size, Particles particle, Object... args) {
      Pixelmon.network.sendToAllAround(new SummonParticleArcanery(posX, posY, posZ, motionX, motionY, motionZ, size, particle, args), new NetworkRegistry.TargetPoint(dimension, posX, posY, posZ, range));
   }
}
