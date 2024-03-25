package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AIHarvestFarmLand extends EntityAIMoveToBlock {
   private final EntityNPC npc;
   private boolean hasFarmItem;
   private boolean wantsToReapStuff;
   private int currentTask;

   public AIHarvestFarmLand(EntityNPC theVillagerIn, double speedIn) {
      super(theVillagerIn, speedIn, 16);
      this.npc = theVillagerIn;
   }

   public boolean func_75250_a() {
      if (this.field_179496_a <= 0) {
         if (!this.npc.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
            return false;
         }

         this.currentTask = -1;
         this.hasFarmItem = this.npc.isFarmItemInInventory();
         this.wantsToReapStuff = this.npc.hasItemToPlant();
      }

      return super.func_75250_a();
   }

   public boolean func_75253_b() {
      return this.currentTask >= 0 && super.func_75253_b();
   }

   public void func_75249_e() {
      super.func_75249_e();
   }

   public void func_75251_c() {
      super.func_75251_c();
   }

   public void func_75246_d() {
      super.func_75246_d();
      this.npc.func_70671_ap().func_75650_a((double)this.field_179494_b.func_177958_n() + 0.5, (double)(this.field_179494_b.func_177956_o() + 1), (double)this.field_179494_b.func_177952_p() + 0.5, 10.0F, (float)this.npc.func_70646_bf());
      if (this.func_179487_f()) {
         World world = this.npc.field_70170_p;
         BlockPos blockpos = this.field_179494_b.func_177984_a();
         IBlockState iblockstate = world.func_180495_p(blockpos);
         Block block = iblockstate.func_177230_c();
         if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).func_185525_y(iblockstate)) {
            world.func_175655_b(blockpos, true);
         } else if (this.currentTask == 1 && iblockstate.func_185904_a() == Material.field_151579_a) {
            InventoryBasic inventorybasic = this.npc.getNPCInventory();

            for(int i = 0; i < inventorybasic.func_70302_i_(); ++i) {
               ItemStack itemstack = inventorybasic.func_70301_a(i);
               boolean flag = false;
               if (!itemstack.func_190926_b()) {
                  if (itemstack.func_77973_b() == Items.field_151014_N) {
                     world.func_180501_a(blockpos, Blocks.field_150464_aj.func_176223_P(), 3);
                     flag = true;
                  } else if (itemstack.func_77973_b() == Items.field_151174_bG) {
                     world.func_180501_a(blockpos, Blocks.field_150469_bN.func_176223_P(), 3);
                     flag = true;
                  } else if (itemstack.func_77973_b() == Items.field_151172_bF) {
                     world.func_180501_a(blockpos, Blocks.field_150459_bM.func_176223_P(), 3);
                     flag = true;
                  }
               }

               if (flag) {
                  itemstack.func_190920_e(itemstack.func_190916_E() - 1);
                  if (itemstack.func_190916_E() <= 0) {
                     inventorybasic.func_70299_a(i, ItemStack.field_190927_a);
                  }
                  break;
               }
            }
         }

         this.currentTask = -1;
         this.field_179496_a = 10;
      }

   }

   protected boolean func_179488_a(World worldIn, BlockPos pos) {
      Block block = worldIn.func_180495_p(pos).func_177230_c();
      if (block == Blocks.field_150458_ak) {
         pos = pos.func_177984_a();
         IBlockState iblockstate = worldIn.func_180495_p(pos);
         block = iblockstate.func_177230_c();
         if (block instanceof BlockCrops && ((BlockCrops)block).func_185525_y(iblockstate) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0)) {
            this.currentTask = 0;
            return true;
         }

         if (iblockstate.func_185904_a() == Material.field_151579_a && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0)) {
            this.currentTask = 1;
            return true;
         }
      }

      return false;
   }
}
