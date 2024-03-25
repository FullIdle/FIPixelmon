package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.legendary.ArceusEvent;
import com.pixelmonmod.pixelmon.blocks.BlockPlateHolder;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

public class TileEntityPlateHolder extends TileEntity implements ITickable {
   public ArrayList platesIn = new ArrayList();
   public short animationTimer = 0;
   public boolean animating = false;
   public byte plateAdded = -1;
   public boolean itemThere = false;
   private EntityItem spawnedItem = null;
   private transient UUID lastUsedPlayer = null;

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = super.func_189517_E_();
      nbt.func_74783_a("Plates", ArrayUtils.toPrimitive((Integer[])this.platesIn.toArray(new Integer[0])));
      nbt.func_74777_a("AnimationTimer", this.animationTimer);
      nbt.func_74757_a("Animating", this.animating);
      nbt.func_74774_a("CurrentPlate", this.plateAdded);
      nbt.func_74757_a("ItemThere", this.itemThere);
      return nbt;
   }

   public void handleUpdateTag(NBTTagCompound tag) {
      this.func_145839_a(tag);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74783_a("Plates", ArrayUtils.toPrimitive((Integer[])this.platesIn.toArray(new Integer[0])));
      nbt.func_74777_a("AnimationTimer", this.animationTimer);
      nbt.func_74757_a("Animating", this.animating);
      nbt.func_74774_a("CurrentPlate", this.plateAdded);
      nbt.func_74757_a("ItemThere", this.itemThere);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      ArrayList platesRead = new ArrayList();
      int[] var3 = nbt.func_74759_k("Plates");
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int i = var3[var5];
         platesRead.add(i);
      }

      this.platesIn = platesRead;
      this.animationTimer = nbt.func_74765_d("AnimationTimer");
      this.animating = nbt.func_74767_n("Animating");
      this.plateAdded = nbt.func_74771_c("CurrentPlate");
      this.itemThere = nbt.func_74767_n("ItemThere");
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      NBTTagCompound tagCompound = new NBTTagCompound();
      this.func_189515_b(tagCompound);
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, tagCompound);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
      this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
   }

   public void activate(EntityPlayer player, BlockPlateHolder block, IBlockState state, ItemStack item) {
      if (!this.field_145850_b.field_72995_K) {
         EnumPlate plate = EnumPlate.getPlateForItem(item.func_77973_b());
         if (plate != null) {
            if (!this.platesIn.contains(plate.ordinal()) && this.plateAdded != plate.ordinal()) {
               if (Pixelmon.EVENT_BUS.post(new ArceusEvent.AddPlate((EntityPlayerMP)player, this, item))) {
                  return;
               }

               item.func_190918_g(1);
               if (this.plateAdded != -1) {
                  this.platesIn.add(Integer.valueOf(this.plateAdded));
               }

               this.plateAdded = (byte)plate.ordinal();
               this.animationTimer = 0;
               this.animating = true;
               this.lastUsedPlayer = player.func_110124_au();
               this.field_145850_b.func_184148_a((EntityPlayer)null, (double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), SoundEvents.field_187618_I, SoundCategory.BLOCKS, 0.6F, 0.75F);
               ChatHandler.sendChat(player, "pixelmon.blocks.plate.add", EnumPlate.values().length - this.platesIn.size() - 1);
               this.func_70296_d();
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.plate.fail");
            }
         }
      }

   }

   public void func_70296_d() {
      this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
      super.func_70296_d();
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.spawnedItem != null && this.spawnedItem.field_70128_L && this.itemThere || this.spawnedItem == null && this.itemThere) {
            this.spawnedItem = null;
            this.itemThere = false;
            this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
            this.func_70296_d();
         }

         if (this.animating) {
            ++this.animationTimer;
            if (this.platesIn.size() >= 16) {
               if (this.animationTimer > 500) {
                  this.platesIn.clear();
                  this.plateAdded = -1;
                  this.animating = false;
                  this.animationTimer = 0;
                  ItemStack azureFlute = new ItemStack(PixelmonItems.azureFlute, 1);
                  EntityItem item = new EntityItem(this.func_145831_w(), (double)this.field_174879_c.func_177958_n() + 0.5, (double)(this.field_174879_c.func_177956_o() + 1), (double)this.field_174879_c.func_177952_p() + 0.5, azureFlute);
                  item.func_189654_d(true);
                  item.field_70181_x = item.field_70159_w = item.field_70179_y = 0.0;
                  Pixelmon.EVENT_BUS.post(new ArceusEvent.CreateFlute(this.lastUsedPlayer, this, item));
                  this.field_145850_b.func_184148_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 1.25, (double)this.field_174879_c.func_177952_p() + 0.5, SoundEvents.field_187539_bB, SoundCategory.AMBIENT, 0.5F, 1.0E-4F);
                  this.field_145850_b.func_72838_d(item);
                  this.spawnedItem = item;
                  this.itemThere = true;
               }
            } else if (this.animationTimer > 100) {
               this.platesIn.add(Integer.valueOf(this.plateAdded));
               this.plateAdded = -1;
               this.animating = false;
               this.animationTimer = 0;
            }

            this.func_70296_d();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }
}
