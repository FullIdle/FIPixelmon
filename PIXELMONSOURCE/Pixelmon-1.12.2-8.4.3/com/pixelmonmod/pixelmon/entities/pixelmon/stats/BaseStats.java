package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.LevelCondition;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.LevelingEvolution;
import com.pixelmonmod.pixelmon.enums.EnumEggGroup;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.ExperienceGroup;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen1TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen2TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen3TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen4TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen5TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen6TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen7TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.util.SoundEvent;
import org.apache.commons.lang3.tuple.Pair;

public class BaseStats {
   /** @deprecated */
   @Deprecated
   public static final transient Gson GSON;
   public static final transient EnumMap allBaseStats;
   /** @deprecated */
   @Deprecated
   public String pixelmonName;
   /** @deprecated */
   @Deprecated
   public EnumSpecies pokemon;
   /** @deprecated */
   @Deprecated
   public transient int nationalPokedexNumber;
   /** @deprecated */
   @Deprecated
   public int form = 0;
   /** @deprecated */
   @Deprecated
   public LinkedHashMap stats = null;
   /** @deprecated */
   @Deprecated
   public Integer catchRate = null;
   /** @deprecated */
   @Deprecated
   public Double malePercent = null;
   /** @deprecated */
   @Deprecated
   public Integer spawnLevel = null;
   /** @deprecated */
   @Deprecated
   public Integer spawnLevelRange = null;
   /** @deprecated */
   @Deprecated
   public Integer baseExp = null;
   /** @deprecated */
   @Deprecated
   public Integer baseFriendship = null;
   /** @deprecated */
   @Deprecated
   public ArrayList types = null;
   /** @deprecated */
   @Deprecated
   public Float height = null;
   /** @deprecated */
   @Deprecated
   public Float width = null;
   /** @deprecated */
   @Deprecated
   public Float length = null;
   /** @deprecated */
   @Deprecated
   public Boolean isRideable = null;
   /** @deprecated */
   @Deprecated
   public Boolean canFly = null;
   /** @deprecated */
   @Deprecated
   public Boolean canSurf = null;
   /** @deprecated */
   @Deprecated
   public Boolean canShoulder = null;
   public String[] preEvolutions = null;
   public transient PokemonSpec[] specPreEvolutions = null;
   public transient EnumSpecies[] legacyPreEvolutions = null;
   /** @deprecated */
   @Deprecated
   public ExperienceGroup experienceGroup = null;
   /** @deprecated */
   @Deprecated
   public Aggression aggression = null;
   public SwimmingParameters swimmingParameters = null;
   public FlyingParameters flyingParameters = null;
   public MountedFlying mountedFlying = null;
   public SpawnLocationType[] spawnLocations = null;
   public LinkedHashMap evYields = null;
   public RidingOffsets ridingOffsets = null;
   /** @deprecated */
   @Deprecated
   public Float hoverHeight = null;
   /** @deprecated */
   @Deprecated
   public Float weight = null;
   private EntityBoundsData bounding_box = null;
   /** @deprecated */
   @Deprecated
   public ArrayList evolutions = null;
   /** @deprecated */
   @Deprecated
   public String[] abilities = null;
   public EnumEggGroup[] eggGroups = null;
   /** @deprecated */
   @Deprecated
   public Integer eggCycles = null;
   /** @deprecated */
   @Deprecated
   public LinkedHashMap levelUpMoves = null;
   /** @deprecated */
   @Deprecated
   public LinkedHashSet tmMoves = null;
   /** @deprecated */
   @Deprecated
   public LinkedHashSet tutorMoves = null;
   /** @deprecated */
   @Deprecated
   public LinkedHashSet eggMoves = null;
   private LinkedHashSet transferMoves = null;
   private LinkedHashSet hmMoves = null;
   private LinkedHashSet trMoves = null;
   private LinkedHashSet tmMoves8 = null;
   private LinkedHashSet tmMoves7 = null;
   private LinkedHashSet tmMoves6 = null;
   private LinkedHashSet tmMoves5 = null;
   private LinkedHashSet tmMoves4 = null;
   private LinkedHashSet tmMoves3 = null;
   private LinkedHashSet tmMoves2 = null;
   private LinkedHashSet tmMoves1 = null;
   public LinkedHashMap forms = null;
   public transient BaseStats parent = null;
   private final transient HashMap sounds = new HashMap();
   private transient boolean[] soundRegistered = new boolean[]{false, false, false};
   public transient int minLevel = 1;
   public transient int maxLevel;

