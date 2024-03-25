package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.raids.EndRaidEvent;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.OHKO;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.ai.RaidAI;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.battles.raids.RaidGovernor;
import com.pixelmonmod.pixelmon.battles.raids.RaidSettings;
import com.pixelmonmod.pixelmon.battles.status.GastroAcid;
import com.pixelmonmod.pixelmon.battles.status.LightScreen;
import com.pixelmonmod.pixelmon.battles.status.Reflect;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.tasks.HPUpdateTask;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.forms.EnumUrshifu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

public class RaidPixelmonParticipant extends BattleParticipant {
   private final RaidData raid;
   private final RaidGovernor governor;
   private RaidAI ai;
   private static int idCounter = 0;
   private PixelmonWrapper lastPokemonThatHitShields;
   private boolean justTransformed = false;
   public static final String[] BANNED_ATTACKS = new String[]{"Bug Bite", "Curse", "Destiny Bond", "Explosion", "Incinerate", "Knock Off", "Perish Song", "Pluck", "Self-Destruct", "Self Destruct", "Super Fang"};
   private static final Set BLOCKED_STATUSES;

   public RaidPixelmonParticipant(RaidData raid, RaidSettings settings, EntityPixelmon pixelmon) {
      super(1);
      this.raid = raid;
      this.governor = new RaidGovernor(settings);
      this.init(pixelmon);
   }

   public PixelmonWrapper getWrapper() {
      return (PixelmonWrapper)this.controlledPokemon.get(0);
   }

   private void init(EntityPixelmon pixelmon) {
      this.governor.init(pixelmon);
      PixelmonWrapper pw = new PixelmonWrapper(this, pixelmon, 0);
      this.allPokemon = new PixelmonWrapper[]{pw};
      this.controlledPokemon.add(pw);
   }

   public ParticipantType getType() {
      return ParticipantType.RaidPokemon;
   }

   public boolean hasMorePokemon() {
      return this.countAblePokemon() == 1;
   }

   public boolean hasMorePokemonReserve() {
      return false;
   }

   public boolean canGainXP() {
      return false;
   }

   public void startBattle(BattleControllerBase bc) {
      super.startBattle(bc);
      PixelmonWrapper pw = this.getWrapper();
      EnumBossMode bossMode = this.governor.bossMode;
      if (bossMode != EnumBossMode.NotBoss) {
         int lvl = 1;
         Iterator var5 = bc.participants.iterator();

         while(var5.hasNext()) {
            BattleParticipant p = (BattleParticipant)var5.next();
            if (p.team != this.team && p instanceof PlayerParticipant) {
               int highestLevel = ((PlayerParticipant)p).getHighestLevel();
               lvl = Math.max(lvl, highestLevel);
            }
         }

         lvl += bossMode.getExtraLevels();
         Stats stats = pw.getStats();
         stats.ivs.maximizeIVs();
         stats.evs.randomizeMaxEVs();
         pw.setTempLevel(lvl);
      }

      pw.setHealth(pw.getMaxHealth());
      pw.getMoveset().forEach((attack) -> {
         if (attack != null) {
            attack.pp = attack.getMaxPP();
         }

      });
      this.setDynamax(pw);
      this.ai = new RaidAI(this.governor, this);
      this.setBattleAI(this.ai);
      this.governor.cycleDynamax(this);
      bc.sendHealPacket(this.getWrapper(), this.getWrapper().getMaxHealth());
   }

   private void setDynamax(PixelmonWrapper pw) {
      if (this.governor.isGigantamax()) {
         pw.setPrevForm(pw.getFormEnum());
         if (pw.getSpecies() == EnumSpecies.Urshifu) {
            EnumUrshifu form = (EnumUrshifu)pw.getFormEnum();
            pw.setForm(form.getGigantamax());
         } else {
            pw.setForm(pw.pokemon.getSpecies().getFormEnum("gmax"));
         }

         pw.isDynamax = 2;
      } else {
         pw.isDynamax = 1;
      }

      pw.dynamaxTurns = Integer.MAX_VALUE;
      pw.dynamaxAnimationTicks = 50;
      this.dynamax = pw.getPokemonUUID();
      pw.updateHPIncrease();
      pw.setHealth(pw.getMaxHealth());
      pw.update(EnumUpdateType.ALL);
   }

