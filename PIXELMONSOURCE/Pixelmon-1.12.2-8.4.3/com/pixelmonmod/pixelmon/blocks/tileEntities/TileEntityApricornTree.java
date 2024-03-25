package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.enums.EnumApricornTrees;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.world.WorldServer;

public class TileEntityApricornTree extends TileEntityDecorativeBase {
   public static final short numStages = 6;
   private short stage = 0;
   public EnumApricornTrees tree;
   public long timeLastWatered;

   public TileEntityApricornTree() {
   }

   public TileEntityApricornTree(EnumApricornTrees tree) {
      this.tree = tree;
      this.timeLastWatered = 0L;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74777_a("stage", this.stage);
      nbt.func_74772_a("TimeLastWatered", this.timeLastWatered);
      nbt.func_74777_a("ApricornTreeID", (short)this.tree.id);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.stage = nbt.func_74765_d("stage");
      this.tree = EnumApricornTrees.getFromID(nbt.func_74765_d("ApricornTreeID"));
      this.timeLastWatered = nbt.func_74763_f("TimeLastWatered");
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
      this.func_145839_a(pkt.func_148857_g());
   }

   public boolean wasWateredToday() {
      if (this.timeLastWatered == 0L) {
         return false;
      } else {
         long tickTimeOfDay = this.field_145850_b.func_72820_D() % 24000L;
         long tickTimeSinceLastWatering = this.field_145850_b.func_82737_E() - this.timeLastWatered;
         return tickTimeSinceLastWatering <= tickTimeOfDay;
      }
   }

   public void updateWatering() {
      this.timeLastWatered = this.field_145850_b.func_82737_E();
   }

   public void setStage(int s) {
      if (this.stage != s && s < 6) {
         this.stage = (short)s;
         if (this.func_145830_o() && !this.field_145850_b.field_72995_K) {
            ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
            this.func_70296_d();
         }
      }

   }

   public void setGenerated() {
      this.stage = 5;
   }

   public int getStage() {
      return this.stage;
   }
}
