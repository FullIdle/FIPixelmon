package com.pixelmonmod.pixelmon.pokedex;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.comm.ClientUpdatePokedex;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Pokedex {
   /** @deprecated */
   @Deprecated
   public static final HashMap fullPokedex = new HashMap();
   /** @deprecated */
   @Deprecated
   public static int pokedexSize;
   /** @deprecated */
   @Deprecated
   public static EnumSpecies[] actualPokedex;
   private static List enabledSpecies;
   /** @deprecated */
   @Deprecated
   public UUID uuid;
   private Map seenMap;
   /** @deprecated */
   @Deprecated
   public Table formDex;

   public static List getEnabledSpecies() {
      return Collections.unmodifiableList(enabledSpecies);
   }

   public static int size() {
      return enabledSpecies.size();
   }

   /** @deprecated */
   @Deprecated
   public static void loadPokedex() {
      init();
   }

   public static void init() {
      Iterator var0 = enabledSpecies.iterator();

      while(var0.hasNext()) {
         EnumSpecies species = (EnumSpecies)var0.next();
         BaseStats baseStats = species.getBaseStats();
         if (baseStats == null) {
            Pixelmon.LOGGER.warn("Species " + species.getPokemonName() + " has no base stats!");
         } else {
            PokedexEntry entry = new PokedexEntry(species.getNationalPokedexInteger(), baseStats.getPokemonName(), baseStats.getWeight(), baseStats.getHeight());
            fullPokedex.put(species.getNationalPokedexInteger(), entry);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public static int nameToID(String name) {
      EnumSpecies species = EnumSpecies.getFromNameAnyCase(name);
      return species != null ? species.getNationalPokedexInteger() : 0;
   }

   public static boolean isEntryEmpty(int i) {
      if (!fullPokedex.containsKey(i)) {
         return true;
      } else {
         String checkName = ((PokedexEntry)fullPokedex.get(i)).name;
         return checkName.equals("???") || !EnumSpecies.getNameList().contains(checkName);
      }
   }

   public Pokedex() {
      this((UUID)null);
   }

   public Pokedex(UUID uuid) {
      this.formDex = HashBasedTable.create();
      this.uuid = uuid;
      this.seenMap = new HashMap();
   }

   public Pokedex setUUID(UUID uuid) {
      this.uuid = uuid;
      return this;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public Map getSeenMap() {
      return this.seenMap;
   }

   public Table getFormDex() {
      return this.formDex;
   }

   public NBTTagCompound readFromNBT(NBTTagCompound nbt) {
      this.seenMap.clear();
      NBTTagList nbtl = nbt.func_150295_c("Pokedex", 8);

      for(int i = 0; i < nbtl.func_74745_c(); ++i) {
         try {
            String[] s = nbtl.func_150307_f(i).split(":");
            this.seenMap.put(Integer.parseInt(s[0]), EnumPokedexRegisterStatus.get(Integer.parseInt(s[1])));
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      NBTTagList nbt2 = nbt.func_150295_c("FormDex", 8);

      for(int i = 0; i < nbt2.func_74745_c(); ++i) {
         try {
            String[] s = nbt2.func_150307_f(i).split(":");
            this.formDex.put(Integer.parseInt(s[0]), Short.parseShort(s[1]), Short.parseShort(s[2]));
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return nbt;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      NBTTagList nbtl = new NBTTagList();
      Iterator var3 = this.seenMap.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry e = (Map.Entry)var3.next();
         nbtl.func_74742_a(new NBTTagString(e.getKey() + ":" + ((EnumPokedexRegisterStatus)e.getValue()).ordinal()));
      }

      nbt.func_74782_a("Pokedex", nbtl);
      NBTTagList nbt2 = new NBTTagList();
      this.formDex.cellSet().forEach((cell) -> {
         nbt2.func_74742_a(new NBTTagString(cell.getRowKey() + ":" + cell.getColumnKey() + ":" + cell.getValue()));
      });
      nbt.func_74782_a("FormDex", nbt2);
      return nbt;
   }

   public void update() {
      EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.uuid);
      if (player != null) {
         Pixelmon.network.sendTo(new ClientUpdatePokedex(this), player);
      }

   }

   public boolean set(Pokemon pokemon, EnumPokedexRegisterStatus drs) {
      short id = (short)FormLogRegistry.getFirstIdFromPokemon(pokemon);
      if (id != -1) {
         Short aShort = (Short)this.formDex.get(pokemon.getSpecies().getNationalPokedexInteger(), id);
         if (aShort == null) {
            aShort = Short.valueOf((short)0);
         }

         this.formDex.put(pokemon.getSpecies().getNationalPokedexInteger(), id, (short)(aShort + 1));
      }

      return this.set(pokemon.getSpecies().getNationalPokedexInteger(), drs);
   }

   public boolean set(int id, EnumPokedexRegisterStatus drs) {
      if (this.seenMap.containsKey(id) && ((EnumPokedexRegisterStatus)this.seenMap.get(id)).ordinal() > drs.ordinal()) {
         return false;
      } else {
         EnumSpecies latestSpecies = EnumSpecies.values()[EnumSpecies.values().length - 1];
         if (id > 0 && id <= latestSpecies.getNationalPokedexInteger() && EnumSpecies.getFromDex(id) != null) {
            this.seenMap.put(id, drs);
            this.checkForCharms();
            return true;
         } else {
            this.checkForCharms();
            return false;
         }
      }
   }

   public static boolean set(UUID playerUUID, Pokemon id, EnumPokedexRegisterStatus drs) {
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(playerUUID);
      boolean bool = storage.pokedex.set(id, drs);
      storage.pokedex.update();
      return bool;
   }

   public void wipe() {
      this.seenMap.clear();
      this.update();
   }

   public EnumPokedexRegisterStatus get(int id) {
      EnumPokedexRegisterStatus d = (EnumPokedexRegisterStatus)this.seenMap.get(id);
      if (d == null) {
         d = EnumPokedexRegisterStatus.unknown;
      }

      return d;
   }

   public PokedexEntry getEntry(int id) {
      switch (this.get(id)) {
         case caught:
            return (PokedexEntry)fullPokedex.get(id);
         case seen:
            return new PokedexEntry(id, ((PokedexEntry)fullPokedex.get(id)).name);
         default:
            return new PokedexEntry(id, "???");
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean isUnknown(int id) {
      return this.get(id) == EnumPokedexRegisterStatus.unknown;
   }

   public boolean isRegistered(EnumSpecies species) {
      return this.get(species.getNationalPokedexInteger()) != EnumPokedexRegisterStatus.unknown;
   }

   /** @deprecated */
   @Deprecated
   public boolean hasSeen(int id) {
      EnumPokedexRegisterStatus d = this.get(id);
      return d == EnumPokedexRegisterStatus.seen || this.hasCaught(id);
   }

   public boolean hasSeen(EnumSpecies species) {
      EnumPokedexRegisterStatus d = this.get(species.getNationalPokedexInteger());
      return d == EnumPokedexRegisterStatus.seen || this.hasCaught(species.getNationalPokedexInteger());
   }

   /** @deprecated */
   @Deprecated
   public boolean hasCaught(int id) {
      return this.get(id) == EnumPokedexRegisterStatus.caught;
   }

   public boolean hasCaught(EnumSpecies species) {
      return this.get(species.getNationalPokedexInteger()) == EnumPokedexRegisterStatus.caught;
   }

   public int countCaught() {
      return (int)enabledSpecies.stream().filter(this::hasCaught).count();
   }

   public int countCaught(int generation) {
      return generation <= 0 ? this.countCaught() : (int)enabledSpecies.stream().filter((s) -> {
         return s.getGeneration() == generation;
      }).filter(this::hasCaught).count();
   }

   public int countSeen() {
      return (int)enabledSpecies.stream().filter(this::hasSeen).count();
   }

   public int countSeen(int generation) {
      return generation == 0 ? this.countSeen() : (int)enabledSpecies.stream().filter((s) -> {
         return s.getGeneration() == generation;
      }).filter(this::hasSeen).count();
   }

   @SideOnly(Side.CLIENT)
   public void setSeenMap(Map seenMap) {
      this.seenMap = seenMap;
   }

   public Map seenForms(EnumSpecies species) {
      return this.formDex.row(species.getNationalPokedexInteger());
   }

   public void checkForCharms() {
      this.checkForOvalCharm();
      this.checkForShinyCharm();
   }

   public void checkForOvalCharm() {
      if (PixelmonConfig.allowOvalCharmFromPokedex) {
         int latestGeneration = EnumSpecies.getGeneration(EnumSpecies.values()[EnumSpecies.values().length - 1].getGeneration());

         for(int generation = 1; generation <= latestGeneration; ++generation) {
            if (this.countCaught(generation) == EnumSpecies.getCountInGeneration(generation)) {
               PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(this.uuid);
               if (partyStorage.getOvalCharm() == EnumFeatureState.Disabled) {
                  partyStorage.setOvalCharm(EnumFeatureState.Active);
                  OpenScreen.open(partyStorage.getPlayer(), EnumGuiScreen.OvalCharm);
               }
            }
         }

      }
   }

   public void checkForShinyCharm() {
      if (PixelmonConfig.allowShinyCharmFromPokedex && this.countCaught() >= size()) {
         PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(this.uuid);
         if (partyStorage.getShinyCharm() == EnumFeatureState.Disabled) {
            partyStorage.setShinyCharm(EnumFeatureState.Active);
            OpenScreen.open(partyStorage.getPlayer(), EnumGuiScreen.ShinyCharm);
         }

      }
   }

   public void setSeenList(HashMap data) {
      this.seenMap = data;
   }

   static {
      List species = Lists.newArrayList(EnumSpecies.values());
      species.removeIf((it) -> {
         return !PixelmonConfig.isGenerationEnabled(it.getGeneration()) || it == EnumSpecies.MissingNo;
      });
      enabledSpecies = species;
      actualPokedex = (EnumSpecies[])species.toArray(new EnumSpecies[0]);
      pokedexSize = species.size();
   }

   public static class PokedexSpawnData {
      private Biome biome;
      private List times = Lists.newArrayList();
      private List weathers = Lists.newArrayList();
      private Integer maxY;
      private Integer minY;

      public PokedexSpawnData(Biome biome) {
         this.biome = biome;
      }

      public Biome getBiome() {
         return this.biome;
      }

      public List getTimes() {
         return this.times;
      }

      public List getWeathers() {
         return this.weathers;
      }

      public int getMaxY() {
         return this.maxY;
      }

      public int getMinY() {
         return this.minY;
      }

      public void setMaxY(int maxY) {
         this.maxY = maxY;
      }

      public void setMinY(int minY) {
         this.minY = minY;
      }

      public void addWeather(WeatherType weather) {
         this.weathers.add(weather);
      }

      public void addTime(WorldTime time) {
         this.times.add(time);
      }

      public void encryptTo(ByteBuf buffer) {
         buffer.writeInt(Biome.func_185362_a(this.biome));
         buffer.writeInt(this.times.size());
         Iterator var2 = this.times.iterator();

         while(var2.hasNext()) {
            WorldTime time = (WorldTime)var2.next();
            buffer.writeInt(time.ordinal());
         }

         buffer.writeInt(this.weathers.size());
         var2 = this.weathers.iterator();

         while(var2.hasNext()) {
            WeatherType weather = (WeatherType)var2.next();
            buffer.writeInt(weather.ordinal());
         }

         buffer.writeBoolean(this.maxY != null);
         if (this.maxY != null) {
            buffer.writeInt(this.maxY);
         }

         buffer.writeBoolean(this.minY != null);
         if (this.minY != null) {
            buffer.writeInt(this.minY);
         }

      }

      public static PokedexSpawnData decryptFrom(ByteBuf buffer) {
         PokedexSpawnData data = new PokedexSpawnData(Biome.func_185357_a(buffer.readInt()));
         int size = buffer.readInt();

         int i;
         for(i = 0; i < size; ++i) {
            data.addTime(WorldTime.values()[buffer.readInt()]);
         }

         size = buffer.readInt();

         for(i = 0; i < size; ++i) {
            data.addWeather(WeatherType.values()[buffer.readInt()]);
         }

         if (buffer.readBoolean()) {
            data.maxY = buffer.readInt();
         }

         if (buffer.readBoolean()) {
            data.minY = buffer.readInt();
         }

         return data;
      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         if (this.biome != null) {
            sb.append(this.biome.field_76791_y);
         }

         int i;
         if (!this.times.isEmpty()) {
            sb.append(" ").append(I18n.func_135052_a("gui.pokedex.spawning.times1", new Object[0]));

            for(i = 0; i < this.times.size(); ++i) {
               WorldTime time = (WorldTime)this.times.get(i);
               if (i == 0) {
                  sb.append(" ").append(time.getLocalizedName().toLowerCase());
               } else {
                  sb.append(", ");
                  if (i < this.times.size() - 1) {
                     sb.append(time.getLocalizedName().toLowerCase());
                  } else {
                     sb.append(I18n.func_135052_a("gui.pokedex.spawning.times2", new Object[0])).append(" ").append(time.getLocalizedName().toLowerCase());
                  }
               }
            }
         }

         if (!this.weathers.isEmpty()) {
            sb.append(" ").append(I18n.func_135052_a("gui.pokedex.spawning.weather1", new Object[0]));

            for(i = 0; i < this.weathers.size(); ++i) {
               WeatherType weather = (WeatherType)this.weathers.get(i);
               if (i == 0) {
                  sb.append(" ").append(weather.getLocalizedName().toLowerCase());
               } else {
                  sb.append(", ");
                  if (i < this.weathers.size() - 1) {
                     sb.append(weather.getLocalizedName().toLowerCase());
                  } else {
                     sb.append(I18n.func_135052_a("gui.pokedex.spawning.weather2", new Object[0])).append(" ").append(weather.getLocalizedName().toLowerCase());
                  }
               }
            }
         }

         if (this.minY != null) {
            sb.append(" ").append(I18n.func_135052_a("gui.pokedex.spawning.above", new Object[0])).append(this.minY);
         }

         if (this.maxY != null) {
            sb.append(" ").append(I18n.func_135052_a("gui.pokedex.spawning.below", new Object[0])).append(this.maxY);
         }

         return sb.toString();
      }
   }
}
