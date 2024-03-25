package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.blocks.IPokemonOwner;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Aggression;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public abstract class Entity1Base extends EntityTameable {
   static final DataParameter dwSpecies;
   static final DataParameter dwForm;
   static final DataParameter dwGender;
   static final DataParameter dwGrowth;
   static final DataParameter dwBossMode;
   static final DataParameter dwLevel;
   static final DataParameter dwMaxHP;
   static final DataParameter dwExp;
   static final DataParameter dwNickname;
   static final DataParameter dwRibbon;
   static final DataParameter dwSpawnLocation;
   static final DataParameter dwUUID;
   protected Pokemon pokemon;
   public IPokemonOwner blockOwner = null;
   public EntityPokeBall hitByPokeball = null;
   public boolean canDespawn = true;
   public float length;
   public EnumAggression aggression;
   SpawnLocationType spawnLocation;
   protected EnumBossMode bossMode;
   private List delay;

   public Entity1Base(World par1World) {
      super(par1World);
      this.spawnLocation = SpawnLocationType.Land;
      this.bossMode = EnumBossMode.NotBoss;
      this.delay = Lists.newArrayList();
      this.field_70180_af.func_187214_a(dwSpecies, -1);
      this.field_70180_af.func_187214_a(dwForm, -1);
      this.field_70180_af.func_187214_a(dwGender, (byte)2);
      this.field_70180_af.func_187214_a(dwGrowth, -1);
      this.field_70180_af.func_187214_a(dwBossMode, -1);
      this.field_70180_af.func_187214_a(dwLevel, -1);
      this.field_70180_af.func_187214_a(dwMaxHP, 10);
      this.field_70180_af.func_187214_a(dwExp, 0);
      this.field_70180_af.func_187214_a(dwNickname, "");
      this.field_70180_af.func_187214_a(dwSpawnLocation, -1);
      this.field_70180_af.func_187214_a(dwUUID, Optional.of(this.func_110124_au()));
      this.field_70180_af.func_187214_a(dwRibbon, EnumRibbonType.NONE.ordinal());
   }

   public void setPokemon(Pokemon pokemon) {
      if (this.pokemon != null) {
         throw new IllegalArgumentException("Value already set");
      } else {
         this.pokemon = pokemon;
         this.pokemon.registerDataParameters(this.getDataWatcherMap());
         this.func_184221_a(this.pokemon.getUUID());
         this.resetDataWatchers();
         this.initBaseEntity();
         this.func_70606_j((float)pokemon.getHealth());
         if (!this.field_70170_p.field_72995_K) {
            this.giveWildAggression();
            if (pokemon.getPokerus() == null && PixelmonConfig.pokerusEnabled && PixelmonConfig.pokerusRate > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.pokerusRate)) {
               pokemon.setPokerus(new Pokerus(EnumPokerusType.getRandomType()));
            }
         }

      }
   }

   public void func_184221_a(UUID uuid) {
      super.func_184221_a(uuid);
      this.func_184212_Q().func_187227_b(dwUUID, Optional.of(uuid));
   }

   public abstract void resetDataWatchers();

   public abstract Map getDataWatcherMap();

   void initBaseEntity() {
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.pokemon.getMaxHealth());
      this.field_70178_ae = this.getBaseStats().types.contains(EnumType.Fire);
   }

   public Pokemon getPokemonData() {
      return this.pokemon;
   }

   public EnumSpecies getSpecies() {
      return this.pokemon.getSpecies();
   }

   public boolean isPokemon(EnumSpecies... species) {
      return this.pokemon.isPokemon(species);
   }

   public EnumBossMode getBossMode() {
      return this.bossMode;
   }

   public void setBoss(EnumBossMode mode) {
      this.field_70180_af.func_187227_b(EntityPixelmon.dwBossMode, (byte)mode.index);
   }

   public boolean isBossPokemon() {
      return this.getBossMode().isBossPokemon();
   }

   public boolean isLegendary() {
      return this.getPokemonData() != null && this.getPokemonData().isLegendary();
   }

   public BaseStats getBaseStats() {
      return this.pokemon != null ? this.pokemon.getBaseStats() : null;
   }

   public EnumAggression getAggression() {
      return this.aggression == null ? EnumAggression.passive : this.aggression;
   }

   public void setAggression(EnumAggression aggression) {
      if (aggression == null) {
         aggression = EnumAggression.passive;
      }

      this.aggression = aggression;
   }

   public void giveWildAggression() {
      if (this.func_70902_q() != null) {
         this.aggression = EnumAggression.passive;
      } else {
         int random = this.field_70146_Z.nextInt(100) + 1;
         Aggression aggression = this.getBaseStats().getAggression();
         if (aggression == null) {
            this.aggression = EnumAggression.passive;
         } else {
            if (random < aggression.timid) {
               this.aggression = EnumAggression.timid;
            } else if (random < aggression.timid + aggression.passive) {
               this.aggression = EnumAggression.passive;
            } else if (random < aggression.timid + aggression.passive + aggression.aggressive) {
               this.aggression = EnumAggression.aggressive;
            } else {
               this.aggression = EnumAggression.passive;
            }

         }
      }
   }

   public SpawnLocationType getSpawnLocation() {
      return this.spawnLocation;
   }

   public void setSpawnLocation(SpawnLocationType spawnLocation) {
      if (spawnLocation == null) {
         spawnLocation = SpawnLocationType.Land;
      }

      this.spawnLocation = spawnLocation;
   }

   public boolean hasOwner() {
      return this.func_184753_b() != null;
   }

   @Nullable
   public UUID func_184753_b() {
      return this.field_70170_p.field_72995_K ? super.func_184753_b() : (this.pokemon == null ? null : this.pokemon.getOwnerPlayerUUID());
   }

   public boolean belongsTo(EntityPlayer player) {
      return Objects.equals(player.func_110124_au(), this.pokemon.getOwnerPlayerUUID());
   }

   public void update(EnumUpdateType... types) {
      this.pokemon.markDirty(types);
   }

   @Nullable
   public PokemonStorage getStorage() {
      return (PokemonStorage)this.pokemon.getStorageAndPosition().func_76341_a();
   }

   @Nullable
   public PlayerPartyStorage getPlayerParty() {
      PokemonStorage storage = this.getStorage();
      return storage != null && storage instanceof PlayerPartyStorage ? (PlayerPartyStorage)storage : null;
   }

   public java.util.Optional getPlayerStorage() {
      return this.pokemon.getStorageAndPosition() != null && this.pokemon.getStorageAndPosition().func_76341_a() instanceof PlayerPartyStorage ? java.util.Optional.of((PlayerPartyStorage)this.pokemon.getStorageAndPosition().func_76341_a()) : java.util.Optional.empty();
   }

   public String func_70005_c_() {
      return this.getPokemonName();
   }

   public boolean func_145818_k_() {
      return false;
   }

   public String getPokemonName() {
      return this.pokemon.getBaseStats().getPokemonName();
   }

   public String getLocalizedName() {
      return this.pokemon.getLocalizedName();
   }

   public String getNickname() {
      return this.pokemon.getDisplayName();
   }

   public String getEscapedNickname() {
      return Matcher.quoteReplacement(this.getNickname());
   }

   public AxisAlignedBB func_70046_E() {
      return null;
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return this.func_174813_aQ();
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.55);
   }

   public boolean func_70094_T() {
      if (super.func_70094_T()) {
         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(1, 0, 0)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t + 1.0, this.field_70163_u, this.field_70161_v);
         }

         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(-1, 0, 0)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t - 1.0, this.field_70163_u, this.field_70161_v);
         }

         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(0, 1, 0)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u + 1.0, this.field_70161_v);
         }

         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(0, -1, 0)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u - 1.0, this.field_70161_v);
         }

         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(0, 0, 1)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v + 1.0);
         }

         if (!this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(0, 0, -1)).func_191058_s()) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v - 1.0);
         }

         return super.func_70094_T();
      } else {
         return false;
      }
   }

   public ItemStack func_184614_ca() {
      return this.pokemon.getHeldItem();
   }

   public boolean func_70104_M() {
      return true;
   }

   public int func_70874_b() {
      return 0;
   }

   protected boolean func_70692_ba() {
      return true;
   }

   @Nullable
   public Entity changeDimension(int dimensionIn, ITeleporter teleporter) {
      return null;
   }

   public void func_70645_a(DamageSource cause) {
      boolean showDeathMessages = this.field_70170_p.func_82736_K().func_82766_b("showDeathMessages");
      this.field_70170_p.func_82736_K().func_82764_b("showDeathMessages", "false");
      super.func_70645_a(cause);
      this.field_70170_p.func_82736_K().func_82764_b("showDeathMessages", showDeathMessages ? "true" : "false");
   }

   @Nullable
   public EntityAgeable func_90011_a(EntityAgeable ageable) {
      return null;
   }

   public boolean func_184652_a(EntityPlayer player) {
      return this.func_70902_q() == player;
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (source.func_76364_f() == this.func_70902_q()) {
         this.pokemon.decreaseFriendship(20);
      }

      return super.func_70097_a(source, amount);
   }

   public void func_70077_a(EntityLightningBolt lightningBolt) {
      if (this.getSpecies() == EnumSpecies.Magikarp) {
         this.getPokemonData().setForm(EnumMagikarp.ROASTED);
      } else {
         super.func_70077_a(lightningBolt);
      }

   }

   public void func_180430_e(float distance, float damageMultiplier) {
      if (this.getSpawnLocation() != SpawnLocationType.Water) {
         super.func_180430_e(distance, damageMultiplier);
      }
   }

   public void func_70071_h_() {
      super.func_70071_h_();
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      stripVanillaNBT(nbt);
      this.pokemon.writeToNBT(nbt);
      if (this.aggression != null && this.aggression != EnumAggression.passive) {
         nbt.func_74774_a("Aggression", (byte)this.aggression.index);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (this.pokemon == null) {
         this.pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID());
         this.pokemon.registerDataParameters(EntityPixelmon.dwMap);
         this.func_184221_a(this.pokemon.getUUID());
      }

      this.pokemon.readFromNBT(nbt);
      this.resetDataWatchers();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.pokemon.getMaxHealth());
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.55);
      this.aggression = nbt.func_74764_b("Aggression") ? EnumAggression.getAggression(nbt.func_74771_c("Aggression")) : EnumAggression.passive;
   }

   public NBTTagCompound getEntityData() {
      return this.pokemon.getPersistentData();
   }

   public void func_184206_a(DataParameter key) {
      if (this.pokemon == null && this.getDataWatcherMap().containsValue(key)) {
         if (key.func_187155_a() != dwSpecies.func_187155_a()) {
            if (this.delay == null) {
               this.delay = Lists.newArrayList();
            }

            this.delay.add(key);
            return;
         }

         this.pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.getFromDex((Integer)this.field_70180_af.func_187225_a(dwSpecies)));
         this.pokemon.registerDataParameters(this.getDataWatcherMap());
         this.delay.forEach((k) -> {
            this.pokemon.dataManagerChange(k, this.field_70180_af.func_187225_a(k));
         });
      }

      try {
         if (this.getDataWatcherMap().containsValue(key) && this.pokemon.dataManagerChange(key, this.field_70180_af.func_187225_a(key))) {
         }

         if (key.func_187155_a() == dwSpecies.func_187155_a() || key.func_187155_a() == dwForm.func_187155_a() || key.func_187155_a() == dwGender.func_187155_a()) {
            this.initBaseEntity();
         }

         if (key.func_187155_a() == dwLevel.func_187155_a()) {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.getPokemonData().getMaxHealth());
         }

         if (key.func_187155_a() == dwSpawnLocation.func_187155_a()) {
            this.spawnLocation = SpawnLocationType.getFromIndex((Integer)this.field_70180_af.func_187225_a(dwSpawnLocation));
         }

         if (key.func_187155_a() == dwBossMode.func_187155_a()) {
            this.bossMode = EnumBossMode.getMode((Byte)this.field_70180_af.func_187225_a(dwBossMode));
         }
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public static void stripVanillaNBT(NBTTagCompound nbt) {
      nbt.func_82580_o("HurtByTimestamp");
      nbt.func_82580_o("HurtTime");
      nbt.func_82580_o("Sitting");
      nbt.func_82580_o("FallFlying");
      nbt.func_82580_o("Leashed");
      nbt.func_82580_o("CanPickUpLoot");
      nbt.func_82580_o("PortalCooldown");
      nbt.func_82580_o("LeftHanded");
      nbt.func_82580_o("HandItems");
      nbt.func_82580_o("HandDropChances");
      nbt.func_82580_o("ArmorItems");
      nbt.func_82580_o("ArmorDropChances");
      nbt.func_82580_o("AbsorptionAmount");
      nbt.func_82580_o("ForcedAge");
      nbt.func_82580_o("InLove");
      nbt.func_82580_o("Age");
      if (nbt.func_74765_d("Air") == 300) {
         nbt.func_82580_o("Air");
      }

      if (nbt.func_74765_d("Fire") == -1) {
         nbt.func_82580_o("Fire");
      }

      if (nbt.func_74765_d("DeathTime") == 0) {
         nbt.func_82580_o("DeathTime");
      }

      if (nbt.func_74760_g("FallDistance") == 0.0F) {
         nbt.func_82580_o("FallDistance");
      }

      if (!nbt.func_74767_n("UpdateBlocked")) {
         nbt.func_82580_o("UpdateBlocked");
      }

      nbt.func_82580_o("");
   }

   /** @deprecated */
   @Deprecated
   public static String getLocalizedDescription(String name) {
      return I18n.func_74838_a("pixelmon." + name.toLowerCase() + ".description");
   }

   public static String getLocalizedName(String name) {
      return I18n.func_74838_a("pixelmon." + name.toLowerCase() + ".name");
   }

   public static String getLocalizedName(EnumSpecies species) {
      return species.getLocalizedName();
   }

   static {
      dwSpecies = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwForm = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187191_a);
      dwGender = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187191_a);
      dwGrowth = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187191_a);
      dwBossMode = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187191_a);
      dwLevel = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwMaxHP = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwExp = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwNickname = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187194_d);
      dwRibbon = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwSpawnLocation = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187192_b);
      dwUUID = EntityDataManager.func_187226_a(Entity1Base.class, DataSerializers.field_187203_m);
   }
}
