package com.pixelmonmod.pixelmon.blocks.tileEntities;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWashingMachine extends TileEntity {
   private int furnaceBurnTime = 0;
   private ItemStackHandler cooking = new ItemStackHandler(1);
   private ItemStackHandler cooked = new ItemStackHandler(1);

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      nbt.func_74768_a("smeltTime", this.furnaceBurnTime);
      nbt.func_74782_a("Cooking", this.cooking.serializeNBT());
      nbt.func_74782_a("Cooked", this.cooked.serializeNBT());
      return super.func_189515_b(nbt);
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.cooking.deserializeNBT(nbt.func_74775_l("Cooking"));
      this.cooked.deserializeNBT(nbt.func_74775_l("Cooked"));
   }

   public boolean hasCapability(Capability capability, @Nullable EnumFacing facing) {
      return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN) || super.hasCapability(capability, facing);
   }

   @Nullable
   public Object getCapability(Capability capability, @Nullable EnumFacing facing) {
      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
         if (facing == EnumFacing.UP) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.cooking);
         }

         if (facing == EnumFacing.DOWN) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.cooking);
         }
      }

      return super.getCapability(capability, facing);
   }

   public void update() {
      if (!this.field_145850_b.field_72995_K) {
         ++this.furnaceBurnTime;
         if (this.furnaceBurnTime >= 75) {
            this.furnaceBurnTime = 0;
            this.smeltItem();
         }

         this.func_70296_d();
      }

   }

   private boolean canSmelt() {
      if (this.cooking.getStackInSlot(0).func_190926_b()) {
         return false;
      } else {
         ItemStack itemstack = this.cooking.getStackInSlot(0).func_77946_l();
         if (itemstack.func_190926_b()) {
            return false;
         } else {
            EnumDyeColor enumdyecolor = EnumDyeColor.func_176766_a(itemstack.func_77960_j());
            if (enumdyecolor == EnumDyeColor.BLACK) {
               return false;
            } else {
               ItemStack itemstack1 = this.cooked.getStackInSlot(0);
               ItemStack itemstack2 = itemstack.func_77946_l();
               itemstack2.func_190920_e(1);
               if (itemstack1.func_190926_b()) {
                  return true;
               } else if (itemstack1.func_77973_b() == itemstack2.func_77973_b() && itemstack1.func_77985_e()) {
                  return true;
               } else if (itemstack1.func_190916_E() + itemstack2.func_190916_E() <= this.cooked.getSlotLimit(0) && itemstack1.func_190916_E() + itemstack2.func_190916_E() <= itemstack1.func_77976_d()) {
                  return true;
               } else {
                  return itemstack1.func_190916_E() + itemstack.func_190916_E() <= itemstack.func_77976_d();
               }
            }
         }
      }
   }

   public void smeltItem() {
      if (this.canSmelt()) {
         ItemStack itemstack = this.cooking.getStackInSlot(0);
         ItemStack itemstack1 = itemstack.func_77946_l();
         itemstack1.func_190920_e(1);
         itemstack1.func_77964_b(0);
         ItemStack itemstack2 = this.cooked.getStackInSlot(0);
         if (itemstack2.func_190926_b()) {
            this.cooked.setStackInSlot(0, itemstack1);
         } else if (itemstack2.func_77985_e() && itemstack2.func_77969_a(itemstack1)) {
            itemstack2.func_190917_f(itemstack1.func_190916_E());
         }

         itemstack.func_190918_g(1);
      }

   }

   private IBlockState getState() {
      return this.field_145850_b.func_180495_p(this.field_174879_c);
   }

   public void add(ItemStack stack) {
      this.cooked.insertItem(0, stack, false);
   }

   public boolean isRunning() {
      return this.canSmelt();
   }

   public int getSmeltTimeRemainingScaled(int i) {
      return (int)((double)i * ((double)this.furnaceBurnTime / 120.0));
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound tag = new NBTTagCompound();
      this.func_189515_b(tag);
      return tag;
   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
      return oldState.func_177230_c() != newState.func_177230_c();
   }

   public ItemStackHandler getCooked() {
      return this.cooked;
   }

   public ItemStackHandler getCooking() {
      return this.cooking;
   }

   public int getSmeltTime() {
      return this.furnaceBurnTime;
   }

   public void setSmeltTime(int smeltTime) {
      this.furnaceBurnTime = smeltTime;
   }
}
