package com.pixelmonmod.tcg.commands;

import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class TCGEndBattleCommand extends SubcommandBase {
   String getHelp(ICommandSender sender) {
      return null;
   }

   String getPermissionNode() {
      return null;
   }

   int getPermissionLevel() {
      return 0;
   }

   protected boolean execute(ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)sender;
         List tileEntities = (List)player.func_130014_f_().field_147482_g.stream().filter((te) -> {
            return te instanceof TileEntityBattleController && ((TileEntityBattleController)te).getGameServer().hasPlayer(player);
         }).collect(Collectors.toList());
         Iterator var5 = tileEntities.iterator();

         while(var5.hasNext()) {
            TileEntity tileEntity = (TileEntity)var5.next();
            TileEntityBattleController bc = (TileEntityBattleController)tileEntity;
            PlayerServerState p = bc.getGameServer().getPlayer(player);
            bc.endGame(bc.getGameServer().getOpponent(p), p, false);
         }
      }

      return false;
   }
}
