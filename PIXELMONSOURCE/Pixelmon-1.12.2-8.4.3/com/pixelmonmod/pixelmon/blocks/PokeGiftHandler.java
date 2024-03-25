package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumPokegiftEventType;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;

public class PokeGiftHandler {
   private World world;

   public void generate(World world) {
      EnumPokegiftEventType type = this.checkTime();
      if (type != EnumPokegiftEventType.None) {
         this.world = world;
         int x;
         int y;
         int z;
         BlockPos pos;
         if (PixelmonConfig.pokegiftEventCoords.size() >= 3) {
            List coords = PixelmonConfig.pokegiftEventCoords;

            try {
               x = Integer.parseInt((String)coords.get(0));
               y = Integer.parseInt((String)coords.get(1));
               z = Integer.parseInt((String)coords.get(2));
            } catch (NumberFormatException var9) {
               Pixelmon.LOGGER.catching(var9);
               return;
            }

            pos = new BlockPos(x, y, z);
            boolean existsOrPlaced;
            if (!(world.func_180495_p(pos).func_177230_c() instanceof BlockPokegift)) {
               existsOrPlaced = world.func_175656_a(pos, PixelmonBlocks.pokegiftEventBlock.func_176223_P());
            } else {
               existsOrPlaced = true;
            }

            if (!this.ensureChunkExists(x, z) || !existsOrPlaced) {
               return;
            }
         } else {
            pos = world.func_175694_M();
            if (type == EnumPokegiftEventType.Christmas) {
               x = pos.func_177958_n() + 5;
               z = pos.func_177952_p() + 5;
            } else if (type == EnumPokegiftEventType.Halloween) {
               x = pos.func_177958_n() - 5;
               z = pos.func_177952_p() - 5;
            } else {
               if (type != EnumPokegiftEventType.Custom) {
                  return;
               }

               x = pos.func_177958_n() + 5;
               z = pos.func_177952_p() - 5;
            }

            y = world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o();
            pos = new BlockPos(x, y, z);
            if (!this.ensureChunkExists(x, z)) {
               return;
            }

            BlockPos underPos = new BlockPos(pos.func_177958_n(), pos.func_177956_o() - 1, pos.func_177952_p());
            Block underBlock = world.func_180495_p(underPos).func_177230_c();

            while(underPos.func_177956_o() > 0 && world.func_175623_d(underPos) || underBlock.isLeaves(underBlock.func_176194_O().func_177621_b(), this.world, underPos) || underBlock.isWood(this.world, underPos)) {
               pos = underPos;
               underPos = new BlockPos(underPos.func_177958_n(), underPos.func_177956_o() - 1, underPos.func_177952_p());
               underBlock = world.func_180495_p(underPos).func_177230_c();
               if (underBlock instanceof BlockPokegift) {
                  PixelmonConfig.disableEventLoading();
                  return;
               }
            }
         }

         world.func_175656_a(pos, PixelmonBlocks.pokegiftEventBlock.func_176223_P());
         Pixelmon.LOGGER.info("Pokegift Event spawned at " + pos.func_177958_n() + ", " + pos.func_177956_o() + ", " + pos.func_177952_p());
         PixelmonConfig.disableEventLoading();
      }
   }

   private boolean ensureChunkExists(int x, int z) {
      ChunkProviderServer ccServer = (ChunkProviderServer)this.world.func_72863_F();
      int chunkX = x >> 4;
      int chunkZ = z >> 4;
      if (!ccServer.func_73149_a(chunkX, chunkZ)) {
         ccServer.func_186028_c(chunkX, chunkZ);
         return ccServer.func_73149_a(chunkX, chunkZ);
      } else {
         return true;
      }
   }

   public EnumPokegiftEventType checkTime() {
      LocalDate localDate = LocalDate.now();
      String[] dayMonth = PixelmonConfig.customPokegiftEventTime.split("/");
      if (dayMonth.length == 2 && (!dayMonth[0].equalsIgnoreCase("D") || !dayMonth[1].equalsIgnoreCase("M"))) {
         int day = Integer.parseInt(dayMonth[0]);
         int month = Integer.parseInt(dayMonth[1]);
         if (localDate.getMonthValue() == month && localDate.getDayOfMonth() == day) {
            return EnumPokegiftEventType.Custom;
         }
      }

      if (localDate.getMonth() == Month.OCTOBER && localDate.getDayOfMonth() > 12 && localDate.getDayOfMonth() < 26) {
         return EnumPokegiftEventType.Halloween;
      } else {
         return localDate.getMonth() == Month.DECEMBER && localDate.getDayOfMonth() > 7 && localDate.getDayOfMonth() < 30 ? EnumPokegiftEventType.Christmas : EnumPokegiftEventType.None;
      }
   }
}
