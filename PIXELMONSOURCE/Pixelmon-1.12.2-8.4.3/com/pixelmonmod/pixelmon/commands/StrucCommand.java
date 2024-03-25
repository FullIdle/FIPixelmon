package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.ComponentTownPart;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen.WorldGenGym;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class StrucCommand extends PixelmonCommand {
   public StrucCommand() {
      super("struc", "/struc [name]", 4);
   }

   public String func_71517_b() {
      return "struc";
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      World world = sender.func_130014_f_();
      BlockPos cc = sender.func_180425_c();
      Random random = RandomHelper.staticRandomWithXZSeed(world, cc.func_177958_n() >> 4, cc.func_177952_p() >> 4);
      Object structure;
      if (args.length == 0) {
         structure = StructureRegistry.getScatteredStructureFromBiome(random, world.func_180494_b(cc));
         if (structure == null) {
            sender.func_145747_a(new TextComponentTranslation("pixelmon.command.struc.nostruc", new Object[]{world.func_180494_b(cc).getRegistryName().func_110623_a()}));
            return;
         }
      } else {
         if (args[0].equals("list")) {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator var13 = StructureRegistry.structureIds.iterator();

            while(var13.hasNext()) {
               String name = (String)var13.next();
               stringBuilder.append(name).append(" ");
            }

            sender.func_145747_a(new TextComponentString(stringBuilder.toString()));
            return;
         }

         structure = StructureRegistry.getByID(args[0]);
      }

      if (structure == null) {
         sender.func_145747_a(new TextComponentTranslation("pixelmon.command.struc.notfound", new Object[]{args[0]}));
      } else {
         StructureScattered ss = ((StructureInfo)structure).createStructure(random, cc, true, false, func_71521_c(sender).func_174811_aO(), args.length > 1 && args[1].startsWith("technical"));
         if (!ss.generate(world, random)) {
            sender.func_145747_a(new TextComponentTranslation("pixelmon.command.struc.cantfit", new Object[]{cc.toString()}));
         } else if (structure instanceof GymInfo) {
            GymInfo gymInfo = (GymInfo)structure;
            int level = -1;
            if (args.length > 1 && args[1].startsWith("lvl")) {
               try {
                  int parseLevel = Integer.parseInt(args[1].replaceFirst("lvl", ""));
                  if (parseLevel > 0) {
                     level = Math.min(parseLevel, PixelmonServerConfig.maxLevel);
                  }
               } catch (NumberFormatException var11) {
               }
            }

            gymInfo.level = level;
            WorldGenGym.spawnNPCs(world, ss, gymInfo);
         } else {
            ComponentTownPart.spawnVillagers(ss, (StructureInfo)structure, world, ss.func_74874_b());
         }

      }
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/struc [name]";
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return tabComplete(args, StructureRegistry.structureIds);
   }
}
