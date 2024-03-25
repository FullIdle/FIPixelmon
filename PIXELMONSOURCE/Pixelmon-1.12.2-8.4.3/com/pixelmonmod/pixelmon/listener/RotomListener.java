package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityOccupiedPokeball;
import com.pixelmonmod.pixelmon.enums.forms.EnumRotom;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotomListener {
   @SubscribeEvent
   public static void rotomChangeForm(PokeballImpactEvent event) {
      try {
         if (!event.pokeball.field_70170_p.field_72995_K && event.ballPosition.field_72313_a == Type.BLOCK && event.pokeball instanceof EntityOccupiedPokeball && ((EntityOccupiedPokeball)event.pokeball).getPokeNameUnlocalized().equalsIgnoreCase("pixelmon.rotom.name") && event.pokeball.func_85052_h() instanceof EntityPlayerMP) {
            World world = event.pokeball.field_70170_p;
            Block block = world.func_180495_p(event.ballPosition.func_178782_a()).func_177230_c();
            if (EnumRotom.getBlockImpactMap().containsKey(block)) {
               try {
                  EnumRotom form = (EnumRotom)EnumRotom.getBlockImpactMap().get(block);
                  PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)event.pokeball.func_85052_h());
                  Pokemon pokemon = party.find(((EntityOccupiedPokeball)event.pokeball).getPokeUUID());
                  if (pokemon == null) {
                     return;
                  }

                  if (form == pokemon.getFormEnum()) {
                     form = EnumRotom.NORMAL;
                  }

                  if (replaceMove(pokemon, (EnumRotom)pokemon.getFormEnum(), form)) {
                     pokemon.setForm(form);
                  } else {
                     LearnMoveController.sendLearnMove(party.getPlayer(), pokemon.getUUID(), (AttackBase)AttackBase.getAttackBase(form.attack).get());
                  }
               } catch (Exception var6) {
                  var6.printStackTrace();
               }
            }
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

   }

   public static void replacedMove(PokemonLink pixelmon, Attack attack) {
      EnumRotom form = null;
      EnumRotom[] var3 = EnumRotom.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumRotom f = var3[var5];
         if (f.attack.equalsIgnoreCase(attack.getMove().getAttackName())) {
            form = f;
         }
      }

      if (form != null) {
         if (pixelmon instanceof DelegateLink) {
            pixelmon.getEntity().setForm(form);
         }

      }
   }

   public static void replacedMove(Pokemon pokemon, Attack attack) {
      EnumRotom form = null;
      EnumRotom[] var3 = EnumRotom.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumRotom f = var3[var5];
         if (f.attack.equalsIgnoreCase(attack.getMove().getAttackName())) {
            form = f;
         }
      }

      if (form != null) {
         pokemon.setForm(form);
      }
   }

   private static boolean replaceMove(Pokemon pokemon, EnumRotom from, EnumRotom to) {
      int index = -1;
      if (from != EnumRotom.NORMAL) {
         for(int i = 0; i < 4; ++i) {
            Attack attack = pokemon.getMoveset().get(i);
            if (attack != null && attack.isAttack(from.attack)) {
               index = i;
            }
         }
      }

      if (to == EnumRotom.NORMAL) {
         if (index != -1) {
            if (pokemon.getMoveset().size() == 1) {
               pokemon.getMoveset().set(index, new Attack((AttackBase)AttackBase.getAttackBase(EnumRotom.NORMAL.attack).get()));
            } else {
               pokemon.getMoveset().remove(index);
            }
         }

         return true;
      } else if (index != -1) {
         pokemon.getMoveset().set(index, new Attack((AttackBase)AttackBase.getAttackBase(to.attack).get()));
         return true;
      } else {
         return pokemon.getMoveset().hasAttack(to.attack) || pokemon.getMoveset().add(new Attack((AttackBase)AttackBase.getAttackBase(to.attack).get()));
      }
   }
}
