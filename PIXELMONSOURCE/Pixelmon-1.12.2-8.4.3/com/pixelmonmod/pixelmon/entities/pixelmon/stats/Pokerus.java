package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class Pokerus {
   public final EnumPokerusType type;
   public int secondsSinceInfection = 0;
   public boolean announced = false;
   public static final DataSerializer SERIALIZER = new DataSerializer() {
      public void write(PacketBuffer buf, Pokerus value) {
         buf.writeShort(value.type.ordinal());
         buf.writeInt(value.secondsSinceInfection);
         buf.writeBoolean(value.announced);
      }

      public Pokerus read(PacketBuffer buf) throws IOException {
         Pokerus p = new Pokerus(EnumPokerusType.values()[buf.readShort()]);
         p.secondsSinceInfection = buf.readInt();
         p.announced = buf.readBoolean();
         return p;
      }

      public DataParameter func_187161_a(int id) {
         return new DataParameter(id, this);
      }

      public Pokerus copyValue(Pokerus value) {
         return value;
      }
   };
   private static List pendingRequests = new ArrayList();

   public Pokerus(EnumPokerusType type) {
      this.type = type;
   }

   public boolean canInfect() {
      return this.secondsSinceInfection != -1 && this.secondsSinceInfection < this.type.duration;
   }

   public NBTTagCompound serializeToNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74777_a("Type", (short)this.type.ordinal());
      if (this.canInfect()) {
         nbt.func_74768_a("Spread", this.secondsSinceInfection);
      } else {
         nbt.func_82580_o("Spread");
      }

      nbt.func_74757_a("Announced", this.announced);
      return nbt;
   }

   public static Pokerus deserializeFromNBT(NBTTagCompound nbt) {
      if (nbt.func_82582_d()) {
         return null;
      } else {
         Pokerus p = new Pokerus(EnumPokerusType.values()[nbt.func_74765_d("Type")]);
         p.secondsSinceInfection = nbt.func_74764_b("Spread") ? nbt.func_74762_e("Spread") : -1;
         p.announced = nbt.func_74767_n("Announced");
         return p;
      }
   }

   public static void informPlayer(EntityPlayerMP player) {
      if (PixelmonConfig.pokerusInformPlayers) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         Pokemon[] var2 = party.getAll();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Pokemon pokemon = var2[var4];
            if (pokemon != null) {
               Pokerus pokerus = pokemon.getPokerus();
               if (pokerus != null && !pokerus.announced) {
                  if (!pendingRequests.contains(player.func_110124_au())) {
                     pendingRequests.add(player.func_110124_au());
                  }

                  pokerus.announced = true;
                  pokemon.markDirty(EnumUpdateType.Pokerus);
               }
            }
         }

      }
   }

   public static void onHealerClose(EntityPlayerMP player, String npc) {
      if (PixelmonConfig.pokerusInformPlayers && pendingRequests.contains(player.func_110124_au())) {
         pendingRequests.remove(player.func_110124_au());
         List dialogues = new ArrayList();
         dialogues.add(Dialogue.builder().setName(npc).setLocalizedText("pokerus.message.inform1").build());
         dialogues.add(Dialogue.builder().setName(npc).setLocalizedText("pokerus.message.inform2").build());
         dialogues.add(Dialogue.builder().setName(npc).setLocalizedText("pokerus.message.inform3").build());
         Dialogue.setPlayerDialogueData(player, dialogues, true);
      }

   }
}
