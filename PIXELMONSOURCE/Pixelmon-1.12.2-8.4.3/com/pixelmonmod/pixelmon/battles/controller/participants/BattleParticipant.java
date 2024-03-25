package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.battles.SetBattleAIEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RunAway;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class BattleParticipant {
   public boolean startedBattle = false;
   public BattleControllerBase bc;
   public int team = 0;
   public boolean wait = false;
   public List controlledPokemon;
   public PixelmonWrapper[] allPokemon;
   public boolean isDefeated = false;
   public Long lngLastMoveMilli;
   public int numControlledPokemon;
   private double[] originalPos;
   public boolean faintedLastTurn = false;
   public EnumPokeballs lastFailedCapture;
   public ArrayList switchingIn = new ArrayList(2);
   public List switchingOut = new ArrayList();
   private BattleAIBase battleAI;
   public UUID evolution = null;
   public UUID ultraBurst = null;
   public UUID ashNinja = null;
   public UUID dynamax = null;
   public boolean usedZ = false;

   public Long getCurrentTime() {
      return Long.parseLong("" + (new Date()).getTime());
   }

   public void resetMoveTimer() {
      this.lngLastMoveMilli = this.getCurrentTime();
   }

   public Long getTurnTimeSeconds() {
      if (this.lngLastMoveMilli == null) {
         this.resetMoveTimer();
         return 0L;
      } else {
         return (this.getCurrentTime() - this.lngLastMoveMilli) / 1000L;
      }
   }

   public BattleParticipant(int numControlledPokemon) {
      this.setNumControlledPokemon(numControlledPokemon);
      this.lngLastMoveMilli = this.getCurrentTime();
   }

   public void setNumControlledPokemon(int numControlledPokemon) {
      this.numControlledPokemon = numControlledPokemon;
      this.controlledPokemon = new ArrayList(numControlledPokemon);
   }

   public boolean hasMorePokemon() {
      return this.countAblePokemon() > 0;
   }

   public abstract boolean hasMorePokemonReserve();

   public abstract boolean canGainXP();

   public void startBattle(BattleControllerBase bc) {
      this.bc = bc;
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper p = var2[var4];
         p.bc = bc;
         int levelCap = bc.rules.levelCap + (this instanceof TrainerParticipant ? ((TrainerParticipant)this).trainer.getBossMode().getExtraLevels() : 0);
         p.startHealth = p.getHealth();
         if ((levelCap < PixelmonServerConfig.maxLevel || bc.rules.raiseToCap) && (p.getLevelNum() > levelCap || bc.rules.raiseToCap)) {
            float healthPercent = p.getHealthPercent();
            p.setTempLevel(levelCap);
            p.animateHP = false;
            p.healByPercent(Math.max(0.0F, healthPercent - p.getHealthPercent()));
            p.animateHP = true;
         }

         int maxHealth = p.getMaxHealth();
         int startHealth = p.getHealth();
         if (startHealth > maxHealth) {
            p.setHealth(maxHealth);
         }

         if (bc.rules.fullHeal) {
            p.setHealth(maxHealth);
            p.clearStatus();
            Iterator var9 = p.getMoveset().iterator();

            while(var9.hasNext()) {
               Attack attack = (Attack)var9.next();
               if (attack != null) {
                  attack.pp = attack.getMaxPP();
               }
            }
         }
      }

      PixelmonWrapper p;
      for(Iterator var11 = this.controlledPokemon.iterator(); var11.hasNext(); p.entity.battleController = bc) {
         p = (PixelmonWrapper)var11.next();
      }

   }

   public abstract void endBattle(EnumBattleEndCause var1);

   public abstract TextComponentBase getName();

   public abstract MoveChoice getMove(PixelmonWrapper var1);

   public abstract PixelmonWrapper switchPokemon(PixelmonWrapper var1, UUID var2);

   public abstract boolean checkPokemon();

   public abstract void updatePokemon(PixelmonWrapper var1);

   public abstract EntityLivingBase getEntity();

   public abstract void updateOtherPokemon();

   public abstract ParticipantType getType();

   public abstract void getNextPokemon(int var1);

   public abstract UUID getNextPokemonUUID();

   public int countTeam() {
      return this.allPokemon.length;
   }

   public int countAblePokemon() {
      int i = 0;
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper pw = var2[var4];
         if (pw.isAlive()) {
            ++i;
         }
      }

      return i;
   }

   public float countHealthPercent() {
      float percent = 0.0F;
      float teamCount = 0.0F;
      PixelmonWrapper[] var3 = this.allPokemon;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PixelmonWrapper pw = var3[var5];
         if (pw.isAlive()) {
            ++teamCount;
            percent += pw.getHealthPercent();
         }
      }

      if (teamCount != 0.0F) {
         return percent / 100.0F / teamCount;
      } else {
         return 0.0F;
      }
   }

   public void tick() {
      this.animateDynamax();
   }

   public void clearTurnVariables() {
      Iterator var1 = this.controlledPokemon.iterator();

      while(var1.hasNext()) {
         PixelmonWrapper p = (PixelmonWrapper)var1.next();
         p.clearTurnVariables();
      }

   }

   public void selectAction() {
      Iterator var1 = this.controlledPokemon.iterator();

      while(var1.hasNext()) {
         PixelmonWrapper p = (PixelmonWrapper)var1.next();
         p.selectAIAction();
      }

   }

   public void animateDynamax() {
      Iterator var1 = this.controlledPokemon.iterator();

      while(var1.hasNext()) {
         PixelmonWrapper p = (PixelmonWrapper)var1.next();
         if (p.dynamaxAnimationTicks > 0) {
            p.entity.addDynamaxScale(0.02F);
            --p.dynamaxAnimationTicks;
         } else if (p.dynamaxAnimationTicks < 0) {
            p.entity.addDynamaxScale(-0.02F);
            ++p.dynamaxAnimationTicks;
            if (p.dynamaxAnimationTicks == 0) {
               p.entity.setDynamaxScale(0.0F);
            }
         }
      }

   }

   public boolean getWait() {
      if (this.wait) {
         return true;
      } else {
         Iterator var1 = this.controlledPokemon.iterator();

         PixelmonWrapper p;
         do {
            do {
               if (!var1.hasNext()) {
                  return false;
               }

               p = (PixelmonWrapper)var1.next();
            } while(!p.wait);
         } while(!p.isAlive() && !p.isSwitching);

         return true;
      }
   }

   public PixelmonWrapper getFaintedPokemon() {
      Iterator var1 = this.controlledPokemon.iterator();

      PixelmonWrapper pw;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         pw = (PixelmonWrapper)var1.next();
      } while(!pw.isFainted());

      return pw;
   }

   public boolean hasRemainingPokemon() {
      Iterator var1 = this.controlledPokemon.iterator();

      PixelmonWrapper pw;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         pw = (PixelmonWrapper)var1.next();
      } while(!pw.isAlive());

      return true;
   }

   public void setOriginalPosition() {
      this.originalPos = new double[3];
      this.originalPos[0] = this.getEntity().field_70165_t;
      this.originalPos[1] = this.getEntity().field_70163_u;
      this.originalPos[2] = this.getEntity().field_70161_v;
   }

   public void setNewPositions(BlockPos arenaPosition) {
   }

   public void setPosition(double[] ds) {
      this.getEntity().func_70634_a(ds[0], ds[1], ds[2]);
   }

   public void setToOriginalPosition() {
      this.setPosition(this.originalPos);
   }

   public void sendDamagePacket(PixelmonWrapper target, int damage) {
   }

   public void sendHealPacket(PixelmonWrapper target, int amount) {
   }

   public String getDisplayName() {
      return "";
   }

   public static boolean[] canSwitch(PixelmonWrapper p) {
      boolean canSwitch = true;
      boolean canFlee = true;

      StatusBase status;
      for(int i = 0; i < p.getStatusSize(); ++i) {
         status = p.getStatus(i);

         try {
            if (status.stopsSwitching()) {
               canSwitch = false;
               canFlee = false;
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      Iterator var8 = p.bc.globalStatusController.getGlobalStatuses().iterator();

      while(var8.hasNext()) {
         status = (StatusBase)var8.next();

         try {
            if (status.stopsSwitching()) {
               canSwitch = false;
               canFlee = false;
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      ArrayList opponents = p.bc.getOpponentPokemon(p.getParticipant());
      Iterator var10 = opponents.iterator();

      while(var10.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var10.next();
         if (pw.getBattleAbility().stopsSwitching(pw, p)) {
            canSwitch = false;
            canFlee = false;
         }
      }

      if (p.hasType(EnumType.Ghost)) {
         canSwitch = true;
         canFlee = true;
      }

      if (!canSwitch && p.getHeldItem().getHeldItemType() == EnumHeldItems.shedShell) {
         canSwitch = true;
      }

      if (!canFlee && (p.getHeldItem().getHeldItemType() == EnumHeldItems.smokeBall || p.getBattleAbility() instanceof RunAway)) {
         canFlee = true;
      }

      return new boolean[]{canSwitch, canFlee};
   }

   public PartyStorage getStorage() {
      return null;
   }

   public BattleParticipant[] getParticipantList() {
      return new BattleParticipant[]{this};
   }

   public ArrayList getOpponents() {
      return this.bc.getOpponents(this);
   }

   public ArrayList getAllies() {
      return this.bc.getTeam(this);
   }

   public ArrayList getOpponentPokemon() {
      return this.bc.getOpponentPokemon(this);
   }

   public ArrayList getTeamPokemon() {
      return this.bc.getTeamPokemon(this);
   }

   public ArrayList getActiveUnfaintedPokemon() {
      ArrayList unfaintedPokemon = new ArrayList(this.controlledPokemon.size());
      Iterator var2 = this.controlledPokemon.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         if (!pw.isFainted()) {
            unfaintedPokemon.add(pw);
         }
      }

      return unfaintedPokemon;
   }

   public PixelmonWrapper getPokemonFromUUID(UUID uuid) {
      Iterator var2 = this.controlledPokemon.iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(!uuid.equals(pw.getPokemonUUID()));

      return pw;
   }

   public int getPartyPosition(PixelmonWrapper pokemon) {
      for(int i = 0; i < this.allPokemon.length; ++i) {
         if (pokemon == this.allPokemon[i]) {
            return i;
         }
      }

      return -1;
   }

   protected void loadParty(PartyStorage party) {
      List team = party.getTeam();
      this.loadParty(team);
   }

   protected void loadParty(List party) {
      this.allPokemon = new PixelmonWrapper[party.size()];

      for(int i = 0; i < this.allPokemon.length; ++i) {
         this.allPokemon[i] = new PixelmonWrapper(this, (Pokemon)party.get(i), i);
      }

   }

   protected void loadSingle(Pokemon pokemon) {
      this.allPokemon = new PixelmonWrapper[]{new PixelmonWrapper(this, pokemon, 0)};
   }

   public PixelmonWrapper getPokemonFromParty(UUID uuid) {
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper pw = var2[var4];
         if (pw.getPokemonUUID().equals(uuid)) {
            return pw;
         }
      }

      return null;
   }

   protected PixelmonWrapper getPokemonFromParty(EntityPixelmon entity) {
      PixelmonWrapper pw = this.getPokemonFromParty(entity.func_110124_au());
      if (entity.getFormEnum().isTemporary()) {
         entity.setForm(entity.getFormEnum().getDefaultFromTemporary(entity.getPokemonData()));
      }

      if (pw == null) {
         return new PixelmonWrapper(this, entity, 0);
      } else {
         pw.entity = entity;
         return pw;
      }
   }

   public World getWorld() {
      return this.getEntity().field_70170_p;
   }

   public boolean addSwitchingOut(PixelmonWrapper pw) {
      if (this.hasMorePokemonReserve()) {
         this.switchingOut.add(pw);
         return true;
      } else {
         return false;
      }
   }

   public void switchAllFainted() {
      Iterator var1 = this.switchingOut.iterator();

      while(var1.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var1.next();
         pw.doSwitch();
      }

      this.switchingOut.clear();
      this.switchingIn.clear();
   }

   public PixelmonWrapper getRandomPartyPokemon() {
      List choices = new ArrayList();
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper pw = var2[var4];
         if (pw.entity == null && !pw.isFainted()) {
            choices.add(pw);
         }
      }

      return (PixelmonWrapper)RandomHelper.getRandomElementFromList(choices);
   }

   public boolean canMegaEvolve() {
      return true;
   }

   public boolean canDynamax() {
      return true;
   }

   public BattleAIBase getBattleAI() {
      return this.battleAI;
   }

   public void setBattleAI(BattleAIBase ai) {
      SetBattleAIEvent event = new SetBattleAIEvent(this.bc, this, ai);
      Pixelmon.EVENT_BUS.post(event);
      this.battleAI = event.getAI();
   }

   public void sendMessage(IMessage message) {
   }

   public void onEndTurn(BattleControllerBase bc) {
   }

   public boolean onTakeTurn(BattleControllerBase bc, PixelmonWrapper pw) {
      return false;
   }

   public boolean onUseAttack(BattleControllerBase bc, PixelmonWrapper pw) {
      return false;
   }

   public void onUseAttackPost(BattleControllerBase bc, PixelmonWrapper pw) {
   }

   public boolean onAddStatus(BattleControllerBase bc, PixelmonWrapper user, PixelmonWrapper target, StatusBase status) {
      return false;
   }

   public float onHit(PixelmonWrapper source, float damage, DamageTypeEnum damageType) {
      return damage;
   }

   public void onOpponentKO(BattleControllerBase bc, PixelmonWrapper pw) {
   }

   public boolean onTargeted(PixelmonWrapper user, Attack attack) {
      return false;
   }

   public boolean onUseAttackOther(BattleControllerBase bc, Attack attack, BattleParticipant bp, PixelmonWrapper user) {
      return false;
   }

   public void onSwitchIn(BattleControllerBase bc, PixelmonWrapper pw) {
   }
}
