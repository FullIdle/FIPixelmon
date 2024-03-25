package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;

public class IVEVSpec extends SpecValue implements ISpecType {
   public StatsType stat;
   public String nbtTag;
   public List keys;
   public boolean isIVs = true;
   public Operation operation;

   public IVEVSpec(List keys, String nbtTag, StatsType stat, boolean isIVs, int value, Operation operation) {
      super((String)keys.get(0), value);
      this.operation = IVEVSpec.Operation.SET;
      this.keys = keys;
      this.stat = stat;
      this.nbtTag = nbtTag;
      this.isIVs = isIVs;
      if (operation != null) {
         this.operation = operation;
      }

   }

   public List getKeys() {
      return this.keys;
   }

   public IVEVSpec parse(String arg) {
      if (arg == null) {
         return null;
      } else {
         Operation operation = IVEVSpec.Operation.SET;
         if (arg.contains("+")) {
            operation = IVEVSpec.Operation.ADD;
         } else if (arg.contains("-")) {
            operation = IVEVSpec.Operation.SUBTRACT;
         }

         arg = arg.replace("+", "");
         arg = arg.replace("-", "");
         int amount = -1;

         try {
            amount = Integer.parseInt(arg);
         } catch (NumberFormatException var5) {
         }

         return amount >= 0 && amount <= (this.isIVs ? 31 : 252) ? new IVEVSpec(this.keys, this.nbtTag, this.stat, this.isIVs, amount, operation) : null;
      }
   }

   public IVEVSpec readFromNBT(NBTTagCompound nbt) {
      return this.parse(nbt.func_74779_i(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74778_a(value.key, this.operation.sign + value.value);
   }

   public Class getSpecClass() {
      return this.getClass();
   }

   public String toParameterForm(SpecValue value) {
      return value.key + ":" + ((IVEVSpec)value).operation.sign + value.value.toString();
   }

   public Class getValueClass() {
      return Integer.class;
   }

   public void apply(EntityPixelmon pixelmon) {
      if (this.operation == IVEVSpec.Operation.SET) {
         if (this.isIVs) {
            pixelmon.getPokemonData().getIVs().setStat(this.stat, (Integer)this.value);
         } else {
            pixelmon.getPokemonData().getEVs().setStat(this.stat, (Integer)this.value);
         }
      } else {
         int multiplier = this.operation == IVEVSpec.Operation.ADD ? 1 : -1;
         int change = multiplier * (Integer)this.value;
         if (this.isIVs) {
            pixelmon.getPokemonData().getIVs().addStat(this.stat, change);
         } else {
            pixelmon.getPokemonData().getEVs().addStat(this.stat, change);
         }
      }

   }

   public void apply(Pokemon pokemon) {
      if (this.operation == IVEVSpec.Operation.SET) {
         if (this.isIVs) {
            pokemon.getIVs().setStat(this.stat, (Integer)this.value);
         } else {
            pokemon.getEVs().setStat(this.stat, (Integer)this.value);
         }
      } else {
         int multiplier = this.operation == IVEVSpec.Operation.ADD ? 1 : -1;
         int change = multiplier * (Integer)this.value;
         if (this.isIVs) {
            pokemon.getIVs().addStat(this.stat, change);
         } else {
            pokemon.getEVs().addStat(this.stat, change);
         }
      }

   }

   public boolean matches(EntityPixelmon pixelmon) {
      if (this.operation == IVEVSpec.Operation.SET) {
         return this.isIVs ? pixelmon.getPokemonData().getIVs().getStat(this.stat) == (Integer)this.value : pixelmon.getPokemonData().getEVs().getStat(this.stat) == (Integer)this.value;
      } else if (this.operation == IVEVSpec.Operation.ADD) {
         return this.isIVs ? pixelmon.getPokemonData().getIVs().getStat(this.stat) >= (Integer)this.value : pixelmon.getPokemonData().getEVs().getStat(this.stat) >= (Integer)this.value;
      } else {
         return this.isIVs ? pixelmon.getPokemonData().getIVs().getStat(this.stat) <= (Integer)this.value : pixelmon.getPokemonData().getEVs().getStat(this.stat) <= (Integer)this.value;
      }
   }

   public boolean matches(Pokemon pokemon) {
      if (this.operation == IVEVSpec.Operation.SET) {
         return this.isIVs ? pokemon.getIVs().getStat(this.stat) == (Integer)this.value : pokemon.getEVs().getStat(this.stat) == (Integer)this.value;
      } else if (this.operation == IVEVSpec.Operation.ADD) {
         return this.isIVs ? pokemon.getIVs().getStat(this.stat) >= (Integer)this.value : pokemon.getEVs().getStat(this.stat) >= (Integer)this.value;
      } else {
         return this.isIVs ? pokemon.getIVs().getStat(this.stat) <= (Integer)this.value : pokemon.getEVs().getStat(this.stat) <= (Integer)this.value;
      }
   }

   public SpecValue clone() {
      return new IVEVSpec(this.keys, this.nbtTag, this.stat, this.isIVs, (Integer)this.value, this.operation);
   }

   public static enum Operation {
      ADD("+"),
      SUBTRACT("-"),
      SET("");

      public final String sign;

      private Operation(String sign) {
         this.sign = sign;
      }
   }
}
