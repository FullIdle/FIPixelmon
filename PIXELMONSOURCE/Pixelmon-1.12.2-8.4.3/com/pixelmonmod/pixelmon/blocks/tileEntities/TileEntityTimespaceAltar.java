package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.legendary.TimespaceEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.blocks.BlockRotation;
import com.pixelmonmod.pixelmon.blocks.machines.BlockTimespaceAltar;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.RotateEntity;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.heldItems.ItemTimespaceOrb;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.EncounterData;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTimespaceAltar extends TileEntity implements ITickable {
   public EncounterData encounters;
   public Orb orbIn;
   public boolean chainIn;
   public boolean flutePlayed;
   private boolean spawning;
   public int timeSpent;
   public boolean summoningShiny;
   public EntityPlayer summoningPlayer;
   public IBlockState summoningState;

   public TileEntityTimespaceAltar() {
      this.encounters = new EncounterData(PixelmonConfig.shrineEncounterMode);
      this.orbIn = TileEntityTimespaceAltar.Orb.NONE;
      this.chainIn = false;
      this.flutePlayed = false;
      this.spawning = false;
      this.timeSpent = 0;
      this.summoningShiny = false;
      this.summoningPlayer = null;
      this.summoningState = null;
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      return this.func_189515_b(nbt);
   }

   public void handleUpdateTag(NBTTagCompound tag) {
      this.func_145839_a(tag);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74782_a("EncounterData", this.encounters.writeToNBT(new NBTTagCompound()));
      nbt.func_74768_a("OrbIn", this.orbIn.ordinal());
      nbt.func_74757_a("ChainIn", this.chainIn);
      nbt.func_74757_a("FlutePlayed", this.flutePlayed);
      nbt.func_74768_a("TimeSpent", this.timeSpent);
      nbt.func_74757_a("IsShiny", this.summoningShiny);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.orbIn = TileEntityTimespaceAltar.Orb.values()[nbt.func_74762_e("OrbIn")];
      this.chainIn = nbt.func_74767_n("ChainIn");
      this.flutePlayed = nbt.func_74767_n("FlutePlayed");
      this.timeSpent = nbt.func_74762_e("TimeSpent");
      this.summoningShiny = nbt.func_74767_n("IsShiny");
      this.encounters = new EncounterData(PixelmonConfig.shrineEncounterMode);
      if (nbt.func_74764_b("EncounterData")) {
         this.encounters.readFromNBT(nbt.func_74775_l("EncounterData"));
      }

      if (this.getTileData().func_74764_b("encounters")) {
         NBTTagList data = this.getTileData().func_150295_c("encounters", 8);
         Iterator var3 = data.iterator();

         while(var3.hasNext()) {
            NBTBase base = (NBTBase)var3.next();
            NBTTagString str = (NBTTagString)base;
            UUID uuid = UUID.fromString(str.func_150285_a_());
            this.encounters.addEncounter(uuid, 1L);
         }

         this.getTileData().func_82580_o("encounters");
      }

   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
      this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
   }

   public void func_70296_d() {
      this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
      super.func_70296_d();
   }

   public void activate(EntityPlayer player, BlockTimespaceAltar block, IBlockState state, ItemStack item) {
      if ((this.orbIn != TileEntityTimespaceAltar.Orb.NONE || this.chainIn) && player.func_70093_af() && (this.orbIn == TileEntityTimespaceAltar.Orb.NONE || !this.chainIn)) {
         if (this.orbIn != TileEntityTimespaceAltar.Orb.NONE) {
            if (!Pixelmon.EVENT_BUS.post(new TimespaceEvent.TakeOrb(player, this, item))) {
               switch (this.orbIn) {
                  case PALKIA:
                     player.func_191521_c(new ItemStack(PixelmonItemsHeld.lustrous_orb));
                     break;
                  case DIALGA:
                     player.func_191521_c(new ItemStack(PixelmonItemsHeld.adamant_orb));
                     break;
                  case GIRATINA:
                     player.func_191521_c(new ItemStack(PixelmonItemsHeld.griseous_orb));
               }

               this.orbIn = TileEntityTimespaceAltar.Orb.NONE;
               this.func_70296_d();
            }
         } else if (this.chainIn && !Pixelmon.EVENT_BUS.post(new TimespaceEvent.TakeChain(player, this, item))) {
            player.func_191521_c(new ItemStack(PixelmonItems.redchain));
            this.field_145850_b.func_184148_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundEvents.field_187713_n, SoundCategory.BLOCKS, 1.0F, 50.0F);
            this.chainIn = false;
            this.func_70296_d();
         }

      } else {
         if (item != null && item != ItemStack.field_190927_a) {
            ItemStack stack;
            if (!(item.func_77973_b() instanceof ItemTimespaceOrb) && item.func_77973_b() != PixelmonItems.redchain) {
               if (this.chainIn && !this.spawning && !this.flutePlayed) {
                  stack = new ItemStack(PixelmonItems.redchain);
                  if (!Pixelmon.EVENT_BUS.post(new TimespaceEvent.TakeChain(player, this, stack))) {
                     this.chainIn = false;
                     player.func_191521_c(stack);
                     this.func_70296_d();
                  }
               } else {
                  ChatHandler.sendChat(player, "pixelmon.blocks.timespace.notorb");
               }
            } else if (!player.field_70170_p.field_72995_K) {
               if (this.timeSpent == 0) {
                  if (item.func_77973_b() instanceof ItemTimespaceOrb) {
                     stack = ItemStack.field_190927_a;
                     if (this.orbIn == TileEntityTimespaceAltar.Orb.PALKIA) {
                        stack = new ItemStack(PixelmonItemsHeld.lustrous_orb, 1);
                     } else if (this.orbIn == TileEntityTimespaceAltar.Orb.DIALGA) {
                        stack = new ItemStack(PixelmonItemsHeld.adamant_orb, 1);
                     } else if (this.orbIn == TileEntityTimespaceAltar.Orb.GIRATINA) {
                        stack = new ItemStack(PixelmonItemsHeld.griseous_orb, 1);
                     }

                     if (stack != ItemStack.field_190927_a) {
                        if (Pixelmon.EVENT_BUS.post(new TimespaceEvent.TakeOrb(player, this, stack))) {
                           return;
                        }

                        player.func_191521_c(stack);
                     }
                  }

                  if (item.func_77973_b() instanceof ItemTimespaceOrb) {
                     if (!this.chainIn) {
                        ChatHandler.sendChat(player, "pixelmon.blocks.timespace.needchain");
                        return;
                     }

                     if (Pixelmon.EVENT_BUS.post(new TimespaceEvent.PlaceOrb(player, this, item))) {
                        return;
                     }

                     if (!this.encounters.canEncounter(player)) {
                        if (this.encounters.getMode().isTimedAccess()) {
                           ChatHandler.sendChat(player, "pixelmon.blocks.shrine.today");
                        } else {
                           ChatHandler.sendChat(player, "pixelmon.blocks.shrine.encountered");
                        }

                        return;
                     }

                     this.field_145850_b.func_184148_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundEvents.field_187766_dk, SoundCategory.BLOCKS, 1.0F, 5.0F);
                     if (item.func_77973_b() == PixelmonItemsHeld.lustrous_orb) {
                        this.orbIn = TileEntityTimespaceAltar.Orb.PALKIA;
                     } else if (item.func_77973_b() == PixelmonItemsHeld.adamant_orb) {
                        this.orbIn = TileEntityTimespaceAltar.Orb.DIALGA;
                     } else if (item.func_77973_b() == PixelmonItemsHeld.griseous_orb) {
                        this.orbIn = TileEntityTimespaceAltar.Orb.GIRATINA;
                     }

                     this.func_70296_d();
                     if (!player.func_184812_l_()) {
                        item.func_190918_g(1);
                     }
                  } else if (item.func_77973_b() == PixelmonItems.redchain) {
                     if (Pixelmon.EVENT_BUS.post(new TimespaceEvent.PlaceChain(player, this, item))) {
                        return;
                     }

                     if (!this.encounters.canEncounter(player)) {
                        if (this.encounters.getMode().isTimedAccess()) {
                           ChatHandler.sendChat(player, "pixelmon.blocks.shrine.today");
                        } else {
                           ChatHandler.sendChat(player, "pixelmon.blocks.shrine.encountered");
                        }

                        return;
                     }

                     this.chainIn = true;
                     this.field_145850_b.func_184148_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundEvents.field_187713_n, SoundCategory.BLOCKS, 1.0F, 50.0F);
                     this.func_70296_d();
                     if (!player.func_184812_l_()) {
                        item.func_190918_g(1);
                     }
                  }
               }

               if (this.orbIn != TileEntityTimespaceAltar.Orb.NONE && this.chainIn && !this.spawning && !this.flutePlayed) {
                  this.summoningPlayer = player;
                  this.summoningState = state;
                  this.spawning = true;
                  this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187810_eg, SoundCategory.NEUTRAL, 1.0F, 0.1F);
                  this.summoningShiny = PixelmonConfig.getShinyRate(this.field_145850_b.field_73011_w.getDimension()) > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(this.field_145850_b.field_73011_w.getDimension()));
                  Pixelmon.EVENT_BUS.post(new TimespaceEvent.Summon.Pre(player, this));
                  this.func_70296_d();
               }
            }
         }

      }
   }

   public void func_73660_a() {
      if ((this.spawning || this.flutePlayed) && !this.field_145850_b.field_72995_K) {
         ++this.timeSpent;
         if (this.summoningPlayer == null) {
            this.reset();
            this.func_70296_d();
            return;
         }

         if (this.timeSpent > 200) {
            EntityPixelmon pixelmonEntity;
            if (this.spawning) {
               switch (this.orbIn) {
                  case PALKIA:
                     pixelmonEntity = (EntityPixelmon)PixelmonEntityList.createEntityByName(EnumSpecies.Palkia.name, this.field_145850_b);
                     pixelmonEntity.getPokemonData().setHeldItem(new ItemStack(PixelmonItemsHeld.lustrous_orb));
                     break;
                  case DIALGA:
                     pixelmonEntity = (EntityPixelmon)PixelmonEntityList.createEntityByName(EnumSpecies.Dialga.name, this.field_145850_b);
                     pixelmonEntity.getPokemonData().setHeldItem(new ItemStack(PixelmonItemsHeld.adamant_orb));
                     break;
                  case GIRATINA:
                     pixelmonEntity = (EntityPixelmon)PixelmonEntityList.createEntityByName(EnumSpecies.Giratina.name, this.field_145850_b);
                     pixelmonEntity.getPokemonData().setHeldItem(new ItemStack(PixelmonItemsHeld.griseous_orb));
                     break;
                  default:
                     return;
               }
            } else {
               pixelmonEntity = (EntityPixelmon)PixelmonEntityList.createEntityByName("Arceus", this.field_145850_b);
               pixelmonEntity.getLvl().setLevel(80);
            }

            pixelmonEntity.getPokemonData().setShiny(this.summoningShiny);
            PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)this.summoningPlayer);
            BlockRotation rot = BlockRotation.getRotationFromMetadata(this.func_145832_p());
            double xOffset;
            double zOffset;
            float spawnRot;
            if (rot == BlockRotation.Normal) {
               xOffset = 0.5;
               zOffset = 5.5;
               spawnRot = 0.0F;
            } else if (rot == BlockRotation.Rotate180) {
               xOffset = 0.5;
               zOffset = -4.5;
               spawnRot = 180.0F;
            } else if (rot == BlockRotation.CW) {
               xOffset = -4.5;
               zOffset = 0.5;
               spawnRot = 90.0F;
            } else {
               xOffset = 5.5;
               zOffset = 0.5;
               spawnRot = 270.0F;
            }

            this.encounters.registerEncounter(this.summoningPlayer);
            EntityPixelmon startingPixelmon = party.getAndSendOutFirstAblePokemon(this.summoningPlayer);
            if (startingPixelmon == null || BattleRegistry.getBattle(this.summoningPlayer) != null) {
               this.spawnEntity(pixelmonEntity, (double)this.field_174879_c.func_177958_n() + xOffset, (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p() + zOffset, spawnRot);
               Pixelmon.EVENT_BUS.post(new TimespaceEvent.Summon.Post(this.summoningPlayer, this, pixelmonEntity));
               this.reset();
               this.func_70296_d();
               return;
            }

            this.spawnEntity(pixelmonEntity, (double)this.field_174879_c.func_177958_n() + xOffset, (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p() + zOffset, spawnRot);
            Pixelmon.EVENT_BUS.post(new TimespaceEvent.Summon.Post(this.summoningPlayer, this, pixelmonEntity));
            PlayerParticipant playerParticipant = new PlayerParticipant((EntityPlayerMP)this.summoningPlayer, new EntityPixelmon[]{startingPixelmon});
            WildPixelmonParticipant wildPixelmonParticipant = new WildPixelmonParticipant(false, new EntityPixelmon[]{pixelmonEntity});
            wildPixelmonParticipant.startedBattle = true;
            BattleRegistry.startBattle(playerParticipant, wildPixelmonParticipant);
            this.reset();
         }

         this.func_70296_d();
      }

   }

   public void reset() {
      this.orbIn = TileEntityTimespaceAltar.Orb.NONE;
      this.chainIn = false;
      this.spawning = false;
      this.flutePlayed = false;
      this.timeSpent = 0;
      this.summoningState = null;
      this.summoningPlayer = null;
      this.summoningShiny = false;
   }

   public void spawnEntity(EntityPixelmon pixelmonEntity, double x, double y, double z, float rotation) {
      pixelmonEntity.func_70080_a(x, y, z, rotation, pixelmonEntity.field_70125_A);
      this.func_145831_w().func_72838_d(pixelmonEntity);
      Pixelmon.network.sendToDimension(new RotateEntity(pixelmonEntity.func_145782_y(), rotation, pixelmonEntity.field_70125_A), pixelmonEntity.field_71093_bK);
      pixelmonEntity.func_70091_d((MoverType)null, 0.1, 0.1, 0.1);
      pixelmonEntity.field_70126_B = rotation;
      pixelmonEntity.field_70177_z = rotation;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }

   public static enum Orb {
      NONE,
      PALKIA,
      DIALGA,
      GIRATINA;
   }
}
