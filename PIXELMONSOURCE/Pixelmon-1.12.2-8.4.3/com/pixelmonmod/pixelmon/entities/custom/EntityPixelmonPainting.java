package com.pixelmonmod.pixelmon.entities.custom;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPixelmonPainting extends EntityHanging {
   public static final DataParameter ITEMSTACK;

   public EntityPixelmonPainting(World worldIn) {
      super(worldIn);
   }

   public EntityPixelmonPainting(World worldIn, BlockPos pos, EnumFacing facing) {
      super(worldIn, pos);
      this.func_174859_a(facing);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(ITEMSTACK, ItemStack.field_190927_a);
   }

   public void func_70014_b(NBTTagCompound tagCompound) {
      super.func_70014_b(tagCompound);
      if (this.getDisplayedItem() != null) {
         tagCompound.func_74782_a("Item", this.getDisplayedItem().func_77955_b(new NBTTagCompound()));
      }

   }

   public void func_70037_a(NBTTagCompound tagCompund) {
      super.func_70037_a(tagCompund);
      NBTTagCompound nbttagcompound1 = tagCompund.func_74775_l("Item");
      if (!nbttagcompound1.func_82582_d()) {
         this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound1), false);
      }

   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (this.field_70170_p.field_72995_K) {
         return true;
      } else {
         ItemStack stack = player.func_184586_b(hand);
         if (this.getDisplayedItem() == null && !stack.func_190926_b() && stack.func_77973_b() == PixelmonItems.itemPixelmonSprite) {
            this.setDisplayedItem(stack);
            stack.func_190918_g(1);
            if (!player.field_71075_bZ.field_75098_d && stack.func_190916_E() <= 0) {
               player.func_184611_a(hand, ItemStack.field_190927_a);
            }
         }

         return true;
      }
   }

   public void func_110128_b(Entity brokenEntity) {
      this.dropItemOrSelf(brokenEntity, true);
   }

   public void dropItemOrSelf(Entity p_146065_1_, boolean p_146065_2_) {
      if (this.field_70170_p.func_82736_K().func_82766_b("doTileDrops")) {
         ItemStack itemstack = this.getDisplayedItem();
         if (p_146065_1_ instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)p_146065_1_;
            if (entityplayer.field_71075_bZ.field_75098_d) {
               return;
            }
         }

         if (p_146065_2_) {
            this.func_70099_a(new ItemStack(PixelmonItems.pixelmonPaintingItem), 0.0F);
         }

         if (itemstack != null) {
            itemstack = itemstack.func_77946_l();
            this.func_70099_a(itemstack, 0.0F);
         }
      }

   }

   public void func_174856_o() {
      if (this.field_70170_p.field_72995_K) {
         this.field_174860_b = EnumFacing.func_176733_a((double)this.field_70177_z);
      }

      if (this.field_174860_b != null) {
         double x = this.field_70165_t = (double)this.field_174861_a.func_177958_n() + 0.5;
         double y = this.field_70163_u = (double)this.field_174861_a.func_177956_o() + 0.5;
         double z = this.field_70161_v = (double)this.field_174861_a.func_177952_p() + 0.5;
         if (this.field_174860_b == EnumFacing.NORTH) {
            this.func_174826_a(new AxisAlignedBB(x - 1.5, y - 1.5, z + 0.3, x + 0.5, y + 0.5, z + 0.5));
         } else if (this.field_174860_b == EnumFacing.SOUTH) {
            this.func_174826_a(new AxisAlignedBB(x - 0.5, y - 1.5, z - 0.3, x + 1.5, y + 0.5, z - 0.5));
         } else if (this.field_174860_b == EnumFacing.EAST) {
            this.func_174826_a(new AxisAlignedBB(x - 0.3, y - 1.5, z - 1.5, x - 0.5, y + 0.5, z + 0.5));
         } else {
            this.func_174826_a(new AxisAlignedBB(x + 0.3, y - 1.5, z - 0.5, x + 0.5, y + 0.5, z + 1.5));
         }
      }

   }

   public int func_82329_d() {
      return 32;
   }

   public int func_82330_g() {
      return 32;
   }

   public boolean func_70518_d() {
      return true;
   }

   private void setDisplayedItemWithUpdate(ItemStack stack, boolean p_174864_2_) {
      if (stack != null) {
         stack = stack.func_77946_l();
         stack.func_190920_e(1);
      }

      this.func_184212_Q().func_187227_b(ITEMSTACK, stack);
      if (p_174864_2_ && this.field_174861_a != null) {
         this.field_70170_p.func_175666_e(this.field_174861_a, Blocks.field_150350_a);
      }

   }

   public void setDisplayedItem(ItemStack stack) {
      this.setDisplayedItemWithUpdate(stack, true);
   }

   public ItemStack getDisplayedItem() {
      return this.field_70180_af.func_187225_a(ITEMSTACK) != ItemStack.field_190927_a ? (ItemStack)this.func_184212_Q().func_187225_a(ITEMSTACK) : null;
   }

   public void func_184523_o() {
   }

   static {
      ITEMSTACK = EntityDataManager.func_187226_a(EntityPixelmonPainting.class, DataSerializers.field_187196_f);
   }
}
