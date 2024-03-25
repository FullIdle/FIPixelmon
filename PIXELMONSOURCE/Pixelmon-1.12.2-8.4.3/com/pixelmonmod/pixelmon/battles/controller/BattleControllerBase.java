package com.pixelmonmod.pixelmon.battles.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.LostToWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedAbnormalEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.api.events.battles.ApplyBonusStatsEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.log.FleeAction;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.tasks.FailedSwitchFleeTask;
import com.pixelmonmod.pixelmon.battles.tasks.SwitchOutTask;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SwitchCamera;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.DynamaxMegaRule;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EndSpectate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.FormBattleUpdate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UpdateTurn;
import com.pixelmonmod.pixelmon.config.EnumForceBattleResult;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Pickup;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RunAway;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BonusStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.EnumXerneas;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BattleControllerBase {
   public ArrayList participants = new ArrayList();
   public GlobalStatusController globalStatusController = new GlobalStatusController(this);
   public ArrayList spectators = new ArrayList();
   protected int battleTicks = 0;
   public int battleTurn = -1;
   public int playerNumber = 0;
   public boolean battleEnded = false;
   public int battleIndex;
   public BattleLog battleLog;
   public Attack lastAttack;
   public Attack lastSimulatedAttack;
   public Attack lastTempAttack;
   public Attack lastSimulatedTempAttack;
   public int numFainted;
   public BattleRules rules;
   public boolean simulateMode = false;
   public boolean sendMessages = true;
   public boolean wasFishing = false;
   public Quadstate oldGen;
   private boolean calculatedTurnOrder;
   private List switchingOut;
   private static final int TICK_TOP = 20;
   public static ArrayList currentAnimations = new ArrayList();
   public Set checkedPokemon;
   private boolean init;
   private BattleStage stage;
   public int turn;
   public ArrayList turnList;
   private int doLaterTicks;
   private Runnable doLaterAction;
   boolean paused;

   public BattleControllerBase(BattleParticipant[] team1, BattleParticipant[] team2, BattleRules rules) {
      this.oldGen = Quadstate.BOTH;
      this.calculatedTurnOrder = false;
      this.switchingOut = new ArrayList();
      this.checkedPokemon = Sets.newHashSet();
      this.init = false;
      this.stage = BattleStage.PICKACTION;
      this.turn = 0;
      this.turnList = new ArrayList();
      this.doLaterTicks = 0;
      this.doLaterAction = null;
      this.paused = false;
      int p1 = 0;
      BattleParticipant[] var5 = team1;
      int var6 = team1.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         BattleParticipant p = var5[var7];
         if (p.allPokemon.length > 0) {
            p.allPokemon[0].battlePosition = p1;
         }

         p.team = 0;
         this.participants.add(p);
         ++p1;
      }

      int p2 = 0;
      BattleParticipant[] var11 = team2;
      var7 = team2.length;

      for(int var12 = 0; var12 < var7; ++var12) {
         BattleParticipant p = var11[var12];
         if (p.allPokemon.length > 0) {
            p.allPokemon[0].battlePosition = p2;
         }

         p.team = 1;
         this.participants.add(p);
         ++p2;
      }

      rules.validateRules();
      this.rules = rules;
      this.battleLog = new BattleLog(this);
   }

   protected void initBattle() throws Exception {
      Iterator var1 = this.participants.iterator();

      BattleParticipant p;
      while(var1.hasNext()) {
         p = (BattleParticipant)var1.next();
         if (!p.checkPokemon()) {
            throw new Exception("Battle could not start!");
         }
      }

      var1 = this.participants.iterator();

      while(true) {
         TrainerParticipant tp;
         do {
            do {
               if (!var1.hasNext()) {
                  int megaRings;
                  int dynaBands;
                  switch (this.rules.oldgen) {
                     case None:
                        this.oldGen = Quadstate.NONE;
                        break;
                     case Both:
                        this.oldGen = Quadstate.BOTH;
                        break;
                     case Mega:
                        this.oldGen = Quadstate.YES;
                        break;
                     case Dynamax:
                        this.oldGen = Quadstate.NO;
                        break;
                     case World:
                        int dim = ((BattleParticipant)this.participants.get(0)).getEntity().field_71093_bK;
                        boolean oldGenDimension = PixelmonConfig.oldGenDimensions.contains(dim);
                        boolean bothGenDimension = PixelmonConfig.bothGenDimensions.contains(dim);
                        boolean relaxedRules = PixelmonConfig.relaxedBattleGimmickRules;
                        if (this.isPvP()) {
                           megaRings = 0;
                           dynaBands = 0;
                           Iterator var7 = this.participants.iterator();

                           while(var7.hasNext()) {
                              BattleParticipant p = (BattleParticipant)var7.next();
                              if (p instanceof PlayerParticipant) {
                                 PlayerParticipant pp = (PlayerParticipant)p;
                                 if (pp.party.getMegaItem().canMega()) {
                                    ++megaRings;
                                 }

                                 if (pp.party.getMegaItem().canDynamax()) {
                                    ++dynaBands;
                                 }
                              }
                           }

                           if (!bothGenDimension && !relaxedRules) {
                              if (oldGenDimension && megaRings == 0 && dynaBands > 0) {
                                 this.oldGen = Quadstate.NO;
                              } else if (!oldGenDimension && dynaBands == 0 && megaRings > 0) {
                                 this.oldGen = Quadstate.YES;
                              } else {
                                 this.oldGen = Quadstate.valueOf(oldGenDimension);
                              }
                           } else {
                              this.oldGen = Quadstate.BOTH;
                           }
                        } else {
                           boolean npcOverride = false;
                           Iterator var24 = this.participants.iterator();

                           while(var24.hasNext()) {
                              BattleParticipant p = (BattleParticipant)var24.next();
                              if (p instanceof TrainerParticipant && p.getEntity() instanceof NPCTrainer) {
                                 TrainerParticipant t = (TrainerParticipant)p;
                                 NPCTrainer npc = (NPCTrainer)t.getEntity();
                                 npcOverride = true;
                                 switch (npc.getOldGen()) {
                                    case None:
                                       this.oldGen = Quadstate.NONE;
                                       break;
                                    case Both:
                                       this.oldGen = Quadstate.BOTH;
                                       break;
                                    case Mega:
                                       this.oldGen = Quadstate.YES;
                                       break;
                                    case Dynamax:
                                       this.oldGen = Quadstate.NO;
                                       break;
                                    default:
                                       npcOverride = false;
                                 }
                              }
                           }

                           if (!npcOverride) {
                              if (!bothGenDimension && !relaxedRules) {
                                 this.oldGen = Quadstate.valueOf(oldGenDimension);
                              } else {
                                 this.oldGen = Quadstate.BOTH;
                              }
                           }
                        }
                  }

                  var1 = this.participants.iterator();

                  while(var1.hasNext()) {
                     p = (BattleParticipant)var1.next();
                     p.startBattle(this);
                  }

                  this.modifyStats();
                  List turnOrder = this.getDefaultTurnOrder();
                  Iterator var16 = turnOrder.iterator();

                  while(var16.hasNext()) {
                     PixelmonWrapper pw = (PixelmonWrapper)var16.next();
                     pw.getBattleAbility().beforeSwitch(pw);
                  }

                  var16 = this.participants.iterator();

                  while(true) {
                     BattleParticipant p;
                     while(var16.hasNext()) {
                        p = (BattleParticipant)var16.next();
                        p.updateOtherPokemon();
                        PixelmonWrapper[] var20 = p.allPokemon;
                        megaRings = var20.length;

                        PixelmonWrapper apw;
                        for(dynaBands = 0; dynaBands < megaRings; ++dynaBands) {
                           apw = var20[dynaBands];
                           if (apw != null) {
                              if (apw.getSpecies() == EnumSpecies.Xerneas) {
                                 if (apw.getFormEnum() == EnumXerneas.NEUTRAL) {
                                    apw.setForm(EnumXerneas.ACTIVE);
                                 } else if (apw.getFormEnum() == EnumXerneas.NEUTRAL_CREATOR) {
                                    apw.setForm(EnumXerneas.ACTIVE_CREATOR);
                                 }
                              }

                              apw.getHeldItem().onStartOfBattle(apw);
                              apw.pokemon.lastBattleCrits = 0;
                           }
                        }

                        Iterator var21 = p.controlledPokemon.iterator();

                        while(var21.hasNext()) {
                           PixelmonWrapper pw = (PixelmonWrapper)var21.next();
                           pw.addAttackers();
                           ApplyBonusStatsEvent.Pre applyBonusStatsEventPre = new ApplyBonusStatsEvent.Pre(this, pw.pokemon, pw.pokemon.getBonusStats());
                           Pixelmon.EVENT_BUS.post(applyBonusStatsEventPre);
                           if (!applyBonusStatsEventPre.isCanceled()) {
                              BonusStats bonusStats = applyBonusStatsEventPre.getBonusStats();
                              if (bonusStats.modifyPokemon(pw)) {
                                 StatsType stat = bonusStats.getStatModified();
                                 this.sendToAll("pixelmon.battletext.aura", pw.getNickname());
                                 int rank = Math.min(3, bonusStats.getStatModificationRank());
                                 this.sendToAll((stat != null ? "pixelmon.battletext.statsrosespecific" : "pixelmon.battletext.statsrose") + rank, stat != null ? stat.getTranslatedName() : "");
                                 ApplyBonusStatsEvent.Post applyBonusStatsEventPost = new ApplyBonusStatsEvent.Post(this, pw.pokemon, bonusStats);
                                 Pixelmon.EVENT_BUS.post(applyBonusStatsEventPost);
                              }
                           }

                           pw.getBattleStats().setLoweredThisTurn(false);
                           pw.getBattleStats().setRaisedThisTurn(false);
                        }

                        var20 = p.allPokemon;
                        megaRings = var20.length;

                        for(dynaBands = 0; dynaBands < megaRings; ++dynaBands) {
                           apw = var20[dynaBands];
                           if (apw != null) {
                              Iterator var32 = p.getOpponents().iterator();

                              while(var32.hasNext()) {
                                 BattleParticipant o = (BattleParticipant)var32.next();
                                 Iterator var36 = o.controlledPokemon.iterator();

                                 while(var36.hasNext()) {
                                    PixelmonWrapper opw = (PixelmonWrapper)var36.next();
                                    apw.getBattleAbility().applyStartOfBattleHeadOfPartyEffect(apw, opw);
                                 }
                              }
                              break;
                           }
                        }
                     }

                     var16 = this.participants.iterator();

                     while(var16.hasNext()) {
                        p = (BattleParticipant)var16.next();
                        if (p instanceof PlayerParticipant) {
                           PlayerParticipant player = (PlayerParticipant)p;
                           player.openGui();
                           if (player.getEntity() instanceof EntityPlayerMP) {
                              Pixelmon.network.sendTo(new DynamaxMegaRule(this.oldGen), (EntityPlayerMP)player.getEntity());
                           }

                           ++this.playerNumber;
                        }
                     }

                     this.battleTurn = 0;
                     this.doLater(() -> {
                        Iterator var2 = turnOrder.iterator();

                        while(var2.hasNext()) {
                           PixelmonWrapper pw = (PixelmonWrapper)var2.next();
                           pw.afterSwitch();
                        }

                        PlayerParticipant playerParticipant = null;
                        TrainerParticipant trainerParticipant = null;
                        Iterator var4 = this.participants.iterator();

                        while(var4.hasNext()) {
                           BattleParticipant bp = (BattleParticipant)var4.next();
                           if (bp instanceof PlayerParticipant) {
                              playerParticipant = (PlayerParticipant)bp;
                           } else if (bp instanceof TrainerParticipant) {
                              trainerParticipant = (TrainerParticipant)bp;
                           }

                           PixelmonWrapper[] var6 = bp.allPokemon;
                           int var7 = var6.length;

                           for(int var8 = 0; var8 < var7; ++var8) {
                              PixelmonWrapper pwx = var6[var8];
                              this.updateFormChange(pwx);
                           }
                        }

                        if (playerParticipant != null && trainerParticipant != null) {
                           String loc = ((EntityPlayerMP)playerParticipant.getEntity()).field_71148_cg;
                           if (trainerParticipant.trainer.getGreeting(loc) != null) {
                              ChatHandler.sendBattleMessage((Entity)playerParticipant.player, trainerParticipant.trainer.getName(loc) + ": " + trainerParticipant.trainer.getGreeting(loc));
                           }
                        }

                        this.sendToPlayers(new UpdateTurn(this.battleTurn));
                     }, 10);
                     this.init = true;
                     return;
                  }
               }

               p = (BattleParticipant)var1.next();
            } while(!(p instanceof TrainerParticipant));

            tp = (TrainerParticipant)p;
         } while(tp.trainer.getAIMode() != EnumTrainerAI.StillAndEngage && tp.trainer.getAIMode() != EnumTrainerAI.WanderAndEngage);

         if (!this.rules.hasClause("forfeit")) {
            ArrayList newClauses = new ArrayList();
            newClauses.addAll(this.rules.getClauseList());
            newClauses.add(BattleClauseRegistry.getClauseRegistry().getClause("forfeit"));
            this.rules.setNewClauses(newClauses);
         }
      }
   }

   public void doLater(Runnable runnable, int ticks) {
      this.doLaterTicks = ticks;
      this.doLaterAction = runnable;
   }

   public void update() {
      if (this.battleEnded) {
         BattleRegistry.deRegisterBattle(this);
      } else {
         try {
            if (!this.init) {
               try {
                  this.initBattle();
               } catch (Exception var9) {
                  BattleRegistry.deRegisterBattle(this);
                  var9.printStackTrace();
                  this.init = false;
                  this.endBattle(EnumBattleEndCause.FORCE);
                  return;
               }
            }

            this.onUpdate();
            boolean hasAnimationsPlaying = CollectionHelper.find(currentAnimations, (animation) -> {
               return animation.bc == this;
            }) != null;
            if (hasAnimationsPlaying || this.isEvolving() || this.isWaiting() || this.paused || this.simulateMode || this.participants.size() < 2) {
               return;
            }

            if (this.battleTicks++ > 20) {
               Iterator var2;
               BattleParticipant p;
               if (this.stage == BattleStage.PICKACTION) {
                  var2 = this.participants.iterator();

                  while(var2.hasNext()) {
                     p = (BattleParticipant)var2.next();
                     p.clearTurnVariables();
                     p.selectAction();
                  }

                  this.stage = BattleStage.DOACTION;
                  this.turn = 0;
               } else if (this.stage == BattleStage.DOACTION) {
                  this.modifyStats();
                  Iterator var5;
                  if (this.turn == 0 && !this.calculatedTurnOrder) {
                     try {
                        if (!this.simulateMode) {
                           var2 = this.getActivePokemon().iterator();

                           while(var2.hasNext()) {
                              PixelmonWrapper currentPokemon = (PixelmonWrapper)var2.next();
                              boolean hasEvolved = false;
                              if (!(currentPokemon.priority < 6.0F)) {
                                 if (currentPokemon.attack != null && currentPokemon.willEvolve) {
                                    hasEvolved = currentPokemon.megaEvolve();
                                 }
                              } else {
                                 var5 = this.getDefaultTurnOrder().iterator();

                                 label196:
                                 while(true) {
                                    PixelmonWrapper pw;
                                    do {
                                       if (!var5.hasNext()) {
                                          break label196;
                                       }

                                       pw = (PixelmonWrapper)var5.next();
                                    } while(!pw.willEvolve);

                                    hasEvolved = pw.megaEvolve() || hasEvolved;
                                 }
                              }

                              if (hasEvolved) {
                                 return;
                              }
                           }
                        }

                        CalcPriority.checkMoveSpeed(this);
                        this.calculatedTurnOrder = true;
                     } catch (Exception var10) {
                        this.battleLog.onCrash(var10, "Problem checking move speed.");
                     }
                  }

                  if (this.turn < this.turnList.size()) {
                     this.modifyStatsCancellable((PixelmonWrapper)this.turnList.get(this.turn));
                  }

                  var2 = this.participants.iterator();

                  while(true) {
                     Iterator var15;
                     if (!var2.hasNext()) {
                        boolean endTurn = false;
                        int numTurns = this.turnList.size();
                        if (this.turn < numTurns) {
                           this.participants.stream().filter((bp) -> {
                              return bp.getType() == ParticipantType.Player;
                           }).forEach((bp) -> {
                              bp.wait = true;
                           });
                           PixelmonWrapper currentPokemon = (PixelmonWrapper)this.turnList.get(this.turn);
                           this.takeTurn(currentPokemon);
                           numTurns = this.turnList.size();
                           ++this.turn;
                           if (this.turn >= numTurns) {
                              endTurn = true;
                              var5 = this.participants.iterator();

                              label163:
                              while(var5.hasNext()) {
                                 BattleParticipant p = (BattleParticipant)var5.next();
                                 Iterator var7 = p.controlledPokemon.iterator();

                                 while(true) {
                                    while(true) {
                                       if (!var7.hasNext()) {
                                          continue label163;
                                       }

                                       PixelmonWrapper pw = (PixelmonWrapper)var7.next();
                                       if (pw.newPokemonUUID != null && pw.isFainted()) {
                                          this.takeTurn(pw);
                                       } else if (pw.nextSwitchIsMove) {
                                          endTurn = false;
                                       }
                                    }
                                 }
                              }
                           }
                        } else {
                           endTurn = true;
                        }

                        this.calculatedTurnOrder = false;
                        if (endTurn) {
                           var15 = this.participants.iterator();

                           while(var15.hasNext()) {
                              BattleParticipant participant = (BattleParticipant)var15.next();
                              Iterator var20 = participant.controlledPokemon.iterator();

                              while(var20.hasNext()) {
                                 PixelmonWrapper pixelmonWrapper = (PixelmonWrapper)var20.next();
                                 this.tryFlee(pixelmonWrapper, true);
                              }
                           }
                        }

                        if (endTurn) {
                           this.applyRepeatedEffects();
                           this.endTurn();
                        }

                        this.checkPokemon();
                        if (endTurn) {
                           this.checkFaint();
                        }
                        break;
                     }

                     p = (BattleParticipant)var2.next();
                     var15 = p.controlledPokemon.iterator();

                     while(var15.hasNext()) {
                        PixelmonWrapper poke = (PixelmonWrapper)var15.next();
                        if (poke.entity != null && !poke.entity.isLoaded(true)) {
                           poke.entity.retrieve();
                           if (this.getPlayers().isEmpty()) {
                              this.endBattle(EnumBattleEndCause.FORCE);
                              return;
                           }

                           poke.entity.field_70170_p.func_175681_c(Lists.newArrayList(new Entity[]{poke.entity}));
                           poke.entity.releaseFromPokeball();
                           poke.entity.field_70172_ad = 0;
                        }
                     }
                  }
               }

               this.battleTicks = 0;
            }
         } catch (Exception var11) {
            if (this.battleLog != null) {
               this.battleLog.onCrash(var11, "Caught error in battle. Continuing...");
            } else if (PixelmonConfig.printErrors) {
               Pixelmon.LOGGER.info("Caught error in battle. Continuing...");
               var11.printStackTrace();
            }
         }

      }
   }

   private boolean isEvolving() {
      Iterator var1 = this.participants.iterator();

      while(var1.hasNext()) {
         BattleParticipant p = (BattleParticipant)var1.next();
         Iterator var3 = p.controlledPokemon.iterator();

         while(var3.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var3.next();
            if (pw.evolution != null) {
               if (!pw.evolution.isEnded()) {
                  return true;
               }

               pw.evolution = null;
               p.wait = false;
               pw.wait = false;
               String path = "";
               if (pw.entity.getSpecies().equals(EnumSpecies.Greninja)) {
                  path = "ashgreninja.evolve";
               } else if (pw.entity.getSpecies().equals(EnumSpecies.Necrozma)) {
                  path = "ultraburst";
               } else if (pw.entity.func_110124_au().equals(p.evolution)) {
                  path = "megaevolve";
               } else if (pw.entity.func_110124_au().equals(p.dynamax)) {
                  if (pw.canGigantamax()) {
                     path = "gigantamax";
                  } else {
                     path = "dynamax";
                  }
               }

               if (!path.isEmpty()) {
                  this.sendToAll("pixelmon.battletext." + path, pw.getNickname(), pw.pokemon.getLocalizedName());
               }

               pw.getBattleAbility().applySwitchInEffect(pw);
            }
         }
      }

      return false;
   }

   public void modifyStats() {
      Iterator var1 = this.participants.iterator();

      while(var1.hasNext()) {
         BattleParticipant p = (BattleParticipant)var1.next();
         p.controlledPokemon.forEach(this::modifyStats);
      }

   }

   public void modifyStats(PixelmonWrapper pw) {
      int[] stats = pw.getBattleStats().getBaseBattleStats();

      for(int i = 0; i < pw.getStatusSize(); ++i) {
         stats = pw.getStatus(i).modifyStats(pw, stats);
      }

      stats = pw.getBattleAbility().modifyStats(pw, stats);

      PixelmonWrapper teammate;
      Iterator var5;
      for(var5 = this.getTeamPokemon(pw).iterator(); var5.hasNext(); stats = teammate.getBattleAbility().modifyStatsTeammate(pw, stats)) {
         teammate = (PixelmonWrapper)var5.next();
      }

      stats = pw.getUsableHeldItem().modifyStats(pw, stats);

      GlobalStatusBase gsb;
      for(var5 = this.globalStatusController.getGlobalStatuses().iterator(); var5.hasNext(); stats = gsb.modifyStats(pw, stats)) {
         gsb = (GlobalStatusBase)var5.next();
      }

      pw.getBattleStats().setStatsForTurn(stats);
   }

   public void modifyStats(PixelmonWrapper pw, int[] stats) {
      for(int i = 0; i < pw.getStatusSize(); ++i) {
         stats = pw.getStatus(i).modifyStats(pw, stats);
      }

      stats = pw.getBattleAbility().modifyStats(pw, stats);

      PixelmonWrapper teammate;
      Iterator var5;
      for(var5 = this.getTeamPokemon(pw).iterator(); var5.hasNext(); stats = teammate.getBattleAbility().modifyStatsTeammate(pw, stats)) {
         teammate = (PixelmonWrapper)var5.next();
      }

      stats = pw.getUsableHeldItem().modifyStats(pw, stats);

      GlobalStatusBase gsb;
      for(var5 = this.globalStatusController.getGlobalStatuses().iterator(); var5.hasNext(); stats = gsb.modifyStats(pw, stats)) {
         gsb = (GlobalStatusBase)var5.next();
      }

      pw.getBattleStats().setStatsForTurn(stats);
   }

   public void modifyStatsCancellable(PixelmonWrapper attacker) {
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         Iterator var4 = p.controlledPokemon.iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var4.next();
            int[] stats = pw.getBattleStats().getBattleStats();
            if (!AbilityBase.ignoreAbility(attacker, pw) || !attacker.targets.contains(pw)) {
               stats = pw.getBattleAbility().modifyStatsCancellable(pw, stats);

               PixelmonWrapper teammate;
               for(Iterator var7 = this.getTeamPokemon(pw).iterator(); var7.hasNext(); stats = teammate.getBattleAbility().modifyStatsCancellableTeammate(pw, stats)) {
                  teammate = (PixelmonWrapper)var7.next();
               }
            }

            for(int i = 0; i < pw.getStatusSize(); ++i) {
               StatusBase status = pw.getStatus(i);
               if (!status.ignoreStatus(attacker, pw)) {
                  stats = status.modifyStatsCancellable(pw, stats);
               }
            }

            pw.getBattleStats().setStatsForTurn(stats);
         }
      }

   }

   public void participantReady(PlayerParticipant p) {
      p.wait = false;
   }

   public void setAllReady() {
      Iterator var1 = this.getPlayers().iterator();

      while(var1.hasNext()) {
         PlayerParticipant player = (PlayerParticipant)var1.next();
         this.participantReady(player);
      }

   }

   private void applyRepeatedEffects() {
      boolean teamAble = false;
      boolean opponentAble = false;
      BattleParticipant participant = (BattleParticipant)this.participants.get(0);
      Iterator var4 = this.getTeamPokemon(participant).iterator();

      PixelmonWrapper pw;
      while(var4.hasNext()) {
         pw = (PixelmonWrapper)var4.next();
         if (pw.isAlive()) {
            teamAble = true;
            break;
         }
      }

      BattleParticipant p;
      if (!teamAble) {
         var4 = this.getTeam((BattleParticipant)this.participants.get(0)).iterator();

         while(var4.hasNext()) {
            p = (BattleParticipant)var4.next();
            if (p.countAblePokemon() > 0) {
               teamAble = true;
               break;
            }
         }
      }

      var4 = this.getOpponentPokemon(participant).iterator();

      while(var4.hasNext()) {
         pw = (PixelmonWrapper)var4.next();
         if (pw.isAlive()) {
            opponentAble = true;
            break;
         }
      }

      if (!opponentAble) {
         var4 = this.getOpponents((BattleParticipant)this.participants.get(0)).iterator();

         while(var4.hasNext()) {
            p = (BattleParticipant)var4.next();
            if (p.countAblePokemon() > 0) {
               opponentAble = true;
               break;
            }
         }
      }

      if (teamAble && opponentAble) {
         var4 = this.globalStatusController.getGlobalStatuses().iterator();

         while(var4.hasNext()) {
            GlobalStatusBase gsb = (GlobalStatusBase)var4.next();
            gsb.applyRepeatedEffect(this.globalStatusController);
         }

         var4 = this.getDefaultTurnOrder().iterator();

         while(var4.hasNext()) {
            pw = (PixelmonWrapper)var4.next();
            pw.turnTick();
         }
      }

   }

   private void endTurn() {
      this.stage = BattleStage.PICKACTION;
      Iterator var1 = this.participants.iterator();

      while(var1.hasNext()) {
         BattleParticipant p = (BattleParticipant)var1.next();
         p.onEndTurn(this);
         p.faintedLastTurn = false;
         Iterator var3 = p.controlledPokemon.iterator();

         while(var3.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var3.next();
            if (pw.isDynamax > 0) {
               --pw.dynamaxTurns;
               if (pw.dynamaxTurns <= 0) {
                  pw.dynamax(true, pw.getHealthPercent());
               }
            }
         }
      }

      this.getActivePokemon().forEach((pwx) -> {
         pwx.getBattleStats().setLoweredThisTurn(false);
         pwx.getBattleStats().setRaisedThisTurn(false);
      });
      ++this.battleTurn;
      this.battleLog.turnTick(this.battleTurn);
      this.sendToPlayers(new UpdateTurn(this.battleTurn));
      Pixelmon.EVENT_BUS.post(new TurnEndEvent(this));
   }

   public void checkReviveSendOut(BattleParticipant p) {
      if (!this.rules.battleType.areSlotsLocked() && this.rules.battleType.numPokemon > 1 && p != null && p.getType() == ParticipantType.Player && this.getTeam(p).size() == 1 && p.controlledPokemon.size() < this.rules.battleType.numPokemon && p.countAblePokemon() > p.controlledPokemon.size()) {
         PlayerParticipant player = (PlayerParticipant)p;
         List wholeTeamStatuses = new ArrayList();
         Iterator var4 = p.controlledPokemon.iterator();
         Iterator var6;
         if (var4.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var4.next();
            wholeTeamStatuses = new ArrayList();
            var6 = pw.getStatuses().iterator();

            while(var6.hasNext()) {
               StatusBase status = (StatusBase)var6.next();
               if (status.isWholeTeamStatus()) {
                  wholeTeamStatuses.add(status.copy());
               }
            }
         }

         int oldPosition = false;
         int newPosition = 0;

         do {
            var6 = p.controlledPokemon.iterator();

            while(var6.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var6.next();
               if (newPosition == pw.battlePosition) {
                  ++newPosition;
               }
            }
         } while(newPosition != newPosition);

         PixelmonWrapper revived = null;
         PixelmonWrapper[] var16 = player.allPokemon;
         int var8 = var16.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            PixelmonWrapper pw = var16[var9];
            if (pw.isAlive() && pw.entity == null) {
               EntityPixelmon revivedPokemon = p.getStorage().find(pw.getPokemonUUID()).getOrSpawnPixelmon(player.player);
               revivedPokemon.func_70606_j((float)pw.getHealth());
               pw.update(EnumUpdateType.HP);
               revivedPokemon.battleController = this;
               revived = pw;
               pw.entity = revivedPokemon;
               break;
            }
         }

         if (revived == null) {
            return;
         }

         revived.battlePosition = newPosition;
         Iterator var17 = wholeTeamStatuses.iterator();

         while(var17.hasNext()) {
            StatusBase status = (StatusBase)var17.next();
            revived.addStatus(status, revived);
         }

         player.controlledPokemon.add(newPosition, revived);
         revived.bc = player.bc;
         PartyStorage storage = player.getStorage();
         if (storage == null) {
            this.endBattle(EnumBattleEndCause.FORCE);
            return;
         }

         if (storage.find(revived.getPokemonUUID()).getPixelmonIfExists() == null) {
            revived.entity.func_70012_b(player.player.field_70165_t, player.player.field_70163_u, player.player.field_70161_v, player.player.field_70177_z, 0.0F);
            revived.entity.releaseFromPokeball();
         }

         if (!AbilityBase.ignoreAbility(revived, this)) {
            revived.getBattleAbility().beforeSwitch(revived);
         }

         ChatHandler.sendBattleMessage((Entity)player.player, "playerparticipant.go", new TextComponentTranslation("ribbon." + revived.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{revived.getNickname()}));
         this.sendToOthers("battlecontroller.sendout", player, player.player.func_145748_c_().func_150260_c(), new TextComponentTranslation("ribbon." + revived.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{revived.getNickname()}));
         Iterator var20 = this.participants.iterator();

         while(var20.hasNext()) {
            BattleParticipant p2 = (BattleParticipant)var20.next();
            p2.updateOtherPokemon();
         }

         revived.afterSwitch();
         revived.addAttackers();
         this.sendSwitchPacket((UUID)null, revived);
      }

   }

   public void reviveAfterDefeat(BattleParticipant p) {
      if (p != null) {
         List wholeTeamStatuses = new ArrayList();
         Iterator var3 = p.controlledPokemon.iterator();
         Iterator var5;
         if (var3.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var3.next();
            wholeTeamStatuses = new ArrayList();
            var5 = pw.getStatuses().iterator();

            while(var5.hasNext()) {
               StatusBase status = (StatusBase)var5.next();
               if (status.isWholeTeamStatus()) {
                  wholeTeamStatuses.add(status.copy());
               }
            }
         }

         int oldPosition = false;
         int newPosition = 0;

         do {
            var5 = p.controlledPokemon.iterator();

            while(var5.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var5.next();
               if (newPosition == pw.battlePosition) {
                  ++newPosition;
               }
            }
         } while(newPosition != newPosition);

         PixelmonWrapper revived = null;
         PixelmonWrapper[] var15 = p.allPokemon;
         int var7 = var15.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            PixelmonWrapper pw = var15[var8];
            if (pw.isAlive() && pw.entity == null) {
               EntityPixelmon revivedPokemon = p.getStorage().find(pw.getPokemonUUID()).getOrSpawnPixelmon(p.getEntity());
               revivedPokemon.func_70606_j((float)pw.getHealth());
               pw.update(EnumUpdateType.HP);
               revivedPokemon.battleController = this;
               revived = pw;
               pw.entity = revivedPokemon;
               break;
            }
         }

         if (revived == null) {
            return;
         }

         revived.battlePosition = newPosition;
         Iterator var16 = wholeTeamStatuses.iterator();

         while(var16.hasNext()) {
            StatusBase status = (StatusBase)var16.next();
            revived.addStatus(status, revived);
         }

         p.controlledPokemon.add(newPosition, revived);
         revived.bc = p.bc;
         PartyStorage storage = p.getStorage();
         if (storage == null) {
            this.endBattle(EnumBattleEndCause.FORCE);
            return;
         }

         if (!AbilityBase.ignoreAbility(revived, this)) {
            revived.getBattleAbility().beforeSwitch(revived);
         }

         ChatHandler.sendBattleMessage((Entity)p.getEntity(), "playerparticipant.go", new TextComponentTranslation("ribbon." + revived.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{revived.getNickname()}));
         this.sendToOthers("battlecontroller.sendout", p, p.getDisplayName(), new TextComponentTranslation("ribbon." + revived.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{revived.getNickname()}));
         Iterator var19 = this.participants.iterator();

         while(var19.hasNext()) {
            BattleParticipant p2 = (BattleParticipant)var19.next();
            p2.updateOtherPokemon();
         }

         revived.afterSwitch();
         revived.addAttackers();
         this.sendSwitchPacket((UUID)null, revived);
      }

   }

   void checkFaint() {
      List fainted = new ArrayList();
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         List toRemove = new ArrayList();
         Iterator var5 = p.controlledPokemon.iterator();

         PixelmonWrapper pw;
         while(var5.hasNext()) {
            pw = (PixelmonWrapper)var5.next();
            if (pw.isFainted() && !pw.isSwitching) {
               if (pw.removePrimaryStatus() != null) {
                  pw.sendStatusPacket(-1);
               }

               pw.update(EnumUpdateType.Status);
               p.updatePokemon(pw);
               if (p.addSwitchingOut(pw)) {
                  fainted.add(pw);
               } else {
                  toRemove.add(pw);
               }
            }
         }

         var5 = toRemove.iterator();

         while(var5.hasNext()) {
            pw = (PixelmonWrapper)var5.next();
            pw.isSwitching = false;
            pw.wait = false;
            p.controlledPokemon.remove(pw);
            this.updateRemovedPokemon(pw);
         }
      }

      this.switchingOut.addAll(fainted);
      var2 = fainted.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         BattleParticipant p = pw.getParticipant();
         p.faintedLastTurn = true;
         pw.willTryFlee = false;
         p.wait = true;
         p.getNextPokemon(pw.battlePosition);
      }

   }

   private void onUpdate() {
      if (this.doLaterTicks > 0) {
         --this.doLaterTicks;
         if (this.doLaterTicks == 0) {
            this.doLaterAction.run();
         }
      }

      this.participants.forEach(BattleParticipant::tick);
      if (this.isPvP()) {
         this.participants.stream().filter((p) -> {
            return ((PlayerParticipant)p).player == null || !((PlayerParticipant)p).player.func_70089_S();
         }).forEach((p) -> {
            this.endBattle(EnumBattleEndCause.FORCE);
         });
      } else {
         this.participants.stream().filter((p) -> {
            return p.getType() == ParticipantType.Player;
         }).filter((p) -> {
            return ((PlayerParticipant)p).player == null || ((PlayerParticipant)p).player.field_70128_L;
         }).forEach((p) -> {
            this.endBattle(EnumBattleEndCause.FORCE);
         });
      }

   }

   public HashMap endBattle(EnumBattleEndCause cause) {
      return this.endBattle(cause, new HashMap());
   }

   public HashMap endBattle(EnumBattleEndCause cause, HashMap results) {
      if (results == null) {
         results = new HashMap();
      }

      this.spectators.forEach((spectator) -> {
         spectator.sendMessage(new EndSpectate());
      });
      boolean abnormal = false;
      this.battleEnded = true;
      this.globalStatusController.endBattle();
      Iterator var4 = this.participants.iterator();

      BattleParticipant p;
      while(var4.hasNext()) {
         p = (BattleParticipant)var4.next();
         Iterator var6 = p.controlledPokemon.iterator();

         while(var6.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var6.next();
            pw.dynamax(true, pw.getHealthPercent());
            pw.getBattleAbility().applyEndOfBattleEffect(pw);
            pw.resetOnSwitch();
            Iterator var8 = pw.getStatuses().iterator();

            while(var8.hasNext()) {
               StatusBase status = (StatusBase)var8.next();
               status.applyEndOfBattleEffect(pw);
            }

            if (cause == EnumBattleEndCause.FORCE) {
               pw.setHeldItem(pw.pokemon.getHeldItem());
            }
         }

         if (cause == EnumBattleEndCause.FLEE) {
            results.put(p, BattleResults.FLEE);
            if (p instanceof PlayerParticipant) {
               Pixelmon.EVENT_BUS.post(new PlayerBattleEndedEvent(((PlayerParticipant)p).player, this, BattleResults.FLEE));
            }
         } else if ((cause != EnumBattleEndCause.FORCE || PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.WINNER) && this.init && cause != EnumBattleEndCause.FORFEIT) {
            BattleResults result = BattleResults.DRAW;
            int allyCount = 0;
            int opponentCount = 0;

            BattleParticipant enemyp;
            Iterator var23;
            for(var23 = this.getTeam(p).iterator(); var23.hasNext(); allyCount += enemyp.countAblePokemon()) {
               enemyp = (BattleParticipant)var23.next();
            }

            for(var23 = this.getOpponents(p).iterator(); var23.hasNext(); opponentCount += enemyp.countAblePokemon()) {
               enemyp = (BattleParticipant)var23.next();
            }

            if (allyCount > opponentCount) {
               result = BattleResults.VICTORY;
            } else if (opponentCount > allyCount) {
               result = BattleResults.DEFEAT;
            } else {
               float allyPercent = 0.0F;
               float opponentPercent = 0.0F;
               int allyParticipants = 0;
               int opponentParticipants = 0;

               Iterator var13;
               BattleParticipant enemyp;
               for(var13 = this.getTeam(p).iterator(); var13.hasNext(); ++allyParticipants) {
                  enemyp = (BattleParticipant)var13.next();
                  allyPercent += enemyp.countHealthPercent();
               }

               for(var13 = this.getOpponents(p).iterator(); var13.hasNext(); ++opponentParticipants) {
                  enemyp = (BattleParticipant)var13.next();
                  opponentPercent += enemyp.countHealthPercent();
               }

               if (allyParticipants == 0) {
                  allyParticipants = 1;
               }

               if (opponentParticipants == 0) {
                  opponentParticipants = 1;
               }

               allyPercent /= (float)allyParticipants;
               opponentPercent /= (float)opponentParticipants;
               if (allyPercent > opponentPercent) {
                  result = BattleResults.VICTORY;
               } else if (opponentPercent > allyPercent) {
                  result = BattleResults.DEFEAT;
               } else {
                  result = BattleResults.DRAW;
               }
            }

            results.put(p, result);
         } else if (cause == EnumBattleEndCause.FORCE) {
            if (PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.DRAW) {
               results.put(p, BattleResults.DRAW);
            } else {
               if (PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.ABNORMAL) {
                  if (p instanceof PlayerParticipant) {
                     Pixelmon.EVENT_BUS.post(new PlayerBattleEndedAbnormalEvent(((PlayerParticipant)p).player, this));
                  }

                  abnormal = true;
               }

               results.put(p, BattleResults.DRAW);
            }
         }

         if (p instanceof PlayerParticipant) {
            Pixelmon.EVENT_BUS.post(new PlayerBattleEndedEvent(((PlayerParticipant)p).player, this, (BattleResults)results.get(p)));
            if (results.get(p) == BattleResults.VICTORY && cause == EnumBattleEndCause.NORMAL) {
               Pickup.pickupItem((PlayerParticipant)p);
            }
         } else if (p instanceof WildPixelmonParticipant) {
            this.resetAggression(p);
         }
      }

      BattleRegistry.deRegisterBattle(this);
      if (this.participants.size() == 2) {
         BattleParticipant bp1 = (BattleParticipant)this.participants.get(0);
         p = (BattleParticipant)this.participants.get(1);
         if (bp1 instanceof PlayerParticipant && p instanceof TrainerParticipant || p instanceof PlayerParticipant && bp1 instanceof TrainerParticipant) {
            NPCEvent.EndBattle npcEndBattleEvent = new NPCEvent.EndBattle(this, cause, abnormal, results);
            Pixelmon.EVENT_BUS.post(npcEndBattleEvent);
         }
      }

      BattleEndEvent event = new BattleEndEvent(this, cause, abnormal, results);
      Pixelmon.EVENT_BUS.post(event);
      Iterator var17 = this.participants.iterator();

      while(var17.hasNext()) {
         BattleParticipant p = (BattleParticipant)var17.next();
         p.endBattle(cause);
      }

      this.checkEvolution();
      return results;
   }

   public void endBattle() {
      this.endBattle(EnumBattleEndCause.NORMAL);
   }

   public void endBattleWithoutXP() {
      this.endBattle(EnumBattleEndCause.FORCE);
   }

   private void checkEvolution() {
      Iterator var1 = this.checkedPokemon.iterator();

      while(var1.hasNext()) {
         Pokemon pokemon = (Pokemon)var1.next();
         pokemon.tryEvolution();
      }

   }

   private void resetAggression(BattleParticipant p) {
      if (p.getType() == ParticipantType.WildPokemon) {
         EntityLivingBase entity = p.getEntity();
         if (entity != null && !entity.field_70128_L) {
            ((EntityPixelmon)entity).aggressionTimer = RandomHelper.getRandomNumberBetween(400, 1000);
         }
      }

   }

   public boolean isPvP() {
      Iterator var1 = this.participants.iterator();

      BattleParticipant p;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         p = (BattleParticipant)var1.next();
      } while(p instanceof PlayerParticipant);

      return false;
   }

   public void pauseBattle() {
      this.paused = true;
   }

   public boolean isWaiting() {
      Iterator var1 = this.participants.iterator();

      BattleParticipant p;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         p = (BattleParticipant)var1.next();
      } while(!p.getWait());

      return true;
   }

   public void endPause() {
      this.paused = false;
   }

   private void takeTurn(PixelmonWrapper p) {
      if (!this.tryFlee(p)) {
         Iterator var2 = this.participants.iterator();

         while(var2.hasNext()) {
            BattleParticipant part = (BattleParticipant)var2.next();
            if (part.bc == null) {
               this.endBattle();
               return;
            }
         }

         if (p.getParticipant() == null || !p.getParticipant().onTakeTurn(this, p)) {
            p.takeTurn();
         }

      }
   }

   public boolean tryFlee(PixelmonWrapper p) {
      return this.tryFlee(p, false);
   }

   public boolean tryFlee(PixelmonWrapper p, boolean turnEnd) {
      if (!turnEnd) {
         for(int i = 0; i < this.turn; ++i) {
            PixelmonWrapper current = (PixelmonWrapper)this.turnList.get(i);
            if (current.willTryFlee && current.getParticipant() == p.getParticipant()) {
               return false;
            }
         }
      }

      if (p.willTryFlee) {
         this.forfeitOrFlee(p);
         p.priority = 6.0F;
         return true;
      } else {
         return false;
      }
   }

   private void forfeitOrFlee(PixelmonWrapper p) {
      boolean isForfeit = false;
      Iterator var3 = this.getOpponents(p.getParticipant()).iterator();

      while(var3.hasNext()) {
         BattleParticipant participant = (BattleParticipant)var3.next();
         if (participant.getType() != ParticipantType.WildPokemon) {
            isForfeit = true;
            break;
         }
      }

      if (isForfeit) {
         this.forfeitBattle(p);
      } else {
         this.calculateEscape(p);
      }

   }

   private void calculateEscape(PixelmonWrapper pw) {
      PixelmonWrapper opponentPixelmon = (PixelmonWrapper)pw.bc.getOpponentPokemon(pw.getParticipant()).get(0);
      double fleeingSpeed = (double)pw.getStats().speed;
      double opponentSpeed = (double)opponentPixelmon.getStats().speed;
      double escapeAttempts = (double)(++pw.escapeAttempts);
      double calculatedFleeValue = fleeingSpeed * 128.0 / opponentSpeed + 30.0 * escapeAttempts;
      int random = RandomHelper.getRandomNumberBetween(1, 255);
      if (!pw.isFainted() && !BattleParticipant.canSwitch(pw)[1]) {
         calculatedFleeValue = 0.0;
         random = 1;
      }

      if (pw.getBattleAbility() instanceof RunAway) {
         this.sendToAll("pixelmon.abilities.runaway", pw.getNickname());
         this.endBattle(EnumBattleEndCause.FLEE);
      } else if (pw.getHeldItem().getHeldItemType() != EnumHeldItems.smokeBall && !pw.hasType(EnumType.Ghost)) {
         BattleParticipant fleeingParticipant;
         Iterator var13;
         PixelmonWrapper pw2;
         if (!(calculatedFleeValue > 255.0) && !((double)random < calculatedFleeValue)) {
            this.sendToAll("battlecontroller.!escaped", pw.isFainted() ? pw.pokemon.getOwnerName() : pw.getNickname());
            fleeingParticipant = pw.getParticipant();
            var13 = fleeingParticipant.controlledPokemon.iterator();

            while(var13.hasNext()) {
               pw2 = (PixelmonWrapper)var13.next();
               pw.bc.battleLog.addEvent(new FleeAction(pw.bc.battleTurn, pw.bc.getPositionOfPokemon(pw2), pw));
            }
         } else {
            this.sendToAll("battlecontroller.escaped", pw.getNickname());
            fleeingParticipant = pw.getParticipant();
            if (fleeingParticipant != null) {
               var13 = fleeingParticipant.controlledPokemon.iterator();

               while(var13.hasNext()) {
                  pw2 = (PixelmonWrapper)var13.next();
                  pw.bc.battleLog.addEvent(new FleeAction(pw.bc.battleTurn, pw.bc.getPositionOfPokemon(pw2), pw));
               }
            }

            this.endBattle(EnumBattleEndCause.FLEE);
         }
      } else {
         this.sendToAll("battlecontroller.escaped", pw.getNickname());
         this.endBattle(EnumBattleEndCause.FLEE);
      }

   }

   private void forfeitBattle(PixelmonWrapper pw) {
      if (!this.rules.hasClause("forfeit")) {
         BattleParticipant forfeitParticipant = pw.getParticipant();
         boolean tie = false;
         ArrayList opponents = this.getOpponents(forfeitParticipant);
         Iterator var5 = opponents.iterator();

         while(true) {
            while(var5.hasNext()) {
               BattleParticipant opponent = (BattleParticipant)var5.next();
               Iterator var7 = opponent.controlledPokemon.iterator();

               while(var7.hasNext()) {
                  PixelmonWrapper opponentPokemon = (PixelmonWrapper)var7.next();
                  if (opponentPokemon.willTryFlee) {
                     tie = true;
                     break;
                  }
               }
            }

            if (tie) {
               this.sendToAll("battlecontroller.draw");
            } else if (forfeitParticipant.getType() == ParticipantType.Player) {
               PlayerParticipant forfeitPlayer = (PlayerParticipant)forfeitParticipant;
               ChatHandler.sendBattleMessage((Entity)forfeitPlayer.player, "battlecontroller.forfeitself");
               this.sendToOthers("battlecontroller.forfeit", forfeitPlayer, forfeitPlayer.getDisplayName());
            }

            HashMap results = new HashMap();

            BattleParticipant participant;
            BattleResults result;
            for(Iterator var11 = this.participants.iterator(); var11.hasNext(); results.put(participant, result)) {
               participant = (BattleParticipant)var11.next();
               if (tie) {
                  result = BattleResults.DRAW;
               } else if (opponents.contains(participant)) {
                  result = BattleResults.VICTORY;
               } else {
                  result = BattleResults.DEFEAT;
               }
            }

            this.endBattle(EnumBattleEndCause.FORFEIT, results);
            return;
         }
      }
   }

   private void checkPokemon() {
      boolean cameraSwitched = false;
      Iterator var2 = this.participants.iterator();

      while(true) {
         BattleParticipant p;
         do {
            do {
               if (!var2.hasNext()) {
                  if (cameraSwitched) {
                     this.spectators.forEach((spectator) -> {
                        spectator.sendMessage(new SwitchCamera());
                     });
                  }

                  return;
               }

               p = (BattleParticipant)var2.next();
               p.resetMoveTimer();
            } while(this.battleEnded);
         } while(p.isDefeated);

         this.checkReviveSendOut(p);
         List faintedPokemon = new ArrayList();
         Iterator var5 = p.controlledPokemon.iterator();

         PixelmonWrapper poke;
         while(var5.hasNext()) {
            poke = (PixelmonWrapper)var5.next();
            if (poke.isFainted()) {
               if (p.getType() == ParticipantType.Player) {
                  Pixelmon.network.sendTo(new SwitchCamera(), (EntityPlayerMP)p.getEntity());
                  cameraSwitched = true;
               }

               if (poke.newPokemonUUID == null && this.turnList.contains(poke) && this.turn <= this.turnList.indexOf(poke)) {
                  this.turnList.remove(poke);
               }

               if (!poke.hasAwardedExp) {
                  Experience.awardExp(this.participants, p, poke);
                  poke.hasAwardedExp = true;
                  if (p instanceof WildPixelmonParticipant && this.participants.size() == 2) {
                     this.participants.stream().filter((part) -> {
                        return part != p && part instanceof PlayerParticipant;
                     }).forEach((part) -> {
                        Pixelmon.storageManager.getParty(((PlayerParticipant)part).player).stats.addKill();
                        Pixelmon.EVENT_BUS.post(new BeatWildPixelmonEvent(((PlayerParticipant)part).player, (WildPixelmonParticipant)p));
                        ((PlayerParticipant)part).checkPlayerItems();
                     });
                  }
               }

               poke.entity.func_70606_j(0.0F);
               poke.entity.func_70106_y();
               poke.entity.retrieve();
               p.updatePokemon(poke);
               if (!p.hasMorePokemonReserve()) {
                  faintedPokemon.add(poke);
                  this.updateRemovedPokemon(poke);
               }
            }
         }

         var5 = faintedPokemon.iterator();

         while(var5.hasNext()) {
            poke = (PixelmonWrapper)var5.next();
            if (!this.battleEnded) {
               this.checkDefeated(p, poke);
            }

            if (!this.battleEnded) {
               p.controlledPokemon.remove(poke);
               poke.entity = null;
            }
         }
      }
   }

   public void updateRemovedPokemon(PixelmonWrapper poke) {
      UUID uuid = poke.getPokemonUUID();
      this.participants.stream().filter((p2) -> {
         return p2 instanceof PlayerParticipant;
      }).forEach((p2) -> {
         Pixelmon.network.sendTo(new SwitchOutTask(uuid), ((PlayerParticipant)p2).player);
      });
      this.spectators.forEach((spectator) -> {
         spectator.sendMessage(new SwitchOutTask(uuid));
      });
   }

   public boolean isOneAlive(List teamPokemon) {
      Iterator var2 = teamPokemon.iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(!pw.isAlive());

      return true;
   }

   public void checkDefeated(BattleParticipant p, PixelmonWrapper poke) {
      if (this.isOneAlive(p.controlledPokemon)) {
         p.isDefeated = false;
      } else if (p.countAblePokemon() == 0 && !p.isDefeated) {
         p.isDefeated = true;
         ChatHandler.sendBattleMessage((Entity)p.getEntity(), "battlecontroller.outofpokemon");
         if (!this.isTeamDefeated(p)) {
            return;
         }

         this.participants.stream().filter((p2) -> {
            return this.getOpponents(p).contains(p2);
         }).forEach((p2) -> {
            ChatHandler.sendBattleMessage((Entity)p2.getEntity(), "battlecontroller.win");
            if (p2 instanceof WildPixelmonParticipant && p.getEntity() instanceof EntityPlayerMP) {
               Pixelmon.EVENT_BUS.post(new LostToWildPixelmonEvent((EntityPlayerMP)p.getEntity(), (WildPixelmonParticipant)p2));
            }

         });
         if (p.getType().equals(ParticipantType.Player)) {
            ((PlayerParticipant)p).resetPayDay();
         }

         this.endBattle();
      }

   }

   public void sendToAll(String string, Object... data) {
      if (this.canSendMessages()) {
         ChatHandler.sendBattleMessage(this.participants, string, data);
      }

   }

   public void sendToAll(TextComponentTranslation message) {
      if (this.canSendMessages()) {
         ChatHandler.sendBattleMessage(this.participants, message);
      }

   }

   public void sendToOthers(String string, BattleParticipant battleParticipant, Object... data) {
      if (this.canSendMessages()) {
         this.participants.stream().filter((p) -> {
            return p != battleParticipant;
         }).forEach((p) -> {
            ChatHandler.sendBattleMessage((Entity)p.getEntity(), string, data);
         });
         this.spectators.forEach((spectator) -> {
            ChatHandler.sendBattleMessage((Entity)spectator.getEntity(), string, data);
         });
      }

   }

   public void sendToPlayer(EntityPlayer player, String string, Object... data) {
      if (player != null && this.canSendMessages()) {
         ChatHandler.sendBattleMessage((Entity)player, string, data);
      }

   }

   public void sendToPlayer(EntityPlayer player, TextComponentTranslation message) {
      if (player != null && this.canSendMessages()) {
         ChatHandler.sendBattleMessage((Entity)player, message);
      }

   }

   private boolean canSendMessages() {
      return !this.simulateMode && this.sendMessages;
   }

   public void sendToPlayers(IMessage message) {
      this.participants.forEach((par) -> {
         par.sendMessage(message);
      });
      this.spectators.forEach((spec) -> {
         spec.sendMessage(message);
      });
   }

   public void clearHurtTimer() {
      Iterator var1 = this.participants.iterator();

      while(var1.hasNext()) {
         BattleParticipant part = (BattleParticipant)var1.next();
         Iterator var3 = part.controlledPokemon.iterator();

         while(var3.hasNext()) {
            PixelmonWrapper pokemon = (PixelmonWrapper)var3.next();
            if (pokemon != null && pokemon.entity != null) {
               pokemon.entity.field_70737_aN = 0;
            }
         }
      }

   }

   public void setUseItem(UUID pokemonUUID, EntityPlayer user, ItemStack usedStack, int additionalInfo) {
      BattleParticipant participant = (BattleParticipant)CollectionHelper.find(this.participants, (bp) -> {
         return bp.getType() == ParticipantType.Player && bp.getEntity() == user;
      });
      PixelmonWrapper userWrapper = participant.getPokemonFromUUID(pokemonUUID);
      userWrapper.willUseItemInStack = usedStack;
      userWrapper.willUseItemInStackInfo = additionalInfo;
      userWrapper.wait = false;
      if (usedStack.func_77973_b() instanceof ItemPokeball) {
         Iterator var7 = participant.controlledPokemon.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var7.next();
            if (pw != userWrapper) {
               pw.wait = false;
               pw.attack = null;
            }
         }
      }

   }

   public void setUseItem(UUID pokemonUUID, EntityPlayer player, ItemStack usedStack, UUID targetPokemonUUID) {
      Iterator var5 = this.participants.iterator();

      while(var5.hasNext()) {
         BattleParticipant p = (BattleParticipant)var5.next();
         if (p.getType() == ParticipantType.Player && p.getEntity() == player) {
            PixelmonWrapper pw = p.getPokemonFromUUID(pokemonUUID);
            if (pw != null) {
               pw.willUseItemInStack = usedStack;
               pw.willUseItemPokemon = targetPokemonUUID;
               pw.willUseItemInStackInfo = -1;
               pw.wait = false;
            }
         }
      }

   }

   public void switchPokemon(UUID switchingPokemonUUID, UUID newPokemonUUID, boolean switchInstantly) {
      PixelmonWrapper pw = this.getPokemonFromUUID(switchingPokemonUUID);
      if (pw != null) {
         BattleParticipant p = pw.getParticipant();
         pw.isSwitching = true;
         pw.newPokemonUUID = newPokemonUUID;
         pw.setTemporaryMoveset((Moveset)null);
         boolean stopWait = true;
         boolean checkFaint = false;
         if (pw.isFainted()) {
            p.switchingIn.add(newPokemonUUID);
            if (!this.switchingOut.isEmpty()) {
               this.switchingOut.remove(pw);
               if (this.switchingOut.isEmpty()) {
                  BattleParticipant participant;
                  for(Iterator var8 = this.participants.iterator(); var8.hasNext(); participant.wait = false) {
                     participant = (BattleParticipant)var8.next();
                     participant.switchAllFainted();
                  }

                  checkFaint = true;
               } else {
                  stopWait = false;
               }
            }
         } else if (switchInstantly) {
            pw.doSwitch();
         }

         if (checkFaint) {
            this.checkPokemon();
            this.checkFaint();
         }

         if (stopWait) {
            pw.wait = false;
            p.wait = false;
         }

      }
   }

   public void setFlee(UUID fleeingUUID) {
      Iterator var2 = this.participants.iterator();

      while(true) {
         while(true) {
            BattleParticipant p;
            PixelmonWrapper pw;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               p = (BattleParticipant)var2.next();
               pw = p.getPokemonFromUUID(fleeingUUID);
            } while(pw == null);

            if (pw.isFainted() && p.getType() == ParticipantType.Player) {
               this.forfeitOrFlee(pw);
               if (!this.battleEnded) {
                  Pixelmon.network.sendTo(new FailedSwitchFleeTask(), ((PlayerParticipant)p).player);
               }
            } else {
               p.wait = false;

               PixelmonWrapper pw2;
               for(Iterator var5 = p.controlledPokemon.iterator(); var5.hasNext(); pw2.wait = false) {
                  pw2 = (PixelmonWrapper)var5.next();
                  pw2.willTryFlee = true;
               }
            }
         }
      }
   }

   public ParticipantType[][] getBattleType(BattleParticipant teammate) {
      ParticipantType[][] type = new ParticipantType[2][];
      ArrayList team1 = new ArrayList();
      ArrayList team2 = new ArrayList();
      Iterator var5 = this.participants.iterator();

      while(var5.hasNext()) {
         BattleParticipant p = (BattleParticipant)var5.next();
         if (p.team == teammate.team) {
            team1.add(p.getType());
         } else {
            team2.add(p.getType());
         }
      }

      type[0] = new ParticipantType[team1.size()];

      int i;
      for(i = 0; i < team1.size(); ++i) {
         type[0][i] = (ParticipantType)team1.get(i);
      }

      type[1] = new ParticipantType[team2.size()];

      for(i = 0; i < team2.size(); ++i) {
         type[1][i] = (ParticipantType)team2.get(i);
      }

      return type;
   }

   public void updatePokemonHealth() {
      if (this.init) {
         this.participants.stream().filter((p) -> {
            return p instanceof PlayerParticipant;
         }).forEach((p) -> {
            ((PlayerParticipant)p).updatePokemonHealth();
         });
      }

   }

   public ArrayList getOpponents(BattleParticipant participant) {
      return (ArrayList)this.participants.stream().filter((p) -> {
         return p.team != participant.team;
      }).collect(Collectors.toList());
   }

   public ArrayList getTeam(BattleParticipant participant) {
      return participant == null ? new ArrayList() : (ArrayList)this.participants.stream().filter((p) -> {
         return p.team == participant.team;
      }).collect(Collectors.toList());
   }

   public ArrayList getActivePokemon() {
      ArrayList activePokemon = new ArrayList();
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         activePokemon.addAll(p.controlledPokemon);
      }

      return activePokemon;
   }

   public ArrayList getActiveUnfaintedPokemon() {
      return (ArrayList)this.getActivePokemon().stream().filter((pw) -> {
         return !pw.isFainted();
      }).collect(Collectors.toList());
   }

   public ArrayList getActiveFaintedPokemon() {
      return (ArrayList)this.getActivePokemon().stream().filter((pw) -> {
         return pw.isFainted();
      }).collect(Collectors.toList());
   }

   public ArrayList getAdjacentPokemon(PixelmonWrapper pokemon) {
      return (ArrayList)this.getActiveUnfaintedPokemon().stream().filter((pw) -> {
         return Math.abs(pw.battlePosition - pokemon.battlePosition) <= 1 && pw != pokemon;
      }).collect(Collectors.toList());
   }

   public ArrayList getAdjacentPokemonAndSelf(PixelmonWrapper pokemon) {
      if (pokemon == null) {
         return new ArrayList();
      } else {
         ArrayList targets = this.getAdjacentPokemon(pokemon);
         targets.add(pokemon);
         return targets;
      }
   }

   public ArrayList getTeamPokemon(BattleParticipant participant) {
      ArrayList team = this.getTeam(participant);
      ArrayList teamPokemon = new ArrayList();
      Iterator var4 = team.iterator();

      while(var4.hasNext()) {
         BattleParticipant p = (BattleParticipant)var4.next();
         Iterator var6 = p.controlledPokemon.iterator();

         while(var6.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var6.next();
            if (pw != null) {
               teamPokemon.add(pw);
            }
         }
      }

      return teamPokemon;
   }

   public ArrayList getTeamPokemon(PixelmonWrapper pokemon) {
      return this.getTeamPokemon(pokemon.getParticipant());
   }

   public ArrayList getTeamPokemon(EntityPixelmon pokemon) {
      return this.getTeamPokemon(pokemon.getParticipant());
   }

   public ArrayList getTeamPokemonExcludeSelf(PixelmonWrapper pokemon) {
      ArrayList teamPokemon = this.getTeamPokemon(pokemon);
      teamPokemon.remove(pokemon);
      return teamPokemon;
   }

   public ArrayList getOpponentPokemon(BattleParticipant participant) {
      ArrayList opponents = this.getOpponents(participant);
      ArrayList opponentPokemon = new ArrayList();
      Iterator var4 = opponents.iterator();

      while(var4.hasNext()) {
         BattleParticipant p = (BattleParticipant)var4.next();
         opponentPokemon.addAll(p.controlledPokemon);
      }

      return opponentPokemon;
   }

   public ArrayList getOpponentPokemon(PixelmonWrapper pw) {
      return this.getOpponentPokemon(pw.getParticipant());
   }

   public boolean isInBattle(PixelmonWrapper pokemon) {
      Iterator var2 = this.getActivePokemon().iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(pw != pokemon);

      return true;
   }

   public BattleParticipant getParticipantForEntity(EntityLivingBase entity) {
      Iterator var2 = this.participants.iterator();

      BattleParticipant p;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         p = (BattleParticipant)var2.next();
      } while(p.getEntity() != entity);

      return p;
   }

   public void sendDamagePacket(PixelmonWrapper target, int damage) {
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant p = (BattleParticipant)var3.next();
         p.sendDamagePacket(target, damage);
      }

      this.spectators.forEach((spectator) -> {
         spectator.sendDamagePacket(target, damage);
      });
   }

   public void sendHealPacket(PixelmonWrapper target, int amount) {
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant p = (BattleParticipant)var3.next();
         p.sendHealPacket(target, amount);
      }

      this.spectators.forEach((spectator) -> {
         spectator.sendHealPacket(target, amount);
      });
   }

   public PixelmonWrapper getOppositePokemon(PixelmonWrapper pw) {
      ArrayList oppPokemon = pw.bc.getOpponentPokemon(pw.getParticipant());
      ArrayList teamPokemon = pw.bc.getTeamPokemon(pw.getParticipant());

      int index;
      for(index = teamPokemon.indexOf(pw); index >= oppPokemon.size(); --index) {
      }

      return (PixelmonWrapper)oppPokemon.get(index);
   }

   public PixelmonWrapper getPokemonAtPosition(int position) {
      ArrayList arr = new ArrayList();
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant bp = (BattleParticipant)var3.next();
         arr.addAll(bp.controlledPokemon);
      }

      return position >= arr.size() ? null : (PixelmonWrapper)arr.get(position);
   }

   public int getPositionOfPokemon(PixelmonWrapper poke) {
      ArrayList arr = new ArrayList();
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant bp = (BattleParticipant)var3.next();
         arr.addAll(bp.controlledPokemon);
      }

      return arr.indexOf(poke);
   }

   public int getPositionOfPokemon(PixelmonWrapper poke, BattleParticipant bp) {
      return bp.controlledPokemon.indexOf(poke);
   }

   public BattleStage getStage() {
      return this.stage;
   }

   public void enableReturnHeldItems(PixelmonWrapper attacker, PixelmonWrapper target) {
      BattleParticipant targetParticipant = target.getParticipant();
      if (!this.simulateMode && !(targetParticipant instanceof WildPixelmonParticipant)) {
         target.enableReturnHeldItem();
         attacker.enableReturnHeldItem();
      }

   }

   public boolean checkValid() {
      if (this.battleEnded) {
         return false;
      } else {
         ArrayList pokemon = new ArrayList();
         ArrayList activePokemon = this.getActivePokemon();
         if (activePokemon.size() <= 1) {
            return false;
         } else {
            Iterator var3 = activePokemon.iterator();

            while(var3.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var3.next();
               if (pokemon.contains(pw)) {
                  this.endBattle(EnumBattleEndCause.FORCE);
                  return false;
               }

               pokemon.add(pw);
            }

            return true;
         }
      }
   }

   public PlayerParticipant getPlayer(String name) {
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         if (p.getType() == ParticipantType.Player) {
            PlayerParticipant player = (PlayerParticipant)p;
            if (player.player.getDisplayNameString().equals(name)) {
               return player;
            }
         }
      }

      return null;
   }

   public PlayerParticipant getPlayer(EntityPlayer player) {
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         if (p.getType() == ParticipantType.Player) {
            PlayerParticipant currentPlayer = (PlayerParticipant)p;
            if (player == currentPlayer.player) {
               return currentPlayer;
            }
         }
      }

      return null;
   }

   public List getPlayers() {
      List players = new ArrayList(this.participants.size());
      Iterator var2 = this.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         if (p.getType() == ParticipantType.Player) {
            players.add((PlayerParticipant)p);
         }
      }

      return players;
   }

   public boolean isTeamDefeated(BattleParticipant participant) {
      Iterator var2 = this.getTeam(participant).iterator();

      BattleParticipant p2;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         p2 = (BattleParticipant)var2.next();
      } while(p2.isDefeated);

      return false;
   }

   public int getTurnForPokemon(PixelmonWrapper pokemon) {
      for(int i = 0; i < this.turnList.size(); ++i) {
         if (this.turnList.get(i) == pokemon) {
            return i;
         }
      }

      return -1;
   }

   public BattleParticipant otherParticipant(BattleParticipant participant) {
      Iterator var2 = this.participants.iterator();

      BattleParticipant p;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         p = (BattleParticipant)var2.next();
      } while(p == participant);

      return p;
   }

   public PixelmonWrapper getFirstMover(PixelmonWrapper... pokemonList) {
      Iterator var2 = this.turnList.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper turnPokemon = (PixelmonWrapper)var2.next();
         PixelmonWrapper[] var4 = pokemonList;
         int var5 = pokemonList.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PixelmonWrapper pokemon = var4[var6];
            if (turnPokemon == pokemon) {
               return pokemon;
            }
         }
      }

      return null;
   }

   public PixelmonWrapper getFirstMover(ArrayList pokemonList) {
      Iterator var2 = this.turnList.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper turnPokemon = (PixelmonWrapper)var2.next();
         Iterator var4 = pokemonList.iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pokemon = (PixelmonWrapper)var4.next();
            if (turnPokemon == pokemon) {
               return pokemon;
            }
         }
      }

      return null;
   }

   public PixelmonWrapper getLastMover(PixelmonWrapper... pokemonList) {
      for(int i = this.turnList.size() - 1; i >= 0; --i) {
         PixelmonWrapper turnPokemon = (PixelmonWrapper)this.turnList.get(i);
         PixelmonWrapper[] var4 = pokemonList;
         int var5 = pokemonList.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PixelmonWrapper pokemon = var4[var6];
            if (turnPokemon == pokemon) {
               return pokemon;
            }
         }
      }

      return null;
   }

   public PixelmonWrapper getLastMover(ArrayList pokemonList) {
      for(int i = this.turnList.size() - 1; i >= 0; --i) {
         PixelmonWrapper turnPokemon = (PixelmonWrapper)this.turnList.get(i);
         Iterator var4 = pokemonList.iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pokemon = (PixelmonWrapper)var4.next();
            if (turnPokemon == pokemon) {
               return pokemon;
            }
         }
      }

      return null;
   }

   public Optional getAbilityIfPresent(Class abilityClass) {
      Iterator var2 = this.getActivePokemon().iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            return Optional.empty();
         }

         pw = (PixelmonWrapper)var2.next();
      } while(!pw.getBattleAbility(false).isAbility(abilityClass));

      return Optional.of(pw.getBattleAbility(false));
   }

   public void sendSwitchPacket(UUID oldUUID, PixelmonWrapper newPokemon) {
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant participant = (BattleParticipant)var3.next();
         if (participant instanceof PlayerParticipant) {
            Pixelmon.network.sendTo(new SwitchOutTask(oldUUID, newPokemon), ((PlayerParticipant)participant).player);
            EntityPlayerMP player = (EntityPlayerMP)participant.getEntity();
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.func_110124_au());
            if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(player.func_110124_au(), newPokemon.pokemon, EnumPokedexRegisterStatus.seen, "pokedexKey"))) {
               storage.pokedex.set(newPokemon.pokemon, EnumPokedexRegisterStatus.seen);
               storage.pokedex.update();
               if (player != null) {
                  storage.pokedex.update();
               }
            }
         }
      }

      this.spectators.forEach((spectator) -> {
         spectator.sendMessage(new SwitchOutTask(oldUUID, newPokemon));
         EntityPlayerMP player = spectator.getEntity();
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.func_110124_au());
         if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(player.func_110124_au(), newPokemon.pokemon, EnumPokedexRegisterStatus.seen, "pokedexKey"))) {
            storage.pokedex.set(newPokemon.pokemon, EnumPokedexRegisterStatus.seen);
            storage.pokedex.update();
            if (player != null) {
               storage.pokedex.update();
            }
         }

      });
   }

   public void addSpectator(Spectator spectator) {
      this.spectators.add(spectator);
      BattleRegistry.registerSpectator(spectator, this);
   }

   public void removeSpectator(Spectator spectator) {
      this.spectators.remove(spectator);
      BattleRegistry.unregisterSpectator(spectator);
      Pixelmon.EVENT_BUS.post(new SpectateEvent.StopSpectate(spectator.getEntity(), this));
   }

   public boolean hasSpectator(EntityPlayer player) {
      Iterator var2 = this.spectators.iterator();

      Spectator spectator;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         spectator = (Spectator)var2.next();
      } while(spectator.getEntity() != player);

      return true;
   }

   public boolean removeSpectator(EntityPlayerMP player) {
      for(int i = 0; i < this.spectators.size(); ++i) {
         Spectator spectator = (Spectator)this.spectators.get(i);
         if (spectator.getEntity() == player) {
            this.spectators.remove(i);
            BattleRegistry.unregisterSpectator(spectator);
            Pixelmon.EVENT_BUS.post(new SpectateEvent.StopSpectate(player, this));
            return true;
         }
      }

      return false;
   }

   public ArrayList getPlayerSpectators(PlayerParticipant player) {
      ArrayList playerSpectators = new ArrayList(this.spectators.size());
      String playerName = player.player.getDisplayNameString();
      playerSpectators.addAll((Collection)this.spectators.stream().filter((spectator) -> {
         return spectator.watchedName.equals(playerName);
      }).collect(Collectors.toList()));
      return playerSpectators;
   }

   public PixelmonWrapper getPokemonFromUUID(UUID uuid) {
      PixelmonWrapper pw = null;
      Iterator var3 = this.participants.iterator();

      while(var3.hasNext()) {
         BattleParticipant p = (BattleParticipant)var3.next();
         pw = p.getPokemonFromUUID(uuid);
         if (pw != null) {
            break;
         }
      }

      return pw;
   }

   public List getDefaultTurnOrder() {
      return CalcPriority.getDefaultTurnOrder(this);
   }

   public void removeFromTurnList(PixelmonWrapper pw) {
      for(int i = this.turn + 1; i < this.turnList.size(); ++i) {
         if (pw.equals(this.turnList.get(i))) {
            this.turnList.remove(i);
            return;
         }
      }

   }

   public boolean isLastMover() {
      return this.turn >= this.turnList.size() - 1;
   }

   public boolean containsParticipantType(Class participantType) {
      Iterator var2 = this.participants.iterator();

      BattleParticipant bp;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         bp = (BattleParticipant)var2.next();
      } while(bp.getClass() != participantType);

      return true;
   }

   public void updateFormChange(EntityPixelmon pokemon) {
      this.sendToPlayers(new FormBattleUpdate(pokemon.getPokemonData().getUUID(), pokemon.getFormIncludeTransformed()));
   }

   public void updateFormChange(PixelmonWrapper pokemon) {
      this.sendToPlayers(new FormBattleUpdate(pokemon.getPokemonUUID(), pokemon.getForm()));
   }

   public boolean isLevelingDisabled() {
      if (!PixelmonConfig.allowPVPExperience) {
         boolean allPlayers = true;
         Iterator var2 = this.participants.iterator();

         while(var2.hasNext()) {
            BattleParticipant p = (BattleParticipant)var2.next();
            if (p.getType() != ParticipantType.Player) {
               allPlayers = false;
            }
         }

         if (allPlayers) {
            return true;
         }
      }

      if (!PixelmonConfig.allowTrainerExperience) {
         Iterator var4 = this.participants.iterator();

         while(var4.hasNext()) {
            BattleParticipant p = (BattleParticipant)var4.next();
            if (p.getType() == ParticipantType.Trainer) {
               return true;
            }
         }
      }

      return this.rules.raiseToCap;
   }

   public boolean isRaid() {
      Iterator var1 = this.participants.iterator();

      while(var1.hasNext()) {
         BattleParticipant bp = (BattleParticipant)var1.next();
         if (bp instanceof RaidPixelmonParticipant) {
            return true;
         }
      }

      return this.rules != null && this.rules.battleType == EnumBattleType.Raid;
   }
}
