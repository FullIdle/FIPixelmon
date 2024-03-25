package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleAIMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

public class TrainerParticipant extends BattleParticipant {
   public NPCTrainer trainer;

   public TrainerParticipant(NPCTrainer trainer, EntityPlayer opponent, int numPokemon) throws IllegalStateException {
      super(numPokemon);
      this.setTrainer(trainer, opponent);
   }

   public TrainerParticipant(NPCTrainer trainer, EntityPlayer opponent, int numPokemon, List teamSelection) throws IllegalStateException {
      super(numPokemon);
      this.setTrainer(trainer, opponent);
      this.loadParty(teamSelection);
   }

   private void setTrainer(NPCTrainer trainer, EntityPlayer opponent) throws IllegalStateException {
      if (trainer.canStartBattle(opponent, true)) {
         this.trainer = trainer;
         this.loadParty(trainer.getPokemonStorage());
      } else {
         throw new IllegalStateException("NPC Trainer already battled: " + trainer.func_70005_c_());
      }
   }

   public TrainerParticipant(NPCTrainer trainer, int numPokemon) throws IllegalStateException {
      super(numPokemon);
      if (trainer.battleController != null) {
         throw new IllegalStateException("NPC Trainer already battled: " + trainer.func_70005_c_());
      } else {
         this.trainer = trainer;
         this.loadParty(trainer.getPokemonStorage());
      }
   }

   public void startBattle(BattleControllerBase bc) {
      this.bc = bc;
      this.trainer.setBattleController(bc);
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      int i;
      for(i = 0; i < var3; ++i) {
         PixelmonWrapper pw = var2[i];
         pw.bc = bc;
      }

      PixelmonWrapper pw;
      int var15;
      if (this.trainer.getBossMode() != EnumBossMode.NotBoss) {
         int lvl = 1;
         Iterator var9 = bc.participants.iterator();

         while(var9.hasNext()) {
            BattleParticipant p = (BattleParticipant)var9.next();
            if (p.team != this.team && p instanceof PlayerParticipant) {
               lvl = Math.max(lvl, ((PlayerParticipant)p).getHighestLevel());
            }
         }

         lvl += this.trainer.getBossMode().getExtraLevels();
         PixelmonWrapper[] var11 = this.allPokemon;
         i = var11.length;

         for(var15 = 0; var15 < i; ++var15) {
            pw = var11[var15];
            pw.bc = bc;
            pw.setTempLevel(lvl);
         }
      }

      EntityPixelmon[] released = this.releasePokemon();
      ArrayList realReleased = new ArrayList();
      EntityPixelmon[] var14 = released;
      var15 = released.length;

      int var16;
      for(var16 = 0; var16 < var15; ++var16) {
         EntityPixelmon aReleased = var14[var16];
         if (aReleased != null) {
            realReleased.add(aReleased);
         }
      }

      for(i = 0; i < realReleased.size(); ++i) {
         EntityPixelmon p = (EntityPixelmon)realReleased.get(i);
         pw = this.getPokemonFromParty(p);
         this.controlledPokemon.add(pw);
      }

      super.startBattle(bc);
      PixelmonWrapper[] var17 = this.allPokemon;
      var15 = var17.length;

      for(var16 = 0; var16 < var15; ++var16) {
         PixelmonWrapper pw = var17[var16];
         pw.setHealth(pw.getMaxHealth());
         pw.enableReturnHeldItem();
      }

      EnumBattleAIMode battleAIMode = this.trainer.getBattleAIMode();
      if (battleAIMode == EnumBattleAIMode.Default) {
         battleAIMode = PixelmonConfig.battleAITrainer;
      }

      this.setBattleAI(battleAIMode.createAI(this));
      this.trainer.startBattle((BattleParticipant)bc.getOpponents(this).get(0));
   }

   private EntityPixelmon[] releasePokemon() {
      EntityPixelmon[] pokemon = new EntityPixelmon[this.numControlledPokemon];

      for(int i = 0; i < this.numControlledPokemon; ++i) {
         if (this.allPokemon.length > i) {
            pokemon[i] = this.trainer.releasePokemon(this.allPokemon[i].getPokemonUUID());
            if (!this.bc.isRaid()) {
               this.allPokemon[i].battlePosition = i;
            }
         }
      }

      return pokemon;
   }

   public ParticipantType getType() {
      return ParticipantType.Trainer;
   }

   public boolean hasMorePokemonReserve() {
      return this.countAblePokemon() > this.getActiveUnfaintedPokemon().size() + this.switchingOut.size();
   }

   public boolean canGainXP() {
      return false;
   }

   public void endBattle(EnumBattleEndCause cause) {
      ArrayList opponents = (ArrayList)this.bc.participants.stream().filter((p) -> {
         return p.team != this.team;
      }).collect(Collectors.toList());
      if (this.trainer.battleController != null) {
         if (!this.hasMorePokemon() && opponents.stream().anyMatch((p) -> {
            return p.hasMorePokemon();
         })) {
            this.trainer.loseBattle(opponents);
         } else {
            this.trainer.winBattle(opponents);
         }

         if (!this.trainer.canEngage) {
            this.trainer.func_70106_y();
            this.trainer.unloadEntity();
         }
      }

      Iterator var3 = this.controlledPokemon.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         pw.resetOnSwitch();
         pw.getBattleStats().clearBattleStats();
         pw.clearStatus();
         pw.resetBattleEvolution();
         if (pw.entity != null) {
            pw.entity.onEndBattle();
            pw.entity.func_70106_y();
            pw.entity.retrieve();
         }
      }

