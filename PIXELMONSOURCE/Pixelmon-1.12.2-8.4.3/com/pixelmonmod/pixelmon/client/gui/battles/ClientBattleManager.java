package com.pixelmonmod.pixelmon.client.gui.battles;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.EnumTriBoolean;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.tasks.BattleMessageTask;
import com.pixelmonmod.pixelmon.battles.tasks.IBattleMessage;
import com.pixelmonmod.pixelmon.battles.tasks.IBattleTask;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.CameraMode;
import com.pixelmonmod.pixelmon.client.camera.CameraTargetEntity;
import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.camera.ICameraTarget;
import com.pixelmonmod.pixelmon.client.gui.GuiEvolve;
import com.pixelmonmod.pixelmon.client.gui.battles.tasks.AFKTask;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ChooseAttack;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.Flee;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ParticipantReady;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SwitchPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.BagSection;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemBattleItem;
import com.pixelmonmod.pixelmon.items.ItemData;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.ItemMedicine;
import com.pixelmonmod.pixelmon.items.ItemPPRestore;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.ItemStatusAilmentHealer;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryGinema;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryJuice;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryLeppa;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryRestoreHP;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryStatus;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMentalHerb;
import com.pixelmonmod.pixelmon.items.heldItems.ItemWhiteHerb;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.commons.lang3.tuple.Pair;

public class ClientBattleManager {
   public static final Timer TIMER = new Timer("ClientBattleTimer");
   public int battleControllerIndex = -1;
   public BattleMode mode;
   public BagSection bagSection;
   public boolean battleEnded = true;
   public boolean isSpectating = false;
   @Nonnull
   private EnumTriBoolean isProcessingTask;
   private final Deque battleTasks;
   private final List parallelTasks;
   public UUID[] teamPokemon;
   public PixelmonInGui[] displayedEnemyPokemon;
   public PixelmonInGui[] displayedOurPokemon;
   public PixelmonInGui[] displayedAllyPokemon;
   public List fullOurPokemon;
   public ArrayList bagStore;
   public ArrayList levelUpList;
   public ArrayList newAttackList;
   public int startIndex;
   public ItemData itemToUse;
   public Pair lastItem;
   public IMessage sendPacket;
   public BattleMode oldMode;
   public BattleMode yesNoOrigin;
   public List evolveList;
   public boolean isHealing;
   public boolean enforcedFleeFailed;
   public int healAmount;
   public int selectedAttack;
   public int currentPokemon;
   public boolean[][] targetted;
   public boolean choosingPokemon;
   public int battleTurn;
   public StatusType weather;
   public StatusType terrain;
   public int catchCombo;
   public ArrayList selectedActions;
   public EnumBattleType battleType;
   public boolean afkOn;
   public boolean afkActive;
   public int afkActivate;
   public int afkTurn;
   public int afkTime;
   private AFKTask afkTask;
   public boolean waitingText;
   public boolean megaEvolving;
   public boolean dynamaxing;
   public UUID megaEvolution;
   public UUID ultraBurst;
   public UUID dynamax;
   public boolean hasUltraBurst;
   public boolean hasDynamaxed;
   public boolean dynamaxDisabled;
   public boolean gigantamax;
   public boolean showZMoves;
   public boolean usedZMove;
   public Quadstate oldGen;
   public BattleRules rules;
   public boolean canSwitch;
   public boolean canFlee;
   public ParticipantType[][] battleSetup;
   public boolean healFinished;
   int ticksSincePicked;
   public ArrayList pokemonToChoose;
   public UUID spectatingUUID;
   private EntityPlayer spectating;
   private int thirdP;

   public ClientBattleManager() {
      this.isProcessingTask = EnumTriBoolean.NULL;
      this.battleTasks = new ArrayDeque();
      this.parallelTasks = new ArrayList();
      this.bagStore = new ArrayList();
      this.levelUpList = new ArrayList(6);
      this.newAttackList = new ArrayList();
      this.startIndex = 0;
      this.evolveList = new ArrayList(6);
      this.isHealing = false;
      this.enforcedFleeFailed = false;
      this.healAmount = 0;
      this.selectedAttack = -1;
      this.currentPokemon = -1;
      this.targetted = new boolean[2][];
      this.selectedActions = new ArrayList();
      this.megaEvolution = null;
      this.ultraBurst = null;
      this.dynamax = null;
      this.hasUltraBurst = false;
      this.hasDynamaxed = false;
      this.dynamaxDisabled = false;
      this.gigantamax = false;
      this.showZMoves = false;
      this.usedZMove = false;
      this.oldGen = Quadstate.NO;
      this.rules = new BattleRules();
      this.canSwitch = true;
      this.canFlee = true;
      this.healFinished = false;
      this.ticksSincePicked = 0;
      this.thirdP = 0;
   }

   public boolean isBattling() {
      return this.battleControllerIndex != -1 && !this.battleEnded && !this.isSpectating;
   }