   BaseStats() {
      this.maxLevel = PixelmonServerConfig.maxLevel;
   }

   public EnumSpecies getSpecies() {
      return this.pokemon;
   }

   public String getPokemonName() {
      return this.pixelmonName;
   }

   public int getFormNumber() {
      return this.form;
   }

   public IEnumForm getEnumForm() {
      return this.getSpecies().getFormEnum(this.getFormNumber());
   }

   public int getStat(StatsType stat) {
      return (Integer)this.stats.getOrDefault(stat, -1);
   }

   /** @deprecated */
   @Deprecated
   public int get(StatsType stat) {
      return (Integer)this.stats.getOrDefault(stat, -1);
   }

   public int getCatchRate() {
      return this.catchRate;
   }

   public double getMalePercent() {
      return this.malePercent;
   }

   public boolean isMaleOnly() {
      return this.malePercent >= 100.0;
   }

   public boolean isFemaleOnly() {
      return this.malePercent == 0.0;
   }

   public boolean isGenderless() {
      return this.malePercent < 0.0;
   }

   public Gender getRandomGender(Random random) {
      if (this.isGenderless()) {
         return Gender.None;
      } else if (this.isFemaleOnly()) {
         return Gender.Female;
      } else if (this.isMaleOnly()) {
         return Gender.Male;
      } else {
         return random.nextDouble() * 100.0 < this.malePercent ? Gender.Male : Gender.Female;
      }
   }

   public int getSpawnLevel() {
      return this.spawnLevel;
   }

   public int getSpawnLevelRange() {
      return this.spawnLevelRange;
   }

   public int getBaseExp() {
      return this.baseExp;
   }

   public int getBaseFriendship() {
      return this.baseFriendship;
   }

   public List getTypeList() {
      return this.types;
   }

   public EnumType getType1() {
      return (EnumType)this.types.get(0);
   }

   public EnumType getType2() {
      return this.types.size() > 1 ? (EnumType)this.types.get(1) : null;
   }

   /** @deprecated */
   @Deprecated
   public Float getHeight() {
      return this.height;
   }

   /** @deprecated */
   @Deprecated
   public Float getWidth() {
      return (float)this.getBoundsData().getWidth();
   }

   /** @deprecated */
   @Deprecated
   public Float getLength() {
      return (float)this.getBoundsData().getWidth();
   }

   public Boolean IsRideable() {
      return this.isRideable;
   }

   public Boolean canFly() {
      return this.canFly;
   }

   public Boolean canSurf() {
      return this.canSurf;
   }

   public Boolean canShoulder() {
      return this.canShoulder;
   }

   public ExperienceGroup getExperienceGroup() {
      return this.experienceGroup;
   }

   public Aggression getAggression() {
      return this.aggression;
   }

   public List getSpawnLocations() {
      return (List)(this.spawnLocations != null ? ImmutableList.copyOf(this.spawnLocations) : Collections.emptyList());
   }

   /** @deprecated */
   @Deprecated
   public float getHoverHeight() {
      return 0.0F;
   }

   public EntityBoundsData getBoundsData() {
      if (this.bounding_box == null) {
         this.bounding_box = new EntityBoundsData(this.width != null ? Double.parseDouble(this.width.toString()) : 1.0, this.height != null ? Double.parseDouble(this.height.toString()) : 1.0, (this.height != null ? this.height : 1.0F) * 0.85F);
      }

      return this.bounding_box;
   }

   /** @deprecated */
   @Deprecated
   public boolean hovers() {
      return false;
   }

   public float getWeight() {
      return this.weight;
   }

   @Nonnull
   public List getEvolutions() {
      return (List)(this.evolutions != null ? ImmutableList.copyOf(this.evolutions) : Collections.emptyList());
   }

   public String[] getAbilitiesArray() {
      return this.abilities;
   }

   public List getAllAbilities() {
      return (List)Arrays.stream(this.abilities).filter(Objects::nonNull).map(AbilityBase::getAbility).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
   }

   public Optional getHiddenAbility() {
      return this.abilities[2] != null ? AbilityBase.getAbility(this.abilities[2]) : Optional.empty();
   }

