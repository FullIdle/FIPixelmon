package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class InteractionZombie implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150428_aP)) {
         World world = pixelmon.field_70170_p;
         if (pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Gyarados})) {
            return false;
         }

         if ((pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Greninja}) || EnumSpecies.zombieTextured.contains(pixelmon.getBaseStats().getSpecies())) && world.func_175678_i(pixelmon.func_180425_c())) {
            long time = world.func_72820_D() % 192000L;
            if (time >= 22500L || time < 13500L) {
               return false;
            }

            if (world.func_175671_l(pixelmon.func_180425_c()) > 11 && !world.field_72995_K) {
               if (pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Greninja})) {
                  EnumGreninja form = (EnumGreninja)pixelmon.getPokemonData().getFormEnum();
                  if (form.isCosmetic()) {
                     return false;
                  }

                  EnumGreninja zombie = form == EnumGreninja.BASE ? EnumGreninja.ZOMBIE : (form == EnumGreninja.BATTLE_BOND ? EnumGreninja.ZOMBIE_BATTLE_BOND : EnumGreninja.ASH_ZOMBIE);
                  pixelmon.getPokemonData().setForm(zombie);
               } else {
                  pixelmon.getPokemonData().setForm(EnumSpecial.Zombie);
               }

               return true;
            }
         }
      }

      return false;
   }
}