   public void addMessage(String s) {
      this.battleTasks.add(new BattleMessageTask(new TextComponentString(s)));
   }

   public void addMessage(String key, Object... arguments) {
      this.battleTasks.add(new BattleMessageTask(new TextComponentString(I18n.func_135052_a(key, arguments))));
   }

   @Nullable
   public IBattleTask getNextBattleTask() {
      return (IBattleTask)this.battleTasks.peek();
   }

   public void removeBattleTask() {
      this.battleTasks.poll();
      this.isProcessingTask = EnumTriBoolean.NULL;
      this.checkClearedMessages();
   }

   public boolean hasMoreMessages() {
      return this.battleTasks.stream().anyMatch((it) -> {
         return it instanceof IBattleMessage;
      });
   }

   public boolean hasMoreTasks() {
      return !this.battleTasks.isEmpty();
   }

   public void addBattleMessage(IBattleTask message) {
      this.battleTasks.add(message);
   }

   public void processBattleTasks() {
      this.parallelTasks.removeIf((it) -> {
         return !it.process(this);
      });
      if (this.isProcessingTask != EnumTriBoolean.FALSE) {
         this.isProcessingTask = EnumTriBoolean.TRUE;
         if (this.getNextBattleTask() != null) {
            IBattleTask task = this.getNextBattleTask();
            if (!task.shouldRunParallel()) {
               boolean shouldContinue = this.getNextBattleTask().process(this);
               boolean removed = false;
               if (!shouldContinue) {
                  this.isProcessingTask = EnumTriBoolean.FALSE;
                  if (!(this.getNextBattleTask() instanceof IBattleMessage)) {
                     this.removeBattleTask();
                     removed = true;
                  }
               }

               Iterator i = this.battleTasks.iterator();
               if (!removed) {
                  i.next();
               }

               while(i.hasNext()) {
                  IBattleTask next = (IBattleTask)i.next();
                  if (!next.shouldRunParallel()) {
                     break;
                  }

                  if (this.parallelTasks.stream().noneMatch((it) -> {
                     return Objects.equals(it.getPokemonID(), next.getPokemonID());
                  })) {
                     this.parallelTasks.add(next);
                     i.remove();
                  }
               }
            } else if (this.parallelTasks.stream().noneMatch((it) -> {
               return Objects.equals(it.getPokemonID(), task.getPokemonID());
            })) {
               this.parallelTasks.add(task);
               task.process(this);
               this.isProcessingTask = EnumTriBoolean.FALSE;
               if (!(this.getNextBattleTask() instanceof IBattleMessage)) {
                  this.removeBattleTask();
               }
            }
         }

      }
   }

   public boolean isProcessingTask() {
      return !this.battleTasks.isEmpty() && this.isProcessingTask == EnumTriBoolean.TRUE;
   }

   public void sendParticipantReady() {
      if (!this.isSpectating && !this.hasMoreMessages() && this.battleTasks.isEmpty()) {
         Pixelmon.network.sendToServer(new ParticipantReady(this.battleControllerIndex, Minecraft.func_71410_x().field_71439_g.func_110124_au().toString()));
      }

   }

   public void checkClearedMessages() {
      if (!this.hasMoreTasks() && this.parallelTasks.isEmpty() && (this.mode == BattleMode.Waiting || this.mode == BattleMode.MainMenu) && !this.choosingPokemon && !this.hasLevelUps()) {
         this.sendParticipantReady();
         if (this.afkOn) {
            this.resetAFKTime();
         }
      }

   }

   public PixelmonInGui getCurrentPokemon() {
      if (this.currentPokemon != -1) {
         if (this.battleControllerIndex == -1 && ClientStorageManager.party.get(this.currentPokemon) != null) {
            return new PixelmonInGui(ClientStorageManager.party.get(this.currentPokemon));
         }

         if (this.fullOurPokemon == null) {
            return null;
         }

         if (this.teamPokemon == null) {
            return (PixelmonInGui)this.fullOurPokemon.get(this.currentPokemon);
         }

         if (this.currentPokemon < this.teamPokemon.length) {
            Iterator var1 = this.fullOurPokemon.iterator();

            while(var1.hasNext()) {
               PixelmonInGui pig = (PixelmonInGui)var1.next();
               if (this.teamPokemon[this.currentPokemon].equals(pig.pokemonUUID)) {
                  return pig;
               }
            }
         }
      }

      return null;
   }

   public EntityPixelmon getUserPokemon() {
      return this.getUserPokemon(CameraMode.Battle);
   }

