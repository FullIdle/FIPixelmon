package com.pixelmonmod.pixelmon.blocks.tileEntities;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityTree extends TileEntity {
   private int treeType = 1;
   private UUID owner = null;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.setTreeType(nbt.func_74762_e("treeType"));
      if (nbt.func_74764_b("owner")) {
         UUID uuid = UUID.fromString(nbt.func_74779_i("owner"));
         this.owner = uuid;
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("treeType", this.treeType);
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

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.setTreeType(nbt.func_74762_e("treeType"));
   }

   public void onLoad() {
      if (this.func_145830_o() && this.func_145831_w() instanceof WorldServer) {
         ((WorldServer)this.func_145831_w()).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public int getTreeType() {
      return this.treeType;
   }

   public void setTreeType(int type) {
      this.treeType = type;
      this.func_70296_d();
   }
}
