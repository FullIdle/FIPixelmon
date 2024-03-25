package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.Pixelmon;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.util.Tuple;

public class BlockRevealParser {
   private static HashMap blocksToReveal = new HashMap();

   public static void parse() {
      Iterator var0 = PixelmonConfig.oreColors.iterator();

      while(var0.hasNext()) {
         String value = (String)var0.next();
         String[] args = value.split(";");
         if (args.length == 3) {
            String block = args[0];
            String colorStr = args[1];
            String patternStr = args[2];

            try {
               blocksToReveal.put(block, new Tuple(Integer.parseInt(colorStr, 16), Integer.parseInt(patternStr)));
            } catch (Exception var7) {
               Pixelmon.LOGGER.warn("Invalid oreColor: " + value);
            }
         }
      }

   }

   public static Tuple getEntryForBlock(String registryName) {
      return (Tuple)blocksToReveal.get(registryName);
   }
}
