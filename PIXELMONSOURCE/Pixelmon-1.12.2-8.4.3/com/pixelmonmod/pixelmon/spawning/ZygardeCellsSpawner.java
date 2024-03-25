package com.pixelmonmod.pixelmon.spawning;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.blocks.BlockZygardeCell;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeCell;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.Scheduling;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ZygardeCellsSpawner {
   private static final boolean DEBUG = false;
   private static final int SEA_LEVEL = 62;
   private static final int HALF_HOUR_IN_TICKS = 36000;
   private static final Set SPAWNABLE_BLOCKS;
   private static final Set hasCube;
   private static boolean clientHasCube;
   private static long clientLastCheck;

   public static void setup() {
      Scheduling.schedule(200, (Consumer)((task) -> {
         MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
         hasCube.clear();
         if (server != null && server.func_71278_l()) {
            Iterator var2 = server.func_184103_al().func_181057_v().iterator();

            while(var2.hasNext()) {
               EntityPlayerMP player = (EntityPlayerMP)var2.next();
               if (checkForCube(player)) {
                  hasCube.add(player.func_110124_au());
               }
            }
         }

      }), true);
      Scheduling.schedule(200, (Consumer)((task) -> {
         if (PixelmonConfig.spawnZygardeCells) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server != null && server.func_71278_l()) {
               if (hasCube.isEmpty()) {
                  return;
               }

               UUID random = (UUID)RandomHelper.getRandomElementFromArray(hasCube.toArray(new UUID[0]));
               EntityPlayerMP player = server.func_184103_al().func_177451_a(random);
               if (player == null || player.func_175149_v()) {
                  return;
               }

               List chunks = Lists.newArrayList();
               int distance = server.func_184103_al().func_72395_o();
               int x1 = player.field_70176_ah + distance;
               int z1 = player.field_70164_aj + distance;
               int x2 = player.field_70176_ah - distance;
               int z2 = player.field_70164_aj - distance;
               int cellCount = 0;

               int x;
               for(x = x1; x >= x2; --x) {
                  for(int z = z1; z >= z2; --z) {
                     if (x < player.field_70176_ah - 1 || x > player.field_70176_ah + 1 || z < player.field_70164_aj - 1 || z > player.field_70164_aj + 1) {
                        Chunk chunk = player.func_71121_q().func_72863_F().func_186026_b(x, z);
                        if (chunk != null && chunk.func_177410_o()) {
                           Iterator var14 = chunk.func_177434_r().values().iterator();

                           while(var14.hasNext()) {
                              TileEntity te = (TileEntity)var14.next();
                              if (te instanceof TileEntityZygardeCell) {
                                 ++cellCount;
                              }
                           }

                           if (chunk.func_177416_w() < 36000L) {
                              chunks.add(chunk);
                           }
                        }
                     }
                  }
               }

               if (!chunks.isEmpty() && cellCount < 3) {
                  for(x = 0; x < 3 && !trySpawnInChunk(player, (Chunk)RandomHelper.getRandomElementFromList(chunks)); ++x) {
                  }
               }
            }

         }
      }), true);
   }

   public static boolean hasCube(EntityPlayer player) {
      return hasCube.contains(player.func_110124_au());
   }

   @SideOnly(Side.CLIENT)
   public static boolean clientHasCube() {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (clientLastCheck < player.field_70170_p.func_82737_E() - 20L) {
         clientHasCube = checkForCube(player);
         clientLastCheck = player.field_70170_p.func_82737_E();
      }

      return clientHasCube;
   }

   public static boolean checkForCube(@Nonnull EntityPlayer player) {
      InventoryPlayer inv = player.field_71071_by;
      Predicate check = (itemStack) -> {
         return itemStack.func_77973_b() == PixelmonItems.zygardeCube;
      };
      return inv.field_70462_a.stream().anyMatch(check) || inv.field_184439_c.stream().anyMatch(check);
   }

   public static boolean trySpawnInChunk(EntityPlayerMP player, Chunk chunk) {
      World world = chunk.func_177412_p();
      int x = RandomHelper.getRandomNumberBetween(1, 14);
      int z = RandomHelper.getRandomNumberBetween(1, 14);
      BlockPos pos = chunk.func_76632_l().func_180331_a(x, 62, z);
      Biome biome = chunk.func_177411_a(pos, world.func_72959_q());
      if (biome.field_76752_A != Blocks.field_150349_c.func_176223_P()) {
         return false;
      } else {
         TileEntityZygardeCell te = (TileEntityZygardeCell)BlockHelper.findClosestTileEntity(TileEntityZygardeCell.class, player.func_71121_q(), pos, 72.0, (t) -> {
            return true;
         });
         if (te != null) {
            return false;
         } else {
            int y;
            for(y = 62; y <= 120; ++y) {
               pos = chunk.func_76632_l().func_180331_a(x, y, z);
               Block block = chunk.func_177435_g(pos).func_177230_c();
               if (block == Blocks.field_150350_a && chunk.func_177444_d(pos)) {
                  if (world.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150355_j || world.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150353_l) {
                     y = 120;
                  }
                  break;
               }
            }

            if (y != 120) {
               Multimap map = MultimapBuilder.hashKeys().hashSetValues().build();
               int x1 = pos.func_177958_n() + 1;
               int y1 = pos.func_177956_o() + 1;
               int z1 = pos.func_177952_p() + 1;
               int x2 = pos.func_177958_n() - 1;
               int y2 = pos.func_177956_o() - 4;
               int z2 = pos.func_177952_p() - 1;

               for(int lx = x1; lx >= x2; --lx) {
                  for(int ly = y1; ly >= y2; --ly) {
                     for(int lz = z1; lz >= z2; --lz) {
                        pos = new BlockPos(lx, ly, lz);
                        Block b = chunk.func_177435_g(pos).func_177230_c();
                        if (SPAWNABLE_BLOCKS.contains(b)) {
                           map.put(b, pos);
                        }
                     }
                  }
               }

               if (!map.isEmpty()) {
                  EnumFacing facing;
                  ArrayList grass;
                  Iterator var23;
                  BlockPos pos1;
                  IBlockState state;
                  if (map.containsKey(Blocks.field_150364_r) || map.containsKey(Blocks.field_150363_s)) {
                     grass = Lists.newArrayList(map.get(Blocks.field_150364_r));
                     grass.addAll(map.get(Blocks.field_150363_s));
                     Collections.shuffle(grass);
                     var23 = grass.iterator();

                     while(var23.hasNext()) {
                        pos1 = (BlockPos)var23.next();
                        state = chunk.func_177435_g(pos1);
                        if (state.func_177229_b(BlockLog.field_176299_a) == EnumAxis.X) {
                           facing = hasAirPocket(chunk, pos1, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST);
                           if (facing != null) {
                              spawnOn(chunk, pos1.func_177972_a(facing), facing.func_176734_d(), player);
                              return true;
                           }
                        } else if (state.func_177229_b(BlockLog.field_176299_a) == EnumAxis.Z) {
                           facing = hasAirPocket(chunk, pos1, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH);
                           if (facing != null) {
                              spawnOn(chunk, pos1.func_177972_a(facing), facing.func_176734_d(), player);
                              return true;
                           }
                        } else {
                           facing = hasAirPocket(chunk, pos1, EnumFacing.field_176754_o);
                           if (facing != null) {
                              spawnOn(chunk, pos1.func_177972_a(facing), facing.func_176734_d(), player);
                              return true;
                           }
                        }
                     }
                  }

                  if (map.containsKey(Blocks.field_150362_t) || map.containsKey(Blocks.field_150361_u)) {
                     grass = Lists.newArrayList(map.get(Blocks.field_150362_t));
                     grass.addAll(map.get(Blocks.field_150361_u));
                     Collections.shuffle(grass);
                     var23 = grass.iterator();

                     while(var23.hasNext()) {
                        pos1 = (BlockPos)var23.next();
                        state = chunk.func_177435_g(pos1);
                        if ((Boolean)state.func_177229_b(BlockLeaves.field_176237_a)) {
                           facing = hasAirPocket(chunk, pos1, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
                           if (facing != null) {
                              spawnOn(chunk, pos1.func_177972_a(facing), facing.func_176734_d(), player);
                              return true;
                           }
                        }
                     }
                  }

                  if (map.containsKey(Blocks.field_150349_c)) {
                     grass = Lists.newArrayList(map.get(Blocks.field_150349_c));
                     Collections.shuffle(grass);
                     var23 = grass.iterator();

                     while(var23.hasNext()) {
                        pos1 = (BlockPos)var23.next();
                        EnumFacing facing = hasAirPocket(chunk, pos1, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
                        if (facing != null) {
                           spawnOn(chunk, pos1.func_177972_a(facing), facing.func_176734_d(), player);
                           return true;
                        }
                     }
                  }
               }
            }

            return false;
         }
      }
   }

   private static EnumFacing hasAirPocket(Chunk chunk, BlockPos pos, EnumFacing... facings) {
      List facingList = Lists.newArrayList();
      EnumFacing[] var4 = facings;
      int var5 = facings.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing facing = var4[var6];
         BlockPos offset = pos.func_177972_a(facing);
         if (chunk.func_76632_l().equals(new ChunkPos(offset)) && chunk.func_177435_g(offset).func_177230_c() == Blocks.field_150350_a) {
            facingList.add(facing);
         }
      }

      return (EnumFacing)RandomHelper.getRandomElementFromList(facingList);
   }

   private static void spawnOn(Chunk chunk, BlockPos pos, EnumFacing facing, EntityPlayerMP player) {
      EnumFacing rotation = facing.func_176740_k() == Axis.Y ? (EnumFacing)RandomHelper.getRandomElementFromArray(EnumFacing.field_176754_o) : (RandomHelper.getRandomChance() ? EnumFacing.UP : EnumFacing.DOWN);
      Block block = RandomHelper.getRandomChance(5) ? PixelmonBlocks.zygardeCore : PixelmonBlocks.zygardeCell;
      IBlockState state = block.func_176223_P().func_177226_a(BlockZygardeCell.ORIENTATION_PROPERTY, facing).func_177226_a(BlockZygardeCell.ROTATION_PROPERTY, rotation);
      chunk.func_177412_p().func_175656_a(pos, state);
   }

   static {
      SPAWNABLE_BLOCKS = Sets.newHashSet(new Block[]{Blocks.field_150349_c, Blocks.field_150362_t, Blocks.field_150361_u, Blocks.field_150364_r, Blocks.field_150363_s});
      hasCube = Sets.newHashSet();
      clientHasCube = false;
      clientLastCheck = 0L;
   }
}
