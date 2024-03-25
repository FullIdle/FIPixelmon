package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.legendary.Gen2BellEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.blocks.BlockBell;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.RotateEntity;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBell extends TileEntity implements ITickable {
   public boolean spawning = false;
   private BlockBell.Type type;
   public UUID owner;

   public TileEntityBell() {
   }

   public TileEntityBell(BlockBell.Type type) {
      this.type = type;
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
      nbt.func_74757_a("BellSpawning", this.spawning);
      nbt.func_74768_a("BellType", this.type.ordinal());
      nbt.func_74778_a("BellOwner", this.owner == null ? "" : this.owner.toString());
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.spawning = nbt.func_74767_n("BellSpawning");
      this.type = BlockBell.Type.values()[nbt.func_74762_e("BellType")];
      this.owner = nbt.func_74779_i("BellOwner").isEmpty() ? null : UUID.fromString(nbt.func_74779_i("BellOwner"));
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

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         int clampedTime = (int)(this.field_145850_b.func_72820_D() % 24000L);
         if (this.spawning && this.field_145850_b.field_73012_v.nextDouble() < (this.field_145850_b.func_72820_D() > 12000L ? (this.field_145850_b.func_72820_D() > 12500L ? 0.01 : 0.005) : 0.001)) {
            this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, PixelSounds.bellRing, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         if (clampedTime == 1) {
            if (!this.spawning) {
               int count = this.countNearbyBells();
               if (count == 0) {
                  ++count;
               }

               Gen2BellEvent.RollSuccessEvent event = new Gen2BellEvent.RollSuccessEvent(this.owner, this);
               if (!Pixelmon.EVENT_BUS.post(event) && RandomHelper.getRandomChance(event.chance * (double)count)) {
                  this.spawning = true;
                  this.func_70296_d();
               }
            }
         } else if (clampedTime == 13000 && this.spawning) {
            EntityPlayerMP ownerPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.owner);
            if (ownerPlayer == null || ownerPlayer.func_174818_b(this.field_174879_c) > 4096.0) {
               this.spawning = false;
               this.func_70296_d();
               return;
            }

            Pixelmon.EVENT_BUS.post(new Gen2BellEvent.SummonLegendary(this.owner, this));
            Pokemon pokemon = Pixelmon.pokemonFactory.create(this.type == BlockBell.Type.Tidal ? EnumSpecies.Lugia : EnumSpecies.Hooh);
            if (pokemon.getSpecies() == EnumSpecies.Hooh) {
               if ((double)this.field_145850_b.field_73012_v.nextFloat() > 0.2) {
                  pokemon.setHeldItem(new ItemStack(PixelmonItems.sacredAsh));
               } else {
                  pokemon.setHeldItem(new ItemStack(PixelmonItems.rainbowWing));
               }
            }

            if (this.owner != null) {
               PlayerPartyStorage storage = Pixelmon.storageManager.getParty(ownerPlayer);
               if (ownerPlayer != null && storage.countAblePokemon() >= 1 && BattleRegistry.getBattle(ownerPlayer) == null) {
                  EntityPixelmon startingPixelmon = storage.findOne((p) -> {
                     return !p.isEgg() && p.getHealth() > 0;
                  }).getOrSpawnPixelmon(ownerPlayer);
                  EntityPixelmon pixelmon = pokemon.getOrSpawnPixelmon(this.field_145850_b, (double)this.field_174879_c.func_177958_n() + 0.5, (double)(this.field_174879_c.func_177956_o() - 1), (double)this.field_174879_c.func_177952_p() + 0.5);
                  PlayerParticipant playerParticipant = new PlayerParticipant(ownerPlayer, new EntityPixelmon[]{startingPixelmon});
                  WildPixelmonParticipant wildPixelmonParticipant = new WildPixelmonParticipant(false, new EntityPixelmon[]{pixelmon});
                  wildPixelmonParticipant.startedBattle = true;
                  BattleRegistry.startBattle(playerParticipant, wildPixelmonParticipant);
               } else {
                  pokemon.getOrSpawnPixelmon(this.field_145850_b, (double)this.field_174879_c.func_177958_n() + 0.5, (double)(this.field_174879_c.func_177956_o() - 1), (double)this.field_174879_c.func_177952_p() + 0.5);
               }

               Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.DISCHARGE, (double)this.field_174879_c.func_177958_n() + 0.5, (double)(this.field_174879_c.func_177956_o() - 1), (double)this.field_174879_c.func_177952_p() + 0.5, this.field_145850_b.field_73011_w.getDimension(), 0.0F, false, new double[]{0.0, 0.0, 0.0, 1.0}), this.field_145850_b.field_73011_w.getDimension());
               this.field_145850_b.func_175698_g(this.field_174879_c);
               this.field_145850_b.func_175713_t(this.field_174879_c);
            }
         }
      }

   }

   private int countNearbyBells() {
      int bells = 0;
      Iterator var2 = this.field_145850_b.field_147482_g.iterator();

      while(var2.hasNext()) {
         TileEntity te = (TileEntity)var2.next();
         if (te instanceof TileEntityBell && te != this && te.func_174877_v().func_185332_f(this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p()) <= (double)PixelmonConfig.bellInclusionRange) {
            ++bells;
         }
      }

      return bells;
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
}
