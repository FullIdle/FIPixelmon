package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TerrainExamine;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.ai.RandomAI;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import com.pixelmonmod.pixelmon.battles.tasks.HPUpdateTask;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BackToMainMenu;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ExitBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetAllBattlingPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetBattlingPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetPokemonBattleData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetPokemonTeamData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.StartBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.music.PlayBattleMusic;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurmy;
import com.pixelmonmod.pixelmon.items.ItemShrineOrb;
import com.pixelmonmod.pixelmon.sounds.BattleMusicType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.AirSaver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerParticipant extends BattleParticipant {
   public EntityPlayerMP player;
   public PlayerPartyStorage party;
   long guiCheck = 0L;
   public boolean hasAmuletCoin = false;
   public boolean hasHappyHour = false;
   public int payDay = 0;
   private AirSaver airSaver;

   public PlayerParticipant(EntityPlayerMP p, EntityPixelmon... startingPixelmon) {
      super(startingPixelmon.length);
      this.player = p;
      if (this.bc != null && this.bc.rules != null && this.bc.rules.battleType != null && this.bc.rules.battleType == EnumBattleType.Raid) {
         this.loadSingle(startingPixelmon[0].getPokemonData());
      } else {
         this.loadParty(this.getStorage());
      }

      this.initialize(p, startingPixelmon);
   }

   public PlayerParticipant(EntityPlayerMP p, List teamSelection, int numControlledPokemon) {
      super(numControlledPokemon);
      this.loadParty(teamSelection);
      EntityPixelmon[] startingPixelmon = new EntityPixelmon[numControlledPokemon];

      for(int i = 0; i < numControlledPokemon; ++i) {
         if (teamSelection.size() > i) {
            startingPixelmon[i] = ((Pokemon)teamSelection.get(i)).getOrSpawnPixelmon(p);
         }
      }

      this.initialize(p, startingPixelmon);
   }

   public PlayerParticipant(boolean raid, EntityPlayerMP p, EntityPixelmon... startingPixelmon) {
      super(startingPixelmon.length);
      this.player = p;
      if (raid) {
         this.loadSingle(startingPixelmon[0].getPokemonData());
      } else {
         this.loadParty(this.getStorage());
      }

      this.initialize(p, startingPixelmon);
   }

   private void initialize(EntityPlayerMP p, EntityPixelmon... startingPixelmon) {
      this.player = p;
      this.party = this.getStorage();
      int positionIndex = 0;
      EntityPixelmon[] var4 = startingPixelmon;
      int var5 = startingPixelmon.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPixelmon pokemon = var4[var6];
         if (pokemon != null) {
            PixelmonWrapper pw = this.getPokemonFromParty(pokemon);
            if (pw != null) {
               this.controlledPokemon.add(pw);
               pw.battlePosition = positionIndex++;
            }
         }
      }

      this.airSaver = new AirSaver(p);
      p.func_70637_d(false);
      p.field_191988_bg = 0.0F;
      p.field_70702_br = 0.0F;
      p.func_70095_a(false);
   }

   private void enableReturnHeldItems() {
      PixelmonWrapper[] var1 = this.allPokemon;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PixelmonWrapper pw = var1[var3];
         pw.enableReturnHeldItem();
      }

   }

   public ParticipantType getType() {
      return ParticipantType.Player;
   }

   public boolean canGainXP() {
      return true;
   }

   public boolean hasMorePokemonReserve() {
      return this.countAblePokemon() > this.getActiveUnfaintedPokemon().size() + this.switchingOut.size();
   }

   public void startBattle(BattleControllerBase bc) {
      super.startBattle(bc);
      this.setBattleAI(new RandomAI(this));
      this.player.field_71075_bZ.field_75102_a = true;
      boolean useTurnTime = false;
      int activateTime = 0;
      int turnTime = 0;
      if (bc.rules.turnTime > 0) {
         useTurnTime = true;
         activateTime = bc.rules.turnTime;
         turnTime = bc.rules.turnTime;
      } else if (PixelmonServerConfig.afkHandlerOn) {
         useTurnTime = true;
         activateTime = PixelmonServerConfig.afkTimerActivateSeconds;
         turnTime = PixelmonConfig.afkTimerTurnSeconds;
      }

      ParticipantType[][] battleType = bc.getBattleType(this);
      int catchCombo = 0;
      if (this.getOpponents().stream().allMatch((it) -> {
         return it instanceof WildPixelmonParticipant;
      }) && this.getOpponentPokemon().stream().allMatch((it) -> {
         return it.getSpecies() == this.party.transientData.captureCombo.getCurrentSpecies();
      })) {
         catchCombo = this.party.transientData.captureCombo.getCurrentCombo();
      }

      StartBattle startPacket;
      if (useTurnTime) {
         startPacket = new StartBattle(bc.battleIndex, battleType, activateTime, turnTime, catchCombo, bc.rules);
      } else {
         startPacket = new StartBattle(bc.battleIndex, battleType, -1, -1, catchCombo, bc.rules);
      }

      Pixelmon.network.sendTo(startPacket, this.player);
      this.party.pokedex.update();
      if (bc.rules.fullHeal || PixelmonConfig.returnHeldItems && bc.isPvP()) {
         this.enableReturnHeldItems();
      }

      Pixelmon.network.sendTo(new SetAllBattlingPokemon(PixelmonInGui.convertToGUI(Arrays.asList(this.allPokemon))), this.player);
      Pixelmon.network.sendTo(new SetBattlingPokemon(this.getTeamPokemonList()), this.player);
      Pixelmon.storageManager.getParty(this.player).getTeam().forEach((pokemon) -> {
         pokemon.ifEntityExists(EntityPixelmon::retrieve);
      });

      PixelmonWrapper pw;
      for(Iterator var8 = this.controlledPokemon.iterator(); var8.hasNext(); pw.entity.battleController = bc) {
         pw = (PixelmonWrapper)var8.next();
         if (pw.pokemon.getPixelmonIfExists() == null) {
            pw.entity.func_70012_b(this.player.field_70165_t, this.player.field_70163_u, this.player.field_70161_v, this.player.field_70177_z, 0.0F);
            pw.entity.releaseFromPokeball();
         }

         pw.entity.field_70128_L = false;
      }

   }

   public ArrayList getTeamPokemonList() {
      ArrayList team = this.bc.getTeam(this);
      ArrayList teamPokemon = new ArrayList();
      Iterator var3 = team.iterator();

      while(var3.hasNext()) {
         BattleParticipant p = (BattleParticipant)var3.next();
         teamPokemon.addAll(p.controlledPokemon);
      }

      return teamPokemon;
   }

   public void endBattle(EnumBattleEndCause cause) {
      if (!this.player.field_71075_bZ.field_75098_d) {
         this.player.field_71075_bZ.field_75102_a = false;
      }

      EnumBurmy burmyForm = null;
      PixelmonWrapper[] var3 = this.allPokemon;
      int var4 = var3.length;

      int var5;
      PixelmonWrapper pw;
      for(var5 = 0; var5 < var4; ++var5) {
         pw = var3[var5];
         if (pw.changeBurmy && !this.controlledPokemon.isEmpty()) {
            if (burmyForm == null) {
               burmyForm = EnumBurmy.getFromType(TerrainExamine.getTerrain((PixelmonWrapper)this.controlledPokemon.get(0)));
            }

            pw.pokemon.setForm(burmyForm);
         }
      }

      Iterator var7 = this.controlledPokemon.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var7.next();
         pw.resetOnSwitch();
         pw.resetBattleEvolution();
         if (pw.entity != null) {
            pw.entity.onEndBattle();
            pw.entity.retrieve();
         }

         if (cause == EnumBattleEndCause.FLEE || cause == EnumBattleEndCause.FORFEIT || cause == EnumBattleEndCause.FORCE) {
            this.resetPayDay();
         }
      }

      var3 = this.allPokemon;
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         pw = var3[var5];
         pw.resetBattleEvolution();
         pw.writeToNBT();
      }

      if (this.payDay > 0 && this.hasMorePokemon()) {
         this.payDay *= this.getPrizeMoneyMultiplier();
         Pixelmon.moneyManager.getBankAccount(this.player).ifPresent((account) -> {
            account.changeMoney(this.payDay);
            ChatHandler.sendBattleMessage((Entity)this.getEntity(), "pixelmon.effect.paydayend", this.getName(), this.payDay);
         });
      }

      if (cause == EnumBattleEndCause.FLEE && this.getOpponents().stream().allMatch((it) -> {
         return it instanceof WildPixelmonParticipant;
      }) && this.bc.getOpponentPokemon((BattleParticipant)this).stream().anyMatch((it) -> {
         return it.willTryFlee;
      })) {
         this.party.transientData.captureCombo.clearCombo();
      }

      Pixelmon.network.sendTo(new ExitBattle(), this.player);
      if (this.bc != null && this.bc.participants.size() == 2 && this.bc.otherParticipant(this) instanceof PlayerParticipant) {
         if (!this.hasMorePokemon()) {
            this.party.stats.addLoss();
         } else {
            this.party.stats.addWin();
         }
      }

   }

   public void getNextPokemon(int position) {
      if (position < this.controlledPokemon.size() && !this.bc.battleEnded) {
         boolean switching = false;
         PixelmonWrapper switchingPokemon = (PixelmonWrapper)this.controlledPokemon.get(position);
         switchingPokemon.isSwitching = true;
         switching = true;
         switchingPokemon.wait = true;
         if (switchingPokemon.newPokemonUUID == null) {
            Pixelmon.network.sendTo(new EnforcedSwitchTask(position), this.player);
         }

         if (!switching) {
            this.wait = false;
         }

      } else {
         this.wait = false;
      }
   }

   public int getPrizeMoneyMultiplier() {
      int multiplier = 1;
      if (this.hasAmuletCoin) {
         multiplier *= 2;
      }

      if (this.hasHappyHour) {
         multiplier *= 2;
      }

      return multiplier;
   }

   public UUID getNextPokemonUUID() {
      Pokemon first = this.party.findOne((pokemon) -> {
         return pokemon.getHealth() > 0 && !pokemon.isEgg() && pokemon.getPixelmonIfExists() == null;
      });
      return first == null ? null : first.getUUID();
   }

   public TextComponentBase getName() {
      return new TextComponentString(this.player.func_145748_c_().func_150260_c());
   }

   public void selectAction() {
      this.getMove((PixelmonWrapper)null);
   }

   public MoveChoice getMove(PixelmonWrapper pokemon) {
      if (this.bc == null) {
         return null;
      } else {
         boolean canSwitch = true;
         boolean canFlee = true;
         ArrayList pokemonToChoose = new ArrayList();
         Iterator var5 = this.controlledPokemon.iterator();

         while(true) {
            while(var5.hasNext()) {
               PixelmonWrapper p = (PixelmonWrapper)var5.next();
               if (p.getMoveset().isEmpty()) {
                  this.bc.endBattle(EnumBattleEndCause.FORCE);
                  return null;
               }

               boolean[] canExit = canSwitch(p);
               canSwitch = canExit[0];
               canFlee = canExit[1];
               if (p.attack != null && p.attack.doesPersist(p)) {
                  p.wait = false;
               } else {
                  pokemonToChoose.add(p);
                  p.wait = true;
               }
            }

            if (!pokemonToChoose.isEmpty()) {
               Pixelmon.network.sendTo(new BackToMainMenu(canSwitch, canFlee, pokemonToChoose), this.player);
               ArrayList playerSpectators = this.bc.getPlayerSpectators(this);
               playerSpectators.forEach((spectator) -> {
                  spectator.sendMessage(new BackToMainMenu(true, true, pokemonToChoose));
               });
            }

            return null;
         }
      }
   }

   public PixelmonWrapper switchPokemon(PixelmonWrapper pw, UUID newPixelmonUUID) {
      double x = this.player.field_70165_t;
      double y = this.player.field_70163_u;
      double z = this.player.field_70161_v;
      String beforeName = pw.getNickname();
      PixelmonWrapper newWrapper = this.getPokemonFromParty(newPixelmonUUID);
      pw.beforeSwitch(newWrapper);
      if (!pw.isFainted() && !pw.nextSwitchIsMove) {
         ChatHandler.sendBattleMessage((Entity)this.player, "playerparticipant.enough", pw.getNickname());
         this.bc.sendToOthers("playerparticipant.withdrew", this, this.player.func_145748_c_().func_150260_c(), beforeName);
      }

      if (!this.bc.simulateMode) {
         pw.entity.retrieve();
         pw.entity = null;
         EntityPixelmon pixelmon = this.party.find(newPixelmonUUID).getOrSpawnPixelmon(this.player);
         pixelmon.field_70159_w = pixelmon.field_70181_x = pixelmon.field_70179_y = 0.0;
         pixelmon.func_70012_b(x, y, z, this.player.field_70177_z, 0.0F);
         newWrapper.entity = pixelmon;
      }

      newWrapper.battlePosition = pw.battlePosition;
      newWrapper.getBattleAbility().beforeSwitch(newWrapper);
      String newNickname = newWrapper.getNickname();
      ChatHandler.sendBattleMessage((Entity)this.player, "playerparticipant.go", new TextComponentTranslation("ribbon." + newWrapper.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{newNickname}));
      this.bc.sendToOthers("battlecontroller.sendout", this, this.player.func_145748_c_().func_150260_c(), new TextComponentTranslation("ribbon." + newWrapper.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{newWrapper.getNickname()}));
      int index = this.controlledPokemon.indexOf(pw);
      this.controlledPokemon.set(index, newWrapper);
      this.bc.participants.forEach(BattleParticipant::updateOtherPokemon);
      return newWrapper;
   }

   public boolean checkPokemon() {
      Iterator var1 = this.party.getTeam().iterator();

      Pokemon pokemon;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         pokemon = (Pokemon)var1.next();
      } while(!pokemon.getMoveset().isEmpty());

      ChatHandler.sendChat(this.player, "playerparticipant.load");
      return false;
   }

   public void updatePokemon(PixelmonWrapper pw) {
      pw.update(EnumUpdateType.HP);
   }

   public EntityLivingBase getEntity() {
      return this.player;
   }

   public void updateOtherPokemon() {
      if (this.bc == null) {
         this.endBattle(EnumBattleEndCause.FORCE);
      } else if (!this.bc.simulateMode) {
         ArrayList opponents = this.bc.getOpponentPokemon((BattleParticipant)this);

         for(int i = 0; i < opponents.size(); ++i) {
            if (opponents.get(i) == null) {
               opponents.remove(i);
               --i;
            }
         }

         ArrayList playerSpectators;
         ArrayList teamPokemon;
         if (this.bc.battleTurn == -1) {
            this.updateOpponentPokemon();
            if (this.bc.getTeam(this).size() > 1) {
               teamPokemon = this.getAllyData();
               Pixelmon.network.sendTo(new SetPokemonTeamData(teamPokemon), this.player);
               playerSpectators = this.bc.getPlayerSpectators(this);
               playerSpectators.forEach((spectator) -> {
                  spectator.sendMessage(new SetPokemonTeamData(teamPokemon));
               });
            }
         }

         if (!this.bc.battleEnded) {
            teamPokemon = this.getTeamPokemonList();
            Pixelmon.network.sendTo(new SetBattlingPokemon(teamPokemon), this.player);
            playerSpectators = this.bc.getPlayerSpectators(this);
            playerSpectators.forEach((spectator) -> {
               spectator.sendMessage(new SetBattlingPokemon(teamPokemon));
            });
         }

      }
   }

   public ArrayList getAllyData() {
      ArrayList team = this.bc.getTeam(this);
      ArrayList otherTeamPokemon = new ArrayList(6);
      team.stream().filter((p) -> {
         return p != this;
      }).forEach((p) -> {
         otherTeamPokemon.addAll(p.controlledPokemon);
      });
      return otherTeamPokemon;
   }

   public PixelmonInGui[] getOpponentData() {
      ArrayList opponents = this.bc.getOpponentPokemon((BattleParticipant)this);
      return PixelmonInGui.convertToGUI(opponents);
   }

   public void updateOpponentPokemon() {
      PixelmonInGui[] data = this.getOpponentData();
      Pixelmon.network.sendTo(new SetPokemonBattleData(data), this.player);
      ArrayList playerSpectators = this.bc.getPlayerSpectators(this);
      playerSpectators.forEach((spectator) -> {
         spectator.sendMessage(new SetPokemonBattleData(data));
      });
   }

   public void updatePokemonHealth() {
      this.updateOtherPokemon();
   }

   public void checkPlayerItems() {
      for(int i = 0; i < this.player.field_71071_by.field_70462_a.size(); ++i) {
         if (this.player.field_71071_by.field_70462_a.get(i) != null && ((ItemStack)this.player.field_71071_by.field_70462_a.get(i)).func_77973_b() instanceof ItemShrineOrb) {
            ItemStack item = (ItemStack)this.player.field_71071_by.field_70462_a.get(i);
            int dmg = item.func_77952_i();
            if (dmg < ItemShrineOrb.full) {
               item.func_77964_b(dmg + 1);
               break;
            }
         }
      }

   }

   public int getHighestLevel() {
      int lvl = -1;
      PixelmonWrapper[] var2 = this.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper pw = var2[var4];
         lvl = Math.max(lvl, pw.getLevelNum());
      }

      return lvl;
   }

   public void tick() {
      super.tick();
      this.airSaver.tick();
      if (this.party != null && !this.party.guiOpened) {
         this.openGui();
      }

      if (this.player.func_110143_aJ() <= 0.0F) {
         this.bc.endBattle(EnumBattleEndCause.FORCE);
         BattleRegistry.deRegisterBattle(this.bc);
      }

   }

   public void givePlayerExp(PixelmonWrapper pixelmon) {
      int opponentPixelmonLevel = pixelmon.getLevelNum();
      int expAmount = false;
      int divisor = 5;
      int expAmount;
      if (opponentPixelmonLevel >= 75) {
         expAmount = opponentPixelmonLevel / (divisor * 5);
         this.player.func_71023_q(expAmount);
      } else if (opponentPixelmonLevel >= 50) {
         expAmount = opponentPixelmonLevel / (divisor * 4);
         this.player.func_71023_q(expAmount);
      } else if (opponentPixelmonLevel >= 35) {
         expAmount = opponentPixelmonLevel / (divisor * 2);
         this.player.func_71023_q(expAmount);
      } else if (opponentPixelmonLevel > divisor) {
         expAmount = opponentPixelmonLevel / divisor;
         this.player.func_71023_q(expAmount);
      } else {
         this.player.func_71023_q(1);
      }

   }

   public void openGui() {
      if (this.guiCheck == 0L || System.currentTimeMillis() - this.guiCheck > 1000L) {
         OpenScreen.open(this.player, EnumGuiScreen.Battle, this.bc.battleIndex);
         this.guiCheck = System.currentTimeMillis();
         Pixelmon.network.sendTo(new PlayBattleMusic(this.getBattleMusicType(), -1, 0L, true), this.player);
      }

   }

   public BattleMusicType getBattleMusicType() {
      if (this.getOpponents().stream().anyMatch((p) -> {
         return p.getType() == ParticipantType.RaidPokemon;
      })) {
         return BattleMusicType.RAID;
      } else if (this.getOpponents().stream().anyMatch((p) -> {
         return p.getType() == ParticipantType.Player;
      })) {
         return BattleMusicType.PLAYER;
      } else if (this.getOpponents().stream().anyMatch((p) -> {
         return p.getType() == ParticipantType.Trainer;
      })) {
         return this.getOpponents().stream().filter((p) -> {
            return p.getType() == ParticipantType.Trainer && p instanceof TrainerParticipant;
         }).map((p) -> {
            return (TrainerParticipant)p;
         }).anyMatch((p) -> {
            return p.trainer != null && p.trainer.isGymLeader;
         }) ? BattleMusicType.GYM : BattleMusicType.TRAINER;
      } else if (this.getOpponents().stream().anyMatch((p) -> {
         return p.getType() == ParticipantType.WildPokemon;
      })) {
         if (this.getOpponents().stream().filter((p) -> {
            return p.getType() == ParticipantType.WildPokemon && p instanceof WildPixelmonParticipant;
         }).map(BattleParticipant::getEntity).anyMatch((p) -> {
            return p instanceof EntityPixelmon && ((EntityPixelmon)p).isBossPokemon();
         })) {
            return BattleMusicType.BOSS;
         } else {
            return this.getOpponents().stream().filter((p) -> {
               return p.getType() == ParticipantType.WildPokemon && p instanceof WildPixelmonParticipant;
            }).map(BattleParticipant::getEntity).anyMatch((p) -> {
               return p instanceof EntityPixelmon && ((EntityPixelmon)p).isLegendary();
            }) ? BattleMusicType.LEGENDARY : BattleMusicType.WILD;
         }
      } else {
         return BattleMusicType.WILD;
      }
   }

   public void setPosition(double[] ds) {
      this.player.field_71135_a.func_147364_a(ds[0], ds[1], ds[2], this.player.field_70177_z, this.player.field_70125_A);
   }

   public void sendDamagePacket(PixelmonWrapper user, int damage) {
      Pixelmon.network.sendTo(new HPUpdateTask(user, -damage), this.player);
   }

   public void sendHealPacket(PixelmonWrapper target, int amount) {
      Pixelmon.network.sendTo(new HPUpdateTask(target, amount), this.player);
   }

   public String getDisplayName() {
      return this.player.getDisplayNameString();
   }

   public PlayerPartyStorage getStorage() {
      return Pixelmon.storageManager.getParty(this.player);
   }

   public boolean canMegaEvolve() {
      return this.bc != null && this.bc.isRaid() ? false : this.getStorage().getMegaItem().canMega();
   }

   public boolean canDynamax() {
      if (this.bc != null && this.bc.isRaid()) {
         BattleParticipant bp = (BattleParticipant)this.getOpponents().get(0);
         if (bp instanceof RaidPixelmonParticipant) {
            RaidPixelmonParticipant rpp = (RaidPixelmonParticipant)bp;
            return rpp.canDynamax(this);
         }
      }

      return this.getStorage().getMegaItem().canDynamax();
   }

   public void sendMessage(IMessage message) {
      Pixelmon.network.sendTo(message, this.player);
   }

   public void resetPayDay() {
      this.payDay = 0;
   }
}
