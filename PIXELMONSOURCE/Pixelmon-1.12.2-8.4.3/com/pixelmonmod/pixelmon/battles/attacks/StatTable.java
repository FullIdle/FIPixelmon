package com.pixelmonmod.pixelmon.battles.attacks;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.util.Tuple;

public class StatTable {
   int atk = 0;
   int def = 0;
   int spa = 0;
   int spd = 0;
   int spe = 0;

   public Tuple getStatChanges() {
      int index = 0;
      int[] changes = new int[5];
      StatsType[] types = new StatsType[5];
      if (this.atk != 0) {
         changes[index] = this.atk;
         types[index++] = StatsType.Attack;
      }

      if (this.def != 0) {
         changes[index] = this.def;
         types[index++] = StatsType.Defence;
      }

      if (this.spa != 0) {
         changes[index] = this.spa;
         types[index++] = StatsType.SpecialAttack;
      }

      if (this.spd != 0) {
         changes[index] = this.spd;
         types[index++] = StatsType.SpecialDefence;
      }

      if (this.spe != 0) {
         changes[index] = this.spe;
         types[index++] = StatsType.Speed;
      }

      if (index == 5) {
         return new Tuple(changes, types);
      } else {
         int[] c = new int[index];
         StatsType[] t = new StatsType[index];
         System.arraycopy(changes, 0, c, 0, index);
         System.arraycopy(types, 0, t, 0, index);
         return new Tuple(c, t);
      }
   }
}
