package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

public class ReloadMoveAnimations extends PixelmonCommand {
   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.sendMessage(sender, TextFormatting.RED, "Missing argument: Move name", new Object[0]);
      } else {
         String moveName = args[0];

         for(int i = 1; i < args.length; ++i) {
            moveName = moveName + " " + args[i];
         }

         moveName = moveName.replace(".json", "");
         File file = new File("pixelmon/moves/" + moveName + ".json");
         if (!file.exists()) {
            this.sendMessage(sender, TextFormatting.RED, "Couldn't find file: \"pixelmon/moves/\" + moveName + \".json\"", new Object[0]);
         } else {
            try {
               AttackBase attackBase = (AttackBase)AttackBase.GSON.fromJson(new FileReader(file), AttackBase.class);
               if (attackBase == null) {
                  throw new Exception();
               } else {
                  AttackBase existing = (AttackBase)AttackBase.getAttackBase(attackBase.getAttackId()).get();
                  existing.animations = attackBase.animations;
                  this.sendMessage(sender, TextFormatting.DARK_GREEN, "Reloaded animations for move: " + attackBase.getTranslatedName(), new Object[0]);
               }
            } catch (Exception var7) {
               this.sendMessage(sender, "There was a problem in the JSON", new Object[0]);
               var7.printStackTrace();
            }
         }
      }
   }

   public String func_71517_b() {
      return "reloadmoveanimations";
   }

   public String func_71518_a(ICommandSender sender) {
      return "/reloadmoveanimations <move name>";
   }

   public List func_71514_a() {
      return Arrays.asList("reloadmoveanimation");
   }
}
