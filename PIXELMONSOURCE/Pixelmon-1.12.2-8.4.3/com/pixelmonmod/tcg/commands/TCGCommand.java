package com.pixelmonmod.tcg.commands;

import com.google.common.collect.Lists;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TCGCommand extends CommandBase {
   private TCGHelpCommand TCGHelpCommand = new TCGHelpCommand();
   private TCGGiveCommand TCGGiveCommand = new TCGGiveCommand();
   private TCGEssenceReset TCGEssenceResetCommand = new TCGEssenceReset();
   private TCGEndBattleCommand TCGEndBattleCommand = new TCGEndBattleCommand();

   public String func_71517_b() {
      return "tcg";
   }

   public String func_71518_a(ICommandSender sender) {
      StringBuilder builder = new StringBuilder("/tcg Pixelmon TCG related commands\n/tcg help: get help\n/tcg endbattle: end battle");
      if (this.TCGGiveCommand.canUse(sender)) {
         builder.append("/tcg give: give players cards or theme decks");
      }

      if (this.TCGEssenceResetCommand.canUse(sender)) {
         builder.append("/tcg essencereset: used for reseting a players essence");
      }

      return builder.toString();
   }

   public int func_82362_a() {
      return 0;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         SubcommandBase.sendMessage(sender, TextFormatting.GOLD, "Pixelmon TCG 1.12.2-8.4.3-universal");
         SubcommandBase.sendMessage(sender, TextFormatting.RESET, "/tcg help: how to play");
      } else {
         switch (args[0].toLowerCase()) {
            case "help":
               this.TCGHelpCommand.processCommand(sender, args);
               break;
            case "give":
               this.TCGGiveCommand.processCommand(sender, args);
               break;
            case "endbattle":
               this.TCGEndBattleCommand.processCommand(sender, args);
               break;
            case "essencereset":
               this.TCGEssenceResetCommand.processCommand(sender, args);
         }

      }
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      List completions = Lists.newArrayList();
      List baseCommands = Arrays.asList("card", "deck", "pack", "essence", "cardback", "coin");
      if (args.length == 1) {
         completions.addAll(Arrays.asList("give", "help", "endbattle", "essencereset"));
      }

      int var9;
      int var10;
      String s;
      String[] opts;
      String[] var13;
      if (args.length == 2) {
         if (Objects.equals(args[0], "give")) {
            if (args[1].isEmpty()) {
               completions.addAll(baseCommands);
            } else {
               Iterator var7 = baseCommands.iterator();

               while(var7.hasNext()) {
                  String s = (String)var7.next();
                  if (s.startsWith(args[1])) {
                     completions.add(s);
                  }
               }
            }
         }

         if (Objects.equals(args[0], "essencereset")) {
            if (args[1].isEmpty()) {
               opts = FMLCommonHandler.instance().getMinecraftServerInstance().func_71213_z();
               completions.addAll(Arrays.asList(opts));
            } else {
               opts = FMLCommonHandler.instance().getMinecraftServerInstance().func_71213_z();
               var13 = opts;
               var9 = opts.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  s = var13[var10];
                  if (s.startsWith(args[1])) {
                     completions.add(s);
                  }
               }
            }
         }
      }

      if (args.length == 3 && Objects.equals(args[0], "give")) {
         if (args[2].isEmpty()) {
            completions.addAll(Arrays.asList(FMLCommonHandler.instance().getMinecraftServerInstance().func_71213_z()));
         } else {
            opts = FMLCommonHandler.instance().getMinecraftServerInstance().func_71213_z();
            var13 = opts;
            var9 = opts.length;

            for(var10 = 0; var10 < var9; ++var10) {
               s = var13[var10];
               if (s.startsWith(args[2])) {
                  completions.add(s);
               }
            }
         }
      }

      if (args.length == 4 && Objects.equals(args[0], "give")) {
         List energies;
         Iterator var15;
         ImmutableCard c;
         CardBack[] cardbacks;
         ThemeDeck c;
         Coin[] coins;
         CardSet c;
         CardBack[] var23;
         Coin[] var24;
         CardBack c;
         Coin c;
         if (args[3].isEmpty()) {
            if (Objects.equals(args[1], "card")) {
               energies = CardRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (ImmutableCard)var15.next();
                  completions.add(c.getCode());
               }
            }

            if (Objects.equals(args[1], "deck")) {
               energies = ThemeDeckRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (ThemeDeck)var15.next();
                  completions.add(c.getName());
               }
            }

            if (Objects.equals(args[1], "pack")) {
               energies = CardSetRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (CardSet)var15.next();
                  completions.add(c.getName());
               }
            }

            if (Objects.equals(args[1], "essence")) {
               Energy[] energies = Energy.values();
               Energy[] var20 = energies;
               var9 = energies.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  Energy e = var20[var10];
                  if (e != Energy.RAINBOW) {
                     completions.add(e.getUnlocalizedName());
                  }
               }

               completions.add("all");
            }

            if (Objects.equals(args[1], "cardback")) {
               cardbacks = (CardBack[])CardBackRegistry.getAll().toArray(new CardBack[0]);
               var23 = cardbacks;
               var9 = cardbacks.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  c = var23[var10];
                  completions.add(c.getName().replace(" ", "_"));
               }
            }

            if (Objects.equals(args[1], "coin")) {
               coins = (Coin[])CoinRegistry.getAll().toArray(new Coin[0]);
               var24 = coins;
               var9 = coins.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  c = var24[var10];
                  completions.add(c.getName().replace(" ", "_"));
               }
            }
         } else {
            if (Objects.equals(args[1], "card")) {
               energies = CardRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (ImmutableCard)var15.next();
                  if (c.getName().startsWith(args[3])) {
                     completions.add(c.getName());
                  }
               }
            }

            if (Objects.equals(args[1], "deck")) {
               energies = ThemeDeckRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (ThemeDeck)var15.next();
                  if (c.getName().startsWith(args[3])) {
                     completions.add(c.getName());
                  }
               }
            }

            if (Objects.equals(args[1], "pack")) {
               energies = CardSetRegistry.getAll();
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  c = (CardSet)var15.next();
                  if (c.getName().startsWith(args[3])) {
                     completions.add(c.getName());
                  }
               }
            }

            if (Objects.equals(args[1], "pack")) {
               energies = Arrays.asList(Energy.values());
               var15 = energies.iterator();

               while(var15.hasNext()) {
                  Energy e = (Energy)var15.next();
                  if (e.getUnlocalizedName().startsWith(args[3])) {
                     completions.add(e.getUnlocalizedName());
                  }
               }
            }

            if (Objects.equals(args[1], "cardback")) {
               cardbacks = (CardBack[])CardBackRegistry.getAll().toArray(new CardBack[0]);
               var23 = cardbacks;
               var9 = cardbacks.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  c = var23[var10];
                  if (c.getName().startsWith(args[3])) {
                     completions.add(c.getName());
                  }
               }
            }

            if (Objects.equals(args[1], "coin")) {
               coins = (Coin[])CoinRegistry.getAll().toArray(new Coin[0]);
               var24 = coins;
               var9 = coins.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  c = var24[var10];
                  if (c.getName().startsWith(args[3])) {
                     completions.add(c.getName());
                  }
               }
            }
         }
      }

      return completions;
   }
}