      PixelmonWrapper[] var7 = this.allPokemon;
      int var8 = var7.length;

      for(int var5 = 0; var5 < var8; ++var5) {
         PixelmonWrapper pw = var7[var5];
         pw.resetBattleEvolution();
         pw.writeToNBT();
      }

      this.trainer.setAttackTargetPix((EntityLivingBase)null);
      this.trainer.healAllPokemon();
      this.trainer.restoreAllFriendship();
      this.trainer.setBattleController((BattleControllerBase)null);
   }

   public void getNextPokemon(int position) {
      Iterator var2 = this.controlledPokemon.iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            return;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(pw.battlePosition != position);

      this.bc.switchPokemon(pw.getPokemonUUID(), this.getBattleAI().getNextSwitch(pw), true);
   }

   public UUID getNextPokemonUUID() {
      return this.trainer.getNextPokemonUUID();
   }

   private String getTranslatedName() {
      String loc = null;
      if (this.bc != null) {
         Iterator var2 = this.bc.getOpponents(this).iterator();

         while(var2.hasNext()) {
            BattleParticipant p = (BattleParticipant)var2.next();
            if (p instanceof PlayerParticipant) {
               loc = ((EntityPlayerMP)p.getEntity()).field_71148_cg;
            }
         }
      }

      return loc == null ? this.trainer.func_70005_c_() : this.trainer.getName(loc);
   }

   public TextComponentBase getName() {
      return new TextComponentString(this.getTranslatedName());
   }

   public MoveChoice getMove(PixelmonWrapper p) {
      return this.getBattleAI().getNextMove(p);
   }

   public PixelmonWrapper switchPokemon(PixelmonWrapper oldPokemon, UUID newPixelmonUUID) {
      int index = oldPokemon.getControlledIndex();
      if (index == -1 && this.bc.simulateMode) {
         index = 0;
      }

      String beforeName = oldPokemon.getNickname();
      PixelmonWrapper newWrapper = this.getPokemonFromParty(newPixelmonUUID);
      oldPokemon.beforeSwitch(newWrapper);
      if (!oldPokemon.isFainted() && !oldPokemon.nextSwitchIsMove) {
         this.bc.sendToOthers("playerparticipant.withdrew", this, this.getTranslatedName(), beforeName);
      }

      if (!this.bc.simulateMode) {
         oldPokemon.entity.retrieve();
         oldPokemon.entity = null;
         EntityPixelmon pixelmon = this.trainer.releasePokemon(newPixelmonUUID);
         newWrapper.entity = pixelmon;
      }

      if (this.trainer.getBossMode() != EnumBossMode.NotBoss) {
         int lvl = 1;
         Iterator var7 = this.bc.participants.iterator();

         while(var7.hasNext()) {
            BattleParticipant p = (BattleParticipant)var7.next();
            if (p.team != this.team && p instanceof PlayerParticipant) {
               lvl = Math.max(lvl, ((PlayerParticipant)p).getHighestLevel());
            }
         }

         lvl += this.trainer.getBossMode().getExtraLevels();
         newWrapper.setLevelNum(lvl);
      }

      this.controlledPokemon.set(index, newWrapper);
      newWrapper.getBattleAbility().beforeSwitch(newWrapper);
      if (!this.bc.simulateMode) {
         this.bc.sendToOthers("pixelmon.battletext.sentout", this, this.getTranslatedName(), newWrapper.getNickname());
         this.bc.participants.forEach(BattleParticipant::updateOtherPokemon);
      }

      return newWrapper;
   }

   public boolean checkPokemon() {
      Iterator var1 = this.trainer.getPokemonStorage().getTeam().iterator();

      Pokemon pokemon;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         pokemon = (Pokemon)var1.next();
      } while(pokemon.getMoveset().size() != 0);

      if (PixelmonConfig.printErrors) {
         Pixelmon.LOGGER.info("Couldn't load Pokémon's moves.");
      }

      return false;
   }

   public boolean canMegaEvolve() {
      if (this.bc != null && this.bc.isRaid()) {
         return false;
      } else {
         return this.dynamax == null && this.trainer.getMegaItem().canMega();
      }
   }

   public boolean canDynamax() {
      if (this.bc != null && this.bc.isRaid()) {
         BattleParticipant bp = (BattleParticipant)this.getOpponents().get(0);
         if (bp instanceof RaidPixelmonParticipant) {
            RaidPixelmonParticipant rpp = (RaidPixelmonParticipant)bp;
            return rpp.canDynamax(this);
         }
      }

      return this.evolution == null && (this.trainer.getMegaItem().canDynamax() || this.trainer.isGymLeader);
   }

   public void updatePokemon(PixelmonWrapper p) {
   }

   public EntityLiving getEntity() {
      return this.trainer.getEntity();
   }

   public void updateOtherPokemon() {
   }

   public String getDisplayName() {
      return this.getTranslatedName();
   }

   public TrainerPartyStorage getStorage() {
      return this.trainer.getPokemonStorage();
   }
}
