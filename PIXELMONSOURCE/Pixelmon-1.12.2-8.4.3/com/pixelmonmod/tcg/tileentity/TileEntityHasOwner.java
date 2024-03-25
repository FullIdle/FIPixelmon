package com.pixelmonmod.tcg.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHasOwner extends TileEntity {
   private static final String OWNER_KEY = "Owner";
   private String ownerId = null;

   public SPacketUpdateTileEntity func_189518_D_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return new SPacketUpdateTileEntity(this.field_174879_c, 1, nbt);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.func_145839_a(nbt);
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("Owner")) {
         this.ownerId = nbt.func_74779_i("Owner");
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.ownerId != null) {
         nbt.func_74778_a("Owner", this.ownerId);
      }

      return nbt;
   }

   public String getOwnerId() {
      return this.ownerId;
   }

   public void setOwnerId(String ownerId) {
      this.ownerId = ownerId;
   }
}
