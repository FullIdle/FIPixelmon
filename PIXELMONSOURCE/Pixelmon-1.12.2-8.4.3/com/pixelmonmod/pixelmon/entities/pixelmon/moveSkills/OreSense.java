package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.IlluminatePositionsPacket;
import com.pixelmonmod.pixelmon.config.BlockRevealParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class OreSense {
   public static int durationTicks = 300;
   public static int maxSearchRadius = 10;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("ore_sense")).setName("pixelmon.moveskill.ore_sense.name").describe("pixelmon.moveskill.ore_sense.description1", "pixelmon.moveskill.ore_sense.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/oresense.png")).setAnyMoves("Magnet Rise", "Metal Sound", "Steel Beam").setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("ab:MagnetPull")})).setDefaultCooldownTicks(2400).setUsePP(true);
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         final World world = pixelmon.field_70170_p;
         final BlockPos middle = pixelmon.func_180425_c();
         (new Timer()).schedule(new TimerTask() {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);

            public void run() {
               ArrayList oreLocations = new ArrayList();

               for(int x = middle.func_177958_n() - OreSense.maxSearchRadius; x <= middle.func_177958_n() + OreSense.maxSearchRadius; ++x) {
                  for(int y = middle.func_177956_o() - OreSense.maxSearchRadius; y <= middle.func_177956_o() + OreSense.maxSearchRadius; ++y) {
                     for(int z = middle.func_177952_p() - OreSense.maxSearchRadius; z <= middle.func_177952_p() + OreSense.maxSearchRadius; ++z) {
                        IBlockState state = world.func_180495_p(this.pos.func_181079_c(x, y, z));
                        if (OreSense.isAnOre(state.func_177230_c())) {
                           oreLocations.add(new BlockPos(this.pos));
                        }
                     }
                  }
               }

               world.func_73046_m().func_152344_a(() -> {
                  EntityPlayerMP player = (EntityPlayerMP)pixelmon.func_70902_q();
                  if (player != null) {
                     HashMap locationColors = new HashMap();
                     Iterator var5 = oreLocations.iterator();

                     while(var5.hasNext()) {
                        BlockPos pos = (BlockPos)var5.next();
                        Block block = world.func_180495_p(pos).func_177230_c();
                        if (block.getRegistryName() != null) {
                           Tuple entry = BlockRevealParser.getEntryForBlock(block.getRegistryName().toString());
                           int color = (Integer)entry.func_76341_a();
                           if (color != -1) {
                              locationColors.put(pos, entry);
                           }
                        }
                     }

                     if (locationColors.isEmpty()) {
                        ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.ore_sense.none");
                     } else {
                        ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.ore_sense.some", pixelmon.func_145748_c_());
                        Pixelmon.network.sendTo(new IlluminatePositionsPacket(OreSense.durationTicks, locationColors), player);
                     }

                  }
               });
            }
         }, 1L);
         return moveSkill.cooldownTicks;
      });
   }

   private static boolean isAnOre(Block block) {
      if (block.getRegistryName() != null) {
         String blockName = block.getRegistryName().toString();
         return BlockRevealParser.getEntryForBlock(blockName) != null;
      } else {
         return false;
      }
   }
}
