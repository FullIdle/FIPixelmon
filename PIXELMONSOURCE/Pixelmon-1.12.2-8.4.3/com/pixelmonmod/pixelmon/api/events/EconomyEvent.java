package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EconomyEvent extends Event {
   public final EntityPlayerMP player;

   public EconomyEvent(EntityPlayerMP player) {
      this.player = player;
   }

   public static class PostTransactionEvent extends EconomyEvent {
      public final transactionType type;
      public final int oldBalance;
      public final int newBalance;

      public PostTransactionEvent(EntityPlayerMP player, transactionType type, int oldBalance, int newBalance) {
         super(player);
         this.type = type;
         this.oldBalance = oldBalance;
         this.newBalance = newBalance;
      }
   }

   public static class PreTransactionEvent extends EconomyEvent {
      public final transactionType type;
      public final int balance;
      public int change;

      public PreTransactionEvent(EntityPlayerMP player, transactionType type, int balance, int change) {
         super(player);
         this.type = type;
         this.balance = balance;
         this.change = change;
      }
   }

   public static class GetBalanceEvent extends EconomyEvent {
      public int balance;

      public GetBalanceEvent(EntityPlayerMP player, int balance) {
         super(player);
         this.balance = balance;
      }

      public boolean isCancelable() {
         return false;
      }
   }

   public static enum transactionType {
      deposit,
      withdraw;
   }
}
