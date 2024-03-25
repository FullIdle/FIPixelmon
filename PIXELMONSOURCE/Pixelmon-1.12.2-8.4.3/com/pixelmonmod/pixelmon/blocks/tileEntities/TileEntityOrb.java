package com.pixelmonmod.pixelmon.blocks.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityOrb extends TileEntity {
   private byte pieces;
   public int clientTicker;

   public TileEntityOrb() {
      this(1);
   }

   public TileEntityOrb(int pieces) {
      this.pieces = 0;
      this.clientTicker = 0;
      this.pieces = (byte)pieces;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74774_a("pieces", this.pieces);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.pieces = nbt.func_74771_c("pieces");
   }

   public NBTTagCompound func_189517_E_() {
      return this.func_189515_b(new NBTTagCompound());
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public void addPiece() {
      this.setPieces(this.pieces + 1);
   }

   public void setPieces(int pieces) {
      this.pieces = (byte)pieces;
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }

   public byte getPieces() {
      return this.pieces;
   }
}
