package com.pixelmonmod.pixelmon.comm;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class PixelmonStatsData {
   public int HP;
   public int Speed;
   public int Attack;
   public int Defence;
   public int SpecialAttack;
   public int SpecialDefence;

   public PixelmonStatsData() {
   }

   public PixelmonStatsData(NBTTagCompound nbt) {
      this.HP = nbt.func_74762_e("StatsHP");
      this.Speed = nbt.func_74762_e("StatsSpeed");
      this.Attack = nbt.func_74762_e("StatsAttack");
      this.Defence = nbt.func_74762_e("StatsDefence");
      this.SpecialAttack = nbt.func_74762_e("StatsSpecialAttack");
      this.SpecialDefence = nbt.func_74762_e("StatsSpecialDefence");
   }

   public PixelmonStatsData(Stats stats) {
      this.HP = stats.hp;
      this.Attack = stats.attack;
      this.Defence = stats.defence;
      this.SpecialAttack = stats.specialAttack;
      this.SpecialDefence = stats.specialDefence;
      this.Speed = stats.speed;
   }

   public static PixelmonStatsData createPacket(PokemonLink pixelmon) {
      PixelmonStatsData p = new PixelmonStatsData();
      Stats stats = pixelmon.getStats();
      p.HP = stats.hp;
      p.Speed = stats.speed;
      p.Attack = stats.attack;
      p.Defence = stats.defence;
      p.SpecialAttack = stats.specialAttack;
      p.SpecialDefence = stats.specialDefence;
      return p;
   }

   public void writePacketData(ByteBuf buffer) {
      buffer.writeShort((short)this.HP);
      buffer.writeShort((short)this.Speed);
      buffer.writeShort((short)this.Attack);
      buffer.writeShort((short)this.Defence);
      buffer.writeShort((short)this.SpecialAttack);
      buffer.writeShort((short)this.SpecialDefence);
   }

   public void readPacketData(ByteBuf data) {
      this.HP = data.readShort();
      this.Speed = data.readShort();
      this.Attack = data.readShort();
      this.Defence = data.readShort();
      this.SpecialAttack = data.readShort();
      this.SpecialDefence = data.readShort();
   }
}
