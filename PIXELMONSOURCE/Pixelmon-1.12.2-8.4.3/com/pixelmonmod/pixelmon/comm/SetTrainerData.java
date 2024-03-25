package com.pixelmonmod.pixelmon.comm;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class SetTrainerData {
   public String name;
   public String greeting;
   public String win;
   public String lose;
   public ItemStack[] winnings;
   public int id;
   public int winMoney;
   public BattleRules rules;

   public SetTrainerData() {
      this.name = "";
      this.greeting = "";
      this.win = "";
      this.lose = "";
      this.id = 0;
      this.winMoney = 10;
   }

   public SetTrainerData(String name, String greeting, String win, String lose, int winMoney, ItemStack[] winnings) {
      this(name, greeting, win, lose, winMoney, winnings, (BattleRules)null);
   }

   public SetTrainerData(String name, String greeting, String win, String lose, int winMoney, ItemStack[] winnings, BattleRules rules) {
      this.name = "";
      this.greeting = "";
      this.win = "";
      this.lose = "";
      this.id = 0;
      this.winMoney = 10;
      this.name = name;
      if (greeting.length() > 32767) {
         greeting = greeting.substring(0, 32767);
      }

      if (win.length() > 32767) {
         win = win.substring(0, 32767);
      }

      if (lose.length() > 32767) {
         lose = lose.substring(0, 32767);
      }

      this.greeting = greeting;
      this.win = win;
      this.lose = lose;
      this.winnings = winnings;
      this.winMoney = winMoney;
      this.rules = rules;
   }

   public SetTrainerData(NPCTrainer trainer, String localization) {
      this(trainer.getName(localization), trainer.getGreeting(localization), trainer.getWinMessage(localization), trainer.getLoseMessage(localization), trainer.getWinMoney(), trainer.getWinnings(), trainer.battleRules);
   }

   public void encodeInto(ByteBuf buffer) {
      buffer.writeInt(this.id);
      ByteBufUtils.writeUTF8String(buffer, this.name);
      ByteBufUtils.writeUTF8String(buffer, this.greeting);
      ByteBufUtils.writeUTF8String(buffer, this.win);
      ByteBufUtils.writeUTF8String(buffer, this.lose);
      buffer.writeInt(this.winnings.length);

      for(int i = 0; i < this.winnings.length; ++i) {
         ByteBufUtils.writeItemStack(buffer, this.winnings[i]);
      }

      buffer.writeInt(this.winMoney);
      if (this.rules == null) {
         buffer.writeBoolean(false);
      } else {
         buffer.writeBoolean(true);
         this.rules.encodeInto(buffer);
      }

   }

   public void decodeInto(ByteBuf buffer) {
      this.id = buffer.readInt();
      this.name = ByteBufUtils.readUTF8String(buffer);
      this.greeting = ByteBufUtils.readUTF8String(buffer);
      this.win = ByteBufUtils.readUTF8String(buffer);
      this.lose = ByteBufUtils.readUTF8String(buffer);
      this.winnings = new ItemStack[buffer.readInt()];

      for(int i = 0; i < this.winnings.length; ++i) {
         this.winnings[i] = ByteBufUtils.readItemStack(buffer);
      }

      this.winMoney = buffer.readInt();
      if (buffer.readBoolean()) {
         this.rules = new BattleRules(buffer);
      }

   }
}
