package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CurryFinishedEvent;
import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.items.ItemCurryKey;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCookingPot extends TileEntity implements IInventory, ITickable {
   public int renderPass = 0;
   public ItemStackHandler itemStacks = new ItemStackHandler(11) {
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
         return slot == 10 ? stack.func_77973_b() instanceof ItemCurryKey : stack.func_77973_b() instanceof ItemBerry;
      }
   };
   private boolean cooking = false;
   private int cookingTicks = 0;
   private int berryQuality = 0;
   private EnumBerryFlavor cookingFlavor;
   public double fanningPoints;
   private double stirringPoints;
   private EnumCurryKey curryKey;
   private EntityPlayerMP startingPlayer;
   public double frame;

   public TileEntityCookingPot() {
      this.cookingFlavor = EnumBerryFlavor.NONE;
      this.fanningPoints = 0.0;
      this.stirringPoints = 0.0;
      this.curryKey = EnumCurryKey.NONE;
      this.startingPlayer = null;
      this.frame = 0.0;
   }

   public int func_70302_i_() {
      return this.itemStacks.getSlots();
   }

   public boolean func_191420_l() {
      for(int i = 0; i < this.itemStacks.getSlots(); ++i) {
         if (this.itemStacks.getStackInSlot(i) != ItemStack.field_190927_a) {
            return false;
         }
      }

      return true;
   }

   public ItemStack func_70301_a(int slotIndex) {
      return this.itemStacks.getStackInSlot(slotIndex);
   }

   public ItemStack func_70298_a(int index, int count) {
      if (!this.itemStacks.getStackInSlot(index).func_190926_b()) {
         ItemStack stack;
         if (this.itemStacks.getStackInSlot(index).func_190916_E() <= count) {
            stack = this.itemStacks.getStackInSlot(index);
            this.itemStacks.setStackInSlot(index, ItemStack.field_190927_a);
         } else {
            stack = this.itemStacks.getStackInSlot(index).func_77979_a(count);
            if (this.itemStacks.getStackInSlot(index).func_190916_E() == 0) {
               this.itemStacks.setStackInSlot(index, ItemStack.field_190927_a);
            }
         }

         return stack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int index) {
      if (!this.itemStacks.getStackInSlot(index).func_190926_b()) {
         ItemStack itemStack = this.itemStacks.getStackInSlot(index);
         this.itemStacks.setStackInSlot(index, ItemStack.field_190927_a);
         return itemStack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.itemStacks.setStackInSlot(index, stack);
      if (!stack.func_190926_b() && stack.func_190916_E() > this.func_70297_j_()) {
         stack.func_190920_e(this.func_70297_j_());
      }

   }

   public int func_70297_j_() {
      return 1;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) <= 64.0;
   }

   public boolean func_94041_b(int slotIndex, ItemStack itemStack) {
      Item item = itemStack.func_77973_b();
      switch (slotIndex) {
         case 10:
            return item instanceof ItemCurryKey;
         default:
            return item instanceof ItemBerry;
      }
   }

   public void func_174889_b(EntityPlayer playerIn) {
   }

   public void func_174886_c(EntityPlayer playerIn) {
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
   }

   public String func_70005_c_() {
      return null;
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public boolean shouldRenderInPass(int pass) {
      this.renderPass = pass;
      return true;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.isCooking()) {
            ++this.cookingTicks;
            if (this.cookingTicks < 300) {
               this.fanningPoints -= 0.05;
               this.fanningPoints = Math.max(0.0, this.fanningPoints);
            } else if (this.cookingTicks < 600) {
               this.stirringPoints -= 0.05;
               this.stirringPoints = Math.max(0.0, this.stirringPoints);
               this.frame += this.stirringPoints / 20.0;
               if (this.frame > 80.0) {
                  this.frame = 0.0;
               }
            } else {
               this.cooking = false;
               ItemStack curry = new ItemStack(this.curryKey.getDishItem());
               NBTTagCompound tag = curry.func_77978_p();
               if (tag == null) {
                  tag = new NBTTagCompound();
               }

               int quality = (int)((double)this.berryQuality + Math.max(0.0, 25.0 - Math.abs(25.0 - this.fanningPoints)) + Math.max(0.0, 25.0 - Math.abs(25.0 - this.stirringPoints)) + (double)this.curryKey.getRatingBoost());
               EnumCurryRating rating = EnumCurryRating.ratingFromQuality(quality);
               tag.func_74774_a("CurryQuality", (byte)rating.ordinal());
               if (this.curryKey != EnumCurryKey.GIGANTAMIX) {
                  tag.func_74774_a("BerryFlavor", (byte)this.cookingFlavor.ordinal());
               }

               curry.func_77982_d(tag);
               this.field_145850_b.func_72838_d(new EntityItem(this.field_145850_b, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o() + 2.5, (double)this.field_174879_c.func_177952_p(), curry));
               Pixelmon.EVENT_BUS.post(new CurryFinishedEvent(this.startingPlayer, rating, this.curryKey, this.cookingFlavor));
               if (this.startingPlayer != null) {
                  Pixelmon.storageManager.getParty(this.startingPlayer).updateSingleCurryData(this.curryKey, this.cookingFlavor, rating);
                  BlockSpawningHandler.getInstance().performBattleStartCheck(this.field_145850_b, this.field_174879_c, this.startingPlayer, (EntityPixelmon)null, EnumBattleStartTypes.CURRY, (IBlockState)null, this.cookingFlavor, rating);
               }

               this.startingPlayer = null;
            }

            this.sendUpdates();
         }
      } else if (this.isCooking()) {
         EnumParticleTypes type = null;
         if (this.fanningPoints > 25.0) {
            if (this.field_145850_b.field_73012_v.nextInt((int)this.fanningPoints) > 15) {
               type = EnumParticleTypes.SMOKE_LARGE;
            } else {
               type = EnumParticleTypes.FLAME;
            }
         } else if ((double)this.field_145850_b.field_73012_v.nextInt(25) < this.fanningPoints) {
            type = EnumParticleTypes.FLAME;
         }

         if (type != null) {
            this.func_145831_w().func_175682_a(type, false, (double)this.field_174879_c.func_177958_n() + 0.5 + this.field_145850_b.field_73012_v.nextDouble() / 2.0 - 0.25, (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p() + 0.5 + this.field_145850_b.field_73012_v.nextDouble() / 2.0 - 0.25, this.field_145850_b.field_73012_v.nextDouble() / 20.0, this.field_145850_b.field_73012_v.nextDouble() / 30.0, this.field_145850_b.field_73012_v.nextDouble() / 20.0, new int[0]);
         }

         if (this.stirringPoints > 25.0 && this.field_145850_b.field_73012_v.nextInt((int)this.stirringPoints) > 15) {
            this.func_145831_w().func_175682_a(EnumParticleTypes.DRAGON_BREATH, false, (double)this.field_174879_c.func_177958_n() + 0.5 + this.field_145850_b.field_73012_v.nextDouble() / 2.0 - 0.25, (double)this.field_174879_c.func_177956_o() + 1.25, (double)this.field_174879_c.func_177952_p() + 0.5 + this.field_145850_b.field_73012_v.nextDouble() / 2.0 - 0.25, this.field_145850_b.field_73012_v.nextDouble() / 30.0, this.field_145850_b.field_73012_v.nextDouble() / 20.0, this.field_145850_b.field_73012_v.nextDouble() / 30.0, new int[0]);
         }
      }

   }

   private IBlockState getState() {
      return this.field_145850_b.func_180495_p(this.field_174879_c);
   }

   private void sendUpdates() {
      this.field_145850_b.func_175704_b(this.field_174879_c, this.field_174879_c);
      this.field_145850_b.func_184138_a(this.field_174879_c, this.getState(), this.getState(), 3);
      this.field_145850_b.func_180497_b(this.field_174879_c, this.func_145838_q(), 0, 0);
      this.func_70296_d();
   }

   public boolean isCooking() {
      return this.cooking;
   }

   public boolean canStart() {
      for(int i = 0; i < this.itemStacks.getSlots() - 1; ++i) {
         if (!this.itemStacks.getStackInSlot(i).func_190926_b()) {
            return true;
         }
      }

      return false;
   }

   public void startCooking(EntityPlayerMP player) {
      this.startingPlayer = player;
      int[] flavors = new int[]{0, 0, 0, 0, 0};
      byte count = 0;

      int maxAt;
      for(maxAt = 0; maxAt < this.itemStacks.getSlots() - 1; ++maxAt) {
         if (!this.itemStacks.getStackInSlot(maxAt).func_190926_b()) {
            EnumBerry berry = ((ItemBerry)this.itemStacks.getStackInSlot(maxAt).func_77973_b()).getBerry();
            flavors[0] += berry.spicy;
            flavors[1] += berry.dry;
            flavors[2] += berry.sweet;
            flavors[3] += berry.bitter;
            flavors[4] += berry.sour;
            ++count;
            this.itemStacks.getStackInSlot(maxAt).func_190918_g(1);
         }
      }

      this.berryQuality = (flavors[0] + flavors[1] + flavors[2] + flavors[3] + flavors[4]) / (count + 2) + count * 2;
      maxAt = -1;
      int maxVal = 0;

      int i;
      for(i = 0; i < flavors.length; ++i) {
         if (flavors[i] > maxVal) {
            maxAt = i;
            maxVal = flavors[i];
         }
      }

      for(i = 0; i < flavors.length; ++i) {
         if (i != maxAt && flavors[i] + 20 >= maxVal) {
            maxAt = -1;
            break;
         }
      }

      if (maxAt != -1) {
         this.cookingFlavor = EnumBerryFlavor.values()[maxAt];
      } else {
         this.cookingFlavor = EnumBerryFlavor.NONE;
      }

      if (this.itemStacks.getStackInSlot(10) != null && !this.itemStacks.getStackInSlot(10).func_190926_b()) {
         this.curryKey = ((ItemCurryKey)this.itemStacks.getStackInSlot(10).func_77973_b()).getKey();
         this.itemStacks.getStackInSlot(10).func_190918_g(1);
      } else {
         this.curryKey = EnumCurryKey.NONE;
      }

      this.fanningPoints = 15.0;
      this.stirringPoints = 10.0;
      this.cookingTicks = 0;
      this.cooking = true;
   }

   public void processCookingInteract() {
      if (this.cookingTicks < 200) {
         ++this.fanningPoints;
      } else {
         ++this.stirringPoints;
      }

   }

   public void func_145839_a(NBTTagCompound nbtTagCompound) {
      super.func_145839_a(nbtTagCompound);
      this.itemStacks.deserializeNBT(nbtTagCompound.func_74775_l("Items"));
      this.cooking = nbtTagCompound.func_74767_n("Cooking");
      this.fanningPoints = (double)nbtTagCompound.func_74771_c("FanPoints");
      this.stirringPoints = (double)nbtTagCompound.func_74771_c("StirPoints");
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtTagCompound) {
      super.func_189515_b(nbtTagCompound);
      nbtTagCompound.func_74782_a("Items", this.itemStacks.serializeNBT());
      nbtTagCompound.func_74757_a("Cooking", this.isCooking());
      nbtTagCompound.func_74774_a("FanPoints", (byte)((int)this.fanningPoints));
      nbtTagCompound.func_74774_a("StirPoints", (byte)((int)this.stirringPoints));
      return nbtTagCompound;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbtTagCompound = super.func_189517_E_();
      nbtTagCompound.func_74757_a("Cooking", this.isCooking());
      nbtTagCompound.func_74774_a("FanPoints", (byte)((int)this.fanningPoints));
      nbtTagCompound.func_74774_a("StirPoints", (byte)((int)this.stirringPoints));
      nbtTagCompound.func_74780_a("frame", this.frame);
      return nbtTagCompound;
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbtTagCompound = pkt.func_148857_g();
      this.cooking = nbtTagCompound.func_74767_n("Cooking");
      this.fanningPoints = (double)nbtTagCompound.func_74771_c("FanPoints");
      this.stirringPoints = (double)nbtTagCompound.func_74771_c("StirPoints");
      this.frame = nbtTagCompound.func_74769_h("frame");
   }
}
