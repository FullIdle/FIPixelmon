package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.util.LootClaim;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityPokegift extends TileEntity {
   private EnumPokechestVisibility visibility;
   private UUID ownerID;
   private boolean chestOneTime;
   private boolean dropOneTime;
   private Pokemon pokemon;
   private ArrayList specialPixelmon;
   private EnumPokegiftType type;
   private ArrayList claimed;

   public TileEntityPokegift() {
      this.visibility = EnumPokechestVisibility.Visible;
      this.ownerID = null;
      this.chestOneTime = true;
      this.dropOneTime = true;
      this.pokemon = null;
      this.specialPixelmon = new ArrayList();
      this.type = EnumPokegiftType.GIFT;
      this.claimed = new ArrayList();
   }

   public void setOwner(UUID id) {
      this.ownerID = id;
   }

   public UUID getOwner() {
      return this.ownerID;
   }

   public void setType(EnumPokegiftType t) {
      this.type = t;
   }

   public EnumPokegiftType getType() {
      return this.type;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74772_a("ownerIDMost", this.ownerID == null ? -1L : this.ownerID.getMostSignificantBits());
      compound.func_74772_a("ownerIDLeast", this.ownerID == null ? -1L : this.ownerID.getLeastSignificantBits());
      compound.func_74757_a("chestOneTime", this.chestOneTime);
      compound.func_74757_a("dropOneTime", this.dropOneTime);
      NBTTagCompound pokemon;
      NBTTagCompound nbt;
      if (!this.claimed.isEmpty()) {
         pokemon = new NBTTagCompound();

         for(int i = 0; i < this.claimed.size(); ++i) {
            nbt = new NBTTagCompound();
            LootClaim playerClaim = (LootClaim)this.claimed.get(i);
            nbt.func_74772_a("most", playerClaim.getPlayerID().getMostSignificantBits());
            nbt.func_74772_a("least", playerClaim.getPlayerID().getLeastSignificantBits());
            nbt.func_74772_a("timeClaimed", playerClaim.getTimeClaimed());
            pokemon.func_74782_a("player" + i, nbt);
         }

         compound.func_74782_a("claimedPlayers", pokemon);
      }

      compound.func_74768_a("type", this.type.ordinal());
      if (this.type == EnumPokegiftType.GIFT) {
         if (this.pokemon != null) {
            pokemon = new NBTTagCompound();
            this.pokemon.writeToNBT(pokemon);
            compound.func_74782_a("pixelmon", pokemon);
         }
      } else if (!this.specialPixelmon.isEmpty()) {
         Iterator var6 = this.specialPixelmon.iterator();

         while(var6.hasNext()) {
            PokemonSpec p = (PokemonSpec)var6.next();
            nbt = new NBTTagCompound();
            p.writeToNBT(nbt);
         }
      }

      return compound;
   }

   public void func_145839_a(NBTTagCompound tagger) {
      if (tagger.func_74763_f("ownerIDMost") != -1L) {
         this.ownerID = new UUID(tagger.func_74763_f("ownerIDMost"), tagger.func_74763_f("ownerIDLeast"));
      }

      this.chestOneTime = tagger.func_74767_n("chestOneTime");
      this.dropOneTime = tagger.func_74767_n("dropOneTime");
      NBTTagCompound specialTag;
      int i;
      NBTTagCompound nbt;
      if (tagger.func_74764_b("claimedPlayers")) {
         specialTag = (NBTTagCompound)tagger.func_74781_a("claimedPlayers");
         this.claimed.clear();

         for(i = 0; specialTag.func_74764_b("player" + i); ++i) {
            nbt = (NBTTagCompound)specialTag.func_74781_a("player" + i);
            this.claimed.add(new LootClaim(new UUID(nbt.func_74763_f("most"), nbt.func_74763_f("least")), nbt.func_74763_f("timeClaimed")));
         }
      }

      this.type = EnumPokegiftType.values()[tagger.func_74762_e("type")];
      if (this.type == EnumPokegiftType.GIFT) {
         if (tagger.func_74764_b("pixelmon")) {
            this.pokemon = Pixelmon.pokemonFactory.create(tagger.func_74775_l("pixelmon"));
         } else {
            this.pokemon = null;
         }
      } else if (tagger.func_74764_b("specials")) {
         specialTag = (NBTTagCompound)tagger.func_74781_a("specials");

         for(i = 0; specialTag.func_74764_b("special" + i); ++i) {
            nbt = (NBTTagCompound)specialTag.func_74781_a("special" + i);
            this.specialPixelmon.add((new PokemonSpec(new String[0])).readFromNBT(nbt));
         }
      }

      super.func_145839_a(tagger);
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound compound = super.func_189517_E_();
      this.func_189515_b(compound);
      compound.func_82580_o("claimedPlayers");
      compound.func_82580_o("specials");
      compound.func_82580_o("pixelmon");
      return compound;
   }

   public boolean canClaim(UUID playerID) {
      if (!this.dropOneTime) {
         return true;
      } else {
         LootClaim claim = this.getLootClaim(playerID);
         return claim == null;
      }
   }

   public LootClaim getLootClaim(UUID playerID) {
      Iterator var2 = this.claimed.iterator();

      LootClaim claim;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         claim = (LootClaim)var2.next();
      } while(!claim.getPlayerID().toString().equals(playerID.toString()));

      return claim;
   }

   public void addClaimer(UUID playerID) {
      if (this.dropOneTime) {
         this.claimed.add(new LootClaim(playerID, System.currentTimeMillis()));
      }

   }

   public void removeClaimer(UUID playerID) {
      this.claimed.remove(this.getLootClaim(playerID));
   }

   public boolean shouldBreakBlock() {
      return this.chestOneTime;
   }

   public void setChestOneTime(boolean val) {
      this.chestOneTime = val;
   }

   public boolean getChestMode() {
      return this.chestOneTime;
   }

   public void setDropOneTime(boolean val) {
      this.dropOneTime = val;
   }

   public boolean getDropMode() {
      return this.dropOneTime;
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) < 64.0;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public void setPokemon(Pokemon pokemon) {
      this.pokemon = pokemon;
   }

   public ArrayList getSpecialPixelmon() {
      return this.specialPixelmon;
   }

   public void setAllSpecialPixelmon(ArrayList pixelmon) {
      this.specialPixelmon.clear();
      this.specialPixelmon.addAll(pixelmon);
   }

   public void setSpecialPixelmon(PokemonSpec pixelmon) {
      this.specialPixelmon.add(pixelmon);
   }

   public EnumPokechestVisibility getVisibility() {
      return this.visibility;
   }

   public void setVisibility(EnumPokechestVisibility visible) {
      this.visibility = visible;
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }
}
