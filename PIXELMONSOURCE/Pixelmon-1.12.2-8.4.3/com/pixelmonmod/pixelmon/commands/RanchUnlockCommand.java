package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class RanchUnlockCommand extends PixelmonCommand {
   public RanchUnlockCommand() {
      super("unlock", "/unlock <player>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         this.execute(sender, new String[]{sender.func_70005_c_()});
      } else if (args.length == 1) {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         Iterator var4 = player.field_70170_p.func_175644_a(EntityBreeding.class, (input) -> {
            return input.func_152114_e(player);
         }).iterator();

         while(var4.hasNext()) {
            EntityBreeding pixelmon = (EntityBreeding)var4.next();
            if (pixelmon.blockOwner instanceof TileEntityRanchBlock) {
               TileEntityRanchBlock te = (TileEntityRanchBlock)pixelmon.blockOwner;
               te.removePokemon(player, pixelmon);
            }
         }

         PlayerPartyStorage storage = getPlayerStorage(player);
         Pokemon pokemon;
         Iterator var11;
         if (storage != null) {
            boolean flag = false;
            var11 = storage.getTeam().iterator();

            while(var11.hasNext()) {
               pokemon = (Pokemon)var11.next();
               if (pokemon.isInRanch()) {
                  pokemon.setInRanch(false);
                  flag = true;
               }
            }
         }

         PCStorage computerStorage = getComputerStorage(player);
         var11 = computerStorage.findAll(Pokemon::isInRanch).iterator();

         while(var11.hasNext()) {
            pokemon = (Pokemon)var11.next();
            pokemon.setInRanch(false);
         }

         func_152374_a(sender, this, 0, "pixelmon.command.unlock", new Object[]{player.func_70005_c_()});
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return tabCompleteUsernames(args);
   }

   public boolean func_82358_a(String[] args, int index) {
      return index == 0;
   }
}
