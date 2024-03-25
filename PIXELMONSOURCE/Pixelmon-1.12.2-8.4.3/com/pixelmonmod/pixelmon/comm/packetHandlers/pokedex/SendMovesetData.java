package com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumMovesetGroup;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen1TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen2TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen3TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen4TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen5TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen6TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen7TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;

public class SendMovesetData implements IMessage {
   private EnumSpecies species;
   private byte form;
   private String texture;
   private Map moveset;

   public SendMovesetData() {
   }

   public SendMovesetData(EnumSpecies species, byte form, String texture) {
      this.species = species;
      this.form = form;
      this.texture = texture;
      this.moveset = species.getMoveset(form, texture);
   }

   public void toBytes(ByteBuf byteBuf) {
      byteBuf.writeInt(this.species.getNationalPokedexInteger());
      byteBuf.writeByte(this.form);
      ByteBufUtils.writeUTF8String(byteBuf, this.texture);
      BitSet gen1 = new BitSet(Gen1TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Kanto)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen1.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen1.toByteArray());
      BitSet gen2 = new BitSet(Gen2TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Johto)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen2.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen2.toByteArray());
      BitSet gen3 = new BitSet(Gen3TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Hoenn)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen3.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen3.toByteArray());
      BitSet gen4 = new BitSet(Gen4TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Sinnoh)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen4.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen4.toByteArray());
      BitSet gen5 = new BitSet(Gen5TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Unova)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen5.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen5.toByteArray());
      BitSet gen6 = new BitSet(Gen6TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Kalos)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen6.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen6.toByteArray());
      BitSet gen7 = new BitSet(Gen7TechnicalMachines.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Alola)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            gen7.set(l.getId() - 1);
         }
      });
      this.writeByteArray(byteBuf, gen7.toByteArray());
      BitSet gen8 = new BitSet(Gen8TechnicalMachines.values().length + Gen8TechnicalRecords.values().length);
      ((List)this.moveset.get(EnumMovesetGroup.Galar)).forEach((o) -> {
         if (o != null) {
            ITechnicalMove l = (ITechnicalMove)o;
            if (l instanceof Gen8TechnicalMachines) {
               gen8.set(l.getId());
            } else {
               gen8.set(l.getId() + 100);
            }

         }
      });
      this.writeByteArray(byteBuf, gen8.toByteArray());
      byteBuf.writeShort(((List)this.moveset.get(EnumMovesetGroup.Egg)).size());

      int i;
      for(i = 0; i < ((List)this.moveset.get(EnumMovesetGroup.Egg)).size(); ++i) {
         byteBuf.writeShort(((Attack)((List)this.moveset.get(EnumMovesetGroup.Egg)).get(i)).getMove().getAttackId());
      }

      byteBuf.writeShort(((List)this.moveset.get(EnumMovesetGroup.LevelUp)).size());

      for(i = 0; i < ((List)this.moveset.get(EnumMovesetGroup.LevelUp)).size(); ++i) {
         Pair a = (Pair)((List)this.moveset.get(EnumMovesetGroup.LevelUp)).get(i);
         byteBuf.writeByte((Integer)a.getLeft());
         byteBuf.writeShort(((AttackBase)a.getRight()).getAttackId());
      }

   }

   private void writeByteArray(ByteBuf byteBuf, byte[] arr) {
      byteBuf.writeByte(arr.length);
      byteBuf.writeBytes(arr);
   }

   private byte[] readByteArray(ByteBuf byteBuf) {
      byte[] ret = new byte[byteBuf.readByte()];
      byteBuf.readBytes(ret);
      return ret;
   }

   public void fromBytes(ByteBuf byteBuf) {
      this.species = EnumSpecies.getFromDex(byteBuf.readInt());
      this.form = byteBuf.readByte();
      this.texture = ByteBufUtils.readUTF8String(byteBuf);
      this.moveset = new HashMap();
      EnumMovesetGroup[] var2 = EnumMovesetGroup.values();
      int i = var2.length;

      int i;
      for(i = 0; i < i; ++i) {
         EnumMovesetGroup group = var2[i];
         this.moveset.put(group, Lists.newArrayList());
      }

      BitSet gen1 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(i = 0; i < gen1.length(); ++i) {
         if (gen1.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Kanto)).add(Gen1TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen2 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(i = 0; i < gen2.length(); ++i) {
         if (gen2.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Johto)).add(Gen2TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen3 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen3.length(); ++i) {
         if (gen3.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Hoenn)).add(Gen3TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen4 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen4.length(); ++i) {
         if (gen4.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Sinnoh)).add(Gen4TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen5 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen5.length(); ++i) {
         if (gen5.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Unova)).add(Gen5TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen6 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen6.length(); ++i) {
         if (gen6.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Kalos)).add(Gen6TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen7 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen7.length(); ++i) {
         if (gen7.get(i)) {
            ((List)this.moveset.get(EnumMovesetGroup.Alola)).add(Gen7TechnicalMachines.getTm(i + 1));
         }
      }

      BitSet gen8 = BitSet.valueOf(this.readByteArray(byteBuf));

      for(int i = 0; i < gen8.length(); ++i) {
         if (gen8.get(i)) {
            if (i > 99) {
               ((List)this.moveset.get(EnumMovesetGroup.Galar)).add(Gen8TechnicalRecords.getTr(i - 100));
            } else {
               ((List)this.moveset.get(EnumMovesetGroup.Galar)).add(Gen8TechnicalMachines.getTm(i));
            }
         }
      }

      int eggSize = byteBuf.readShort();

      for(int i = 0; i < eggSize; ++i) {
         ((List)this.moveset.get(EnumMovesetGroup.Egg)).add(AttackBase.getAttackBase(byteBuf.readShort()).get());
      }

      int levelSize = byteBuf.readShort();

      for(int i = 0; i < levelSize; ++i) {
         ((List)this.moveset.get(EnumMovesetGroup.LevelUp)).add(Pair.of(Integer.valueOf(byteBuf.readByte()), AttackBase.getAttackBase(byteBuf.readShort()).get()));
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SendMovesetData message, MessageContext ctx) {
         ClientStorageManager.pokedex.updateMoveset(message.species, message.form, message.texture, message.moveset);
      }
   }
}