   public boolean hasAbility(String ability) {
      String[] var2 = this.abilities;
      int var3 = var2.length;
      byte var4 = 0;
      if (var4 >= var3) {
         return false;
      } else {
         String ab = var2[var4];
         return ab != null && ab.toLowerCase().replace(" ", "").equals(ability.toLowerCase().replace(" ", ""));
      }
   }

   public EnumEggGroup[] getEggGroupsArray() {
      return this.eggGroups;
   }

   public List getEggGroups() {
      return Lists.newArrayList(this.eggGroups);
   }

   public boolean hasEggGroup(EnumEggGroup group) {
      return this.getEggGroups().contains(group);
   }

   public Integer getEggCycles() {
      return this.eggCycles;
   }

   public BaseStats getParent() {
      return this.parent == null ? this : this.parent;
   }

   public void expand(BaseStats comparison) {
      this.nationalPokedexNumber = comparison.nationalPokedexNumber;
      Field[] var2 = this.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field f = var2[var4];

         try {
            String name = f.getName();
            if (!name.equals("forms")) {
               Object o1 = f.get(this);
               Object o2 = f.get(comparison);
               EnumSpecies species = comparison.getSpecies();
               if ((!species.getFormEnum(this.form).isRegionalForm() || !name.equals("transferMoves") && !name.equals("trMoves") && !name.startsWith("tmMoves")) && o1 == null && o2 != null) {
                  f.set(this, o2);
               }
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      }

   }

   public List getFormatLevelMoves() {
      List check = new ArrayList();
      Iterator var2 = this.getLevelupMoves().entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         Integer lv = (Integer)entry.getKey();
         List ab = (List)entry.getValue();
         Iterator var6 = ab.iterator();

         while(var6.hasNext()) {
            AttackBase a = (AttackBase)var6.next();
            check.add(Pair.of(lv, a));
         }
      }

      check.sort(Comparator.comparingInt(Pair::getLeft));
      return check;
   }

   public void addSound(SoundType type, SoundEvent sound) {
      List stats = Lists.newArrayList(new BaseStats[]{this});
      if (this.forms != null) {
         stats.addAll(this.forms.values());
      }

      Iterator var4 = stats.iterator();

      while(var4.hasNext()) {
         BaseStats form = (BaseStats)var4.next();
         ArrayList list = (ArrayList)form.sounds.getOrDefault(type, new ArrayList());
         if (!this.soundRegistered[type.ordinal()]) {
            list.clear();
         }

         list.add(sound);
         form.sounds.put(type, list);
         if (this == form) {
            this.soundRegistered[type.ordinal()] = true;
         }
      }

   }

   public SoundEvent getSound(SoundType type) {
      ArrayList list = (ArrayList)this.sounds.get(type);
      return list == null ? null : (SoundEvent)RandomHelper.getRandomElementFromList(list);
   }

   private boolean hasSound(SoundType type) {
      return this.sounds.containsKey(type);
   }

   public boolean hasSoundForGender(Gender gender) {
      return this.sounds.containsKey(gender == Gender.Male ? BaseStats.SoundType.Male : BaseStats.SoundType.Female) || this.sounds.containsKey(BaseStats.SoundType.Neutral);
   }

   public SoundEvent getSoundForGender(Gender gender) {
      if (gender == Gender.Male) {
         if (this.hasSound(BaseStats.SoundType.Male)) {
            return this.getSound(BaseStats.SoundType.Male);
         }
      } else if (gender == Gender.Female && this.hasSound(BaseStats.SoundType.Female)) {
         return this.getSound(BaseStats.SoundType.Female);
      }

      return this.hasSound(BaseStats.SoundType.Neutral) ? this.getSound(BaseStats.SoundType.Neutral) : null;
   }

