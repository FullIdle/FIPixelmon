package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import com.pixelmonmod.pixelmon.api.events.MegaEvolutionEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonFaintEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonKnockoutEvent;
import com.pixelmonmod.pixelmon.api.events.battles.UseBattleItemEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.BattleDamageSource;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.attacks.TargetingInfo;
import com.pixelmonmod.pixelmon.battles.attacks.ZMove;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.AttackModifierBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.CriticalHit;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.MultipleHit;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.Recoil;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.BatonPass;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.BeatUp;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.DragonDarts;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.FalseSwipe;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TripleAxel;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TripleKick;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnSpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackAction;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.BagItemAction;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.log.SwitchAction;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.ProtectVariation;
import com.pixelmonmod.pixelmon.battles.status.PsychicTerrain;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Substitute;
import com.pixelmonmod.pixelmon.battles.tasks.HPIncreaseTask;
import com.pixelmonmod.pixelmon.battles.tasks.RaidShieldsTask;
import com.pixelmonmod.pixelmon.battles.tasks.StatusUpdateTask;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.Dynamax;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.MegaEvolve;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UpdateMoveset;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UseItem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UseZMove;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Comatose;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Corrosion;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Illusion;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Imposter;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Levitate;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Multitype;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RKSSystem;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Trace;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BattleStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.TempBattleLevel;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.RecoilStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.EnumUrshifu;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.ItemMemory;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBlankTechnicalMachine;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.items.heldItems.ItemOrb;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.ItemZCrystal;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class PixelmonWrapper {
   public EntityPixelmon entity;
   public Pokemon pokemon;
   public Pokemon initialCopyOfPokemon;
   private BattleParticipant participant;
   public BattleControllerBase bc;
   public Attack attack;
   public Attack selectedAttack;
   public Attack lastAttack;
   public Attack lastSimulatedAttack;
   public Attack lastTempAttack;
   public Attack lastSimulatedTempAttack;
   public List lastTargets;
   public List lastSimulatedTargets;
   public int targetIndex;
   public Set usedMoves;
   public int escapeAttempts;
   public int damageTakenThisTurn;
   public float priority;
   public boolean canAttack;
   public boolean willTryFlee;
   public boolean isSwitching;
   public boolean willEvolve;
   public boolean nextSwitchIsMove;
   public UUID willUseItemPokemon;
   public ItemStack willUseItemInStack;
   public int willUseItemInStackInfo;
   public boolean wait;
   public int battlePosition;
   private int partyPosition;
   private double[] basePos;
   private List initialType;
   public EnumType addedType;
   public AbilityBase tempAbility;
   public Moveset temporaryMoveset;
   private boolean returnHeldItem;
   public List targets;
   public UUID newPokemonUUID;
   public boolean inMultipleHit;
   public boolean inParentalBond;
   public int protectsInARow;
   public boolean hasAwardedExp;
   private Set attackers;
   public boolean switchedThisTurn;
   public boolean changeBurmy;
   public Attack choiceLocked;
   public boolean choiceSwapped;
   public int metronomeBoost;
   private ItemHeld consumedHeldItem;
   public boolean eatenBerry;
   public boolean eatingBerry;
   public int lastDirectDamage;
   public int lastHP;
   public AttackCategory lastDirectCategory;
   public int lastDirectPosition;
   public EvolutionQuery evolution;
   private boolean tempLevel;
   protected Level temporaryLevel;
   int startHealth;
   private BattleStats battleStats;
   private List status;
   public List type;
   public boolean isMega;
   public int isDynamax;
   public int dynamaxTurns;
   public int dynamaxAnimationTicks;
   public int prevForm;
   public static final int dynamaxAnimationTicksUp = 50;
   public static final float dynamaxAnimationTicksUpAmount = 0.02F;
   public static final int dynamaxAnimationTicksDown = 50;
   public static final float dynamaxAnimationTicksDownAmount = 0.02F;
   public boolean animateHP;
   public boolean usingZ;
   public ZMove zMove;
   public boolean ignoringZCrystal;
   public boolean skipZConvert;
   public int shields;
   public int maxShields;

   private PixelmonWrapper(BattleParticipant participant) {
      this.lastTargets = new ArrayList();
      this.lastSimulatedTargets = new ArrayList();
      this.usedMoves = new HashSet();
      this.escapeAttempts = 0;
      this.damageTakenThisTurn = 0;
      this.canAttack = true;
      this.willTryFlee = false;
      this.isSwitching = false;
      this.willEvolve = false;
      this.initialType = null;
      this.tempAbility = null;
      this.temporaryMoveset = null;
      this.returnHeldItem = false;
      this.targets = new ArrayList();
      this.inMultipleHit = false;
      this.inParentalBond = false;
      this.protectsInARow = 0;
      this.hasAwardedExp = false;
      this.attackers = new HashSet();
      this.switchedThisTurn = false;
      this.changeBurmy = false;
      this.choiceSwapped = false;
      this.consumedHeldItem = NoItem.noItem;
      this.eatenBerry = false;
      this.eatingBerry = false;
      this.lastDirectDamage = -1;
      this.lastHP = -1;
      this.evolution = null;
      this.temporaryLevel = null;
      this.battleStats = new BattleStats(this);
      this.status = new ArrayList();
      this.type = new ArrayList();
      this.isDynamax = 0;
      this.dynamaxTurns = 3;
      this.dynamaxAnimationTicks = 0;
      this.prevForm = -1;
      this.animateHP = true;
      this.usingZ = false;
      this.zMove = null;
      this.ignoringZCrystal = false;
      this.skipZConvert = false;
      this.shields = 0;
      this.maxShields = 0;
      this.participant = participant;
   }

   public PixelmonWrapper(BattleParticipant participant, EntityPixelmon entity, int partyPosition) {
      this(participant);
      this.pokemon = entity.getPokemonData();
      this.entity = entity;
      this.entity.setPixelmonWrapper(this);
      this.loadFromPokemon(this.pokemon);
      this.isMega = entity.getSpecies().hasMega() && entity.getBossMode().isMegaBossPokemon();
   }

   public PixelmonWrapper(BattleParticipant participant, EntityPixelmon pixelmon, int partyPosition, BattleControllerBase bc) {
      this(participant, pixelmon, partyPosition);
      this.entity.battleController = bc;
      this.bc = bc;
   }

   public PixelmonWrapper(BattleParticipant participant, Pokemon pokemon, int partyPosition) {
      this(participant);
      this.pokemon = pokemon;
      this.loadFromPokemon(pokemon);
      this.isMega = false;
      this.isDynamax = 0;
      this.partyPosition = partyPosition;
   }

   public PixelmonWrapper(PixelmonWrapper pw) {
      this.lastTargets = new ArrayList();
      this.lastSimulatedTargets = new ArrayList();
      this.usedMoves = new HashSet();
      this.escapeAttempts = 0;
      this.damageTakenThisTurn = 0;
      this.canAttack = true;
      this.willTryFlee = false;
      this.isSwitching = false;
      this.willEvolve = false;
      this.initialType = null;
      this.tempAbility = null;
      this.temporaryMoveset = null;
      this.returnHeldItem = false;
      this.targets = new ArrayList();
      this.inMultipleHit = false;
      this.inParentalBond = false;
      this.protectsInARow = 0;
      this.hasAwardedExp = false;
      this.attackers = new HashSet();
      this.switchedThisTurn = false;
      this.changeBurmy = false;
      this.choiceSwapped = false;
      this.consumedHeldItem = NoItem.noItem;
      this.eatenBerry = false;
      this.eatingBerry = false;
      this.lastDirectDamage = -1;
      this.lastHP = -1;
      this.evolution = null;
      this.temporaryLevel = null;
      this.battleStats = new BattleStats(this);
      this.status = new ArrayList();
      this.type = new ArrayList();
      this.isDynamax = 0;
      this.dynamaxTurns = 3;
      this.dynamaxAnimationTicks = 0;
      this.prevForm = -1;
      this.animateHP = true;
      this.usingZ = false;
      this.zMove = null;
      this.ignoringZCrystal = false;
      this.skipZConvert = false;
      this.shields = 0;
      this.maxShields = 0;
      this.addedType = pw.addedType;
      this.animateHP = pw.animateHP;
      this.battlePosition = pw.battlePosition;
      this.canAttack = pw.canAttack;
      this.changeBurmy = pw.changeBurmy;
      this.choiceSwapped = pw.choiceSwapped;
      this.damageTakenThisTurn = pw.damageTakenThisTurn;
      this.dynamaxAnimationTicks = pw.dynamaxAnimationTicks;
      this.dynamaxTurns = pw.dynamaxTurns;
      this.eatenBerry = pw.eatenBerry;
      this.eatingBerry = pw.eatingBerry;
      this.escapeAttempts = pw.escapeAttempts;
      this.hasAwardedExp = pw.hasAwardedExp;
      this.ignoringZCrystal = pw.ignoringZCrystal;
      this.inMultipleHit = pw.inMultipleHit;
      this.inParentalBond = pw.inParentalBond;
      this.isDynamax = pw.isDynamax;
      this.isMega = pw.isMega;
      this.isSwitching = pw.isSwitching;
      this.lastDirectDamage = pw.lastDirectDamage;
      this.lastDirectPosition = pw.lastDirectPosition;
      this.lastHP = pw.lastHP;
      this.maxShields = pw.maxShields;
      this.metronomeBoost = pw.metronomeBoost;
      this.nextSwitchIsMove = pw.nextSwitchIsMove;
      this.partyPosition = pw.partyPosition;
      this.prevForm = pw.prevForm;
      this.priority = pw.priority;
      this.protectsInARow = pw.protectsInARow;
      this.returnHeldItem = pw.returnHeldItem;
      this.shields = pw.shields;
      this.skipZConvert = pw.skipZConvert;
      this.startHealth = pw.startHealth;
      this.switchedThisTurn = pw.switchedThisTurn;
      this.targetIndex = pw.targetIndex;
      this.tempLevel = pw.tempLevel;
      this.usingZ = pw.usingZ;
      this.wait = pw.wait;
      this.willEvolve = pw.willEvolve;
      this.willTryFlee = pw.willTryFlee;
      this.willUseItemInStackInfo = pw.willUseItemInStackInfo;
      this.attack = pw.attack;
      this.battleStats = pw.battleStats;
      this.bc = pw.bc;
      this.choiceLocked = pw.choiceLocked;
      this.consumedHeldItem = pw.consumedHeldItem;
      this.entity = pw.entity;
      this.evolution = pw.evolution;
      this.initialCopyOfPokemon = pw.initialCopyOfPokemon;
      this.lastAttack = pw.lastAttack;
      this.lastSimulatedAttack = pw.lastSimulatedAttack;
      this.lastTempAttack = pw.lastTempAttack;
      this.lastSimulatedTempAttack = pw.lastSimulatedTempAttack;
      this.lastDirectCategory = pw.lastDirectCategory;
      this.lastTargets = pw.lastTargets;
      this.lastSimulatedTargets = pw.lastSimulatedTargets;
      this.newPokemonUUID = pw.newPokemonUUID;
      this.participant = pw.participant;
      this.pokemon = pw.pokemon;
      this.selectedAttack = pw.selectedAttack;
      this.tempAbility = pw.tempAbility;
      this.temporaryLevel = pw.temporaryLevel;
      this.temporaryMoveset = pw.temporaryMoveset;
      this.willUseItemInStack = pw.willUseItemInStack;
      this.willUseItemPokemon = pw.willUseItemPokemon;
      this.zMove = pw.zMove;
      this.attackers = new HashSet(pw.attackers.size());
      this.attackers.addAll(pw.attackers);
      this.basePos = (double[])pw.basePos.clone();
      this.initialType = new ArrayList(pw.initialType.size());
      this.initialType.addAll(pw.initialType);
      this.status = new ArrayList(pw.status.size());
      this.status.addAll(pw.status);
      this.targets = new ArrayList(pw.targets.size());
      this.targets.addAll(pw.targets);
      this.type = new ArrayList(pw.type.size());
      this.type.addAll(pw.type);
      this.usedMoves = new HashSet(pw.usedMoves.size());
      this.usedMoves.addAll(pw.usedMoves);
   }

   private void loadFromPokemon(Pokemon pokemon) {
      this.isMega = false;
      this.isDynamax = 0;
      this.dynamaxAnimationTicks = 0;
      this.type = new ArrayList(pokemon.getBaseStats().types);
      this.initialCopyOfPokemon = Pixelmon.pokemonFactory.create(pokemon.writeToNBT(new NBTTagCompound()));
      if (pokemon.getStatus() != NoStatus.noStatus) {
         this.getStatuses().add(pokemon.getStatus());
      }

   }

   public BaseStats getBaseStats() {
      return this.pokemon.getBaseStats();
   }

   public Stats getStats() {
      return this.pokemon.getStats();
   }

   public BattleStats getBattleStats() {
      return this.battleStats;
   }

   public List getInitialType() {
      return this.initialType;
   }

   public int getFriendship() {
      return this.pokemon.getFriendship();
   }

   public Set getAttackers() {
      return this.attackers;
   }

   public Optional getPokerus() {
      return Optional.ofNullable(this.getInnerLink().getPokerus());
   }

   public boolean hasType(EnumType... types) {
      EnumType[] var2 = types;
      int var3 = types.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumType type = var2[var4];
         if (this.type.contains(type)) {
            return true;
         }
      }

      return false;
   }

   public void clearTurnVariables() {
      this.canAttack = true;
      this.willTryFlee = false;
      if (this.isAlive()) {
         this.isSwitching = false;
      }

      this.switchedThisTurn = false;
   }

   public void setMoveTargets(PixelmonWrapper... pokemon) {
      this.targets.clear();
      Collections.addAll(this.targets, pokemon);
   }

   public void selectAIAction() {
      if (this.attack == null || !this.attack.doesPersist(this)) {
         if (this.bc == null) {
            this.bc = this.participant.bc;
         }

         this.chooseMove();
      }

   }

   public void chooseMove() {
      this.chooseMove(this.participant.getMove(this));
   }

   public void chooseMove(MoveChoice moveChoice) {
      if (moveChoice != null) {
         if (moveChoice.isAttack()) {
            this.setAttack(moveChoice.attack, moveChoice.targets, true);
         } else {
            this.bc.switchPokemon(this.getPokemonUUID(), moveChoice.switchPokemon, false);
         }
      }

   }

   public ArrayList getTargets(Attack chosenAttack) {
      ArrayList targets = new ArrayList();
      if (chosenAttack == null) {
         return targets;
      } else {
         TargetingInfo info = chosenAttack.getMove().getTargetingInfo();
         ArrayList teamPokemon = this.bc.getTeamPokemon(this.participant);
         ArrayList opponentPokemon = this.bc.getOpponentPokemon(this.participant);
         boolean[][] targetted = new boolean[][]{new boolean[teamPokemon.size()], new boolean[opponentPokemon.size()]};
         int mypos = teamPokemon.indexOf(this);
         if (this.bc.rules.battleType == EnumBattleType.Horde && info.hitsAll && info.hitsOppositeFoe && info.hitsAdjacentFoe) {
            targets.addAll(opponentPokemon);
            return targets;
         } else {
            int j;
            if (info.hitsAll) {
               if (info.hitsOppositeFoe && targetted[1].length > mypos) {
                  targetted[1][mypos] = true;
               }

               if (info.hitsAdjacentFoe) {
                  if (mypos - 1 >= 0) {
                     targetted[1][mypos - 1] = true;
                  }

                  if (mypos + 1 < targetted[1].length) {
                     targetted[1][mypos + 1] = true;
                  }
               }

               if (info.hitsExtendedFoe) {
                  if (mypos - 2 >= 0) {
                     targetted[1][mypos - 2] = true;
                  }

                  if (mypos + 2 < targetted[1].length) {
                     targetted[1][mypos + 2] = true;
                  }
               }

               if (info.hitsSelf) {
                  targetted[0][mypos] = true;
               }

               if (info.hitsAdjacentAlly) {
                  if (mypos - 1 >= 0) {
                     targetted[0][mypos - 1] = true;
                  }

                  if (mypos + 1 < targetted[0].length) {
                     targetted[0][mypos + 1] = true;
                  }
               }

               if (info.hitsExtendedAlly) {
                  if (mypos - 2 >= 0) {
                     targetted[0][mypos - 2] = true;
                  }

                  if (mypos + 2 < targetted[0].length) {
                     targetted[0][mypos + 2] = true;
                  }
               }

               if (this.bc.isRaid() && info.hitsSelf && info.hitsAdjacentAlly && info.hitsExtendedAlly) {
                  if (mypos - 3 >= 0) {
                     targetted[0][mypos - 3] = true;
                  }

                  if (mypos + 3 < targetted[0].length) {
                     targetted[0][mypos + 3] = true;
                  }
               }

               for(int i = 0; i < 2; ++i) {
                  for(j = 0; j < targetted[i].length; ++j) {
                     if (targetted[i][j]) {
                        if (i == 0) {
                           targets.add(teamPokemon.get(j));
                        } else {
                           targets.add(opponentPokemon.get(j));
                        }
                     }
                  }
               }
            } else {
               ArrayList tempTargets = new ArrayList();
               if (info.hitsSelf) {
                  tempTargets.add(this);
               }

               if (info.hitsAdjacentAlly) {
                  if (mypos > 0 && teamPokemon.size() > mypos - 1) {
                     tempTargets.add(teamPokemon.get(mypos - 1));
                  }

                  if (mypos < teamPokemon.size() - 1) {
                     tempTargets.add(teamPokemon.get(mypos + 1));
                  }
               }

               if (info.hitsExtendedAlly) {
                  if (mypos > 1 && teamPokemon.size() > mypos - 2) {
                     tempTargets.add(teamPokemon.get(mypos - 2));
                  }

                  if (mypos < teamPokemon.size() - 2) {
                     tempTargets.add(teamPokemon.get(mypos + 2));
                  }
               }

               if (info.hitsOppositeFoe && mypos < opponentPokemon.size()) {
                  tempTargets.add(opponentPokemon.get(mypos));
               }

               if (info.hitsAdjacentFoe) {
                  if (mypos > 0 && opponentPokemon.size() > mypos - 1) {
                     tempTargets.add(opponentPokemon.get(mypos - 1));
                  }

                  if (mypos < opponentPokemon.size() - 1) {
                     tempTargets.add(opponentPokemon.get(mypos + 1));
                  }
               }

               if (info.hitsExtendedFoe) {
                  if (mypos > 1 && opponentPokemon.size() > mypos - 2) {
                     tempTargets.add(opponentPokemon.get(mypos - 2));
                  }

                  if (mypos < opponentPokemon.size() - 2) {
                     tempTargets.add(opponentPokemon.get(mypos + 2));
                  }
               }

               for(j = 0; j < tempTargets.size(); ++j) {
                  if (tempTargets.get(j) == null) {
                     tempTargets.remove(j);
                     --j;
                  }
               }

               ArrayList invalid = new ArrayList();
               if (!tempTargets.isEmpty()) {
                  if (info.hitsOppositeFoe || info.hitsExtendedFoe || info.hitsAdjacentFoe) {
                     invalid.addAll((Collection)tempTargets.stream().filter((pw) -> {
                        return pw.getParticipant().team == this.getParticipant().team || pw.isFainted();
                     }).collect(Collectors.toList()));
                  }

                  tempTargets.removeAll(invalid);
                  if (!tempTargets.isEmpty()) {
                     targets.add(tempTargets.get(RandomHelper.getRandomNumberBetween(0, tempTargets.size() - 1)));
                  }
               }
            }

            return targets;
         }
      }
   }

   public void useAttack() {
      this.selectedAttack = this.attack;
      if (!this.bc.simulateMode) {
         Iterator var1 = this.bc.participants.iterator();

         while(var1.hasNext()) {
            BattleParticipant participant = (BattleParticipant)var1.next();
            participant.getBattleAI().registerMove(this);
         }
      }

      this.useAttack(true);
   }

   public void useAttack(boolean affectPP) {
      if (this.attack != null && !this.isFainted()) {
         if (this.participant.bc.oldGen.isYes() && this.participant.canMegaEvolve()) {
            if (this.usingZ && this.zMove == null) {
               this.zMove = this.attack.getMove().getZMove(this.pokemon, this.ignoringZCrystal);
            }

            if (this.zMove == null) {
               this.usingZ = false;
            }
         }

         this.usedMoves.add(this.attack);
         this.getBattleAbility().startMove(this);
         if (!this.attack.isAttack("Focus Punch") && !this.hasStatus(StatusType.Bide)) {
            this.bc.sendToAll("pixelmon.battletext.used", this.getNickname(), this.usingZ ? this.zMove.getLocalizedName() : this.attack.getMove().getTranslatedName());
         }

         boolean reducePP = false;
         PixelmonWrapper firstPokemon;
         if (this.bc.rules.battleType.numPokemon > 1 && !this.attack.getMove().getTargetingInfo().hitsAll) {
            if (!this.attack.isAttack("Me First") && !this.attack.getMove().getTargetingInfo().hitsAdjacentAlly && this.attack.getMove().getTargetingInfo().hitsAdjacentFoe && this.attack.getMove().getTargetingInfo().hitsOppositeFoe) {
               this.targets = this.getTargets(this.attack);
            } else if (this.targets.size() > 1) {
               firstPokemon = (PixelmonWrapper)this.targets.get(0);
               this.targets.clear();
               this.targets.add(firstPokemon);
            }
         }

         ArrayList correctedTargets;
         PixelmonWrapper target;
         if (this.attack.isAttack("Expanding Force") && this.bc.globalStatusController.getTerrain() instanceof PsychicTerrain) {
            firstPokemon = (PixelmonWrapper)this.targets.get(0);
            if (!this.isAlly(firstPokemon)) {
               this.targets.clear();
               correctedTargets = this.bc.getAdjacentPokemon(this);
               Iterator var5 = correctedTargets.iterator();

               while(var5.hasNext()) {
                  target = (PixelmonWrapper)var5.next();
                  if (!this.isAlly(target)) {
                     this.targets.add(target);
                  }
               }
            }
         }

         if (this.bc.rules.battleType == EnumBattleType.Horde && this.attack.getMove().getTargetingInfo().hitsAll && this.attack.getMove().getTargetingInfo().hitsOppositeFoe && this.attack.getMove().getTargetingInfo().hitsAdjacentFoe) {
            this.targets = this.getTargets(this.attack);
         }

         List deadPokes = new ArrayList();
         correctedTargets = new ArrayList();
         if (!this.attack.canHitNoTarget()) {
            this.targets.stream().filter(PixelmonWrapper::isFainted).forEach((targetx) -> {
               deadPokes.add(targetx);
               if (!targetx.isSameTeam(this) && this.targets.size() == 1) {
                  Iterator var4 = targetx.getTeamPokemon().iterator();

                  while(true) {
                     PixelmonWrapper otherTarget;
                     do {
                        do {
                           if (!var4.hasNext()) {
                              return;
                           }

                           otherTarget = (PixelmonWrapper)var4.next();
                        } while(otherTarget.isFainted());
                     } while(correctedTargets.stream().count() >= 1L && !this.attack.getMove().getTargetingInfo().hitsAll);

                     correctedTargets.add(otherTarget);
                  }
               }
            });
            List var10001 = this.targets;
            deadPokes.forEach(var10001::remove);
            this.targets.addAll((Collection)correctedTargets.stream().collect(Collectors.toList()));
         }

         this.targets = CalcPriority.getTurnOrder(this.targets);
         MoveResults[] arr = new MoveResults[this.targets.size()];
         this.attack.hasPlayedAnimationOnce = false;

         for(this.targetIndex = 0; this.targetIndex < this.targets.size(); ++this.targetIndex) {
            target = (PixelmonWrapper)this.targets.get(this.targetIndex);
            MoveResults results = new MoveResults(target, 0, this.priority, AttackResult.proceed);
            this.attack.saveAttack();
            reducePP = this.attack.use(this, target, results, this.usingZ ? this.zMove : null) || reducePP;
            CriticalHit critModifier = null;
            Iterator var9 = (new ArrayList(this.attack.getMove().effects)).iterator();

            EffectBase effect;
            while(var9.hasNext()) {
               effect = (EffectBase)var9.next();
               if (effect instanceof AttackModifierBase && effect instanceof CriticalHit) {
                  critModifier = (CriticalHit)effect;
               }
            }

            if (this.attack.didCrit) {
               ++this.pokemon.lastBattleCrits;
            }

            if (this.targetIndex == this.targets.size() - 1) {
               var9 = this.attack.getMove().effects.iterator();

               while(var9.hasNext()) {
                  effect = (EffectBase)var9.next();
                  effect.applyEffectAfterAllTargets(this);
               }
            }

            this.attack.restoreAttack();
            this.attack.resetOverridePower();
            if (this.targetIndex >= arr.length) {
               arr = (MoveResults[])Arrays.copyOf(arr, this.targetIndex + 1);
            }

            if (arr[this.targetIndex] == null) {
               arr[this.targetIndex] = results;
            }
         }

         this.attack.hasPlayedAnimationOnce = false;
         boolean hadTarget = false;
         boolean ignore = false;
         MoveResults[] var16 = arr;
         int var19 = arr.length;

         for(int var21 = 0; var21 < var19; ++var21) {
            MoveResults result = var16[var21];
            if (result != null) {
               if (result.result == AttackResult.ignore) {
                  ignore = true;
               }

               if (result.result != AttackResult.notarget) {
                  hadTarget = true;
               }

               if (result.result != AttackResult.charging && result.result != AttackResult.ignore) {
                  if (!this.bc.simulateMode) {
                     this.lastAttack = new Attack(this.attack);
                     this.lastTargets = new ArrayList(this.targets.size());
                     this.lastTargets.addAll(this.targets);
                     this.bc.lastAttack = new Attack(this.attack);
                     if (this.attack != this.selectedAttack) {
                        this.lastTempAttack = new Attack(this.attack);
                        this.bc.lastTempAttack = new Attack(this.attack);
                     }
                  } else {
                     this.lastSimulatedAttack = new Attack(this.attack);
                     this.lastSimulatedTargets = new ArrayList(this.targets.size());
                     this.lastSimulatedTargets.addAll(this.targets);
                     this.bc.lastSimulatedAttack = new Attack(this.attack);
                     if (this.attack != this.selectedAttack) {
                        this.lastSimulatedTempAttack = new Attack(this.attack);
                        this.bc.lastSimulatedTempAttack = new Attack(this.attack);
                     }
                  }
               }
            }
         }

         if (!hadTarget && this.hasStatus(StatusType.MultiTurn)) {
            Iterator var17 = this.attack.getMove().effects.iterator();

            while(var17.hasNext()) {
               EffectBase effect = (EffectBase)var17.next();
               effect.applyMissEffect(this, this);
            }
         }

         if (!ignore) {
            this.bc.battleLog.addEvent(new AttackAction(this.bc.battleTurn, this.bc.getPositionOfPokemon(this), this, this.attack.getMove(), arr));
         }

         if (reducePP && affectPP && this.attack.pp > 0) {
            if (this.attack.isMax) {
               --this.attack.originalMove.pp;
            } else {
               --this.attack.pp;
            }

            this.setTemporaryMoveset(this.temporaryMoveset);
         }

         if (this.participant.bc.oldGen.isYes() && this.participant.canMegaEvolve() && this.usingZ) {
            this.participant.usedZ = true;
            if (this.participant.getType() == ParticipantType.Player) {
               PlayerParticipant player = (PlayerParticipant)this.participant;
               Pixelmon.network.sendTo(new UseZMove(), player.player);
            }

            this.zMove = null;
         }

         this.participant.onUseAttackPost(this.bc, this);
      }
   }

   public MoveResults[] useAttackOnly() {
      MoveResults saveResult = this.attack.moveResult;
      MoveResults[] resultsArray = new MoveResults[this.targets.size()];

      for(this.targetIndex = 0; this.targetIndex < this.targets.size(); ++this.targetIndex) {
         PixelmonWrapper target = (PixelmonWrapper)this.targets.get(this.targetIndex);
         resultsArray[this.targetIndex] = new MoveResults(target, 0, this.priority, AttackResult.proceed);
         this.attack.saveAttack();
         this.attack.use(this, target, resultsArray[this.targetIndex]);
         this.attack.restoreAttack();
      }

      this.attack.moveResult = saveResult;
      return resultsArray;
   }

   public void useItem() {
      PixelmonItem item = null;
      EntityPlayerMP user = null;
      ItemStack usedStack = null;
      int additionalInfo = false;
      PlayerParticipant playerPart = (PlayerParticipant)this.participant;
      user = playerPart.player;
      PartyStorage party = playerPart.getStorage();
      if (party != null) {
         PixelmonWrapper target = this;
         if (this.willUseItemInStackInfo == -1) {
            target = this.getParticipant().getPokemonFromParty(this.willUseItemPokemon);
         }

         usedStack = this.willUseItemInStack;
         int additionalInfo = this.willUseItemInStackInfo;
         this.willUseItemInStack = null;
         this.willUseItemInStackInfo = 0;
         item = (PixelmonItem)usedStack.func_77973_b();
         boolean isPokeBall = item instanceof ItemPokeball;
         if (isPokeBall && this.bc.getOpponentPokemon(this.participant).size() >= 1) {
            Iterator var9 = this.bc.getOpponentPokemon(this.participant).iterator();

            while(var9.hasNext()) {
               PixelmonWrapper opponent = (PixelmonWrapper)var9.next();
               if (opponent.isAlive()) {
                  target = opponent;
                  break;
               }
            }
         }

         UseBattleItemEvent event = new UseBattleItemEvent(this, target, usedStack, additionalInfo);
         if (Pixelmon.EVENT_BUS.post(event)) {
            this.bc.getPlayers().forEach((pp) -> {
               pp.wait = false;
            });
         } else {
            item = (PixelmonItem)event.stack.func_77973_b();
            additionalInfo = event.additionalInfo;
            if (target != null) {
               this.bc.battleLog.addEvent(new BagItemAction(this.bc.battleTurn, this.bc.getPositionOfPokemon(this), this, target.getPokemonName(), item));
            }

            if (isPokeBall) {
               this.bc.sendToAll("pixelmon.pokeballs.throw", this.participant.getDisplayName(), item.getLocalizedName());
            } else {
               this.bc.sendToAll("pixelmon.items.useitem", this.participant.getDisplayName(), item.getLocalizedName());
            }

            if (!isPokeBall && this.hasStatus(StatusType.Embargo)) {
               this.bc.sendToAll("pixelmon.status.embargo", this.getNickname());
            } else if (item.useFromBag(this, target, additionalInfo)) {
               user.field_71071_by.func_174925_a(item, usedStack.func_77960_j(), 1, (NBTTagCompound)null);
               Pixelmon.network.sendTo(new UseItem(item), user);
            }

         }
      }
   }

   public void useTempAttack(Attack tempAttack) {
      if (tempAttack != null) {
         this.useTempAttack(tempAttack, this.getTargets(tempAttack), false);
      }

   }

   public void useTempAttack(Attack tempAttack, PixelmonWrapper target) {
      this.useTempAttack(tempAttack, (List)Lists.newArrayList(new PixelmonWrapper[]{target}));
   }

   public void useTempAttack(Attack tempAttack, List targets) {
      this.useTempAttack(tempAttack, targets, false);
   }

   public void useTempAttack(Attack tempAttack, List targets, boolean affectPP) {
      this.targets = targets;
      this.selectedAttack = this.attack;
      this.attack = tempAttack;
      if (this.usingZ) {
         this.ignoringZCrystal = true;
         if (this.zMove == null) {
            this.zMove = tempAttack.getMove().getZMove(this.pokemon, true);
         }
      }

      if (!this.hasStatus(StatusType.Flinch) || this.getStatus(StatusType.Flinch).canAttackThisTurn(this, tempAttack)) {
         this.useAttack(affectPP);
         Iterator var4 = this.attack.getMove().effects.iterator();

         while(var4.hasNext()) {
            EffectBase effect = (EffectBase)var4.next();
            if (effect instanceof MultiTurnSpecialAttackBase) {
               --this.selectedAttack.pp;
               return;
            }
         }
      }

      this.ignoringZCrystal = false;
      this.attack = this.selectedAttack;
   }

   public void turnTick() {
      if (this.bc == null) {
         this.bc = this.participant.bc;
      }

      this.returnToBasePos();
      Moveset moveset = this.getMoveset();

      for(int i = 0; i < moveset.size(); ++i) {
         moveset.get(i).setDisabled(false, this);
      }

      this.checkSkyBattleDisable();
      boolean isActive = this.isAlive() && this.bc != null && !this.bc.battleEnded;
      if (isActive) {
         this.getBattleAbility().applyRepeatedEffect(this);
      }

      ItemHeld usableItem = this.getUsableHeldItem();
      if (isActive && this.isAlive()) {
         usableItem.applyRepeatedEffect(this);
      }

      for(int i = 0; i < this.getStatusSize(); ++i) {
         StatusBase s = this.getStatus(i);

         try {
            if (this.bc != null && !this.bc.battleEnded && (this.isAlive() || s.isTeamStatus())) {
               int beforeSize = this.getStatusSize();
               s.applyRepeatedEffect(this);
               if (beforeSize > this.getStatusSize()) {
                  --i;
               }
            }
         } catch (Exception var7) {
            this.bc.battleLog.onCrash(var7, "Error calculating applyRepeatedEffect() for " + s.type.toString());
         }
      }

      if (isActive && this.isAlive()) {
         usableItem.applyRepeatedEffectAfterStatus(this);
         this.getBattleAbility().applyRepeatedEffectAfterStatus(this);
      }

      this.damageTakenThisTurn = 0;
      boolean resetProtect = this.attack == null;
      if (!resetProtect) {
         resetProtect = true;
         Iterator var10 = this.attack.getMove().effects.iterator();

         while(var10.hasNext()) {
            EffectBase e = (EffectBase)var10.next();
            if (e instanceof ProtectVariation) {
               resetProtect = false;
               break;
            }
         }
      }

      if (resetProtect) {
         this.protectsInARow = 0;
      }

      this.switchedThisTurn = false;
      this.lastDirectDamage = -1;
      this.lastHP = -1;
   }

   public BattleParticipant getParticipant() {
      return this.participant;
   }

   public PixelmonWrapper doSwitch() {
      if (!this.bc.simulateMode) {
         this.isSwitching = false;
         this.canAttack = false;
         if (this.isFainted()) {
            this.resetBattleEvolution();
            if (this.isMega) {
               this.isMega = false;
            }

            if (this.isDynamax > 0) {
               this.dynamax(true, this.getHealthPercent());
            }
         }
      }

      ArrayList statusCopy = new ArrayList();
      boolean batonPassing = false;
      BattleStats tempStats = new BattleStats(this);
      boolean wasSimulateMode = this.bc.simulateMode;
      this.bc.simulateMode = false;
      tempStats.copyStats(this.getBattleStats());
      this.bc.simulateMode = wasSimulateMode;
      if (this.nextSwitchIsMove && this.attack != null && this.attack.isAttack("Baton Pass")) {
         batonPassing = true;
      }

      if (!this.bc.simulateMode) {
         for(int i = 0; i < this.getStatusSize(); ++i) {
            try {
               StatusBase status = this.getStatus(i);
               if (!status.isTeamStatus() && (!batonPassing || !BatonPass.isBatonPassable(status))) {
                  if (!StatusType.isPrimaryStatus(status.type)) {
                     this.removeStatus(status, false);
                     --i;
                  }
               } else {
                  statusCopy.add(status);
               }
            } catch (Exception var10) {
               this.bc.battleLog.onCrash(var10, "Error in doSwitch().");
            }
         }
      }

      UUID oldUUID = this.getPokemonUUID();
      if (!this.bc.simulateMode) {
         this.update(EnumUpdateType.Status);
      }

      if (this.newPokemonUUID == null) {
         return null;
      } else {
         PixelmonWrapper newPokemon = this.participant.switchPokemon(this, this.newPokemonUUID);
         this.newPokemonUUID = null;
         if (newPokemon.entity != null) {
            newPokemon.entity.func_70606_j((float)newPokemon.getHealth());
            newPokemon.entity.battleController = this.bc;
         }

         newPokemon.battlePosition = this.battlePosition;
         newPokemon.switchedThisTurn = true;
         if (!this.bc.simulateMode) {
            newPokemon.isDynamax = 0;
            newPokemon.dynamaxTurns = 0;
            newPokemon.dynamaxAnimationTicks = 0;
         }

         if (newPokemon.getPokemonUUID().equals(this.participant.evolution)) {
            ItemMegaStone megaStone = newPokemon.getHeldItem() instanceof ItemMegaStone ? (ItemMegaStone)newPokemon.getHeldItem() : null;
            newPokemon.isMega = true;
            int form = megaStone == null ? 1 : megaStone.getForm(newPokemon.getForm());
            newPokemon.setForm(form);
         }

         if (!this.bc.simulateMode) {
            this.bc.sendSwitchPacket(oldUUID, newPokemon);
         }

         if (batonPassing) {
            this.bc.simulateMode = false;
            newPokemon.getBattleStats().copyStats(tempStats);
            this.bc.simulateMode = wasSimulateMode;
         }

         if (this.attack != null && this.attack.moveResult != null && !this.bc.simulateMode && (this.attack.moveResult.result == AttackResult.succeeded || this.attack.moveResult.result == AttackResult.proceed) && this.usingZ && this.attack.isAttack("Memento", "Parting Shot")) {
            newPokemon.healByPercent(100.0F);
         }

         Iterator var13;
         if (!statusCopy.isEmpty()) {
            var13 = statusCopy.iterator();

            while(var13.hasNext()) {
               StatusBase e = (StatusBase)var13.next();
               newPokemon.addStatus(e, newPokemon);
            }

            List entryHazards = new ArrayList();
            Iterator var16 = statusCopy.iterator();

            StatusBase e;
            while(var16.hasNext()) {
               e = (StatusBase)var16.next();
               if (!(e instanceof EntryHazard)) {
                  e.applyEffectOnSwitch(newPokemon);
               } else {
                  entryHazards.add(e);
               }
            }

            var16 = entryHazards.iterator();

            while(var16.hasNext()) {
               e = (StatusBase)var16.next();
               e.applyEffectOnSwitch(newPokemon);
            }
         }

         var13 = this.bc.globalStatusController.getGlobalStatuses().iterator();

         while(var13.hasNext()) {
            GlobalStatusBase status = (GlobalStatusBase)var13.next();
            status.applyEffectOnSwitch(newPokemon);
         }

         newPokemon.afterSwitch();
         if (!this.bc.simulateMode) {
            this.bc.getOpponentPokemon(this.participant).stream().filter((pw) -> {
               return pw.targets.contains(this);
            }).forEach((pw) -> {
               pw.targets.remove(this);
               pw.targets.add(newPokemon);
            });
            this.bc.getTeamPokemon(this.participant).stream().filter((pw) -> {
               return pw != this;
            }).filter((pw) -> {
               return pw.targets.contains(this);
            }).forEach((pw) -> {
               pw.targets.remove(this);
               pw.targets.add(newPokemon);
            });
            newPokemon.addAttackers();
            this.bc.participants.stream().filter((p2) -> {
               return p2.team != this.participant.team;
            }).forEach(BattleParticipant::updateOtherPokemon);
            this.getParticipant().onSwitchIn(this.bc, this);
         }

         return newPokemon;
      }
   }

   public void beforeSwitch(PixelmonWrapper switchingIn) {
      if (!this.bc.simulateMode) {
         this.getBattleStats().clearBattleStats();
         if (this.isAlive()) {
            this.getBattleAbility().applySwitchOutEffect(this);
         }

         for(int i = 0; i < this.getStatusSize(); ++i) {
            StatusBase status = this.getStatus(i);
            if (this.isAlive() || status.isTeamStatus()) {
               status.applySwitchOutEffect(this, switchingIn);
            }
         }

         this.getUsableHeldItem().applySwitchOutEffect(this);
         this.resetOnSwitch();
      }

   }

   public void afterSwitch() {
      if (!this.bc.simulateMode) {
         if (this.bc.oldGen.isYes()) {
            this.checkPrimalReversion();
         }

         this.getBattleAbility().applySwitchInEffect(this);
         Iterator var1 = this.getOpponentPokemon().iterator();

         while(var1.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var1.next();
            pw.getBattleAbility().applyFoeSwitchInEffect(pw, this);
         }

         this.getUsableHeldItem().applySwitchInEffect(this);
         if (this.participant.getType() == ParticipantType.Player && this.getSpecies() == EnumSpecies.Burmy) {
            this.changeBurmy = true;
         }

         this.checkSkyBattleDisable();
      }

   }

   private void checkPrimalReversion() {
      if ((this.getSpecies() == EnumSpecies.Kyogre && this.getHeldItem() == PixelmonItemsHeld.blueOrb || this.getSpecies() == EnumSpecies.Groudon && this.getHeldItem() == PixelmonItemsHeld.redOrb) && this.getForm() != EnumPrimal.PRIMAL.getForm()) {
         this.setForm(EnumPrimal.PRIMAL);
         this.bc.sendToAll("pixelmon.battletext.primalreversion", this.getNickname());
      }

   }

   private void checkSkyBattleDisable() {
      Iterator var1 = this.getMoveset().iterator();

      while(var1.hasNext()) {
         Attack move = (Attack)var1.next();
         if (move != null && !move.checkSkyBattle(this.bc)) {
            move.setDisabled(true, this);
         }
      }

   }

   public void takeTurn() {
      if (this.isSwitching) {
         this.bc.battleLog.addEvent(new SwitchAction(this.bc.battleTurn, this.bc.getPositionOfPokemon(this), this, this.newPokemonUUID));
         this.doSwitch();
      } else if (this.willUseItemInStack != null) {
         this.useItem();
      } else {
         this.canAttack = this.getBattleAbility().canAttackThisTurn(this, this.attack);
         Iterator var1;
         StatusBase e;
         if (this.canAttack) {
            var1 = (new ArrayList(this.status)).iterator();

            while(var1.hasNext()) {
               e = (StatusBase)var1.next();

               try {
                  if (e instanceof StatusPersist && !e.canAttackThisTurn(this, this.attack)) {
                     this.canAttack = false;
                     this.bc.battleLog.addEvent(new AttackAction(this.bc.battleTurn, this.bc.getPositionOfPokemon(this), this, (AttackBase)null, new MoveResults[]{new MoveResults((PixelmonWrapper)null, 0, AttackResult.unable)}));
                     break;
                  }
               } catch (Exception var7) {
                  this.bc.battleLog.onCrash(var7, "Error calculating canAttackThisTurn for " + e.type.toString());
               }
            }
         }

         if (this.canAttack) {
            var1 = (new ArrayList(this.status)).iterator();

            while(var1.hasNext()) {
               e = (StatusBase)var1.next();

               try {
                  if (!(e instanceof StatusPersist) && !e.canAttackThisTurn(this, this.attack)) {
                     this.canAttack = false;
                     this.bc.battleLog.addEvent(new AttackAction(this.bc.battleTurn, this.bc.getPositionOfPokemon(this), this, (AttackBase)null, new MoveResults[]{new MoveResults((PixelmonWrapper)null, 0, AttackResult.unable)}));
                     break;
                  }
               } catch (Exception var6) {
                  this.bc.battleLog.onCrash(var6, "Error calculating canAttackThisTurn for " + e.type.toString());
               }
            }
         }

         EffectBase e;
         if (!this.canAttack) {
            if (this.attack != null && this.hasStatus(StatusType.MultiTurn)) {
               var1 = this.attack.getMove().effects.iterator();

               while(var1.hasNext()) {
                  e = (EffectBase)var1.next();
                  if (e instanceof MultiTurnSpecialAttackBase) {
                     ((MultiTurnSpecialAttackBase)e).removeEffect(this, this);
                  }
               }
            }
         } else {
            this.participant.bc.clearHurtTimer();
            this.getUsableHeldItem().onAttackUsed(this, this.attack);

            for(int i = 0; i < this.status.size(); ++i) {
               e = (StatusBase)this.status.get(i);
               int initialSize = this.status.size();
               e.onAttackUsed(this, this.attack);
               if (initialSize > this.status.size()) {
                  --i;
               }
            }

            if (this.attack != null) {
               if (this.usingZ && this.attack.getAttackCategory() == AttackCategory.STATUS && this.attack.getMove().getZMove(this.pokemon, false) != null) {
                  var1 = this.attack.getMove().getZMove(this.pokemon, false).effects.iterator();

                  while(var1.hasNext()) {
                     e = (EffectBase)var1.next();

                     try {
                        if (e instanceof StatsEffect) {
                           ((StatsEffect)e).applyStatEffect(this, this, this.attack.getMove());
                        } else if (e instanceof SpecialAttackBase) {
                           if (e.applyEffectStart(this, this) == AttackResult.proceed) {
                              ((SpecialAttackBase)e).applyEffectDuring(this, this);
                           }
                        } else if (e instanceof StatusBase) {
                           e.applyEffect(this, this);
                        }
                     } catch (Exception var5) {
                        var5.printStackTrace();
                     }
                  }
               }

               if (!(this.participant instanceof RaidPixelmonParticipant)) {
                  if (this.isDynamax == 1) {
                     this.attack = MaxMoveConverter.getMaxMoveFromAttack(this.attack, this);
                  } else if (this.isDynamax == 2) {
                     this.attack = MaxMoveConverter.getGMaxMoveFromAttack(this.attack, this, this.getSpecies(), this.getFormEnum());
                  }
               }
            }

            if (this.attack != null) {
               boolean shouldContinue = true;
               Iterator var11 = this.bc.participants.iterator();

               while(var11.hasNext()) {
                  BattleParticipant bp = (BattleParticipant)var11.next();
                  if (bp.onUseAttackOther(this.bc, this.attack, this.participant, this)) {
                     shouldContinue = false;
                     --this.attack.pp;
                     this.setTemporaryMoveset(this.temporaryMoveset);
                     break;
                  }
               }

               if (shouldContinue && !this.participant.onUseAttack(this.bc, this)) {
                  this.useAttack();
               }
            }
         }

         var1 = (new ArrayList(this.status)).iterator();

         while(var1.hasNext()) {
            e = (StatusBase)var1.next();
            e.onEndOfTurn(this);
         }

         var1 = this.targets.iterator();

         while(var1.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var1.next();
            Iterator var13 = (new ArrayList(target.status)).iterator();

            while(var13.hasNext()) {
               StatusBase status = (StatusBase)var13.next();
               status.onEndOfAttackersTurn(target, this);
            }
         }
      }

   }

   public boolean dynamax(boolean revert, float healthPercentage) {
      int currentHP = this.getHealth();
      int newHP;
      PlayerParticipant player;
      if (revert) {
         if (this.isDynamax > 0) {
            if (this.isDynamax == 2) {
               this.setForm(this.prevForm);
            }

            this.isDynamax = -1;
            this.dynamaxAnimationTicks = -50;
            this.updateHPIncrease();
            newHP = (int)Math.ceil((double)this.getMaxHealth() * ((double)healthPercentage / 100.0));
            this.setHealth(newHP);
            this.updateBattleDamage(currentHP - newHP);
            if (this.participant.getType() == ParticipantType.Player) {
               player = (PlayerParticipant)this.participant;
               Pixelmon.network.sendTo(new Dynamax(UUID.randomUUID(), false), player.player);
            }

            if (!this.bc.battleEnded) {
               String nickname = this.getNickname();
               this.bc.sendToAll("pixelmon.battletext.dynamaxlost", nickname, this.participant.getDisplayName());
            }
         }
      } else if (!this.bc.battleEnded) {
         this.dynamaxTurns = 3;
         this.dynamaxAnimationTicks = 50;
         this.participant.dynamax = this.getPokemonUUID();
         if (!this.getSpecies().is(EnumSpecies.Shedinja)) {
            this.updateHPIncrease();
            newHP = (int)Math.ceil((double)this.getMaxHealth() * ((double)healthPercentage / 100.0));
            this.setHealth(newHP);
            this.updateBattleDamage(currentHP - newHP);
         }

         String nickname = this.getNickname();
         if (this.participant.getType() == ParticipantType.Player) {
            player = (PlayerParticipant)this.participant;
            Pixelmon.network.sendTo(new Dynamax(this.getPokemonUUID(), this.isDynamax == 2), player.player);
         }

         this.bc.sendToAll("pixelmon.battletext.dynamax", nickname, this.participant.getDisplayName());
      }

      return false;
   }

   public boolean megaEvolve() {
      if (this.bc.simulateMode) {
         return false;
      } else {
         this.willEvolve = false;
         boolean gigantamax;
         if (this.bc.oldGen.isYes() && this.canMegaEvolve() && this.participant.canMegaEvolve()) {
            if (this.canUltraBurst() && this.participant.ultraBurst == null) {
               if (Pixelmon.EVENT_BUS.post(new MegaEvolutionEvent.BattleEvolve(this, (ItemMegaStone)null, true))) {
                  return false;
               }

               this.participant.ultraBurst = this.getPokemonUUID();
               this.bc.sendToAll("pixelmon.battletext.necrozmaultrareact", this.participant.getDisplayName());
               this.pokemon.getPersistentData().func_74768_a("SrcForm", this.pokemon.getForm());
               this.evolution = new EvolutionQuery(this.entity, EnumNecrozma.ULTRA.getForm());
               this.tempAbility = null;
               this.setForm(EnumNecrozma.ULTRA);
               if (this.participant.getType() == ParticipantType.Player) {
                  PlayerParticipant player = (PlayerParticipant)this.participant;
                  Pixelmon.network.sendTo(new MegaEvolve(this.getPokemonUUID(), true), player.player);
               }

               this.bc.updateFormChange(this.entity);
               return true;
            }

            gigantamax = this.entity.isPokemon(new EnumSpecies[]{EnumSpecies.Rayquaza});
            if (this.canMegaEvolve() && this.participant.evolution == null && (this.participant.canMegaEvolve() || gigantamax)) {
               ItemMegaStone megaStone = this.getHeldItem() instanceof ItemMegaStone ? (ItemMegaStone)this.getHeldItem() : null;
               if (Pixelmon.EVENT_BUS.post(new MegaEvolutionEvent.BattleEvolve(this, megaStone, false))) {
                  return false;
               } else {
                  this.isMega = true;
                  this.participant.evolution = this.getPokemonUUID();
                  String nickname = this.getNickname();
                  if (gigantamax) {
                     this.bc.sendToAll("pixelmon.battletext.rayquazamegareact", this.participant.getDisplayName());
                  } else {
                     this.bc.sendToAll("pixelmon.battletext.megareact", nickname, this.getHeldItem().getLocalizedName(), this.participant.getDisplayName());
                  }

                  int form = megaStone == null ? 1 : megaStone.getForm(this.getForm());
                  this.evolution = new EvolutionQuery(this.entity, form);
                  this.tempAbility = null;
                  this.setForm(form);
                  if (this.participant.getType() == ParticipantType.Player) {
                     PlayerParticipant player = (PlayerParticipant)this.participant;
                     Pixelmon.network.sendTo(new MegaEvolve(this.getPokemonUUID(), false), player.player);
                  }

                  this.bc.updateFormChange(this.entity);
                  return true;
               }
            }
         }

         if (this.bc.oldGen.isNo() && this.canDynamax() && this.participant.canDynamax() && this.participant.dynamax == null && this.participant.canDynamax()) {
            gigantamax = this.canGigantamax();
            boolean dynamax = this.canDynamax();
            if (gigantamax || dynamax) {
               if (Pixelmon.EVENT_BUS.post(new DynamaxEvent.BattleEvolve(this, gigantamax))) {
                  return false;
               }

               this.getBattleAbility().applyDynamaxEffect(this);
               if (gigantamax) {
                  this.setPrevForm(this.getFormEnum());
                  if (this.getSpecies() == EnumSpecies.Urshifu) {
                     EnumUrshifu form = (EnumUrshifu)this.getFormEnum();
                     this.setForm(form.getGigantamax());
                  } else {
                     this.setForm(this.pokemon.getSpecies().getFormEnum("gmax"));
                  }
               }

               float healthPercentage = this.getHealthPercent();
               this.isDynamax = gigantamax ? 2 : 1;
               this.dynamax(false, healthPercentage);
               return true;
            }
         }

         return false;
      }
   }

   public void setAttack(int buttonId, ArrayList targets, boolean megaEvolving) {
      Attack attack = this.getMoveset().get(buttonId >= 4 ? buttonId - 4 : buttonId);
      if (buttonId >= 4 && !this.participant.usedZ && attack.getMove().hasZMove(this.pokemon) && this.participant.canMegaEvolve()) {
         this.usingZ = this.participant.bc.oldGen.isYes();
      } else {
         this.usingZ = false;
      }

      this.setAttack(attack, targets, megaEvolving);
   }

   public void setAttack(Attack attack, ArrayList targets, boolean megaEvolving) {
      this.attack = attack;
      this.targets = targets;
      if (!this.bc.simulateMode) {
         this.wait = false;
         this.willEvolve = megaEvolving;
      }

   }

   public boolean isFainted() {
      return this.getHealth() <= 0;
   }

   public boolean isAlive() {
      return !this.isFainted();
   }

   public void setStruggle(ArrayList targets) {
      this.setAttack(new Attack("Struggle"), targets, false);
   }

   public void returnToBasePos() {
      if (this.basePos != null && this.entity != null) {
         this.entity.func_70012_b(this.basePos[0], this.basePos[1], this.basePos[2], this.participant.team == 0 ? 270.0F : 90.0F, 0.0F);
         this.entity.field_70126_B = this.entity.field_70177_z;
      }

   }

   public void setBasePosition(double[] ds) {
      if (this.entity != null) {
         this.basePos = ds;
         this.entity.func_70012_b(ds[0], ds[1], ds[2], 0.0F, 0.0F);
      }

   }

   public void setTempType(EnumType newType) {
      this.setTempType((List)newType.makeTypeList());
   }

   public void setTempType(List newType) {
      if (!this.bc.simulateMode) {
         if (this.initialType == null) {
            this.initialType = this.type;
         }

         this.type = newType;
      }

   }

   public void setTempAbility(AbilityBase newAbility) {
      this.setTempAbility(newAbility, false);
   }

   public void setTempAbility(AbilityBase newAbility, boolean formChange) {
      if (!this.bc.simulateMode) {
         this.getBattleAbility().onAbilityLost(this);
         if (newAbility.needNewInstance()) {
            newAbility = newAbility.getNewInstance();
         }

         this.tempAbility = newAbility;
         AbilityBase pokemonAbility = this.getBattleAbility();
         if (formChange || !(pokemonAbility instanceof Trace) && !(pokemonAbility instanceof Imposter)) {
            pokemonAbility.applySwitchInEffect(this);
         }
      }

   }

   public void resetOnSwitch() {
      if (this.bc == null || !this.bc.simulateMode) {
         this.lastAttack = null;
         this.lastSimulatedAttack = null;
         this.lastTempAttack = null;
         this.lastSimulatedAttack = null;
         this.lastTargets = null;
         this.lastSimulatedTargets = null;
         this.protectsInARow = 0;
         if (this.initialType != null) {
            this.type = this.initialType;
            this.initialType = null;
         }

         this.tempAbility = null;
         Moveset moveset = this.getMoveset();

         int i;
         for(i = 0; i < moveset.size(); ++i) {
            moveset.get(i).setDisabled(false, this, true);
         }

         this.usedMoves.clear();
         this.escapeAttempts = 0;
         this.damageTakenThisTurn = 0;
         this.nextSwitchIsMove = false;
         this.wait = false;
         this.isSwitching = false;
         this.choiceLocked = null;
         this.inMultipleHit = false;
         this.inParentalBond = false;

         for(i = 0; i < this.status.size(); ++i) {
            if (!((StatusBase)this.status.get(i)).type.isPrimaryStatus()) {
               this.status.remove(i--);
            }
         }

         this.battleStats.clearBattleStats();
      }
   }

   public float getWeight(boolean ignoreAbility) {
      float weight = this.getBaseStats().getWeight();
      if (!ignoreAbility) {
         weight = this.getBattleAbility().modifyWeight(weight);
      }

      weight = this.getUsableHeldItem().modifyWeight(weight);

      StatusBase status;
      for(Iterator var3 = this.getStatuses().iterator(); var3.hasNext(); weight = status.modifyWeight(weight)) {
         status = (StatusBase)var3.next();
      }

      return weight;
   }

   public void consumeItem() {
      ItemHeld item = this.getHeldItem();
      this.setConsumedItem(item);
      this.removeHeldItem();
      this.bc.getActiveUnfaintedPokemon().forEach((pw) -> {
         pw.getBattleAbility().onItemConsumed(pw, this, item);
      });
   }

   public void setConsumedItem(ItemHeld heldItem) {
      if (!this.bc.simulateMode) {
         this.consumedHeldItem = (ItemHeld)(heldItem == null ? NoItem.noItem : heldItem);
      }

   }

   public ItemHeld getConsumedItem() {
      return this.consumedHeldItem;
   }

   public void setNewHeldItem(ItemHeld heldItem) {
      this.setHeldItem(heldItem);
      this.getUsableHeldItem().applySwitchInEffect(this);
   }

   public boolean isGrounded() {
      return this.hasStatus(StatusType.SmackedDown) || this.bc.globalStatusController.hasStatus(StatusType.Gravity) || this.getUsableHeldItem().getHeldItemType() == EnumHeldItems.ironBall;
   }

   public boolean isAirborne() {
      return !this.isGrounded() && (this.hasType(EnumType.Flying) || this.getBattleAbility() instanceof Levitate || this.hasStatus(StatusType.MagnetRise, StatusType.Telekinesis) || this.getUsableHeldItem().getHeldItemType() == EnumHeldItems.airBalloon);
   }

   public boolean addTeamStatus(StatusBase status, PixelmonWrapper cause) {
      boolean succeeded = false;

      PixelmonWrapper pw;
      for(Iterator var4 = this.getTeamPokemon().iterator(); var4.hasNext(); succeeded = pw.addStatus(status.copy(), cause) || succeeded) {
         pw = (PixelmonWrapper)var4.next();
      }

      return succeeded;
   }

   public boolean removeTeamStatus(StatusBase status) {
      return this.removeTeamStatus(status.type);
   }

   public boolean removeTeamStatus(StatusType... statuses) {
      boolean hadStatus = false;
      StatusType[] var3 = statuses;
      int var4 = statuses.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StatusType status = var3[var5];

         PixelmonWrapper pw;
         for(Iterator var7 = this.getTeamPokemon().iterator(); var7.hasNext(); hadStatus = pw.removeStatus(status) || hadStatus) {
            pw = (PixelmonWrapper)var7.next();
         }
      }

      return hadStatus;
   }

   public ArrayList getTeamPokemon() {
      return this.bc.getTeamPokemon(this);
   }

   public ArrayList getTeamPokemonExcludeSelf() {
      return this.bc.getTeamPokemonExcludeSelf(this);
   }

   public ArrayList getOpponentPokemon() {
      return this.bc.getOpponentPokemon(this);
   }

   public boolean isAlly(PixelmonWrapper pokemon) {
      return this.getTeamPokemon().contains(pokemon);
   }

   public boolean isOpponent(PixelmonWrapper pokemon) {
      return this.getOpponentPokemon().contains(pokemon);
   }

   public int getControlledIndex() {
      return this.participant.controlledPokemon.indexOf(this);
   }

   public BattleAIBase getBattleAI() {
      return this.getParticipant().getBattleAI();
   }

   public void setUpSwitchMove() {
      this.wait = true;
      this.nextSwitchIsMove = true;
      this.canAttack = false;
      this.bc.removeFromTurnList(this);
   }

   public void forceRandomSwitch(UUID switchPokemon) {
      this.setUpSwitchMove();
      this.bc.switchPokemon(this.getPokemonUUID(), switchPokemon, true);
   }

   public boolean isImmuneToPowder() {
      if (!this.hasType(EnumType.Grass) && !(this.getBattleAbility() instanceof Overcoat)) {
         ItemHeld item = this.getHeldItem();
         return item.getHeldItemType() == EnumHeldItems.safetyGoggles;
      } else {
         return true;
      }
   }

   public String toString() {
      return this.getPokemonName();
   }

   public boolean equals(Object other) {
      if (super.equals(other)) {
         return true;
      } else if (other instanceof PixelmonWrapper) {
         PixelmonWrapper otherPW = (PixelmonWrapper)other;
         return this.getPokemonUUID().equals(otherPW.getPokemonUUID());
      } else {
         return false;
      }
   }

   public boolean hasMoved() {
      return this.bc.turnList.indexOf(this) <= this.bc.turn;
   }

   public boolean isFirstTurn() {
      return this.bc.battleTurn == 0 || this.bc.battleLog.getActionForPokemon(this.bc.battleTurn - 1, this) == null || this.bc.battleLog.getActionForPokemon(this.bc.battleTurn - 1, this) instanceof SwitchAction || this.lastAttack == null && this.bc.battleLog.getActionForPokemon(this.bc.battleTurn - 1, this) instanceof AttackAction;
   }

   public boolean isSameTeam(PixelmonWrapper other) {
      return this.getParticipant().team == other.getParticipant().team;
   }

   public Moveset getMoveset() {
      return this.temporaryMoveset == null ? this.pokemon.getMoveset() : this.temporaryMoveset;
   }

   public void setTemporaryMoveset(Moveset moveset) {
      this.temporaryMoveset = moveset;
      if (this.participant instanceof PlayerParticipant) {
         EntityPlayerMP player = ((PlayerParticipant)this.participant).player;
         Pixelmon.network.sendTo(new UpdateMoveset(this), player);
      }

   }

   public boolean removeStatus(StatusType s) {
      for(int i = 0; i < this.status.size(); ++i) {
         StatusBase base = (StatusBase)this.status.get(i);
         if (base.type == s) {
            this.removeStatus(base);
            return true;
         }
      }

      return false;
   }

   public boolean removeStatus(StatusType s, boolean message) {
      for(int i = 0; i < this.status.size(); ++i) {
         StatusBase base = (StatusBase)this.status.get(i);
         if (base.type == s) {
            this.removeStatus(base, message);
            return true;
         }
      }

      return false;
   }

   public boolean removeStatuses(StatusType... statuses) {
      return this.removeStatuses(true, statuses);
   }

   public boolean removeStatuses(boolean showMessage, StatusType... statuses) {
      boolean wasRemoved = false;

      for(int i = 0; i < this.status.size(); ++i) {
         StatusBase base = (StatusBase)this.status.get(i);
         StatusType[] var6 = statuses;
         int var7 = statuses.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            StatusType type = var6[var8];
            if (base.type == type) {
               int beforeSize = this.status.size();
               this.removeStatus(base, showMessage);
               if (this.status.size() < beforeSize) {
                  --i;
               }

               wasRemoved = true;
               break;
            }
         }
      }

      return wasRemoved;
   }

   public void removeStatus(int i) {
      this.removeStatus((StatusBase)this.status.get(i));
   }

   public StatusBase getStatus(StatusType type) {
      Iterator var2 = this.status.iterator();

      StatusBase base;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         base = (StatusBase)var2.next();
      } while(base.type != type);

      return base;
   }

   public boolean hasStatus(StatusType... statuses) {
      Iterator var2 = this.status.iterator();

      while(var2.hasNext()) {
         StatusBase base = (StatusBase)var2.next();
         StatusType[] var4 = statuses;
         int var5 = statuses.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StatusType current = var4[var6];
            if (base.type == current) {
               return true;
            }

            if (current == StatusType.Sleep && this.getBattleAbility().isAbility(Comatose.class)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasPrimaryStatus() {
      return this.hasPrimaryStatus(true);
   }

   public boolean hasPrimaryStatus(boolean includeComatose) {
      return this.hasStatus(StatusType.Poison, StatusType.Burn, StatusType.PoisonBadly, StatusType.Freeze, StatusType.Sleep, StatusType.Paralysis) || includeComatose && this.getBattleAbility().isAbility(Comatose.class);
   }

   public int countStatuses(StatusType... statuses) {
      int count = 0;
      Iterator var3 = this.status.iterator();

      while(var3.hasNext()) {
         StatusBase base = (StatusBase)var3.next();
         StatusType[] var5 = statuses;
         int var6 = statuses.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            StatusType current = var5[var7];
            if (base.type == current) {
               ++count;
            }
         }
      }

      return count;
   }

   public int getStatusSize() {
      return this.status.size();
   }

   public List getStatuses() {
      return this.status;
   }

   public StatusBase getStatus(int i) {
      return (StatusBase)this.status.get(i);
   }

   public int getStatusIndex(StatusType findStatus) {
      for(int i = 0; i < this.status.size(); ++i) {
         StatusBase base = (StatusBase)this.status.get(i);
         if (base.type == findStatus) {
            return i;
         }
      }

      return -1;
   }

   public StatusPersist getPrimaryStatus() {
      Iterator var1 = this.status.iterator();

      StatusBase s;
      do {
         if (!var1.hasNext()) {
            return NoStatus.noStatus;
         }

         s = (StatusBase)var1.next();
      } while(!(s instanceof StatusPersist));

      return (StatusPersist)s;
   }

   public void setStatus(int i, StatusBase newStatus) {
      this.status.set(i, newStatus);
   }

   public void clearStatus() {
      if (!this.bc.simulateMode) {
         if (this.hasPrimaryStatus(false) && this.entity != null) {
            this.sendStatusPacket(-1);
         }

         this.status.clear();
      }
   }

   public boolean addStatus(StatusBase e, PixelmonWrapper opponent) {
      return this.addStatus(e, opponent, (TextComponentTranslation)null);
   }

   public boolean addStatus(StatusBase e, PixelmonWrapper opponent, TextComponentTranslation message) {
      if (this.cannotHaveStatus(e, opponent)) {
         return false;
      } else if (this.participant.onAddStatus(this.bc, opponent, this, e)) {
         return true;
      } else {
         e.applyBeforeEffect(this, opponent);
         if (!this.bc.simulateMode) {
            this.status.add(e);
         }

         if (this.bc.sendMessages) {
            if (message != null) {
               this.bc.sendToAll(message);
            }

            if (!this.bc.simulateMode && e.type.isPrimaryStatus()) {
               this.sendStatusPacket(e.type.ordinal());
            }

            this.getBattleAbility().onStatusAdded(e, this, opponent);
            this.getUsableHeldItem().onStatusAdded(this, opponent, e);
            if (!this.bc.simulateMode) {
               if (this.getPlayerOwner() != null && e.type.isPrimaryStatus()) {
                  this.update(EnumUpdateType.Status);
               }

               this.bc.updatePokemonHealth();
            }
         }

         return true;
      }
   }

   public void sendStatusPacket(int statusID) {
      if (this.bc != null && !this.bc.simulateMode) {
         this.bc.participants.stream().filter((p) -> {
            return p.getType() == ParticipantType.Player;
         }).forEach((p) -> {
            Pixelmon.network.sendTo(new StatusUpdateTask(this.getPokemonUUID(), statusID), ((PlayerParticipant)p).player);
         });
         this.bc.spectators.forEach((spectator) -> {
            spectator.sendMessage(new StatusUpdateTask(this.getPokemonUUID(), statusID));
         });
      }

   }

   public void removeStatus(StatusBase e) {
      this.removeStatus(e, true);
   }

   public void removeStatus(StatusBase e, boolean showMessage) {
      if (!this.bc.simulateMode) {
         if (this.status.remove(e) && showMessage) {
            String message = this.eatingBerry ? e.getCureMessageItem() : e.getCureMessage();
            if (!message.isEmpty()) {
               String nickname = this.getNickname();
               if (this.eatingBerry) {
                  this.bc.sendToAll(message, nickname, this.getHeldItem().getLocalizedName());
               } else {
                  this.bc.sendToAll(message, nickname);
               }
            }
         }

         if (e.type.isPrimaryStatus()) {
            this.sendStatusPacket(-1);
            this.update(EnumUpdateType.Status);
         }

         this.bc.updatePokemonHealth();
      }
   }

   public StatusBase removePrimaryStatus() {
      return this.removePrimaryStatus(true);
   }

   public StatusBase removePrimaryStatus(boolean showMessage) {
      try {
         Iterator var2 = this.status.iterator();

         StatusBase base;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            base = (StatusBase)var2.next();
         } while(!StatusType.isPrimaryStatus(base.type));

         this.removeStatus(base, showMessage);
         return base;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public boolean cannotHaveStatus(StatusBase t, PixelmonWrapper opponent) {
      return this.cannotHaveStatus(t, opponent, false);
   }

   public boolean cannotHaveStatus(StatusBase t, PixelmonWrapper opponent, boolean ignorePrimaryOverlap) {
      StatusType type = t.type;
      if (StatusType.isPrimaryStatus(type) && this.hasPrimaryStatus() && !ignorePrimaryOverlap) {
         return true;
      } else if ((!t.isImmune(this) || !(t instanceof Poison) || opponent.getBattleAbility().getClass() == Corrosion.class) && (!this.isFainted() || t.isTeamStatus()) && !this.hasStatus(type)) {
         Iterator var5 = this.status.iterator();

         while(var5.hasNext()) {
            StatusBase current = (StatusBase)var5.next();

            try {
               if (current.stopsStatusChange(type, this, opponent)) {
                  return true;
               }
            } catch (Exception var9) {
               System.out.println("Problem with cannotHaveStatus for StatusType " + type.name());
               var9.printStackTrace();
            }
         }

         var5 = this.bc.globalStatusController.getGlobalStatuses().iterator();

         while(var5.hasNext()) {
            GlobalStatusBase current = (GlobalStatusBase)var5.next();

            try {
               if (current.stopsStatusChange(type, this, opponent)) {
                  return true;
               }
            } catch (Exception var8) {
               System.out.println("Problem with cannotHaveStatus for StatusType " + type.name());
               var8.printStackTrace();
            }
         }

         var5 = this.bc.getTeamPokemon(this).iterator();

         PixelmonWrapper ally;
         do {
            if (!var5.hasNext()) {
               return !this.getBattleAbility().allowsStatus(type, this, opponent);
            }

            ally = (PixelmonWrapper)var5.next();
         } while(ally.getBattleAbility(opponent).allowsStatusTeammate(type, ally, this, opponent));

         return true;
      } else {
         return true;
      }
   }

   public List getEntryHazards() {
      List hazards = new ArrayList();
      Iterator var2 = this.status.iterator();

      while(var2.hasNext()) {
         StatusBase s = (StatusBase)var2.next();
         if (s instanceof EntryHazard) {
            hazards.add((EntryHazard)s);
         }
      }

      return hazards;
   }

   private void updateBattleDamage(float damage) {
      this.updateBattleDamage((int)damage);
   }

   private void updateBattleDamage(int damage) {
      if (this.entity != null) {
         this.bc.updatePokemonHealth();
         this.bc.sendDamagePacket(this, damage);
         this.update(EnumUpdateType.HP);
         if (this.isFainted()) {
            int turnIndex = this.bc.turnList.indexOf(this);
            if (turnIndex > this.bc.turn) {
               this.bc.turnList.remove(turnIndex);
            }

            if (this.removePrimaryStatus(false) != null) {
               this.sendStatusPacket(-1);
               this.update(EnumUpdateType.Status);
            }
         }

         NPCTrainer trainer = this.entity.getTrainer();
         if (trainer != null) {
         }

      }
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      List effectiveTypes = user.getBattleAbility().getEffectiveTypes(user, target);
      if (!effectiveTypes.equals(target.type)) {
         return effectiveTypes;
      } else {
         for(int i = 0; i < target.getStatusSize(); ++i) {
            effectiveTypes = target.getStatus(i).getEffectiveTypes(user, target);
            if (!effectiveTypes.equals(target.type)) {
               return effectiveTypes;
            }
         }

         effectiveTypes = target.getUsableHeldItem().getEffectiveTypes(user, target);

         GlobalStatusBase status;
         for(Iterator var6 = target.bc.globalStatusController.getGlobalStatuses().iterator(); var6.hasNext(); effectiveTypes = status.getEffectiveTypes(user, target)) {
            status = (GlobalStatusBase)var6.next();
         }

         return effectiveTypes;
      }
   }

   public float doBattleDamage(PixelmonWrapper source, float damage, DamageTypeEnum damageType) {
      this.lastHP = this.getHealth();
      AbilityBase thisAbility = this.getBattleAbility(source);
      ItemHeld thisHeldItem = this.getUsableHeldItem();
      if (source == null) {
         source = this;
      }

      if (this.isFainted()) {
         return -1.0F;
      } else {
         boolean isMultiHit = false;
         Substitute substitute = null;
         boolean hitSubstitute = false;
         Iterator var18;
         if (source != this) {
            if (damageType == DamageTypeEnum.ATTACK || damageType == DamageTypeEnum.ATTACKFIXED) {
               substitute = (Substitute)this.getStatus(StatusType.Substitute);
               hitSubstitute = substitute != null && !substitute.ignoreSubstitute(source);
            }

            Iterator var9;
            if (damageType == DamageTypeEnum.ATTACK) {
               var9 = source.attack.getMove().effects.iterator();

               label239: {
                  EffectBase e;
                  do {
                     if (!var9.hasNext()) {
                        break label239;
                     }

                     e = (EffectBase)var9.next();
                  } while(!(e instanceof MultipleHit) && !(e instanceof TripleKick) && !(e instanceof BeatUp) && !(e instanceof DragonDarts) && !(e instanceof TripleAxel));

                  isMultiHit = true;
               }

               if (!isMultiHit) {
                  source.attack.sendEffectiveChat(source, this);
               }

               ArrayList allies = this.bc.getTeamPokemon(this.getParticipant());
               if (allies.size() > 1) {
                  var18 = allies.iterator();

                  while(var18.hasNext()) {
                     PixelmonWrapper ally = (PixelmonWrapper)var18.next();
                     if (ally != this) {
                        damage = (float)ally.getBattleAbility().modifyDamageTeammate((int)damage, source, this, source.attack);
                     }
                  }
               }

               damage = (float)source.getBattleAbility().modifyDamageUser((int)damage, source, this, source.attack);
               if (!AbilityBase.ignoreAbility(source, this)) {
                  damage = (float)thisAbility.modifyDamageTarget((int)damage, source, this, source.attack);
                  if (!hitSubstitute) {
                     damage = (float)thisAbility.modifyDamageIncludeFixed((int)damage, source, this, source.attack);
                  }
               }

               if (!hitSubstitute) {
                  StatusBase status;
                  for(var18 = this.getStatuses().iterator(); var18.hasNext(); damage = (float)status.modifyDamageIncludeFixed((int)damage, source, this, source.attack, damageType)) {
                     status = (StatusBase)var18.next();
                  }

                  damage = (float)thisHeldItem.modifyDamageIncludeFixed((double)damage, source, this, source.attack);
               }
            } else if (damageType == DamageTypeEnum.ATTACKFIXED && !hitSubstitute) {
               StatusBase status;
               for(var9 = this.getStatuses().iterator(); var9.hasNext(); damage = (float)status.modifyDamageIncludeFixed((int)damage, source, this, source.attack, damageType)) {
                  status = (StatusBase)var9.next();
               }

               damage = (float)thisAbility.modifyDamageIncludeFixed((int)damage, source, this, source.attack);
               damage = (float)thisHeldItem.modifyDamageIncludeFixed((double)damage, source, this, source.attack);
            }
         }

         damage = (float)Math.floor((double)damage);
         if (this.getParticipant() != null && !this.bc.simulateMode) {
            damage = this.getParticipant().onHit(source, damage, damageType);
         }

         float damageResult;
         EffectBase effect;
         if (hitSubstitute) {
            damageResult = substitute.attackSubstitute(damage, source, this);
            source.attack.moveResult.damage = (int)damageResult;
            source.attack.moveResult.fullDamage = (int)damage;
            source.attack.moveResult.target = this;
            source.attack.moveResult.result = AttackResult.succeeded;
            source.getUsableHeldItem().postProcessDamagingAttackUser(source, this, source.attack, damageResult);
            if (!isMultiHit) {
               Attack.postProcessAttackAllHits(source, this, source.attack, damageResult, damageType, true);
            }

            var18 = source.attack.getMove().effects.iterator();

            while(var18.hasNext()) {
               effect = (EffectBase)var18.next();
               if (effect instanceof Recoil) {
                  ((Recoil)effect).applyRecoil(source, damageResult);
               }
            }

            return damageResult;
         } else {
            if (source.attack != null && source.attack.getMove().hasEffect(FalseSwipe.class) && this != source) {
               damage = Math.min(damage, (float)(this.getHealth() - 1));
            }

            damageResult = Math.min((float)this.getHealth(), damage);
            if (source != this && source.attack != null && source.attack.moveResult != null) {
               source.attack.moveResult.damage = (int)damageResult;
               source.attack.moveResult.fullDamage = (int)damage;
               source.attack.moveResult.target = this;
               source.attack.moveResult.result = AttackResult.hit;
            }

            if (!this.bc.simulateMode) {
               if (this.entity != null) {
                  double prevMotionX = this.entity.field_70159_w;
                  double prevMotionY = this.entity.field_70181_x;
                  double prevMotionZ = this.entity.field_70179_y;
                  this.entity.func_70097_a(new BattleDamageSource("battle", source), 0.0F);
                  this.entity.field_70159_w = prevMotionX;
                  this.entity.field_70181_x = prevMotionY;
                  this.entity.field_70179_y = prevMotionZ;
               }

               this.setHealth(Math.max(this.getHealth() - (int)damageResult, 0));
               this.updateBattleDamage(damage);
               if (source.attack != null && !this.isSameTeam(source)) {
                  this.lastDirectPosition = source.battlePosition;
                  this.lastDirectDamage = (int)damageResult;
                  this.lastDirectCategory = source.attack.getAttackCategory();
               }
            }

            for(int i = 0; i < this.getStatusSize(); ++i) {
               this.getStatus(i).onDamageReceived(source, this, source.attack, (int)damageResult, damageType);
            }

            this.getBattleAbility(source).onDamageReceived(source, this, source.attack, (int)damageResult, damageType);
            if (source != this && damageType.isDirect()) {
               if (source.attack.getMove().getMakesContact()) {
                  Attack.applyContact(source, this);
               }

               source.getBattleAbility(source).tookDamageUser((int)damageResult, source, this, source.attack);
               thisAbility.tookDamageTarget((int)damageResult, source, this, source.attack);
            }

            if (this.isFainted()) {
               ArrayList allies = this.bc.getTeamPokemon(this);
               ArrayList foes = this.bc.getOpponentPokemon(this);
               this.getBattleAbility(source).onSelfFaint(this, source);
               Iterator var26 = allies.iterator();

               PixelmonWrapper pw;
               while(var26.hasNext()) {
                  pw = (PixelmonWrapper)var26.next();
                  if (pw != this) {
                     pw.getBattleAbility(source).onAllyFaint(pw, this, source);
                  }
               }

               var26 = foes.iterator();

               while(var26.hasNext()) {
                  pw = (PixelmonWrapper)var26.next();
                  pw.getBattleAbility(source).onFoeFaint(pw, this, source);
                  if (pw.getParticipant() != null && !this.bc.simulateMode) {
                     pw.getParticipant().onOpponentKO(this.bc, this);
                  }
               }
            }

            if (source != this && damageType.isDirect()) {
               source.getUsableHeldItem().postProcessDamagingAttackUser(source, this, source.attack, damageResult);
               if (!isMultiHit) {
                  Attack.postProcessAttackAllHits(source, this, source.attack, damageResult, damageType, false);
               }

               var18 = source.attack.getMove().effects.iterator();

               while(var18.hasNext()) {
                  effect = (EffectBase)var18.next();
                  if (effect instanceof Recoil) {
                     ((Recoil)effect).applyRecoil(source, damageResult);
                  }
               }
            }

            if (source.attack == null || !source.attack.canRemoveBerry()) {
               thisHeldItem.tookDamage(source, this, damageResult, damageType);
            }

            if (this.getHealth() <= 0) {
               String name = this.getNickname();
               this.bc.sendToAll("battlecontroller.hasfainted", name);
               if (this.participant.getType() == ParticipantType.WildPokemon) {
                  this.entity.func_70097_a(new BattleDamageSource("battle", source), this.entity.func_110138_aP());
               } else {
                  this.pokemon.decreaseFriendship(1);
                  Pixelmon.EVENT_BUS.post(new PixelmonFaintEvent(this.getPlayerOwner(), this.entity));
                  RecoilStats stats = (RecoilStats)this.pokemon.getExtraStats(RecoilStats.class);
                  if (stats != null) {
                     stats.setRecoil((int)((float)stats.recoil() + damage));
                  }
               }

               Pixelmon.EVENT_BUS.post(new PixelmonKnockoutEvent(source, this));
            } else if (!this.bc.simulateMode) {
               this.damageTakenThisTurn = (int)((float)this.damageTakenThisTurn + damage);
               if (damageType == DamageTypeEnum.RECOIL) {
                  RecoilStats stats = (RecoilStats)this.pokemon.getExtraStats(RecoilStats.class);
                  if (stats != null) {
                     stats.setRecoil((int)((float)stats.recoil() + damage));
                  }
               }
            }

            return damageResult;
         }
      }
   }

   public AbilityBase getAbility() {
      return this.pokemon.getAbility();
   }

   public AbilityBase getBattleAbility() {
      return this.getBattleAbility(true, (PixelmonWrapper)null);
   }

   public AbilityBase getBattleAbility(PixelmonWrapper moveUser) {
      return this.getBattleAbility(true, moveUser);
   }

   public AbilityBase getBattleAbility(boolean canIgnore) {
      return this.getBattleAbility(canIgnore, (PixelmonWrapper)null);
   }

   public AbilityBase getBattleAbility(boolean canIgnore, PixelmonWrapper moveUser) {
      if (canIgnore && AbilityBase.ignoreAbility(moveUser, this)) {
         return ComingSoon.noAbility;
      } else {
         return this.tempAbility != null ? this.tempAbility : this.pokemon.getAbility();
      }
   }

   public boolean hasHeldItem() {
      return this.pokemon.getHeldItemAsItemHeld() != NoItem.noItem;
   }

   public ItemHeld getHeldItem() {
      return this.pokemon.getHeldItemAsItemHeld();
   }

   public ItemHeld getUsableHeldItem() {
      return (ItemHeld)(ItemHeld.canUseItem(this) ? this.getHeldItem() : NoItem.noItem);
   }

   public void removeHeldItem() {
      this.setHeldItem((ItemHeld)NoItem.noItem);
   }

   public void setHeldItem(ItemHeld newItem) {
      if (!this.bc.simulateMode) {
         this.pokemon.setHeldItem(newItem == null ? null : new ItemStack(newItem));
         this.getBattleAbility().onItemChanged(this, this.pokemon.getHeldItemAsItemHeld());
      }

   }

   public void setHeldItem(ItemStack itemStack) {
      if (!this.bc.simulateMode) {
         this.pokemon.setHeldItem(itemStack);
         this.getBattleAbility().onItemChanged(this, this.pokemon.getHeldItemAsItemHeld());
      }

   }

   public boolean canMegaEvolve() {
      return this.getSpecies() == EnumSpecies.Rayquaza ? this.pokemon.getMoveset().hasAttack("Dragon Ascent") : canMegaEvolve(this.getHeldItem(), this.pokemon.getSpecies(), this.pokemon.getForm());
   }

   public boolean canUltraBurst() {
      return canUltraBurst(this.getSpecies(), this.getHeldItem(), this.getForm());
   }

   public boolean canDynamax() {
      return canDynamax(this.getSpecies(), this.getFormEnum(), this.getHeldItem());
   }

   public boolean canGigantamax() {
      return canGigantamax(this.hasGigantamaxFactor(), this.getSpecies(), this.getFormEnum());
   }

   public static boolean canMegaEvolve(ItemStack heldItem, EnumSpecies pokemon, int form) {
      return canMegaEvolve(ItemHeld.getItemHeld(heldItem), pokemon, form);
   }

   public static boolean canUseZMove(ItemStack heldItem) {
      return heldItem.func_77973_b() instanceof ItemZCrystal;
   }

   public static boolean canMegaEvolve(ItemHeld heldItem, EnumSpecies species, int form) {
      return (form == 1 || form == 2) && species == EnumSpecies.Necrozma && heldItem == PixelmonItemsHeld.ultranecrozium_z ? true : hasCompatibleMegaStone(heldItem, species, form);
   }

   public static boolean canUltraBurst(EnumSpecies species, ItemHeld heldItem, int form) {
      return species == EnumSpecies.Necrozma && (form == 1 || form == 2) && heldItem == PixelmonItemsHeld.ultranecrozium_z;
   }

   public static boolean canDynamax(EnumSpecies species, IEnumForm form, ItemHeld item) {
      if ((species != EnumSpecies.Kyogre || item != PixelmonItemsHeld.blueOrb) && (species != EnumSpecies.Groudon || item != PixelmonItemsHeld.redOrb)) {
         return !EnumSpecies.cannotDynamax.contains(species);
      } else {
         return false;
      }
   }

   public static boolean canGigantamax(boolean gigantamaxFactor, EnumSpecies species, IEnumForm form) {
      return gigantamaxFactor && canDynamax(species, form, (ItemHeld)null) && EnumGigantamaxPokemon.hasGigantamaxForm(species, form);
   }

   public boolean hasCompatibleMegaStone() {
      return hasCompatibleMegaStone(this.getHeldItem(), this.getSpecies(), this.getForm());
   }

   public static boolean hasCompatibleMegaStone(ItemHeld heldItem, EnumSpecies pokemon, int form) {
      EnumMegaPokemon mega = EnumMegaPokemon.getMega(pokemon);
      if (mega != null && mega.getMegaEvoItems().length == 0) {
         return true;
      } else if (heldItem.getHeldItemType() != EnumHeldItems.megaStone) {
         return false;
      } else {
         ItemMegaStone megaStone = (ItemMegaStone)heldItem;
         return megaStone.pokemon == pokemon && megaStone.isFormAllowed(form) && megaStone.getForm(form) != form;
      }
   }

   public boolean isItemRemovable(PixelmonWrapper user) {
      if (user != (PixelmonWrapper)this && this.getBattleAbility(user) instanceof StickyHold) {
         return false;
      } else if (user.isWildPokemon()) {
         return false;
      } else if (this.hasStatus(StatusType.Substitute)) {
         return false;
      } else if (this.hasSpecialItem(user)) {
         return false;
      } else {
         ItemHeld heldItem = this.getHeldItem();
         if (heldItem instanceof ItemBlankTechnicalMachine) {
            return false;
         } else if (heldItem.getHeldItemType() == EnumHeldItems.mail) {
            return false;
         } else if (!hasCompatibleMegaStone(heldItem, user.getSpecies(), user.getForm()) && !this.hasCompatibleMegaStone()) {
            if (heldItem.getHeldItemType() != EnumHeldItems.megaStone || !this.isMega && !user.isMega) {
               if (heldItem.getHeldItemType() == EnumHeldItems.zCrystal) {
                  return false;
               } else if (heldItem == PixelmonItemsHeld.griseous_orb && (this.getSpecies() == EnumSpecies.Giratina || user.getSpecies() == EnumSpecies.Giratina)) {
                  return false;
               } else if (heldItem.getHeldItemType() == EnumHeldItems.drive && (this.getSpecies() == EnumSpecies.Genesect || user.getSpecies() == EnumSpecies.Genesect)) {
                  return false;
               } else if ((this.getSpecies() == EnumSpecies.Kyogre && heldItem == PixelmonItemsHeld.blueOrb || this.getSpecies() == EnumSpecies.Groudon && heldItem == PixelmonItemsHeld.redOrb) && this.getForm() != EnumPrimal.PRIMAL.getForm()) {
                  return false;
               } else if ((user.getSpecies() == EnumSpecies.Kyogre && user.getHeldItem() == PixelmonItemsHeld.blueOrb || user.getSpecies() == EnumSpecies.Groudon && user.getHeldItem() == PixelmonItemsHeld.redOrb) && user.getForm() != EnumPrimal.PRIMAL.getForm()) {
                  return false;
               } else if (heldItem.getHeldItemType() == EnumHeldItems.memory && (user.getSpecies() == EnumSpecies.Silvally || this.getSpecies() == EnumSpecies.Silvally)) {
                  return false;
               } else if (heldItem instanceof ItemPlate && this.getSpecies() == EnumSpecies.Arceus) {
                  return false;
               } else {
                  return heldItem != PixelmonItemsHeld.rustedSword || user.getSpecies() != EnumSpecies.Zacian && user.getSpecies() != EnumSpecies.Zamazenta && this.getSpecies() != EnumSpecies.Zacian && this.getSpecies() != EnumSpecies.Zamazenta;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isItemGivable(ItemHeld item) {
      if (item == PixelmonItemsHeld.griseous_orb && this.getSpecies() == EnumSpecies.Giratina) {
         return false;
      } else if (item.getHeldItemType() == EnumHeldItems.drive && this.getSpecies() == EnumSpecies.Genesect) {
         return false;
      } else if (item instanceof ItemPlate && this.getSpecies() == EnumSpecies.Arceus) {
         return false;
      } else if (item instanceof ItemMemory && this.getSpecies() == EnumSpecies.Silvally) {
         return false;
      } else if (item instanceof ItemMegaStone && hasCompatibleMegaStone(item, this.getSpecies(), this.getForm())) {
         return false;
      } else {
         if (item instanceof ItemOrb) {
            ItemOrb orb = (ItemOrb)item;
            if (orb.shard == EnumOrbShard.BLUE && this.getSpecies() == EnumSpecies.Kyogre) {
               return false;
            }

            if (orb.shard == EnumOrbShard.RED && this.getSpecies() == EnumSpecies.Groudon) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean hasSpecialItem(PixelmonWrapper user) {
      ItemHeld item = this.getHeldItem();
      if (item == null) {
         return false;
      } else if ((this.getSpecies() == EnumSpecies.Kyogre || user.getSpecies() == EnumSpecies.Kyogre) && item == PixelmonItemsHeld.blueOrb) {
         return true;
      } else if ((this.getSpecies() == EnumSpecies.Groudon || user.getSpecies() == EnumSpecies.Groudon) && item == PixelmonItemsHeld.redOrb) {
         return true;
      } else if (item instanceof ItemPlate && this.getBattleAbility() instanceof Multitype) {
         return true;
      } else if (item instanceof ItemMemory && this.getBattleAbility() instanceof RKSSystem) {
         return true;
      } else if (item instanceof ItemZCrystal) {
         return true;
      } else if ((this.getSpecies() == EnumSpecies.Giratina || user.getSpecies() == EnumSpecies.Giratina) && item == PixelmonItemsHeld.griseous_orb) {
         return true;
      } else if ((this.getSpecies() == EnumSpecies.Genesect || user.getSpecies() == EnumSpecies.Genesect) && item.getHeldItemType() == EnumHeldItems.drive) {
         return true;
      } else {
         return this.hasCompatibleMegaStone() || hasCompatibleMegaStone(item, user.getSpecies(), user.getForm());
      }
   }

   public String getNickname() {
      AbilityBase battleAbility = this.getBattleAbility();
      if (battleAbility instanceof Illusion) {
         Illusion illusion = (Illusion)battleAbility;
         if (illusion.disguisedPokemon != null) {
            if (!illusion.disguisedNickname.isEmpty()) {
               return illusion.disguisedNickname;
            }

            return illusion.disguisedPokemon.getLocalizedName();
         }
      }

      return this.getRealNickname();
   }

   public String getRealNickname() {
      return this.pokemon.getDisplayName();
   }

   public Gender getGender() {
      return this.pokemon.getGender();
   }

   public int getHealth(boolean ignoreDynamax) {
      int hp = this.pokemon.getHealth();
      if (this.isDynamax > 0 && ignoreDynamax) {
         hp = (int)Math.ceil((double)hp / (1.0 + this.getDynamaxHealthMod()));
      }

      return hp;
   }

   public int getHealth() {
      return this.getHealth(false);
   }

   public int getMaxHealth(boolean ignoreDynamax) {
      int maxHP = this.pokemon.getStat(StatsType.HP);
      if (this.isDynamax > 0 && !ignoreDynamax) {
         double percent = this.getDynamaxHealthMod();
         maxHP += (int)Math.ceil((double)maxHP * percent);
      }

      return maxHP;
   }

   public int getMaxHealth() {
      return this.getMaxHealth(false);
   }

   public double getDynamaxHealthMod() {
      return 0.5 + (double)this.pokemon.getDynamaxLevel() * 0.05;
   }

   public float getHealthPercent() {
      return this.getHealthPercent((float)this.getHealth());
   }

   public float getHealthPercent(float amount) {
      return amount / (float)this.getMaxHealth() * 100.0F;
   }

   public float getHealPercent(float amount) {
      return this.getHealthPercent(Math.min(amount, (float)this.getHealthDeficit()));
   }

   public int getHealthDeficit() {
      return this.getMaxHealth() - this.getHealth();
   }

   public boolean hasFullHealth() {
      return this.getHealth() >= this.getMaxHealth();
   }

   public int getPercentMaxHealth(float percent, boolean ignoreDynamax) {
      int maxHP = this.getMaxHealth();
      if (ignoreDynamax) {
         maxHP = (int)Math.ceil((double)maxHP / (1.0 + this.getDynamaxHealthMod()));
      }

      return Math.max(1, (int)((float)maxHP * percent / 100.0F));
   }

   public int getPercentMaxHealth(float percent) {
      return this.getPercentMaxHealth(percent, false);
   }

   public void healByPercent(float percent) {
      this.healEntityBy(this.getPercentMaxHealth(percent));
   }

   public void healEntityBy(int i) {
      if (i + this.getHealth() > this.getMaxHealth()) {
         i = this.getMaxHealth() - this.getHealth();
      }

      if (i != 0 && !this.bc.simulateMode) {
         if (this.animateHP) {
            this.bc.sendHealPacket(this, i);
         }

         this.setHealth(this.getHealth() + i);
      }

   }

   public void setHealth(int newHP) {
      this.pokemon.setHealth(newHP);
   }

   public void setAttackFailed() {
      if (this.attack != null && this.attack.moveResult != null) {
         this.attack.moveResult.result = AttackResult.failed;
      }

   }

   public boolean doesLevel() {
      return this.pokemon.doesLevel();
   }

   public UUID getPokemonUUID() {
      return this.pokemon.getUUID();
   }

   public void update(EnumUpdateType... types) {
      PartyStorage storage = this.participant.getStorage();
      if (storage != null) {
         this.pokemon.markDirty(types);
      }

   }

   public int getForm() {
      return this.pokemon.getForm();
   }

   public IEnumForm getFormEnum() {
      return this.pokemon.getFormEnum();
   }

   public void setForm(IEnumForm form) {
      this.setForm(form.getForm());
      this.bc.updateFormChange(this);
   }

   public void setForm(int form) {
      if (this.bc == null || !this.bc.simulateMode) {
         float healthPercent = this.pokemon.getHealthPercentage();
         float dynScale = this.entity != null ? this.entity.getDynamaxScale() : 1.0F;
         this.pokemon.setForm(form);
         if (this.entity != null) {
            this.entity.setDynamaxScale(dynScale);
         }

         this.type = this.getBaseStats().getTypeList();
         this.initialType = this.type;
         if (this.tempLevel && !this.bc.battleEnded) {
            this.pokemon.getStats().setLevelStats(this.getNature(), this.getBaseStats(), this.temporaryLevel.getLevel());
            this.setHealth(Math.round(healthPercent / 100.0F * (float)this.getMaxHealth()));
         }

         this.updateHPIncrease();
      }
   }

   public void setPrevForm(IEnumForm form) {
      this.setPrevForm(form.getForm());
   }

   public void setPrevForm(int form) {
      this.prevForm = form;
   }

   public boolean hasGigantamaxFactor() {
      return this.pokemon.hasGigantamaxFactor();
   }

   public void resetBattleEvolution() {
      if (!this.isWildPokemon() || !this.entity.isBossPokemon()) {
         if (this.getFormEnum().isTemporary()) {
            this.setForm(this.initialCopyOfPokemon.getFormEnum());
         }

         if (this.getFormEnum().isTemporary()) {
            this.setForm(this.getFormEnum().getDefaultFromTemporary(this.pokemon));
         }

      }
   }

   public BlockPos getWorldPosition() {
      EntityLivingBase entity = this.entity;
      if (this.entity == null) {
         entity = this.getParticipant().getEntity();
      }

      return ((EntityLivingBase)entity).func_180425_c();
   }

   public World getWorld() {
      return this.getParticipant().getEntity().field_70170_p;
   }

   public EntityPlayerMP getPlayerOwner() {
      BattleParticipant participant = this.getParticipant();
      if (participant instanceof PlayerParticipant) {
         PlayerParticipant player = (PlayerParticipant)participant;
         return player.player;
      } else {
         return null;
      }
   }

   public NPCTrainer getTrainerOwner() {
      BattleParticipant participant = this.getParticipant();
      if (participant instanceof TrainerParticipant) {
         TrainerParticipant trainer = (TrainerParticipant)participant;
         return trainer.trainer;
      } else {
         return null;
      }
   }

   public String getOwnerName() {
      if (this.getPlayerOwner() != null) {
         return this.getPlayerOwner().func_70005_c_();
      } else {
         return this.getTrainerOwner() != null ? this.getTrainerOwner().func_70005_c_() : this.getNickname();
      }
   }

   public boolean isWildPokemon() {
      BattleParticipant participant = this.getParticipant();
      return participant.getType() == ParticipantType.WildPokemon;
   }

   public boolean isRaidPokemon() {
      return this.getParticipant().getType() == ParticipantType.RaidPokemon;
   }

   public boolean isSingleType() {
      return this.type.size() == 1;
   }

   public boolean isSingleType(EnumType type) {
      return this.isSingleType() && this.hasType(type);
   }

   public void addAttackers() {
      ArrayList opponents = this.getOpponentPokemon();
      this.attackers.addAll(opponents);
      Iterator var2 = opponents.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var2.next();
         opponent.attackers.add(this);
      }

   }

   public String getOriginalTrainer() {
      return this.pokemon.getOriginalTrainer();
   }

   public String getRealTextureNoCheck() {
      IEnumForm form = EnumSpecies.mfTextured.contains(this.getSpecies()) ? this.pokemon.getGender() : this.pokemon.getFormEnum();
      String customTexture = this.pokemon.getCustomTexture();
      return Entity2Client.getTextureFor(this.getSpecies(), (IEnumForm)form, this.pokemon.getGender(), customTexture, this.pokemon.isShiny()).toString();
   }

   public String getPokemonName() {
      return this.getSpecies().name;
   }

   public EnumSpecies getSpecies() {
      return this.pokemon.getSpecies();
   }

   public EnumNature getNature() {
      return this.pokemon.getNature();
   }

   public EnumNature getBaseNature() {
      return this.pokemon.getBaseNature();
   }

   public Level getLevel() {
      return this.tempLevel ? this.temporaryLevel : this.pokemon.getLevelContainer();
   }

   public int getLevelNum() {
      return this.tempLevel ? this.temporaryLevel.getLevel() : this.pokemon.getLevel();
   }

   public int getExp() {
      return this.tempLevel ? this.temporaryLevel.getExp() : this.pokemon.getExperience();
   }

   public void setLevelNum(int level) {
      if (this.tempLevel) {
         this.temporaryLevel.setLevel(level);
      } else {
         this.pokemon.setLevelNum(level);
      }

   }

   public void setExp(int experience) {
      this.pokemon.setExperience(experience);
   }

   public void setTempLevel(int level) {
      this.tempLevel = true;
      this.temporaryLevel = new TempBattleLevel(new DelegateLink(this.pokemon), level);
   }

   public Pokemon getInnerLink() {
      return this.pokemon;
   }

   public int getPartyPosition() {
      return this.partyPosition;
   }

   public void enableReturnHeldItem() {
      this.returnHeldItem = true;
   }

   public void writeToNBT() {
      if (this.pokemon != null) {
         if (!this.tempLevel && this.entity != null) {
            this.entity.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.pokemon.getMaxHealth());
         }

         if (this.tempLevel) {
            float heath = this.pokemon.getHealthPercentage();
            this.pokemon.getStats().setLevelStats(this.pokemon.getNature(), this.pokemon.getBaseStats(), this.pokemon.getLevel());
            this.setHealth(Math.round(heath / 100.0F * (float)this.pokemon.getMaxHealth()));
            this.update(EnumUpdateType.Stats);
         }

         if (this.bc.rules.fullHeal) {
            this.pokemon.setHealth(this.startHealth);
         } else {
            this.pokemon.setStatus(this.getPrimaryStatus());
         }

         if (!this.tempLevel) {
         }

         if (this.getHeldItem() != this.initialCopyOfPokemon.getHeldItemAsItemHeld() && this.returnHeldItem) {
            this.pokemon.setHeldItem(this.initialCopyOfPokemon.getHeldItem());
         }

         this.pokemon.markDirty();
      }
   }

   public void updateHPIncrease() {
      if (this.bc != null) {
         this.bc.sendToPlayers(new HPIncreaseTask(this.getPokemonUUID(), this.getLevelNum(), this.getHealth(), this.getMaxHealth()));
      }

   }

   public boolean isDynamax() {
      return this.isDynamax > 0;
   }

   public boolean isGigantamax() {
      return this.isDynamax == 2;
   }

   public void setRaidShields(int shields) {
      this.maxShields = shields;
      this.shields = shields;
      if (this.bc != null) {
         this.bc.sendToPlayers(new RaidShieldsTask(this.getPokemonUUID(), this.shields, this.maxShields));
      }

   }

   public void updateRaidShields(int shields) {
      this.shields = shields;
      if (this.bc != null) {
         this.bc.sendToPlayers(new RaidShieldsTask(this.getPokemonUUID(), this.shields, this.maxShields));
      }

   }

   public void addGlobalStatus(GlobalStatusBase g) {
      this.bc.globalStatusController.addGlobalStatus(g);
      this.getHeldItem().onGlobalStatusAdded(this, g);
   }
}
