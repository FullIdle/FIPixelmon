package com.pixelmonmod.pixelmon.blocks.tileEntities;

import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class TileEntityGymSign extends TileEntity implements ISpecialTexture {
   private ItemStack itemInSign = null;
   private UUID owner = null;
   private String colour = "red";
   private ResourceLocation texture = new ResourceLocation("pixelmon:textures/blocks/gymSign/Texture_red.png");
   private boolean itemDrops = true;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("ItemIn")) {
         this.itemInSign = new ItemStack(nbt.func_74775_l("ItemIn"));
      } else {
         this.itemInSign = null;
      }

      if (nbt.func_74764_b("ItemDrops")) {
         this.itemDrops = nbt.func_74767_n("ItemDrops");
      }

      if (nbt.func_74764_b("owner")) {
         this.owner = UUID.fromString(nbt.func_74779_i("owner"));
         this.colour = nbt.func_74779_i("colour");
         this.refreshTexture();
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.itemInSign == null) {
         nbt.func_82580_o("ItemIn");
      } else {
         NBTTagCompound itemTag = new NBTTagCompound();
         this.itemInSign.func_77955_b(itemTag);
         nbt.func_74782_a("ItemIn", itemTag);
      }

      nbt.func_74757_a("ItemDrops", this.itemDrops);
      if (this.owner != null) {
         nbt.func_74778_a("colour", this.colour);
         nbt.func_74778_a("owner", this.owner.toString());
      }

      return nbt;
   }

   private void refreshTexture() {
      this.texture = new ResourceLocation("pixelmon:textures/blocks/gymSign/Texture_" + this.colour + ".png");
   }

   public String getColour() {
      return this.colour;
   }

   public void setColour(String colour) {
      this.colour = colour;
      this.sendChanges();
      this.func_70296_d();
   }

   public ResourceLocation getTexture() {
      return this.texture;
   }

   public void sendChanges() {
      if (this.func_145830_o() && this.func_145831_w() instanceof WorldServer) {
         ((WorldServer)this.func_145831_w()).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74778_a("colour", this.colour);
      this.func_189515_b(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
      NBTTagCompound data = pkt.func_148857_g();
      this.colour = data.func_74779_i("colour");
   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public ItemStack getItemInSign() {
      return this.itemInSign;
   }

   public void setItemInSign(ItemStack item) {
      this.itemInSign = item;
      this.func_70296_d();
   }

   public void setDroppable(boolean b) {
      this.itemDrops = false;
   }

   public boolean isDroppable() {
      return this.itemDrops;
   }
}
