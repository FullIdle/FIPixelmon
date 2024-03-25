package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.MechanicalAnvilEvent;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPokeballDisc;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;

public class TileEntityMechanicalAnvil extends TileEntity implements ISidedInventory, ITickable {
   private NonNullList anvilItemStacks;
   public int fuelBurnTime;
   public int currentFuelBurnTime;
   public int anvilHammerTime;
   public Item pokeBallType;
   private int stateTimer;
   public int state;
   public int frame;

   public TileEntityMechanicalAnvil() {
      this.anvilItemStacks = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
      this.stateTimer = 0;
      this.state = 0;
      this.frame = 0;
   }

   public int func_70302_i_() {
      return this.anvilItemStacks.size();
   }

   public boolean func_191420_l() {
      Iterator var1 = this.anvilItemStacks.iterator();

      ItemStack itemstack;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         itemstack = (ItemStack)var1.next();
      } while(itemstack == null || itemstack.func_190926_b());

      return false;
   }

   public ItemStack func_70301_a(int slotIndex) {
      return (ItemStack)this.anvilItemStacks.get(slotIndex);
   }

   public ItemStack func_70298_a(int slotIndex, int decreaseBy) {
      if (!((ItemStack)this.anvilItemStacks.get(slotIndex)).func_190926_b()) {
         ItemStack itemStack;
         if (((ItemStack)this.anvilItemStacks.get(slotIndex)).func_190916_E() <= decreaseBy) {
            itemStack = (ItemStack)this.anvilItemStacks.get(slotIndex);
            this.anvilItemStacks.set(slotIndex, ItemStack.field_190927_a);
         } else {
            itemStack = ((ItemStack)this.anvilItemStacks.get(slotIndex)).func_77979_a(decreaseBy);
            if (((ItemStack)this.anvilItemStacks.get(slotIndex)).func_190916_E() == 0) {
               this.anvilItemStacks.set(slotIndex, ItemStack.field_190927_a);
            }
         }

         return itemStack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int slotIndex) {
      if (!((ItemStack)this.anvilItemStacks.get(slotIndex)).func_190926_b()) {
         ItemStack itemStack = (ItemStack)this.anvilItemStacks.get(slotIndex);
         MechanicalAnvilEvent.RemoveStack event = new MechanicalAnvilEvent.RemoveStack(this, itemStack, slotIndex);
         Pixelmon.EVENT_BUS.post(event);
         this.anvilItemStacks.set(slotIndex, ItemStack.field_190927_a);
         return event.getItem();
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void func_70299_a(int slotIndex, ItemStack itemStack) {
      MechanicalAnvilEvent.SetStack event = new MechanicalAnvilEvent.SetStack(this, itemStack, slotIndex);
      Pixelmon.EVENT_BUS.post(event);
      ItemStack stack = event.getItem();
      this.anvilItemStacks.set(slotIndex, stack);
      if (!stack.func_190926_b() && stack.func_190916_E() > this.func_70297_j_()) {
         stack.func_190920_e(this.func_70297_j_());
      }

   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) <= 64.0;
   }

   public boolean func_94041_b(int slotIndex, ItemStack itemStack) {
      Item item = itemStack.func_77973_b();
      switch (slotIndex) {
         case 0:
            return item.equals(PixelmonItemsPokeballs.aluDisc) || item.equals(PixelmonItemsPokeballs.ironDisc) || item instanceof ItemPokeballDisc;
         case 1:
            return TileEntityFurnace.func_145954_b(itemStack);
         case 2:
            return false;
         default:
            return false;
      }
   }

   public int[] func_180463_a(EnumFacing side) {
      return new int[]{0, 1, 2};
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return this.func_94041_b(index, itemStackIn);
   }

   public boolean func_180461_b(int slotIndex, ItemStack itemStack, EnumFacing direction) {
      switch (slotIndex) {
         case 0:
            return false;
         case 1:
            return itemStack.func_77973_b() == Items.field_151133_ar;
         case 2:
            return true;
         default:
            return false;
      }
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound compound = super.func_189517_E_();
      compound.func_74777_a("RunTime", (short)this.fuelBurnTime);
      compound.func_74782_a("itemInAnvil", ((ItemStack)this.anvilItemStacks.get(0)).func_77955_b(new NBTTagCompound()));
      return compound;
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.func_148857_g();
      this.fuelBurnTime = compound.func_74765_d("RunTime");
      this.pokeBallType = (new ItemStack(compound.func_74775_l("itemInAnvil"))).func_77973_b();
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void func_145839_a(NBTTagCompound nbtTagCompound) {
      super.func_145839_a(nbtTagCompound);
      NBTTagList nbttaglist = nbtTagCompound.func_150295_c("Items", 10);
      this.anvilItemStacks = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         if (b0 >= 0 && b0 < this.anvilItemStacks.size()) {
            this.anvilItemStacks.set(b0, new ItemStack(nbttagcompound1));
         }
      }

      this.fuelBurnTime = nbtTagCompound.func_74765_d("RunTime");
      this.currentFuelBurnTime = TileEntityFurnace.func_145952_a((ItemStack)this.anvilItemStacks.get(1));
      this.anvilHammerTime = nbtTagCompound.func_74765_d("HammerTime");
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtTagCompound) {
      super.func_189515_b(nbtTagCompound);
      nbtTagCompound.func_74777_a("RunTime", (short)this.fuelBurnTime);
      nbtTagCompound.func_74777_a("HammerTime", (short)this.anvilHammerTime);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.anvilItemStacks.size(); ++i) {
         if (!((ItemStack)this.anvilItemStacks.get(i)).func_190926_b()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            ((ItemStack)this.anvilItemStacks.get(i)).func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbtTagCompound.func_74782_a("Items", nbttaglist);
      return nbtTagCompound;
   }

   public boolean isRunning() {
      return this.fuelBurnTime > 0;
   }

   public void func_73660_a() {
      if (!Pixelmon.EVENT_BUS.post(new MechanicalAnvilEvent.Tick.Pre(this))) {
         boolean flag = this.isRunning();
         boolean flag1 = false;
         if (!this.field_145850_b.field_72995_K) {
            if (this.fuelBurnTime > 0) {
               --this.fuelBurnTime;
               if (this.fuelBurnTime == 0) {
                  ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
               }
            }

            if (this.fuelBurnTime != 0 || !((ItemStack)this.anvilItemStacks.get(1)).func_190926_b() && !((ItemStack)this.anvilItemStacks.get(0)).func_190926_b()) {
               if (this.fuelBurnTime == 0 && this.canHammer()) {
                  this.currentFuelBurnTime = this.fuelBurnTime = TileEntityFurnace.func_145952_a((ItemStack)this.anvilItemStacks.get(1));
                  ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
                  if (this.fuelBurnTime > 0) {
                     flag1 = true;
                     if (!((ItemStack)this.anvilItemStacks.get(1)).func_190926_b()) {
                        if (((ItemStack)this.anvilItemStacks.get(1)).func_190916_E() == 1) {
                           this.anvilItemStacks.set(1, ((ItemStack)this.anvilItemStacks.get(1)).func_77973_b().getContainerItem((ItemStack)this.anvilItemStacks.get(1)));
                        } else {
                           ((ItemStack)this.anvilItemStacks.get(1)).func_190918_g(1);
                        }
                     }
                  }
               }

               if (this.isRunning() && this.canHammer()) {
                  ++this.anvilHammerTime;
                  if (this.anvilHammerTime == 200) {
                     this.anvilHammerTime = 0;
                     this.hammerItem();
                     flag1 = true;
                  }
               } else {
                  this.anvilHammerTime = 0;
               }
            }

            if (flag != this.fuelBurnTime > 0) {
               flag1 = true;
            }
         }

         if (flag1) {
            this.func_70296_d();
         }

         if (this.isRunning()) {
            this.frame += 2;
            if (this.frame == 200) {
               this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187599_cE, SoundCategory.BLOCKS, 0.7F, 1.0F);
            }

            if (this.frame > 399) {
               this.frame = 0;
            }

            ++this.stateTimer;
            if (this.stateTimer > 66 && this.state == 0) {
               this.state = 1;
            }

            if (this.stateTimer > 133 && this.state == 1) {
               this.state = 2;
            }

            if (this.stateTimer >= 199) {
               this.state = 0;
            }

            if (this.stateTimer == 200) {
               this.stateTimer = 0;
            }
         } else {
            this.frame = 0;
            this.stateTimer = 0;
            this.state = 0;
         }

         Pixelmon.EVENT_BUS.post(new MechanicalAnvilEvent.Tick.Post(this));
      }
   }

   private boolean canHammer() {
      if (((ItemStack)this.anvilItemStacks.get(0)).func_190926_b()) {
         return false;
      } else {
         ItemStack itemstack = getHammerResult(this, (ItemStack)this.anvilItemStacks.get(0));
         if (itemstack == null) {
            return false;
         } else if (((ItemStack)this.anvilItemStacks.get(2)).func_190926_b()) {
            return true;
         } else if (!((ItemStack)this.anvilItemStacks.get(2)).func_77969_a(itemstack)) {
            return false;
         } else {
            int result = ((ItemStack)this.anvilItemStacks.get(2)).func_190916_E() + itemstack.func_190916_E();
            return result <= this.func_70297_j_() && result <= ((ItemStack)this.anvilItemStacks.get(2)).func_77976_d();
         }
      }
   }

   public void hammerItem() {
      if (this.canHammer()) {
         ItemStack itemstack = getHammerResult(this, (ItemStack)this.anvilItemStacks.get(0));
         MechanicalAnvilEvent.Hammer event = new MechanicalAnvilEvent.Hammer(this, itemstack);
         if (!Pixelmon.EVENT_BUS.post(event)) {
            itemstack = event.result;
            if (((ItemStack)this.anvilItemStacks.get(2)).func_190926_b()) {
               this.anvilItemStacks.set(2, itemstack.func_77946_l());
            } else if (((ItemStack)this.anvilItemStacks.get(2)).func_77973_b() == itemstack.func_77973_b()) {
               ((ItemStack)this.anvilItemStacks.get(2)).func_190920_e(((ItemStack)this.anvilItemStacks.get(2)).func_190916_E() + itemstack.func_190916_E());
            }

            ((ItemStack)this.anvilItemStacks.get(0)).func_190918_g(1);
            if (((ItemStack)this.anvilItemStacks.get(0)).func_190916_E() <= 0) {
               this.anvilItemStacks.set(0, ItemStack.field_190927_a);
            }
         }
      }

   }

   public static ItemStack getHammerResult(TileEntityMechanicalAnvil anvil, ItemStack itemStack) {
      Item itemInAnvil = itemStack.func_77973_b();
      ItemStack result = ItemStack.field_190927_a;
      if (itemInAnvil == PixelmonItemsPokeballs.aluDisc) {
         result = new ItemStack(PixelmonItemsPokeballs.aluBase);
      } else if (itemInAnvil == PixelmonItemsPokeballs.ironDisc) {
         result = new ItemStack(PixelmonItemsPokeballs.ironBase);
      } else if (itemInAnvil instanceof ItemPokeballDisc) {
         result = new ItemStack(PixelmonItemsPokeballs.getLidFromEnum(((ItemPokeballDisc)itemInAnvil).pokeball), 1);
      }

      MechanicalAnvilEvent.HammerResult event = new MechanicalAnvilEvent.HammerResult(anvil, result);
      Pixelmon.EVENT_BUS.post(event);
      return event.result;
   }

   public void func_174889_b(EntityPlayer playerIn) {
   }

   public void func_174886_c(EntityPlayer playerIn) {
   }

   public int func_174887_a_(int id) {
      switch (id) {
         case 0:
            return this.fuelBurnTime;
         case 1:
            return this.currentFuelBurnTime;
         case 2:
            return this.anvilHammerTime;
         default:
            return 0;
      }
   }

   public void func_174885_b(int id, int value) {
      switch (id) {
         case 0:
            this.fuelBurnTime = value;
            break;
         case 1:
            this.currentFuelBurnTime = value;
            break;
         case 2:
            this.anvilHammerTime = value;
      }

   }

   public int func_174890_g() {
      return 3;
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
}
