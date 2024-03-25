package com.pixelmonmod.pixelmon.battles.attacks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.AttackModifierBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.CriticalHit;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.MultipleHit;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Assurance;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.BeatUp;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Drain;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Fling;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.FreezeDry;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.IgnoreDefense;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TripleKick;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnSpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.clauses.SkyBattle;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AngerPoint;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Corrosion;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.KeenEye;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.LongReach;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Merciless;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ParentalBond;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Sniper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SuperLuck;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Unaware;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;

public class Attack {
   public static final float EFFECTIVE_NORMAL = 1.0F;
   public static final float EFFECTIVE_SUPER = 2.0F;
   public static final float EFFECTIVE_MAX = 4.0F;
   public static final float EFFECTIVE_NOT = 0.5F;
   public static final float EFFECTIVE_BARELY = 0.25F;
   public static final float EFFECTIVE_NONE = 0.0F;
   public static final int ATTACK_PHYSICAL = 0;
   public static final int ATTACK_SPECIAL = 1;
   public static final int ATTACK_STATUS = 2;
   public static final int NEVER_MISS = -1;
   public static final int IGNORE_SEMIINVULNERABLE = -2;
   private AttackBase baseAttack;
   private AttackBase overrideAttack = null;
   public int pp;
   public int ppLevel;
   public int movePower;
   public int overridePower = -1;
   public int moveAccuracy;
   public boolean cantMiss;
   private boolean disabled;
   public MoveResults moveResult;
   public float damageResult;
   public boolean didCrit = false;
   public AttackBase savedAttack;
   public int savedPower;
   public int savedAccuracy;
   private Integer overridePPMax = null;
   private AttackCategory overrideAttackCategory = null;
   private EnumType overrideType = null;
   public transient boolean hasPlayedAnimationOnce = false;
   public transient boolean isZ = false;
   public transient boolean isMax = false;
   public transient Attack originalMove = null;
   public transient boolean fromDancer = false;

   public Attack(AttackBase base) {
      this.initializeAttack(base);
   }

   public Attack(String moveName) {
      AttackBase.getAttackBase(moveName).ifPresent(this::initializeAttack);
      if (this.getMove() == null) {
         Pixelmon.LOGGER.error("No attack found with name: " + moveName);
      }

   }

   public Attack(int attackIndex) {
      AttackBase.getAttackBase(attackIndex).ifPresent(this::initializeAttack);
      if (this.getMove() == null) {
         Pixelmon.LOGGER.error("No attack found with index: " + attackIndex);
      }

   }

   public void initializeAttack(AttackBase base) {
      this.baseAttack = base;
      this.movePower = this.getMove().getBasePower();
      this.pp = this.getMove().getPPBase();
   }

   public AttackBase getMove() {
      return this.overrideAttack != null ? this.overrideAttack : this.baseAttack;
   }

   public AttackBase getActualMove() {
      return this.baseAttack;
   }

   public int getMaxPP() {
      return this.overridePPMax != null ? this.overridePPMax : this.getMove().getPPBase() + (int)((double)this.getMove().getPPBase() * 0.2 * (double)this.ppLevel);
   }

   public void overridePPMax(int pp) {
      this.overridePPMax = pp == -1 ? null : pp;
   }

   public Integer getOverriddenPPMax() {
      return this.overridePPMax;
   }

   public AttackCategory getAttackCategory() {
      return this.overrideAttackCategory != null ? this.overrideAttackCategory : this.getMove().getAttackCategory();
   }

   public void overrideAttackCategory(AttackCategory category) {
      this.overrideAttackCategory = category;
   }

   public EnumType getType() {
      return this.overrideType != null ? this.overrideType : this.getActualType();
   }

   public EnumType getActualType() {
      return this.getMove().getAttackType();
   }

   public void overrideType(EnumType type) {
      this.overrideType = type;
   }

   public void resetMove() {
      this.overrideAttack = null;
      this.overrideType((EnumType)null);
      this.overrideAttackCategory((AttackCategory)null);
   }

   public void resetOverridePower() {
      this.overridePower = -1;
   }

   public boolean use(PixelmonWrapper user, PixelmonWrapper target, MoveResults moveResults) {
      return this.use(user, target, moveResults, (ZMove)null);
   }