   public void endBattle(EnumBattleEndCause cause) {
      Pixelmon.EVENT_BUS.post(new EndRaidEvent(this.raid, this.governor, this.bc.getOpponents(this), this, this.isDefeated));
      Iterator var2;
      if (this.isDefeated) {
         var2 = this.raid.getPlayers().iterator();

         while(var2.hasNext()) {
            RaidData.RaidPlayer rp = (RaidData.RaidPlayer)var2.next();
            rp.winRaid(this, this.raid, this.governor);
         }
      } else if (!this.getEntity().func_130014_f_().field_72995_K) {
         this.raid.getDenEntity(this.getEntity().func_130014_f_()).ifPresent((den) -> {
            if (!den.func_130014_f_().field_72995_K) {
               double radius = 3.0;
               WorldServer world = (WorldServer)den.func_130014_f_();
               world.func_184133_a((EntityPlayer)null, den.func_180425_c(), SoundEvents.field_187539_bB, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.2F) * 0.7F);

               for(int i = 0; i < 200; ++i) {
                  Vec3d sphere = RandomHelper.nextSpherePoint(radius);
                  world.func_180505_a(EnumParticleTypes.SMOKE_LARGE, false, den.field_70165_t + sphere.field_72450_a, den.field_70163_u + world.field_73012_v.nextDouble() * radius * 2.0, den.field_70161_v + sphere.field_72449_c, 1, world.field_73012_v.nextDouble() * 2.0 - 1.0, world.field_73012_v.nextDouble() * 2.0 - 1.0, world.field_73012_v.nextDouble() * 2.0 - 1.0, world.field_73012_v.nextDouble() * 0.5, new int[0]);
               }
            }

         });
      }

