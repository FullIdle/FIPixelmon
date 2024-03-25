package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.enums.EnumTriBoolean;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.raids.JoinRaidEvent;
import com.pixelmonmod.pixelmon.api.events.raids.RaidDropsEvent;
import com.pixelmonmod.pixelmon.api.events.raids.StartRaidEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.tasks.RaidDropsTask;
import com.pixelmonmod.pixelmon.comm.packetHandlers.raids.CloseRaid;
import com.pixelmonmod.pixelmon.comm.packetHandlers.raids.OpenRaid;
import com.pixelmonmod.pixelmon.comm.packetHandlers.raids.UpdateRaidCatch;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.entities.pokeballs.PokeballTypeHelper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RaidData {
   public final UUID uuid;
   private final int stars;
   private final EnumSpecies species;
   private final IEnumForm form;
   private final int entityID;
   public final boolean canAllCatch;
   public final boolean canUseMaster;
   private boolean locked;
   private boolean starting;
   private UUID owner;
   private final ArrayList players;
   public int tick;
   private static final int[][] npcOffsets = new int[][]{{2, 0}, {0, 2}, {-2, 0}, {0, -2}};
   private static final float[] npcRotations = new float[]{180.0F, 270.0F, 0.0F, 90.0F};

   public RaidData(int entityID, int stars, EnumSpecies species) {
      this(entityID, stars, species, (IEnumForm)RandomHelper.getRandomElementFromCollection(species.getDefaultForms()));
   }

   public RaidData(int entityID, int stars, EnumSpecies species, IEnumForm form) {
      this.uuid = UUID.randomUUID();
      this.locked = false;
      this.starting = false;
      this.owner = null;
      this.players = new ArrayList();
      this.tick = 0;
      this.entityID = entityID;
      this.stars = stars;
      this.species = species;
      this.form = form;
      this.canAllCatch = PixelmonConfig.getRaidCanAllCatch(this.stars);
      this.canUseMaster = PixelmonConfig.getRaidCanUseMaster(this.stars);
   }

   public RaidData(int entityID, int stars, int species, int form) {
      this.uuid = UUID.randomUUID();
      this.locked = false;
      this.starting = false;
      this.owner = null;
      this.players = new ArrayList();
      this.tick = 0;
      this.entityID = entityID;
      this.stars = stars;
      this.species = EnumSpecies.values()[species];
      this.form = form < 0 ? null : this.species.getFormEnum(form);
      this.canAllCatch = PixelmonConfig.getRaidCanAllCatch(this.stars);
      this.canUseMaster = PixelmonConfig.getRaidCanUseMaster(this.stars);
   }

   public RaidData(NBTTagCompound tag) {
      this.uuid = UUID.randomUUID();
      this.locked = false;
      this.starting = false;
      this.owner = null;
      this.players = new ArrayList();
      this.tick = 0;
      NBTTagCompound nbt = tag.func_74775_l("RaidData");
      this.tick = nbt.func_74762_e("Tick");
      this.entityID = nbt.func_74762_e("EntityID");
      this.stars = nbt.func_74765_d("Stars");
      this.species = EnumSpecies.values()[nbt.func_74765_d("Species")];
      if (nbt.func_74767_n("HasForm")) {
         this.form = this.species.getFormEnum(nbt.func_74771_c("Form"));
      } else {
         this.form = null;
      }

      this.locked = nbt.func_74767_n("Locked");
      this.starting = nbt.func_74767_n("Starting");
      if (nbt.func_186855_b("Owner")) {
         this.owner = nbt.func_186857_a("Owner");
      }

      NBTTagList participants = nbt.func_150295_c("Players", 10);
      Iterator var4 = participants.iterator();

      while(var4.hasNext()) {
         NBTBase entry = (NBTBase)var4.next();
         this.players.add(new RaidPlayer((NBTTagCompound)entry));
      }

      this.canAllCatch = nbt.func_74767_n("CanAllCatch");
      this.canUseMaster = nbt.func_74767_n("CanUseMaster");
   }

   public RaidData(ByteBuf buf) {
      this.uuid = UUID.randomUUID();
      this.locked = false;
      this.starting = false;
      this.owner = null;
      this.players = new ArrayList();
      this.tick = 0;
      this.tick = buf.readInt();
      this.entityID = buf.readInt();
      this.stars = buf.readShort();
      this.species = EnumSpecies.values()[buf.readShort()];
      if (buf.readBoolean()) {
         this.form = this.species.getFormEnum(buf.readByte());
      } else {
         this.form = null;
      }

      this.locked = buf.readBoolean();
      this.starting = buf.readBoolean();
      if (buf.readBoolean()) {
         this.owner = UUIDHelper.readUUID(buf);
      }

      int count = buf.readInt();

      for(int i = 0; i < count; ++i) {
         this.players.add(new RaidPlayer(buf));
      }

      this.canAllCatch = buf.readBoolean();
      this.canUseMaster = buf.readBoolean();
   }

   public int getDen() {
      return this.entityID;
   }

   public int getStars() {
      return this.stars;
   }

   public EnumSpecies getSpecies() {
      return this.species;
   }

   public IEnumForm getForm() {
      return this.form;
   }

   public ArrayList getPlayers() {
      return this.players;
   }

   public boolean hasPlayers() {
      HashSet playersToRemove = new HashSet();
      Iterator var2 = this.players.iterator();

      while(var2.hasNext()) {
         RaidPlayer rp = (RaidPlayer)var2.next();
         if (rp.isPlayer() && rp.playerEntity.func_193105_t()) {
            playersToRemove.add(rp.playerEntity);
         }
      }

      var2 = playersToRemove.iterator();

      while(var2.hasNext()) {
         EntityPlayerMP toRemove = (EntityPlayerMP)var2.next();
         this.removePlayer(toRemove);
      }

      return !this.players.isEmpty();
   }

   public boolean isSolo() {
      return this.players.size() == 1 && this.locked || this.players.size() == 0;
   }

   public void processAction(int action, int slot, EntityPlayerMP ep) {
      if (!this.starting || action == 10) {
         RaidPlayer player = this.getPlayer(ep);
         if (player != null) {
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(ep);
            Pokemon[] team = pps.getAll();
            switch (action) {
               case 0:
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
                  Pokemon pokemon = team[action];
                  if (pokemon != null && pokemon.canBattle()) {
                     player.updatePokemon(action, pokemon);
                     this.updateAll();
                  }
                  break;
               case 6:
                  if (this.players.size() == 1) {
                     this.locked = true;
                     this.starting = true;
                     this.updateAll();
                  }
                  break;
               case 7:
                  if (this.players.size() == 1) {
                     this.locked = false;
                     this.updateAll();
                  }
                  break;
               case 8:
                  this.removePlayer(ep);
                  break;
               case 9:
                  if (this.players.size() >= 1) {
                     this.locked = true;
                     this.starting = true;
                     this.updateAll();
                  }
                  break;
               case 10:
                  if (!player.catchAttempted && player.possibleCatch != null) {
                     player.catchAttempted = true;
                     if (slot >= 0 && slot <= ep.field_71071_by.func_70302_i_()) {
                        ItemStack stack = ep.field_71071_by.func_70301_a(slot);
                        if (!stack.func_190926_b() && stack.func_77973_b() instanceof ItemPokeball) {
                           ItemPokeball pokeball = (ItemPokeball)stack.func_77973_b();
                           if (!this.canUseMaster && pokeball.type.getBallBonus() >= 255.0) {
                              return;
                           }

                           int shakes = player.tryCatch(this, pokeball.type);
                           boolean sentToPC = false;
                           if (shakes >= 3) {
                              if (Pixelmon.EVENT_BUS.post(new CaptureEvent.SuccessfulRaidCapture(ep, player.possibleCatch, this))) {
                                 shakes = RandomHelper.getRandomNumberBetween(1, 2);
                              } else {
                                 player.possibleCatch.setOriginalTrainer(ep);
                                 player.possibleCatch.setCaughtBall(pokeball.type);
                                 EnumTriBoolean result = pps.addSilently(player.possibleCatch);
                                 if (result == EnumTriBoolean.NULL) {
                                    shakes = -1;
                                 } else if (result == EnumTriBoolean.FALSE) {
                                    sentToPC = true;
                                 }
                              }
                           }

                           if (shakes > 0 && !ep.func_184812_l_()) {
                              stack.func_190918_g(1);
                           }

                           if (shakes > 0 && shakes < 3) {
                              Pixelmon.EVENT_BUS.post(new CaptureEvent.FailedRaidCapture(ep, player.possibleCatch, this));
                           }

                           Pixelmon.network.sendTo(new UpdateRaidCatch(shakes, sentToPC), ep);
                        }
                     }
                  }
            }
         } else {
            Pixelmon.network.sendTo(new CloseRaid(), ep);
         }

      }
   }

   public void lock() {
      this.locked = true;
   }

   public void unlock() {
      this.locked = false;
   }

   public void updateAll() {
      Iterator var1 = this.players.iterator();

      while(var1.hasNext()) {
         RaidPlayer player = (RaidPlayer)var1.next();
         EntityPlayerMP ep = player.playerEntity;
         if (ep != null) {
            Pixelmon.network.sendTo(new OpenRaid(this), ep);
         }
      }

   }

   public void kickAll(EnumRaidKickReason reason) {
      Iterator var2 = this.players.iterator();

      while(var2.hasNext()) {
         RaidPlayer player = (RaidPlayer)var2.next();
         EntityPlayerMP ep = player.playerEntity;
         if (ep != null) {
            ep.func_145747_a(new TextComponentTranslation(reason.getMessage(), new Object[0]));
            Pixelmon.network.sendTo(new CloseRaid(), ep);
         }
      }

      this.players.clear();
   }

   public boolean isStarting() {
      return this.starting;
   }

   public void setStarting(boolean starting) {
      this.starting = starting;
   }

   public boolean addPlayer(int playerCap, EntityPlayerMP ep, Pokemon pokemon, int index) {
      Iterator var5 = this.players.iterator();

      RaidPlayer player;
      do {
         if (!var5.hasNext()) {
            if (!this.locked && !this.starting && this.players.size() < playerCap) {
               if (!Pixelmon.EVENT_BUS.post(new JoinRaidEvent(this, ep, pokemon))) {
                  this.players.add(new RaidPlayer(index, ep, pokemon));
                  if (this.players.size() == 1) {
                     this.owner = ep.func_110124_au();
                     this.locked = true;
                  }

                  this.updateAll();
               }

               return true;
            }

            return false;
         }

         player = (RaidPlayer)var5.next();
      } while(!ep.func_110124_au().equals(player.player));

      this.updateAll();
      return true;
   }

   public boolean removePlayer(EntityPlayerMP ep) {
      boolean removed = false;
      boolean shouldReset = false;
      Iterator iterator = this.players.iterator();

      while(iterator.hasNext()) {
         RaidPlayer player = (RaidPlayer)iterator.next();
         if (ep.func_110124_au().equals(player.player)) {
            iterator.remove();
            removed = true;
            if (player.player.equals(this.owner)) {
               shouldReset = true;
            }
         }
      }

      if (shouldReset) {
         this.locked = false;
         this.kickAll(EnumRaidKickReason.LEADER_LEFT);
      } else {
         this.updateAll();
      }

      Pixelmon.network.sendTo(new CloseRaid(), ep);
      return removed;
   }

   public boolean updatePlayer(EntityPlayerMP ep, Pokemon pokemon, int index) {
      Iterator var4 = this.players.iterator();

      RaidPlayer player;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         player = (RaidPlayer)var4.next();
      } while(!ep.func_110124_au().equals(player.player));

      player.updatePokemon(index, pokemon);
      this.updateAll();
      return true;
   }

   public RaidPlayer getPlayer(UUID uuid) {
      Iterator var2 = this.players.iterator();

      RaidPlayer player;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         player = (RaidPlayer)var2.next();
      } while(!uuid.equals(player.player));

      return player;
   }

   public RaidPlayer getPlayer(EntityPlayerMP ep) {
      return this.getPlayer(ep.func_110124_au());
   }

   public Optional getDenEntity(World world) {
      Entity entity = world.func_73045_a(this.getDen());
      return Optional.ofNullable(entity instanceof EntityDen ? (EntityDen)entity : null);
   }

   public boolean isOwner(UUID uuid) {
      return uuid.equals(this.owner);
   }

   public void onUpdate(EntityDen den) {
      boolean shouldReset = false;
      Iterator iterator = this.players.iterator();

      while(true) {
         RaidPlayer player;
         do {
            do {
               do {
                  if (!iterator.hasNext()) {
                     if (shouldReset) {
                        this.kickAll(EnumRaidKickReason.LEADER_LEFT);
                     } else if (this.isStarting()) {
                        if (this.tick == 0) {
                           this.populateWithNPCs();
                        }

                        ++this.tick;
                        if (this.tick % 20 == 0) {
                           this.updateAll();
                        }

                        if (this.tick >= 100) {
                           this.startRaid(den);
                        }
                     }

                     return;
                  }

                  player = (RaidPlayer)iterator.next();
               } while(player.playerEntity == null);
            } while(!player.playerEntity.func_193105_t());

            iterator.remove();
         } while(this.owner != null && !this.owner.equals(player.player));

         shouldReset = true;
      }
   }

   public void populateWithNPCs() {
      int maxLevel = 1;

      RaidPlayer player;
      for(Iterator var2 = this.players.iterator(); var2.hasNext(); maxLevel = Math.max(maxLevel, player.level)) {
         player = (RaidPlayer)var2.next();
      }

      int npcs = 4 - this.players.size();

      for(int i = 0; i < npcs; ++i) {
         this.players.add(new RaidPlayer(RaidRandomizer.getRandomAlly(this), maxLevel));
      }

      this.updateAll();
   }

   public void startRaid(EntityDen den) {
      boolean canStart = true;
      BattleParticipant[] allies = new BattleParticipant[4];
      Set trainers = new HashSet();
      int i = 0;

      Iterator raidPixelmon;
      try {
         for(raidPixelmon = this.players.iterator(); raidPixelmon.hasNext(); ++i) {
            RaidPlayer player = (RaidPlayer)raidPixelmon.next();
            if (player.playerEntity != null) {
               Pixelmon.network.sendTo(new CloseRaid(), player.playerEntity);
               PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player.playerEntity);
               Pokemon pokemon = pps.get(player.uuid);
               if (pokemon == null) {
                  canStart = false;
                  break;
               }

               allies[i] = new PlayerParticipant(true, player.playerEntity, new EntityPixelmon[]{pokemon.getOrSpawnPixelmon(player.playerEntity)});
            } else {
               int index = i % 4;
               NPCTrainer trainer = new NPCTrainer(den.field_70170_p);
               int[] offset = npcOffsets[index];
               trainer.func_70634_a(den.field_70165_t + (double)offset[0], den.field_70163_u, den.field_70161_v + (double)offset[1]);
               BaseTrainer base = ServerNPCRegistry.trainers.getRandomBase();
               trainer.init(base);
               trainer.setAIMode(EnumTrainerAI.StandStill);
               trainer.initAI();
               trainer.clearGreetings();
               trainer.setName(player.name);
               trainer.canEngage = false;
               MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
               server.func_152344_a(() -> {
                  den.field_70170_p.func_72838_d(trainer);
                  trainer.getEntityData().func_74757_a("IsRaidTrainer", true);
                  trainer.field_70177_z = npcRotations[index];
                  trainer.func_70080_a(trainer.field_70165_t, trainer.field_70163_u, trainer.field_70161_v, npcRotations[index], 0.0F);
                  trainer.func_70634_a(trainer.field_70165_t, trainer.field_70163_u, trainer.field_70161_v);
               });

               for(int j = 0; j < 6; ++j) {
                  trainer.getPokemonStorage().set(j, j == 0 ? player.pokemon : null);
               }

               trainers.add(trainer);
               allies[i] = new TrainerParticipant(trainer, 1);
            }
         }
      } catch (Exception var15) {
         Pixelmon.LOGGER.error("Failed to start raid due to error, catching and continuing safely...");
         var15.printStackTrace();
         canStart = false;
      }

      if (canStart) {
         raidPixelmon = null;

         try {
            boolean canContinue = true;
            BattleParticipant[] var19 = allies;
            int var23 = allies.length;

            for(int var25 = 0; var25 < var23; ++var25) {
               BattleParticipant ally = var19[var25];
               if (ally == null) {
                  canContinue = false;
                  break;
               }

               if (ally instanceof TrainerParticipant || ally instanceof PlayerParticipant) {
                  if (ally instanceof PlayerParticipant) {
                     PlayerParticipant pp = (PlayerParticipant)ally;
                     if (pp.player.func_193105_t()) {
                        canContinue = false;
                        break;
                     }
                  }

                  if (ally.getEntity() == null || ally.allPokemon == null || ally.allPokemon.length <= 0 || ally.allPokemon[0] == null) {
                     canContinue = false;
                     break;
                  }
               }
            }

            if (canContinue) {
               Pokemon raidEnemy = Pixelmon.pokemonFactory.create(this.getSpecies());
               if (this.getForm() != null) {
                  raidEnemy.setForm(this.getForm());
               }

               EntityPixelmon raidPixelmon = raidEnemy.getOrSpawnPixelmon(den.field_70170_p, den.field_70165_t, den.field_70163_u + 3.0, den.field_70161_v);
               if (this.getForm() != null) {
                  raidPixelmon.setForm(this.getForm());
               }

               RaidPixelmonParticipant rpp = new RaidPixelmonParticipant(this, RaidSettings.builder().setStars(this.stars).build(), raidPixelmon);
               if (!Pixelmon.EVENT_BUS.post(new StartRaidEvent(den, this, allies, raidPixelmon, rpp))) {
                  BattleRegistry.startBattle(allies, new BattleParticipant[]{rpp}, EnumBattleType.Raid);
                  den.setInUseRaidData();
                  den.clearData();
                  return;
               }

               raidPixelmon.unloadEntity();
            }
         } catch (Exception var14) {
            Pixelmon.LOGGER.error("Failed to start raid battle due to error, catching and continuing safely...");
            var14.printStackTrace();
            if (raidPixelmon != null) {
               raidPixelmon.unloadEntity();
            }
         }
      }

      raidPixelmon = trainers.iterator();

      while(raidPixelmon.hasNext()) {
         NPCTrainer trainer = (NPCTrainer)raidPixelmon.next();
         trainer.func_70106_y();
      }

      this.locked = false;
      this.starting = false;
      this.tick = 0;
      this.kickAll(EnumRaidKickReason.INVALID_POKEMON);
   }

   public void writeToNBT(NBTTagCompound tag) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74768_a("Tick", this.tick);
      nbt.func_74768_a("EntityID", this.entityID);
      nbt.func_74777_a("Stars", (short)this.stars);
      nbt.func_74777_a("Species", (short)this.species.ordinal());
      nbt.func_74757_a("HasForm", this.form != null);
      if (this.form != null) {
         nbt.func_74774_a("Form", this.form.getForm());
      }

      nbt.func_74757_a("Locked", this.locked);
      nbt.func_74757_a("Starting", this.starting);
      if (this.owner != null) {
         nbt.func_186854_a("Owner", this.owner);
      }

      NBTTagList players = new NBTTagList();
      Iterator var4 = this.players.iterator();

      while(var4.hasNext()) {
         RaidPlayer player = (RaidPlayer)var4.next();
         players.func_74742_a(player.writeToNBT());
      }

      nbt.func_74782_a("Players", players);
      nbt.func_74757_a("CanAllCatch", this.canAllCatch);
      nbt.func_74757_a("CanUseMaster", this.canUseMaster);
      tag.func_74782_a("RaidData", tag);
   }

   public void writeToByteBuf(ByteBuf buf) {
      buf.writeInt(this.tick);
      buf.writeInt(this.entityID);
      buf.writeShort(this.stars);
      buf.writeShort(this.species.ordinal());
      buf.writeBoolean(this.form != null);
      if (this.form != null) {
         buf.writeByte(this.form.getForm());
      }

      buf.writeBoolean(this.locked);
      buf.writeBoolean(this.starting);
      buf.writeBoolean(this.owner != null);
      if (this.owner != null) {
         UUIDHelper.writeUUID(this.owner, buf);
      }

      buf.writeInt(this.players.size());
      Iterator var2 = this.players.iterator();

      while(var2.hasNext()) {
         RaidPlayer player = (RaidPlayer)var2.next();
         player.writeToByteBuf(buf);
      }

      buf.writeBoolean(this.canAllCatch);
      buf.writeBoolean(this.canUseMaster);
   }

   public static class RaidPlayer {
      public final UUID player;
      public String name;
      public UUID uuid;
      public EnumSpecies species;
      public Gender gender;
      public IEnumForm form;
      public boolean shiny;
      public int index;
      public EntityPlayerMP playerEntity;
      public int level;
      public Pokemon possibleCatch = null;
      public boolean canCatch = true;
      public boolean catchAttempted = false;
      public ArrayList drops;
      public Pokemon pokemon;

      public RaidPlayer(int index, EntityPlayerMP player, Pokemon pokemon) {
         this.player = player.func_110124_au();
         this.playerEntity = player;
         this.name = player.func_70005_c_();
         this.updatePokemon(index, pokemon);
      }

      public RaidPlayer(RaidPokemon pokemon, int maxLevel) {
         this.player = null;
         this.playerEntity = null;
         this.pokemon = pokemon.makePokemon(Math.max(1, (int)Math.floor((double)maxLevel * 0.7)));
         this.name = pokemon.getName();
         this.updatePokemon(0, this.pokemon);
      }

      public void updatePokemon(int index, Pokemon pokemon) {
         this.uuid = pokemon.getUUID();
         this.species = pokemon.getSpecies();
         this.gender = pokemon.getGender();
         this.form = pokemon.getFormEnum();
         this.shiny = pokemon.isShiny();
         this.level = pokemon.getLevel();
         this.index = index;
      }

      public RaidPlayer(NBTTagCompound tag) {
         if (tag.func_186855_b("player")) {
            this.player = tag.func_186857_a("player");
         } else {
            this.player = null;
         }

         this.uuid = tag.func_186857_a("uuid");
         this.species = EnumSpecies.values()[tag.func_74765_d("species")];
         this.gender = Gender.values()[tag.func_74765_d("gender")];
         this.form = this.species.getFormEnum(tag.func_74771_c("form"));
         this.shiny = tag.func_74767_n("shiny");
         this.index = tag.func_74762_e("index");
      }

      public RaidPlayer(ByteBuf buf) {
         if (buf.readBoolean()) {
            this.player = UUIDHelper.readUUID(buf);
         } else {
            this.player = null;
         }

         this.uuid = UUIDHelper.readUUID(buf);
         this.species = EnumSpecies.values()[buf.readShort()];
         this.gender = Gender.values()[buf.readShort()];
         this.form = this.species.getFormEnum(buf.readByte());
         this.shiny = buf.readBoolean();
         this.index = buf.readInt();
      }

      public boolean isPlayer() {
         return this.player != null && this.playerEntity != null;
      }

      public void winRaid(RaidPixelmonParticipant rpp, RaidData raid, RaidGovernor governor) {
         if (this.isPlayer()) {
            PixelmonWrapper pw = rpp.getWrapper();
            RaidDropsEvent dropsEvent = new RaidDropsEvent(raid, this, governor, governor.getSettings().getCatchablePokemon(pw.pokemon), governor.getSettings().getDrops(pw.pokemon.getBaseStats().getTypeList()), governor.settings.canCatch && (raid.isOwner(this.player) || raid.canAllCatch));
            Pixelmon.EVENT_BUS.post(dropsEvent);
            this.possibleCatch = dropsEvent.getPossibleCatch();
            this.canCatch = dropsEvent.canCatch();
            this.drops = dropsEvent.getDrops();
            rpp.bc.getPlayer((EntityPlayer)this.playerEntity).sendMessage(new RaidDropsTask(this.canCatch, raid, governor.settings.shiny, this));
            Iterator var6 = this.drops.iterator();

            while(var6.hasNext()) {
               ItemStack drop = (ItemStack)var6.next();
               if (!this.playerEntity.func_191521_c(drop)) {
                  this.playerEntity.func_71019_a(drop, true);
               }
            }
         }

      }

      public int tryCatch(RaidData raid, EnumPokeballs ball) {
         if (this.playerEntity != null && this.possibleCatch != null && this.canCatch) {
            int catchRate = PokeballTypeHelper.modifyCaptureRate(ball, this.possibleCatch, this.possibleCatch.getBaseStats().getCatchRate());
            double ballBonus = PokeballTypeHelper.getBallBonus(ball, this.playerEntity, this.possibleCatch, EnumPokeBallMode.empty);
            CaptureEvent.StartRaidCapture event = new CaptureEvent.StartRaidCapture(this.playerEntity, this.possibleCatch, raid, catchRate, ballBonus);
            if (!Pixelmon.EVENT_BUS.post(event) && !this.possibleCatch.getBonusStats().preventsCapture()) {
               if (ball != EnumPokeballs.MasterBall && ball != EnumPokeballs.ParkBall) {
                  int passedShakes = 0;
                  catchRate = event.getCatchRate();
                  ballBonus = event.getBallBonus();
                  if (catchRate > 0) {
                     float hpMax = (float)this.possibleCatch.getMaxHealth();
                     float hpCurrent = Math.max(1.0F, hpMax * PixelmonConfig.getRaidCatchHealthPercentage(raid.stars));
                     double bonusStatus = 1.0;
                     double a = (double)((3.0F * hpMax - 2.0F * hpCurrent) * (float)catchRate) * ballBonus / (double)(3.0F * hpMax) * bonusStatus;
                     double b = (double)Math.round(Math.pow(255.0 / a, 0.25) * 4096.0) / 4096.0;
                     b = Math.floor(65536.0 / b);
                     if (b != 0.0) {
                        if (!(a < 255.0)) {
                           return 3;
                        }

                        for(int i = 0; i < 4; ++i) {
                           int roll = RandomHelper.rand.nextInt(65536);
                           if ((double)roll <= b) {
                              ++passedShakes;
                           }
                        }
                     }
                  }

                  return Math.max(1, passedShakes - 1);
               } else {
                  return 3;
               }
            } else {
               return 1;
            }
         } else {
            return RandomHelper.getRandomNumberBetween(1, 2);
         }
      }

      public NBTTagCompound writeToNBT() {
         NBTTagCompound nbt = new NBTTagCompound();
         this.writeToNBT(nbt);
         return nbt;
      }

      public void writeToNBT(NBTTagCompound tag) {
         if (this.player != null) {
            tag.func_186854_a("player", this.player);
         }

         tag.func_186854_a("uuid", this.uuid);
         tag.func_74777_a("species", (short)this.species.ordinal());
         tag.func_74777_a("gender", (short)this.gender.ordinal());
         tag.func_74774_a("form", this.form.getForm());
         tag.func_74757_a("shiny", this.shiny);
         tag.func_74768_a("index", this.index);
      }

      public void writeToByteBuf(ByteBuf buf) {
         buf.writeBoolean(this.player != null);
         if (this.player != null) {
            UUIDHelper.writeUUID(this.player, buf);
         }

         UUIDHelper.writeUUID(this.uuid, buf);
         buf.writeShort(this.species.ordinal());
         buf.writeShort(this.gender.ordinal());
         buf.writeByte(this.form.getForm());
         buf.writeBoolean(this.shiny);
         buf.writeInt(this.index);
      }
   }
}
