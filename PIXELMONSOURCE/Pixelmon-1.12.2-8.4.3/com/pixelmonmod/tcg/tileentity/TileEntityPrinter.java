package com.pixelmonmod.tcg.tileentity;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.item.ItemBlankCard;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.PrinterSyncPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityPrinter extends TileEntity implements ISidedInventory, ITickable {
   public int count = 0;
   private ItemStack[] itemStacks;
   private short printTime;
   private short totalPrintTime;
   private String printerCustomName;
   private ItemStack printingCard;

   public short getPrintTime() {
      return this.printTime;
   }

   public short getTotalPrintTime() {
      return this.totalPrintTime;
   }

   public TileEntityPrinter() {
      this.itemStacks = new ItemStack[]{ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a, ItemStack.field_190927_a};
      this.printingCard = ItemStack.field_190927_a;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return new SPacketUpdateTileEntity(this.field_174879_c, 1, nbt);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.func_145839_a(nbt);
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      this.printTime = compound.func_74765_d("PrintTime");
      this.totalPrintTime = compound.func_74765_d("PrintTimeTotal");
      NBTTagList nbttaglist = compound.func_150295_c("Items", 10);
      this.itemStacks = new ItemStack[this.func_70302_i_()];

      int j;
      for(j = 0; j < nbttaglist.func_74745_c(); ++j) {
         NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(j);
         int slot = nbttagcompound.func_74771_c("Slot");
         if (slot == -1) {
            this.printingCard = new ItemStack(nbttagcompound);
         } else if (slot >= 0 && slot < this.itemStacks.length) {
            this.itemStacks[slot] = new ItemStack(nbttagcompound);
         }
      }

      if (compound.func_150297_b("CustomName", 8)) {
         this.printerCustomName = compound.func_74779_i("CustomName");
      }

      for(j = 0; j < this.itemStacks.length; ++j) {
         if (this.itemStacks[j] == null) {
            this.itemStacks[j] = ItemStack.field_190927_a;
         }
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74777_a("PrintTime", this.printTime);
      compound.func_74777_a("PrintTimeTotal", this.totalPrintTime);
      NBTTagList nbttaglist = new NBTTagList();
      if (!this.printingCard.func_190926_b()) {
         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.func_74774_a("Slot", (byte)-1);
         this.printingCard.func_77955_b(nbttagcompound);
         nbttaglist.func_74742_a(nbttagcompound);
      }

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (!this.itemStacks[i].func_190926_b()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.func_74774_a("Slot", (byte)i);
            this.itemStacks[i].func_77955_b(nbttagcompound);
            nbttaglist.func_74742_a(nbttagcompound);
         }
      }

      compound.func_74782_a("Items", nbttaglist);
      if (this.func_145818_k_()) {
         compound.func_74778_a("CustomName", this.printerCustomName);
      }

      return compound;
   }

   public boolean isPrinting() {
      return this.printTime > 0;
   }

   public void setPrintTime(short s) {
      this.printTime = s;
   }

   public void func_73660_a() {
      boolean isPrinting = this.isPrinting();
      boolean updated = false;
      if (!this.field_145850_b.field_72995_K) {
         if (this.isPrinting()) {
            ++this.printTime;
            if (this.printTime == this.totalPrintTime) {
               this.printCard();
               if (this.canPrint()) {
                  this.startPrinting();
               } else {
                  this.printTime = 0;
                  this.totalPrintTime = 0;
               }
            }

            this.field_145850_b.func_175684_a(this.func_174877_v(), this.field_145850_b.func_180495_p(this.func_174877_v()).func_177230_c(), 0);
            updated = true;
         }

         if (isPrinting != this.isPrinting()) {
            updated = true;
         }
      }

      if (updated) {
         PacketHandler.net.sendToDimension(new PrinterSyncPacket(this.func_174877_v(), this.printTime), this.field_145850_b.field_73011_w.getDimension());
         this.func_70296_d();
      }

   }

   private void preparePrintingCard() {
      try {
         ItemBlankCard blank = (ItemBlankCard)this.itemStacks[0].func_77973_b();
         ImmutableCard chosenCard;
         if (this.itemStacks[1].func_190926_b()) {
            chosenCard = CardRegistry.getRandomCard(blank.getEnergy());
         } else {
            int ndex = this.itemStacks[1].func_77978_p().func_74765_d("ndex");
            boolean shiny = this.itemStacks[1].func_77978_p().func_74767_n("Shiny");
            Energy blankEnergy = blank.getEnergy();
            chosenCard = CardRegistry.getRandomCard(ndex, blank.getRarity(), shiny, blankEnergy);
         }

         if (chosenCard != null) {
            this.printingCard = chosenCard.getItemStack(1);
            if (this.itemStacks[0].func_190916_E() > 0) {
               this.itemStacks[0].func_190920_e(this.itemStacks[0].func_190916_E() - 1);
            }

            if (this.itemStacks[0].func_190916_E() == 0) {
               this.itemStacks[0] = ItemStack.field_190927_a;
            }

            if (!this.itemStacks[1].func_190926_b()) {
               if (this.itemStacks[1].func_190916_E() > 0) {
                  this.itemStacks[1].func_190920_e(this.itemStacks[1].func_190916_E() - 1);
               }

               if (this.itemStacks[1].func_190916_E() == 0) {
                  this.itemStacks[1] = ItemStack.field_190927_a;
               }
            }
         } else {
            this.printTime = 0;
         }
      } catch (NumberFormatException var6) {
         TCG.logger.catching(var6);
      }

   }

   public short calculateTotalPrintTime(ItemStack blankCard, ItemStack photo) {
      return 200;
   }

   public void startPrinting() {
      this.printTime = 1;
      this.totalPrintTime = this.calculateTotalPrintTime(this.itemStacks[0], this.itemStacks[1]);
      this.preparePrintingCard();
      this.func_70296_d();
   }

   public boolean canPrint() {
      if (this.itemStacks[0].func_190926_b()) {
         return false;
      } else {
         for(int i = 0; i < 8; ++i) {
            if (this.itemStacks[i + 2].func_190926_b()) {
               return true;
            }
         }

         return false;
      }
   }

   private void printCard() {
      if (!this.printingCard.func_190926_b()) {
         int availableSlot = -1;

         for(int i = 0; i < 8; ++i) {
            if (this.itemStacks[i + 2].func_190926_b()) {
               availableSlot = i + 2;
               break;
            }
         }

         if (availableSlot >= 0) {
            this.itemStacks[availableSlot] = this.printingCard;
         }

         this.printingCard = ItemStack.field_190927_a;
         this.func_70296_d();
      }

   }

   public int[] func_180463_a(EnumFacing side) {
      return new int[0];
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return false;
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing direction) {
      return false;
   }

   public int func_70302_i_() {
      return this.itemStacks.length;
   }

   public boolean func_191420_l() {
      return false;
   }

   public ItemStack func_70301_a(int index) {
      return this.itemStacks[index];
   }

   public ItemStack func_70298_a(int index, int count) {
      if (!this.itemStacks[index].func_190926_b()) {
         ItemStack itemstack;
         if (this.itemStacks[index].func_190916_E() <= count) {
            itemstack = this.itemStacks[index];
            this.itemStacks[index] = ItemStack.field_190927_a;
            return itemstack;
         } else {
            itemstack = this.itemStacks[index].func_77979_a(count);
            if (this.itemStacks[index].func_190916_E() == 0) {
               this.itemStacks[index] = ItemStack.field_190927_a;
            }

            return itemstack;
         }
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int index) {
      if (!this.itemStacks[index].func_190926_b()) {
         ItemStack itemstack = this.itemStacks[index];
         this.itemStacks[index] = ItemStack.field_190927_a;
         return itemstack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void func_70299_a(int index, ItemStack itemStack) {
      this.itemStacks[index] = itemStack;
      this.func_70296_d();
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public boolean func_94041_b(int index, ItemStack stack) {
      return true;
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
      this.printingCard = ItemStack.field_190927_a;

      for(int i = 0; i < this.itemStacks.length; ++i) {
         this.itemStacks[i] = ItemStack.field_190927_a;
      }

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
}
