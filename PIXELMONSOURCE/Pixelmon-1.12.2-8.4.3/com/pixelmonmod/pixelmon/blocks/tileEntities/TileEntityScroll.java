package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import com.pixelmonmod.pixelmon.items.ItemScroll;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityScroll extends TileEntity implements IWorldNameable {
   private String name;
   private BlockScroll.Type type;
   private boolean displayOnly;
   private String imageResourceLocation;

   public TileEntityScroll() {
      this.type = BlockScroll.Type.Waters;
   }

   public TileEntityScroll(BlockScroll.Type type) {
      this.name = "";
      this.type = type;
      this.imageResourceLocation = "";
      this.displayOnly = false;
   }

   public boolean isDisplayOnly() {
      return this.displayOnly;
   }

   public BlockScroll.Type getType() {
      return this.type;
   }

   public void setItemValues(ItemStack stack) {
      NBTTagCompound tag = stack.func_190925_c("BlockEntityTag");
      this.displayOnly = tag.func_74767_n("DisplayOnly");
      this.imageResourceLocation = tag.func_74779_i("Resource");
      this.type = ((ItemScroll)stack.func_77973_b()).type;
      if (!this.displayOnly) {
         this.imageResourceLocation = this.type.getDefaultResource();
      }

      this.name = stack.func_82837_s() ? stack.func_82833_r() : null;
   }

   public String func_70005_c_() {
      return this.func_145818_k_() ? this.name : this.type.getName();
   }

   public boolean func_145818_k_() {
      return this.name != null && !this.name.isEmpty();
   }

   public ITextComponent func_145748_c_() {
      return (ITextComponent)(this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      if (this.func_145818_k_()) {
         compound.func_74778_a("CustomName", this.name);
      }

      compound.func_74757_a("DisplayOnly", this.displayOnly);
      compound.func_74778_a("Resource", this.imageResourceLocation == null ? "" : this.imageResourceLocation);
      compound.func_74774_a("Type", (byte)this.type.ordinal());
      return compound;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_150297_b("CustomName", 8)) {
         this.name = compound.func_74779_i("CustomName");
      }

      this.displayOnly = compound.func_74767_n("DisplayOnly");
      this.imageResourceLocation = compound.func_74779_i("Resource");
      this.type = BlockScroll.Type.values()[compound.func_74771_c("Type")];
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public NBTTagCompound func_189517_E_() {
      return this.func_189515_b(new NBTTagCompound());
   }

   @SideOnly(Side.CLIENT)
   public ResourceLocation getResourceLocation() {
      return new ResourceLocation(this.imageResourceLocation != null && !this.imageResourceLocation.isEmpty() ? this.imageResourceLocation : this.type.getDefaultResource());
   }

   public ItemStack getItem() {
      ItemStack itemstack = ItemScroll.makeScroll(this.type, this.displayOnly, this.imageResourceLocation);
      itemstack.func_77982_d(new NBTTagCompound());
      if (this.func_145818_k_()) {
         itemstack.func_151001_c(this.func_70005_c_());
      }

      itemstack.func_77978_p().func_74782_a("BlockEntityTag", this.func_189515_b(new NBTTagCompound()));
      return itemstack;
   }
}
