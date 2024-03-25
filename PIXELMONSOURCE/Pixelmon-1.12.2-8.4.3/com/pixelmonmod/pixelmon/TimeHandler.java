package com.pixelmonmod.pixelmon;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;

public class TimeHandler {
   static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
   private static String time;
   private static String[] Time;
   private static int timeNow;
   private static long worldTime;

   public static void changeTime() {
      if (PixelmonConfig.useSystemWorldTime) {
         try {
            time = sdf.format(Calendar.getInstance().getTime());
            Time = time.split(":");
            timeNow = Integer.parseInt(Time[0] + Time[1] + Time[2].substring(0, Time[2].length() - 1)) + 18000;
            worldTime = (long)timeNow;
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (!server.func_71218_a(0).field_72995_K) {
               for(int j = 0; j < server.field_71305_c.length - 1; ++j) {
                  server.func_71218_a(j).func_72877_b(worldTime);
               }
            }
         } catch (NullPointerException var2) {
            LogManager.getLogger("Pixelmon").error(var2.toString());
         }
      }

   }
}
