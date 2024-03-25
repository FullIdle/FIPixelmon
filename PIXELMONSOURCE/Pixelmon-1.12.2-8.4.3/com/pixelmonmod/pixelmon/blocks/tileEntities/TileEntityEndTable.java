package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.client.models.animations.AnimateTask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityEndTable extends TileEntityInventory implements IFrameCounter {
   public int frame = 0;
   AnimateTask animateTask;

   public void closeDrawer() {
      if (this.frame == 15) {
         if (this.animateTask != null) {
            this.animateTask.cancel();
         }

         this.animateTask = new AnimateTask(this, 15, 30);
         AnimateTask.timer.scheduleAtFixedRate(this.animateTask, 100L, 30L);
         if (!this.field_145850_b.field_72995_K) {
            this.sendAnimation("close");
            this.func_70296_d();
         }

      }
   }

   public void openDrawer() {
      if (this.frame == 0 || this.frame == 30) {
         if (this.animateTask != null) {
            this.animateTask.cancel();
         }

         this.animateTask = new AnimateTask(this, 0, 15);
         AnimateTask.timer.scheduleAtFixedRate(this.animateTask, 100L, 30L);
         if (!this.field_145850_b.field_72995_K) {
            this.sendAnimation("open");
            this.func_70296_d();
         }

      }
   }

   public boolean isOpen() {
      return this.frame == 15;
   }

   public int getFrame() {
      return this.frame;
   }

   public void setFrame(int frame) {
      this.frame = frame;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.setFrame(nbt.func_74762_e("frame"));
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.frame <= 29 && this.frame >= 1) {
         nbt.func_74768_a("frame", 15);
      } else {
         nbt.func_74768_a("frame", 0);
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

   public void sendAnimation(String str) {
      NBTTagCompound nbt = new NBTTagCompound();
      if (this.frame <= 29 && this.frame >= 1) {
         nbt.func_74768_a("frame", 15);
      } else {
         nbt.func_74768_a("frame", 0);
      }

      nbt.func_74778_a("Animation", str);
      SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(this.field_174879_c, 0, nbt);
      this.field_145850_b.func_73046_m().func_184103_al().func_148543_a((EntityPlayer)null, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), 20.0, this.field_145850_b.field_73011_w.getDimension(), packet);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.setFrame(nbt.func_74762_e("frame"));
      if (nbt.func_150297_b("Animation", 8)) {
         if (nbt.func_74779_i("Animation").equals("open")) {
            this.openDrawer();
         } else {
            this.closeDrawer();
         }
      }

   }

   public int func_70302_i_() {
      return 18;
   }

   String getInventoryName() {
      return "EndTable.name";
   }
}
