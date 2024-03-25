package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;

public class CardRegistry {
   private static final Map REGISTRY = Maps.newHashMap();
   private static final Map ENERGIES = Maps.newHashMap();

   public static List getAll() {
      return Lists.newArrayList(REGISTRY.values());
   }

   public static ImmutableCard getRandomCard(Energy energy) {
      List choices = Lists.newArrayList();
      Iterator var2 = getAll().iterator();

      while(true) {
         ImmutableCard card;
         do {
            do {
               if (!var2.hasNext()) {
                  if (choices.isEmpty()) {
                     return null;
                  }

                  return (ImmutableCard)RandomHelper.getRandomElementFromCollection(choices);
               }

               card = (ImmutableCard)var2.next();
            } while(card.getCardType() != CardType.ENERGY);
         } while(card.getMainEnergy() != energy && card.getSecondaryEnergy() != energy);

         choices.add(card);
      }
   }

   public static ImmutableCard getRareOrBetterCard(int setID, float weight, boolean isHolo) {
      List eligible = getEligableCards(setID, Sets.newHashSet(new CardRarity[]{CardRarity.RARE, CardRarity.HOLORARE, CardRarity.ULTRARARE, CardRarity.SECRETRARE}));
      if (eligible.isEmpty()) {
         return null;
      } else {
         int attempts = 0;

         ImmutableCard c;
         do {
            if (attempts >= 100) {
               return null;
            }

            c = (ImmutableCard)RandomHelper.getRandomElementFromCollection(eligible);
            ++attempts;
            float randFloat = RandomHelper.rand.nextFloat();
            if (weight / 60.0F > randFloat && c.getRarity() == CardRarity.SECRETRARE) {
               return c;
            }

            if (weight / 30.0F > randFloat && c.getRarity() == CardRarity.ULTRARARE) {
               return c;
            }

            if (weight / 3.0F > randFloat && c.getRarity() == CardRarity.HOLORARE) {
               return c;
            }
         } while(c.getRarity() != CardRarity.RARE);

         return c;
      }
   }

   public static List getEligableCards(int setID, Set rarities) {
      List eligible = new ArrayList();
      Iterator var3 = getAll().iterator();

      while(true) {
         while(true) {
            ImmutableCard card;
            do {
               if (!var3.hasNext()) {
                  return eligible;
               }

               card = (ImmutableCard)var3.next();
            } while(!rarities.contains(card.getRarity()) && !rarities.isEmpty());

            if (setID != 0 && card.getSetID() != setID) {
               if (card.getCardType() == CardType.ENERGY && !card.isSpecial()) {
                  eligible.add(card);
               }
            } else {
               eligible.add(card);
            }
         }
      }
   }

   public static ImmutableCard getRandomCardAdvanced(int setID, CardRarity cardRarity, boolean isHolo) {
      List eligible = getEligableCards(setID, Sets.newHashSet(new CardRarity[]{cardRarity}));
      return eligible.isEmpty() ? null : (ImmutableCard)RandomHelper.getRandomElementFromCollection(eligible);
   }

   public static ImmutableCard getRandomCard(int pokemonID, CardRarity cardRarity, boolean isHolo) {
      List choices = Lists.newArrayList();
      Iterator var4 = getAll().iterator();

      while(var4.hasNext()) {
         ImmutableCard card = (ImmutableCard)var4.next();
         if (card.getRarity() == cardRarity && card.getPokemonID() == pokemonID) {
            choices.add(card);
         }
      }

      if (choices.isEmpty()) {
         return null;
      } else {
         return (ImmutableCard)RandomHelper.getRandomElementFromCollection(choices);
      }
   }

   public static ImmutableCard getRandomCard(int pokemonID, CardRarity cardRarity, boolean isHolo, Energy energy) {
      List choices = Lists.newArrayList();
      Iterator var5 = getAll().iterator();

      while(var5.hasNext()) {
         ImmutableCard card = (ImmutableCard)var5.next();
         if (card.getRarity() == cardRarity && card.getPokemonID() == pokemonID && card.getMainEnergy() == energy) {
            choices.add(card);
         }
      }

      if (choices.isEmpty()) {
         return null;
      } else {
         return (ImmutableCard)RandomHelper.getRandomElementFromCollection(choices);
      }
   }

   public static ImmutableCard getRandomCard() {
      return (ImmutableCard)RandomHelper.getRandomElementFromCollection(getAll());
   }

   public static List getEnergyCards() {
      return Lists.newArrayList(ENERGIES.values());
   }

   public static ImmutableCard getEnergyCard(Energy e) {
      return (ImmutableCard)ENERGIES.get(e);
   }

   public static ImmutableCard fromId(int id) {
      if (id == -1) {
         return null;
      } else {
         Iterator var1 = getAll().iterator();

         ImmutableCard value;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            value = (ImmutableCard)var1.next();
         } while(value.getID() != id);

         return value;
      }
   }

   public static ImmutableCard fromCode(String code) {
      return (ImmutableCard)REGISTRY.get(code.toLowerCase());
   }

   public static ImmutableCard fromNBT(NBTTagCompound tag) {
      return fromId(tag.func_74762_e(ImmutableCard.CARD_ID_NBT_KEY));
   }

   public static void load() {
      try {
         Iterator var0 = TCG.loader.loadCards().iterator();

         while(var0.hasNext()) {
            ImmutableCard loadCard = (ImmutableCard)var0.next();
            REGISTRY.put(loadCard.getCode().toLowerCase(), loadCard);
            if (loadCard.getCardType() == CardType.ENERGY) {
               ENERGIES.put(loadCard.getMainEnergy(), loadCard);
            }
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
