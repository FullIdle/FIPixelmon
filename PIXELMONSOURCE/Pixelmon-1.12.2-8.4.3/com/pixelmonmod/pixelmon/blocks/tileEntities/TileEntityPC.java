package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.UUID;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class TileEntityPC extends TileEntity implements ISpecialTexture {
   private ResourceLocation texture = new ResourceLocation("pixelmon:textures/blocks/computer/Texture_red.png");
   private String colour = "red";
   private UUID owner = null;
   private boolean rave = false;
   private long lastRaveChange = 0L;
   private String oldColour = "";

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74778_a("colour", this.colour);
      compound.func_74757_a("rave", this.rave);
      if (this.owner != null) {
         compound.func_74778_a("owner", this.owner.toString());
      }

      return compound;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_74764_b("colour")) {
         this.colour = compound.func_74779_i("colour");
         this.rave = compound.func_74767_n("rave");
      }

      if (compound.func_74764_b("owner")) {
         this.owner = UUID.fromString(compound.func_74779_i("owner"));
      }

   }

   public boolean getRave() {
      return this.rave;
   }

   public void setRave(boolean rave) {
      this.rave = rave;
      this.refreshTexture();
   }

   public String getColour() {
      return this.colour;
   }

   public void setColour(String colour) {
      this.colour = colour;
      this.refreshTexture();
   }

   public ResourceLocation getTexture() {
      if (this.rave && (this.lastRaveChange == 0L || this.func_145831_w().func_82737_E() - this.lastRaveChange > 5L)) {
         this.colour = EnumDyeColor.values()[RandomHelper.rand.nextInt(EnumDyeColor.values().length)].func_176610_l();
         this.lastRaveChange = this.func_145831_w().func_82737_E();
         this.refreshTexture();
      }

      if (!this.colour.equals(this.oldColour)) {
         this.texture = new ResourceLocation("pixelmon:textures/blocks/computer/Texture_" + this.colour + ".png");
         this.colour = this.oldColour;
      }

      return this.texture;
   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
      this.colour = packet.func_148857_g().func_74779_i("colour");
      this.rave = packet.func_148857_g().func_74767_n("rave");
      this.refreshTexture();
   }

   private void refreshTexture() {
      if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
         this.func_70296_d();
      }

   }
}