   public EntityPixelmon getUserPokemon(CameraMode mode) {
      if (mode == CameraMode.Battle) {
         if ((this.currentPokemon != -1 || this.isSpectating) && this.teamPokemon != null && this.teamPokemon.length > 0) {
            if (this.currentPokemon == -1) {
               this.currentPokemon = 0;
            }

            int pokemonIndex = Math.min(this.currentPokemon, this.teamPokemon.length - 1);
            return this.getEntity(this.teamPokemon[pokemonIndex]);
         }
      } else if (mode == CameraMode.Evolution) {
         return GuiEvolve.currentPokemon;
      }

      return null;
   }

   public EntityPixelmon getEntity(UUID uuid) {
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.field_71441_e != null) {
         List loadedEntityList = mc.field_71441_e.field_72996_f;

         for(int i = 0; i < loadedEntityList.size(); ++i) {
            Entity e = (Entity)loadedEntityList.get(i);
            if (e instanceof EntityPixelmon) {
               EntityPixelmon pokemon = (EntityPixelmon)e;
               if (pokemon.getPokemonData().getUUID().equals(uuid)) {
                  return pokemon;
               }
            }
         }
      }

      return null;
   }

   public boolean hasLevelUps() {
      return !this.levelUpList.isEmpty();
   }

   public boolean hasNewAttacks() {
      return !this.newAttackList.isEmpty();
   }

   public void startBattle(int battleControllerIndex, ParticipantType[][] battleSetup, int afkActivate, int afkTurn, BattleRules rules) {
      this.battleControllerIndex = battleControllerIndex;
      this.battleSetup = battleSetup;
      this.rules = rules;
      this.displayedOurPokemon = null;
      this.displayedEnemyPokemon = null;
      this.displayedAllyPokemon = null;
      this.fullOurPokemon = null;
      this.teamPokemon = null;
      this.targetted = new boolean[2][];
      this.mode = BattleMode.Waiting;
      this.canSwitch = true;
      this.canFlee = true;
      this.battleEnded = false;
      this.megaEvolution = null;
      this.dynamaxing = false;
      this.dynamax = null;
      this.hasDynamaxed = false;
      this.gigantamax = false;
      this.hasUltraBurst = false;
      this.ultraBurst = null;
      this.showZMoves = false;
      this.usedZMove = false;
      if (afkActivate == -1) {
         this.afkOn = false;
      } else {
         this.afkOn = true;
         this.afkActivate = afkActivate;
         this.afkTurn = afkTurn;
         this.afkActive = false;
         this.resetAFKTime();
      }

      this.battleTasks.clear();
      this.isProcessingTask = EnumTriBoolean.NULL;
   }

   public void startSpectate(EnumBattleType battleType) {
      ClientProxy.battleManager.isSpectating = true;
      ClientProxy.battleManager.battleType = battleType;
      this.spectating = null;
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_147108_a(new GuiBattle());
   }

   public void endSpectate() {
      if (ClientProxy.battleManager.isSpectating) {
         ClientProxy.battleManager.isSpectating = false;
         ClientProxy.battleManager.battleEnded = true;
         this.spectating = null;
         this.spectatingUUID = null;
         this.resetViewEntity();
      }

   }

   @SubscribeEvent
   public void onTick(TickEvent.ClientTickEvent event) {
      if (event.phase == Phase.END && !this.battleEnded) {
         this.tick();
      }

   }

   private void tick() {
      Minecraft minecraft = Minecraft.func_71410_x();
      if (!(minecraft.field_71462_r instanceof GuiBattle) && minecraft.field_71439_g != null) {
         minecraft.func_147108_a(new GuiBattle());
      }

      if (this.ticksSincePicked > 0) {
         --this.ticksSincePicked;
      }

      boolean doCameraTick = true;
      if (ClientProxy.camera != null) {
         ICameraTarget tar = ClientProxy.camera.getTarget();
         if (tar != null) {
            EntityPixelmon userPokemon = this.getUserPokemon();
            if (tar.getTargetData() == userPokemon && (userPokemon == null || userPokemon.field_70128_L || userPokemon.func_110143_aJ() <= 0.0F)) {
               this.setCameraToPlayer();
               doCameraTick = false;
            }
         }
      }

      if (PixelmonConfig.useBattleCamera && doCameraTick && this.ticksSincePicked <= 0 && this.getUserPokemon() != null) {
         Entity viewEntity = minecraft.func_175606_aa();
         if (viewEntity instanceof EntityCamera && viewEntity != ClientProxy.camera) {
            minecraft.func_175607_a(ClientProxy.camera);
         }

         this.setCameraToPixelmon();
      }

   }

   public void restoreSettingsAndClose() {
      this.battleEnded = true;
      if (this.afkTask != null) {
         this.afkTask.cancel();
      }

      this.afkOn = false;
      this.catchCombo = 0;
      this.resetViewEntity();
      this.spectating = null;
      this.spectatingUUID = null;
      this.selectedActions.clear();
      this.enforcedFleeFailed = false;
      this.megaEvolution = null;
      this.dynamax = null;
      this.megaEvolving = false;
      this.dynamaxing = false;
      this.hasDynamaxed = false;
      this.gigantamax = false;
      this.hasUltraBurst = false;
      this.ultraBurst = null;
      this.showZMoves = false;
      this.usedZMove = false;
      this.mode = BattleMode.MainMenu;
      this.isProcessingTask = EnumTriBoolean.NULL;
      this.battleTasks.clear();
      this.parallelTasks.clear();
      this.displayedOurPokemon = null;
      this.displayedAllyPokemon = null;
      this.fullOurPokemon = null;
      this.teamPokemon = null;
      this.battleControllerIndex = -1;
      this.newAttackList.clear();
      this.levelUpList.clear();
      this.weather = null;
      this.terrain = null;
      this.dynamaxDisabled = false;
   }

   public boolean canCatchOpponent() {
      for(int i = 0; i < this.battleSetup[1].length; ++i) {
         ParticipantType t = this.battleSetup[1][i];
         if (t == ParticipantType.WildPokemon && this.displayedEnemyPokemon[i].bossLevel == EnumBossMode.NotBoss.index && !this.displayedEnemyPokemon[i].blockCapture) {
            return true;
         }
      }

      return false;
   }

   public boolean isOpponentWildNotBoss() {
      for(int i = 0; i < this.battleSetup[1].length; ++i) {
         ParticipantType t = this.battleSetup[1][i];
         if (t == ParticipantType.WildPokemon && this.displayedEnemyPokemon[i].bossLevel == EnumBossMode.NotBoss.index) {
            return true;
         }
      }

      return false;
   }

   public boolean canRunFromBattle() {
      for(int i = 0; i < this.battleSetup[1].length; ++i) {
         ParticipantType t = this.battleSetup[1][i];
         if (t == ParticipantType.WildPokemon) {
            return this.canFlee;
         }
      }

      return !this.rules.hasClause("forfeit");
   }

   public boolean canForfeit() {
      for(int i = 0; i < this.battleSetup[1].length; ++i) {
         ParticipantType t = this.battleSetup[1][i];
         if (t != ParticipantType.WildPokemon) {
            return true;
         }
      }

      return false;
   }

   public EntityPlayer getViewPlayer() {
      Minecraft minecraft = Minecraft.func_71410_x();
      if (this.isSpectating && this.spectatingUUID != null) {
         if (this.spectating == null) {
            this.spectating = minecraft.field_71441_e.func_152378_a(this.spectatingUUID);
         }

         if (this.spectating != null) {
            return this.spectating;
         }
      }

      return minecraft.field_71439_g;
   }

   public void startPicking(boolean canSwitch, boolean canFlee, ArrayList pokemonToChoose) {
      this.setCameraToPlayer();
      this.ticksSincePicked = 50;
      if (!this.isSpectating) {
         this.canSwitch = canSwitch;
         this.canFlee = canFlee;
         this.oldMode = BattleMode.MainMenu;
         this.mode = BattleMode.MainMenu;
         this.pokemonToChoose = pokemonToChoose;

         for(int i = 0; i < this.teamPokemon.length; ++i) {
            UUID uuid = this.teamPokemon[i];
            if (this.fullOurPokemon.stream().anyMatch((p) -> {
               return p.pokemonUUID.equals(uuid);
            }) && this.hasTurn(uuid)) {
               this.currentPokemon = i;
               break;
            }
         }

         if (this.displayedOurPokemon != null) {
            PixelmonInGui[] var8 = this.displayedOurPokemon;
            int var9 = var8.length;

            for(int var6 = 0; var6 < var9; ++var6) {
               PixelmonInGui pig = var8[var6];
               if (pig != null) {
                  pig.isSwitchingIn = false;
                  pig.isSwitchingOut = false;
               }
            }
         }
      }

   }

   private boolean hasTurn(UUID uuid) {
      Iterator var2 = this.pokemonToChoose.iterator();

      UUID pokemonUUID;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         pokemonUUID = (UUID)var2.next();
      } while(!pokemonUUID.equals(uuid));

      return true;
   }

   public void selectedMove() {
      this.selectedMove(false);
   }

   public void selectedMove(boolean isEnforcedSwitch) {
      this.afkActive = false;
      this.megaEvolving = false;
      this.dynamaxing = false;
      this.showZMoves = false;
      if (!isEnforcedSwitch && this.teamPokemon.length > this.currentPokemon + 1 && this.teamPokemon.length > this.selectedActions.size()) {
         int prevCurrentPokemon = this.currentPokemon;

         for(int i = this.currentPokemon + 1; i < this.teamPokemon.length; ++i) {
            UUID uuid = this.teamPokemon[i];
            if (!this.fullOurPokemon.stream().anyMatch((p) -> {
               return p.pokemonUUID.equals(uuid);
            })) {
               this.finishSelection();
               return;
            }

            if (this.hasTurn(uuid)) {
               this.currentPokemon = i;
               break;
            }
         }

         if (prevCurrentPokemon == this.currentPokemon) {
            this.finishSelection();
            return;
         }

         this.mode = BattleMode.MainMenu;
      } else {
         this.finishSelection();
      }

   }

   public void afkSelectMove() {
      if (this.mode != BattleMode.Waiting) {
         if (this.mode != BattleMode.EnforcedSwitch) {
            for(int i = this.currentPokemon; i < this.teamPokemon.length; ++i) {
               UUID uuid = this.teamPokemon[i];
               if (this.fullOurPokemon.stream().anyMatch((p) -> {
                  return p.pokemonUUID.equals(uuid);
               }) && this.hasTurn(uuid)) {
                  this.currentPokemon = i;
                  PixelmonInGui currentData = this.getCurrentPokemon();
                  Object action;
                  if (currentData.health <= 0.0F) {
                     action = new SwitchPokemon((UUID)null, this.battleControllerIndex, currentData.pokemonUUID, false);
                  } else {
                     action = new ChooseAttack(this.getCurrentPokemon().pokemonUUID, new boolean[0][0], -1, this.battleControllerIndex, this.megaEvolving, this.dynamaxing);
                  }

                  this.megaEvolving = false;
                  this.showZMoves = false;
                  this.dynamaxing = false;
                  this.selectedActions.add(action);
               }
            }
         } else {
            ArrayList inBattle = new ArrayList(3);
            if (this.teamPokemon != null) {
               UUID[] var2 = this.teamPokemon;
               int var3 = var2.length;
               int var4 = 0;

               while(true) {
                  if (var4 >= var3) {
                     PixelmonInGui currentData = (PixelmonInGui)inBattle.get(this.currentPokemon % inBattle.size());
                     this.selectedActions.add(new SwitchPokemon((UUID)null, this.battleControllerIndex, currentData.pokemonUUID, true));
                     break;
                  }

                  UUID uuid = var2[var4];
                  Iterator var6 = this.fullOurPokemon.iterator();

                  while(var6.hasNext()) {
                     PixelmonInGui pig = (PixelmonInGui)var6.next();
                     if (pig.pokemonUUID.equals(uuid)) {
                        inBattle.add(pig);
                     }
                  }

                  ++var4;
               }
            }
         }

         this.finishSelection();
      }

   }

   public void finishSelection() {
      this.mode = BattleMode.Waiting;
      Stream var10000 = this.selectedActions.stream().filter((action) -> {
         return action != null;
      });
      SimpleNetworkWrapper var10001 = Pixelmon.network;
      var10000.forEach(var10001::sendToServer);
      this.selectedActions.clear();
   }

   public void setTeamPokemon(UUID[] pokemon) {
      this.teamPokemon = pokemon;
      this.targetted[0] = new boolean[pokemon.length];
      if (this.displayedOurPokemon == null) {
         ArrayList ourPokemon = new ArrayList();
         UUID[] var3 = pokemon;
         int var4 = pokemon.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            UUID uuid = var3[var5];
            Iterator var7 = this.fullOurPokemon.iterator();

            while(var7.hasNext()) {
               PixelmonInGui pig = (PixelmonInGui)var7.next();
               if (pig.pokemonUUID.equals(uuid)) {
                  ourPokemon.add(pig);
               }
            }
         }

         this.displayedOurPokemon = (PixelmonInGui[])ourPokemon.toArray(new PixelmonInGui[0]);
         PixelmonInGui[] var9 = this.displayedOurPokemon;
         var4 = var9.length;

         for(var5 = 0; var5 < var4; ++var5) {
            PixelmonInGui pig = var9[var5];
            pig.xPos = 120;
         }
      }

   }

   public void setTeamPokemon(PixelmonInGui[] data) {
      PixelmonInGui[] var2;
      int var3;
      int var4;
      PixelmonInGui replacement;
      if (this.displayedOurPokemon != null) {
         var2 = data;
         var3 = data.length;

         for(var4 = 0; var4 < var3; ++var4) {
            replacement = var2[var4];
            PixelmonInGui[] var6 = this.displayedOurPokemon;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PixelmonInGui current = var6[var8];
               if (replacement.pokemonUUID.equals(current.pokemonUUID)) {
                  replacement.health = current.health;
               }
            }
         }
      }

      var2 = data;
      var3 = data.length;

      for(var4 = 0; var4 < var3; ++var4) {
         replacement = var2[var4];

         for(int i = 0; i < this.fullOurPokemon.size(); ++i) {
            PixelmonInGui pig = (PixelmonInGui)this.fullOurPokemon.get(i);
            if (Objects.equals(pig.pokemonUUID, replacement.pokemonUUID)) {
               replacement.health = pig.health;
               this.fullOurPokemon.set(i, replacement);
            }
         }
      }

      this.displayedOurPokemon = data;
      this.targetted[0] = new boolean[0];
      var2 = this.displayedOurPokemon;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         replacement = var2[var4];
         replacement.xPos = 120;
      }

   }

   public void setOpponents(PixelmonInGui[] data) {
      if (this.displayedEnemyPokemon != null && data != null) {
         PixelmonInGui[] var2 = data;
         int var3 = data.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PixelmonInGui replacement = var2[var4];
            PixelmonInGui[] var6 = this.displayedEnemyPokemon;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PixelmonInGui current = var6[var8];
               if (replacement.pokemonUUID.equals(current.pokemonUUID)) {
                  replacement.health = current.health;
               }
            }
         }
      }

      this.displayedEnemyPokemon = data;
      this.targetted[1] = new boolean[this.displayedEnemyPokemon.length];
   }

   public void setTeamData(PixelmonInGui[] data) {
      PixelmonInGui[] var2;
      int var3;
      int var4;
      PixelmonInGui replacement;
      if (this.displayedAllyPokemon != null && data != null) {
         var2 = data;
         var3 = data.length;

         for(var4 = 0; var4 < var3; ++var4) {
            replacement = var2[var4];
            PixelmonInGui[] var6 = this.displayedAllyPokemon;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PixelmonInGui current = var6[var8];
               if (replacement.pokemonUUID.equals(current.pokemonUUID)) {
                  replacement.health = current.health;
               }
            }
         }
      }

      this.displayedAllyPokemon = data;
      var2 = this.displayedAllyPokemon;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         replacement = var2[var4];
         replacement.xPos = 120;
      }

   }

   public void setFullTeamData(PixelmonInGui[] data) {
      if (this.fullOurPokemon != null) {
         PixelmonInGui[] var2 = data;
         int var3 = data.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PixelmonInGui replacement = var2[var4];
            Iterator var6 = this.fullOurPokemon.iterator();

            while(var6.hasNext()) {
               PixelmonInGui current = (PixelmonInGui)var6.next();
               if (replacement.pokemonUUID.equals(current.pokemonUUID)) {
                  replacement.health = current.health;
               }
            }
         }
      }

      this.fullOurPokemon = Lists.newArrayList(data);

      PixelmonInGui pig;
      for(Iterator var8 = this.fullOurPokemon.iterator(); var8.hasNext(); pig.xPos = 120) {
         pig = (PixelmonInGui)var8.next();
      }

   }

   public PixelmonInGui getUncontrolledTeamPokemon(UUID uuid) {
      if (this.displayedAllyPokemon == null) {
         return null;
      } else {
         PixelmonInGui[] var2 = this.displayedAllyPokemon;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PixelmonInGui data = var2[var4];
            if (uuid.equals(data.pokemonUUID)) {
               return data;
            }
         }

         return null;
      }
   }

   public PixelmonInGui getPokemon(UUID uuid) {
      PixelmonInGui[] var2;
      int var3;
      int var4;
      PixelmonInGui pig;
      if (this.displayedOurPokemon != null) {
         var2 = this.displayedOurPokemon;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            pig = var2[var4];
            if (uuid.equals(pig.pokemonUUID)) {
               return pig;
            }
         }
      }

      if (this.displayedEnemyPokemon != null) {
         var2 = this.displayedEnemyPokemon;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            pig = var2[var4];
            if (uuid.equals(pig.pokemonUUID)) {
               return pig;
            }
         }
      }

      if (this.displayedAllyPokemon != null) {
         var2 = this.displayedAllyPokemon;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            pig = var2[var4];
            if (uuid.equals(pig.pokemonUUID)) {
               return pig;
            }
         }
      }

      if (this.fullOurPokemon != null) {
         Iterator var6 = this.fullOurPokemon.iterator();

         while(var6.hasNext()) {
            PixelmonInGui pig = (PixelmonInGui)var6.next();
            if (uuid.equals(pig.pokemonUUID)) {
               return pig;
            }
         }
      }

      return null;
   }

   public boolean isEnemyPokemon(PixelmonInGui pokemon) {
      PixelmonInGui[] var2 = this.displayedEnemyPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonInGui p = var2[var4];
         if (p == pokemon) {
            return true;
         }
      }

      return false;
   }

   public List getBagItems(BagSection section) {
      Map map = new HashMap();
      InventoryPlayer inventory = Minecraft.func_71410_x().field_71439_g.field_71071_by;

      for(int i = 0; i < inventory.field_70462_a.size(); ++i) {
         if (inventory.field_70462_a.get(i) != null) {
            Item item = ((ItemStack)inventory.field_70462_a.get(i)).func_77973_b();
            boolean valid = false;
            if (section == BagSection.StatusRestore) {
               if (item instanceof ItemStatusAilmentHealer || item == PixelmonItems.fullRestore || item instanceof ItemBerryStatus || item instanceof ItemMentalHerb || item instanceof ItemBerryGinema || item instanceof ItemWhiteHerb) {
                  valid = true;
               }
            } else if (section == BagSection.BattleItems) {
               if (item instanceof ItemBattleItem) {
                  valid = true;
               }
            } else if (section == BagSection.Pokeballs) {
               if (item instanceof ItemPokeball) {
                  valid = true;
               }
            } else if (section == BagSection.HP && (item instanceof ItemMedicine || item instanceof ItemBerryRestoreHP || item instanceof ItemBerryJuice || item instanceof ItemPPRestore || item instanceof ItemBerryLeppa)) {
               valid = true;
            }

            if (valid) {
               if (map.containsKey(item)) {
                  ItemData var10000 = (ItemData)map.get(item);
                  var10000.count += ((ItemStack)inventory.field_70462_a.get(i)).func_190916_E();
               } else {
                  map.put(item, new ItemData(item, ((ItemStack)inventory.field_70462_a.get(i)).func_190916_E()));
               }
            }
         }
      }

      List list = Lists.newArrayList(map.values());
      list.sort(Comparator.comparingInt((it) -> {
         return it.id;
      }));
      return list;
   }

   public void setCameraToPixelmon() {
      if (ClientProxy.camera != null) {
         ICameraTarget tar = ClientProxy.camera.getTarget();
         if (tar != null) {
            EntityPixelmon userPokemon = this.getUserPokemon();
            if (tar.getTargetData() != userPokemon) {
               if (tar instanceof CameraTargetEntity) {
                  tar.setTargetData(userPokemon);
               } else if (!PixelmonConfig.playerControlCamera) {
                  ClientProxy.camera.setTarget(new CameraTargetEntity(userPokemon));
               }
            }
         } else if (!PixelmonConfig.playerControlCamera) {
            ClientProxy.camera.setTarget(new CameraTargetEntity(Minecraft.func_71410_x().field_71439_g));
         }
      }

   }

   public void setCameraToPlayer() {
      if (ClientProxy.camera != null) {
         ICameraTarget tar = ClientProxy.camera.getTarget();
         if (tar != null) {
            if (tar.getTargetData() != this.getViewPlayer()) {
               if (tar instanceof CameraTargetEntity) {
                  tar.setTargetData(this.getViewPlayer());
               } else if (!PixelmonConfig.playerControlCamera) {
                  ClientProxy.camera.setTarget(new CameraTargetEntity(this.getViewPlayer()));
               }
            }
         } else if (ClientProxy.camera != null && this.getViewPlayer() != null) {
            if (!PixelmonConfig.playerControlCamera) {
               ClientProxy.camera.setTarget(new CameraTargetEntity(this.getViewPlayer()));
            }
         } else {
            Pixelmon.LOGGER.warn("Problem finding battle camera.");
         }
      }

   }

   public void setViewEntity(Entity entity) {
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_175607_a(entity);
      mc.field_71474_y.field_74319_N = true;
      this.thirdP = mc.field_71474_y.field_74320_O;
      mc.field_71474_y.field_74320_O = 0;
      GuiIngameForge.renderHotbar = false;
      GuiIngameForge.renderCrosshairs = false;
      GuiIngameForge.renderExperiance = false;
      GuiIngameForge.renderAir = false;
      GuiIngameForge.renderHealth = false;
      GuiIngameForge.renderFood = false;
      GuiIngameForge.renderArmor = false;
   }

   public Entity getViewEntity() {
      return Minecraft.func_71410_x().func_175606_aa();
   }

   public void resetViewEntity() {
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_175607_a(mc.field_71439_g);
      GuiIngameForge.renderHotbar = true;
      GuiIngameForge.renderCrosshairs = true;
      GuiIngameForge.renderExperiance = true;
      GuiIngameForge.renderAir = true;
      GuiIngameForge.renderHealth = true;
      GuiIngameForge.renderFood = true;
      GuiIngameForge.renderArmor = true;
      mc.field_71474_y.field_74319_N = false;
      mc.field_71474_y.field_74320_O = this.thirdP;
      if (ClientProxy.camera != null) {
         ClientProxy.camera.func_70106_y();
      }

      ClientProxy.camera = null;
   }

   public void resetAFKTime() {
      this.setAFKTimer(this.afkActive ? this.afkTurn : this.afkActivate);
   }

   private void setAFKTimer(int time) {
      this.afkTime = time;
      if (this.afkTask != null) {
         this.afkTask.cancel();
      }

      this.afkTask = new AFKTask(this);
      TIMER.scheduleAtFixedRate(this.afkTask, 1000L, 1000L);
   }

   public void setTeamSelectTime() {
      if (this.rules.teamSelectTime > 0) {
         this.afkOn = true;
         this.setAFKTimer(this.rules.teamSelectTime);
      } else {
         this.afkOn = false;
      }

   }

   public Optional getLastUsedItemIfStillAvailable() {
      if (this.lastItem != null) {
         List bag = this.getBagItems((BagSection)this.lastItem.getKey());
         if (bag.contains(this.lastItem.getValue())) {
            return Optional.of(this.lastItem.getValue());
         }
      }

      return Optional.empty();
   }

   public void selectRunAction(UUID pokemonUUID) {
      if (this.canForfeit()) {
         this.oldMode = this.mode;
         this.mode = BattleMode.YesNoForfeit;
         CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      } else {
         Pixelmon.network.sendToServer(new Flee(pokemonUUID));
         this.mode = BattleMode.Waiting;
      }

   }

   public boolean canMegaEvolve() {
      if (this.rules.battleType == EnumBattleType.Raid) {
         return false;
      } else if (this.megaEvolution != null) {
         return false;
      } else {
         Iterator var1 = this.selectedActions.iterator();

         while(var1.hasNext()) {
            IMessage message = (IMessage)var1.next();
            if (message instanceof ChooseAttack) {
               ChooseAttack attackMessage = (ChooseAttack)message;
               if (attackMessage.megaEvolving && attackMessage.pokemonUUID != null && !this.getEntity(attackMessage.pokemonUUID).getSpecies().is(EnumSpecies.Necrozma)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean canDynamax() {
      if (!this.hasDynamaxed && !this.dynamaxDisabled) {
         if (this.dynamax != null) {
            return false;
         } else {
            Iterator var1 = this.selectedActions.iterator();

            while(var1.hasNext()) {
               IMessage message = (IMessage)var1.next();
               if (message instanceof ChooseAttack) {
                  ChooseAttack attackMessage = (ChooseAttack)message;
                  if (attackMessage.dynamaxing) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean canMegaEvolve(PixelmonInGui pig) {
      EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
      if (player == null) {
         return false;
      } else {
         EnumMegaItem emi = EntityPlayerExtension.getPlayerMegaItem(player);
         if (emi == null) {
            return false;
         } else {
            return this.canMegaEvolve() && emi.canMega() && (pig.species == EnumSpecies.Rayquaza && pig.moveset.hasAttack("Dragon Ascent") && pig.heldItem.getHeldItemType() != EnumHeldItems.zCrystal || PixelmonWrapper.canMegaEvolve((ItemHeld)pig.heldItem, pig.species, pig.form));
         }
      }
   }

   public boolean canUltraBurst() {
      if (this.rules.battleType == EnumBattleType.Raid) {
         return false;
      } else if (this.hasUltraBurst) {
         return false;
      } else {
         Iterator var1 = this.selectedActions.iterator();

         while(var1.hasNext()) {
            IMessage message = (IMessage)var1.next();
            if (message instanceof ChooseAttack) {
               ChooseAttack attackMessage = (ChooseAttack)message;
               if (attackMessage.megaEvolving && this.getEntity(attackMessage.pokemonUUID).getSpecies().is(EnumSpecies.Necrozma)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean canUltraBurst(PixelmonInGui pig) {
      EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
      if (player == null) {
         return false;
      } else {
         EnumMegaItem emi = EntityPlayerExtension.getPlayerMegaItem(player);
         if (emi == null) {
            return false;
         } else {
            return this.canUltraBurst() && emi.canMega() && pig.species == EnumSpecies.Necrozma && (pig.form == 1 || pig.form == 2) && pig.heldItem == PixelmonItemsHeld.ultranecrozium_z;
         }
      }
   }

   public boolean canDynamax(PixelmonInGui pig) {
      EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
      if (player == null) {
         return false;
      } else {
         EnumMegaItem emi = EntityPlayerExtension.getPlayerMegaItem(player);
         if (emi == null) {
            return false;
         } else {
            return this.canDynamax() && pig.species != EnumSpecies.Zacian && pig.species != EnumSpecies.Zamazenta && pig.species != EnumSpecies.Eternatus && (pig.species != EnumSpecies.Groudon || pig.heldItem != PixelmonItemsHeld.redOrb) && (pig.species != EnumSpecies.Kyogre || pig.heldItem != PixelmonItemsHeld.blueOrb) && emi.canDynamax();
         }
      }
   }

   public boolean canUseZMove() {
      if (this.rules.battleType == EnumBattleType.Raid) {
         return false;
      } else {
         return !this.usedZMove;
      }
   }

   public boolean canUseZMove(PixelmonInGui pig) {
      EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
      if (player == null) {
         return false;
      } else {
         EnumMegaItem emi = EntityPlayerExtension.getPlayerMegaItem(player);
         if (emi == null) {
            return false;
         } else if (pig.species.is(EnumSpecies.Necrozma) && !pig.mega) {
            return false;
         } else {
            return this.canUseZMove() && emi.canMega() && pig.heldItem.getHeldItemType() == EnumHeldItems.zCrystal && !EntityPlayerExtension.getPlayerMegaItem(Minecraft.func_71410_x().field_71439_g).canDynamax();
         }
      }
   }
}
