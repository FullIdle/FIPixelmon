package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityZygardeCell extends TileEntity implements ITickable {
   private static final int MAX_SP_AGE = 12000;
   private static final int MAX_MP_AGE = 36000;
   private int age;
   private boolean isPermanent = false;
   private ItemZygardeCube.CoreType coreType;
   private final Map encounters;

   public TileEntityZygardeCell() {
      this.coreType = ItemZygardeCube.CoreType.RANDOM;
      this.encounters = Maps.newHashMap();
   }

   public boolean isPermanent() {
      return this.isPermanent;
   }

   public void setPermanent(boolean permanent) {
      this.isPermanent = permanent;
      if (!this.isPermanent) {
         this.encounters.clear();
      }

   }

   public ItemZygardeCube.CoreType getCoreType() {
      return this.coreType;
   }

   public void setCoreType(ItemZygardeCube.CoreType coreType) {
      this.coreType = coreType;
   }

   public boolean addEncounter(UUID uuid) {
      if (this.encounters.containsKey(uuid)) {
         return false;
      } else {
         this.encounters.put(uuid, this.field_145850_b.func_82737_E());
         return true;
      }
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         ++this.age;
         if (!this.isPermanent() && this.age > (FMLCommonHandler.instance().getMinecraftServerInstance().func_71264_H() ? 12000 : '負')) {
            this.field_145850_b.func_175698_g(this.field_174879_c);
         }

      }
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_74764_b("Age")) {
         this.age = compound.func_74762_e("Age");
      }

      if (compound.func_74764_b("isPermanent")) {
         this.isPermanent = compound.func_74767_n("isPermanent");
         this.encounters.clear();
         if (compound.func_74764_b("Encounters")) {
            NBTTagList list = compound.func_150295_c("Encounters", 10);
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               NBTBase base = (NBTBase)var3.next();
               NBTTagCompound tag = (NBTTagCompound)base;
               UUID uuid = tag.func_186857_a("UUID");
               long time = tag.func_74763_f("time");
               this.encounters.put(uuid, time);
            }
         }
      } else {
         this.isPermanent = false;
      }

      if (compound.func_74764_b("CoreType")) {
         this.coreType = ItemZygardeCube.CoreType.fromIndex(compound.func_74771_c("CoreType"));
      } else {
         this.coreType = ItemZygardeCube.CoreType.RANDOM;
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74768_a("Age", this.age);
      if (this.isPermanent) {
         compound.func_74757_a("isPermanent", this.isPermanent);
         if (!this.encounters.isEmpty()) {
            NBTTagList list = new NBTTagList();
            Iterator var3 = this.encounters.entrySet().iterator();

            while(var3.hasNext()) {
               Map.Entry entry = (Map.Entry)var3.next();
               NBTTagCompound tag = new NBTTagCompound();
               tag.func_186854_a("UUID", (UUID)entry.getKey());
               tag.func_74772_a("time", (Long)entry.getValue());
               list.func_74742_a(tag);
            }

            compound.func_74782_a("Encounters", list);
         }
      }

      if (this.coreType != ItemZygardeCube.CoreType.RANDOM) {
         compound.func_74774_a("CoreType", (byte)this.coreType.ordinal());
      }

      return compound;
   }

   public boolean shouldRenderInPass(int pass) {
      return pass == 1;
   }
}
