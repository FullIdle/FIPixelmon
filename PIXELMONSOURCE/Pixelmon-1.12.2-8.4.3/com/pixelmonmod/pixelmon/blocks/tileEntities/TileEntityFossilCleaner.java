package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileEntityFossilCleaner extends TileEntity implements ISidedInventory, IBasicInventory, ITickable {
   public Item itemInCleaner = null;
   public int timer = 360;
   public int renderPass = 0;

   public boolean isOn() {
      return this.itemInCleaner != null && this.timer > 0 && !this.isFossilClean();
   }

   public boolean isFossilClean() {
      return this.itemInCleaner != null && this.itemInCleaner instanceof ItemFossil;
   }

   public void setItemInCleaner(Item item) {
      if (item == null) {
         this.itemInCleaner = null;
         this.timer = 0;
         this.field_145850_b.func_175685_c(this.field_174879_c, this.func_145838_q(), true);
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
      } else if (item instanceof ItemFossil || item instanceof ItemCoveredFossil) {
         this.itemInCleaner = item;
         this.timer = 360;
         this.field_145850_b.func_175685_c(this.field_174879_c, this.func_145838_q(), true);
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   public boolean func_191420_l() {
      return this.itemInCleaner == null;
   }

   public Item getItemInCleaner() {
      return this.itemInCleaner;
   }

   public void func_73660_a() {
      if (this.itemInCleaner != null && !this.isFossilClean()) {
         this.func_145831_w().func_175685_c(this.field_174879_c, this.func_145838_q(), true);
         if (this.timer > 0) {
            --this.timer;
         } else if (this.itemInCleaner instanceof ItemCoveredFossil) {
            this.itemInCleaner = ((ItemCoveredFossil)this.itemInCleaner).cleanedFossil;
            if (this.field_145850_b instanceof WorldServer) {
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            }
         }
      } else {
         this.timer = 360;
      }

   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("ItemIn") && nbt.func_74762_e("ItemIn") != 0) {
         this.itemInCleaner = Item.func_150899_d(nbt.func_74762_e("ItemIn"));
      } else {
         this.itemInCleaner = null;
      }

      if (nbt.func_74764_b("CleanerTimer")) {
         this.timer = nbt.func_74765_d("CleanerTimer");
      } else {
         this.timer = 360;
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.itemInCleaner != null) {
         nbt.func_74768_a("ItemIn", Item.func_150891_b(this.itemInCleaner));
      }

      nbt.func_74777_a("CleanerTimer", (short)this.timer);
      return nbt;
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
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

   public int func_70302_i_() {
      return 1;
   }

   public ItemStack func_70301_a(int index) {
      return index == 0 && this.itemInCleaner != null ? new ItemStack(this.itemInCleaner) : ItemStack.field_190927_a;
   }

   public ItemStack func_70304_b(int index) {
      if (index == 0 && this.itemInCleaner != null) {
         ItemStack stack = new ItemStack(this.itemInCleaner);
         this.setItemInCleaner((Item)null);
         return stack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void func_70299_a(int index, ItemStack stack) {
      if (index == 0) {
         this.setItemInCleaner(stack.func_77973_b());
      }

   }

   public int func_70297_j_() {
      return 1;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public boolean func_94041_b(int index, ItemStack stack) {
      return index == 0 && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemCoveredFossil;
   }

   public String func_70005_c_() {
      return "";
   }

   public int[] func_180463_a(EnumFacing side) {
      return new int[]{0};
   }

   public boolean func_180462_a(int index, ItemStack stack, EnumFacing direction) {
      return index == 0 && direction != EnumFacing.DOWN && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemCoveredFossil;
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing direction) {
      return index == 0 && direction == EnumFacing.DOWN && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemFossil;
   }
}
