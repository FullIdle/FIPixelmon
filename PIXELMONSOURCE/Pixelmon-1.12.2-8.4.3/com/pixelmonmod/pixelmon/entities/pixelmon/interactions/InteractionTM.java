package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemTM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class InteractionTM implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && itemstack.func_77973_b() instanceof ItemTM) {
         if (player == entityPixelmon.func_70902_q()) {
            ItemTM tm = (ItemTM)itemstack.func_77973_b();
            AttackBase ab = (AttackBase)AttackBase.getAttackBase(tm.attackName).orElse((Object)null);
            if (ab == null) {
               ChatHandler.sendChat(entityPixelmon.func_70902_q(), ((ItemTM)itemstack.func_77973_b()).attackName + " is corrupted!");
               return true;
            }

            if (!entityPixelmon.getBaseStats().canLearnHM(ab)) {
               ChatHandler.sendChat(player, "pixelmon.interaction.tmcantlearn", entityPixelmon.getNickname(), ab.getTranslatedName());
               return true;
            }

            if (entityPixelmon.getPokemonData().getMoveset().hasAttack(ab)) {
               ChatHandler.sendChat(entityPixelmon.func_70902_q(), "pixelmon.interaction.tmknown", entityPixelmon.getNickname(), ab.getTranslatedName());
               return true;
            }

            if (entityPixelmon.getPokemonData().getMoveset().size() >= 4) {
               if (!player.func_184812_l_() && !PixelmonConfig.allowTMReuse) {
                  ItemStack cost = itemstack.func_77946_l();
                  cost.func_190920_e(1);
                  LearnMoveController.sendLearnMove((EntityPlayerMP)player, entityPixelmon.func_110124_au(), ab, LearnMoveController.itemCostCondition(cost));
               } else {
                  LearnMoveController.sendLearnMove((EntityPlayerMP)player, entityPixelmon.func_110124_au(), ab);
               }
            } else {
               entityPixelmon.getPokemonData().getMoveset().add(new Attack(ab));
               ChatHandler.sendChat(entityPixelmon.func_70902_q(), "pixelmon.stats.learnedmove", entityPixelmon.getNickname(), ab.getTranslatedName());
               if (!player.field_71075_bZ.field_75098_d) {
                  ((ItemTM)itemstack.func_77973_b()).getClass();
               }
            }

            entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
         } else {
            ChatHandler.sendChat(entityPixelmon.func_70902_q(), "pixelmon.interaction.tmcantlearn", entityPixelmon.getNickname(), new TextComponentTranslation("attack." + ((ItemTM)itemstack.func_77973_b()).attackName.replace(" ", "_").toLowerCase() + ".name", new Object[0]));
         }

         return true;
      } else {
         return false;
      }
   }
}
