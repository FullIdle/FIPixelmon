package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.biome.Biome;

public class GetBiomeData extends PixelmonCommand {
   public GetBiomeData() {
      super("getbiomedata", "/getbiomedata", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      writeToFile("Biome Name, Temperature, Rainfall, RootHeight, HeightVariation, TopBlock, waterColorMultiplier, fillerBlock");
      Iterator var3 = Biome.field_185377_q.iterator();

      while(var3.hasNext()) {
         Biome biome = (Biome)var3.next();
         String strData = "";

         try {
            strData = strData + biome.getRegistryName().func_110623_a();
         } catch (Exception var14) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.func_185353_n();
         } catch (Exception var13) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.func_76727_i();
         } catch (Exception var12) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.func_185355_j();
         } catch (Exception var11) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + (biome.func_185355_j() + biome.func_185360_m());
         } catch (Exception var10) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.field_76752_A.func_177230_c().func_149739_a();
         } catch (Exception var9) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.getWaterColorMultiplier();
         } catch (Exception var8) {
            strData = strData + "---";
         }

         strData = strData + ",";

         try {
            strData = strData + biome.field_76753_B.func_177230_c().func_149732_F();
         } catch (Exception var7) {
            strData = strData + "---";
         }

         strData = strData + ",";
         writeToFile(strData);
      }

   }

   private static String getSaveFile() {
      return Pixelmon.modDirectory + "";
   }

   private static void writeToFile(String strToWrite) {
      File saveDirPath = new File(getSaveFile());
      if (!saveDirPath.exists()) {
         saveDirPath.mkdirs();
      }

      try {
         saveDirPath = new File(getSaveFile() + "/BiomeData.csv");
         FileWriter fw = new FileWriter(saveDirPath.getAbsoluteFile(), true);
         BufferedWriter bw = new BufferedWriter(fw);
         bw.append(strToWrite).append("\n");
         bw.close();
      } catch (IOException var4) {
         System.out.println("[Error] Error saving data file for " + strToWrite);
         var4.printStackTrace();
      }

   }
}
