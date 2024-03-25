package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.blocks.CloningCompleteEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.blocks.BlockRotation;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileEntityCloningMachine extends TileEntityDecorativeBase implements ITickable {
   public boolean processingClone = false;
   public boolean growingPokemon = false;
   public boolean isBroken = false;
   public float xboost = 0.0F;
   public float zboost = 0.0F;
   public EntityPixelmon pixelmon;
   public String pokemonName = "";
   public boolean isShiny = false;
   public Pokemon mew = null;
   public UUID owner = null;
   public int boostLevel = 0;
   public int boostCount = 0;
   public float lasPos = -2.0F;
   public int pokemonProgress = 0;
   public boolean isFinished = false;
   boolean boostSet = false;
   boolean travDown = true;
   int baseCount = 0;

   private void resetMachine() {
      this.mew = null;
      this.owner = null;
      this.isShiny = this.processingClone = this.growingPokemon = this.isFinished = false;
      this.baseCount = this.boostCount = this.boostLevel = this.pokemonProgress = 0;
      this.pokemonName = "";
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }

   public void func_73660_a() {
      if (!this.boostSet) {
         BlockRotation rot = BlockRotation.getRotationFromMetadata(this.func_145832_p());
         if (rot == BlockRotation.Normal) {
            this.xboost = (float)((double)this.xboost + 3.35);
         } else if (rot == BlockRotation.Rotate180) {
            this.xboost = (float)((double)this.xboost - 3.35);
         } else if (rot == BlockRotation.CW) {
            this.zboost = (float)((double)this.zboost - 3.35);
         } else {
            this.zboost = (float)((double)this.zboost + 3.35);
         }

         this.boostSet = true;
      }

      if (!this.field_145850_b.field_72995_K) {
         if (!this.processingClone && this.boostCount == 3 && this.hasMew()) {
            if (this.baseCount < 30) {
               ++this.baseCount;
            } else if (this.baseCount == 30) {
               this.processingClone = true;
               this.baseCount = 0;
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            }
         }

         if (this.processingClone) {
            ++this.baseCount;
            if (this.baseCount >= 280) {
               boolean madeMewtwo = Math.random() < (double)((float)this.boostLevel / 40.0F);
               if (madeMewtwo) {
                  this.pixelmon = (EntityPixelmon)PixelmonEntityList.createEntityByName("Mewtwo", this.field_145850_b);
               } else {
                  this.pixelmon = (EntityPixelmon)PixelmonEntityList.createEntityByName("Ditto", this.field_145850_b);
               }

               this.pixelmon.initAnimation();
               this.isShiny = this.pixelmon.getPokemonData().isShiny();
               this.pokemonName = this.pixelmon.getPokemonName();
               ++((MewStats)this.mew.getExtraStats(MewStats.class)).numCloned;
               this.growingPokemon = true;
               this.processingClone = false;
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            }
         }

         if (this.growingPokemon) {
            if (this.pokemonProgress < 200) {
               ++this.pokemonProgress;
            } else if (!Pixelmon.EVENT_BUS.post(new CloningCompleteEvent(this.mew, this.pixelmon, this.mew.getOwnerPlayer(), this.field_174879_c))) {
               if (this.owner != null) {
                  Pixelmon.storageManager.getParty(this.owner).add(this.mew);
               }

               this.mew = null;
               this.owner = null;
               if (this.pokemonName.equals("Mewtwo")) {
                  this.isBroken = true;
                  this.releasePokemon();
               } else {
                  this.isFinished = true;
               }

               this.growingPokemon = false;
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
               if (this.owner != null) {
                  Pixelmon.storageManager.getParty(this.owner).add(this.mew);
               }

               this.mew = null;
               this.owner = null;
               if (this.pokemonName.equals("Mewtwo")) {
                  this.isBroken = true;
                  this.releasePokemon();
               } else {
                  this.isFinished = true;
               }

               this.growingPokemon = false;
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            } else {
               --((MewStats)this.mew.getExtraStats(MewStats.class)).numCloned;
               Pixelmon.storageManager.getParty(this.owner).add(this.mew);
               this.resetMachine();
            }
         }
      } else {
         if (this.travDown) {
            this.lasPos -= 0.1F;
            if (this.lasPos < -15.0F) {
               this.travDown = false;
            }
         } else {
            this.lasPos += 0.1F;
            if (this.lasPos >= -2.0F) {
               this.travDown = true;
            }
         }

         if (this.growingPokemon && this.pokemonProgress < 200) {
            ++this.pokemonProgress;
         }

         if (this.processingClone) {
            ++this.baseCount;
            if (this.baseCount > 80 && this.baseCount < 280) {
               for(int i = 0; (float)i < 30.0F * ((float)this.baseCount - 80.0F) / 200.0F; ++i) {
                  this.field_145850_b.func_175688_a(EnumParticleTypes.REDSTONE, (double)((float)this.field_174879_c.func_177958_n() + this.xboost + this.field_145850_b.field_73012_v.nextFloat()), (double)((float)this.field_174879_c.func_177956_o() + 0.3F + this.field_145850_b.field_73012_v.nextFloat() + this.field_145850_b.field_73012_v.nextFloat()), (double)((float)this.field_174879_c.func_177952_p() + this.zboost + this.field_145850_b.field_73012_v.nextFloat()), -255.0, 1.0, 255.0, new int[0]);
               }
            }
         }
      }

      if (this.pokemonName.equals("") && this.pixelmon != null) {
         this.pixelmon = null;
      }

   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.boostCount = nbt.func_74765_d("BoostCount");
      this.boostLevel = nbt.func_74765_d("BoostLevel");
      this.isBroken = nbt.func_74767_n("IsBroken");
      this.isFinished = nbt.func_74767_n("IsFinished");
      this.processingClone = nbt.func_74767_n("ProcessingClone");
      this.growingPokemon = nbt.func_74767_n("GrowingPokemon");
      this.pokemonName = nbt.func_74779_i("PokemonName");
      this.baseCount = nbt.func_74762_e("BaseCount");
      this.pokemonProgress = nbt.func_74762_e("PokemonProgress");
      if (nbt.func_74764_b("Mew")) {
         this.mew = Pixelmon.pokemonFactory.create(nbt.func_74775_l("Mew"));
      } else {
         this.mew = null;
      }

      this.owner = nbt.func_186855_b("Owner") ? nbt.func_186857_a("Owner") : null;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74777_a("BoostCount", (short)this.boostCount);
      nbt.func_74777_a("BoostLevel", (short)this.boostLevel);
      nbt.func_74757_a("IsBroken", this.isBroken);
      nbt.func_74757_a("IsFinished", this.isFinished);
      nbt.func_74757_a("ProcessingClone", this.processingClone);
      nbt.func_74757_a("GrowingPokemon", this.growingPokemon);
      nbt.func_74778_a("PokemonName", this.pokemonName);
      nbt.func_74768_a("BaseCount", this.baseCount);
      if (this.hasMew()) {
         NBTTagCompound mewnbt = new NBTTagCompound();
         this.mew.writeToNBT(mewnbt);
         nbt.func_74782_a("Mew", mewnbt);
      }

      if (this.owner != null) {
         nbt.func_186854_a("Owner", this.owner);
      }

      nbt.func_74768_a("PokemonProgress", this.pokemonProgress);
      return nbt;
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return nbt;
   }

   public boolean hasMew() {
      return this.mew != null;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public void activate(EntityPlayer player, ItemStack heldItem) {
      if (!PixelmonConfig.cloningMachineEnabled) {
         ChatHandler.sendChat(player, "pixelmon.general.disabledblock");
      } else {
         PlayerPartyStorage storage;
         if (this.isFinished) {
            storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
            if (storage.countAblePokemon() < 1) {
               this.releasePokemon();
            } else {
               this.fightDitto((EntityPlayerMP)player, storage);
            }

            this.resetMachine();
         } else if (!this.hasMew()) {
            storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
            if (storage.countPokemon() > 1) {
               Pokemon mew = storage.findOne((pokemon) -> {
                  return pokemon.getSpecies() == EnumSpecies.Mew && ((MewStats)pokemon.getExtraStats(MewStats.class)).numCloned < 3;
               });
               if (mew != null) {
                  this.mew = mew;
                  storage.set((StoragePosition)mew.getStorageAndPosition().func_76340_b(), (Pokemon)null);
                  mew.setStorage((PokemonStorage)null, (StoragePosition)null);
                  mew.retrieve();
                  this.owner = storage.getOwnerUUID();
                  ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               } else {
                  ChatHandler.sendChat(player, "pixelmon.blocks.noclone");
               }
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.lastpoke");
            }
         } else if (this.hasMew() && player.func_70093_af() && !this.growingPokemon && !this.processingClone) {
            if (this.owner != null) {
               Pixelmon.storageManager.getParty(this.owner).add(this.mew);
               this.resetMachine();
            }
         } else if (this.boostCount < 3 && heldItem != null && heldItem.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)heldItem.func_77973_b()).func_179223_d();
            if (block == Blocks.field_150339_S) {
               ++this.boostCount;
               ++this.boostLevel;
               ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               if (!player.field_71075_bZ.field_75098_d) {
                  heldItem.func_190918_g(1);
               }
            } else if (block == Blocks.field_150340_R) {
               ++this.boostCount;
               this.boostLevel += 3;
               ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               if (!player.field_71075_bZ.field_75098_d) {
                  heldItem.func_190918_g(1);
               }
            } else if (block == Blocks.field_150484_ah) {
               ++this.boostCount;
               this.boostLevel += 5;
               ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               if (!player.field_71075_bZ.field_75098_d) {
                  heldItem.func_190918_g(1);
               }
            }
         }

      }
   }

   private void fightDitto(EntityPlayerMP player, PlayerPartyStorage storage) {
      if (this.pixelmon != null) {
         EntityPixelmon firstPokemon = storage.findOne((p) -> {
            return !p.isEgg() && p.getHealth() > 0;
         }).getOrSpawnPixelmon(player);
         firstPokemon.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70177_z, 0.0F);
         BattleParticipant part = new WildPixelmonParticipant(new EntityPixelmon[]{this.pixelmon});
         this.pixelmon.startBattle(new PlayerParticipant(player, new EntityPixelmon[]{firstPokemon}), part);
      }
   }

   public void releasePokemon() {
      int xplus = 0;
      int zplus = 0;
      if (this.xboost > 0.0F) {
         ++zplus;
      } else if (this.xboost < 0.0F) {
         --zplus;
      } else if (this.zboost > 0.0F) {
         --xplus;
      } else if (this.zboost < 0.0F) {
         ++xplus;
      }

      if (this.pixelmon != null) {
         this.pixelmon.func_70012_b((double)((float)this.field_174879_c.func_177958_n() + this.xboost + (float)xplus), (double)this.field_174879_c.func_177956_o(), (double)((float)this.field_174879_c.func_177952_p() + this.zboost + (float)zplus), 0.0F, 0.0F);
         this.pixelmon.releaseFromPokeball();
      }

   }

   public void onDestroy() {
      if (!this.field_145850_b.field_72995_K && this.hasMew()) {
         try {
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(this.owner);
            if (storage != null) {
               storage.add(this.mew);
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }
}
