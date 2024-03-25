package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.LootClaim;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityPokeChest extends TileEntity {
   private EnumPokechestVisibility visibility;
   private UUID ownerID;
   private boolean chestOneTime;
   private boolean dropOneTime;
   private boolean doCustomDrop;
   private boolean timeEnabled;
   private ItemStack[] customDrops;
   private int frontFace;
   private boolean grotto;
   private boolean isEvent;
   private ArrayList claimed;

   public TileEntityPokeChest() {
      this.visibility = EnumPokechestVisibility.Visible;
      this.ownerID = null;
      this.chestOneTime = true;
      this.dropOneTime = true;
      this.doCustomDrop = false;
      this.timeEnabled = false;
      this.customDrops = null;
      this.frontFace = 4;
      this.grotto = false;
      this.isEvent = false;
      this.claimed = new ArrayList();
   }

   public void setOwner(UUID id) {
      this.ownerID = id;
   }

   public UUID getOwner() {
      return this.ownerID;
   }

   public EnumPokeChestType getType() {
      if (this.isEvent) {
         return EnumPokeChestType.SPECIAL;
      } else if (this.field_145854_h == PixelmonBlocks.pokeChest) {
         return EnumPokeChestType.POKEBALL;
      } else if (this.field_145854_h == PixelmonBlocks.ultraChest) {
         return EnumPokeChestType.ULTRABALL;
      } else if (this.field_145854_h == PixelmonBlocks.beastChest) {
         return EnumPokeChestType.BEASTBALL;
      } else {
         return this.field_145854_h == PixelmonBlocks.masterChest ? EnumPokeChestType.MASTERBALL : EnumPokeChestType.POKEBALL;
      }
   }

   public void setGrotto() {
      this.grotto = true;
   }

   public boolean isGrotto() {
      return this.grotto;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound tagger) {
      super.func_189515_b(tagger);
      tagger.func_74772_a("ownerIDMost", this.ownerID == null ? -1L : this.ownerID.getMostSignificantBits());
      tagger.func_74772_a("ownerIDLeast", this.ownerID == null ? -1L : this.ownerID.getLeastSignificantBits());
      tagger.func_74757_a("chestOneTime", this.chestOneTime);
      tagger.func_74757_a("dropOneTime", this.dropOneTime);
      if (!this.claimed.isEmpty()) {
         NBTTagCompound claimedTag = new NBTTagCompound();

         for(int i = 0; i < this.claimed.size(); ++i) {
            NBTTagCompound playerInfoTag = new NBTTagCompound();
            LootClaim playerClaim = (LootClaim)this.claimed.get(i);
            playerInfoTag.func_74772_a("most", playerClaim.getPlayerID().getMostSignificantBits());
            playerInfoTag.func_74772_a("least", playerClaim.getPlayerID().getLeastSignificantBits());
            playerInfoTag.func_74772_a("timeClaimed", playerClaim.getTimeClaimed());
            claimedTag.func_74782_a("player" + i, playerInfoTag);
         }

         tagger.func_74782_a("claimedPlayers", claimedTag);
      }

      if (this.customDrops != null) {
         NBTTagList list = new NBTTagList();
         ItemStack[] var9 = this.customDrops;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            ItemStack customDrop = var9[var11];
            NBTTagCompound stackCompound = new NBTTagCompound();
            if (customDrop != null) {
               list.func_74742_a(customDrop.func_77955_b(stackCompound));
            }
         }

         tagger.func_74782_a("customDrops", list);
      }

      tagger.func_74757_a("customDrop", this.doCustomDrop);
      tagger.func_74757_a("timeEnabled", this.timeEnabled);
      tagger.func_74757_a("grotto", this.grotto);
      if (this.isEvent) {
         tagger.func_74757_a("specialEvent", this.isEvent);
      }

      tagger.func_74777_a("visibility", (short)this.visibility.ordinal());
      return tagger;
   }

   public void writeToNBTClient(NBTTagCompound tagger) {
      if (this.isEvent) {
         tagger.func_74757_a("specialEvent", this.isEvent);
      }

      tagger.func_74777_a("visibility", (short)this.visibility.ordinal());
      super.func_189515_b(tagger);
   }

   public void readFromNBTClient(NBTTagCompound tagger) {
      this.isEvent = tagger.func_74764_b("specialEvent");
      this.visibility = EnumPokechestVisibility.values()[tagger.func_74765_d("visibility")];
      super.func_145839_a(tagger);
   }

   public void func_145839_a(NBTTagCompound tagger) {
      if (tagger.func_74763_f("ownerIDMost") != -1L) {
         this.ownerID = new UUID(tagger.func_74763_f("ownerIDMost"), tagger.func_74763_f("ownerIDLeast"));
      }

      this.chestOneTime = tagger.func_74767_n("chestOneTime") && !tagger.func_74764_b("specialEvent");
      this.dropOneTime = tagger.func_74767_n("dropOneTime") || tagger.func_74764_b("specialEvent");
      int i;
      if (tagger.func_74764_b("claimedPlayers")) {
         NBTTagCompound claimedTag = (NBTTagCompound)tagger.func_74781_a("claimedPlayers");

         for(i = 0; claimedTag.func_74764_b("player" + i); ++i) {
            NBTTagCompound playerTag = (NBTTagCompound)claimedTag.func_74781_a("player" + i);
            this.claimed.add(new LootClaim(new UUID(playerTag.func_74763_f("most"), playerTag.func_74763_f("least")), playerTag.func_74763_f("timeClaimed")));
         }
      }

      this.doCustomDrop = tagger.func_74767_n("customDrop");
      if (tagger.func_74764_b("customDropID")) {
         this.customDrops = new ItemStack[]{new ItemStack(Item.func_150899_d(tagger.func_74762_e("customDropID")))};
      } else {
         NBTTagList dropList = tagger.func_150295_c("customDrops", 10);
         this.customDrops = new ItemStack[dropList.func_74745_c()];

         for(i = 0; i < this.customDrops.length; ++i) {
            this.customDrops[i] = new ItemStack(dropList.func_150305_b(i));
         }
      }

      this.timeEnabled = tagger.func_74767_n("timeEnabled") && !tagger.func_74764_b("specialEvent");
      this.grotto = tagger.func_74767_n("grotto");
      this.isEvent = tagger.func_74764_b("specialEvent");
      this.visibility = EnumPokechestVisibility.values()[tagger.func_74765_d("visibility")];
      super.func_145839_a(tagger);
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = super.func_189517_E_();
      this.writeToNBTClient(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.readFromNBTClient(pkt.func_148857_g());
   }

   public boolean canClaim(UUID playerID) {
      if (!this.dropOneTime) {
         return true;
      } else {
         LootClaim claim = this.getLootClaim(playerID);
         if (claim != null) {
            return this.timeEnabled && (System.currentTimeMillis() - claim.getTimeClaimed()) / 1000L > (long)(PixelmonConfig.lootTime * 3600);
         } else {
            return true;
         }
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
      } while(!claim.getPlayerID().equals(playerID));

      return claim;
   }

   public void addClaimer(UUID playerID) {
      if (this.dropOneTime || this.timeEnabled) {
         this.claimed.add(new LootClaim(playerID, System.currentTimeMillis()));
      }

   }

   public void removeClaimer(UUID playerID) {
      this.claimed.remove(this.getLootClaim(playerID));
   }

   public boolean shouldBreakBlock() {
      return this.chestOneTime && !this.timeEnabled;
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

   public boolean isCustomDrop() {
      return this.doCustomDrop && this.customDrops != null;
   }

   public boolean isTimeEnabled() {
      return this.timeEnabled;
   }

   public void setTimeEnabled(boolean val) {
      this.timeEnabled = val;
   }

   public ItemStack[] getCustomDrops() {
      if (this.isEvent) {
         ItemStack[] var1 = this.customDrops;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack s = var1[var3];
            if (!s.func_77985_e()) {
               if (!s.func_77942_o()) {
                  NBTTagCompound nbt = new NBTTagCompound();
                  nbt.func_74757_a("specialEvent", true);
                  s.func_77982_d(nbt);
               } else {
                  s.func_77978_p().func_74757_a("specialEvent", true);
               }
            }
         }
      }

      return this.customDrops;
   }

   public void setCustomDrops(ItemStack... customDrops) {
      this.doCustomDrop = true;
      this.customDrops = new ItemStack[customDrops.length];
      int index = 0;
      ItemStack[] var3 = customDrops;
      int var4 = customDrops.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack stack = var3[var5];
         this.customDrops[index] = stack.func_77946_l();
         ++index;
      }

   }

   public void setCustomDropEnabled(boolean enabled) {
      this.doCustomDrop = enabled;
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) < 64.0;
   }

   public void setFrontFace(int face) {
      this.frontFace = face;
   }

   public int getFrontFace() {
      return this.frontFace;
   }

   public void setSpecialEventDrop(ItemStack itemStack) {
      this.isEvent = true;
      this.chestOneTime = false;
      this.dropOneTime = true;
      this.setCustomDrops(itemStack);
   }

   public EnumPokechestVisibility getVisibility() {
      return this.visibility;
   }

   public void setVisibility(EnumPokechestVisibility visible) {
      this.visibility = visible;
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }
}
