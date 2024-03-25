package com.pixelmonmod.tcg.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

public class TCGHelpCommand extends SubcommandBase {
   String getHelp(ICommandSender sender) {
      return "Pixelmon TCG (Pixelmon Trading Card Game) brings the Pokémon Trading Card Game to Pixelmon, with cards, booster packs, and computer-mediated game boards.\n\nHow to play\n1. Collect cards - §2/tcg help card\n2. Build a deck of 60 cards - §2/tcg help deck\n3. Battle with other players - §2/tcg help battle";
   }

   String getPermissionNode() {
      return null;
   }

   int getPermissionLevel() {
      return 0;
   }

   protected boolean execute(ICommandSender sender, String[] args) throws CommandException {
      TextComponentTranslation chat;
      if (args.length > 1) {
         switch (args[1].toLowerCase()) {
            case "card":
               sendMessage(sender, TextFormatting.AQUA, "How to collect cards");
               chat = new TextComponentTranslation("- Craft from blank cards and Pokemon photo [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://pixelmonmod.com/wiki/index.php?title=Pixelmon_TCG#Printing_cards")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://pixelmonmod.com/wiki/index.php?title=Pixelmon_TCG#Printing_cards")));
               sender.func_145747_a(chat);
               chat = new TextComponentTranslation("- Buy booster packs from shop [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://bulbapedia.bulbagarden.net/wiki/Booster_pack_(TCG)")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://bulbapedia.bulbagarden.net/wiki/Booster_pack_(TCG)")));
               sender.func_145747_a(chat);
               return true;
            case "deck":
               sendMessage(sender, TextFormatting.AQUA, "How to build a deck");
               chat = new TextComponentTranslation("- Collect 60 cards of valid types [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://bulbapedia.bulbagarden.net/wiki/Deck")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://bulbapedia.bulbagarden.net/wiki/Deck")));
               sender.func_145747_a(chat);
               chat = new TextComponentTranslation("- Purchase theme decks from shops [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://bulbapedia.bulbagarden.net/wiki/Theme_Deck_(TCG)")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://bulbapedia.bulbagarden.net/wiki/Theme_Deck_(TCG)")));
               sender.func_145747_a(chat);
               return true;
            case "battle":
               sendMessage(sender, TextFormatting.AQUA, "How to battle");
               sendMessage(sender, TextFormatting.RESET, "- Go to an unoccupied BattleBox in the server\n- Right click the BattleBox when holding a valid deck\n- Wait for the other player");
               chat = new TextComponentTranslation("- Official Battle Rule [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://assets.pokemon.com/assets/cms2/pdf/trading-card-game/rulebook/xy8-rulebook-en.pdf")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://assets.pokemon.com/assets/cms2/pdf/trading-card-game/rulebook/xy8-rulebook-en.pdf")));
               sender.func_145747_a(chat);
               chat = new TextComponentTranslation("- Current limitations [Link]", new Object[0]);
               chat.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://pixelmonmod.com/wiki/index.php?title=Pixelmon_TCG#Battling")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("http://pixelmonmod.com/wiki/index.php?title=Pixelmon_TCG#Battling")));
               sender.func_145747_a(chat);
               return true;
            default:
               return false;
         }
      } else {
         sendMessage(sender, TextFormatting.AQUA, "Pixelmon TCG (Pixelmon Trading Card Game) brings the Pokémon Trading Card Game to Pixelmon, with cards, booster packs, and computer-mediated game boards.");
         sendMessage(sender, TextFormatting.RESET, "How to play");
         chat = new TextComponentTranslation("1. Collect cards - §2/tcg help card", new Object[0]);
         chat.func_150256_b().func_150241_a(new ClickEvent(Action.RUN_COMMAND, "/tcg help card")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("/tcg help card")));
         sender.func_145747_a(chat);
         chat = new TextComponentTranslation("2. Build a deck of 60 cards - §2/tcg help deck", new Object[0]);
         chat.func_150256_b().func_150241_a(new ClickEvent(Action.RUN_COMMAND, "/tcg help deck")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("/tcg help deck")));
         sender.func_145747_a(chat);
         chat = new TextComponentTranslation("3. Battle with other players - §2/tcg help battle", new Object[0]);
         chat.func_150256_b().func_150241_a(new ClickEvent(Action.RUN_COMMAND, "/tcg help battle")).func_150209_a(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT, new TextComponentString("/tcg help battle")));
         sender.func_145747_a(chat);
         return true;
      }
   }
}
