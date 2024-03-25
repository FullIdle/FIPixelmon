package com.pixelmonmod.pixelmon.entities.pixelmon.helpers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.MegaEvolutionEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolvePokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.OpenEvolutionGUI;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MirrorArmor;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Pressure;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.LevelingEvolution;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.AirSaver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EvolutionQuery {
   EntityPixelmon pixelmon;
   Evolution evolution = null;
   PokemonSpec newPokemon;
   public UUID pokemonUUID;
   boolean fromLevelUp;
   int level;
   int newForm;
   EntityPlayerMP player;
   boolean normalEvolution = true;
   EvolutionStage stage;
   private AirSaver airSaver;
   int ticks = 0;

   public EvolutionQuery(EntityPixelmon pixelmon, Evolution evolution, int form) {
      this.pixelmon = pixelmon;
      this.newPokemon = evolution.to.copy();
      this.pokemonUUID = pixelmon.getPokemonData().getUUID();
      this.evolution = evolution;
      this.level = pixelmon.getLvl().getLevel();
      this.fromLevelUp = evolution instanceof LevelingEvolution;
      if (form > -1) {
         this.newPokemon.form = (byte)form;
      }

      if (pixelmon.func_70902_q() != null) {
         this.player = (EntityPlayerMP)pixelmon.func_70902_q();
         this.airSaver = new AirSaver(this.player);
      }

      this.sendQuery();
      pixelmon.field_70714_bg.field_75782_a.clear();
   }

   public EvolutionQuery(EntityPixelmon pixelmon, int form) {
      this.normalEvolution = false;
      this.pixelmon = pixelmon;
      this.pokemonUUID = pixelmon.getPokemonData().getUUID();
      this.fromLevelUp = false;
      this.newForm = form;
      if (pixelmon.func_70902_q() != null) {
         this.player = (EntityPlayerMP)pixelmon.func_70902_q();
         this.airSaver = new AirSaver(this.player);
      }

      this.level = pixelmon.getLvl().getLevel();
      pixelmon.field_70714_bg.field_75782_a.clear();
      this.setStage(EvolutionStage.PreAnimation);
      this.updateAllAround(this.stage);
      synchronized(EvolutionQueryList.queryList) {
         EvolutionQueryList.queryList.add(this);
      }
   }

   void tick(World world) {
      if (this.pixelmon.field_70170_p == world) {
         if (this.airSaver != null) {
            this.airSaver.tick();
         }

         if (this.stage != EvolutionStage.Choice) {
            ++this.ticks;
            if (this.stage == EvolutionStage.PreChoice) {
               if (this.ticks >= this.stage.ticks) {
                  this.ticks = 0;
                  this.setStage(EvolutionStage.Choice);
               }
            } else if (this.stage == EvolutionStage.PreAnimation) {
               if (this.ticks >= this.stage.ticks) {
                  this.ticks = 0;
                  this.setStage(EvolutionStage.PostAnimation);
                  this.updateAllAround(this.stage);
                  this.doEvoSwitch();
               }
            } else if (this.stage == EvolutionStage.PostAnimation && this.ticks >= this.stage.ticks) {
               this.ticks = 0;
               this.setStage(EvolutionStage.End);
               if (this.evolution != null) {
                  this.evolution.finishedEvolving(this.pixelmon);
               }

               this.updateAllAround(this.stage);
               synchronized(EvolutionQueryList.queryList) {
                  EvolutionQueryList.queryList.remove(this);
               }
            }

         }
      }
   }

   private void setStage(EvolutionStage stage) {
      this.stage = stage;
      this.pixelmon.evoStage = stage;
   }

   private void sendQuery() {
      if (this.pixelmon.hasOwner()) {
         Pixelmon.network.sendTo(new OpenEvolutionGUI(this.pokemonUUID, this.newPokemon.name), this.player);
         this.updateAllAround(EvolutionStage.Choice);
         this.removeExisting();
         synchronized(EvolutionQueryList.queryList) {
            EvolutionQueryList.queryList.add(this);
         }
      }

   }

   private void removeExisting() {
      synchronized(EvolutionQueryList.queryList) {
         for(int i = 0; i < EvolutionQueryList.queryList.size(); ++i) {
            EvolutionQuery query = (EvolutionQuery)EvolutionQueryList.queryList.get(i);
            if (query.pokemonUUID.equals(this.pokemonUUID)) {
               EvolutionQueryList.queryList.remove(i);
               break;
            }
         }

      }
   }

   void accept() {
      this.setStage(EvolutionStage.PreAnimation);
      this.ticks = 0;
      this.updateAllAround(this.stage);
   }

   void doEvoSwitch() {
      if (!this.pixelmon.field_70128_L) {
         EntityPixelmon pre = (EntityPixelmon)PixelmonEntityList.createEntityFromNBT(this.pixelmon.func_189511_e(new NBTTagCompound()), this.pixelmon.field_70170_p);
         pre.func_184221_a(UUID.randomUUID());
         if (this.normalEvolution) {
            if (this.newPokemon.name == null) {
               System.out.println("EVOLVE FAULT: " + this.player.func_70005_c_() + " - " + this.pixelmon.func_70005_c_());
               return;
            }

            this.checkMagbyJump();
            this.pixelmon.evolve(this.newPokemon);
            if (this.pixelmon.getSpecies() == pre.getSpecies()) {
               Pixelmon.LOGGER.error("Evolution: " + this);
            }

            this.checkShedinja();
            this.checkForLearnMoves();
            this.checkForEvolutionMoves();
            this.checkCorvisquire();
            this.evolution.finishedEvolving(this.pixelmon);
            Pixelmon.EVENT_BUS.post(new EvolveEvent.PostEvolve(this.player, pre, this.evolution, this.pixelmon));
            if (!this.pixelmon.getPokemonName().equals(EnumSpecies.Shedinja.name)) {
               Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(this.player, ReceiveType.Evolution, this.pixelmon.getPokemonData()));
            }
         } else {
            Pokemon pokemon = this.pixelmon.getPokemonData();
            boolean isUltraburst = PixelmonWrapper.canUltraBurst(pokemon.getSpecies(), pokemon.getHeldItemAsItemHeld(), pokemon.getForm()) && this.newForm == EnumNecrozma.ULTRA.getForm();
            ItemMegaStone megaStone = pokemon.getHeldItemAsItemHeld() instanceof ItemMegaStone ? (ItemMegaStone)pokemon.getHeldItemAsItemHeld() : null;
            if (this.pixelmon.getFormEnum().getForm() != this.newForm) {
               this.pixelmon.setForm(this.newForm);
            }

            this.pixelmon.func_184185_a(SoundEvents.field_187539_bB, 1.0F, 1.0F);
            if (this.pixelmon.battleController == null) {
               this.pixelmon.resetAI();
               this.pixelmon.setBlockTarget((int)this.pixelmon.field_70165_t, (int)this.pixelmon.field_70163_u, (int)this.pixelmon.field_70161_v, EnumFacing.SOUTH, (String)null);
            }

            Pixelmon.EVENT_BUS.post(new MegaEvolutionEvent.PostEvolve(this.player, pre, megaStone, isUltraburst, this.pixelmon));
         }

      }
   }

   private void removeEntity() {
      this.pixelmon.unloadEntity();
   }

   void decline() {
      this.removeEntity();
   }

   private void checkCorvisquire() {
      if (this.pixelmon.getBaseStats().getSpecies() == EnumSpecies.Corvisquire) {
         int slot = this.pixelmon.getPokemonData().getAbilitySlot();
         if (slot == 0) {
            this.pixelmon.getPokemonData().setAbility((AbilityBase)(new Pressure()));
         } else if (slot == 2) {
            this.pixelmon.getPokemonData().setAbility((AbilityBase)(new MirrorArmor()));
         }
      }

   }

   private void checkMagbyJump() {
      if (this.pixelmon.getBaseStats().getSpecies() == EnumSpecies.Magby) {
         PokemonStorage party = this.pixelmon.getPokemonData().getStorage();
         if (party != null) {
            Pokemon[] var2 = party.getAll();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Pokemon poke = var2[var4];
               if (poke != null && poke.getSpecies() == EnumSpecies.Slugma && poke.getFormEnum() != EnumNoForm.NoForm) {
                  this.newPokemon.form = poke.getFormEnum().getForm();
                  break;
               }
            }
         }
      }

   }

   private void checkShedinja() {
      if (this.pixelmon.getBaseStats().getSpecies() == EnumSpecies.Ninjask) {
         PokemonStorage party = (PokemonStorage)this.pixelmon.getPokemonData().getStorageAndPosition().func_76341_a();
         if (party.hasSpace()) {
            EntityPlayerMP player = party instanceof PlayerPartyStorage ? ((PlayerPartyStorage)party).getPlayer() : null;
            if (player != null && player.field_71071_by.func_174925_a(PixelmonItemsPokeballs.pokeBall, 0, 1, (NBTTagCompound)null) == 1) {
               Pokemon shedinja = Pixelmon.pokemonFactory.create(new PokemonSpec(new String[]{EnumSpecies.Shedinja.name, "lvl:" + this.pixelmon.getPokemonData().getLevel()}));
               shedinja.getMoveset().clear();
               shedinja.getMoveset().addAll(this.pixelmon.getPokemonData().getMoveset());
               shedinja.setStatus((StatusPersist)this.pixelmon.getPokemonData().getStatus().copy());
               shedinja.setShiny(this.pixelmon.getPokemonData().isShiny());
               shedinja.setGrowth(this.pixelmon.getPokemonData().getGrowth());
               shedinja.setFriendship(this.pixelmon.getPokemonData().getFriendship());
               shedinja.setNature(this.pixelmon.getPokemonData().getNature());
               shedinja.setExperience(this.pixelmon.getPokemonData().getExperience());
               shedinja.getEVs().fillFromArray(this.pixelmon.getPokemonData().getEVs().getArray());
               shedinja.getIVs().CopyIVs(this.pixelmon.getPokemonData().getIVs());
               shedinja.setOriginalTrainer(this.pixelmon.getPokemonData().getOriginalTrainerUUID(), this.pixelmon.getPokemonData().getOriginalTrainer());
               party.add(shedinja);
            }
         }
      }

   }

   private void checkForLearnMoves() {
      if (this.pixelmon.getBaseStats() != null) {
         int level = this.pixelmon.getLvl().getLevel();
         if (level == 1) {
            level = 0;
         }

         if (!this.pixelmon.getBaseStats().getMovesAtLevel(level).isEmpty()) {
            ArrayList newAttacks = this.pixelmon.getBaseStats().getMovesAtLevel(level);
            Moveset moveset = this.pixelmon.getPokemonData().getMoveset();
            newAttacks.stream().filter((a) -> {
               return !moveset.hasAttack(a);
            }).forEach((a) -> {
               if (moveset.size() >= 4) {
                  LearnMoveController.sendLearnMove(this.player, this.pixelmon.func_110124_au(), a.getActualMove());
               } else {
                  moveset.add(a);
                  this.pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
                  if (BattleRegistry.getBattle((EntityPlayer)this.pixelmon.func_70902_q()) != null) {
                     ChatHandler.sendBattleMessage((Entity)this.pixelmon.func_70902_q(), "pixelmon.stats.learnedmove", this.pixelmon.getNickname(), a.getMove().getTranslatedName());
                  } else {
                     ChatHandler.sendChat(this.pixelmon.func_70902_q(), "pixelmon.stats.learnedmove", this.pixelmon.getNickname(), a.getMove().getTranslatedName());
                  }
               }

            });
         }

      }
   }

   public void checkForEvolutionMoves() {
      if (this.evolution.moves != null && !this.evolution.moves.isEmpty()) {
         List evoMoves = new ArrayList();
         Iterator var2 = this.evolution.moves.iterator();

         while(var2.hasNext()) {
            String moveName = (String)var2.next();
            AttackBase ab = (AttackBase)AttackBase.getAttackBase(moveName).orElse((Object)null);
            if (ab == null) {
               Pixelmon.LOGGER.error("Unknown move in evolution. To: " + this.evolution.to.name + ". Move: " + moveName);
            } else {
               evoMoves.add(ab);
               if (!this.pixelmon.relearnableEvolutionMoves.contains(ab.getAttackId())) {
                  this.pixelmon.relearnableEvolutionMoves.add(ab.getAttackId());
               }
            }
         }

         this.pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
         var2 = evoMoves.iterator();

         while(var2.hasNext()) {
            AttackBase ab = (AttackBase)var2.next();
            Attack a = new Attack(ab);
            Moveset moveset = this.pixelmon.getPokemonData().getMoveset();
            if (!moveset.hasAttack(a)) {
               if (moveset.size() >= 4) {
                  LearnMoveController.sendLearnMove(this.player, this.pixelmon.func_110124_au(), a.getActualMove());
               } else {
                  moveset.add(a);
                  this.pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
                  if (BattleRegistry.getBattle((EntityPlayer)this.pixelmon.func_70902_q()) != null) {
                     ChatHandler.sendBattleMessage((Entity)this.pixelmon.func_70902_q(), "pixelmon.stats.learnedmove", this.pixelmon.getNickname(), a.getMove().getTranslatedName());
                  } else {
                     ChatHandler.sendChat(this.pixelmon.func_70902_q(), "pixelmon.stats.learnedmove", this.pixelmon.getNickname(), a.getMove().getTranslatedName());
                  }
               }
            }
         }

      }
   }

   public boolean isEnded() {
      return this.stage == EvolutionStage.End;
   }

   private void updateAllAround(EvolutionStage currentStage) {
      EntityLivingBase owner = this.pixelmon.func_70902_q();
      if (owner == null) {
         owner = this.pixelmon;
      }

      NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(((EntityLivingBase)owner).field_71093_bK, ((EntityLivingBase)owner).field_70165_t, ((EntityLivingBase)owner).field_70163_u, ((EntityLivingBase)owner).field_70161_v, 60.0);
      Pixelmon.network.sendToAllAround(new EvolvePokemon(this.pokemonUUID, currentStage), point);
   }
}
