package com.pixelmonmod.pixelmon.api.pokemon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.EggHatchEvent;
import com.pixelmonmod.pixelmon.api.events.HeldItemChangedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.pokemon.SetLevellingEvent;
import com.pixelmonmod.pixelmon.api.events.storage.ChangeStorageEvent;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.config.RemapHandler;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BonusStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MiniorStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.enums.forms.EnumSolgaleo;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.util.DataSync;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class Pokemon extends PokemonBase {
   protected UUID uuid;
   protected Moveset moveset;
   protected boolean isShiny;
   protected EnumNature nature;
   protected EnumNature mintNature;
   protected EnumGrowth growth;
   protected int friendship;
   protected Level levelContainer;
   protected int level;
   protected int experience;
   protected int dynamaxLevel;
   protected boolean gigantamaxFactor;
   protected Stats stats;
   protected BonusStats bonusStats;
   protected Integer eggCycles;
   protected Integer eggSteps;
   protected ItemStack heldItem;
   protected StatusPersist status;
   protected ExtraStats extraStats;
   protected AbilityBase ability;
   protected int abilitySlot;
   protected String nickname;
   protected String customTexture;
   protected EnumPokeballs caughtBall;
   protected String originalTrainerName;
   protected UUID originalTrainerUUID;
   protected Pokerus pokerus;
   protected boolean inRanch;
   protected int health;
   protected boolean doesLevel;
   protected ArrayList relearnableMoves;
   protected volatile HashMap moveSkillCooldownData;
   protected ArrayList specFlags;
   protected Set ribbons;
   protected EnumRibbonType displayedRibbon;
   protected NBTTagCompound persistentData;
   public int lastBattleCrits;
   protected transient int dimension;
   protected transient int entityID;
   protected transient PokemonStorage storage;
   protected transient StoragePosition position;
   protected final transient DataSync dsUUID;
   protected final transient DataSync dsGrowth;
   protected final transient DataSync dsLevel;
   protected final transient DataSync dsExp;
   protected final transient DataSync dsShiny;
   protected final transient DataSync dsCustomTexture;
   protected final transient DataSync dsNickname;
   protected final transient DataSync dsRibbon;
   protected final transient DataSync dsHealth;
   protected final transient DataSync dsOwner;

   protected Pokemon() {
      this.moveset = (new Moveset(new Attack[]{null, null, null, null})).withPokemon(this);
      this.isShiny = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(0));
      this.nature = null;
      this.mintNature = null;
      this.growth = null;
      this.levelContainer = new Level(new DelegateLink(this));
      this.level = -1;
      this.experience = 0;
      this.dynamaxLevel = 0;
      this.gigantamaxFactor = false;
      this.stats = (new Stats()).withPokemon(this);
      this.bonusStats = new BonusStats(0, 0, 0, 0, 0, 0, 0);
      this.eggCycles = null;
      this.eggSteps = null;
      this.heldItem = ItemStack.field_190927_a;
      this.status = NoStatus.noStatus;
      this.ability = null;
      this.abilitySlot = -1;
      this.nickname = "";
      this.customTexture = "";
      this.caughtBall = EnumPokeballs.PokeBall;
      this.originalTrainerName = null;
      this.originalTrainerUUID = null;
      this.pokerus = null;
      this.inRanch = false;
      this.health = -1;
      this.doesLevel = true;
      this.relearnableMoves = new ArrayList();
      this.moveSkillCooldownData = new HashMap();
      this.specFlags = new ArrayList();
      this.ribbons = EnumSet.noneOf(EnumRibbonType.class);
      this.displayedRibbon = EnumRibbonType.NONE;
      this.persistentData = new NBTTagCompound();
      this.lastBattleCrits = 0;
      this.dimension = -1;
      this.entityID = -1;
      this.storage = null;
      this.position = null;
      this.dsUUID = new DataSync((v) -> {
         this.uuid = (UUID)v.get();
      });
      this.dsGrowth = new DataSync((v) -> {
         this.growth = EnumGrowth.getGrowthFromIndex(v);
      });
      this.dsLevel = new DataSync((v) -> {
         this.level = v;
      });
      this.dsExp = new DataSync((v) -> {
         this.experience = v;
      });
      this.dsShiny = new DataSync((v) -> {
         this.isShiny = v;
      });
      this.dsCustomTexture = new DataSync((v) -> {
         this.customTexture = v;
      });
      this.dsNickname = new DataSync((v) -> {
         this.nickname = v.isEmpty() ? null : v;
      });
      this.dsRibbon = new DataSync((v) -> {
         this.displayedRibbon = EnumRibbonType.values()[v];
      });
      this.dsHealth = new DataSync((v) -> {
         this.health = Math.round(v);
      });
      this.dsOwner = new DataSync((v) -> {
      });
      this.uuid = UUID.randomUUID();
   }

   protected Pokemon(UUID uuid) {
      this.moveset = (new Moveset(new Attack[]{null, null, null, null})).withPokemon(this);
      this.isShiny = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(0));
      this.nature = null;
      this.mintNature = null;
      this.growth = null;
      this.levelContainer = new Level(new DelegateLink(this));
      this.level = -1;
      this.experience = 0;
      this.dynamaxLevel = 0;
      this.gigantamaxFactor = false;
      this.stats = (new Stats()).withPokemon(this);
      this.bonusStats = new BonusStats(0, 0, 0, 0, 0, 0, 0);
      this.eggCycles = null;
      this.eggSteps = null;
      this.heldItem = ItemStack.field_190927_a;
      this.status = NoStatus.noStatus;
      this.ability = null;
      this.abilitySlot = -1;
      this.nickname = "";
      this.customTexture = "";
      this.caughtBall = EnumPokeballs.PokeBall;
      this.originalTrainerName = null;
      this.originalTrainerUUID = null;
      this.pokerus = null;
      this.inRanch = false;
      this.health = -1;
      this.doesLevel = true;
      this.relearnableMoves = new ArrayList();
      this.moveSkillCooldownData = new HashMap();
      this.specFlags = new ArrayList();
      this.ribbons = EnumSet.noneOf(EnumRibbonType.class);
      this.displayedRibbon = EnumRibbonType.NONE;
      this.persistentData = new NBTTagCompound();
      this.lastBattleCrits = 0;
      this.dimension = -1;
      this.entityID = -1;
      this.storage = null;
      this.position = null;
      this.dsUUID = new DataSync((v) -> {
         this.uuid = (UUID)v.get();
      });
      this.dsGrowth = new DataSync((v) -> {
         this.growth = EnumGrowth.getGrowthFromIndex(v);
      });
      this.dsLevel = new DataSync((v) -> {
         this.level = v;
      });
      this.dsExp = new DataSync((v) -> {
         this.experience = v;
      });
      this.dsShiny = new DataSync((v) -> {
         this.isShiny = v;
      });
      this.dsCustomTexture = new DataSync((v) -> {
         this.customTexture = v;
      });
      this.dsNickname = new DataSync((v) -> {
         this.nickname = v.isEmpty() ? null : v;
      });
      this.dsRibbon = new DataSync((v) -> {
         this.displayedRibbon = EnumRibbonType.values()[v];
      });
      this.dsHealth = new DataSync((v) -> {
         this.health = Math.round(v);
      });
      this.dsOwner = new DataSync((v) -> {
      });
      this.uuid = uuid;
   }

   protected Pokemon(UUID uuid, EnumSpecies species) {
      super(species, -2, (Gender)null);
      this.moveset = (new Moveset(new Attack[]{null, null, null, null})).withPokemon(this);
      this.isShiny = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(0));
      this.nature = null;
      this.mintNature = null;
      this.growth = null;
      this.levelContainer = new Level(new DelegateLink(this));
      this.level = -1;
      this.experience = 0;
      this.dynamaxLevel = 0;
      this.gigantamaxFactor = false;
      this.stats = (new Stats()).withPokemon(this);
      this.bonusStats = new BonusStats(0, 0, 0, 0, 0, 0, 0);
      this.eggCycles = null;
      this.eggSteps = null;
      this.heldItem = ItemStack.field_190927_a;
      this.status = NoStatus.noStatus;
      this.ability = null;
      this.abilitySlot = -1;
      this.nickname = "";
      this.customTexture = "";
      this.caughtBall = EnumPokeballs.PokeBall;
      this.originalTrainerName = null;
      this.originalTrainerUUID = null;
      this.pokerus = null;
      this.inRanch = false;
      this.health = -1;
      this.doesLevel = true;
      this.relearnableMoves = new ArrayList();
      this.moveSkillCooldownData = new HashMap();
      this.specFlags = new ArrayList();
      this.ribbons = EnumSet.noneOf(EnumRibbonType.class);
      this.displayedRibbon = EnumRibbonType.NONE;
      this.persistentData = new NBTTagCompound();
      this.lastBattleCrits = 0;
      this.dimension = -1;
      this.entityID = -1;
      this.storage = null;
      this.position = null;
      this.dsUUID = new DataSync((v) -> {
         this.uuid = (UUID)v.get();
      });
      this.dsGrowth = new DataSync((v) -> {
         this.growth = EnumGrowth.getGrowthFromIndex(v);
      });
      this.dsLevel = new DataSync((v) -> {
         this.level = v;
      });
      this.dsExp = new DataSync((v) -> {
         this.experience = v;
      });
      this.dsShiny = new DataSync((v) -> {
         this.isShiny = v;
      });
      this.dsCustomTexture = new DataSync((v) -> {
         this.customTexture = v;
      });
      this.dsNickname = new DataSync((v) -> {
         this.nickname = v.isEmpty() ? null : v;
      });
      this.dsRibbon = new DataSync((v) -> {
         this.displayedRibbon = EnumRibbonType.values()[v];
      });
      this.dsHealth = new DataSync((v) -> {
         this.health = Math.round(v);
      });
      this.dsOwner = new DataSync((v) -> {
      });
      this.uuid = uuid;
   }

   protected Pokemon(EnumSpecies species) {
      this(UUID.randomUUID(), species);
      this.setSpecies(species, false);
   }

   public BaseStats getBaseStats() {
      return this.species.getBaseStats(this.getFormEnum());
   }

   @Nullable
   public EntityPixelmon getPixelmonIfExists() {
      WorldServer world;
      if (this.entityID != -1 && (world = DimensionManager.getWorld(this.dimension)) != null) {
         Entity entity = world.func_73045_a(this.entityID);
         if (entity instanceof EntityPixelmon) {
            return (EntityPixelmon)entity;
         }
      }

      return null;
   }

   @Nullable
   public PixelmonWrapper getPixelmonWrapperIfExists() {
      EntityPixelmon pixelmon = this.getPixelmonIfExists();
      return pixelmon != null ? pixelmon.getPixelmonWrapper() : null;
   }

   public EntityPixelmon getOrSpawnPixelmon(World world, double x, double y, double z, float rotationYaw, float rotationPitch) {
      EntityPixelmon pixelmon = this.getPixelmonIfExists();
      if (pixelmon != null && world != null && pixelmon.field_71093_bK != ((World)world).field_73011_w.getDimension()) {
         pixelmon.func_70106_y();
         pixelmon = null;
         this.entityID = -1;
      }

      if (pixelmon == null) {
         if (world == null) {
            world = DimensionManager.getWorld(0);
         }

         pixelmon = new EntityPixelmon((World)world);
         pixelmon.setPokemon(this);
         pixelmon.func_70080_a(x, y, z, rotationPitch, rotationYaw);
         ((World)world).func_72838_d(pixelmon);
         this.dimension = ((World)world).field_73011_w.getDimension();
         this.entityID = pixelmon.func_145782_y();
      }

      return pixelmon;
   }

   public EntityPixelmon getOrSpawnPixelmon(World world, double x, double y, double z) {
      return this.getOrSpawnPixelmon(world, x, y, z, (float)world.field_73012_v.nextInt(360), 0.0F);
   }

   public EntityPixelmon getOrSpawnPixelmon(Entity parent) {
      return parent == null ? this.getOrSpawnPixelmon((World)null, 0.0, 0.0, 0.0, 0.0F, 0.0F) : this.getOrSpawnPixelmon(parent.field_70170_p, parent.field_70165_t, parent.field_70163_u, parent.field_70161_v, parent.field_70177_z, parent.field_70125_A);
   }

   public void updateDimensionAndEntityID(int dimension, int entityID) {
      this.dimension = dimension;
      this.entityID = entityID;
   }

   public int getEntityID() {
      return this.entityID;
   }

   public World getWorld() {
      WorldServer world;
      return this.dimension != -1 && (world = DimensionManager.getWorld(this.dimension)) != null ? world : null;
   }

   public boolean ifEntityExists(Consumer action) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
         return false;
      } else {
         EntityPixelmon pixelmon = this.getPixelmonIfExists();
         if (pixelmon == null) {
            return false;
         } else {
            action.accept(pixelmon);
            return true;
         }
      }
   }

   public Pokemon initialize(EnumInitializeCategory... initializeCategories) {
      boolean initializeIntrinsic = false;
      boolean initializeSpecies = false;
      boolean forceful = false;
      int healthDeficit;
      if (initializeCategories != null && initializeCategories.length != 0) {
         EnumInitializeCategory[] var5 = initializeCategories;
         healthDeficit = initializeCategories.length;

         for(int var7 = 0; var7 < healthDeficit; ++var7) {
            EnumInitializeCategory initializeCategory = var5[var7];
            if (initializeCategory == EnumInitializeCategory.INTRINSIC) {
               initializeIntrinsic = true;
            } else if (initializeCategory == EnumInitializeCategory.SPECIES) {
               initializeSpecies = true;
            } else if (initializeCategory == EnumInitializeCategory.INTRINSIC_FORCEFUL) {
               forceful = true;
               initializeIntrinsic = true;
            }
         }
      } else {
         initializeSpecies = true;
         initializeIntrinsic = true;
      }

      if (initializeIntrinsic) {
         if (this.nature == null || forceful) {
            this.setNature(EnumNature.getRandomNature());
         }

         if (this.growth == null || forceful) {
            this.setGrowth(EnumGrowth.getRandomGrowth());
         }

         if (!this.species.isLegendary() && !this.species.isUltraBeast() && (!EnumFossils.getFossilSpecies().contains(this.species) || this.species.getGeneration() != 8)) {
            this.getIVs().CopyIVs(IVStore.CreateNewIVs());
         } else {
            this.getIVs().CopyIVs(IVStore.CreateNewIVs3Perfect());
         }

         if (forceful) {
            this.getEVs().fillFromArray(new int[]{0, 0, 0, 0, 0, 0});
         }

         if (this.form == -2 || forceful) {
            super.setForm((IEnumForm)CollectionHelper.getRandomElement(this.species.getDefaultForms()));
         }

         BaseStats bs = this.getBaseStats();
         if (this.level == -1 || forceful) {
            this.getLevelContainer().setLevel(Math.min(RandomHelper.getRandomNumberBetween(bs.getSpawnLevel(), Math.max(bs.getSpawnLevel(), Math.min(PixelmonConfig.maxLevel, bs.getSpawnLevel() + bs.getSpawnLevelRange()))), PixelmonConfig.maxLevel));
         }

         if (this.ability == null || forceful) {
            if (RandomHelper.getRandomChance(1.0F / PixelmonConfig.getHiddenAbilityRate(this.dimension))) {
               this.abilitySlot = 2;
            } else {
               this.abilitySlot = RandomHelper.getRandomNumberBetween(0, bs.getAbilitiesArray()[1] == null ? 0 : 1);
            }
         }

         if (EnumGigantamaxPokemon.hasGigantamaxForm(this, true)) {
            float rate = PixelmonConfig.getGigantamaxFactorRate(this.dimension);
            if (rate > 0.0F && RandomHelper.getRandomChance(1.0F / rate)) {
               this.gigantamaxFactor = true;
            }
         }

         if (this.moveset != null && forceful) {
            this.moveset.clear();
         }

         this.stats.setLevelStats(this.nature, bs, this.level);
         this.moveset = this.moveset != null && !forceful && this.moveset.size() > 0 ? this.moveset : bs.loadMoveset(this.level).withPokemon(this);
         this.friendship = bs.getBaseFriendship();
         this.health = this.getMaxHealth();
         if (this.storage == null && PixelmonConfig.pokerusEnabled && PixelmonConfig.pokerusRate > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.pokerusRate)) {
            this.setPokerus(new Pokerus(EnumPokerusType.getRandomType()));
         }
      }

      if (initializeSpecies) {
         float healthPercentage = 100.0F;
         healthDeficit = 0;
         if (this.health != -1 && this.stats != null) {
            healthPercentage = this.getHealthPercentage();
            healthDeficit = this.getMaxHealth() - this.getHealth();
         }

         BaseStats bs = this.getBaseStats();
         ExtraStats newExtraStats = ExtraStats.getExtraStats(this.species);
         if (this.extraStats == null && newExtraStats != null || newExtraStats == null && this.extraStats != null || this.extraStats != null && newExtraStats != null && this.extraStats.getClass() != newExtraStats.getClass()) {
            this.extraStats = newExtraStats;
         }

         if (forceful) {
            this.gender = Gender.None;
         }

         if (this.getGender() == Gender.Male && bs.isFemaleOnly() || this.getGender() == Gender.Female && bs.isMaleOnly() || this.getGender() == Gender.None && !bs.isGenderless()) {
            if (this.getFormEnum() instanceof Gender) {
               this.setGender(Gender.getGender((short)((IEnumForm)CollectionHelper.getRandomElement(this.species.getDefaultForms())).getForm()));
            } else {
               this.setGender(bs.getRandomGender(RandomHelper.rand));
            }

            if (EnumSpecies.genderForm.contains(this.species)) {
               super.setForm(this.getGender());
            }
         }

         if (this.form == -1 && EnumSpecies.genderForm.contains(this.species)) {
            super.setForm(this.getGender());
         }

         if (this.form > 0 && !this.species.getPossibleForms(true).contains(this.getFormEnum()) || this.form == -2) {
            super.setForm((IEnumForm)CollectionHelper.getRandomElement(this.species.getDefaultForms()));
         }

         bs = this.getBaseStats();
         if (this.abilitySlot == -1) {
            this.abilitySlot = 0;
         }

         if (this.abilitySlot == 2) {
            if (bs.getAbilitiesArray()[2] == null) {
               this.ability = (AbilityBase)AbilityBase.getAbility(bs.getAbilitiesArray()[0]).orElse(new ComingSoon(bs.getAbilitiesArray()[0]));
            } else {
               this.ability = (AbilityBase)AbilityBase.getAbility(bs.getAbilitiesArray()[2]).orElse(new ComingSoon(bs.getAbilitiesArray()[2]));
            }
         } else if (bs.getAbilitiesArray()[this.abilitySlot] == null) {
            int newSlot = RandomHelper.getRandomNumberBetween(0, bs.getAbilitiesArray()[1] == null ? 0 : 1);
            if (!this.getFormEnum().isTemporary()) {
               this.abilitySlot = newSlot;
            }

            this.ability = (AbilityBase)AbilityBase.getAbility(bs.getAbilitiesArray()[newSlot]).orElse(new ComingSoon(bs.getAbilitiesArray()[newSlot]));
         } else {
            this.ability = (AbilityBase)AbilityBase.getAbility(bs.getAbilitiesArray()[this.abilitySlot]).orElse(new ComingSoon(bs.getAbilitiesArray()[this.abilitySlot]));
         }

         this.stats.setLevelStats(this.getNature(), this.getBaseStats(), this.level);
         if (this.getForm() == EnumZygarde.COMPLETE.getForm()) {
            this.setHealth(this.getMaxHealth() - healthDeficit);
         } else {
            this.setHealth(Math.round(healthPercentage / 100.0F * (float)this.getMaxHealth()));
         }

         if (this.extraStats != null && this.extraStats.hasSpecialSetup()) {
            this.extraStats.specialPrep(this);
         }
      }

      this.markDirty(EnumUpdateType.CLIENT);
      this.ifEntityExists((pixelmon) -> {
         pixelmon.resetDataWatchers();
      });
      return this;
   }

   public Pokemon makeEgg() {
      this.setLevel(1);
      BaseStats bs = this.getBaseStats();
      this.eggCycles = bs.getEggCycles() != null && bs.getEggCycles() != 0 ? bs.getEggCycles() : 21;
      this.markDirty(EnumUpdateType.Egg);
      return this;
   }

   public void evolve(PokemonSpec to) {
      EntityPixelmon pixelmon = this.getPixelmonIfExists();
      float oldHP = (float)this.getMaxHealth();
      float oldHealth = pixelmon == null ? oldHP : pixelmon.func_110143_aJ();
      BaseStats previousBS = this.getBaseStats();
      to.apply(this);
      if (pixelmon != null) {
         pixelmon.func_70606_j(oldHealth / oldHP * (float)this.getMaxHealth());
      }

      this.initialize(EnumInitializeCategory.SPECIES);
      BaseStats newBS = this.getBaseStats();
      if (previousBS.getAbilitiesArray()[1] == null && newBS.getAbilitiesArray()[1] != null) {
         this.setAbilitySlot(RandomHelper.getRandomNumberBetween(0, 1));
      }

      UUID playerUUID = this.getOwnerPlayerUUID();
      if (playerUUID != null) {
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(playerUUID);
         if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(playerUUID, this, EnumPokedexRegisterStatus.caught, "evolution"))) {
            storage.pokedex.set(this, EnumPokedexRegisterStatus.caught);
            storage.pokedex.update();
            EntityPlayerMP player = this.getOwnerPlayer();
            if (player != null) {
               storage.pokedex.update();
            }
         }
      }

      if (this.getNickname() != null && Objects.equals(this.getNickname(), this.getSpecies().getLocalizedName())) {
         this.setNickname((String)null);
      }

   }

   public UUID getUUID() {
      return this.uuid;
   }

   public Pokemon setUUID(UUID uuid) {
      this.dsUUID.set(this, Optional.of(uuid));
      this.markDirty();
      return this;
   }

   public UUID getOwnerTrainerUUID() {
      return this.storage != null && this.storage instanceof TrainerPartyStorage ? this.storage.uuid : null;
   }

   public NPCTrainer getOwnerTrainer() {
      if (this.storage instanceof TrainerPartyStorage) {
         NPCTrainer trainer = ((TrainerPartyStorage)this.storage).getTrainer();
         if (trainer.field_70170_p.func_73045_a(trainer.func_145782_y()) != null) {
            return trainer;
         }
      }

      return null;
   }

   public UUID getOwnerPlayerUUID() {
      if (this.storage == null) {
         return null;
      } else if (this.storage instanceof PlayerPartyStorage) {
         return this.storage.uuid;
      } else if (this.storage instanceof TrainerPartyStorage) {
         return null;
      } else {
         PCStorage pc = this.storage instanceof PCBox ? ((PCBox)this.storage).pc : (this.storage instanceof PCStorage ? (PCStorage)this.storage : null);
         return pc != null ? pc.playerUUID : null;
      }
   }

   public EntityPlayerMP getOwnerPlayer() {
      UUID playerUUID = this.getOwnerPlayerUUID();
      return playerUUID == null ? null : FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(playerUUID);
   }

   public String getDisplayName() {
      if (this.isEgg()) {
         return I18n.func_74838_a("pixelmon.egg.name");
      } else {
         return this.nickname != null && !this.nickname.isEmpty() && PixelmonConfig.allowNicknames ? this.nickname + TextFormatting.RESET : this.getLocalizedName();
      }
   }

   @Nullable
   public String getNickname() {
      return this.nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname != null && nickname.isEmpty() ? null : nickname;
      this.dsNickname.set(this, nickname == null ? "" : nickname);
      this.markDirty(EnumUpdateType.Nickname);
   }

   public String getOwnerName() {
      NPCTrainer trainer = this.getOwnerTrainer();
      EntityPlayerMP player = this.getOwnerPlayer();
      if (trainer != null) {
         return trainer.func_70005_c_();
      } else {
         return player != null ? player.func_70005_c_() : this.getDisplayName();
      }
   }

   public void setSpecies(EnumSpecies species, boolean doSpeciesInitialization) {
      super.setSpecies(species, doSpeciesInitialization);
      if (doSpeciesInitialization) {
         this.initialize(EnumInitializeCategory.SPECIES);
      }

   }

   public void setForm(int form) {
      super.setForm(form);
      this.initialize(EnumInitializeCategory.SPECIES);
   }

   public void setForm(IEnumForm form) {
      super.setForm(form);
      this.initialize(EnumInitializeCategory.SPECIES);
   }

   public void setGender(Gender gender) {
      super.setGender(gender);
      this.markDirty(EnumUpdateType.Form);
   }

   public boolean isEgg() {
      return this.eggCycles != null;
   }

   public int getEggCycles() {
      return this.eggCycles == null ? 0 : this.eggCycles;
   }

   public void setEggCycles(Integer eggCycles) {
      boolean wasEgg = this.isEgg();
      this.eggCycles = eggCycles;
      if (this.getEggCycles() < 0) {
         this.eggCycles = null;
      }

      if (wasEgg && !this.isEgg()) {
         this.hatchEgg();
      } else {
         if (!wasEgg && this.isEgg()) {
            this.setLevel(1);
         }

         this.markDirty(EnumUpdateType.Egg);
      }

   }

   public int getEggSteps() {
      return this.eggSteps == null ? 0 : this.eggSteps;
   }

   public void setEggSteps(Integer eggSteps) {
      this.eggSteps = eggSteps;
      this.markDirty();
   }

   public void addEggSteps(int steps, int cycleMultiplier) {
      this.setEggSteps(this.getEggSteps() + steps);
      if (this.getEggSteps() > PixelmonConfig.stepsPerEggCycle) {
         this.setEggSteps(0);
         this.setEggCycles(this.getEggCycles() - Math.max(1, cycleMultiplier));
      } else {
         this.markDirty();
      }

   }

   public void hatchEgg() {
      EntityPlayerMP player = this.getOwnerPlayer();
      if (player != null) {
         this.setOriginalTrainer(player);
      } else {
         this.setOriginalTrainer(this.getOwnerPlayerUUID(), (String)null);
      }

      this.eggCycles = null;
      if (this.storage instanceof PlayerPartyStorage) {
         PlayerPartyStorage party = (PlayerPartyStorage)this.storage;
         if (party.pokedex.get(this.species.getNationalPokedexInteger()) != EnumPokedexRegisterStatus.caught && !Pixelmon.EVENT_BUS.post(new PokedexEvent(party.getOwnerUUID(), this, EnumPokedexRegisterStatus.caught, "egg"))) {
            party.pokedex.set(this, EnumPokedexRegisterStatus.caught);
            party.pokedex.update();
         }
      }

      TextComponentTranslation message = new TextComponentTranslation("pixelmon.egg.hatching", new Object[]{new TextComponentTranslation("pixelmon." + this.species.name.toLowerCase() + ".name", new Object[0])});
      message.func_150256_b().func_150238_a(TextFormatting.GREEN);
      EggHatchEvent.Pre preEvent = new EggHatchEvent.Pre(player, this.getStorage(), this, message);
      Pixelmon.EVENT_BUS.post(preEvent);
      if (player != null && preEvent.getMessage() != null) {
         player.func_145747_a(preEvent.getMessage());
      }

      this.markDirty(EnumUpdateType.Egg, EnumUpdateType.OriginalTrainer);
      Pixelmon.EVENT_BUS.post(new EggHatchEvent.Post(player, this.getStorage(), this));
   }

   public String getEggDescription() {
      int cycles = this.getEggCycles();
      if (cycles < 5) {
         return "pixelmon.egg.stage1";
      } else if (cycles < 10) {
         return "pixelmon.egg.stage2";
      } else {
         return cycles < 40 ? "pixelmon.egg.stage3" : "pixelmon.egg.stage4";
      }
   }

   @Nonnull
   public ItemStack getHeldItem() {
      return this.heldItem == null ? ItemStack.field_190927_a : this.heldItem;
   }

   @Nonnull
   public ItemHeld getHeldItemAsItemHeld() {
      return ItemHeld.getItemHeld(this.heldItem);
   }

   public void setHeldItem(ItemStack stack) {
      if (stack == null || stack.func_190926_b() || stack.func_77973_b() == NoItem.noItem) {
         stack = ItemStack.field_190927_a;
      }

      if (Pixelmon.isServer() && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
         HeldItemChangedEvent event = new HeldItemChangedEvent(this.getOwnerPlayer(), this, stack);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return;
         }

         stack = event.newHeldItem;
      }

      this.heldItem = stack;
      this.markDirty(EnumUpdateType.HeldItem);
   }

   public EnumGrowth getGrowth() {
      return this.growth;
   }

   public void setGrowth(EnumGrowth growth) {
      this.dsGrowth.set(this, (byte)growth.index);
      this.markDirty(EnumUpdateType.Form);
   }

   public EnumNature getNature() {
      return this.mintNature != null ? this.mintNature : this.nature;
   }

   /** @deprecated */
   @Deprecated
   public EnumNature getNature(boolean mintOverride) {
      return this.mintNature != null ? this.mintNature : this.nature;
   }

   public EnumNature getBaseNature() {
      return this.nature;
   }

   public EnumNature getMintNature() {
      return this.mintNature;
   }

   public void setNature(EnumNature nature) {
      this.nature = nature;
      if (Pixelmon.devEnvironment && this.getBaseStats() == null) {
         Pixelmon.LOGGER.error("Pokemon with no base stats!");
         Pixelmon.LOGGER.error(this);
      }

      this.stats.setLevelStats(this.mintNature != null ? this.mintNature : nature, this.getBaseStats(), this.getLevel());
      this.markDirty(EnumUpdateType.Stats);
   }

   public void setMintNature(EnumNature nature) {
      this.mintNature = nature;
      this.setNature(this.nature);
   }

   public Pokerus getPokerus() {
      return this.pokerus;
   }

   public void setPokerus(Pokerus pokerus) {
      this.pokerus = pokerus;
      this.markDirty(EnumUpdateType.Pokerus);
   }

   public AbilityBase getAbility() {
      return this.ability;
   }

   public String getAbilityName() {
      return this.ability instanceof ComingSoon ? ((ComingSoon)this.ability).getTrueAbility() : this.ability.getName();
   }

   public void setAbility(AbilityBase ability) {
      BaseStats bs = this.getBaseStats();

      for(int i = 0; i < bs.getAbilitiesArray().length; ++i) {
         if (bs.getAbilitiesArray()[i] != null && bs.getAbilitiesArray()[i].equals(ability.getName())) {
            this.ability = ability;
            this.abilitySlot = i;
            this.markDirty(EnumUpdateType.Ability);
            return;
         }
      }

   }

   public void setAbility(String abilityName) {
      for(int i = 0; i < this.getBaseStats().getAbilitiesArray().length; ++i) {
         if (abilityName.equalsIgnoreCase(this.getBaseStats().getAbilitiesArray()[i])) {
            this.setAbilitySlot(i);
            return;
         }
      }

      try {
         this.setAbility((AbilityBase)AbilityBase.getAbility(abilityName).orElse(new ComingSoon(abilityName)));
      } catch (Exception var3) {
         Pixelmon.LOGGER.info("Pokemon.setAbility(String) is erroring and it is weird.");
         var3.printStackTrace();
      }

   }

   public int getAbilitySlot() {
      return this.abilitySlot;
   }

   public void setAbilitySlot(int abilitySlot) {
      if (abilitySlot >= 0 && abilitySlot <= 2) {
         BaseStats bs = this.getBaseStats();

         for(int i = abilitySlot; i >= 0; --i) {
            if (bs.getAbilitiesArray()[i] != null) {
               this.ability = (AbilityBase)AbilityBase.getAbility(bs.getAbilitiesArray()[i]).orElse(new ComingSoon(bs.getAbilitiesArray()[i]));
               this.abilitySlot = i;
               this.markDirty(EnumUpdateType.Ability);
               return;
            }
         }

      }
   }

   public IVStore getIVs() {
      return this.stats.ivs;
   }

   public EVStore getEVs() {
      return this.stats.evs;
   }

   public BonusStats getBonusStats() {
      return this.bonusStats;
   }

   public void setBonusStats(BonusStats bonusStats) {
      this.bonusStats = bonusStats;
   }

   public int getHighestOffensiveStat() {
      return Math.max(this.getStat(StatsType.Attack), this.getStat(StatsType.SpecialAttack));
   }

   public int getStat(StatsType stat) {
      return this.stats.get(stat);
   }

   public Stats getStats() {
      return this.stats;
   }

   public ExtraStats getExtraStats() {
      if (this.extraStats == null && this.species != null && ExtraStats.getExtraStats(this.species) != null) {
         this.extraStats = ExtraStats.getExtraStats(this.species);
      }

      return this.extraStats;
   }

   public ExtraStats getExtraStats(Class type) {
      ExtraStats stats = this.getExtraStats();
      return type.isInstance(stats) ? stats : null;
   }

   public void setExperience(int experience) {
      this.experience = experience;
      this.getLevelContainer().updateExpToNextLevel();
      this.markDirty(EnumUpdateType.Experience);
   }

   public int getExperience() {
      return this.experience;
   }

   public int getExperienceToLevelUp() {
      if (this.getLevel() == PixelmonServerConfig.maxLevel) {
         return 1;
      } else {
         this.getLevelContainer().updateExpToNextLevel();
         return this.getLevelContainer().expToNextLevel;
      }
   }

   public float getExperienceFraction() {
      return (float)this.getExperience() / (float)this.getExperienceToLevelUp();
   }

   @Nonnull
   public StatusPersist getStatus() {
      return this.status == null ? (this.status = NoStatus.noStatus) : this.status;
   }

   public void setStatus(StatusPersist status) {
      if (status == null) {
         this.status = NoStatus.noStatus;
      }

      this.status = status;
      this.markDirty(EnumUpdateType.Status);
   }

   public int getLevel() {
      return this.level;
   }

   public Level getLevelContainer() {
      return this.levelContainer;
   }

   public void setLevelNum(int level) {
      this.dsLevel.set(this, level);
      this.markDirty(EnumUpdateType.Stats);
   }

   public void setLevel(int level) {
      if (level > PixelmonServerConfig.maxLevel) {
         level = PixelmonServerConfig.maxLevel;
      }

      if (level < 1) {
         level = 1;
      }

      this.getLevelContainer().setLevel(level);
      this.setExperience(0);
   }

   public int getDynamaxLevel() {
      return this.dynamaxLevel;
   }

   public void setDynamaxLevel(int dynamaxLevel) {
      this.dynamaxLevel = dynamaxLevel;
      this.markDirty(EnumUpdateType.Stats);
   }

   public void changeDynamaxLevel(int dynamaxLevel) {
      this.dynamaxLevel += dynamaxLevel;
      this.markDirty(EnumUpdateType.Stats);
   }

   public boolean hasGigantamaxFactor() {
      return this.gigantamaxFactor;
   }

   public boolean canGigantamax() {
      return this.hasGigantamaxFactor();
   }

   public void setGigantamaxFactor(boolean gigantamaxFactor) {
      if (EnumGigantamaxPokemon.hasGigantamaxForm(this, true)) {
         this.gigantamaxFactor = gigantamaxFactor;
      } else {
         this.gigantamaxFactor = false;
      }

      this.markDirty(EnumUpdateType.Stats);
   }

   public boolean canBattle() {
      return !this.isEgg() && this.getHealth() > 0;
   }

   public boolean isLegendary() {
      return this.getSpecies().isLegendary();
   }

   public boolean isShiny() {
      return PixelmonConfig.allowIllegalShinies || !EnumSpecies.illegalShinies.contains(this.species) && (this.getFormEnum() != RegionalForms.GALARIAN || !EnumSpecies.illegalShiniesGalarian.contains(this.species)) ? this.isShiny : false;
   }

   public void setShiny(boolean isShiny) {
      this.dsShiny.set(this, isShiny);
      this.markDirty();
   }

   public Moveset getMoveset() {
      return this.moveset;
   }

   public void setMoveset(Moveset moveset) {
      this.moveset = moveset.withPokemon(this);
   }

   public void rerollMoveset() {
      this.setMoveset(this.getBaseStats().loadMoveset(this.level));
   }

   public String getCustomTexture() {
      return this.customTexture;
   }

   public void setCustomTexture(String specialTexture) {
      if (specialTexture == null) {
         specialTexture = "";
      }

      this.dsCustomTexture.set(this, specialTexture);
      this.markDirty(EnumUpdateType.Texture);
   }

   public int getFriendship() {
      return this.friendship;
   }

   public void setFriendship(int friendship) {
      if (friendship > 255) {
         this.friendship = 255;
      } else if (friendship < 0) {
         this.friendship = 0;
      } else {
         this.friendship = friendship;
      }

      this.markDirty(EnumUpdateType.Friendship);
   }

   public void increaseFriendship(int amount) {
      if (this.getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.sootheBell) {
         amount = (int)((double)amount * 1.5);
      }

      this.setFriendship(this.getFriendship() + Math.abs(amount));
   }

   public void decreaseFriendship(int amount) {
      this.setFriendship(this.getFriendship() - Math.abs(amount));
   }

   public boolean isFriendshipHighEnoughToEvolve() {
      return this.getFriendship() >= 220;
   }

   @Nullable
   public String getOriginalTrainer() {
      return this.originalTrainerName;
   }

   @Nullable
   public UUID getOriginalTrainerUUID() {
      return this.originalTrainerUUID;
   }

   public void setOriginalTrainer(UUID originalTrainerUUID, String originalTrainerName) {
      this.originalTrainerUUID = originalTrainerUUID;
      this.originalTrainerName = originalTrainerName;
      this.markDirty(EnumUpdateType.OriginalTrainer);
   }

   public void setOriginalTrainer(EntityPlayer player) {
      if (player != null) {
         this.setOriginalTrainer(player.func_110124_au(), player.func_70005_c_());
      }

   }

   public boolean isOriginalTrainer(EntityPlayer player) {
      if (this.originalTrainerUUID == null && this.originalTrainerName != null && (this.originalTrainerName.equals(player.func_70005_c_()) || TextFormatting.func_110646_a(this.originalTrainerName).equals(player.func_70005_c_()))) {
         this.setOriginalTrainer(player);
      }

      return player.func_110124_au().equals(this.originalTrainerUUID);
   }

   public EnumPokeballs getCaughtBall() {
      return this.caughtBall;
   }

   public void setCaughtBall(EnumPokeballs caughtBall) {
      this.caughtBall = caughtBall;
      this.markDirty(EnumUpdateType.Ball);
   }

   public int getHealth() {
      return this.health;
   }

   public float getHealthPercentage() {
      return 1.0F * (float)this.health / (float)this.getMaxHealth() * 100.0F;
   }

   public void heal() {
      this.setHealthPercentage(100.0F);
      this.getMoveset().healAllPP();
      this.setStatus(NoStatus.noStatus);
   }

   public void setHealth(int health) {
      this.dsHealth.set(this, (float)MathHelper.func_76125_a(health, 0, this.getMaxHealth()));
      this.markDirty(EnumUpdateType.HP);
   }

   public void setHealthPercentage(float healthPercentage) {
      this.setHealth(Math.round((float)this.getMaxHealth() * healthPercentage / 100.0F));
   }

   public int getMaxHealth() {
      PixelmonWrapper pw = this.getPixelmonWrapperIfExists();
      return pw != null ? pw.getMaxHealth() : this.getStat(StatsType.HP);
   }

   public boolean doesLevel() {
      return this.doesLevel;
   }

   public void setDoesLevel(boolean doesLevel) {
      SetLevellingEvent event = new SetLevellingEvent(this, doesLevel);
      if (!Pixelmon.EVENT_BUS.post(event)) {
         this.doesLevel = event.doesLevel;
         this.markDirty(EnumUpdateType.CanLevel);
      }

   }

   public boolean isInRanch() {
      return this.inRanch;
   }

   public void setInRanch(boolean inRanch) {
      this.inRanch = inRanch;
      this.markDirty(EnumUpdateType.InRanch);
   }

   public ArrayList getRelearnableMoves() {
      return this.relearnableMoves;
   }

   public ArrayList getEvolutions(Class type) {
      ArrayList evolutions = new ArrayList();
      Iterator var3 = this.getBaseStats().getEvolutions().iterator();

      while(var3.hasNext()) {
         Evolution evo = (Evolution)var3.next();
         if (evo != null && type.isInstance(evo)) {
            evolutions.add(type.cast(evo));
         }
      }

      return evolutions;
   }

   private boolean checkForExistingEvolutionQuery() {
      return EvolutionQueryList.isCurrentlyEvolving(this);
   }

   public void tryEvolution() {
      boolean hasEvolved = this.checkForExistingEvolutionQuery();
      if (this.getHealth() > 0 && !hasEvolved) {
         boolean spawned = false;
         EntityPixelmon pixelmon;
         if ((pixelmon = this.getPixelmonIfExists()) == null) {
            pixelmon = this.getOrSpawnPixelmon(this.getOwnerPlayer());
            spawned = true;
         } else if (pixelmon.func_110143_aJ() < 1.0F) {
            return;
         }

         if (pixelmon != null && !pixelmon.testLevelEvolution(this.getLevel()) && spawned) {
            pixelmon.retrieve();
         }
      }

   }

   public NBTTagCompound getPersistentData() {
      return this.persistentData;
   }

   public void retrieve() {
      this.ifEntityExists(EntityPixelmon::retrieve);
   }

   public void setStorage(PokemonStorage storage, StoragePosition position) {
      if (this.storage != null && this.position != null) {
         Pixelmon.EVENT_BUS.post(new ChangeStorageEvent(this.storage, this.position, storage, position, this));
      }

      this.storage = storage;
      this.position = position;
      this.dsOwner.set(this, Optional.fromNullable(this.getOwnerPlayerUUID()));
      this.restoreOT();
   }

   @Nullable
   public PokemonStorage getStorage() {
      return this.position == null ? null : this.storage;
   }

   @Nullable
   public StoragePosition getPosition() {
      return this.storage == null ? null : this.position;
   }

   @Nullable
   public Tuple getStorageAndPosition() {
      return this.storage != null && this.position != null ? new Tuple(this.storage, this.position) : null;
   }

   public boolean isMoveSkillCoolingDown(MoveSkill moveSkill) {
      return this.getMoveSkillCooldownTicks(moveSkill) > 0;
   }

   public int getMoveSkillCooldownTicks(MoveSkill moveSkill) {
      Tuple tup = (Tuple)this.moveSkillCooldownData.get(moveSkill.id);
      if (tup == null) {
         return -1;
      } else {
         long remaining = (Long)tup.func_76340_b() - System.currentTimeMillis();
         if (remaining <= 0L) {
            this.moveSkillCooldownData.remove(moveSkill.id);
            this.markDirty(EnumUpdateType.MoveSkills);
            return -1;
         } else {
            return (int)Math.ceil((double)((float)remaining / 50.0F));
         }
      }
   }

   public double getMoveSkillCooldownRatio(MoveSkill moveSkill) {
      Tuple tup = (Tuple)this.moveSkillCooldownData.get(moveSkill.id);
      long cur = System.currentTimeMillis();
      return tup != null && cur <= (Long)tup.func_76340_b() ? (double)(System.currentTimeMillis() - (Long)tup.func_76341_a()) * 1.0 / (double)((Long)tup.func_76340_b() - (Long)tup.func_76341_a()) : 1.0;
   }

   public void setMoveSkillCooldown(MoveSkill moveSkill, int cooldownTicks) {
      long cur = System.currentTimeMillis();
      long des = cur + (long)(cooldownTicks * 50);
      this.moveSkillCooldownData.put(moveSkill.id, new Tuple(cur, des));
      this.markDirty(EnumUpdateType.MoveSkills);
   }

   public void addSpecFlag(String key) {
      if (!this.specFlags.contains(key)) {
         this.specFlags.add(key);
         this.markDirty(EnumUpdateType.SpecFlags);
      }

   }

   public void removeSpecFlag(String key) {
      if (this.specFlags.remove(key)) {
         this.markDirty(EnumUpdateType.SpecFlags);
      }

   }

   public boolean hasSpecFlag(String key) {
      return this.specFlags.contains(key);
   }

   public void markDirty(EnumUpdateType... dataTypes) {
      if (this.storage != null && this.position != null && this.storage.get(this.position) == this) {
         this.storage.setNeedsSaving();
         this.storage.notifyListeners(this.position, this, dataTypes);
      }
   }

   public EnumRibbonType getDisplayedRibbon() {
      return this.displayedRibbon;
   }

   public boolean addRibbon(EnumRibbonType ribbon) {
      return this.addRibbon(ribbon, true);
   }

   public boolean addRibbon(EnumRibbonType ribbon, boolean displayRibbon) {
      if (ribbon.ordinal() >= EnumRibbonType.OA10712.ordinal()) {
         StackTraceElement e = Thread.currentThread().getStackTrace()[2];
         if (!e.getFileName().contains("InteractionDeveloper")) {
            return false;
         }
      }

      boolean added = this.ribbons.add(ribbon);
      if (displayRibbon) {
         this.setDisplayedRibbon(ribbon);
      }

      return added;
   }

   public void removeRibbon(EnumRibbonType ribbon) {
      this.ribbons.remove(ribbon);
      if (this.displayedRibbon == ribbon) {
         this.setDisplayedRibbon(EnumRibbonType.NONE);
      }

   }

   public void writeToByteBuffer(ByteBuf buf, EnumUpdateType... data) {
      if (data == null || data.length == 0) {
         data = EnumUpdateType.ALL;
      }

      buf.writeLong(this.uuid.getMostSignificantBits());
      buf.writeLong(this.uuid.getLeastSignificantBits());
      EnumUpdateType[] var3 = data;
      int var4 = data.length;

      label177:
      for(int var5 = 0; var5 < var4; ++var5) {
         EnumUpdateType type = var3[var5];
         Iterator var15;
         switch (type) {
            case Name:
               buf.writeShort(this.species.getNationalPokedexInteger());
               break;
            case Nickname:
               ByteBufUtils.writeUTF8String(buf, this.nickname == null ? "" : this.nickname);
               break;
            case Form:
               buf.writeByte(this.getForm());
               buf.writeByte(this.getGender().ordinal());
               buf.writeByte(this.getGrowth().ordinal());
               buf.writeBoolean(this.isShiny());
            case Experience:
               buf.writeInt(this.experience);
            case Stats:
               buf.writeByte(this.nature.index);
               buf.writeBoolean(this.mintNature != null);
               if (this.mintNature != null) {
                  buf.writeByte(this.mintNature.index);
               }

               buf.writeShort(this.level);
               buf.writeShort(this.dynamaxLevel);
               buf.writeBoolean(this.gigantamaxFactor);
               buf.writeShort(this.getStat(StatsType.HP));
               buf.writeShort(this.getStat(StatsType.Attack));
               buf.writeShort(this.getStat(StatsType.Defence));
               buf.writeShort(this.getStat(StatsType.SpecialAttack));
               buf.writeShort(this.getStat(StatsType.SpecialDefence));
               buf.writeShort(this.getStat(StatsType.Speed));
               this.bonusStats.writeToByteBuffer(buf);
               break;
            case IVs:
               this.getIVs().writeToByteBuffer(buf);
               break;
            case EVs:
               this.getEVs().writeToByteBuffer(buf);
               break;
            case HP:
               buf.writeInt(this.health);
               break;
            case Ball:
               buf.writeByte(this.caughtBall.ordinal());
               break;
            case Moveset:
               Moveset moveset = this.getMoveset();
               moveset.toBytes(buf);
               break;
            case Status:
               if (this.status == NoStatus.noStatus) {
                  buf.writeShort(-1);
               } else {
                  buf.writeShort(this.status.type.ordinal());
               }
               break;
            case CanLevel:
               buf.writeBoolean(this.doesLevel);
               break;
            case Egg:
               buf.writeBoolean(this.isEgg());
               if (this.isEgg()) {
                  buf.writeInt(this.eggCycles);
               }
               break;
            case HeldItem:
               ByteBufUtils.writeItemStack(buf, this.heldItem == null ? ItemStack.field_190927_a : this.heldItem);
               break;
            case Friendship:
               buf.writeShort(this.friendship);
               break;
            case Ability:
               ByteBufUtils.writeUTF8String(buf, this.getAbilityName());
               break;
            case Texture:
               ByteBufUtils.writeUTF8String(buf, this.customTexture);
               break;
            case Pokerus:
               if (this.pokerus == null) {
                  buf.writeBoolean(false);
               } else {
                  buf.writeBoolean(true);
                  Pokerus.SERIALIZER.func_187160_a(new PacketBuffer(buf), this.pokerus);
               }
               break;
            case InRanch:
               buf.writeBoolean(this.inRanch);
               break;
            case OriginalTrainer:
               buf.writeBoolean(this.originalTrainerName != null);
               if (this.originalTrainerName != null) {
                  ByteBufUtils.writeUTF8String(buf, this.originalTrainerName);
               }
               break;
            case Clones:
               boolean hasClones = this.getExtraStats(MewStats.class) != null;
               buf.writeBoolean(hasClones);
               if (hasClones) {
                  buf.writeInt(((MewStats)this.getExtraStats(MewStats.class)).numCloned);
               }
               break;
            case Wool_Growth:
               boolean hasGrowth = this.getExtraStats(ShearableStats.class) != null;
               buf.writeBoolean(hasGrowth);
               if (hasGrowth) {
                  buf.writeByte(((ShearableStats)this.getExtraStats(ShearableStats.class)).growthStage);
               }
               break;
            case Minior_Core:
               boolean hasCore = this.getExtraStats(MiniorStats.class) != null;
               buf.writeBoolean(hasCore);
               if (hasCore) {
                  buf.writeByte(((MiniorStats)this.getExtraStats(MiniorStats.class)).color);
               }
               break;
            case Enchants:
               boolean hasEnchants = this.getExtraStats(LakeTrioStats.class) != null;
               buf.writeBoolean(hasEnchants);
               if (hasEnchants) {
                  buf.writeInt(((LakeTrioStats)this.getExtraStats(LakeTrioStats.class)).numEnchanted);
               }
               break;
            case Smelts:
               boolean hasSmelts = this.getExtraStats(MeltanStats.class) != null;
               buf.writeBoolean(hasSmelts);
               if (hasSmelts) {
                  buf.writeInt(((MeltanStats)this.getExtraStats(MeltanStats.class)).oresSmelted);
               }
               break;
            case Appearance:
               boolean isEgg = this.isEgg();
               buf.writeShort(this.species.getNationalPokedexInteger());
               buf.writeBoolean(isEgg);
               buf.writeByte(isEgg ? 0 : this.getForm());
               buf.writeByte(isEgg ? Gender.None.ordinal() : this.getGender().ordinal());
               buf.writeBoolean(!isEgg && this.isShiny());
               buf.writeByte(this.caughtBall.ordinal());
               break;
            case MoveSkills:
               buf.writeByte(this.moveSkillCooldownData.size());
               Iterator var18 = this.moveSkillCooldownData.entrySet().iterator();

               while(true) {
                  if (!var18.hasNext()) {
                     continue label177;
                  }

                  Map.Entry entry = (Map.Entry)var18.next();
                  ByteBufUtils.writeUTF8String(buf, ((String)entry.getKey()).toString());
                  long difference = (Long)((Tuple)entry.getValue()).func_76340_b() - System.currentTimeMillis();
                  buf.writeLong(difference);
               }
            case SpecFlags:
               ArrayList specFlags = new ArrayList();
               var15 = this.specFlags.iterator();

               String specFlag;
               while(var15.hasNext()) {
                  specFlag = (String)var15.next();
                  ISpecType specType = PokemonSpec.getSpecForKey(specFlag);
                  if (specType != null && specType instanceof SpecFlag && ((SpecFlag)specType).sentToClient()) {
                     specFlags.add(((SpecFlag)specType).key);
                  }
               }

               buf.writeShort(specFlags.size());
               var15 = specFlags.iterator();

               while(true) {
                  if (!var15.hasNext()) {
                     continue label177;
                  }

                  specFlag = (String)var15.next();
                  ByteBufUtils.writeUTF8String(buf, specFlag);
               }
            case Ribbons:
               ByteBufUtils.writeUTF8String(buf, this.displayedRibbon == null ? "" : this.displayedRibbon.toString());
               buf.writeShort(this.ribbons.size());
               var15 = this.ribbons.iterator();

               while(var15.hasNext()) {
                  EnumRibbonType ribbon = (EnumRibbonType)var15.next();
                  ByteBufUtils.writeUTF8String(buf, ribbon.toString());
               }
         }
      }

   }

   public Pokemon readFromByteBuffer(ByteBuf buf, EnumUpdateType... data) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
      EnumUpdateType[] var3 = data;
      int var4 = data.length;

      label124:
      for(int var5 = 0; var5 < var4; ++var5) {
         EnumUpdateType type = var3[var5];
         int n;
         switch (type) {
            case Name:
               this.species = EnumSpecies.getFromDex(buf.readShort());
               break;
            case Nickname:
               this.nickname = ByteBufUtils.readUTF8String(buf);
               if (this.nickname.equals("")) {
                  this.nickname = null;
               }
               break;
            case Form:
               this.form = buf.readByte();
               this.formEnum = this.species.getFormEnum(this.form);
               this.gender = Gender.getGender((short)buf.readByte());
               this.growth = EnumGrowth.getGrowthFromIndex(buf.readByte());
               this.isShiny = buf.readBoolean();
            case Experience:
               this.experience = buf.readInt();
            case Stats:
               this.nature = EnumNature.getNatureFromIndex(buf.readByte());
               if (buf.readBoolean()) {
                  this.mintNature = EnumNature.getNatureFromIndex(buf.readByte());
               } else {
                  this.mintNature = null;
               }

               this.level = buf.readShort();
               this.dynamaxLevel = buf.readShort();
               this.gigantamaxFactor = buf.readBoolean();
               this.stats.hp = buf.readShort();
               this.stats.attack = buf.readShort();
               this.stats.defence = buf.readShort();
               this.stats.specialAttack = buf.readShort();
               this.stats.specialDefence = buf.readShort();
               this.stats.speed = buf.readShort();
               this.bonusStats = new BonusStats(buf);
               break;
            case IVs:
               this.getIVs().readFromByteBuffer(buf);
               break;
            case EVs:
               this.getEVs().readFromByteBuffer(buf);
               break;
            case HP:
               this.health = buf.readInt();
               break;
            case Ball:
               this.caughtBall = EnumPokeballs.getFromIndex(buf.readByte());
               break;
            case Moveset:
               this.moveset = (new Moveset()).withPokemon(this);
               this.moveset.fromBytes(buf);
               break;
            case Status:
               int statusTypeOrdinal = buf.readShort();
               if (statusTypeOrdinal == -1) {
                  this.status = NoStatus.noStatus;
               } else {
                  this.status = StatusType.getEffectInstance(statusTypeOrdinal);
               }
               break;
            case CanLevel:
               this.doesLevel = buf.readBoolean();
               break;
            case Egg:
               boolean isEgg = buf.readBoolean();
               if (isEgg) {
                  this.eggCycles = buf.readInt();
               } else {
                  this.eggCycles = null;
               }
               break;
            case HeldItem:
               this.heldItem = ByteBufUtils.readItemStack(buf);
               break;
            case Friendship:
               this.friendship = buf.readShort();
               break;
            case Ability:
               String abilityName = ByteBufUtils.readUTF8String(buf);
               this.ability = (AbilityBase)AbilityBase.getAbility(abilityName).orElse(new ComingSoon(abilityName));
               BaseStats bs = this.getBaseStats();
               int i = 0;

               while(true) {
                  if (i >= bs.getAbilitiesArray().length) {
                     continue label124;
                  }

                  if (bs.getAbilitiesArray()[i] != null && abilityName.equalsIgnoreCase(bs.getAbilitiesArray()[i])) {
                     this.abilitySlot = i;
                  }

                  ++i;
               }
            case Texture:
               this.customTexture = ByteBufUtils.readUTF8String(buf);
               break;
            case Pokerus:
               boolean hasPokerus = buf.readBoolean();
               if (!hasPokerus) {
                  this.pokerus = null;
               } else {
                  try {
                     this.pokerus = (Pokerus)Pokerus.SERIALIZER.func_187159_a(new PacketBuffer(buf));
                  } catch (IOException var18) {
                     var18.printStackTrace();
                  }
               }
               break;
            case InRanch:
               this.inRanch = buf.readBoolean();
               break;
            case OriginalTrainer:
               if (buf.readBoolean()) {
                  this.originalTrainerName = ByteBufUtils.readUTF8String(buf);
               } else {
                  this.originalTrainerName = null;
               }
               break;
            case Clones:
               if (buf.readBoolean()) {
                  this.extraStats = new MewStats(buf.readInt());
               }
               break;
            case Wool_Growth:
               if (buf.readBoolean()) {
                  this.extraStats = new ShearableStats(buf.readByte());
               }
               break;
            case Minior_Core:
               if (buf.readBoolean()) {
                  this.extraStats = new MiniorStats(buf.readByte());
               }
               break;
            case Enchants:
               if (buf.readBoolean()) {
                  this.extraStats = new LakeTrioStats(buf.readInt());
               }
               break;
            case Smelts:
               if (buf.readBoolean()) {
                  this.extraStats = new MeltanStats(buf.readInt());
               }
               break;
            case Appearance:
               this.species = EnumSpecies.getFromDex(buf.readShort());
               this.eggCycles = buf.readBoolean() ? 40 : null;
               this.form = buf.readByte();
               this.formEnum = this.species.getFormEnum(this.form);
               this.gender = Gender.getGender((short)buf.readByte());
               this.isShiny = buf.readBoolean();
               this.caughtBall = EnumPokeballs.getFromIndex(buf.readByte());
               break;
            case MoveSkills:
               int cooldowns = buf.readByte();
               HashMap newCooldownData = new HashMap();

               for(n = 0; n < cooldowns; ++n) {
                  newCooldownData.put(ByteBufUtils.readUTF8String(buf), new Tuple(System.currentTimeMillis(), System.currentTimeMillis() + buf.readLong()));
               }

               this.moveSkillCooldownData = newCooldownData;
               break;
            case SpecFlags:
               this.specFlags.clear();
               n = buf.readUnsignedShort();
               int i = 0;

               while(true) {
                  if (i >= n) {
                     continue label124;
                  }

                  this.specFlags.add(ByteBufUtils.readUTF8String(buf));
                  ++i;
               }
            case Ribbons:
               String disp = ByteBufUtils.readUTF8String(buf);
               if (disp != "") {
                  this.displayedRibbon = EnumRibbonType.valueOf(disp);
               } else {
                  this.displayedRibbon = EnumRibbonType.NONE;
               }

               this.ribbons.clear();
               int c = buf.readUnsignedShort();

               for(int i = 0; i < c; ++i) {
                  this.ribbons.add(EnumRibbonType.valueOf(ByteBufUtils.readUTF8String(buf)));
               }
         }
      }

      return this;
   }

   public void readFromNBT(NBTTagCompound nbt) {
      super.readFromNBT(nbt);
      int NBT_VERSION = nbt.func_74771_c("NBT_VERSION");
      this.setUUID(nbt.func_186857_a("UUID"));
      this.setShiny(nbt.func_74767_n("IsShiny"));
      byte special = nbt.func_74771_c("specialTexture");
      switch (special) {
         case 1:
            this.dsForm.set(this, EnumMagikarp.ROASTED.getForm());
            break;
         case 2:
            this.dsForm.set(this, EnumSpecial.Zombie.getForm());
            break;
         case 3:
            this.dsForm.set(this, EnumSpecial.Online.getForm());
            break;
         case 4:
            this.dsForm.set(this, EnumSpecial.Drowned.getForm());
            break;
         case 5:
            this.dsForm.set(this, EnumSpecial.Valentine.getForm());
            break;
         case 6:
            this.dsForm.set(this, EnumSpecial.Rainbow.getForm());
            break;
         case 7:
            this.dsForm.set(this, EnumSpecial.Alien.getForm());
            break;
         case 8:
            this.dsForm.set(this, EnumSolgaleo.Real.getForm());
            break;
         case 9:
            this.dsForm.set(this, EnumSpecial.Alter.getForm());
            break;
         case 10:
            this.dsForm.set(this, EnumSpecial.Pink.getForm());
            break;
         case 11:
            this.dsForm.set(this, EnumSpecial.Summer.getForm());
            break;
         case 12:
            this.dsForm.set(this, EnumSpecial.Crystal.getForm());
      }

      this.setCustomTexture(nbt.func_74779_i("CustomTexture"));
      this.setNickname(nbt.func_74779_i("Nickname"));
      this.setCaughtBall(nbt.func_74764_b("CaughtBall") ? EnumPokeballs.getFromIndex(nbt.func_74771_c("CaughtBall")) : EnumPokeballs.PokeBall);
      this.setNature(EnumNature.getNatureFromIndex(nbt.func_74771_c("Nature")));
      byte mintNature = nbt.func_74764_b("MintNature") ? nbt.func_74771_c("MintNature") : -1;
      this.mintNature = mintNature == -1 ? null : EnumNature.getNatureFromIndex(mintNature);
      if (NBT_VERSION == 0 && this.mintNature == EnumNature.Hardy) {
         this.mintNature = null;
      }

      this.setGrowth(EnumGrowth.getGrowthFromIndex(nbt.func_74771_c("Growth")));
      this.eggCycles = nbt.func_74764_b("eggCycles") ? nbt.func_74762_e("eggCycles") : null;
      this.eggSteps = this.eggCycles != null && nbt.func_74764_b("steps") ? nbt.func_74762_e("steps") : null;
      this.originalTrainerName = nbt.func_74779_i("originalTrainer");
      if (nbt.func_186855_b("originalTrainerUUID")) {
         this.originalTrainerUUID = nbt.func_186857_a("originalTrainerUUID");
      }

      this.restoreOT();
      this.levelContainer.setLevel(nbt.func_74762_e("Level"));
      this.dynamaxLevel = nbt.func_74762_e("DynamaxLevel");
      if (EnumGigantamaxPokemon.hasGigantamaxForm(this, true)) {
         this.gigantamaxFactor = nbt.func_74767_n("GigantamaxFactor");
      } else {
         this.gigantamaxFactor = false;
      }

      this.experience = nbt.func_74762_e("EXP");
      this.setDoesLevel(nbt.func_74767_n("DoesLevel"));
      this.setFriendship(nbt.func_74765_d("Friendship"));
      this.moveset.readFromNBT(nbt);
      this.stats.readFromNBT(nbt);
      this.bonusStats.readFromNBT(nbt);
      this.health = nbt.func_74762_e("Health");
      if (this.getExtraStats() != null) {
         this.extraStats.readFromNBT(nbt);
      }

      String disp;
      int tempAbilitySlot;
      if (nbt.func_74764_b("AbilitySlot")) {
         this.setAbilitySlot(nbt.func_74771_c("AbilitySlot"));
      } else if (nbt.func_74764_b("Ability")) {
         try {
            disp = nbt.func_74779_i("Ability");

            try {
               AbilityBase.getAbility(disp).get();
               this.setAbility(disp);
            } catch (Exception var10) {
               if (disp.equals("ComingSoon")) {
                  if (nbt.func_74764_b("AbilitySlot")) {
                     tempAbilitySlot = nbt.func_74762_e("AbilitySlot");
                     if (this.getBaseStats().getAbilitiesArray() != null && this.getBaseStats().getAbilitiesArray()[tempAbilitySlot] != null) {
                        disp = this.getBaseStats().getAbilitiesArray()[tempAbilitySlot];
                        this.setAbility((AbilityBase)AbilityBase.getAbility(disp).get());
                     }
                  } else if (RandomHelper.getRandomChance(1.0F / PixelmonConfig.getHiddenAbilityRate(this.dimension))) {
                     this.setAbilitySlot(2);
                  } else {
                     this.setAbilitySlot(RandomHelper.getRandomNumberBetween(0, this.getBaseStats().getAbilitiesArray()[1] == null ? 0 : 1));
                  }
               } else if (RandomHelper.getRandomChance(1.0F / PixelmonConfig.getHiddenAbilityRate(this.dimension))) {
                  this.setAbilitySlot(2);
               } else {
                  this.setAbilitySlot(RandomHelper.getRandomNumberBetween(0, this.getBaseStats().getAbilitiesArray()[1] == null ? 0 : 1));
               }
            }
         } catch (Exception var11) {
            Pixelmon.LOGGER.info("Didn't have an Ability; giving it one.");
            if (RandomHelper.getRandomChance(1.0F / PixelmonConfig.getHiddenAbilityRate(this.dimension))) {
               this.setAbilitySlot(2);
            } else {
               this.setAbilitySlot(RandomHelper.getRandomNumberBetween(0, this.getBaseStats().getAbilitiesArray()[1] == null ? 0 : 1));
            }
         }
      } else if (RandomHelper.getRandomChance(1.0F / PixelmonConfig.getHiddenAbilityRate(this.dimension))) {
         this.setAbilitySlot(2);
      } else {
         this.setAbilitySlot(RandomHelper.getRandomNumberBetween(0, this.getBaseStats().getAbilitiesArray()[1] == null ? 0 : 1));
      }

      if (nbt.func_74764_b("Pokerus")) {
         this.pokerus = Pokerus.deserializeFromNBT(nbt.func_74775_l("Pokerus"));
      }

      this.inRanch = nbt.func_74767_n("isInRanch");
      this.relearnableMoves = new ArrayList();
      int var6;
      int moveID;
      int[] id;
      if (nbt.func_74764_b("EggMoves")) {
         id = nbt.func_74759_k("EggMoves");
         var6 = id.length;

         for(tempAbilitySlot = 0; tempAbilitySlot < var6; ++tempAbilitySlot) {
            moveID = id[tempAbilitySlot];
            this.relearnableMoves.add(moveID);
         }
      }

      if (nbt.func_74764_b("RelearnableMoves")) {
         id = nbt.func_74759_k("RelearnableMoves");
         var6 = id.length;

         for(tempAbilitySlot = 0; tempAbilitySlot < var6; ++tempAbilitySlot) {
            moveID = id[tempAbilitySlot];
            if (!this.relearnableMoves.contains(moveID)) {
               this.relearnableMoves.add(moveID);
            }
         }
      }

      this.status = StatusPersist.readStatusFromNBT(nbt);
      NBTTagCompound moveSkillCooldowns;
      if (nbt.func_74764_b("HeldItemStack")) {
         moveSkillCooldowns = nbt.func_74775_l("HeldItemStack");
         if (RemapHandler.modfix != null) {
            moveSkillCooldowns = FMLCommonHandler.instance().getDataFixer().func_188257_a(FixTypes.ITEM_INSTANCE, moveSkillCooldowns);
         }

         ItemStack stack = new ItemStack(moveSkillCooldowns);
         this.heldItem = stack.func_190926_b() ? ItemStack.field_190927_a : stack;
      }

      this.persistentData = nbt.func_74775_l("PersistentData");
      if (nbt.func_74764_b("pixelmonID1")) {
         id = new int[]{nbt.func_74762_e("pixelmonID1"), nbt.func_74762_e("pixelmonID2")};
         this.persistentData.func_74783_a("OLD_pixelmonID", id);
      }

      if (nbt.func_74764_b("ForgeData") && nbt.func_74764_b("pixelmonID1")) {
         moveSkillCooldowns = nbt.func_74775_l("ForgeData");
         this.persistentData.func_179237_a(moveSkillCooldowns);
      }

      if (nbt.func_74764_b("isEgg") && !nbt.func_74767_n("isEgg")) {
         this.eggCycles = null;
      }

      String key;
      if (nbt.func_74764_b("MoveSkillCooldowns")) {
         moveSkillCooldowns = nbt.func_74775_l("MoveSkillCooldowns");
         Iterator var15 = moveSkillCooldowns.func_150296_c().iterator();

         while(var15.hasNext()) {
            key = (String)var15.next();
            if (key.endsWith("Most")) {
               key = key.substring(0, key.length() - 4);
            }

            MoveSkill moveSkill = MoveSkill.getMoveSkillByID(key);
            if (moveSkill != null) {
               UUID cooldown = moveSkillCooldowns.func_186857_a(moveSkill.id);
               this.moveSkillCooldownData.put(moveSkill.id, new Tuple(cooldown.getMostSignificantBits(), cooldown.getLeastSignificantBits()));
            }
         }
      }

      NBTBase str;
      Iterator var18;
      if (nbt.func_74764_b("SpecFlags")) {
         var18 = nbt.func_150295_c("SpecFlags", 8).iterator();

         while(var18.hasNext()) {
            str = (NBTBase)var18.next();
            key = ((NBTTagString)str).func_150285_a_();
            this.specFlags.add(key);
         }
      } else {
         var18 = PokemonSpec.extraSpecTypes.iterator();

         while(var18.hasNext()) {
            ISpecType specType = (ISpecType)var18.next();
            if (specType instanceof SpecFlag) {
               key = ((SpecFlag)specType).key;
               if (this.persistentData.func_74767_n(key)) {
                  this.specFlags.add(((SpecFlag)specType).key);
                  this.persistentData.func_82580_o(key);
               }
            }
         }
      }

      if (nbt.func_74764_b("ribbon_display")) {
         disp = nbt.func_74779_i("ribbon_display");
         this.displayedRibbon = disp == "" ? EnumRibbonType.NONE : EnumRibbonType.valueOf(disp);
      }

      if (nbt.func_74764_b("ribbons")) {
         this.ribbons.clear();
         var18 = nbt.func_150295_c("ribbons", 8).iterator();

         while(var18.hasNext()) {
            str = (NBTBase)var18.next();
            this.ribbons.add(EnumRibbonType.valueOf(((NBTTagString)str).func_150285_a_()));
         }
      }

   }

   private void restoreOT() {
      if (FMLCommonHandler.instance().getSide().isServer() && this.originalTrainerName != null && !this.originalTrainerName.isEmpty() && this.originalTrainerUUID == null) {
         MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
         if (server != null) {
            EntityPlayerMP player = server.func_184103_al().func_152612_a(this.originalTrainerName);
            if (player != null) {
               this.originalTrainerUUID = player.func_110124_au();
            } else if (this.storage != null) {
               this.originalTrainerName = this.getOwnerName();
               this.originalTrainerUUID = this.getOwnerPlayerUUID();
               if (this.originalTrainerUUID == null) {
                  this.originalTrainerUUID = this.getOwnerTrainerUUID();
               }
            }
         }
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      nbt.func_74774_a("NBT_VERSION", (byte)1);
      nbt.func_186854_a("UUID", this.uuid);
      nbt.func_74757_a("IsShiny", this.isShiny);
      if (!this.customTexture.isEmpty()) {
         nbt.func_74778_a("CustomTexture", this.customTexture);
      } else {
         nbt.func_82580_o("CustomTexture");
      }

      if (this.nickname != null && !this.nickname.isEmpty() && !Objects.equals(this.nickname, this.species.name)) {
         nbt.func_74778_a("Nickname", this.nickname);
      } else {
         nbt.func_82580_o("Nickname");
      }

      if (this.caughtBall != null) {
         nbt.func_74774_a("CaughtBall", (byte)this.caughtBall.ordinal());
      } else {
         nbt.func_82580_o("CaughtBall");
      }

      nbt.func_74774_a("Nature", (byte)this.nature.index);
      if (this.mintNature != null) {
         nbt.func_74774_a("MintNature", (byte)this.mintNature.index);
      }

      nbt.func_74774_a("Growth", (byte)this.growth.index);
      if (this.eggCycles != null) {
         nbt.func_74768_a("eggCycles", this.eggCycles);
         nbt.func_74768_a("steps", this.getEggSteps());
      } else {
         nbt.func_82580_o("eggCycles");
      }

      if (this.originalTrainerName != null && !this.originalTrainerName.isEmpty()) {
         nbt.func_74778_a("originalTrainer", this.originalTrainerName);
      } else {
         nbt.func_82580_o("originalTrainer");
      }

      if (this.originalTrainerUUID != null) {
         nbt.func_186854_a("originalTrainerUUID", this.originalTrainerUUID);
      } else {
         nbt.func_82580_o("originalTrainerUUID");
      }

      nbt.func_74768_a("Level", this.level);
      nbt.func_74768_a("DynamaxLevel", this.dynamaxLevel);
      nbt.func_74757_a("GigantamaxFactor", this.gigantamaxFactor);
      nbt.func_74768_a("EXP", this.experience);
      nbt.func_74757_a("DoesLevel", this.doesLevel);
      nbt.func_74757_a("isInRanch", this.inRanch);
      nbt.func_74777_a("Friendship", (short)this.friendship);
      this.moveset.writeToNBT(nbt);
      this.stats.writeToNBT(nbt);
      this.bonusStats.writeToNBT(nbt);
      nbt.func_74768_a("Health", this.health);
      if (this.getExtraStats() != null) {
         this.extraStats.writeToNBT(nbt);
      }

      if (this.abilitySlot != -1) {
         nbt.func_74774_a("AbilitySlot", (byte)this.abilitySlot);
      } else if (this.ability != null) {
         String abilityName = this.getAbilityName();
         nbt.func_74778_a("Ability", abilityName);
      }

      if (this.pokerus != null) {
         nbt.func_74782_a("Pokerus", this.pokerus.serializeToNBT());
      }

      this.status.writeToNBT(nbt);
      if (this.heldItem != null && !this.heldItem.func_190926_b()) {
         nbt.func_74782_a("HeldItemStack", this.heldItem.func_77955_b(new NBTTagCompound()));
      } else {
         nbt.func_82580_o("HeldItemStack");
      }

      nbt.func_74782_a("PersistentData", this.persistentData);
      if (!this.relearnableMoves.isEmpty()) {
         int[] relearnableMoves = new int[this.relearnableMoves.size()];

         for(int i = 0; i < this.relearnableMoves.size(); ++i) {
            relearnableMoves[i] = (Integer)this.relearnableMoves.get(i);
         }

         nbt.func_74783_a("RelearnableMoves", relearnableMoves);
      }

      NBTTagCompound moveSkillCooldowns = new NBTTagCompound();
      long cur = FMLCommonHandler.instance().getMinecraftServerInstance().field_71305_c[0].func_82737_E();
      Iterator var5 = this.moveSkillCooldownData.entrySet().iterator();

      while(var5.hasNext()) {
         Map.Entry entry = (Map.Entry)var5.next();
         if ((Long)((Tuple)entry.getValue()).func_76340_b() >= cur) {
            moveSkillCooldowns.func_186854_a((String)entry.getKey(), new UUID((Long)((Tuple)entry.getValue()).func_76341_a(), (Long)((Tuple)entry.getValue()).func_76340_b()));
         }
      }

      if (moveSkillCooldowns.func_186856_d() != 0) {
         nbt.func_74782_a("MoveSkillCooldowns", moveSkillCooldowns);
      }

      NBTTagList specList = new NBTTagList();
      Iterator var13 = this.specFlags.iterator();

      while(var13.hasNext()) {
         String specFlag = (String)var13.next();
         specList.func_74742_a(new NBTTagString(specFlag));
      }

      nbt.func_74782_a("SpecFlags", specList);
      if (this.displayedRibbon != null) {
         nbt.func_74778_a("ribbon_display", this.displayedRibbon.toString());
      }

      NBTTagList ribbonList = new NBTTagList();
      Iterator var15 = this.ribbons.iterator();

      while(var15.hasNext()) {
         EnumRibbonType ribbon = (EnumRibbonType)var15.next();
         ribbonList.func_74742_a(new NBTTagString(ribbon.toString()));
      }

      nbt.func_74782_a("ribbons", ribbonList);
      return nbt;
   }

   public void registerDataParameters(Map map) {
      super.registerDataParameters(map);
      this.dataSyncMap.put(((DataParameter)map.get("uuid")).func_187155_a(), this.dsUUID.registerDataParameter((DataParameter)map.get("uuid")));
      this.dataSyncMap.put(((DataParameter)map.get("growth")).func_187155_a(), this.dsGrowth.registerDataParameter((DataParameter)map.get("growth")));
      this.dataSyncMap.put(((DataParameter)map.get("level")).func_187155_a(), this.dsLevel.registerDataParameter((DataParameter)map.get("level")));
      this.dataSyncMap.put(((DataParameter)map.get("exp")).func_187155_a(), this.dsExp.registerDataParameter((DataParameter)map.get("exp")));
      this.dataSyncMap.put(((DataParameter)map.get("shiny")).func_187155_a(), this.dsShiny.registerDataParameter((DataParameter)map.get("shiny")));
      this.dataSyncMap.put(((DataParameter)map.get("customTexture")).func_187155_a(), this.dsCustomTexture.registerDataParameter((DataParameter)map.get("customTexture")));
      this.dataSyncMap.put(((DataParameter)map.get("nickname")).func_187155_a(), this.dsNickname.registerDataParameter((DataParameter)map.get("nickname")));
      this.dataSyncMap.put(((DataParameter)map.get("ribbon")).func_187155_a(), this.dsRibbon.registerDataParameter((DataParameter)map.get("ribbon")));
      this.dataSyncMap.put(((DataParameter)map.get("health")).func_187155_a(), this.dsHealth.registerDataParameter((DataParameter)map.get("health")));
      if (map.containsKey("owner")) {
         this.dataSyncMap.put(((DataParameter)map.get("owner")).func_187155_a(), this.dsOwner.registerDataParameter((DataParameter)map.get("owner")));
      }

   }

   public String toString() {
      return "Pokemon{" + this.getLocalizedName() + "}";
   }

   public Set getRibbonsSet() {
      return this.ribbons;
   }

   /** @deprecated */
   @Deprecated
   public ArrayList getRibbons() {
      return Lists.newArrayList(this.ribbons);
   }

   public void setDisplayedRibbon(EnumRibbonType sel) {
      if (this.ribbons.contains(sel)) {
         this.displayedRibbon = sel;
         this.dsRibbon.set(this, sel.ordinal());
      } else {
         this.displayedRibbon = EnumRibbonType.NONE;
         this.dsRibbon.set(this, EnumRibbonType.NONE.ordinal());
      }

      this.markDirty(EnumUpdateType.Ribbons);
   }
}