   public boolean use(PixelmonWrapper user, PixelmonWrapper target, MoveResults moveResults, ZMove zMove) {
      boolean z = zMove != null;
      if (z) {
         if (user.skipZConvert) {
            user.skipZConvert = false;
         } else {
            this.isZ = true;
            Optional opt = AttackBase.getAttackBase(zMove.attackName);
            if (opt.isPresent()) {
               this.overrideAttack = (AttackBase)opt.get();
               if (this.overrideAttack.getAttackCategory() != AttackCategory.STATUS) {
                  this.overrideAttackCategory(this.getActualMove().getAttackCategory());
               }
            }
         }
      }

      this.moveResult = moveResults;
      this.damageResult = -1.0F;
      if (user.bc != null && target.bc != null) {
         EnumType type = user.getBattleAbility().modifyType(user, user.attack);
         if (type != null) {
            user.attack.overrideType(type);
         }

         if (!this.checkSkyBattle(user.bc)) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            moveResults.result = AttackResult.failed;
            return false;
         } else if (target.getParticipant() != null && target.getParticipant().onTargeted(user, this)) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            moveResults.result = AttackResult.failed;
            return false;
         } else {
            AbilityBase userAbility = user.getBattleAbility();
            AbilityBase targetAbility = target.getBattleAbility();
            if (!this.canHit(user, target) && !this.canHitNoTarget()) {
               moveResults.result = AttackResult.notarget;
               return true;
            } else {
               ArrayList effects = new ArrayList(this.getMove().effects);
               boolean shouldNotLosePP = false;

               int livePower;
               EffectBase e;
               for(livePower = 0; livePower < effects.size(); ++livePower) {
                  e = (EffectBase)effects.get(livePower);
                  if (e instanceof MultiTurnSpecialAttackBase) {
                     shouldNotLosePP = ((MultiTurnSpecialAttackBase)e).shouldNotLosePP(user);
                  }
               }

               Iterator var36;
               Iterator var39;
               if (user == target) {
                  var36 = user.bc.getActiveUnfaintedPokemon().iterator();

                  while(var36.hasNext()) {
                     PixelmonWrapper activePokemon = (PixelmonWrapper)var36.next();
                     var39 = activePokemon.getStatuses().iterator();

                     while(var39.hasNext()) {
                        StatusBase status = (StatusBase)var39.next();
                        if (status.stopsSelfStatusMove(activePokemon, user, this)) {
                           moveResults.result = AttackResult.failed;
                           return true;
                        }
                     }
                  }

                  if (!user.bc.simulateMode) {
                     var36 = this.getMove().animations.iterator();

                     label376:
                     while(true) {
                        AttackAnimation anim;
                        do {
                           do {
                              if (!var36.hasNext()) {
                                 break label376;
                              }

                              anim = (AttackAnimation)var36.next();
                           } while(anim.usedOncePerTurn() && this.hasPlayedAnimationOnce);
                        } while(user != target && !(user.entity.func_70032_d(target.entity) < 20.0F));

                        BattleControllerBase.currentAnimations.add(anim.instantiate(user, target, this));
                     }
                  }

                  this.applySelfStatusMove(user, moveResults);
                  return true;
               } else {
                  if (user.targets.size() == 1) {
                     ArrayList opponents = user.bc.getOpponentPokemon(user.getParticipant());
                     if (opponents.size() > 1) {
                        Iterator var37 = opponents.iterator();

                        label565:
                        while(true) {
                           PixelmonWrapper pw;
                           do {
                              if (!var37.hasNext()) {
                                 break label565;
                              }

                              pw = (PixelmonWrapper)var37.next();
                           } while(pw == target);

                           Iterator var14 = pw.getStatuses().iterator();

                           while(var14.hasNext()) {
                              StatusBase status = (StatusBase)var14.next();
                              if (status.redirectAttack(user, pw, this)) {
                                 target = pw;
                                 break;
                              }
                           }

                           if (pw.getBattleAbility().redirectAttack(user, pw, this)) {
                              target = pw;
                              break;
                           }
                        }
                     }
                  }

                  if (z && !zMove.attackName.equals(this.getMove().getAttackName()) && this.getAttackCategory() != AttackCategory.STATUS) {
                     effects.clear();
                     effects.addAll(zMove.effects);
                  }

                  var36 = effects.iterator();

                  while(var36.hasNext()) {
                     e = (EffectBase)var36.next();

                     try {
                        if (e.applyEffectStart(user, target) != AttackResult.proceed) {
                           return true;
                        }
                     } catch (Exception var31) {
                        var31.printStackTrace();
                     }
                  }

                  livePower = this.getMove().getBasePower();
                  if (z) {
                     livePower = zMove.basePower;
                  } else if (this.overridePower > -1) {
                     livePower = this.overridePower;
                  }

                  int[] modifiedMoveStats = userAbility.modifyPowerAndAccuracyUser(livePower, this.getMove().getAccuracy(), user, target, this);

                  PixelmonWrapper teammate;
                  for(var39 = user.bc.getTeamPokemon(user).iterator(); var39.hasNext(); modifiedMoveStats = teammate.getBattleAbility().modifyPowerAndAccuracyTeammate(modifiedMoveStats[0], modifiedMoveStats[1], user, target, this)) {
                     teammate = (PixelmonWrapper)var39.next();
                  }

                  modifiedMoveStats = targetAbility.modifyPowerAndAccuracyTarget(modifiedMoveStats[0], modifiedMoveStats[1], user, target, this);
                  int saveAccuracy = 0;
                  if (modifiedMoveStats[1] < 0) {
                     saveAccuracy = modifiedMoveStats[1];
                  }

                  modifiedMoveStats = user.getUsableHeldItem().modifyPowerAndAccuracyUser(modifiedMoveStats, user, target, this);
                  modifiedMoveStats = target.getUsableHeldItem().modifyPowerAndAccuracyTarget(modifiedMoveStats, user, target, this);
                  int beforeSize = user.getStatuses().size();

                  StatusBase status;
                  for(int i = 0; i < beforeSize; ++i) {
                     status = user.getStatus(i);
                     modifiedMoveStats = status.modifyPowerAndAccuracyUser(modifiedMoveStats[0], modifiedMoveStats[1], user, target, this);
                     if (user.getStatuses().size() != beforeSize) {
                        break;
                     }
                  }

                  Iterator var45;
                  for(var45 = target.getStatuses().iterator(); var45.hasNext(); modifiedMoveStats = status.modifyPowerAndAccuracyTarget(modifiedMoveStats[0], modifiedMoveStats[1], user, target, this)) {
                     status = (StatusBase)var45.next();
                  }

                  for(var45 = user.bc.globalStatusController.getGlobalStatuses().iterator(); var45.hasNext(); modifiedMoveStats = status.modifyPowerAndAccuracyTarget(modifiedMoveStats[0], modifiedMoveStats[1], user, target, this)) {
                     status = (StatusBase)var45.next();
                  }

                  if (saveAccuracy < 0 && modifiedMoveStats[1] >= 0) {
                     modifiedMoveStats[1] = saveAccuracy;
                  }

                  this.movePower = modifiedMoveStats[0];
                  if (!user.inMultipleHit && this.moveAccuracy >= 0) {
                     this.moveAccuracy = Math.min(modifiedMoveStats[1], 100);
                  }

                  this.cantMiss = false;
                  if (user.entity != null && target.entity != null) {
                     user.entity.func_70671_ap().func_75651_a(target.entity, 0.0F, 0.0F);
                  }

                  double accuracy = (double)this.moveAccuracy;
                  if (this.moveAccuracy >= 0) {
                     int evasion = target.getBattleStats().getEvasionStage();
                     if (user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
                        evasion = Math.max(-6, evasion - 2);
                     }

                     if (user.getBattleAbility() instanceof KeenEye) {
                        evasion = Math.min(0, evasion);
                     }

                     if (this.getMove().hasEffect(IgnoreDefense.class) || userAbility instanceof Unaware || target.hasStatus(StatusType.Foresight)) {
                        evasion = 0;
                     }

                     double combinedAccuracy = (double)(user.getBattleStats().getAccuracyStage() - evasion);
                     if (combinedAccuracy > 6.0) {
                        combinedAccuracy = 6.0;
                     } else if (combinedAccuracy < -6.0) {
                        combinedAccuracy = -6.0;
                     }

                     accuracy = (double)this.moveAccuracy * ((double)user.getBattleStats().GetAccOrEva(combinedAccuracy) / 100.0);
                  }

                  ArrayList allStatuses = new ArrayList(target.getStatuses());
                  allStatuses.addAll((Collection)target.bc.globalStatusController.getGlobalStatuses().stream().collect(Collectors.toList()));
                  Iterator var50 = user.getStatuses().iterator();

                  StatusBase e;
                  while(var50.hasNext()) {
                     e = (StatusBase)var50.next();

                     try {
                        if (e.stopsIncomingAttackUser(target, user)) {
                           return !shouldNotLosePP;
                        }
                     } catch (Exception var30) {
                        user.bc.battleLog.onCrash(var30, "Error calculating stopsIncomingAttack for " + e.type.toString() + " for attack " + this.getMove().getLocalizedName());
                     }
                  }

                  var50 = allStatuses.iterator();

                  while(var50.hasNext()) {
                     e = (StatusBase)var50.next();

                     try {
                        if (e.stopsIncomingAttack(target, user)) {
                           this.onMiss(user, target, moveResults, e);
                           return !shouldNotLosePP;
                        }
                     } catch (Exception var29) {
                        user.bc.battleLog.onCrash(var29, "Error calculating stopsIncomingAttack for " + e.type.toString() + " for attack " + this.getMove().getLocalizedName());
                     }
                  }

                  boolean targetAbilityIsCause = true;
                  boolean allowed = target.getBattleAbility(user).allowsIncomingAttack(target, user, this);
                  if (allowed) {
                     allowed = target.getUsableHeldItem().allowsIncomingAttack(target, user, this);
                     if (!allowed) {
                        targetAbilityIsCause = false;
                     }
                  }

                  if (allowed) {
                     Iterator var20 = target.bc.getTeamPokemon(target).iterator();

                     while(var20.hasNext()) {
                        PixelmonWrapper ally = (PixelmonWrapper)var20.next();
                        if (!ally.getBattleAbility().allowsIncomingAttackTeammate(ally, target, user, this)) {
                           allowed = false;
                           break;
                        }
                     }
                  }

                  if (!allowed) {
                     try {
                        if (targetAbilityIsCause) {
                           this.onMiss(user, target, moveResults, targetAbility);
                        } else {
                           this.onMiss(user, target, moveResults, target.getUsableHeldItem());
                        }

                        return !shouldNotLosePP;
                     } catch (Exception var33) {
                        user.bc.battleLog.onCrash(var33, "Error calculating allowsIncomingAttack for attack " + this.getMove().getLocalizedName());
                     }
                  }

                  if (!user.getBattleAbility().allowsOutgoingAttack(user, target, this)) {
                     this.onMiss(user, target, moveResults, targetAbility);
                     return !shouldNotLosePP;
                  } else if (this.hasNoEffect(user, target)) {
                     this.onMiss(user, target, moveResults, EnumType.Mystery);
                     return !shouldNotLosePP;
                  } else {
                     if (!shouldNotLosePP) {
                        targetAbility.preProcessAttack(target, user, this);
                        userAbility.preProcessAttackUser(user, target, this);
                     }

                     this.cantMiss = z || this.cantMiss(user) || this.moveAccuracy < 0;
                     if (user.bc.simulateMode) {
                        this.moveResult.accuracy = this.moveAccuracy;
                        accuracy = 100.0;
                     }

                     AttackEvent.Use event = new AttackEvent.Use(user, target, this.getMove(), accuracy, this.cantMiss);
                     Pixelmon.EVENT_BUS.post(event);
                     accuracy = event.accuracy;
                     this.cantMiss = event.cantMiss;
                     CriticalHit critModifier = null;
                     if (!this.cantMiss && !RandomHelper.getRandomChance((int)accuracy)) {
                        this.onMiss(user, target, moveResults, (Object)null);
                     } else {
                        AttackResult finalResult = AttackResult.proceed;
                        AttackResult applyEffectResult = AttackResult.proceed;
                        Iterator var24 = effects.iterator();

                        EffectBase e;
                        while(var24.hasNext()) {
                           e = (EffectBase)var24.next();

                           try {
                              if (e instanceof AttackModifierBase) {
                                 if (e instanceof CriticalHit) {
                                    critModifier = (CriticalHit)e;
                                 } else {
                                    applyEffectResult = ((AttackModifierBase)e).applyEffectDuring(user, target);
                                    if (applyEffectResult != AttackResult.proceed) {
                                       finalResult = applyEffectResult;
                                    }
                                 }
                              } else if (e instanceof SpecialAttackBase) {
                                 applyEffectResult = ((SpecialAttackBase)e).applyEffectDuring(user, target);
                                 if (applyEffectResult != AttackResult.proceed) {
                                    finalResult = applyEffectResult;
                                 }
                              } else if (e instanceof MultiTurnSpecialAttackBase) {
                                 applyEffectResult = ((MultiTurnSpecialAttackBase)e).applyEffectDuring(user, target);
                                 if (applyEffectResult != AttackResult.proceed) {
                                    break;
                                 }
                              }

                              if (finalResult != AttackResult.succeeded && finalResult != AttackResult.failed && finalResult != AttackResult.charging && finalResult != AttackResult.notarget) {
                                 if (finalResult == AttackResult.hit) {
                                    if (target.isAlive()) {
                                       moveResults.result = AttackResult.hit;
                                    } else {
                                       moveResults.result = AttackResult.killed;
                                    }
                                 }
                              } else {
                                 moveResults.result = finalResult;
                              }
                           } catch (Exception var32) {
                              user.bc.battleLog.onCrash(var32, "Error in applyEffect for " + e.getClass().toString() + " for attack " + this.getMove().getLocalizedName());
                           }
                        }

                        if (moveResults.result.isSuccess() || moveResults.result == AttackResult.charging) {
                           this.playAnimation(user, target);
                        }

                        if (applyEffectResult == AttackResult.proceed) {
                           if (userAbility instanceof ParentalBond) {
                              user.inMultipleHit = true;
                              user.inParentalBond = true;
                              var24 = effects.iterator();

                              label433:
                              while(true) {
                                 while(true) {
                                    if (!var24.hasNext()) {
                                       break label433;
                                    }

                                    e = (EffectBase)var24.next();
                                    if (!(e instanceof BeatUp) && !(e instanceof Fling) && !(e instanceof MultiTurnCharge) && !(e instanceof MultipleHit) && !(e instanceof TripleKick)) {
                                       if (e instanceof Assurance) {
                                          this.getMove().setBasePower(this.getMove().getBasePower() * 2);
                                       }
                                    } else {
                                       user.inMultipleHit = false;
                                       user.inParentalBond = false;
                                    }
                                 }
                              }
                           }

                           this.playAnimation(user, target);
                           this.hasPlayedAnimationOnce = true;
                           this.executeAttackEffects(user, target, moveResults, critModifier, 1.0F);
                           if (user.inParentalBond && this.getAttackCategory() != AttackCategory.STATUS && target.isAlive() && user.isAlive() && user.targets.size() == 1) {
                              var24 = effects.iterator();

                              while(var24.hasNext()) {
                                 e = (EffectBase)var24.next();
                                 if (e instanceof Assurance) {
                                    this.getMove().setBasePower(this.getMove().getBasePower() * 2);
                                 }
                              }

                              user.inMultipleHit = false;
                              user.inParentalBond = false;
                              this.executeAttackEffects(user, target, moveResults, critModifier, 0.25F);
                              user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
                           }

                           user.inParentalBond = false;
                           var24 = effects.iterator();

                           while(var24.hasNext()) {
                              e = (EffectBase)var24.next();
                              if (e instanceof SpecialAttackBase) {
                                 ((SpecialAttackBase)e).applyAfterEffect(user);
                              }
                           }

                           target.getBattleAbility().postProcessAttack(target, user, this);
                           user.getBattleAbility().postProcessAttackUser(user, target, this);
                           user.getHeldItem().postProcessAttackUser(user, user, this);
                           var24 = user.bc.getActivePokemon().iterator();

                           while(var24.hasNext()) {
                              PixelmonWrapper wrapper = (PixelmonWrapper)var24.next();
                              wrapper.getBattleAbility().postProcessAttackOther(wrapper, user, target, this);
                           }
                        }
                     }

                     for(int i = 0; i < target.getStatusSize(); ++i) {
                        int sizeBefore = target.getStatusSize();
                        target.getStatus(i).onAttackEnd(target);
                        if (sizeBefore > target.getStatusSize()) {
                           --i;
                        }
                     }

                     if (!user.bc.simulateMode) {
                        EnumUpdateType[] updateTypes = new EnumUpdateType[]{EnumUpdateType.HP, EnumUpdateType.Moveset};
                        if (user.getPlayerOwner() != null) {
                           user.update(updateTypes);
                        }

                        if (target.getPlayerOwner() != null) {
                           target.update(updateTypes);
                        }
                     }

                     PlayerPartyStorage pps;
                     try {
                        pps = Pixelmon.storageManager.getParty(user.pokemon.getOwnerPlayer());
                        pps.getQuestData(true).receive("BATTLE_MOVE_USER", user.pokemon, target.pokemon, this, moveResults);
                     } catch (Exception var28) {
                     }

                     try {
                        pps = Pixelmon.storageManager.getParty(target.pokemon.getOwnerPlayer());
                        pps.getQuestData(true).receive("BATTLE_MOVE_TARGET", user.pokemon, target.pokemon, this, moveResults);
                     } catch (Exception var27) {
                     }

                     return !shouldNotLosePP;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean hasNoEffect(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hasNoEffect = this.getTypeEffectiveness(user, target) == 0.0 && (this.getAttackCategory() != AttackCategory.STATUS || this.isAttack("Poison Gas", "Poison Powder", "Thunder Wave", "Toxic"));
      if (!hasNoEffect && target.hasType(EnumType.Grass) && Overcoat.isPowderMove(this)) {
         hasNoEffect = true;
      }

      for(int o = 0; o < this.getMove().effects.size(); ++o) {
         if (this.getMove().effects.get(o) instanceof MultiTurnSpecialAttackBase && ((MultiTurnSpecialAttackBase)this.getMove().effects.get(o)).ignoresType(user)) {
            hasNoEffect = false;
            break;
         }
      }

      return hasNoEffect;
   }

   private void executeAttackEffects(PixelmonWrapper user, PixelmonWrapper target, MoveResults moveResults, EffectBase critModifier, float damageMultiplier) {
      double crit = calcCriticalHit(critModifier, user, target);
      int power = (int)((float)this.doDamageCalc(user, target, crit) * damageMultiplier);
      this.damageResult = (float)power;
      if (this.getAttackCategory() == AttackCategory.STATUS) {
         int power = false;
      } else if (target.isAlive()) {
         if (crit > 1.0) {
            if (user.targets.size() > 1) {
               user.bc.sendToAll("pixelmon.battletext.criticalhittarget", target.getNickname());
            } else {
               user.bc.sendToAll("pixelmon.battletext.criticalhit");
            }
         }

         this.damageResult = target.doBattleDamage(user, (float)power, DamageTypeEnum.ATTACK);
         if (target.isAlive()) {
            moveResults.result = AttackResult.hit;
         } else {
            moveResults.result = AttackResult.killed;
         }
      } else {
         this.damageResult = -1.0F;
         moveResults.result = AttackResult.notarget;
      }

      AttackResult tempResult = this.applyAttackEffect(user, target);
      if (tempResult != null) {
         moveResults.result = tempResult;
      }

      applyContactLate(user, target);
      if (this.canRemoveBerry()) {
         target.getUsableHeldItem().tookDamage(user, target, this.damageResult, DamageTypeEnum.ATTACK);
      }

   }

   public void playAnimation(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.bc.simulateMode && user.entity != null && target.entity != null) {
         if (user.entity.field_70165_t == 0.0 && user.entity.field_70163_u == 0.0 && user.entity.field_70161_v == 0.0) {
            EntityLivingBase owner = user.getParticipant().getEntity();
            if (owner != null) {
               user.entity.func_70012_b(owner.field_70165_t, owner.field_70163_u, owner.field_70161_v, owner.field_70177_z, 0.0F);
            }
         }

         if (!user.bc.simulateMode) {
            Iterator var5 = this.getMove().animations.iterator();

            while(true) {
               AttackAnimation anim;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  anim = (AttackAnimation)var5.next();
               } while(anim.usedOncePerTurn() && this.hasPlayedAnimationOnce);

               BattleControllerBase.currentAnimations.add(anim.instantiate(user, target, this));
            }
         }
      }
   }

   public int doDamageCalc(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper, double crit) {
      if (this.movePower <= 0) {
         return 0;
      } else {
         AbilityBase userAbility = userWrapper.getBattleAbility();
         double stab = 1.0;
         if (this.hasSTAB(userWrapper)) {
            stab = 1.5;
         }

         stab = userAbility.modifyStab(stab);
         AttackEvent.Stab stabEvent = new AttackEvent.Stab(userWrapper, targetWrapper, stab);
         Pixelmon.EVENT_BUS.post(stabEvent);
         stab = stabEvent.stabMultiplier;
         double type = this.getTypeEffectiveness(userWrapper, targetWrapper);
         double modifier = stab * type * crit;
         double attack = 0.0;
         double defense = 0.0;
         double level = (double)userWrapper.getLevelNum();
         StatsType attackStat;
         StatsType defenseStat;
         if (this.getAttackCategory() == AttackCategory.SPECIAL) {
            attackStat = StatsType.SpecialAttack;
            if (this.isAttack("Psyshock", "Psystrike", "Secret Sword")) {
               defenseStat = StatsType.Defence;
            } else {
               defenseStat = StatsType.SpecialDefence;
            }
         } else {
            attackStat = StatsType.Attack;
            defenseStat = StatsType.Defence;
         }

         attack = (double)userWrapper.getBattleStats().getStatWithMod(attackStat);
         defense = (double)targetWrapper.getBattleStats().getStatWithMod(defenseStat);
         if (crit > 1.0) {
            attack = Math.max((double)userWrapper.getBattleStats().getStatFromEnum(attackStat), attack);
            defense = Math.min((double)targetWrapper.getBattleStats().getStatFromEnum(defenseStat), defense);
         }

         if (this.getMove().hasEffect(IgnoreDefense.class) || userAbility instanceof Unaware) {
            defense = (double)targetWrapper.getBattleStats().getStatFromEnum(defenseStat);
         }

         if (this.isAttack("Beat Up") || targetWrapper.getBattleAbility(userWrapper) instanceof Unaware) {
            attack = (double)userWrapper.getBattleStats().getStatFromEnum(attackStat);
         }

         double dmgRand = (double)RandomHelper.getRandomNumberBetween(85, 100) / 100.0;
         if (userWrapper.bc.simulateMode) {
            dmgRand = 1.0;
         }

         double damageBase = (2.0 * level / 5.0 + 2.0) * attack * (double)this.movePower * 0.02 / defense + 2.0;
         double damage = damageBase * modifier * dmgRand;
         damage = userWrapper.getUsableHeldItem().preProcessDamagingAttackUser(userWrapper, targetWrapper, this, damage);
         damage = targetWrapper.getUsableHeldItem().preProcessDamagingAttackTarget(userWrapper, targetWrapper, this, damage);
         if (userWrapper.targets.size() > 1) {
            damage *= 0.75;
         }

         AttackEvent.Damage damageEvent = new AttackEvent.Damage(userWrapper, targetWrapper, damage);
         Pixelmon.EVENT_BUS.post(damageEvent);
         damage = damageEvent.damage;
         if (damage < 1.0 && damage > 0.0 && this.movePower > 0) {
            damage = 1.0;
         }

         return (int)damage;
      }
   }

   public void applySelfStatusMove(PixelmonWrapper user, MoveResults moveResults) {
      if (user.entity != null) {
         user.entity.func_70671_ap().func_75651_a(user.entity, 0.0F, 0.0F);
      }

      for(int j = 0; j < this.getMove().effects.size(); ++j) {
         EffectBase e = (EffectBase)this.getMove().effects.get(j);

         try {
            if (e instanceof StatsEffect) {
               AttackResult statsResult = ((StatsEffect)e).applyStatEffect(user, user, this.getMove());
               if (moveResults.result != AttackResult.succeeded) {
                  moveResults.result = statsResult;
               }
            } else if (e instanceof SpecialAttackBase) {
               if (e.applyEffectStart(user, user) != AttackResult.proceed) {
                  return;
               }

               this.moveResult.result = ((SpecialAttackBase)e).applyEffectDuring(user, user);
            } else if (e instanceof MultiTurnSpecialAttackBase) {
               this.moveResult.result = ((MultiTurnSpecialAttackBase)e).applyEffectDuring(user, user);
            } else if (e instanceof StatusBase) {
               e.applyEffect(user, user);
            }
         } catch (Exception var6) {
            user.bc.battleLog.onCrash(var6, "Error in applyEffect for " + e.getClass().toString() + " for attack " + this.getMove().getLocalizedName());
         }
      }

      user.getUsableHeldItem().onStatModified(user);
      if (!user.bc.simulateMode && user.getPlayerOwner() != null) {
         user.update(EnumUpdateType.HP);
         user.setTemporaryMoveset(user.temporaryMoveset);
      }

      if (moveResults.result == AttackResult.proceed) {
         moveResults.result = AttackResult.succeeded;
      }

      if (moveResults.result == AttackResult.succeeded) {
         user.getBattleAbility().postProcessAttackUser(user, user, this);
         user.getHeldItem().postProcessAttackUser(user, user, this);
         Iterator var7 = user.bc.getActivePokemon().iterator();

         while(var7.hasNext()) {
            PixelmonWrapper wrapper = (PixelmonWrapper)var7.next();
            wrapper.getBattleAbility().postProcessAttackOther(wrapper, user, user, this);
         }
      }

   }

   public AttackResult applyAttackEffect(PixelmonWrapper user, PixelmonWrapper target) {
      AttackResult returnResult = null;

      for(int j = 0; j < this.getMove().effects.size(); ++j) {
         EffectBase e = (EffectBase)this.getMove().effects.get(j);

         try {
            if (e instanceof StatsEffect) {
               StatsEffect statsEffect = (StatsEffect)e;
               boolean abilityAllowsChange = target.getBattleAbility(user).allowsStatChange(target, user, statsEffect);
               if (abilityAllowsChange) {
                  Iterator var8 = target.bc.getTeamPokemon(target).iterator();

                  while(var8.hasNext()) {
                     PixelmonWrapper ally = (PixelmonWrapper)var8.next();
                     if (!ally.getBattleAbility().allowsStatChangeTeammate(ally, target, user, statsEffect)) {
                        abilityAllowsChange = false;
                        break;
                     }
                  }
               }

               if (abilityAllowsChange) {
                  AttackResult statsResult = statsEffect.applyStatEffect(user, target, this.getMove());
                  if (returnResult != AttackResult.succeeded) {
                     returnResult = statsResult;
                  }
               }
            } else if (e instanceof StatusBase) {
               boolean shouldApply = true;

               for(int i = 0; i < target.getStatusSize(); ++i) {
                  StatusBase et = target.getStatus(i);
                  if (user == target && et.stopsStatusChange(et.type, target, user)) {
                     shouldApply = false;
                     break;
                  }
               }

               if (shouldApply) {
                  e.applyEffect(user, target);
               }
            } else {
               e.applyEffect(user, target);
            }
         } catch (Exception var10) {
            user.bc.battleLog.onCrash(var10, "Error in applyEffect for " + e.getClass().toString() + " for attack " + this.getMove().getLocalizedName());
         }
      }

      user.getUsableHeldItem().onStatModified(user);
      return returnResult;
   }

   public static void applyContact(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasStatus(StatusType.Substitute)) {
         user.getBattleAbility().applyEffectOnContactUser(user, target);
         if (user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.protectivePads || user.getBattleAbility().isAbility(LongReach.class)) {
            return;
         }

         target.getUsableHeldItem().applyEffectOnContact(user, target);
         target.getBattleAbility().applyEffectOnContactTarget(user, target);
      }

   }

   public static void applyContactLate(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack != null && user.attack.getMove().getMakesContact() && !target.hasStatus(StatusType.Substitute) && !user.getBattleAbility().isAbility(LongReach.class)) {
         target.getBattleAbility().applyEffectOnContactTargetLate(user, target);
      }

   }

