package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.api.recipe.InfuserRecipes;
import java.util.Iterator;
import java.util.Map;
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
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityInfuser extends TileEntity implements ISidedInventory, ITickable {
   private NonNullList infuserItemStacks;
   public int infuserRunTime;
   public int currentItemRunTime;
   public int infuserProgressTime;
   public int renderPass;
   public boolean isRunning;
   public Item itemOnInfuser;
   public Item secondItemOnInfuser;
   private int stateTimer;
   public int state;
   public int frame;

   public TileEntityInfuser() {
      this.infuserItemStacks = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
      this.renderPass = 0;
      this.isRunning = false;
      this.itemOnInfuser = null;
      this.secondItemOnInfuser = null;
      this.stateTimer = 0;
      this.state = 0;
      this.frame = 0;
   }

   public int func_70302_i_() {
      return this.infuserItemStacks.size();
   }

   public boolean func_191420_l() {
      Iterator var1 = this.infuserItemStacks.iterator();

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
      return (ItemStack)this.infuserItemStacks.get(slotIndex);
   }

   public ItemStack func_70298_a(int slotIndex, int decreaseBy) {
      if (!((ItemStack)this.infuserItemStacks.get(slotIndex)).func_190926_b()) {
         ItemStack itemStack;
         if (((ItemStack)this.infuserItemStacks.get(slotIndex)).func_190916_E() <= decreaseBy) {
            itemStack = (ItemStack)this.infuserItemStacks.get(slotIndex);
            this.infuserItemStacks.set(slotIndex, ItemStack.field_190927_a);
         } else {
            itemStack = ((ItemStack)this.infuserItemStacks.get(slotIndex)).func_77979_a(decreaseBy);
            if (((ItemStack)this.infuserItemStacks.get(slotIndex)).func_190916_E() == 0) {
               this.infuserItemStacks.set(slotIndex, ItemStack.field_190927_a);
            }
         }

         return itemStack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int slotIndex) {
      if (!((ItemStack)this.infuserItemStacks.get(slotIndex)).func_190926_b()) {
         ItemStack itemStack = (ItemStack)this.infuserItemStacks.get(slotIndex);
         this.infuserItemStacks.set(slotIndex, ItemStack.field_190927_a);
         return itemStack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public void func_70299_a(int slotIndex, ItemStack itemStack) {
      this.infuserItemStacks.set(slotIndex, itemStack);
      if (!itemStack.func_190926_b() && itemStack.func_190916_E() > this.func_70297_j_()) {
         itemStack.func_190920_e(this.func_70297_j_());
      }

   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) <= 64.0;
   }

   public boolean func_94041_b(int slotIndex, ItemStack itemStack) {
      switch (slotIndex) {
         case 0:
            return TileEntityFurnace.func_145954_b(itemStack);
         case 3:
            return false;
         default:
            return true;
      }
   }

   public int[] func_180463_a(EnumFacing side) {
      return new int[]{0, 1, 2, 3};
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return this.func_94041_b(index, itemStackIn);
   }

   public boolean func_180461_b(int slotIndex, ItemStack itemStack, EnumFacing direction) {
      switch (slotIndex) {
         case 0:
            return itemStack.func_77973_b() == Items.field_151133_ar;
         case 3:
            return true;
         default:
            return false;
      }
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      nbt.func_74757_a("isRunning", this.isRunning());
      if (!((ItemStack)this.infuserItemStacks.get(1)).func_190926_b()) {
         nbt.func_74782_a("itemInInfuser", ((ItemStack)this.infuserItemStacks.get(1)).func_77955_b(new NBTTagCompound()));
      }

      if (!((ItemStack)this.infuserItemStacks.get(2)).func_190926_b()) {
         nbt.func_74782_a("secondaryItemInInfuser", ((ItemStack)this.infuserItemStacks.get(1)).func_77955_b(new NBTTagCompound()));
      }

      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.isRunning = nbt.func_74767_n("isRunning");
      if (nbt.func_74764_b("itemInInfuser")) {
         this.itemOnInfuser = (new ItemStack(nbt.func_74775_l("itemInInfuser"))).func_77973_b();
      } else {
         this.itemOnInfuser = null;
      }

      if (nbt.func_74764_b("secondaryItemInInfuser")) {
         this.secondItemOnInfuser = (new ItemStack(nbt.func_74775_l("secondaryItemInInfuser"))).func_77973_b();
      } else {
         this.secondItemOnInfuser = null;
      }

      if (this.isRunning) {
         this.frame = 0;
      } else {
         this.itemOnInfuser = null;
         this.secondItemOnInfuser = null;
      }

   }

   public void func_145839_a(NBTTagCompound nbtTagCompound) {
      super.func_145839_a(nbtTagCompound);
      NBTTagList nbttaglist = nbtTagCompound.func_150295_c("Items", 10);
      this.infuserItemStacks = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         if (b0 >= 0 && b0 < this.infuserItemStacks.size()) {
            this.infuserItemStacks.set(b0, new ItemStack(nbttagcompound1));
         }
      }

      this.infuserRunTime = nbtTagCompound.func_74765_d("RunTime");
      this.infuserProgressTime = nbtTagCompound.func_74765_d("InfusionTime");
      this.currentItemRunTime = TileEntityFurnace.func_145952_a((ItemStack)this.infuserItemStacks.get(1));
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtTagCompound) {
      super.func_189515_b(nbtTagCompound);
      nbtTagCompound.func_74777_a("RunTime", (short)this.infuserRunTime);
      nbtTagCompound.func_74777_a("InfusionTime", (short)this.infuserProgressTime);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.infuserItemStacks.size(); ++i) {
         if (!((ItemStack)this.infuserItemStacks.get(i)).func_190926_b()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            ((ItemStack)this.infuserItemStacks.get(i)).func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbtTagCompound.func_74782_a("Items", nbttaglist);
      return nbtTagCompound;
   }

   @SideOnly(Side.CLIENT)
   public int getInfusionProgressScaled(int p_145953_1_) {
      int val;
      try {
         val = (Integer)((Tuple)InfuserRecipes.instance().getRecipe((ItemStack)this.infuserItemStacks.get(1), (ItemStack)this.infuserItemStacks.get(2)).getValue()).func_76340_b();
      } catch (Exception var4) {
         val = Integer.MAX_VALUE;
      }

      return this.infuserProgressTime * p_145953_1_ / val;
   }

   @SideOnly(Side.CLIENT)
   public int getBurnTimeRemainingScaled(int p_145955_1_) {
      if (this.currentItemRunTime == 0) {
         this.currentItemRunTime = 200;
      }

      return this.infuserRunTime * p_145955_1_ / this.currentItemRunTime;
   }

   public boolean isRunning() {
      return this.infuserRunTime > 0;
   }

   public void func_73660_a() {
      boolean flag = this.infuserRunTime > 0;
      boolean flag1 = false;
      if (!this.field_145850_b.field_72995_K) {
         if (this.infuserRunTime > 0) {
            --this.infuserRunTime;
            if (this.infuserRunTime == 0) {
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            }
         }

         if (this.infuserRunTime != 0 || !((ItemStack)this.infuserItemStacks.get(2)).func_190926_b() && !((ItemStack)this.infuserItemStacks.get(1)).func_190926_b() && !((ItemStack)this.infuserItemStacks.get(0)).func_190926_b()) {
            if (this.infuserRunTime == 0 && this.canInfuse()) {
               this.currentItemRunTime = this.infuserRunTime = TileEntityFurnace.func_145952_a((ItemStack)this.infuserItemStacks.get(0));
               ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
               if (this.infuserRunTime > 0) {
                  flag1 = true;
                  if (!((ItemStack)this.infuserItemStacks.get(0)).func_190926_b()) {
                     if (((ItemStack)this.infuserItemStacks.get(0)).func_190916_E() == 1) {
                        this.infuserItemStacks.set(0, ((ItemStack)this.infuserItemStacks.get(0)).func_77973_b().getContainerItem((ItemStack)this.infuserItemStacks.get(0)));
                     } else {
                        ((ItemStack)this.infuserItemStacks.get(0)).func_190918_g(1);
                     }
                  }
               }
            }

            if (this.isRunning() && this.canInfuse()) {
               ++this.infuserProgressTime;
               if (this.infuserProgressTime >= (Integer)((Tuple)InfuserRecipes.instance().getRecipe((ItemStack)this.infuserItemStacks.get(1), (ItemStack)this.infuserItemStacks.get(2)).getValue()).func_76340_b()) {
                  this.infuserProgressTime = 0;
                  this.infuseItem();
                  flag1 = true;
               }
            } else {
               this.infuserProgressTime = 0;
            }
         }

         if (flag != this.infuserRunTime > 0) {
            flag1 = true;
         }
      }

      if (flag1) {
         this.func_70296_d();
      }

      if (this.isRunning) {
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

   }

   private boolean canInfuse() {
      if (!((ItemStack)this.infuserItemStacks.get(1)).func_190926_b() && !((ItemStack)this.infuserItemStacks.get(2)).func_190926_b()) {
         Map.Entry s = InfuserRecipes.instance().getRecipe((ItemStack)this.infuserItemStacks.get(1), (ItemStack)this.infuserItemStacks.get(2));
         if (s != null && ((Tuple)s.getValue()).func_76341_a() != ItemStack.field_190927_a) {
            if (((ItemStack)this.infuserItemStacks.get(3)).func_190926_b()) {
               return true;
            } else if (!((ItemStack)this.infuserItemStacks.get(3)).func_77969_a((ItemStack)((Tuple)s.getValue()).func_76341_a())) {
               return false;
            } else {
               int result = ((ItemStack)this.infuserItemStacks.get(3)).func_190916_E() + ((ItemStack)((Tuple)s.getValue()).func_76341_a()).func_190916_E();
               return result <= this.func_70297_j_() && result <= ((ItemStack)this.infuserItemStacks.get(3)).func_77976_d();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void infuseItem() {
      if (this.canInfuse()) {
         Map.Entry s = InfuserRecipes.instance().getRecipe((ItemStack)this.infuserItemStacks.get(1), (ItemStack)this.infuserItemStacks.get(2));
         if (((ItemStack)this.infuserItemStacks.get(3)).func_190926_b()) {
            this.infuserItemStacks.set(3, ((ItemStack)((Tuple)s.getValue()).func_76341_a()).func_77946_l());
         } else if (((ItemStack)this.infuserItemStacks.get(3)).func_77973_b() == ((ItemStack)((Tuple)s.getValue()).func_76341_a()).func_77973_b()) {
            ((ItemStack)this.infuserItemStacks.get(3)).func_190920_e(((ItemStack)this.infuserItemStacks.get(3)).func_190916_E() + ((ItemStack)((Tuple)s.getValue()).func_76341_a()).func_190916_E());
         }

         ((ItemStack)this.infuserItemStacks.get(1)).func_190918_g(((ItemStack)((Tuple)s.getKey()).func_76341_a()).func_190916_E());
         if (((ItemStack)this.infuserItemStacks.get(1)).func_190916_E() <= 0) {
            this.infuserItemStacks.set(1, ItemStack.field_190927_a);
         }

         ((ItemStack)this.infuserItemStacks.get(2)).func_190918_g(((ItemStack)((Tuple)s.getKey()).func_76340_b()).func_190916_E());
         if (((ItemStack)this.infuserItemStacks.get(2)).func_190916_E() <= 0) {
            this.infuserItemStacks.set(2, ItemStack.field_190927_a);
         }
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
}
