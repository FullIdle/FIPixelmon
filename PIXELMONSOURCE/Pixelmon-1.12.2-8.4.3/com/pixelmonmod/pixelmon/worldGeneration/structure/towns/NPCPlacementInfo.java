package com.pixelmonmod.pixelmon.worldGeneration.structure.towns;

import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class NPCPlacementInfo {
   public int x;
   public int y;
   public int z;
   public EnumNPCType npcType;
   public String extraData;
   public String npcName;
   public int rotation;
   public boolean hasRotation;
   public int tier;
   public ArrayList drops;

   public NPCPlacementInfo(EnumNPCType type, int x, int y, int z) {
      this.hasRotation = false;
      this.npcType = type;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public NPCPlacementInfo(String npcname, EnumNPCType type, int x, int y, int z) {
      this(type, x, y, z);
      this.npcName = npcname;
   }

   public NPCPlacementInfo(String npcname, EnumNPCType type, int x, int y, int z, int rotation, int tier) {
      this(npcname, type, x, y, z);
      this.hasRotation = true;
      this.rotation = rotation;
      this.tier = tier;
   }

   public void setData(String data) {
      this.extraData = data;
   }

   public void addDrop(ItemStack item) {
      if (this.drops == null) {
         this.drops = new ArrayList();
      }

      this.drops.add(item);
   }
}