   public static void postProcessAttackAllHits(PixelmonWrapper user, PixelmonWrapper target, Attack attack, float power, DamageTypeEnum damageType, boolean onSubstitute) {
      Iterator var6;
      EffectBase effect;
      if (!user.inParentalBond && attack != null) {
         var6 = attack.getMove().effects.iterator();

         while(var6.hasNext()) {
            effect = (EffectBase)var6.next();
            effect.dealtDamage(user, target, attack, damageType);
         }

         if (!onSubstitute) {
            target.getBattleAbility().tookDamageTargetAfterMove(user, target, attack, power);
            target.getUsableHeldItem().postProcessDamagingAttackTarget(user, target, attack, power);
         }

         user.getUsableHeldItem().dealtDamage(user, target, attack, damageType);
      } else if (user.inParentalBond && attack.getMove().hasEffect(Drain.class)) {
         var6 = attack.getMove().effects.iterator();

         while(var6.hasNext()) {
            effect = (EffectBase)var6.next();
            effect.dealtDamage(user, target, attack, damageType);
         }
      }

   }

   public void onMiss(PixelmonWrapper user, PixelmonWrapper target, MoveResults results, Object cause) {
      try {
         Iterator var5 = this.getMove().effects.iterator();

         EffectBase effect;
         while(var5.hasNext()) {
            effect = (EffectBase)var5.next();
            if (effect instanceof MultiTurnSpecialAttackBase && ((MultiTurnSpecialAttackBase)effect).isCharging(user, target)) {
               results.result = ((MultiTurnSpecialAttackBase)effect).applyEffectDuring(user, target);
               if (results.result == AttackResult.charging) {
                  return;
               }
            }
         }

         if (cause instanceof StatusBase) {
            ((StatusBase)cause).stopsIncomingAttackMessage(target, user);
         } else if (cause instanceof AbilityBase) {
            ((AbilityBase)cause).allowsIncomingAttackMessage(target, user, this);
         } else if (cause instanceof ItemHeld) {
            ((ItemHeld)cause).allowsIncomingAttackMessage(target, user, this);
         } else if (cause instanceof EnumType) {
            user.bc.sendToAll("pixelmon.battletext.noeffect", target.getNickname());
         } else {
            user.bc.sendToAll("pixelmon.battletext.missedattack", target.getNickname());
            results.result = AttackResult.missed;
         }

         var5 = this.getMove().effects.iterator();

         while(var5.hasNext()) {
            effect = (EffectBase)var5.next();
            effect.applyMissEffect(user, target);
         }

         user.getUsableHeldItem().onMiss(user, target, this);
      } catch (Exception var7) {
         user.bc.battleLog.onCrash(var7, "Error in applyMissEffect for attack " + this.getMove().getTranslatedName());
      }

      if (results.result != AttackResult.missed) {
         results.result = AttackResult.failed;
      }

   }

