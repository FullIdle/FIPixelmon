package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.IPokemonOwner;
import com.pixelmonmod.pixelmon.blocks.ranch.BreedingConditions;
import com.pixelmonmod.pixelmon.blocks.ranch.RanchBounds;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.EnumRanchClientPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.RanchBlockClientPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBreedingStrength;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.EnumCheatItemType;
import com.pixelmonmod.pixelmon.items.ItemIsisHourglass;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.Bounds;
import com.pixelmonmod.pixelmon.util.helpers.BreedLogic;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRanchBlock extends TileEntity implements IPokemonOwner, ITickable {
   private static final int REFRESH_RATE = 100;
   private static final int UPDATE_BREEDING_RATE = 400;
   protected static final int ranchWidth = 4;
   protected static final int ranchLength = 4;
   protected List pokemon = Lists.newArrayList();
   protected BiMap entityMap = HashBiMap.create(2);
   protected RanchBounds ranchBounds = new RanchBounds(this);
   int tick = 0;
   private UUID ownerUUID = null;
   private Pokemon egg = null;
   public boolean aboveGround = false;
   public int percentAbove = 0;
   private EnumSpecies species = null;

   public void func_73660_a() {
      ++this.tick;
      if (!this.field_145850_b.field_72995_K) {
         if (this.tick % 100 == 0 && this.isRanchOwnerInGame()) {
            EntityPlayerMP player = (EntityPlayerMP)this.field_145850_b.func_152378_a(this.ownerUUID);
            this.trySpawnPokes(player);
         }

         if (this.tick % 400 == 0 && this.isRanchOwnerInGame() && this.entityMap.size() == 2 && BreedLogic.canBreed(((EntityBreeding)this.entityMap.get(this.pokemon.get(0))).getPokemonData(), ((EntityBreeding)this.entityMap.get(this.pokemon.get(1))).getPokemonData())) {
            try {
               this.pokemon.forEach(this::updateBreeding);
               this.func_70296_d();
            } catch (ConcurrentModificationException var2) {
            }
         }
      }

      this.checkAboveGround();
   }

   public void updateBreeding(RanchPoke poke) {
      if (poke.breedingLevel > PixelmonConfig.numBreedingLevels) {
         poke.breedingLevel = PixelmonConfig.numBreedingLevels;
      } else {
         EntityBreeding pixelmon = (EntityBreeding)this.entityMap.get(poke);
         if (pixelmon == null) {
            return;
         }

         this.refreshBreedingStrength(pixelmon);
         if (this.egg != null) {
            return;
         }

         if (poke.breedingStrength == EnumBreedingStrength.NONE) {
            poke.lastBreedingTime = this.field_145850_b.func_82737_E();
            if (poke.breedingLevel > 0) {
               BreedEvent.BreedingLevelChanged event = new BreedEvent.BreedingLevelChanged(pixelmon.func_184753_b(), this, pixelmon, poke.breedingLevel, poke.breedingLevel - 1);
               Pixelmon.EVENT_BUS.post(event);
               poke.breedingLevel = event.getNewLevel();
            }
         } else {
            long currentTime = this.field_145850_b.func_82737_E();
            int breedingTicks = PixelmonConfig.breedingTicks;
            boolean ovalCharm = false;
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(pixelmon.func_184753_b());
            if (pps != null && pps.getOvalCharm().isActive()) {
               ovalCharm = true;
               breedingTicks = (int)((float)breedingTicks * PixelmonConfig.ovalCharmMultiplier);
            }

            BreedEvent.BreedingTicks ticksEvent = new BreedEvent.BreedingTicks(pixelmon.func_184753_b(), this, pixelmon, breedingTicks, ovalCharm);
            Pixelmon.EVENT_BUS.post(ticksEvent);
            breedingTicks = ticksEvent.getBreedingTicks();

            while((float)(currentTime - poke.lastBreedingTime) - (float)breedingTicks / poke.breedingStrength.value >= 0.0F) {
               BreedEvent.BreedingLevelChanged levelChangedEvent = new BreedEvent.BreedingLevelChanged(pixelmon.func_184753_b(), this, pixelmon, poke.breedingLevel, poke.breedingLevel + 1);
               Pixelmon.EVENT_BUS.post(levelChangedEvent);
               poke.breedingLevel = levelChangedEvent.getNewLevel();
               poke.lastBreedingTime = (long)((float)poke.lastBreedingTime + (float)breedingTicks / poke.breedingStrength.value);
               if (poke.breedingLevel > PixelmonConfig.numBreedingLevels) {
                  break;
               }
            }

            if (poke.breedingLevel >= PixelmonConfig.numBreedingLevels) {
               this.updateStatus();
            }
         }

         if (pixelmon.getNumBreedingLevels() != poke.breedingLevel) {
            pixelmon.func_184212_Q().func_187227_b(EntityBreeding.dwNumBreedingLevels, (byte)poke.breedingLevel);
         }
      }

   }

   public void updateStatus() {
      if (this.egg == null) {
         if (this.entityMap.size() == 2) {
            Pokemon parent1 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(0))).getPokemonData();
            Pokemon parent2 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(1))).getPokemonData();
            if (BreedLogic.canBreed(parent1, parent2) && ((RanchPoke)this.pokemon.get(0)).readyToMakeEgg() && ((RanchPoke)this.pokemon.get(1)).readyToMakeEgg()) {
               Pokemon pokemon = BreedLogic.makeEgg(parent1, parent2);
               BreedEvent.MakeEgg eggEvent = new BreedEvent.MakeEgg(parent1.getOwnerPlayerUUID(), this, pokemon, parent1, parent2);
               if (!Pixelmon.EVENT_BUS.post(eggEvent)) {
                  pokemon = eggEvent.getEgg();
                  this.egg = pokemon;
               }

               ((RanchPoke)this.pokemon.get(0)).resetBreedingLevel(this.field_145850_b);
               ((RanchPoke)this.pokemon.get(1)).resetBreedingLevel(this.field_145850_b);
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
               this.func_70296_d();
            }
         } else {
            this.pokemon.forEach((p) -> {
               p.resetBreedingLevel(this.field_145850_b);
            });
         }

         this.func_145831_w().func_175685_c(this.field_174879_c, this.func_145838_q(), true);
      }
   }

   public void refreshBreedingStrength(EntityBreeding pixelmon) {
      if (pixelmon != null) {
         float breedingStrengthFloat;
         if (PixelmonConfig.useBreedingEnvironment) {
            BreedingConditions conditions = ((RanchBounds)this.getBounds()).getContainingBreedingConditions(this.field_145850_b);
            breedingStrengthFloat = conditions.getBreedingStrength(pixelmon.getBaseStats().types);
         } else {
            breedingStrengthFloat = 2.0F;
         }

         RanchBounds bounds = (RanchBounds)this.getBounds();
         BreedEvent.EnvironmentStrength strengthEvent = new BreedEvent.EnvironmentStrength(pixelmon.func_184753_b(), this, pixelmon, bounds, breedingStrengthFloat);
         Pixelmon.EVENT_BUS.post(strengthEvent);
         ((RanchPoke)this.entityMap.inverse().get(pixelmon)).breedingStrength = EnumBreedingStrength.of(strengthEvent.breedingStrength);
      }
   }

   public void onActivate(EntityPlayerMP player) {
      if (!PixelmonConfig.allowBreeding) {
         ChatHandler.sendChat(player, "pixelmon.general.disabledblock");
      } else if (this.tick >= 100) {
         this.updateStatus();
         PCStorage pc = Pixelmon.storageManager.getPCForPlayer(player);
         Pixelmon.network.sendTo(new RanchBlockClientPacket(this, EnumRanchClientPacketMode.ViewBlock), player);
         Pixelmon.network.sendTo(new ClientChangeOpenPC(pc.uuid), player);
         OpenScreen.open(player, EnumGuiScreen.RanchBlock, this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p());
      }
   }

   public boolean onActivate(EntityPlayer player, EntityBreeding pixelmon, EnumHand hand) {
      this.updateStatus();
      if (this.pokemon.size() > 1) {
         ItemStack itemStack = player.func_184586_b(hand);
         RanchPoke poke;
         Pokemon parent2;
         if (!itemStack.func_190926_b()) {
            Item item = itemStack.func_77973_b();
            if (item instanceof ItemIsisHourglass && ((ItemIsisHourglass)item).type == EnumCheatItemType.Silver) {
               poke = (RanchPoke)this.entityMap.inverse().get(pixelmon);
               if (poke.breedingStrength != EnumBreedingStrength.NONE && !this.hasEgg() && poke.breedingLevel < PixelmonConfig.numBreedingLevels && this.entityMap.size() > 1 && this.entityMap.get(this.pokemon.get(0)) != null && this.entityMap.get(this.pokemon.get(1)) != null) {
                  Pokemon parent1 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(0))).getPokemonData();
                  parent2 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(1))).getPokemonData();
                  if (parent1 != null && parent2 != null && BreedLogic.canBreed(parent1, parent2)) {
                     BreedEvent.BreedingLevelChanged levelChangedEvent = new BreedEvent.BreedingLevelChanged(this.ownerUUID, this, (EntityBreeding)this.entityMap.get(poke), poke.breedingLevel, poke.breedingLevel + 1);
                     Pixelmon.EVENT_BUS.post(levelChangedEvent);
                     poke.breedingLevel = levelChangedEvent.getNewLevel();
                     poke.lastBreedingTime = this.field_145850_b.func_82737_E();
                     player.func_145747_a(new TextComponentTranslation("ranch.hourglass.upgrade", new Object[]{pixelmon.getNickname()}));
                     if (!player.field_71075_bZ.field_75098_d) {
                        player.func_184586_b(hand).func_190918_g(1);
                     }

                     return true;
                  }
               }

               return false;
            }
         }

         RanchPoke poke = (RanchPoke)this.entityMap.inverse().get(pixelmon);
         poke = this.pokemon.get(0) == poke ? (RanchPoke)this.pokemon.get(1) : (RanchPoke)this.pokemon.get(0);
         EntityBreeding otherPixelmon = (EntityBreeding)this.entityMap.get(poke);
         if (otherPixelmon != null) {
            parent2 = pixelmon.getPokemonData();
            Pokemon parent2 = ((EntityBreeding)this.entityMap.get(poke)).getPokemonData();
            if (parent2 != null && parent2 != null && !BreedLogic.canBreed(parent2, parent2)) {
               player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.notcompatible", new Object[]{pixelmon.getNickname()}));
               return true;
            } else {
               if (poke.breedingStrength == EnumBreedingStrength.NONE) {
                  this.refreshBreedingStrength(pixelmon);
                  this.refreshBreedingStrength(otherPixelmon);
               }

               if (poke.breedingLevel >= PixelmonConfig.numBreedingLevels) {
                  player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.maxaffection", new Object[]{pixelmon.getNickname(), otherPixelmon.getNickname()}));
               } else {
                  player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.level" + poke.breedingStrength.ordinal(), new Object[]{pixelmon.getNickname(), otherPixelmon.getNickname()}));
               }

               return true;
            }
         } else {
            player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.notcompatible", new Object[]{pixelmon.getNickname()}));
            return true;
         }
      } else {
         player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.nopartner", new Object[]{pixelmon.getNickname()}));
         return true;
      }
   }

   public Collection getEntities() {
      return this.entityMap.values();
   }

   public boolean canBreed() {
      if (this.entityMap.size() == 2 && this.pokemon.size() == 2) {
         Pokemon parent1 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(0))).getPokemonData();
         Pokemon parent2 = ((EntityBreeding)this.entityMap.get(this.pokemon.get(1))).getPokemonData();
         if (BreedLogic.canBreed(parent1, parent2)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasEgg() {
      return this.egg != null || this.species != null;
   }

   public boolean claimEgg(EntityPlayerMP player) {
      if (this.egg != null) {
         BreedEvent.CollectEgg collectEggEvent = new BreedEvent.CollectEgg(player.func_110124_au(), this, this.egg);
         if (!Pixelmon.EVENT_BUS.post(collectEggEvent)) {
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
            storage.add(collectEggEvent.getEgg());
            this.egg = null;
            this.pokemon.forEach((p) -> {
               p.resetBreedingLevel(this.field_145850_b);
            });
            this.updateStatus();
            this.func_70296_d();
            ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            return true;
         }
      }

      return false;
   }

   public void addPokemon(EntityPlayerMP player, UUID uuid, StoragePosition position) {
      try {
         PCStorage storage = Pixelmon.storageManager.getPCForPlayer(player);
         Pokemon pokemon = storage.get(position);
         if (pokemon == null || !pokemon.getUUID().equals(uuid) || pokemon.isInRanch()) {
            boolean isNull = pokemon == null;
            boolean isInvalid = pokemon != null && !pokemon.getUUID().equals(uuid);
            boolean isInRanch = pokemon != null && pokemon.isInRanch();
            Pixelmon.LOGGER.info("TileEntityRanchBlock " + player.func_70005_c_() + " " + player.func_110124_au() + " " + this.field_174879_c + ": Attempted to add Pokemon to ranch that was not valid. isNull = " + isNull + ", isInvalid = " + isInvalid + ", isInRanch = " + isInRanch);
            return;
         }

         BreedEvent.AddPokemon event = new BreedEvent.AddPokemon(player, this, pokemon);
         if (Pixelmon.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
            pokemon.setInRanch(false);
            return;
         }

         if (PixelmonConfig.maxCumulativePokemonInRanch > 0) {
            int count = storage.findAll(Pokemon::isInRanch).size();
            if (count + 1 > PixelmonConfig.maxCumulativePokemonInRanch && event.getResult() != Result.ALLOW) {
               player.func_145747_a(new TextComponentTranslation("pixelmon.ranch.maxcumulativepokemon", new Object[0]));
               pokemon.setInRanch(false);
               return;
            }
         }

         pokemon.setInRanch(true);
         pokemon.ifEntityExists(EntityPixelmon::retrieve);
         this.pokemon.add(new RanchPoke(uuid, position));
         this.pokemon.forEach((p) -> {
            p.resetBreedingLevel(this.field_145850_b);
         });
         this.trySpawnPokes(player);
         this.func_70296_d();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void removePokemon(EntityPlayerMP player, UUID uuid, StoragePosition position) {
      try {
         Iterator iterator = this.pokemon.iterator();

         while(iterator.hasNext()) {
            RanchPoke poke = (RanchPoke)iterator.next();
            if (Objects.equals(poke.pos, position)) {
               PCStorage storage = Pixelmon.storageManager.getPCForPlayer(player);
               Pokemon pokemon = poke.getPokemon(storage);
               if (pokemon == null || !Objects.equals(poke.uuid, uuid)) {
                  Pixelmon.LOGGER.info("Remove pokemon called with null or invalid pokemon. (But we're continuing to remove it)");
               }

               EntityBreeding breeding = (EntityBreeding)this.entityMap.remove(poke);
               if (breeding != null) {
                  breeding.field_70170_p.func_72900_e(breeding);
               }

               iterator.remove();
               this.pokemon.forEach((p) -> {
                  p.resetBreedingLevel(this.field_145850_b);
               });
               this.func_70296_d();
               pokemon.setInRanch(false);
               break;
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void removePokemon(EntityPlayerMP player, EntityBreeding pixelmon) {
      try {
         Iterator iterator = this.entityMap.keySet().iterator();

         while(true) {
            RanchPoke poke;
            do {
               if (!iterator.hasNext()) {
                  return;
               }

               poke = (RanchPoke)iterator.next();
            } while(this.entityMap.get(poke) != pixelmon && ((EntityBreeding)this.entityMap.get(poke)).getPokemonData() != pixelmon.getPokemonData());

            Pixelmon.LOGGER.warn("Forcibly removing pokemon from ranch block at " + this.field_174879_c);
            this.pokemon.remove(poke);
            pixelmon.func_70106_y();
            iterator.remove();
            this.pokemon.forEach((p) -> {
               p.resetBreedingLevel(this.field_145850_b);
            });
            PCStorage storage = Pixelmon.storageManager.getPCForPlayer(player);
            this.func_70296_d();
            poke.getPokemon(storage).setInRanch(false);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public void removeAllPokemon() {
      for(Iterator iterator = this.pokemon.iterator(); iterator.hasNext(); this.func_70296_d()) {
         RanchPoke poke = (RanchPoke)iterator.next();
         EntityBreeding pixelmon = (EntityBreeding)this.entityMap.remove(poke);
         if (pixelmon != null) {
            pixelmon.func_70106_y();
         }

         iterator.remove();
         PCStorage storage = Pixelmon.storageManager.getPCForPlayer(this.getOwnerUUID());
         Pokemon pokemon = poke.getPokemon(storage);
         if (pokemon != null) {
            pokemon.setInRanch(false);
         }
      }

   }

   public boolean applyHourglass() {
      boolean hourglassused = false;
      Iterator var2 = this.pokemon.iterator();

      while(var2.hasNext()) {
         RanchPoke poke = (RanchPoke)var2.next();
         if (poke.breedingStrength != EnumBreedingStrength.NONE && poke.breedingLevel < PixelmonConfig.numBreedingLevels) {
            hourglassused = true;
            BreedEvent.BreedingLevelChanged levelChangedEvent = new BreedEvent.BreedingLevelChanged(this.ownerUUID, this, (EntityBreeding)this.entityMap.get(poke), poke.breedingLevel, poke.breedingLevel + 1);
            Pixelmon.EVENT_BUS.post(levelChangedEvent);
            poke.breedingLevel = levelChangedEvent.getNewLevel();
            poke.lastBreedingTime = this.field_145850_b.func_82737_E();
         }
      }

      return hourglassused;
   }

   private void trySpawnPokes(EntityPlayerMP player) {
      if (player != null) {
         PCStorage storage = Pixelmon.storageManager.getPCForPlayer(player);
         Iterator iterator = this.pokemon.iterator();

         while(true) {
            while(iterator.hasNext()) {
               RanchPoke poke = (RanchPoke)iterator.next();
               Pokemon pokemon = poke.getPokemon(storage);
               if (pokemon != null && pokemon.isInRanch()) {
                  EntityBreeding pixelmon;
                  if (this.entityMap.containsKey(poke)) {
                     pixelmon = (EntityBreeding)this.entityMap.get(poke);
                     if (!pixelmon.field_70128_L && !(pixelmon.func_110143_aJ() <= 0.0F) && pixelmon.field_70170_p.func_73045_a(pixelmon.func_145782_y()) != null) {
                        if (!this.ranchBounds.isIn(pixelmon.func_174791_d())) {
                        }
                     } else {
                        this.entityMap.remove(poke);
                     }
                  }

                  if (!this.entityMap.containsKey(poke)) {
                     if (pokemon.getHealth() <= 0) {
                        pokemon.setHealth(1);
                     }

                     pixelmon = new EntityBreeding(player.field_70170_p);
                     pixelmon.setPokemon(pokemon);
                     pixelmon.setRanchBlockOwner(this);
                     if (this.setLocationForEntity(pixelmon)) {
                        pixelmon.field_70170_p.func_72838_d(pixelmon);
                        this.entityMap.put(poke, pixelmon);
                        this.refreshBreedingStrength(pixelmon);
                     }
                  }
               } else {
                  this.entityMap.remove(poke);
                  Optional.ofNullable(storage.findOne((p) -> {
                     return Objects.equals(p.getUUID(), poke.uuid);
                  })).ifPresent((p) -> {
                     p.setInRanch(false);
                  });
                  PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
                  Optional.ofNullable(party.findOne((p) -> {
                     return Objects.equals(p.getUUID(), poke.uuid);
                  })).ifPresent((p) -> {
                     p.setInRanch(false);
                  });
                  iterator.remove();
                  Pixelmon.LOGGER.info("Removed pokemon from ranchblock at " + this.field_174879_c + " cause it was not in the correct storage pos or was not marked as in a ranch block.");
               }
            }

            if (this.hasEgg()) {
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            }

            return;
         }
      }
   }

   public List getPokemonData() {
      return this.pokemon;
   }

   public void setPokemonData(List pokemon) {
      this.pokemon = pokemon;
   }

   public int getEntityCount() {
      return this.entityMap.size();
   }

   public Bounds getBounds() {
      return this.ranchBounds;
   }

   public void setInitBounds() {
      this.ranchBounds = new RanchBounds(this, this.field_174879_c.func_177952_p() + 4, this.field_174879_c.func_177958_n() - 4, this.field_174879_c.func_177952_p() - 4, this.field_174879_c.func_177958_n() + 4, this.field_174879_c.func_177956_o());
   }

   public void setOwner(EntityPlayerMP entity) {
      this.ownerUUID = entity.func_110124_au();
   }

   public UUID getOwnerUUID() {
      return this.ownerUUID;
   }

   public boolean isRanchOwnerInGame() {
      return this.ownerUUID != null && this.field_145850_b.func_152378_a(this.ownerUUID) != null;
   }

   public Pokemon getEgg() {
      return this.egg;
   }

   public EnumSpecies getEggSpecies() {
      return this.egg != null ? this.egg.getSpecies() : this.species;
   }

   @SideOnly(Side.CLIENT)
   public void setEggSpecies(EnumSpecies species) {
      this.species = species;
   }

   protected void checkAboveGround() {
      if (this.aboveGround && this.percentAbove < 100) {
         this.percentAbove += 5;
      } else if (!this.aboveGround && this.percentAbove > 0) {
         this.percentAbove -= 5;
      }

      if (this.hasEgg()) {
         this.aboveGround = true;
      } else if (this.tick % 20 == 0) {
         int x = this.field_174879_c.func_177958_n();
         int y = this.field_174879_c.func_177956_o();
         int z = this.field_174879_c.func_177952_p();
         List closePlayers = this.field_145850_b.func_72872_a(EntityPlayer.class, new AxisAlignedBB((double)(x - 5), (double)(y - 5), (double)(z - 5), (double)(x + 5), (double)(y + 5), (double)(z + 5)));
         this.aboveGround = false;
         Iterator var5 = closePlayers.iterator();

         while(var5.hasNext()) {
            EntityPlayer closePlayer = (EntityPlayer)var5.next();
            if (Objects.equals(closePlayer.func_110124_au(), this.ownerUUID)) {
               this.aboveGround = true;
               break;
            }
         }
      }

   }

   public void onDestroy() {
      if (!this.field_145850_b.field_72995_K) {
         try {
            this.removeAllPokemon();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   private boolean setLocationForEntity(EntityBreeding pixelmon) {
      for(int i = 0; i < 5; ++i) {
         int[] xz = this.ranchBounds.getRandomLocation(this.field_145850_b.field_73012_v);
         BlockPos pos = new BlockPos(xz[0], this.field_174879_c.func_177956_o() + RandomHelper.getRandomNumberBetween(0, 3), xz[1]);
         pos = pos.func_177984_a();
         pixelmon.func_70012_b((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), 0.0F, 0.0F);
         if (pixelmon.func_70601_bi()) {
            return true;
         }
      }

      return false;
   }

   public void func_145834_a(World world) {
      super.func_145834_a(world);
      this.ranchBounds.setWorldObj(world);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      NBTTagList list = new NBTTagList();
      Iterator var3 = this.pokemon.iterator();

      while(var3.hasNext()) {
         RanchPoke poke = (RanchPoke)var3.next();
         NBTTagCompound compound = new NBTTagCompound();
         poke.writeToNBT(compound);
         list.func_74742_a(compound);
      }

      nbt.func_74782_a("PokemonList", list);
      this.ranchBounds.writeToNBT(nbt);
      if (this.egg != null) {
         NBTTagCompound compound = new NBTTagCompound();
         this.egg.writeToNBT(compound);
         nbt.func_74782_a("egg", compound);
      }

      if (this.ownerUUID != null) {
         nbt.func_74772_a("UUIDMost", this.ownerUUID.getMostSignificantBits());
         nbt.func_74772_a("UUIDLeast", this.ownerUUID.getLeastSignificantBits());
      }

      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.ranchBounds.readFromNBT(nbt);
      if (nbt.func_74764_b("PokemonList")) {
         NBTTagList list = nbt.func_150295_c("PokemonList", 10);
         this.pokemon.clear();
         this.entityMap.values().forEach((pixelmon) -> {
            pixelmon.func_70106_y();
         });
         this.entityMap.clear();
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            NBTBase tag = (NBTBase)var3.next();
            NBTTagCompound compound = (NBTTagCompound)tag;
            RanchPoke poke = new RanchPoke((UUID)null, (StoragePosition)null);
            poke.readFromNBT(compound);
            this.pokemon.add(poke);
         }
      }

      if (nbt.func_74764_b("egg")) {
         this.egg = Pixelmon.pokemonFactory.create(nbt.func_74775_l("egg"));
      } else {
         this.egg = null;
      }

      if (nbt.func_150297_b("UUIDMost", 4) && nbt.func_150297_b("UUIDLeast", 4)) {
         this.ownerUUID = new UUID(nbt.func_74763_f("UUIDMost"), nbt.func_74763_f("UUIDLeast"));
      } else {
         this.ownerUUID = null;
      }

   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      super.func_189515_b(nbt);
      this.ranchBounds.writeToNBT(nbt);
      if (this.egg != null) {
         nbt.func_74768_a("eggdex", this.egg.getSpecies().getNationalPokedexInteger());
      }

      if (this.ownerUUID != null) {
         nbt.func_74772_a("UUIDMost", this.ownerUUID.getMostSignificantBits());
         nbt.func_74772_a("UUIDLeast", this.ownerUUID.getLeastSignificantBits());
      }

      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.func_145839_a(pkt.func_148857_g());
      this.ranchBounds.readFromNBT(pkt.func_148857_g());
      NBTTagCompound nbt = pkt.func_148857_g();
      if (nbt.func_74764_b("eggdex")) {
         this.species = EnumSpecies.getFromDex(nbt.func_74762_e("eggdex"));
      } else {
         this.species = null;
      }

      if (nbt.func_150297_b("UUIDMost", 4) && nbt.func_150297_b("UUIDLeast", 4)) {
         this.ownerUUID = new UUID(nbt.func_74763_f("UUIDMost"), nbt.func_74763_f("UUIDLeast"));
      } else {
         this.ownerUUID = null;
      }

      this.updateStatus();
   }

   public static class RanchPoke {
      public UUID uuid;
      public StoragePosition pos;
      int breedingLevel;
      long lastBreedingTime;
      EnumBreedingStrength breedingStrength;

      public RanchPoke(UUID uuid, StoragePosition position) {
         this.breedingStrength = EnumBreedingStrength.NONE;
         this.uuid = uuid;
         this.pos = position;
      }

      public void resetBreedingLevel(World world) {
         this.breedingLevel = 0;
         this.lastBreedingTime = world.func_82737_E();
      }

      public boolean readyToMakeEgg() {
         return this.breedingLevel >= PixelmonConfig.numBreedingLevels;
      }

      @Nullable
      public Pokemon getPokemon(PCStorage storage) {
         Pokemon pokemon = storage.get(this.pos);
         return pokemon != null && Objects.equals(pokemon.getUUID(), this.uuid) ? pokemon : null;
      }

      public void writeToNBT(NBTTagCompound compound) {
         compound.func_186854_a("uuid", this.uuid);
         compound.func_74777_a("box", (short)this.pos.box);
         compound.func_74774_a("order", (byte)this.pos.order);
         compound.func_74774_a("breedLevel", (byte)this.breedingLevel);
         compound.func_74772_a("lastBreedingTime", this.lastBreedingTime);
      }

      public void readFromNBT(NBTTagCompound compound) {
         this.uuid = compound.func_186857_a("uuid");
         this.pos = new StoragePosition(compound.func_74765_d("box"), compound.func_74771_c("order"));
         this.breedingLevel = compound.func_74771_c("breedLevel");
         this.lastBreedingTime = compound.func_74763_f("lastBreedingTime");
      }

      public boolean matches(Pokemon pokemon) {
         return pokemon != null && this.pos.equals(pokemon.getPosition()) && this.uuid.equals(pokemon.getUUID());
      }
   }
}
