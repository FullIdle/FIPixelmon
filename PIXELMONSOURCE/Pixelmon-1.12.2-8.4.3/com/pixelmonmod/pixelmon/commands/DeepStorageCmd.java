package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.deepstorage.DeepStorage;
import com.pixelmonmod.pixelmon.storage.deepstorage.DeepStorageManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DeepStorageCmd extends PixelmonCommand {
   public String func_71517_b() {
      return "deepstorage";
   }

   public String func_71518_a(ICommandSender sender) {
      return "/deepstorage <collect | view [player]>";
   }

   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.sendMessage(sender, "Not enough arguments. " + this.func_71518_a(sender), new Object[0]);
      } else {
         EntityPlayerMP player;
         DeepStorage deepStorage;
         if (args[0].toLowerCase().equals("collect")) {
            if (!(sender instanceof EntityPlayerMP)) {
               this.sendMessage(sender, "You have to be a player to collect deep storage Pokémon. How else would you have any.", new Object[0]);
               return;
            }

            player = (EntityPlayerMP)sender;
            if (!DeepStorageManager.hasPokemonInDeepStorage(player.func_110124_au())) {
               this.sendMessage(sender, "You don't have any Pokémon in deep storage.", new Object[0]);
               return;
            }

            deepStorage = DeepStorageManager.getOrCreateDeepStorage(player.func_110124_au());
            if (deepStorage == null) {
               this.sendMessage(sender, "An unknown problem was encountered while trying to retrieve deep storage.", new Object[0]);
               return;
            }

            PCStorage pc = Pixelmon.storageManager.getPCForPlayer(player);
            ArrayList retrieved = deepStorage.tryRetrieve();
            ArrayList restored = new ArrayList();
            retrieved.removeIf((nbtx) -> {
               return pc.add(Pixelmon.pokemonFactory.create(nbtx)) ? restored.add(nbtx) : false;
            });
            if (!retrieved.isEmpty()) {
               this.sendMessage(sender, retrieved.size() + " were unable to be added to the PC due to lack of space. Make room and try again.", new Object[0]);
               retrieved.removeAll(restored);
               Iterator var8 = retrieved.iterator();

               while(var8.hasNext()) {
                  NBTTagCompound nbt = (NBTTagCompound)var8.next();
                  deepStorage.put(nbt);
               }
            }

            DeepStorageManager.save(player.func_110124_au());
            this.sendMessage(sender, TextFormatting.DARK_GREEN, "Successfully restored " + restored.size() + " Pokémon from deep storage!", new Object[0]);
         } else {
            if (!args[0].toLowerCase().equals("view")) {
               this.sendMessage(sender, "Unknown argument: " + args[0] + ". " + this.func_71518_a(sender), new Object[0]);
               return;
            }

            if (args.length == 1 && !(sender instanceof EntityPlayerMP)) {
               this.sendMessage(sender, "To view a deep storage, you need to specify a player or be a player.", new Object[0]);
               this.sendMessage(sender, this.func_71518_a(sender), new Object[0]);
               return;
            }

            player = null;
            if (sender instanceof EntityPlayerMP) {
               player = (EntityPlayerMP)sender;
            }

            if (args.length > 1) {
               player = getPlayer(args[1]);
               if (player == null) {
                  this.sendMessage(sender, "Unknown player: " + args[1], new Object[0]);
                  return;
               }
            }

            if (!DeepStorageManager.hasPokemonInDeepStorage(player.func_110124_au())) {
               this.sendMessage(sender, "No Pokémon in deep storage for " + player.func_70005_c_(), new Object[0]);
               return;
            }

            deepStorage = DeepStorageManager.getOrCreateDeepStorage(player.func_110124_au());
            List list = deepStorage.getArchivedPokemon();
            String message = "";
            if (list.isEmpty()) {
               message = "No Pokémon in deep storage for " + player.func_70005_c_();
               DeepStorageManager.save(player.func_110124_au());
            } else {
               NBTTagCompound nbt;
               for(Iterator var12 = list.iterator(); var12.hasNext(); message = message + nbt.func_74779_i("Name") + TextFormatting.WHITE + ", ") {
                  nbt = (NBTTagCompound)var12.next();
                  if (EnumSpecies.getFromNameAnyCaseNoTranslate(nbt.func_74779_i("Name")) != null) {
                     message = message + TextFormatting.DARK_GREEN;
                  } else {
                     message = message + TextFormatting.GRAY;
                  }
               }

               message = message.substring(0, message.length() - 2);
            }

            sender.func_145747_a(new TextComponentString(message));
         }

      }
   }
}
