package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.Iterator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class SetRaidCommand extends PixelmonCommand {
   public SetRaidCommand() {
      super("setraid", "/setraid <species> <stars> [x] [y] [z] [dimensionid]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 2 && args.length != 6) {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         sender.func_145747_a(format(this.func_71518_a(sender), new Object[0]));
      } else {
         EnumSpecies species;
         IEnumForm form;
         if (args[0].contains(":")) {
            String[] split = args[0].split(":");
            species = (EnumSpecies)require(EnumSpecies.getFromName(split[0]), "argument.species.notfound", new Object[]{split[0]});
            form = species.getFormEnum(split[1]);
         } else {
            species = (EnumSpecies)require(EnumSpecies.getFromName(args[0]), "argument.species.notfound", new Object[]{args[0]});
            form = null;
         }

         if (form == null) {
            form = (IEnumForm)species.getDefaultForms().get(0);
         }

         int stars = requireInt(args[1], 1, 5, "argument.raidstars.invalid", new Object[]{args[1]});
         EntityDen den = null;
         if (args.length == 2) {
            EntityPlayerMP player = requireEntityPlayer(sender);
            float lastDenDistance = Float.MAX_VALUE;
            Iterator var18 = player.func_71121_q().func_72872_a(EntityDen.class, player.func_174813_aQ().func_186662_g(10.0)).iterator();

            while(var18.hasNext()) {
               EntityDen entity = (EntityDen)var18.next();
               float denDistance = player.func_70032_d(entity);
               if (denDistance < lastDenDistance) {
                  den = entity;
               }
            }
         } else {
            int x = requireInt(args[2], "argument.coordinate.invalid");
            int y = requireInt(args[3], "argument.coordinate.invalid");
            int z = requireInt(args[4], "argument.coordinate.invalid");
            float lastDenDistance = Float.MAX_VALUE;
            WorldServer world = (WorldServer)require(DimensionManager.getWorld(Integer.parseInt(args[5])), "argument.world.notfound", new Object[]{args[5]});
            Iterator var12 = world.func_72872_a(EntityDen.class, (new AxisAlignedBB(new BlockPos(x, y, z))).func_186662_g(10.0)).iterator();

            while(var12.hasNext()) {
               EntityDen entity = (EntityDen)var12.next();
               float denDistance = (float)entity.func_70011_f((double)x, (double)y, (double)z);
               if (denDistance < lastDenDistance) {
                  den = entity;
               }
            }
         }

         if (den != null) {
            den.setData(new RaidData(den.func_145782_y(), stars, species, form));
            sender.func_145747_a(format(TextFormatting.GREEN, "pixelmon.command.setraid", new Object[]{stars, species.getPokemonName()}));
         } else {
            sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.setraid.noden", new Object[0]));
         }
      }

   }
}
