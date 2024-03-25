package com.pixelmonmod.pixelmon.api.pokemon;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenReplaceMoveScreen;
import com.pixelmonmod.pixelmon.util.AirSaver;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LearnMoveController {
   private static final ListMultimap userMap = MultimapBuilder.hashKeys().arrayListValues().build();

   public static boolean canLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move) {
      if (userMap.containsKey(player.func_110124_au())) {
         Iterator iterator = userMap.get(player.func_110124_au()).iterator();

         while(iterator.hasNext()) {
            LearnMove learnMove = (LearnMove)iterator.next();
            if (Objects.equals(learnMove.pokemon, pokemon) && learnMove.move == move) {
               iterator.remove();
               return learnMove.condition == null || learnMove.condition.test(player);
            }
         }
      }

      return false;
   }

   public static boolean hasCondition(EntityPlayerMP player, UUID pokemon, AttackBase move) {
      return userMap.containsKey(player.func_110124_au()) ? userMap.get(player.func_110124_au()).stream().filter((it) -> {
         return Objects.equals(it.pokemon, pokemon) && it.move == move;
      }).anyMatch((it) -> {
         return it.condition != null;
      }) : false;
   }

   public static void addLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move) {
      userMap.put(player.func_110124_au(), new LearnMove(player.func_110124_au(), pokemon, move));
   }

   public static void addLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move, Predicate condition) {
      userMap.put(player.func_110124_au(), new LearnMove(player.func_110124_au(), pokemon, move, condition));
   }

   public static void sendLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move) {
      addLearnMove(player, pokemon, move);
      Pixelmon.network.sendTo(new OpenReplaceMoveScreen(pokemon, move), player);
   }

   public static void sendLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move, boolean checkEvo) {
      addLearnMove(player, pokemon, move);
      Pixelmon.network.sendTo(new OpenReplaceMoveScreen(pokemon, move, checkEvo), player);
   }

   public static void sendLearnMove(EntityPlayerMP player, UUID pokemon, AttackBase move, Predicate condition) {
      addLearnMove(player, pokemon, move, condition);
      Pixelmon.network.sendTo(new OpenReplaceMoveScreen(pokemon, move), player);
   }

   public static void clearLearnMoves(EntityPlayerMP player) {
      userMap.removeAll(player.func_110124_au());
   }

   public static void clearLearnMoves(UUID player) {
      userMap.removeAll(player);
   }

   public static Predicate itemCostCondition(ItemStack cost) {
      return itemCostCondition((Collection)Sets.newHashSet(new ItemStack[]{cost}));
   }

   public static Predicate itemCostCondition(Collection costs) {
      return (player) -> {
         Set taken = Sets.newHashSet();
         Iterator var3 = costs.iterator();

         while(var3.hasNext()) {
            ItemStack cost = (ItemStack)var3.next();
            if (!takeItem(player.field_71071_by.field_70462_a, cost) && !takeItem(player.field_71071_by.field_184439_c, cost)) {
               InventoryPlayer var10001 = player.field_71071_by;
               taken.forEach(var10001::func_70441_a);
               ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.npc.cantpay");
               return false;
            }

            taken.add(cost);
         }

         return true;
      };
   }

   private static boolean takeItem(NonNullList inv, ItemStack cost) {
      Iterator var2 = inv.iterator();

      ItemStack stack;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         stack = (ItemStack)var2.next();
      } while(stack.func_190926_b() || !stack.func_77969_a(cost) || !ItemStack.func_77970_a(stack, cost) || stack.func_190916_E() < cost.func_190916_E());

      stack.func_190918_g(cost.func_190916_E());
      return true;
   }

   public static void tick(World world) {
      Iterator var1 = userMap.values().iterator();

      while(var1.hasNext()) {
         LearnMove value = (LearnMove)var1.next();
         value.tick(world);
      }

   }

   static class LearnMove {
      UUID player;
      UUID pokemon;
      AttackBase move;
      Predicate condition;
      private final AirSaver airSaver;

      public LearnMove(UUID player, UUID pokemon, AttackBase move) {
         this.condition = null;
         this.player = player;
         this.pokemon = pokemon;
         this.move = move;
         this.airSaver = new AirSaver(FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.player));
      }

      public LearnMove(UUID player, UUID pokemon, AttackBase move, Predicate condition) {
         this(player, pokemon, move);
         this.condition = condition;
      }

      public void tick(World world) {
         this.airSaver.tick();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            LearnMove learnMove = (LearnMove)o;
            return this.player.equals(learnMove.player) && this.pokemon.equals(learnMove.pokemon) && this.move.equals(learnMove.move);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.player, this.pokemon, this.move});
      }
   }
}
