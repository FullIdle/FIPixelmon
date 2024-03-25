package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;

public class TileEntityHealer extends TileEntity implements ITickable, ISpecialTexture {
   private static ResourceLocation[] textures = new ResourceLocation[EnumDyeColor.values().length];
   private EnumDyeColor color;
   private boolean rave;
   private long lastRaveChange;
   private UUID owner;
   private static final int ticksToPlace = 5;
   private static final int ticksToHeal = 100;
   public boolean beingUsed;
   public EnumPokeballs[] pokeballType;
   private PlayerPartyStorage storage;
   public EntityPlayer player;
   private int pokemonLastPlaced;
   private float healingRate;
   private int tickCount;
   public float rotation;
   public int flashTimer;
   public boolean allPlaced;
   public boolean playSound;
   public boolean stayDark;

   public TileEntityHealer() {
      this.color = EnumDyeColor.WHITE;
      this.rave = false;
      this.lastRaveChange = 0L;
      this.owner = null;
      this.beingUsed = false;
      this.pokeballType = new EnumPokeballs[6];
      this.pokemonLastPlaced = -1;
      this.healingRate = 1.0F;
      this.tickCount = 0;
      this.rotation = 0.0F;
      this.flashTimer = 0;
      this.allPlaced = false;
      this.playSound = false;
      this.stayDark = false;
   }

   public void use(NPCNurseJoy npc, EntityPlayer player) {
      this.startHealing(player, 1.5F, npc.getTextureIndex());
   }

   public void use(EntityPlayer player) {
      this.startHealing(player, 1.0F, 0);
   }

   private void startHealing(EntityPlayer player, float healingRate, int textureIndex) {
      this.healingRate = healingRate;
      this.storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
      this.storage.teleportPos.store(player.field_71093_bK, player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
      this.player = player;
      this.beingUsed = true;
      if (healingRate == 1.5F) {
         OpenScreen.open(player, EnumGuiScreen.HealerNurseJoy, textureIndex);
      } else {
         OpenScreen.open(player, EnumGuiScreen.Healer);
      }

      this.tickCount = 0;
      this.allPlaced = false;
      this.pokemonLastPlaced = -1;

      for(int i = 0; i < this.pokeballType.length; ++i) {
         this.pokeballType[i] = null;
      }

      this.stayDark = false;
      if (!this.field_145850_b.field_72995_K && this.beingUsed) {
         this.sendRedstoneSignal();
      }

   }

   private int getTicksToHeal() {
      return (int)(100.0F / this.healingRate);
   }

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         this.rotation += 19.0F;
         ++this.flashTimer;
      } else {
         if (this.beingUsed) {
            if (this.allPlaced && !this.playSound) {
               this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, PixelSounds.healerActive, SoundCategory.BLOCKS, 0.7F, 1.0F);
               this.playSound = true;
            }

            ++this.tickCount;
            if (!this.allPlaced && this.tickCount == 5) {
               boolean hasNextPokemon = false;
               this.storage.retrieveAll();

               for(int i = this.pokemonLastPlaced + 1; i < this.storage.getTeam().size(); ++i) {
                  if (this.storage.getTeam().get(i) != null) {
                     this.pokemonLastPlaced = i;
                     hasNextPokemon = true;
                     this.pokeballType[i] = ((Pokemon)this.storage.getTeam().get(i)).getCaughtBall();
                     break;
                  }
               }

               if (!hasNextPokemon) {
                  this.allPlaced = true;
               }

               this.tickCount = 0;
               this.refreshTexture();
            }

            int ticksToHeal = this.getTicksToHeal();
            if (this.tickCount == ticksToHeal - 30) {
               this.stayDark = true;
               this.refreshTexture();
            }

            if (this.tickCount == ticksToHeal) {
               this.storage.getTeam().forEach(Pokemon::heal);
               this.beingUsed = false;
               this.playSound = false;
               this.refreshTexture();
               this.player.func_71053_j();
               if (!this.field_145850_b.field_72995_K) {
                  this.sendRedstoneSignal();
               }
            }
         }

      }
   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74778_a("colour", this.color.func_176610_l());
      compound.func_74757_a("rave", this.rave);
      if (this.owner != null) {
         compound.func_74778_a("owner", this.owner.toString());
      }

      return compound;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_74764_b("colour")) {
         this.color = (EnumDyeColor)Arrays.stream(EnumDyeColor.values()).filter((e) -> {
            return e.func_176610_l().equals(compound.func_74779_i("colour"));
         }).findFirst().orElse(EnumDyeColor.WHITE);
         this.rave = compound.func_74767_n("rave");
      }

      if (compound.func_74764_b("owner")) {
         this.owner = UUID.fromString(compound.func_74779_i("owner"));
      }

   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);

      for(int i = 0; i < this.pokeballType.length; ++i) {
         if (this.pokeballType[i] != null) {
            nbt.func_74777_a("PokeballType" + i, (short)this.pokeballType[i].ordinal());
         }
      }

      nbt.func_74757_a("BeingUsed", this.beingUsed);
      nbt.func_74757_a("AllPlaced", this.allPlaced);
      nbt.func_74757_a("StayDark", this.stayDark);
      nbt.func_74776_a("HealRate", this.healingRate);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.func_148857_g();
      this.func_145839_a(compound);

      for(int i = 0; i < this.pokeballType.length; ++i) {
         this.pokeballType[i] = null;
         if (compound.func_74764_b("PokeballType" + i)) {
            this.pokeballType[i] = EnumPokeballs.getFromIndex(compound.func_74765_d("PokeballType" + i));
         }
      }

      if (compound.func_74764_b("BeingUsed")) {
         this.beingUsed = compound.func_74767_n("BeingUsed");
         this.allPlaced = compound.func_74767_n("AllPlaced");
         this.stayDark = compound.func_74767_n("StayDark");
      }

      if (compound.func_74764_b("HealRate")) {
         this.healingRate = compound.func_74760_g("HealRate");
      }

   }

   public void setColor(EnumDyeColor color) {
      this.color = color;
      this.refreshTexture();
   }

   public ResourceLocation getTexture() {
      if (this.rave && (this.lastRaveChange == 0L || this.func_145831_w().func_82737_E() - this.lastRaveChange > 5L)) {
         this.color = EnumDyeColor.values()[RandomHelper.rand.nextInt(EnumDyeColor.values().length)];
         this.lastRaveChange = this.func_145831_w().func_82737_E();
         this.refreshTexture();
      }

      return textures[this.color.func_176765_a()];
   }

   private void sendRedstoneSignal() {
      Block block = this.func_145838_q();
      if (block != null) {
         this.field_145850_b.func_175685_c(this.field_174879_c, block, true);
      }

   }

   private void refreshTexture() {
      if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
         this.func_70296_d();
      }

   }

   static {
      EnumDyeColor[] var0 = EnumDyeColor.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumDyeColor color = var0[var2];
         textures[color.func_176765_a()] = new ResourceLocation("pixelmon", "textures/blocks/healer/" + color.func_176610_l() + "_healer.png");
      }

   }
}
