package com.pixelmonmod.pixelmon.client.particle;

import java.util.Random;
import net.minecraft.util.math.Vec3d;

public class ParticleMathHelper {
   public static Vec3d generatePointInSphere(double r, Random rand) {
      double radius = (double)((float)rand.nextDouble()) * r;
      double phi = (double)((float)(rand.nextDouble() * Math.PI));
      double theta = (double)((float)(rand.nextDouble() * Math.PI * 2.0));
      double tempsetX = (double)((float)(radius * Math.cos(theta) * Math.sin(phi)));
      double tempsetY = (double)((float)(radius * Math.sin(theta) * Math.sin(phi)));
      double tempsetZ = (double)((float)(radius * Math.cos(phi)));
      return new Vec3d(tempsetX, tempsetY, tempsetZ);
   }
}
