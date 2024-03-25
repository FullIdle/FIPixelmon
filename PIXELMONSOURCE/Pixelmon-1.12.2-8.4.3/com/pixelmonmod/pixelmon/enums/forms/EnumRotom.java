package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.HashBiMap;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.init.Blocks;

public enum EnumRotom implements IEnumForm {
   NORMAL("Thunder Shock"),
   HEAT("Overheat"),
   WASH("Hydro Pump"),
   FROST("Blizzard"),
   FAN("Air Slash"),
   MOW("Leaf Storm");

   public String attack;
   private static HashBiMap blockMap = HashBiMap.create();

   private EnumRotom(String attackName) {
      this.attack = attackName;
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public static HashBiMap getBlockImpactMap() {
      if (blockMap.isEmpty()) {
         blockMap.put(Blocks.field_150460_al, HEAT);
         blockMap.put(PixelmonBlocks.fridgeBlock, FROST);
         blockMap.put(PixelmonBlocks.mower, MOW);
         blockMap.put(PixelmonBlocks.washingMachine, WASH);
         blockMap.put(PixelmonBlocks.fan, FAN);
      }

      return blockMap;
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.rotom.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}
