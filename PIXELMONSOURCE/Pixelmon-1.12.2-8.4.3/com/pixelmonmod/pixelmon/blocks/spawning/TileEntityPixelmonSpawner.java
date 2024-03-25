package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.enums.EnumSpawnerAggression;
import com.pixelmonmod.pixelmon.blocks.machines.PokemonRarity;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPixelmonSpawner extends TileEntity implements ITickable {
   public ArrayList pokemonList = new ArrayList();
   public int spawnTick = 40;
   public int spawnRadius = 8;
   public int maxSpawns = 5;
   public int levelMin = 5;
   public int levelMax = 10;
   public boolean fireOnTick = true;
   public boolean spawnRandom = false;
   public EnumSpawnerAggression aggression;
   public int bossRatio;
   public ArrayList spawnedPokemon;
   public SpawnLocationType spawnLocation;
   private boolean editing;
   private int tick;
   public static Set validLandFloorMaterials = new HashSet();
   public static Set validLandAirMaterials;
   private static Set validWaterFloorMaterials;
   private static Set validWaterAirMaterials;

   public TileEntityPixelmonSpawner() {
      this.aggression = EnumSpawnerAggression.Default;
      this.bossRatio = 100;
      this.spawnedPokemon = new ArrayList();
      this.spawnLocation = SpawnLocationType.Land;
      this.editing = false;
      this.tick = -1;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("spawnTick", this.spawnTick);
      nbt.func_74777_a("spawnRadius", (short)this.spawnRadius);
      nbt.func_74777_a("maxSpawns", (short)this.maxSpawns);
      nbt.func_74777_a("levelMin", (short)this.levelMin);
      nbt.func_74777_a("levelMax", (short)this.levelMax);
      nbt.func_74757_a("fireOnTick", this.fireOnTick);
      nbt.func_74757_a("spawnRandom", this.spawnRandom);
      nbt.func_74777_a("aggression", (short)this.aggression.ordinal());
      nbt.func_74777_a("bossRatio", (short)this.bossRatio);
      nbt.func_74777_a("numPokemon", (short)this.pokemonList.size());
      nbt.func_74777_a("spawnLocation", (short)this.spawnLocation.ordinal());

      for(int i = 0; i < this.pokemonList.size(); ++i) {
         nbt.func_74782_a("pokemonSpec" + i, ((PokemonRarity)this.pokemonList.get(i)).pokemon.writeToNBT(new NBTTagCompound()));
         nbt.func_74777_a("rarity" + i, (short)((PokemonRarity)this.pokemonList.get(i)).rarity);
      }

      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.spawnTick = nbt.func_74762_e("spawnTick");
      this.spawnRadius = nbt.func_74765_d("spawnRadius");
      this.maxSpawns = nbt.func_74765_d("maxSpawns");
      if (nbt.func_74764_b("levelMin")) {
         this.levelMin = nbt.func_74765_d("levelMin");
      }

      if (nbt.func_74764_b("levelMax")) {
         this.levelMax = nbt.func_74765_d("levelMax");
      }

      if (nbt.func_74764_b("fireOnTick")) {
         this.fireOnTick = nbt.func_74767_n("fireOnTick");
      }

      if (nbt.func_74764_b("spawnRandom")) {
         this.spawnRandom = nbt.func_74767_n("spawnRandom");
      }

      if (nbt.func_74764_b("aggression")) {
         this.aggression = EnumSpawnerAggression.getFromOrdinal(nbt.func_74765_d("aggression"));
      }

      if (nbt.func_74764_b("bossRatio")) {
         this.bossRatio = nbt.func_74765_d("bossRatio");
      }

      if (nbt.func_74764_b("spawnLocation")) {
         this.spawnLocation = SpawnLocationType.getFromIndex(nbt.func_74765_d("spawnLocation"));
      }

      int numPokemon = nbt.func_74765_d("numPokemon");
      this.pokemonList.clear();

      for(int i = 0; i < numPokemon; ++i) {
         if (nbt.func_74764_b("pokemonName" + i)) {
            this.pokemonList.add(new PokemonRarity(new PokemonSpec(nbt.func_74779_i("pokemonName" + i)), nbt.func_74765_d("rarity" + i)));
         } else if (nbt.func_74764_b("pokemonSpec" + i)) {
            this.pokemonList.add(new PokemonRarity((new PokemonSpec(new String[0])).readFromNBT(nbt.func_74775_l("pokemonSpec" + i)), nbt.func_74765_d("rarity" + i)));
         }
      }

   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && this.fireOnTick && !this.editing) {
         this.doSpawning(false);
      }

   }

   protected static boolean isMostlyEnclosedSpace(World world, BlockPos pos, int radius) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing dir = var3[var5];
         boolean ok = false;

         for(int i = 0; i < radius; ++i) {
            if (world.func_175677_d(new BlockPos(pos.func_177958_n() + dir.func_82601_c() * i, pos.func_177956_o() + dir.func_96559_d() * i, pos.func_177952_p() + dir.func_82599_e() * i), true)) {
               ok = true;
               break;
            }
         }

         if (!ok) {
            return false;
         }
      }

      return true;
   }

   public static boolean isBlockValidForPixelmonSpawning(World world, BlockPos pos, AreaType type) {
      IBlockState groundBlock = world.func_180495_p(pos.func_177977_b());
      Material spawnFloorGroundMaterial = groundBlock.func_185904_a();
      Material[] spawnAirMaterial = new Material[]{world.func_180495_p(pos).func_185904_a(), world.func_180495_p(pos.func_177984_a()).func_185904_a()};
      if (type == TileEntityPixelmonSpawner.AreaType.LAND) {
         return validLandFloorMaterials.contains(spawnFloorGroundMaterial) && validLandAirMaterials.contains(spawnAirMaterial[0]) && validLandAirMaterials.contains(spawnAirMaterial[1]);
      } else if (type == TileEntityPixelmonSpawner.AreaType.WATER) {
         return validWaterFloorMaterials.contains(spawnFloorGroundMaterial) && validWaterAirMaterials.contains(spawnAirMaterial[0]) && validWaterAirMaterials.contains(spawnAirMaterial[1]);
      } else {
         return isMostlyEnclosedSpace(world, pos, 5) && validLandFloorMaterials.contains(spawnFloorGroundMaterial) && validLandAirMaterials.contains(spawnAirMaterial[0]) && validLandAirMaterials.contains(spawnAirMaterial[1]);
      }
   }

   private void spawnPixelmon(PokemonSpec spec) {
      this.checkForDead();
      if (this.spawnedPokemon.size() < this.maxSpawns) {
         if (this.spawnLocation != null) {
            int x = this.field_174879_c.func_177958_n() + this.field_145850_b.field_73012_v.nextInt(this.spawnRadius * 2 + 1) - this.spawnRadius;
            int y = this.field_174879_c.func_177956_o() - 1;
            int z = this.field_174879_c.func_177952_p() + this.field_145850_b.field_73012_v.nextInt(this.spawnRadius * 2 + 1) - this.spawnRadius;
            boolean valid = false;
            if (this.spawnLocation == SpawnLocationType.Land) {
               y = this.getTopSolidBlock(x, y, z);
               valid = isBlockValidForPixelmonSpawning(this.field_145850_b, new BlockPos(x, y, z), TileEntityPixelmonSpawner.AreaType.LAND);
            } else if (this.spawnLocation == SpawnLocationType.Air) {
               y = this.getTopSolidBlock(x, y, z);
               valid = isBlockValidForPixelmonSpawning(this.field_145850_b, new BlockPos(x, y, z), TileEntityPixelmonSpawner.AreaType.LAND);
            } else {
               Integer ytmp;
               if (this.spawnLocation == SpawnLocationType.AirPersistent) {
                  ytmp = this.getFirstAirBlock(x, y, z);
                  if (ytmp != null) {
                     y = ytmp;
                     valid = true;
                  }
               } else if (this.spawnLocation == SpawnLocationType.Water) {
                  ytmp = this.getFirstWaterBlock(x, y, z);
                  if (ytmp != null) {
                     y = ytmp;
                     valid = isBlockValidForPixelmonSpawning(this.field_145850_b, new BlockPos(x, y + 1, z), TileEntityPixelmonSpawner.AreaType.WATER);
                  }
               } else if (this.spawnLocation == SpawnLocationType.UnderGround) {
                  y = this.getTopSolidBlock(x, y, z);
                  valid = isBlockValidForPixelmonSpawning(this.field_145850_b, new BlockPos(x, y, z), TileEntityPixelmonSpawner.AreaType.UNDERGROUND);
               }
            }

            if (valid) {
               spec = spec.copy();
               if (this.bossRatio > 0 && this.field_145850_b.field_73012_v.nextInt(this.bossRatio) == 0 && spec.boss == null) {
                  spec.boss = (byte)EnumBossMode.getRandomMode().ordinal();
               }

               if (spec.level == null) {
                  spec.level = this.field_145850_b.field_73012_v.nextInt(this.levelMax + 1 - this.levelMin) + this.levelMin;
               }

               PixelmonSpawnerEvent event = new PixelmonSpawnerEvent(this, spec, new BlockPos.MutableBlockPos(x, y, z));
               if (Pixelmon.EVENT_BUS.post(event)) {
                  return;
               }

               EntityPixelmon pixelmon = spec.create(this.field_145850_b);
               pixelmon.func_70107_b((double)event.pos.func_177958_n() + 0.5, (double)event.pos.func_177956_o(), (double)event.pos.func_177952_p() + 0.5);
               this.field_145850_b.func_72838_d(pixelmon);
               if (this.aggression != EnumSpawnerAggression.Default) {
                  if (this.aggression == EnumSpawnerAggression.Timid) {
                     pixelmon.aggression = EnumAggression.timid;
                  } else if (this.aggression == EnumSpawnerAggression.Passive) {
                     pixelmon.aggression = EnumAggression.passive;
                  } else if (this.aggression == EnumSpawnerAggression.Aggressive) {
                     pixelmon.aggression = EnumAggression.aggressive;
                  }
               }

               pixelmon.setSpawnLocation(this.spawnLocation);
               pixelmon.setSpawnerParent(this);
               this.spawnedPokemon.add(pixelmon);
            }

         }
      }
   }

   private int getTopSolidBlock(int x, int y, int z) {
      boolean valid = false;

      int i;
      BlockPos pos;
      Material blockMaterial;
      for(i = 1; i <= this.spawnRadius / 2; ++i) {
         pos = new BlockPos(x, y + i, z);
         blockMaterial = this.field_145850_b.func_180495_p(pos).func_185904_a();
         if (validLandAirMaterials.contains(blockMaterial) && this.isSolidSurface(this.field_145850_b, pos)) {
            y += i;
            valid = true;
            break;
         }
      }

      if (!valid) {
         for(i = 1; i <= this.spawnRadius / 2; ++i) {
            pos = new BlockPos(x, y - i, z);
            blockMaterial = this.field_145850_b.func_180495_p(pos).func_185904_a();
            if (validLandAirMaterials.contains(blockMaterial) && this.isSolidSurface(this.field_145850_b, pos)) {
               y -= i;
               break;
            }
         }
      }

      return y;
   }

   private boolean isSolidSurface(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177977_b()).isSideSolid(worldIn, pos, EnumFacing.UP) && !worldIn.func_180495_p(pos).func_185904_a().func_76220_a() && !worldIn.func_180495_p(pos.func_177984_a()).func_185904_a().func_76220_a();
   }

   private Integer getFirstAirBlock(int x, int y, int z) {
      int i;
      for(i = 0; this.field_145850_b.func_180495_p(new BlockPos(x, y + i, z)).func_185904_a() != Material.field_151579_a; ++i) {
         if (i > this.spawnRadius / 2) {
            return null;
         }
      }

      return y + i;
   }

   private Integer getFirstWaterBlock(int x, int y, int z) {
      int i;
      for(i = 0; this.field_145850_b.func_180495_p(new BlockPos(x, y + i, z)).func_185904_a() != Material.field_151586_h; ++i) {
         if (this.field_145850_b.func_180495_p(new BlockPos(x, y + i, z)).func_185904_a() == Material.field_151579_a) {
            return null;
         }
      }

      return y + i;
   }

   private void doSpawning(boolean override) {
      if (this.tick == 0 || override) {
         PokemonSpec p = this.selectPokemonForSpawn();
         if (p == null) {
            return;
         }

         this.spawnPixelmon(p);
         this.resetSpawnTick();
         if (override) {
            return;
         }
      }

      if (this.tick == -1) {
         this.resetSpawnTick();
      }

      --this.tick;
   }

   private void checkForDead() {
      for(int i = 0; i < this.spawnedPokemon.size(); ++i) {
         EntityPixelmon p = (EntityPixelmon)this.spawnedPokemon.get(i);
         if (!p.isLoaded(false) || p.field_70128_L) {
            this.spawnedPokemon.remove(i);
            --i;
         }
      }

   }

   private void resetSpawnTick() {
      this.tick = (int)((double)this.spawnTick * (1.0 + (this.field_145850_b.field_73012_v.nextDouble() - 0.5) * 0.2));
   }

   private PokemonSpec selectPokemonForSpawn() {
      int total = 0;

      PokemonRarity aPokemonList1;
      for(Iterator var2 = this.pokemonList.iterator(); var2.hasNext(); total += aPokemonList1.rarity) {
         aPokemonList1 = (PokemonRarity)var2.next();
      }

      if (this.spawnRandom) {
         return new PokemonSpec(EnumSpecies.randomPoke().name());
      } else if (total <= 0) {
         return null;
      } else {
         int rand = this.field_145850_b.field_73012_v.nextInt(total);
         total = 0;
         Iterator var6 = this.pokemonList.iterator();

         PokemonRarity aPokemonList;
         do {
            if (!var6.hasNext()) {
               return null;
            }

            aPokemonList = (PokemonRarity)var6.next();
            total += aPokemonList.rarity;
         } while(rand >= total);

         return aPokemonList.pokemon;
      }
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public void updateRedstone() {
      if (!this.fireOnTick && !this.editing) {
         this.doSpawning(true);
      }

   }

   public void onActivate() {
      this.editing = true;
   }

   public void finishEdit() {
      this.editing = false;
      this.resetSpawnTick();

      while(!this.spawnedPokemon.isEmpty()) {
         ((EntityPixelmon)this.spawnedPokemon.get(0)).unloadEntity();
         this.spawnedPokemon.remove(0);
      }

      for(int i = 0; i < this.pokemonList.size(); ++i) {
         PokemonRarity pkr = (PokemonRarity)this.pokemonList.get(i);
         PokemonSpec oldSpec = pkr.pokemon;
         if (pkr.pokemon.args.length > 0) {
            pkr.pokemon = new PokemonSpec(pkr.pokemon.args);
            pkr.pokemon.name = oldSpec.name;
         }
      }

   }

   public void despawnAllPokemon() {
      while(!this.spawnedPokemon.isEmpty()) {
         ((EntityPixelmon)this.spawnedPokemon.get(0)).unloadEntity();
         this.spawnedPokemon.remove(0);
      }

   }

   static {
      validLandFloorMaterials.add(Material.field_151577_b);
      validLandFloorMaterials.add(Material.field_151593_r);
      validLandFloorMaterials.add(Material.field_151580_n);
      validLandFloorMaterials.add(Material.field_151592_s);
      validLandFloorMaterials.add(Material.field_151575_d);
      validLandFloorMaterials.add(Material.field_151578_c);
      validLandFloorMaterials.add(Material.field_151576_e);
      validLandFloorMaterials.add(Material.field_151595_p);
      validLandFloorMaterials.add(Material.field_151588_w);
      validLandFloorMaterials.add(Material.field_151597_y);
      validLandFloorMaterials.add(Material.field_151596_z);
      validLandFloorMaterials.add(Material.field_151598_x);
      validLandAirMaterials = new HashSet();
      validLandAirMaterials.add(Material.field_151579_a);
      validLandAirMaterials.add(Material.field_151597_y);
      validLandAirMaterials.add(Material.field_151585_k);
      validLandAirMaterials.add(Material.field_151582_l);
      validWaterFloorMaterials = new HashSet();
      validWaterFloorMaterials.add(Material.field_151586_h);
      validWaterAirMaterials = new HashSet();
      validWaterAirMaterials.add(Material.field_151579_a);
      validWaterAirMaterials.add(Material.field_151586_h);
   }

   public static enum AreaType {
      LAND,
      WATER,
      UNDERGROUND;
   }
}
