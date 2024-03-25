package com.pixelmonmod.pixelmon.util;

import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EncounterData {
   protected final EnumEncounterMode defaultMode;
   protected EnumEncounterMode mode;
   protected long lastEncounter;
   protected final Map playerEncounters = new HashMap();

   public EncounterData(EnumEncounterMode defaultMode) {
      this.defaultMode = defaultMode;
   }

   public boolean canEncounter(EntityPlayer player) {
      if (this.getMode() == EnumEncounterMode.Once) {
         return this.playerEncounters.isEmpty();
      } else if (this.getMode() == EnumEncounterMode.OncePerPlayer) {
         return !this.playerEncounters.containsKey(player.func_110124_au());
      } else if (this.getMode() == EnumEncounterMode.OncePerDay) {
         return Instant.now().getEpochSecond() - this.lastEncounter > 86400L;
      } else if (this.getMode() == EnumEncounterMode.OncePerMCDay) {
         return player.field_70170_p.func_82737_E() - this.lastEncounter > 24000L;
      } else {
         return true;
      }
   }

   public void registerEncounter(EntityPlayer player) {
      long now = Instant.now().getEpochSecond();
      this.playerEncounters.put(player.func_110124_au(), now);
      if (this.getMode() == EnumEncounterMode.OncePerMCDay) {
         this.lastEncounter = player.field_70170_p.func_82737_E();
      } else {
         this.lastEncounter = now;
      }

   }

   public void addEncounter(UUID player, long time) {
      this.playerEncounters.put(player, time);
   }

   public EnumEncounterMode getMode() {
      return this.mode != null ? this.mode : this.defaultMode;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.func_74772_a("lastEnc", this.lastEncounter);
      if (this.mode != null) {
         compound.func_74774_a("mode", (byte)this.mode.ordinal());
      }

      NBTTagList list = new NBTTagList();
      Iterator var3 = this.playerEncounters.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry entry = (Map.Entry)var3.next();
         NBTTagCompound encounter = new NBTTagCompound();
         encounter.func_186854_a("UUID", (UUID)entry.getKey());
         encounter.func_74772_a("time", (Long)entry.getValue());
         list.func_74742_a(encounter);
      }

      compound.func_74782_a("encounters", list);
      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      if (compound.func_74764_b("lastEnc")) {
         this.lastEncounter = compound.func_74763_f("lastEnc");
      } else {
         this.lastEncounter = 0L;
      }

      if (compound.func_74764_b("mode")) {
         this.mode = EnumEncounterMode.getFromIndex(compound.func_74771_c("mode"));
      } else {
         this.mode = null;
      }

      this.playerEncounters.clear();
      if (compound.func_74764_b("encounters")) {
         NBTTagList list = compound.func_150295_c("encounters", 10);
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            NBTBase base = (NBTBase)var3.next();
            NBTTagCompound encounter = (NBTTagCompound)base;
            UUID uuid = encounter.func_186857_a("UUID");
            long time = encounter.func_74763_f("time");
            this.playerEncounters.put(uuid, time);
         }
      }

   }
}
