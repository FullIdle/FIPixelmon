package com.pixelmonmod.pixelmon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RandomHelper {
   public static final Random rand = new Random();

   public static int getRandomNumberBetween(int min, int max) {
      return useRandomForNumberBetween(rand, min, max);
   }

   public static float getRandomNumberBetween(float min, float max) {
      return useRandomForNumberBetween(rand, min, max);
   }

   public static int[] getRandomDistinctNumbersBetween(int min, int max, int numElements) {
      int totalNumbers = max - min + 1;
      if (numElements < 1) {
         return new int[0];
      } else {
         if (numElements > totalNumbers) {
            numElements = totalNumbers;
         }

         int[] randomNumbers = new int[numElements];
         List allNumbers = new ArrayList(totalNumbers);

         int i;
         for(i = 0; i < totalNumbers; ++i) {
            allNumbers.add(min + i);
         }

         for(i = 0; i < numElements; ++i) {
            randomNumbers[i] = (Integer)getRandomElementFromList(allNumbers);
            allNumbers.remove(new Integer(randomNumbers[i]));
         }

         return randomNumbers;
      }
   }

   public static int useRandomForNumberBetween(Random random, int min, int max) {
      return random.nextInt(Math.max(1, max - min + 1)) + min;
   }

   public static float useRandomForNumberBetween(Random random, float min, float max) {
      return random.nextFloat() * (max - min) + min;
   }

   /** @deprecated */
   public static Object getRandomElementFromList(List list) {
      return getRandomElementFromCollection(list);
   }

   public static Object getRandomElementFromCollection(Collection collection) {
      if (collection.isEmpty()) {
         return null;
      } else {
         int index = rand.nextInt(collection.size());
         Iterator iterator = collection.iterator();

         for(int i = 0; i < index; ++i) {
            iterator.next();
         }

         return iterator.next();
      }
   }

   public static Object getRandomElementFromArray(Object[] array) {
      return array != null && array.length != 0 ? array[getRandomNumberBetween(0, array.length - 1)] : null;
   }

   public static Object removeRandomElementFromList(List list) {
      return !list.isEmpty() ? list.remove(getRandomNumberBetween(0, list.size() - 1)) : null;
   }

   public static Object removeRandomElementFromCollection(Collection collection) {
      Object element = getRandomElementFromCollection(collection);
      if (element != null) {
         collection.remove(element);
         return element;
      } else {
         return null;
      }
   }

   public static boolean getRandomChance(double chance) {
      return rand.nextDouble() < chance;
   }

   public static boolean getRandomChance(float chance) {
      return rand.nextFloat() < chance;
   }

   public static boolean getRandomChance(int chance) {
      return getRandomChance((float)chance / 100.0F);
   }

   public static boolean getRandomChance() {
      return getRandomChance(0.5F);
   }

   public static boolean getRandomChance(Random random, int chance) {
      return random.nextFloat() < (float)chance / 100.0F;
   }

   public static int getFortuneAmount(int fortune) {
      return fortune > 0 ? Math.max(1, rand.nextInt(fortune + 2)) : 1;
   }

   public static void initXZSeed(Random random, World world, int chunkX, int chunkZ) {
      long seed = world == null ? 0L : world.func_72905_C();
      seed *= seed * 6364136223846793005L + 1442695040888963407L;
      seed += (long)chunkX;
      seed *= seed * 6364136223846793005L + 1442695040888963407L;
      seed += (long)chunkZ;
      seed *= seed * 6364136223846793005L + 1442695040888963407L;
      seed += (long)chunkX;
      seed *= seed * 6364136223846793005L + 1442695040888963407L;
      seed += (long)chunkZ;
      random.setSeed(seed);
   }

   public static Random staticRandomWithXZSeed(World world, int chunkX, int chunkZ) {
      initXZSeed(rand, world, chunkX, chunkZ);
      return rand;
   }

   public static int getRandomIndexFromWeights(List weights) {
      int totalWeight = 0;

      Integer weight;
      for(Iterator var2 = weights.iterator(); var2.hasNext(); totalWeight += weight) {
         weight = (Integer)var2.next();
      }

      if (totalWeight > 0) {
         int num = getRandomNumberBetween(0, totalWeight - 1);
         int sum = 0;

         for(int i = 0; i < weights.size(); ++i) {
            sum += (Integer)weights.get(i);
            if (num < sum) {
               return i;
            }
         }
      }

      return -1;
   }

   public static Color getRandomHighSaturationColor() {
      return Color.getHSBColor(rand.nextFloat() * 360.0F, 1.0F, 1.0F);
   }

   public static Vec3d nextSpherePoint(double radius) {
      double theta = rand.nextDouble() * 2.0 * Math.PI;
      double phi = (rand.nextDouble() - 0.5) * Math.PI;
      double rad = rand.nextDouble() * radius;
      double x = rad * Math.cos(theta) * Math.cos(phi);
      double y = rad * Math.sin(phi);
      double z = rad * Math.sin(theta) * Math.cos(phi);
      return new Vec3d(x, y, z);
   }
}
