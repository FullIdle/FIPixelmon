package com.pixelmonmod.tcg.commands;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ThemeDeck;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import com.pixelmonmod.tcg.api.card.personalization.Coin;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.api.registries.CoinRegistry;
import com.pixelmonmod.tcg.api.registries.ThemeDeckRegistry;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import java.util.Arrays;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TCGGiveCommand extends SubcommandBase {
   String getHelp(ICommandSender sender) {
      return "/tcg give card <player> <card_id>: give a card to a player\n/tcg give deck <player> <deck_id>: give a deck to a player\n/tcg give pack <player> <set_id>: give a booster pack to a player\n/tcg give cardback <player> <cardback_id>: gives a card back to a player\n/tcg give coin <player> <coin_id>: gives a coin to a player";
   }

   String getPermissionNode() {
      return "tcg.give";
   }

   int getPermissionLevel() {
      return 4;
   }

   protected boolean execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length <= 1) {
         return false;
      } else {
         EntityPlayer receiver = null;
         if (Arrays.asList("card", "deck", "pack", "essence", "cardback", "coin").contains(args[1])) {
            if (args.length <= 2) {
               return false;
            }

            receiver = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_152612_a(args[2]);
         }

         if (receiver == null) {
            sendMessage(sender, TextFormatting.RED, "Cannot find player " + args[2]);
            return true;
         } else if (args[1].equalsIgnoreCase("card")) {
            if (args.length > 3) {
               boolean isHolo = false;
               if (args.length > 4) {
                  isHolo = args[4].equalsIgnoreCase("H");
               }

               ImmutableCard card = CardRegistry.fromCode(args[3]);
               if (card == null) {
                  sendMessage(sender, TextFormatting.RED, "Cannot find card with ID " + args[3]);
                  return true;
               } else {
                  if (!receiver.func_191521_c(card.getItemStack(1))) {
                     receiver.func_71019_a(card.getItemStack(1), true);
                  }

                  receiver.func_145747_a(new TextComponentString(LanguageMapTCG.translateKey(card.getName().toLowerCase()) + " card is added to your inventory!"));
                  return true;
               }
            } else {
               return false;
            }
         } else if (args[1].equalsIgnoreCase("pack")) {
            if (args.length > 3) {
               CardSet set = CardSetRegistry.get(args[3]);
               if (set == null) {
                  sendMessage(sender, TextFormatting.RED, "Cannot find set with ID " + args[3]);
                  return true;
               } else {
                  if (!receiver.func_191521_c(set.getItemStack(1))) {
                     receiver.func_71019_a(set.getItemStack(1), true);
                  }

                  sendMessage(receiver, TextFormatting.GREEN, "Pack of " + LanguageMapTCG.translateKey(set.getUnlocalizedName().toLowerCase()) + " is added to your inventory!");
                  return true;
               }
            } else {
               return false;
            }
         } else if (args[1].equalsIgnoreCase("deck")) {
            if (args.length > 3) {
               ThemeDeck deck = ThemeDeckRegistry.get(args[3]);
               if (deck == null) {
                  sendMessage(sender, TextFormatting.RED, "Cannot find deck with ID " + args[3]);
                  return true;
               } else {
                  if (!receiver.func_191521_c(deck.getItemStack())) {
                     receiver.func_71019_a(deck.getItemStack(), true);
                  }

                  receiver.func_145747_a(new TextComponentString(TextFormatting.BOLD + deck.getLocalizedName() + TextFormatting.RESET + " deck is added to your inventory!"));
                  return true;
               }
            } else {
               return false;
            }
         } else if (!args[1].equalsIgnoreCase("essence")) {
            if (args[1].equalsIgnoreCase("cardback")) {
               if (args.length > 3) {
                  CardBack cb = CardBackRegistry.get(args[3]);
                  if (cb == null) {
                     sendMessage(sender, TextFormatting.RED, "Cannot find cardback with ID " + args[3]);
                     return true;
                  } else {
                     if (!receiver.func_191521_c(cb.getItemStack())) {
                        receiver.func_71019_a(cb.getItemStack(), true);
                     }

                     receiver.func_145747_a(new TextComponentString(TextFormatting.BOLD + cb.getName() + TextFormatting.RESET + " cardback is added to your inventory!"));
                     return true;
                  }
               } else {
                  return false;
               }
            } else if (args[1].equalsIgnoreCase("coin")) {
               if (args.length > 3) {
                  Coin coin = CoinRegistry.get(args[3]);
                  if (coin == null) {
                     sendMessage(sender, TextFormatting.RED, "Cannot find coin with ID " + args[3]);
                     return true;
                  } else {
                     if (!receiver.func_191521_c(coin.getItemStack())) {
                        receiver.func_71019_a(coin.getItemStack(), true);
                     }

                     receiver.func_145747_a(new TextComponentString(TextFormatting.BOLD + coin.getName() + TextFormatting.RESET + " coin is added to your inventory!"));
                     return true;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else if (args.length > 4) {
            Integer amount = 1;

            try {
               amount = Integer.parseInt(args[4]);
            } catch (NumberFormatException var9) {
               sendMessage(sender, TextFormatting.RED, "Specify a numeric value for the amount of essence you want to give. You specified " + args[3]);
               return true;
            }

            if (args[3].equalsIgnoreCase("all")) {
               Energy[] var5 = Energy.values();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  Energy e = var5[var7];
                  Energy.givePlayerEssence(sender, receiver, e.getUnlocalizedName(), amount);
               }
            } else {
               Energy.givePlayerEssence(sender, receiver, args[3], amount);
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
