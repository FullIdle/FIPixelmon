package com.pixelmonmod.pixelmon.util.helpers;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockHelper {
   public static TileEntity findClosestTileEntity(Class tileEntity, Entity entity, double range, Predicate predicate) {
      Map map = findAllTileEntityWithinRange(tileEntity, entity, range, predicate);
      if (map.size() == 0) {
         return null;
      } else {
         Map.Entry closest = null;
         double distance = range + 0.1;
         Iterator var9 = map.entrySet().iterator();

         while(var9.hasNext()) {
            Map.Entry entry = (Map.Entry)var9.next();
            double dis = getDistance(entity, (BlockPos)entry.getKey());
            if (dis < distance) {
               closest = entry;
               distance = dis;
            }
         }

         return closest == null ? null : (TileEntity)closest.getValue();
      }
   }

   public static TileEntity findClosestTileEntity(Class tileEntity, WorldServer world, BlockPos source, double range, Predicate predicate) {
      Map map = findAllTileEntityWithinRange(tileEntity, world, source, range, predicate);
      if (map.size() == 0) {
         return null;
      } else {
         Map.Entry closest = null;
         double distance = range + 0.1;
         Iterator var10 = map.entrySet().iterator();

         while(var10.hasNext()) {
            Map.Entry entry = (Map.Entry)var10.next();
            double dis = getDistance(source, (BlockPos)entry.getKey());
            if (dis < distance) {
               closest = entry;
               distance = dis;
            }
         }

         return closest == null ? null : (TileEntity)closest.getValue();
      }
   }

   public static Map findAllTileEntityWithinRange(Class tileEntity, Entity source, double range, Predicate predicate) {
      int chunkXPos = source.func_180425_c().func_177958_n() >> 4;
      int chunkZPos = source.func_180425_c().func_177952_p() >> 4;
      WorldServer world = (WorldServer)source.func_130014_f_();
      int chunkRange = Math.max((int)(range / 16.0), 1) + 1;
      Map map = Maps.newHashMap();

      for(int x = chunkXPos - chunkRange + 1; x < chunkXPos + chunkRange; ++x) {
         for(int z = chunkZPos - chunkRange + 1; z < chunkZPos + chunkRange; ++z) {
            if (world.func_72863_F().func_73149_a(x, z)) {
               Chunk chunk = world.func_72964_e(x, z);
               Iterator var13 = chunk.func_177434_r().entrySet().iterator();

               while(var13.hasNext()) {
                  Map.Entry entry = (Map.Entry)var13.next();
                  if (tileEntity.isAssignableFrom(((TileEntity)entry.getValue()).getClass()) && getDistance(source, (BlockPos)entry.getKey()) <= range && predicate.test((TileEntity)entry.getValue())) {
                     map.put(entry.getKey(), (TileEntity)entry.getValue());
                  }
               }
            }
         }
      }

      return map;
   }

   public static Map findAllTileEntityWithinRange(Class tileEntity, WorldServer world, BlockPos source, double range, Predicate predicate) {
      int chunkXPos = source.func_177958_n() >> 4;
      int chunkZPos = source.func_177952_p() >> 4;
      int chunkRange = Math.max((int)(range / 16.0), 1) + 1;
      Map map = Maps.newHashMap();

      for(int x = chunkXPos - chunkRange + 1; x < chunkXPos + chunkRange; ++x) {
         for(int z = chunkZPos - chunkRange + 1; z < chunkZPos + chunkRange; ++z) {
            if (world.func_72863_F().func_73149_a(x, z)) {
               Chunk chunk = world.func_72964_e(x, z);
               Iterator var13 = chunk.func_177434_r().entrySet().iterator();

               while(var13.hasNext()) {
                  Map.Entry entry = (Map.Entry)var13.next();
                  if (tileEntity.isAssignableFrom(((TileEntity)entry.getValue()).getClass()) && getDistance(source, (BlockPos)entry.getKey()) <= range && predicate.test((TileEntity)entry.getValue())) {
                     map.put(entry.getKey(), (TileEntity)entry.getValue());
                  }
               }
            }
         }
      }

      return map;
   }

   public static int countTileEntitiesOfType(World world, ChunkPos pos, Class clazz) {
      int count = 0;
      Chunk chunk = world.func_72964_e(pos.field_77276_a, pos.field_77275_b);
      Iterator var5 = chunk.func_177434_r().values().iterator();

      while(var5.hasNext()) {
         TileEntity te = (TileEntity)var5.next();
         if (clazz.isInstance(te)) {
            ++count;
         }
      }

      return count;
   }

   public static TileEntity getTileEntity(Class clazz, IBlockAccess world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      if (te != null && !clazz.isInstance(te)) {
         te.func_145843_s();
         te = world.func_175625_s(pos);
         if (te != null && !clazz.isInstance(te)) {
            te.func_145843_s();
            Pixelmon.LOGGER.info("Bad TileEntity " + pos + " expected " + clazz.getSimpleName() + " got " + te.getClass().getSimpleName());
            return null;
         }

         if (te != null && world instanceof WorldServer) {
            te.func_70296_d();
            ((WorldServer)world).func_184164_w().func_180244_a(pos);
         }
      }

      try {
         return te;
      } catch (Exception var5) {
         te.func_145843_s();
         return null;
      }
   }

   public static boolean validateReach(EntityPlayerMP player, BlockPos pos) {
      World world = player.func_71121_q();
      double dist = player.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111126_e() + 3.0;
      dist *= dist;
      return world.func_175667_e(pos) && player.func_70092_e((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) < dist;
   }

   private static double getDistance(Entity entity, BlockPos pos) {
      return entity.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5);
   }

   private static double getDistance(BlockPos source, BlockPos pos) {
      return source.func_185332_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
   }

   public static void placeBed(World world, BlockPos pos1, EnumFacing facing, EnumDyeColor color) {
      BlockPos pos2 = pos1.func_177972_a(facing);
      IBlockState iblockstate2 = Blocks.field_150324_C.func_176223_P().func_177226_a(BlockBed.field_176471_b, Boolean.FALSE).func_177226_a(BlockBed.field_185512_D, facing).func_177226_a(BlockBed.field_176472_a, EnumPartType.FOOT);
      world.func_180501_a(pos1, iblockstate2, 10);
      world.func_180501_a(pos2, iblockstate2.func_177226_a(BlockBed.field_176472_a, EnumPartType.HEAD), 10);
      TileEntity tileentity = world.func_175625_s(pos2);
      if (tileentity instanceof TileEntityBed) {
         ((TileEntityBed)tileentity).func_193052_a(color);
      }

      TileEntity tileentity1 = world.func_175625_s(pos1);
      if (tileentity1 instanceof TileEntityBed) {
         ((TileEntityBed)tileentity1).func_193052_a(color);
      }

      world.func_175722_b(pos1, Blocks.field_150324_C, false);
      world.func_175722_b(pos2, Blocks.field_150324_C, false);
   }
}
