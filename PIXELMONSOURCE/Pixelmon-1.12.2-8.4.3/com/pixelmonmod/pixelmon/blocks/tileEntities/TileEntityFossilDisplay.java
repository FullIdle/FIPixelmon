package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.client.models.animations.AnimateTask;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityFossilDisplay extends TileEntity implements IFrameCounter {
   private ItemFossil itemInDisplay = null;
   private UUID owner = null;
   public int renderPass = 0;
   public int frame = 0;
   AnimateTask animateTask;

   public void openGlass() {
      if (this.getLastFrame() == 30) {
         if (this.animateTask != null) {
            this.animateTask.cancel();
         }

         this.animateTask = new AnimateTask(this, 30, 60);
         AnimateTask.timer.scheduleAtFixedRate(this.animateTask, 100L, 20L);
         if (!this.field_145850_b.field_72995_K) {
            this.sendAnimation("open");
            this.func_70296_d();
         }

      }
   }

   public void closeGlass() {
      if (this.getLastFrame() == 0 || this.getLastFrame() == 60) {
         if (this.animateTask != null) {
            this.animateTask.cancel();
         }

         this.animateTask = new AnimateTask(this, 0, 30);
         AnimateTask.timer.scheduleAtFixedRate(this.animateTask, 100L, 20L);
         if (!this.field_145850_b.field_72995_K) {
            this.sendAnimation("close");
            this.func_70296_d();
         }

      }
   }

   public boolean isOpen() {
      return this.getLastFrame() == 0 || this.getLastFrame() == 60;
   }

   public int getFrame() {
      return this.frame;
   }

   public void setFrame(int frame) {
      this.frame = frame;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      Item item = Item.func_150899_d(nbt.func_74762_e("ItemIn"));
      this.setItemInDisplay(item instanceof ItemFossil ? item : null);
      this.setLastFrame(nbt.func_74762_e("frame"));
      if (nbt.func_74764_b("owner")) {
         this.owner = UUID.fromString(nbt.func_74779_i("owner"));
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("ItemIn", Item.func_150891_b(this.itemInDisplay));
      if (this.frame <= 30 && this.frame >= 1) {
         nbt.func_74768_a("frame", 30);
      } else {
         nbt.func_74768_a("frame", 0);
      }

      if (this.owner != null) {
         nbt.func_74778_a("owner", this.owner.toString());
      }

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

   public void onLoad() {
      if (this.func_145830_o() && this.func_145831_w() instanceof WorldServer) {
         ((WorldServer)this.func_145831_w()).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   public void sendAnimation(String str) {
      NBTTagCompound nbt = new NBTTagCompound();
      if (this.frame <= 30 && this.frame >= 1) {
         nbt.func_74768_a("frame", 30);
      } else {
         nbt.func_74768_a("frame", 0);
      }

      nbt.func_74778_a("Animation", str);
      SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(this.field_174879_c, 0, nbt);
      this.field_145850_b.func_73046_m().func_184103_al().func_148543_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), 20.0, this.field_145850_b.field_73011_w.getDimension(), packet);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      if (nbt.func_150297_b("ItemIn", 3)) {
         Item item = Item.func_150899_d(nbt.func_74762_e("ItemIn"));
         this.setItemInDisplay(item instanceof ItemFossil ? item : null);
      }

      this.setFrame(nbt.func_74762_e("frame"));
      if (nbt.func_150297_b("Animation", 8)) {
         if (nbt.func_74779_i("Animation").equals("open")) {
            this.openGlass();
         } else {
            this.closeGlass();
         }
      }

   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public ItemFossil getItemInDisplay() {
      return this.itemInDisplay;
   }

   public void setItemInDisplay(Item item) {
      if (item == null || item instanceof ItemFossil) {
         this.itemInDisplay = (ItemFossil)item;
         this.func_70296_d();
      }

   }

   public int getLastFrame() {
      return this.frame;
   }

   public void setLastFrame(int frame) {
      this.frame = frame;
   }

   public boolean shouldRenderInPass(int pass) {
      this.renderPass = pass;
      return true;
   }
}
