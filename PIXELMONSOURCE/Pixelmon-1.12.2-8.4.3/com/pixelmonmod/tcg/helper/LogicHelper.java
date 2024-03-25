package com.pixelmonmod.tcg.helper;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class LogicHelper {
   public static List getCardsAndCosmetics(ItemStack itemStack, PlayerServerState self) {
      List deck = new ArrayList();
      if (itemStack.func_77942_o()) {
         NBTTagList items = itemStack.func_77978_p().func_150295_c("DeckInventory", itemStack.func_77978_p().func_74732_a());

         for(int j = 0; j < items.func_74745_c(); ++j) {
            NBTTagCompound itemTag = items.func_150305_b(j);
            byte slot = itemTag.func_74771_c("Slot");
            if (slot >= 0 && slot < 60) {
               deck.add(CardRegistry.fromNBT((new ItemStack(itemTag)).func_77978_p()));
            } else if (slot == 60) {
               self.setCardBack((new ItemStack(itemTag)).func_77978_p().func_74779_i("CardBackID"));
            } else if (slot == 61) {
               self.setCoinSet((new ItemStack(itemTag)).func_77978_p().func_74779_i("CoinID"));
            }
         }
      }

      return deck;
   }

   public static List getCards(ItemStack itemStack) {
      List deck = new ArrayList();
      if (itemStack.func_77942_o()) {
         NBTTagList items = itemStack.func_77978_p().func_150295_c("DeckInventory", itemStack.func_77978_p().func_74732_a());

         for(int j = 0; j < items.func_74745_c(); ++j) {
            NBTTagCompound itemTag = items.func_150305_b(j);
            byte slot = itemTag.func_74771_c("Slot");
            if (slot >= 0 && slot < 60) {
               NBTTagCompound tag = (new ItemStack(itemTag)).func_77978_p();
               if (tag != null) {
                  deck.add(CardRegistry.fromNBT(tag));
               }
            }
         }
      }

      return deck;
   }

   public static List getCards(List items, EntityPlayer player) {
      List deck = new ArrayList();
      Iterator var3 = items.iterator();

      while(var3.hasNext()) {
         ItemStack item = (ItemStack)var3.next();
         if (item != null && item.func_77973_b() == TCG.itemCard && item.func_77942_o()) {
            NBTTagCompound tag = item.func_77978_p();
            if (tag != null) {
               deck.add(CardRegistry.fromNBT(tag));
            }
         }
      }

      return deck;
   }

   public static ResourceLocation validateDeckInHolder(List cards) {
      ResourceLocation red = new ResourceLocation("tcg", "gui/deck/red.png");
      ResourceLocation amber = new ResourceLocation("tcg", "gui/deck/amber.png");
      ResourceLocation green = new ResourceLocation("tcg", "gui/deck/green.png");
      if (!cards.stream().anyMatch((c) -> {
         return c.getCardType() == CardType.BASIC;
      }) && !cards.stream().anyMatch((c) -> {
         return c.getCardType() == CardType.EX;
      })) {
         return red;
      } else {
         List nonEnergy = (List)cards.stream().filter((c) -> {
            return c.getCardType() != CardType.ENERGY || c.getSecondaryEnergy() != null;
         }).map((c) -> {
            return LanguageMapTCG.translateKey(c.getName().toLowerCase());
         }).sorted().collect(Collectors.toList());
         String previousName = null;
         int count = 0;
         int maxCount = 4;

         String name;
         for(Iterator var8 = nonEnergy.iterator(); var8.hasNext(); previousName = name) {
            name = (String)var8.next();
            if (name.equals(previousName)) {
               ++count;
            } else {
               count = 1;
            }

            if (count > maxCount) {
               return red;
            }
         }

         if (cards.size() != 60) {
            return amber;
         } else {
            return green;
         }
      }
   }

   public static String validateDeck(List cards, int deckSize) {
      if (cards.size() != deckSize) {
         return "You need to have " + deckSize + " cards in the deck!";
      } else if (!cards.stream().anyMatch((c) -> {
         return c.getCardType() == CardType.BASIC;
      }) && !cards.stream().anyMatch((c) -> {
         return c.getCardType() == CardType.EX;
      })) {
         return "You need at least a basic Pokemon card!";
      } else {
         List nonEnergy = (List)cards.stream().filter((c) -> {
            return c.getCardType() != CardType.ENERGY || c.getSecondaryEnergy() != null;
         }).map((c) -> {
            return LanguageMapTCG.translateKey(c.getName().toLowerCase());
         }).sorted().collect(Collectors.toList());
         String previousName = null;
         int count = 0;
         int maxCount = 4;

         String name;
         for(Iterator var6 = nonEnergy.iterator(); var6.hasNext(); previousName = name) {
            name = (String)var6.next();
            if (name.equals(previousName)) {
               ++count;
            } else {
               count = 1;
            }

            if (count > maxCount) {
               return "You have more than " + maxCount + " '" + name + "' card!";
            }
         }

         return null;
      }
   }

   public static void shuffleCardList(List cards) {
      Collections.shuffle(cards);
   }

   public static boolean isEnoughEnergy(AttackCard cardAttack, List attachments, PokemonCardState pokemon) {
      List energyRequirements = new ArrayList();
      Collections.addAll(energyRequirements, cardAttack.getEnergy());
      return isEnoughEnergy((List)energyRequirements, attachments, pokemon);
   }

   public static boolean isEnoughEnergy(int count, List attachments, PokemonCardState pokemon) {
      List energyRequirements = new ArrayList();

      for(int x = 0; x < count; ++x) {
         energyRequirements.add(Energy.COLORLESS);
      }

      return isEnoughEnergy((List)energyRequirements, attachments, pokemon);
   }

   public static boolean isEnoughEnergy(List energyRequirements, List attachments, PokemonCardState pokemon) {
      int unmarked = 0;
      Iterator var4 = attachments.iterator();

      while(true) {
         CommonCardState attachment;
         ImmutableCard card;
         while(var4.hasNext()) {
            attachment = (CommonCardState)var4.next();
            card = attachment.getData();
            if (card.getCardType() == CardType.ENERGY) {
               if (!removeEnergyRequirement(energyRequirements, attachment.getMainEnergy(), pokemon)) {
                  ++unmarked;
               }

               if (attachment.getSecondaryEnergy() != null && !removeEnergyRequirement(energyRequirements, attachment.getSecondaryEnergy(), pokemon)) {
                  ++unmarked;
               }
            } else if (pokemon.getData().isPokemonCard() && pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
               List energyEquivalence = pokemon.getAbility().getEffect().getEnergyEquivalence(attachment);
               if (energyEquivalence != null) {
                  Iterator var8 = energyEquivalence.iterator();

                  while(var8.hasNext()) {
                     CommonCardState energy = (CommonCardState)var8.next();
                     if (!removeEnergyRequirement(energyRequirements, energy.getMainEnergy(), pokemon)) {
                        ++unmarked;
                     }
                  }
               }
            }
         }

         var4 = attachments.iterator();

         while(var4.hasNext()) {
            attachment = (CommonCardState)var4.next();
            card = attachment.getData();
            if (card.getCardType() == CardType.ENERGY) {
               if (card.getMainEnergy() == Energy.RAINBOW && !energyRequirements.isEmpty() && unmarked > 0) {
                  energyRequirements.remove(0);
                  --unmarked;
               }

               if (card.getSecondaryEnergy() == Energy.RAINBOW && !energyRequirements.isEmpty() && unmarked > 0) {
                  energyRequirements.remove(0);
               }
            }
         }

         while(unmarked > 0 && energyRequirements.size() > 0 && energyRequirements.get(0) == Energy.COLORLESS) {
            --unmarked;
            energyRequirements.remove(0);
         }

         return energyRequirements.isEmpty();
      }
   }

   public static boolean canEvolve(int cardTurn, int currentTurn) {
      return currentTurn > 1 && cardTurn < currentTurn;
   }

   private static boolean removeEnergyRequirement(List energies, Energy energy, PokemonCardState pokemon) {
      for(int i = 0; i < energies.size(); ++i) {
         if (energy != Energy.RAINBOW && (energies.get(i) == energy || pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null && pokemon.getAbility().getEffect().ignoreEnergyTypes() || pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null && pokemon.getHiddenAbility().getEffect().ignoreEnergyTypes())) {
            energies.remove(i);
            return true;
         }
      }

      return false;
   }

   public static List getEnergiesFromList(List cards) {
      List tempList = new ArrayList();
      cards.stream().filter((card) -> {
         return card.isEnergyEquivalence();
      }).forEach((card) -> {
         CardWithLocation card2 = new CardWithLocation();
         card2.set(card, true, BoardLocation.Attachment, 0);
         tempList.add(card2);
      });
      return tempList;
   }

   public static boolean hasBench(PlayerClientMyState player) {
      PokemonCardState[] var1 = player.getBenchCards();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PokemonCardState bench = var1[var3];
         if (bench != null) {
            return true;
         }
      }

      return false;
   }

   public static PokemonCardState evolveCard(ImmutableCard card, PokemonCardState activeCard, int turnCount) {
      PokemonCardState newCard = new PokemonCardState(card, turnCount);
      newCard.getAttachments().add(activeCard);
      newCard.getAttachments().addAll(activeCard.getAttachments());
      newCard.getStatus().setDamage(activeCard.getStatus().getDamage());
      activeCard.getAttachments().clear();
      activeCard.getStatus().setDamage(0);
      activeCard.getStatus().getConditions().clear();
      return newCard;
   }

   public static int getCostModifier(PlayerCommonState me, PlayerCommonState opp) {
      int costModifier = 0;
      Iterator var3;
      PokemonCardState pokemon;
      if (me != null) {
         var3 = me.getActiveAndBenchCards().iterator();

         while(var3.hasNext()) {
            pokemon = (PokemonCardState)var3.next();
            if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
               costModifier += pokemon.getAbility().getEffect().retreatModifier(pokemon, me);
            }
         }
      }

      if (opp != null) {
         var3 = opp.getActiveAndBenchCards().iterator();

         while(var3.hasNext()) {
            pokemon = (PokemonCardState)var3.next();
            if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
               costModifier += pokemon.getAbility().getEffect().retreatModifier(pokemon, me);
            }
         }
      }

      return costModifier;
   }
}
