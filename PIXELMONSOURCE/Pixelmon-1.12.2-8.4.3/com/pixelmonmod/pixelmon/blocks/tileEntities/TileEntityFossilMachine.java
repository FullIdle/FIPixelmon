package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileEntityFossilMachine extends TileEntity implements ITickable {
   public static final int MAX_POKEMON_PROGRESS = 3200;
   public static final int MAX_FOSSIL_PROGRESS = 1600;
   public int renderPass = 0;
   public EntityStatue statue = null;
   public float fossilJitter;
   public int screenFlickerTick;
   public boolean staticFlicker = false;
   public int dotTicks = 0;
   public String dots = "";
   public boolean pokemonOccupied = false;
   public EnumFossils currentFossil;
   public float fossilProgress;
   public String currentPokemon;
   public float pokemonProgress;
   public int completionRate;
   public int completionSync;
   public boolean isShiny;
   public EnumPokeballs pokeball;

   public TileEntityFossilMachine() {
      this.currentFossil = EnumFossils.NULL;
      this.fossilProgress = 0.0F;
      this.currentPokemon = "";
      this.pokemonProgress = 0.0F;
      this.completionRate = (int)((this.fossilProgress + this.pokemonProgress) * 2.0F / 96.0F);
      this.completionSync = 0;
      this.isShiny = false;
      this.pokeball = null;
   }

   public void func_73660_a() {
      if (this.completionRate != 100 && (this.fossilProgress > 0.0F || this.pokemonProgress > 0.0F)) {
         this.completionRate = (int)((this.fossilProgress + this.pokemonProgress) * 2.0F / 96.0F);
         if (!this.func_145831_w().field_72995_K && this.completionRate % 10 == 0 && this.completionSync != this.completionRate) {
            ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            this.completionSync = this.completionRate;
         }
      }

      if (this.currentFossil.getPokemon() != null) {
         if (this.fossilProgress < 1600.0F) {
            ++this.fossilProgress;
         } else if (!this.func_145831_w().field_72995_K) {
            this.swapFossilForPokemon();
            ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
         }
      }

      if (this.pokemonOccupied && this.pokemonProgress < 3200.0F) {
         ++this.pokemonProgress;
      }

      if (++this.dotTicks > 10) {
         this.dotTicks = 0;
         if (this.dots.length() < 6) {
            this.dots = this.dots + ".";
         } else {
            this.dots = "";
         }
      }

      this.fossilJitter = this.fossilJitter == 0.0F ? 0.01F : 0.0F;
      if (this.staticFlicker) {
         --this.screenFlickerTick;
      } else {
         ++this.screenFlickerTick;
      }

      if (this.screenFlickerTick >= 8) {
         this.staticFlicker = true;
      } else if (this.screenFlickerTick <= 0) {
         this.staticFlicker = false;
      }

      if (this.completionRate < 98) {
         int num = this.completionRate <= 50 ? this.completionRate / 4 : (100 - this.completionRate) / 4;
         double var9 = (double)((float)this.func_174877_v().func_177956_o() + 0.4F);

         for(int i = 0; i < num; ++i) {
            this.func_145831_w().func_175688_a(EnumParticleTypes.REDSTONE, (double)this.func_174877_v().func_177958_n() + 0.1 + (double)this.func_145831_w().field_73012_v.nextFloat() * 0.7, var9 + (double)this.func_145831_w().field_73012_v.nextFloat() + (double)(this.func_145831_w().field_73012_v.nextFloat() * 0.4F), (double)this.func_174877_v().func_177952_p() + 0.1 + (double)this.func_145831_w().field_73012_v.nextFloat() * 0.7, -1.0, 1.2, 1.0, new int[0]);
         }
      }

   }

   public void swapFossilForPokemon() {
      this.currentPokemon = this.currentFossil.getPokemon().name;
      this.pokemonOccupied = true;
      int dimension = this.func_145831_w() == null ? 0 : this.func_145831_w().field_73011_w.getDimension();
      this.isShiny = PixelmonConfig.getShinyRate(dimension) > 0.0F && RandomHelper.rand.nextFloat() < 1.0F / PixelmonConfig.getShinyRate(dimension);
      this.currentFossil = EnumFossils.NULL;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.pokeball != null) {
         nbt.func_74768_a("PokeballType", this.pokeball.getIndex());
      }

      nbt.func_74768_a("FossilType", this.currentFossil.getIndex());
      nbt.func_74776_a("PokemonProgress", this.pokemonProgress);
      nbt.func_74776_a("FossilProgress", this.fossilProgress);
      nbt.func_74778_a("CurrentPokemon", this.currentPokemon);
      nbt.func_74757_a("IsShiny", this.isShiny);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_150297_b("CurrentPokeball", 3)) {
         if (Item.func_150899_d(nbt.func_74762_e("CurrentPokeball")) instanceof ItemPokeball) {
            ItemPokeball itemPokeball = (ItemPokeball)Item.func_150899_d(nbt.func_74762_e("CurrentPokeball"));
            this.pokeball = itemPokeball.type;
         }
      } else if (nbt.func_150297_b("PokeballType", 3)) {
         this.pokeball = EnumPokeballs.getFromIndex(nbt.func_74762_e("PokeballType"));
      } else {
         this.pokeball = null;
      }

      if (nbt.func_74764_b("CurrentFossil")) {
         int curFos = nbt.func_74762_e("CurrentFossil");
         if (Item.func_150899_d(curFos) instanceof ItemFossil) {
            this.currentFossil = ((ItemFossil)Item.func_150899_d(curFos)).fossil;
         } else {
            this.currentFossil = EnumFossils.NULL;
         }
      }

      if (nbt.func_74764_b("FossilType")) {
         this.currentFossil = EnumFossils.fromIndex(nbt.func_74762_e("FossilType"));
      }

      this.fossilProgress = nbt.func_74760_g("FossilProgress");
      this.pokemonProgress = nbt.func_74760_g("PokemonProgress");
      this.currentPokemon = nbt.func_74779_i("CurrentPokemon");
      this.pokemonOccupied = !this.currentPokemon.equals("");
      this.completionRate = (int)((this.fossilProgress + this.pokemonProgress) * 2.0F / 96.0F);
      if (nbt.func_74764_b("IsShiny")) {
         this.isShiny = nbt.func_74767_n("IsShiny");
      }

   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      nbt.func_74768_a("CurrentFossil", this.currentFossil.getIndex());
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public boolean shouldRenderInPass(int pass) {
      this.renderPass = pass;
      return true;
   }
}
