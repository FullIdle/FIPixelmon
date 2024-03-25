package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.ItemTechnicalMove;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionTechnicalMove implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (!(stack.func_77973_b() instanceof ItemTechnicalMove)) {
         return false;
      } else if (!pixelmon.func_152114_e(player)) {
         return false;
      } else {
         Pokemon pokemon = pixelmon.getPokemonData();
         ITechnicalMove technicalMove = ItemTechnicalMove.getMove(stack);
         if (technicalMove == null) {
            return true;
         } else if (!pixelmon.getBaseStats().canLearn(technicalMove)) {
            if (pixelmon.getBaseStats().canLearnViaOtherSet(technicalMove)) {
               ChatHandler.sendChat(player, "pixelmon.interaction.tmcantlearncanother", pixelmon.getNickname(), technicalMove.getAttack().getTranslatedName());
            } else {
               ChatHandler.sendChat(player, "pixelmon.interaction.tmcantlearn", pixelmon.getNickname(), technicalMove.getAttack().getTranslatedName());
            }

            return true;
         } else if (pokemon.getMoveset().hasAttack(technicalMove.getAttackName())) {
            ChatHandler.sendChat(player, "pixelmon.interaction.tmknown", pixelmon.getNickname(), technicalMove.getAttack().getTranslatedName());
            return true;
         } else {
            if (pokemon.getMoveset().size() >= 4) {
               if (!player.func_184812_l_()) {
                  ItemStack cost = stack.func_77946_l();
                  cost.func_190920_e(1);
                  Predicate condition = null;
                  if (technicalMove instanceof Gen8TechnicalRecords) {
                     if (!PixelmonConfig.allowTRReuse) {
                        condition = LearnMoveController.itemCostCondition(cost).and(this.addReminderMove(technicalMove, pokemon));
                     } else {
                        condition = this.addReminderMove(technicalMove, pokemon);
                     }
                  } else if (!PixelmonConfig.allowTMReuse) {
                     condition = LearnMoveController.itemCostCondition(cost);
                  }

                  LearnMoveController.sendLearnMove((EntityPlayerMP)player, pokemon.getUUID(), technicalMove.getAttack(), condition);
               } else {
                  LearnMoveController.sendLearnMove((EntityPlayerMP)player, pokemon.getUUID(), technicalMove.getAttack());
               }
            } else {
               pokemon.getMoveset().add(new Attack(technicalMove.getAttack()));
               if (technicalMove instanceof Gen8TechnicalRecords && !pokemon.getMoveset().getReminderMoves().contains(technicalMove.getAttack())) {
                  pokemon.getMoveset().getReminderMoves().add(technicalMove.getAttack());
               }

               pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
               ChatHandler.sendChat(player, "pixelmon.stats.learnedmove", pixelmon.getNickname(), technicalMove.getAttack().getTranslatedName());
               if (!player.func_184812_l_()) {
                  if (technicalMove instanceof Gen8TechnicalRecords && !PixelmonConfig.allowTRReuse) {
                     stack.func_190918_g(1);
                  } else if (!PixelmonConfig.allowTMReuse) {
                     stack.func_190918_g(1);
                  }
               }
            }

            return true;
         }
      }
   }

   public Predicate addReminderMove(ITechnicalMove move, Pokemon pokemon) {
      return (p) -> {
         if (!pokemon.getMoveset().getReminderMoves().contains(move.getAttack())) {
            pokemon.getMoveset().getReminderMoves().add(move.getAttack());
         }

         return true;
      };
   }
}
