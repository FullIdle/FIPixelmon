package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.Tier;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

public class TierShow extends PixelmonCommand {
   private static final String COMMAND_NAME = "tiershow";

   public String func_71517_b() {
      return "tiershow";
   }

   public String func_71518_a(ICommandSender sender) {
      return String.format("/%s <tier>", "tiershow");
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 1) {
         this.sendMessage(sender, TextFormatting.RED, this.func_71518_a(sender), new Object[0]);
      } else {
         Tier tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(args[0]);
         if (tier == null) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.tiershow.invalid", new Object[]{args[0]});
         } else {
            this.sendMessage(sender, tier.getTierDescription(), new Object[0]);
         }
      }
   }
}