   public void calculateMinMaxLevels() {
      this.maxLevel = this.getHighestFeasibleLevel();
      if (this.maxLevel == -1) {
         this.maxLevel = PixelmonServerConfig.maxLevel;
      }

      this.minLevel = 1;
      if (this.legacyPreEvolutions != null && this.legacyPreEvolutions.length > 0) {
         EnumSpecies[] var1 = this.legacyPreEvolutions;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumSpecies preEvo = var1[var3];

            try {
               if (preEvo != null) {
                  IEnumForm formEnum = preEvo.getFormEnum(this.form);
                  if (formEnum == null) {
                     formEnum = EnumNoForm.NoForm;
                  }

                  BaseStats preEvoBS = preEvo.getBaseStats((IEnumForm)formEnum);
                  if (preEvoBS == null) {
                     break;
                  }

                  int level = preEvoBS.getHighestFeasibleLevel();
                  if (level > this.minLevel) {
                     this.minLevel = level;
                  }
               }
            } catch (NullPointerException var8) {
               var8.printStackTrace();
            }
         }
      }

      if (this.maxLevel < this.minLevel) {
         this.minLevel = this.maxLevel;
      }

   }

   public int getHighestFeasibleLevel() {
      int maxLevel = -1;
      if (!this.getEvolutions().isEmpty()) {
         Iterator var2 = this.getEvolutions().iterator();

         while(true) {
            Evolution evolution;
            do {
               if (!var2.hasNext()) {
                  return maxLevel;
               }

               evolution = (Evolution)var2.next();
            } while(evolution == null);

            if (evolution instanceof LevelingEvolution) {
               int level = ((LevelingEvolution)evolution).getLevel();
               if (level > maxLevel) {
                  maxLevel = level;
               }
            }

            Iterator var6 = evolution.getConditionsOfType(LevelCondition.class).iterator();

            while(var6.hasNext()) {
               LevelCondition condition = (LevelCondition)var6.next();
               if (condition.level > maxLevel) {
                  maxLevel = condition.level;
               }
            }
         }
      } else {
         return maxLevel;
      }
   }

   public void initLevelupMoves() {
      if (this.levelUpMoves.containsKey(0)) {
         if (!this.levelUpMoves.containsKey(1)) {
            this.levelUpMoves.put(1, new LinkedHashSet());
         }

         ((LinkedHashSet)this.levelUpMoves.get(1)).addAll((Collection)this.levelUpMoves.get(0));
         this.levelUpMoves.remove(0);
      }

   }

   public Map getLevelupMoves() {
      Map map = Maps.newHashMap();
      Iterator var2 = this.levelUpMoves.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         int key = (Integer)entry.getKey() == 0 ? 1 : (Integer)entry.getKey();
         map.put(key, ((LinkedHashSet)entry.getValue()).stream().map(Attack::getActualMove).collect(ImmutableList.toImmutableList()));
      }

      return map;
   }

   public ArrayList getMovesUpToLevel(int level) {
      LinkedHashSet attacks = new LinkedHashSet();

      for(int i = 0; i <= level; ++i) {
         if (this.levelUpMoves != null && this.levelUpMoves.containsKey(i)) {
            attacks.addAll((Collection)this.levelUpMoves.get(i));
         }
      }

      return new ArrayList(severReferences(attacks));
   }

   public ArrayList getMovesAtLevel(int level) {
      if (level == 0 || level == 1) {
         level = 1;
      }

      return this.levelUpMoves != null && this.levelUpMoves.containsKey(level) ? new ArrayList(severReferences((Collection)this.levelUpMoves.get(level))) : new ArrayList();
   }

   public Moveset loadMoveset(int level) {
      ArrayList attackList = this.getMovesUpToLevel(level);

      while(attackList.size() > 4) {
         if (PixelmonConfig.useRecentLevelMoves) {
            attackList.remove(0);
         } else {
            RandomHelper.removeRandomElementFromList(attackList);
         }
      }

      if (attackList.isEmpty()) {
         attackList.add(new Attack("Tackle"));
      }

      Moveset moveset = new Moveset();
      moveset.addAll(severReferences(attackList));
      return moveset;
   }

   /** @deprecated */
   @Deprecated
   public LinkedHashSet getTMHMMoves() {
      return this.tmMoves != null ? severReferences(this.tmMoves) : new LinkedHashSet();
   }

   public List getLegacyTMMoves() {
      return this.tmMoves != null ? (List)this.tmMoves.stream().map(Attack::getActualMove).collect(Collectors.toList()) : Collections.emptyList();
   }

   @Nonnull
   public List getTMMovesFor(int gen) {
      switch (gen) {
         case 1:
            return (List)(this.tmMoves1 != null ? new ArrayList(this.tmMoves1) : Collections.emptyList());
         case 2:
            return (List)(this.tmMoves2 != null ? new ArrayList(this.tmMoves2) : Collections.emptyList());
         case 3:
            return (List)(this.tmMoves3 != null ? new ArrayList(this.tmMoves3) : Collections.emptyList());
         case 4:
            return (List)(this.tmMoves4 != null ? new ArrayList(this.tmMoves4) : Collections.emptyList());
         case 5:
            return (List)(this.tmMoves5 != null ? new ArrayList(this.tmMoves5) : Collections.emptyList());
         case 6:
            return (List)(this.tmMoves6 != null ? new ArrayList(this.tmMoves6) : Collections.emptyList());
         case 7:
            return (List)(this.tmMoves7 != null ? new ArrayList(this.tmMoves7) : Collections.emptyList());
         case 8:
            return (List)(this.tmMoves8 != null ? new ArrayList(this.tmMoves8) : Collections.emptyList());
         default:
            return Collections.emptyList();
      }
   }

   public ArrayList getTutorMoves() {
      return this.tutorMoves != null ? new ArrayList(severReferences(this.tutorMoves)) : new ArrayList();
   }

   public ArrayList getTransferMoves() {
      return this.transferMoves != null ? new ArrayList(severReferences(this.transferMoves)) : new ArrayList();
   }

   public ArrayList getEggMoves() {
      return this.eggMoves != null ? new ArrayList(severReferences(this.eggMoves)) : new ArrayList();
   }

   public List getTMMoves() {
      return (List)(this.tmMoves8 == null ? Collections.emptyList() : ImmutableList.copyOf(this.tmMoves8));
   }

   public List getTrMoves() {
      return (List)(this.trMoves == null ? Collections.emptyList() : ImmutableList.copyOf(this.trMoves));
   }

   public List getHmMoves() {
      return (List)(this.hmMoves == null ? Collections.emptyList() : ImmutableList.copyOf(this.hmMoves));
   }

   public ArrayList getAllMoves() {
      ArrayList moves = new ArrayList();
      if (this.levelUpMoves != null && !this.levelUpMoves.isEmpty()) {
         Iterator var2 = this.levelUpMoves.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry entry = (Map.Entry)var2.next();
            moves.addAll(severReferences((Collection)entry.getValue()));
         }
      }

      moves.addAll((Collection)this.getLegacyTMMoves().stream().map(Attack::new).collect(Collectors.toList()));
      moves.addAll((Collection)this.getTMMoves().stream().map(ITechnicalMove::getAttack).map(Attack::new).collect(Collectors.toList()));
      moves.addAll((Collection)this.getTrMoves().stream().map(ITechnicalMove::getAttack).map(Attack::new).collect(Collectors.toList()));
      moves.addAll((Collection)this.getHmMoves().stream().map(Attack::new).collect(Collectors.toList()));
      moves.addAll(this.getTutorMoves());
      moves.addAll(this.getTransferMoves());
      moves.addAll(this.getEggMoves());
      ArrayList distinctMoves = new ArrayList();
      Iterator var6 = moves.iterator();

      while(var6.hasNext()) {
         Attack attack = (Attack)var6.next();
         if (!distinctMoves.contains(attack)) {
            distinctMoves.add(attack);
         }
      }

      return distinctMoves;
   }

   public boolean canLearn(String attackName) {
      ArrayList allMoves = this.getAllMoves();
      Iterator var3 = allMoves.iterator();

      Attack attack;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         attack = (Attack)var3.next();
      } while(!attack.isAttack(attackName));

      return true;
   }

   public boolean canLearn(ITechnicalMove move) {
      if (PixelmonServerConfig.superUniversalTMs) {
         return this.canLearn(move.getAttackName()) || this.getTMMovesFor(move.getGeneration()).contains(move) || this.canLearnViaOtherSet(move);
      } else if (PixelmonServerConfig.universalTMs) {
         return this.canLearnTMTRHM(move.getAttackName()) || this.getTMMovesFor(move.getGeneration()).contains(move) || this.canLearnViaOtherSet(move);
      } else {
         return this.trMoves != null && this.trMoves.contains(move) || this.tmMoves8 != null && this.tmMoves8.contains(move) || this.tmMoves7 != null && this.tmMoves7.contains(move) || this.tmMoves6 != null && this.tmMoves6.contains(move) || this.tmMoves5 != null && this.tmMoves5.contains(move) || this.tmMoves4 != null && this.tmMoves4.contains(move) || this.tmMoves3 != null && this.tmMoves3.contains(move) || this.tmMoves2 != null && this.tmMoves2.contains(move) || this.tmMoves1 != null && this.tmMoves1.contains(move);
      }
   }

   public boolean canLearnHM(AttackBase attack) {
      if (PixelmonServerConfig.superUniversalTMs) {
         return this.canLearn(attack.getAttackName());
      } else {
         return PixelmonServerConfig.universalTMs ? this.canLearnTMTRHM(attack.getAttackName()) : this.hmMoves.contains(attack);
      }
   }

   public boolean canLearnViaOtherSet(ITechnicalMove move) {
      return !(move instanceof Gen8TechnicalRecords) && this.trMoves != null && this.isMoveInSet(this.trMoves, move.getAttackName()) || !(move instanceof Gen8TechnicalMachines) && this.tmMoves8 != null && this.isMoveInSet(this.tmMoves8, move.getAttackName()) || !(move instanceof Gen7TechnicalMachines) && this.tmMoves7 != null && this.isMoveInSet(this.tmMoves7, move.getAttackName()) || !(move instanceof Gen6TechnicalMachines) && this.tmMoves6 != null && this.isMoveInSet(this.tmMoves6, move.getAttackName()) || !(move instanceof Gen5TechnicalMachines) && this.tmMoves5 != null && this.isMoveInSet(this.tmMoves5, move.getAttackName()) || !(move instanceof Gen4TechnicalMachines) && this.tmMoves4 != null && this.isMoveInSet(this.tmMoves4, move.getAttackName()) || !(move instanceof Gen3TechnicalMachines) && this.tmMoves3 != null && this.isMoveInSet(this.tmMoves3, move.getAttackName()) || !(move instanceof Gen2TechnicalMachines) && this.tmMoves2 != null && this.isMoveInSet(this.tmMoves2, move.getAttackName()) || !(move instanceof Gen1TechnicalMachines) && this.tmMoves1 != null && this.isMoveInSet(this.tmMoves1, move.getAttackName());
   }

   public Set getAllTMTRHMMoves() {
      HashSet moves = new HashSet();
      moves.addAll(this.getLegacyTMMoves());
      moves.addAll((Collection)this.getTMMoves().stream().map(ITechnicalMove::getAttack).collect(Collectors.toList()));
      moves.addAll((Collection)this.getTrMoves().stream().map(ITechnicalMove::getAttack).collect(Collectors.toList()));
      moves.addAll(this.getHmMoves());
      return moves;
   }

   public boolean canLearnTMTRHM(String attackName) {
      Set moves = this.getAllTMTRHMMoves();
      Iterator var3 = moves.iterator();

      AttackBase attack;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         attack = (AttackBase)var3.next();
      } while(!attack.isAttack(attackName));

      return true;
   }

   public boolean isMoveInSet(LinkedHashSet tms, String attack) {
      Iterator var3 = tms.iterator();

      ITechnicalMove tm;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         tm = (ITechnicalMove)var3.next();
      } while(!attack.equalsIgnoreCase(tm.getAttackName()));

      return true;
   }

   public boolean canLearn(AttackBase attack, BaseStatsLearnType... types) {
      BaseStatsLearnType[] var3 = types;
      int var4 = types.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BaseStatsLearnType type = var3[var5];
         if (type.canLearn(this, attack)) {
            return true;
         }
      }

      return false;
   }

   /** @deprecated */
   @Deprecated
   public static void loadAllBaseStats() {
      BaseStatsLoader.loadAllBaseStats();
   }

   private static LinkedHashSet severReferences(Collection attacks) {
      LinkedHashSet newAttacks = new LinkedHashSet(attacks.size());
      Iterator var2 = attacks.iterator();

      while(var2.hasNext()) {
         Attack oldAttack = (Attack)var2.next();
         if (oldAttack != null) {
            newAttacks.add(new Attack(oldAttack.getActualMove()));
         }
      }

      return newAttacks;
   }

   static {
      GSON = BaseStatsLoader.GSON;
      allBaseStats = Maps.newEnumMap(EnumSpecies.class);
   }

   public static enum SoundType {
      Neutral,
      Male,
      Female;
   }
}
