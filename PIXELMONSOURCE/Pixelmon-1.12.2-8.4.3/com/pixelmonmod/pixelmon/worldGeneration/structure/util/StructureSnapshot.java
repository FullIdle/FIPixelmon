package com.pixelmonmod.pixelmon.worldGeneration.structure.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.util.PixelBlockSnapshot;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class StructureSnapshot {
   private final int width;
   private final int height;
   private final int length;
   private List statues = Lists.newArrayList();
   private Map tileEntityData = Maps.newHashMap();
   private BiMap idToBlock = HashBiMap.create(32);
   private int[][][] ids;

   public StructureSnapshot(int width, int height, int length) {
      this.width = width;
      this.height = height;
      this.length = length;
      this.ids = new int[width][height][length];
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getLength() {
      return this.length;
   }

   public PixelBlockSnapshot getBlockAt(int x, int y, int z) {
      int id = this.ids[x][y][z];
      BlockPos pos = new BlockPos(x, y, z);
      BlockState state = (BlockState)this.idToBlock.inverse().get(id);
      NBTTagCompound nbt = (NBTTagCompound)this.tileEntityData.getOrDefault(pos, (Object)null);
      return new PixelBlockSnapshot(pos, state.getBlockState(), nbt);
   }

   public List getStatues() {
      return this.statues;
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.func_74774_a("formatVersion", (byte)1);
      compound.func_74768_a("Width", this.width);
      compound.func_74768_a("Height", this.height);
      compound.func_74768_a("Length", this.length);
      NBTTagList palette = new NBTTagList();
      compound.func_74782_a("palette", palette);

      NBTTagCompound blocks;
      for(int i = 0; i < this.idToBlock.size(); ++i) {
         blocks = new NBTTagCompound();
         BlockState state = (BlockState)this.idToBlock.inverse().get(i);
         ResourceLocation rl = state.block.getRegistryName();
         blocks.func_74778_a("id", rl.toString());
         blocks.func_74774_a("meta", (byte)state.meta);
         palette.func_74742_a(blocks);
      }

      boolean largePalette = this.idToBlock.size() > 127;
      blocks = new NBTTagCompound();
      compound.func_74782_a("blocks", blocks);

      for(int y = 0; y < this.height; ++y) {
         int[] ySlice = new int[this.width * this.length];
         int k = 0;

         int i;
         for(int x = 0; x < this.width; ++x) {
            for(i = 0; i < this.length; ++i) {
               ySlice[k++] = this.ids[x][y][i];
            }
         }

         if (largePalette) {
            blocks.func_74783_a(y + "", ySlice);
         } else {
            byte[] newySlice = new byte[ySlice.length];

            for(i = 0; i < ySlice.length; ++i) {
               newySlice[i] = (byte)ySlice[i];
            }

            blocks.func_74773_a(y + "", newySlice);
         }
      }

      NBTTagList tileEntities = new NBTTagList();
      compound.func_74782_a("tileEntities", tileEntities);
      Iterator var14 = this.tileEntityData.entrySet().iterator();

      NBTTagCompound statue;
      while(var14.hasNext()) {
         Map.Entry tile = (Map.Entry)var14.next();
         statue = new NBTTagCompound();
         statue.func_74768_a("x", ((Vec3i)tile.getKey()).func_177958_n());
         statue.func_74768_a("y", ((Vec3i)tile.getKey()).func_177956_o());
         statue.func_74768_a("z", ((Vec3i)tile.getKey()).func_177952_p());
         statue.func_74782_a("data", (NBTBase)tile.getValue());
         tileEntities.func_74742_a(statue);
      }

      NBTTagList statues = new NBTTagList();
      compound.func_74782_a("statues", statues);
      Iterator var17 = this.statues.iterator();

      while(var17.hasNext()) {
         statue = (NBTTagCompound)var17.next();
         statues.func_74742_a(statue);
      }

   }

   public static StructureSnapshot from(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, World world) {
      StructureSnapshot snapshot = new StructureSnapshot(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
      int nextId = 0;
      snapshot.idToBlock.put(new BlockState(Blocks.field_150350_a, 0), nextId++);

      for(int x = minX; x <= maxX; ++x) {
         for(int y = minY; y <= maxY; ++y) {
            for(int z = minZ; z <= maxZ; ++z) {
               BlockPos pos = new BlockPos(x, y, z);
               BlockState state = new BlockState(world.func_180495_p(pos));
               if (!snapshot.idToBlock.containsKey(state)) {
                  snapshot.idToBlock.put(state, nextId++);
               }

               snapshot.ids[x - minX][y - minY][z - minZ] = (Integer)snapshot.idToBlock.get(state);
               if (world.func_175625_s(pos) != null) {
                  TileEntity te = world.func_175625_s(pos);
                  NBTTagCompound compound = new NBTTagCompound();
                  te.func_189515_b(compound);
                  snapshot.tileEntityData.put(new BlockPos(x - minX, y - minY, z - minZ), compound);
               }
            }
         }
      }

      AxisAlignedBB aabb = new AxisAlignedBB((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);
      List statues = world.func_72872_a(EntityStatue.class, aabb);
      Iterator var23 = statues.iterator();

      while(var23.hasNext()) {
         EntityStatue statue = (EntityStatue)var23.next();
         NBTTagCompound statueTag = new NBTTagCompound();
         statue.func_189511_e(statueTag);
         NBTTagList pos = statueTag.func_150295_c("Pos", 6);
         double posX = pos.func_150309_d(0);
         double posY = pos.func_150309_d(1);
         double posZ = pos.func_150309_d(2);
         pos.func_150304_a(0, new NBTTagDouble(posX - (double)minX));
         pos.func_150304_a(1, new NBTTagDouble(posY - (double)minY));
         pos.func_150304_a(2, new NBTTagDouble(posZ - (double)minZ));
         snapshot.statues.add(statueTag);
      }

      return snapshot;
   }

   public static StructureSnapshot readFromNBT(NBTTagCompound compound) {
      return compound.func_74771_c("formatVersion") == 0 ? loadOldFormat(compound) : read(compound);
   }

   private static StructureSnapshot read(NBTTagCompound compound) {
      int width = compound.func_74762_e("Width");
      int height = compound.func_74762_e("Height");
      int length = compound.func_74762_e("Length");
      StructureSnapshot snapshot = new StructureSnapshot(width, height, length);
      NBTTagList palette = compound.func_150295_c("palette", 10);

      NBTTagCompound blocks;
      for(int i = 0; i < palette.func_74745_c(); ++i) {
         blocks = palette.func_150305_b(i);
         Block block = (Block)Block.field_149771_c.func_82594_a(new ResourceLocation(blocks.func_74779_i("id")));
         snapshot.idToBlock.put(new BlockState(block, blocks.func_74762_e("meta")), i);
      }

      boolean largePalette = snapshot.idToBlock.size() > 127;
      blocks = compound.func_74775_l("blocks");
      compound.func_74782_a("blocks", blocks);

      for(int y = 0; y < height; ++y) {
         int[] ySlice = blocks.func_74759_k(y + "");
         byte[] smallSlice = blocks.func_74770_j(y + "");
         int k = 0;

         for(int x = 0; x < width; ++x) {
            for(int z = 0; z < length; ++z) {
               snapshot.ids[x][y][z] = largePalette ? ySlice[k++] : smallSlice[k++];
            }
         }
      }

      NBTTagList tileEntities = compound.func_150295_c("tileEntities", 10);

      for(int i = 0; i < tileEntities.func_74745_c(); ++i) {
         NBTTagCompound tileData = tileEntities.func_150305_b(i);
         Vec3i pos = new Vec3i(tileData.func_74762_e("x"), tileData.func_74762_e("y"), tileData.func_74762_e("z"));
         snapshot.tileEntityData.put(pos, tileData.func_74775_l("data"));
      }

      NBTTagList statues = compound.func_150295_c("statues", 10);

      for(int i = 0; i < statues.func_74745_c(); ++i) {
         snapshot.statues.add(statues.func_150305_b(i));
      }

      return snapshot;
   }

   private static StructureSnapshot loadOldFormat(NBTTagCompound compound) {
      int width = compound.func_74762_e("Width");
      int height = compound.func_74762_e("Height");
      int length = compound.func_74762_e("Length");
      StructureSnapshot snapshot = new StructureSnapshot(width, height, length);
      int nextId = 0;
      snapshot.idToBlock.put(new BlockState(Blocks.field_150350_a, 0), nextId++);

      int y;
      for(y = 0; y < height; ++y) {
         for(int x = 0; x < width; ++x) {
            for(int z = 0; z < length; ++z) {
               NBTTagCompound blockTag = (NBTTagCompound)compound.func_74781_a("X" + x + "Y" + y + "Z" + z);
               if (blockTag.func_74767_n("hasTE")) {
                  snapshot.tileEntityData.put(new Vec3i(x, y, z), blockTag.func_74775_l("tileEntity"));
               }

               String domain = blockTag.func_74764_b("blockMod") ? blockTag.func_74779_i("blockMod") : "minecraft";
               String resource = blockTag.func_74779_i("blockName");
               int meta = blockTag.func_74762_e("metadata");
               Block block = (Block)Block.field_149771_c.func_82594_a(new ResourceLocation(domain, resource));
               if (block != Blocks.field_150350_a) {
                  BlockState state = new BlockState(block, meta);
                  if (!snapshot.idToBlock.containsKey(state)) {
                     snapshot.idToBlock.put(state, nextId++);
                  }

                  int id = (Integer)snapshot.idToBlock.get(state);
                  snapshot.ids[x][y][z] = id;
               }
            }
         }
      }

      y = compound.func_74762_e("numStatues");
      if (y > 0) {
         float baseX = (float)compound.func_74762_e("baseX");
         float baseY = (float)compound.func_74762_e("baseY");
         float baseZ = (float)compound.func_74762_e("baseZ");

         for(int i = 0; i < y; ++i) {
            NBTTagCompound statue = compound.func_74775_l("statue" + i);
            NBTTagList pos = statue.func_150295_c("Pos", 6);
            double posX = pos.func_150309_d(0);
            double posY = pos.func_150309_d(1);
            double posZ = pos.func_150309_d(2);
            pos.func_150304_a(0, new NBTTagDouble(posX - (double)baseX));
            pos.func_150304_a(1, new NBTTagDouble(posY - (double)baseY));
            pos.func_150304_a(2, new NBTTagDouble(posZ - (double)baseZ));
            snapshot.statues.add(statue);
         }
      }

      return snapshot;
   }

   private static class BlockState {
      Block block;
      int meta;

      public BlockState(Block block, int meta) {
         this.block = block;
         this.meta = meta;
      }

      public BlockState(IBlockState state) {
         this.block = state.func_177230_c();
         this.meta = this.block.func_176201_c(state);
      }

      public IBlockState getBlockState() {
         return this.block.func_176203_a(this.meta);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            BlockState state = (BlockState)o;
            return this.meta == state.meta && Objects.equals(this.block, state.block);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.block, this.meta});
      }
   }
}