   public boolean hasSTAB(PixelmonWrapper user) {
      return user.hasType(this.getType());
   }

   public void setDisabled(boolean value, PixelmonWrapper pixelmon) {
      this.setDisabled(value, pixelmon, false);
   }

   public void setDisabled(boolean value, PixelmonWrapper pixelmon, boolean switching) {
      this.disabled = value;
      if (pixelmon != null && pixelmon.getParticipant() instanceof PlayerParticipant) {
         pixelmon.setTemporaryMoveset(pixelmon.temporaryMoveset);
      }

   }

   public boolean getDisabled() {
      return this.disabled;
   }

   public boolean canUseMove() {
      return !this.getDisabled() && this.pp > 0;
   }

   public static double calcCriticalHit(EffectBase e, PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.getBattleAbility(user).preventsCriticalHits(user) && !target.hasStatus(StatusType.LuckyChant)) {
         int critStage = 1;
         critStage += user.getUsableHeldItem().adjustCritStage(user);
         AbilityBase userAbility = user.getBattleAbility();
         if (userAbility instanceof SuperLuck) {
            ++critStage;
         }

         float percent = 0.0417F;
         if (e instanceof CriticalHit) {
            critStage += ((CriticalHit)e).stages;
         }

         critStage += user.getBattleStats().getCritStage();
         if (critStage == 2) {
            percent = 0.125F;
         } else if (critStage == 3) {
            percent = 0.5F;
         } else if (critStage >= 4) {
            percent = 1.0F;
         }

         if (user.bc.simulateMode && percent < 1.0F && !Merciless.willApply(user, target, user.attack)) {
            percent = 0.0F;
         }

         percent = user.getBattleAbility().adjustCriticalHitChance(user, target, user.attack, percent);
         double crit = 1.0;
         if (RandomHelper.getRandomChance(percent)) {
            AbilityBase targetAbility = target.getBattleAbility();
            if (targetAbility instanceof AngerPoint && !user.bc.simulateMode) {
               ((AngerPoint)targetAbility).wasCrit = true;
            }

            crit = 1.5;
            if (userAbility instanceof Sniper) {
               crit *= 1.5;
            }

            user.attack.didCrit = true;
         } else {
            user.attack.didCrit = false;
         }

         AttackEvent.CriticalHit critEvent = new AttackEvent.CriticalHit(user, target, crit);
         Pixelmon.EVENT_BUS.post(critEvent);
         return critEvent.critMultiplier;
      } else {
         return 1.0;
      }
   }

   public boolean canHit(PixelmonWrapper pixelmon1, PixelmonWrapper pixelmon2) {
      return pixelmon2 != null && !pixelmon2.isFainted();
   }

   public boolean doesPersist(PixelmonWrapper pw) {
      if (this.isAttack("Fly", "Bounce")) {
         return pw.hasStatus(StatusType.Flying);
      } else {
         for(int i = 0; i < this.getMove().effects.size(); ++i) {
            EffectBase e = (EffectBase)this.getMove().effects.get(i);

            try {
               if (e.doesPersist(pw)) {
                  return true;
               }
            } catch (Exception var5) {
               pw.bc.battleLog.onCrash(var5, "Error in doesPersist for " + e.getClass().toString() + " for attack " + this.getMove().getLocalizedName());
            }
         }

         return pw.hasStatus(StatusType.Recharge);
      }
   }

   public boolean cantMiss(PixelmonWrapper user) {
      if (this.cantMiss) {
         return true;
      } else {
         for(int i = 0; i < this.getMove().effects.size(); ++i) {
            EffectBase e = (EffectBase)this.getMove().effects.get(i);

            try {
               if ((!(e instanceof StatsEffect) || this.getMove().getAttackCategory() == AttackCategory.STATUS || !((StatsEffect)e).isUser) && e.cantMiss(user)) {
                  return true;
               }
            } catch (Exception var5) {
               user.bc.battleLog.onCrash(var5, "Error in cantMiss for " + e.getClass().toString() + " for attack " + this.getMove().getLocalizedName());
            }
         }

         return false;
      }
   }

   public void sendEffectiveChat(PixelmonWrapper user, PixelmonWrapper target) {
      String s = null;
      if (this.getAttackCategory() != AttackCategory.STATUS) {
         float effectiveness = (float)this.getTypeEffectiveness(user, target);
         if (effectiveness == 0.0F) {
            user.bc.sendToAll("pixelmon.battletext.noeffect", target.getNickname());
            return;
         }

         if (effectiveness != 0.5F && effectiveness != 0.25F) {
            if (effectiveness == 2.0F || effectiveness == 4.0F) {
               s = "pixelmon.battletext.supereffective";
            }
         } else {
            s = "pixelmon.battletext.wasnoteffective";
         }

         if (s != null) {
            if (user.targets.size() > 1) {
               user.bc.sendToAll(s.concat("target"), target.getNickname());
            } else {
               user.bc.sendToAll(s);
            }
         }
      }

   }

   public static boolean dealsDamage(Attack attack) {
      return attack != null && (attack.getMove().getBasePower() > 0 || attack.isMax && attack.getMove().getAttackName() != "Max Guard");
   }

   public void saveAttack() {
      this.savedPower = this.getMove().getBasePower();
      this.savedAccuracy = this.getMove().getAccuracy();
      this.savedAttack = this.getMove();
   }

   public void restoreAttack() {
      this.getMove().setBasePower(this.savedPower);
      this.getMove().setAccuracy(this.savedAccuracy);
      this.resetMove();
   }

   public boolean isAttack(List attacks) {
      Iterator var2 = attacks.iterator();

      String attack;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         attack = (String)var2.next();
      } while(!this.isAttack(attack));

      return true;
   }

   public boolean isAttack(String... attacks) {
      String[] var2 = attacks;
      int var3 = attacks.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String a = var2[var4];
         if (this.getMove().getAttackName().equalsIgnoreCase(a)) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasAttack(List attackList, String... attackNames) {
      Iterator var2 = attackList.iterator();

      Attack attack;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         attack = (Attack)var2.next();
      } while(attack == null || !attack.isAttack(attackNames));

      return true;
   }

   public static boolean hasOffensiveAttackType(List attackList, EnumType type) {
      Iterator var2 = attackList.iterator();

      Attack attack;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         attack = (Attack)var2.next();
      } while(attack == null || attack.getType() != type || attack.getAttackCategory() == AttackCategory.STATUS);

      return true;
   }

   public void createMoveChoices(PixelmonWrapper pw, ArrayList choices, boolean includeAllies) {
      ArrayList targets = new ArrayList();
      TargetingInfo info = this.getMove().getTargetingInfo();
      if (info.hitsSelf) {
         targets.add(pw);
      }

      if (info.hitsAdjacentAlly && (includeAllies || info.hitsAll)) {
         targets.addAll(pw.bc.getTeamPokemonExcludeSelf(pw));
      }

      if (info.hitsAdjacentFoe) {
         targets.addAll(pw.bc.getOpponentPokemon(pw));
      }

      if (info.hitsAll) {
         choices.add(new MoveChoice(pw, this, targets));
      } else {
         Iterator var6 = targets.iterator();

         while(var6.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var6.next();
            ArrayList targetArray = new ArrayList(1);
            targetArray.add(target);
            choices.add(new MoveChoice(pw, this, targetArray));
         }
      }

   }

   public ArrayList createMoveChoices(PixelmonWrapper pw, boolean includeAllies) {
      ArrayList choices = new ArrayList();
      this.createMoveChoices(pw, choices, includeAllies);
      return choices;
   }

   public ArrayList createList() {
      ArrayList list = new ArrayList(1);
      list.add(this);
      return list;
   }

   public boolean equals(Object compare) {
      return compare != null && compare instanceof Attack ? this.getMove().getAttackName().equalsIgnoreCase(((Attack)compare).getMove().getAttackName()) : false;
   }

   public int hashCode() {
      return this.getMove() == null ? 0 : this.getMove().getAttackName().hashCode();
   }

   public String toString() {
      return this.getMove().getAttackName();
   }

   public double getTypeEffectiveness(PixelmonWrapper user, PixelmonWrapper target) {
      List effectiveTypes = target.getEffectiveTypes(user, target);
      double effectiveness;
      if (effectiveTypes.contains(EnumType.Flying) && this.getMove().isAttack("Thousand Arrows")) {
         effectiveness = 1.0;
      } else {
         if (user.getBattleAbility() instanceof Corrosion && this.getMove().getAttackType() == EnumType.Poison && this.getMove().getAttackCategory() == AttackCategory.STATUS) {
            effectiveTypes.removeIf((type) -> {
               return type == EnumType.Steel;
            });
         }

         boolean inverse = user.bc.rules.hasClause("inverse");
         effectiveness = (double)EnumType.getTotalEffectiveness(effectiveTypes, this.getType(), inverse);

         Iterator var7;
         StatusBase s;
         for(var7 = target.getStatuses().iterator(); var7.hasNext(); effectiveness = inverse ? effectiveness * (double)EnumType.inverseEffectiveness((float)s.modifyTypeEffectiveness(effectiveTypes, this.getType(), 1.0, user.bc)) : s.modifyTypeEffectiveness(effectiveTypes, this.getType(), effectiveness, user.bc)) {
            s = (StatusBase)var7.next();
         }

         var7 = this.getMove().effects.iterator();

         label45:
         while(true) {
            while(true) {
               if (!var7.hasNext()) {
                  break label45;
               }

               EffectBase e = (EffectBase)var7.next();
               if (e instanceof FreezeDry && target.type.contains(EnumType.Water)) {
                  effectiveness = e.modifyTypeEffectiveness(effectiveTypes, this.getType(), effectiveness, user.bc);
               } else {
                  effectiveness = inverse ? effectiveness * (double)EnumType.inverseEffectiveness((float)e.modifyTypeEffectiveness(effectiveTypes, this.getType(), 1.0, user.bc)) : e.modifyTypeEffectiveness(effectiveTypes, this.getType(), effectiveness, user.bc);
               }
            }
         }
      }

      AttackEvent.TypeEffectiveness event = new AttackEvent.TypeEffectiveness(user, target, effectiveness);
      Pixelmon.EVENT_BUS.post(event);
      return event.getMultiplier();
   }

   public boolean canRemoveBerry() {
      return this.isAttack("Bug Bite", "Pluck", "Knock Off");
   }

   public Attack copy() {
      Attack newAttack = new Attack(this.getMove());
      newAttack.pp = this.pp;
      newAttack.ppLevel = this.ppLevel;
      return newAttack;
   }

   public Attack(Attack attack) {
      this.baseAttack = attack.baseAttack;
      this.cantMiss = attack.cantMiss;
      this.damageResult = attack.damageResult;
      this.disabled = attack.disabled;
      this.fromDancer = attack.fromDancer;
      this.hasPlayedAnimationOnce = attack.hasPlayedAnimationOnce;
      this.isMax = attack.isMax;
      this.isZ = attack.isZ;
      this.moveAccuracy = attack.moveAccuracy;
      this.movePower = attack.movePower;
      this.moveResult = attack.moveResult;
      this.originalMove = attack.originalMove;
      this.overrideAttack = attack.overrideAttack;
      this.overrideAttackCategory = attack.overrideAttackCategory;
      this.overridePower = attack.overridePower;
      this.overridePPMax = attack.overridePPMax;
      this.overrideType = attack.overrideType;
      this.pp = attack.pp;
      this.ppLevel = attack.ppLevel;
      this.savedAccuracy = attack.savedAccuracy;
      this.savedAttack = attack.savedAttack;
      this.savedPower = attack.savedPower;
   }

   public boolean checkSkyBattle(BattleControllerBase bc) {
      return !bc.rules.hasClause("sky") || SkyBattle.isMoveAllowed(this);
   }

   public boolean canHitNoTarget() {
      return this.isAttack("Doom Desire", "Future Sight", "Imprison", "Spikes", "Stealth Rock", "Sticky Web", "Toxic Spikes");
   }

   public boolean isSoundBased() {
      return this.getMove().getFlags().sound;
   }

   public static boolean hasAttack(int attackIndex) {
      return attackIndex != -1 && AttackBase.getAttackBase(attackIndex).isPresent();
   }

   public static boolean hasAttack(String moveName) {
      return AttackBase.getAttackBase(moveName).isPresent() && ((AttackBase)AttackBase.getAttackBase(moveName).get()).getAttackId() != -1;
   }

   public static AttackBase[] getAttacks(String[] nameList) {
      AttackBase[] attacks = new AttackBase[nameList.length];

      for(int i = 0; i < nameList.length; ++i) {
         attacks[i] = (AttackBase)AttackBase.getAttackBase(nameList[i].toLowerCase()).orElse((Object)null);
         if (attacks[i] == null) {
            Pixelmon.LOGGER.error("No attack found with name: " + nameList[i]);
         }
      }

      return attacks;
   }

   public int getOverridePower() {
      return this.overridePower;
   }
}
