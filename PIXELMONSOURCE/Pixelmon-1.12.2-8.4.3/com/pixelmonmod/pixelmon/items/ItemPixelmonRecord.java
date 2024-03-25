package com.pixelmonmod.pixelmon.items;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemPixelmonRecord extends ItemRecord {
   public String musicName;

   public ItemPixelmonRecord(String name, SoundEvent record) {
      super(name, record);
      this.musicName = name;
      this.func_77655_b("record");
      this.setRegistryName("pixelmon:record." + name);
   }
}
