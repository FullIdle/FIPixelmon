package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttackCardRegistry {
   private static final Map REGISTRY = Maps.newConcurrentMap();

   public static AttackCard get(String name, int attackNumber) {
      List attackCards = (List)REGISTRY.get(name.toLowerCase());
      if (attackCards != null && !attackCards.isEmpty()) {
         Iterator var3 = attackCards.iterator();

         AttackCard attackCard;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            attackCard = (AttackCard)var3.next();
         } while(attackCard.getAttackNumber() != attackNumber);

         return attackCard;
      } else {
         return null;
      }
   }

   public static AttackCard[] get(String name) {
      List attackCards = (List)REGISTRY.get(name.toLowerCase());
      return attackCards != null && !attackCards.isEmpty() ? (AttackCard[])attackCards.toArray(new AttackCard[0]) : null;
   }

   public static List getAll() {
      List cards = Lists.newArrayList();
      Iterator var1 = REGISTRY.values().iterator();

      while(var1.hasNext()) {
         List value = (List)var1.next();
         cards.addAll(value);
      }

      return cards;
   }

   public static void load() {
      try {
         Iterator var0 = TCG.loader.loadAttacks().iterator();

         while(var0.hasNext()) {
            AttackCard loadAttack = (AttackCard)var0.next();
            ((List)REGISTRY.computeIfAbsent(loadAttack.getCardCode().toLowerCase(), (___) -> {
               return Lists.newCopyOnWriteArrayList();
            })).add(loadAttack);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