      var2 = this.controlledPokemon.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         pw.entity.onEndBattle();
         pw.entity.func_70106_y();
      }

   }

   private boolean shouldDespawn() {
      return true;
   }

   public void getNextPokemon(int position) {
   }

   public UUID getNextPokemonUUID() {
      return null;
   }

   public TextComponentBase getName() {
      String key = "";
      if (!this.controlledPokemon.isEmpty()) {
         key = "pixelmon." + ((PixelmonWrapper)this.controlledPokemon.get(0)).entity.getLocalizedName().toLowerCase() + ".name";
      }

      return new TextComponentTranslation(key, new Object[0]);
   }

   public MoveChoice getMove(PixelmonWrapper pw) {
      return this.bc == null ? null : this.getBattleAI().getNextMove(pw);
   }

   public PixelmonWrapper switchPokemon(PixelmonWrapper pw, UUID newPixelmonUUID) {
      return null;
   }

   public boolean checkPokemon() {
      boolean allGood = true;
      Iterator var2 = this.controlledPokemon.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         if (pw.getMoveset().isEmpty()) {
            pw.entity.getPokemonData().getMoveset().clear();
            pw.entity.getPokemonData().getMoveset().addAll(pw.entity.getPokemonData().getBaseStats().loadMoveset(pw.getLevelNum()));
            if (pw.getMoveset().isEmpty()) {
               if (PixelmonConfig.printErrors) {
                  Pixelmon.LOGGER.info("Couldn't load " + pw.entity.getLocalizedName() + "'s moves.");
               }

               allGood = false;
            }
         }
      }

      return allGood;
   }

   public void updatePokemon(PixelmonWrapper pw) {
   }

   public EntityLiving getEntity() {
      return this.controlledPokemon.isEmpty() ? null : ((PixelmonWrapper)this.controlledPokemon.get(0)).entity;
   }

   public void updateOtherPokemon() {
   }

   public void setPosition(double[] ds) {
   }

   public boolean canDynamax() {
      return true;
   }

   public String getDisplayName() {
      Iterator var1 = this.controlledPokemon.iterator();
      if (var1.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var1.next();
         return pw.getNickname();
      } else {
         return super.getDisplayName();
      }
   }

   public boolean areShieldsUp() {
      return this.governor.shields > 0;
   }

   public void onSwitchIn(BattleControllerBase bc, PixelmonWrapper pw) {
      this.setDynamax(pw);
   }

   public void onEndTurn(BattleControllerBase bc) {
      if (!bc.simulateMode && this.isAlive() && !this.governor.done) {
         float hp = this.getWrapper().getHealthPercent();
         if (hp < 50.0F && this.governor.previousTurnHP >= 50.0F && !this.governor.desperate) {
            this.governor.desperate = true;
            bc.sendToAll("raid.desperate", this.getDisplayName());
         }

         this.tryShields();
         this.lastPokemonThatHitShields = null;
         this.governor.previousTurnHP = this.getWrapper().getHealthPercent();
         int i = 0;

         for(Iterator var4 = this.bc.getOpponents(this).iterator(); var4.hasNext(); ++i) {
            BattleParticipant bp = (BattleParticipant)var4.next();
            if (this.governor.isKnockedOut(i) && this.governor.tryRevive(i)) {
               bp.isDefeated = false;
               bp.allPokemon[0].healByPercent(100.0F);
               String name = bp.getDisplayName();
               bc.sendToAll("raid.recover", name);
               this.bc.reviveAfterDefeat(bp);
            }

            if (bp.dynamax != null) {
               this.governor.onDynamax();
            }
         }

         this.governor.cycleDynamax(this);
      }

   }

   public boolean onTakeTurn(BattleControllerBase bc, PixelmonWrapper pw) {
      if (!this.governor.done && !bc.simulateMode) {
         if (this.governor.incrementTurnCounter()) {
            this.governor.done = true;
            bc.sendToAll("raid.storm.c");
            bc.sendToAll("raid.blowout");
            bc.endBattle();
            return false;
         } else {
            int i = 0;

            for(Iterator var4 = this.bc.getOpponents(this).iterator(); var4.hasNext(); ++i) {
               BattleParticipant bp = (BattleParticipant)var4.next();
               if (this.governor.canCheer(i)) {
                  this.tryCheer(bp);
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean onUseAttack(BattleControllerBase bc, PixelmonWrapper pw) {
      if (this.isAlive() && !this.governor.done && !bc.simulateMode) {
         if (this.governor.tryShockwave(pw.hasPrimaryStatus(false))) {
            this.shockwave(pw);
         }

         this.doAttacks(pw, this.governor.getAttacksPerTurn(), false);
      }

      return true;
   }

   private void doAttacks(PixelmonWrapper pw, int attacks, boolean hasTransformed) {
      if (attacks > 0) {
         ArrayList choices = this.ai.getTopNAttackChoices(attacks, pw);
         Collections.shuffle(choices);
         Iterator var5 = choices.iterator();

         while(var5.hasNext()) {
            MoveChoice choice = (MoveChoice)var5.next();
            if (pw.hasStatus(StatusType.Sleep)) {
               this.bc.sendToAll("pixelmon.status.stillsleeping", this.getDisplayName());
            } else {
               pw.chooseMove(choice);
               pw.useAttack(false);
               if (!hasTransformed && this.justTransformed) {
                  this.justTransformed = false;
                  this.doAttacks(pw, attacks - 1, true);
                  break;
               }
            }
         }

      }
   }

   public void onUseAttackPost(BattleControllerBase bc, PixelmonWrapper pw) {
      if (pw.attack.isAttack("Transform")) {
         this.governor.settings.rerollMoveset(pw.pokemon);
         this.justTransformed = true;
      }

   }

   public boolean onUseAttackOther(BattleControllerBase bc, Attack attack, BattleParticipant bp, PixelmonWrapper user) {
      boolean fail = attack.isAttack(BANNED_ATTACKS);
      boolean ohko = attack.getActualMove().hasEffect(OHKO.class);
      if (ohko && this.areShieldsUp() && bp != this) {
         bc.sendToAll("pixelmon.battletext.used", user.getNickname(), attack.getMove().getTranslatedName());
         RaidGovernor var10000 = this.governor;
         var10000.shields -= 2;
         bc.sendToAll("raid.ohko");
         if (this.governor.shields <= 0) {
            bc.sendToAll("raid.barrier.destroy", this.getDisplayName());
         }

         this.getWrapper().updateRaidShields(this.governor.shields);
      } else if (ohko || fail) {
         bc.sendToAll("pixelmon.battletext.used", user.getNickname(), attack.getMove().getTranslatedName());
         bc.sendToAll("pixelmon.effect.effectfailed");
      }

      return fail || ohko;
   }

   public void shockwave(PixelmonWrapper pw) {
      if (!this.governor.done && !this.bc.simulateMode) {
         this.bc.sendToAll("raid.shockwave", this.getDisplayName());
         Iterator var2 = this.bc.getOpponentPokemon((BattleParticipant)this).iterator();

         while(var2.hasNext()) {
            PixelmonWrapper opw = (PixelmonWrapper)var2.next();
            opw.addStatus(new GastroAcid(), pw);
            opw.getBattleStats().clearBattleStats();
         }

         pw.clearStatus();
      }
   }

   public boolean onTargeted(PixelmonWrapper user, Attack attack) {
      return !this.governor.done && this.areShieldsUp() && attack.getMove().getAttackCategory() == AttackCategory.STATUS;
   }

   public boolean onAddStatus(BattleControllerBase bc, PixelmonWrapper user, PixelmonWrapper target, StatusBase status) {
      return this.areShieldsUp() && BLOCKED_STATUSES.contains(status.type);
   }

   public float onHit(PixelmonWrapper source, float damage, DamageTypeEnum damageType) {
      if (!this.governor.done && !this.bc.simulateMode) {
         if (this.isAlive()) {
            if (this.areShieldsUp() && damageType == DamageTypeEnum.ATTACK) {
               damage = Math.max(0.0F, damage * 0.1F - 10.0F);
               if (this.lastPokemonThatHitShields == null || this.lastPokemonThatHitShields != source) {
                  --this.governor.shields;
               }

               if (this.governor.shields > 0 && source.attack != null && source.attack.isMax) {
                  --this.governor.shields;
                  this.bc.sendToAll("raid.ohko");
               }

               if (this.governor.shields <= 0) {
                  this.bc.sendToAll("raid.barrier.destroy", this.getDisplayName());
               }

               this.getWrapper().updateRaidShields(this.governor.shields);
               this.lastPokemonThatHitShields = source;
            }

            float hp = this.getWrapper().getHealthPercent((float)this.getWrapper().getHealth() - damage);
            if (hp <= this.governor.shieldHPBound) {
               this.governor.shouldRaiseShields = true;
            }

            if (this.tryShields()) {
               int hpNow = this.getWrapper().getHealth();
               float bound = (float)(this.governor.shieldUses + 1) * this.governor.shieldHPPercent;
               int newHp = this.getWrapper().getPercentMaxHealth(bound);
               if (newHp < hpNow) {
                  this.getWrapper().setHealth(newHp);
                  this.bc.sendToPlayers(new HPUpdateTask(this.getWrapper(), newHp - hpNow));
               }

               damage = 0.0F;
            }
         }

         return damage;
      } else {
         return damage;
      }
   }

   private boolean tryShields() {
      if (!this.governor.done && !this.bc.simulateMode) {
         if (this.governor.shouldRaiseShields && this.governor.shields == 0 && this.governor.raiseShields()) {
            this.getWrapper().setRaidShields(this.governor.shields);
            this.bc.sendToAll("raid.barrier", this.getDisplayName());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void tryCheer(BattleParticipant cheerer) {
      if (!this.governor.done && !this.bc.simulateMode) {
         this.bc.sendToAll("raid.cheer", cheerer.getDisplayName());
         if (this.governor.tryCheer()) {
            boolean success;
            int effect = RandomHelper.rand.nextInt(6);
            PixelmonWrapper pw = cheerer.allPokemon[0];
            success = false;
            PixelmonWrapper toHeal;
            Iterator var12;
            label60:
            switch (effect) {
               case 0:
                  if ((new Reflect()).apply(pw, false)) {
                     success = true;
                  }
                  break;
               case 1:
                  if ((new LightScreen()).apply(pw, false)) {
                     success = true;
                  }
                  break;
               case 2:
                  if (this.governor.shields > 0) {
                     this.governor.shields = 0;
                     this.bc.sendToAll("raid.barrier.destroy", this.getDisplayName());
                     this.getWrapper().updateRaidShields(this.governor.shields);
                     success = true;
                  }
                  break;
               case 3:
                  StatsType stat = StatsType.getRandomBattleMutableStat();
                  var12 = this.bc.getTeamPokemon(pw).iterator();

                  while(var12.hasNext()) {
                     toHeal = (PixelmonWrapper)var12.next();
                     toHeal.getBattleStats().increaseStat(1, stat, pw, false);
                  }

                  success = true;
                  break;
               case 4:
                  var12 = this.bc.getTeamPokemon(pw).iterator();

                  while(true) {
                     if (!var12.hasNext()) {
                        break label60;
                     }

                     toHeal = (PixelmonWrapper)var12.next();
                     if (toHeal.removePrimaryStatus(true) != null) {
                        success = true;
                     }
                  }
               case 5:
                  float hp = 100.0F;
                  toHeal = null;
                  Iterator var8 = this.bc.getTeamPokemon(pw).iterator();

                  while(var8.hasNext()) {
                     PixelmonWrapper ally = (PixelmonWrapper)var8.next();
                     float ahp = ally.getHealthPercent();
                     if (!ally.isFainted() && ahp < 100.0F && ahp < hp) {
                        hp = ahp;
                        toHeal = ally;
                     }
                  }

                  if (toHeal != null) {
                     toHeal.healByPercent(100.0F);
                     this.getWrapper().updateHPIncrease();
                     this.bc.sendToAll("pixelmon.effect.washealed", toHeal.getNickname());
                     success = true;
                  }
            }

            if (success) {
               String name = cheerer.getDisplayName();
               this.bc.sendToAll("raid.cheer.success", name);
            } else {
               this.bc.sendToAll("raid.cheer.fail");
            }
         } else {
            this.bc.sendToAll("raid.cheer.fail");
         }

      }
   }

   public void onOpponentKO(BattleControllerBase bc, PixelmonWrapper pw) {
      if (!this.governor.done && !bc.simulateMode) {
         if (this.isAlive()) {
            int i = 0;

            for(Iterator var4 = this.bc.getOpponents(this).iterator(); var4.hasNext(); ++i) {
               BattleParticipant bp = (BattleParticipant)var4.next();
               if (bp.allPokemon[0] == pw) {
                  this.governor.knockout(i);
                  if (pw.isDynamax > 0) {
                     pw.dynamax(true, pw.getHealthPercent());
                  }
                  break;
               }
            }

            ++this.governor.kills;
            if (this.governor.kills >= this.governor.getLives()) {
               this.governor.done = true;
               bc.sendToAll("raid.blowout");
               bc.endBattle();
            } else {
               int message = this.governor.getLives() - this.governor.kills;
               switch (message) {
                  case 1:
                     bc.sendToAll("raid.storm.c");
                     break;
                  case 2:
                     bc.sendToAll("raid.storm.b");
                     break;
                  default:
                     bc.sendToAll("raid.storm.a");
               }
            }
         }

      }
   }

   public boolean canDynamax(BattleParticipant participant) {
      return !this.governor.done && !this.governor.hasDynamaxOccured && this.governor.dynamaxIndex == this.getOpponents().indexOf(participant);
   }

   public boolean isAlive() {
      return this.allPokemon[0].isAlive();
   }

   public RaidGovernor getGovernor() {
      return this.governor;
   }

   static {
      BLOCKED_STATUSES = new HashSet(Arrays.asList(StatusType.Poison, StatusType.PoisonBadly, StatusType.Burn, StatusType.Sleep, StatusType.Paralysis, StatusType.Yawn, StatusType.Freeze, StatusType.Confusion, StatusType.Infatuated));
   }
}
