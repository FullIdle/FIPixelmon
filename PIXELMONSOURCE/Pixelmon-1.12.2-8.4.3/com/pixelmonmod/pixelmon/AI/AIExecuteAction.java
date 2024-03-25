package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AIExecuteAction extends EntityAIBase {
   private EntityCreature theEntity;

   public AIExecuteAction(EntityCreature entity7HasAI) {
      this.theEntity = entity7HasAI;
      this.func_75248_a(4);
   }

   public boolean func_75250_a() {
      try {
         if (this.theEntity.func_70638_az() == null) {
            return false;
         }

         if (this.theEntity instanceof EntityPixelmon) {
            EntityPixelmon pokemon = (EntityPixelmon)this.theEntity;
            if (pokemon.aggressionTimer > 0) {
               return false;
            }

            if (pokemon.battleController != null) {
               return false;
            }

            if (pokemon.hitByPokeball != null) {
               return false;
            }

            if (pokemon.getBossMode() != EnumBossMode.NotBoss) {
               return false;
            }

            if (pokemon.func_70902_q() != null) {
               EntityPlayerMP player = (EntityPlayerMP)pokemon.func_70902_q();
               if (BattleRegistry.getBattle(player) != null) {
                  return false;
               }
            }
         } else if (((NPCTrainer)this.theEntity).battleController != null) {
            return false;
         }

         EntityLivingBase attackTarget = this.theEntity.func_70638_az();
         MoveSkill moveSkill = null;
         double distance = 5.0;
         if (this.theEntity instanceof NPCTrainer) {
            distance = (double)((NPCTrainer)this.theEntity).getEngageDistance();
         }

         EntityPixelmon user;
         if (this.theEntity instanceof EntityPixelmon) {
            user = (EntityPixelmon)this.theEntity;
            String skillId = user.skillId;
            if (skillId != null) {
               moveSkill = MoveSkill.getMoveSkillByID(skillId);
               if (moveSkill != null) {
                  distance = (double)moveSkill.range;
               }
            }
         }

         if ((double)attackTarget.func_70032_d(this.theEntity) < distance) {
            this.theEntity.func_70624_b((EntityLivingBase)null);
            if (this.theEntity instanceof EntityPixelmon) {
               user = (EntityPixelmon)this.theEntity;
               if (moveSkill != null) {
                  if (attackTarget instanceof EntityPixelmon && moveSkill.hasTargetType(MoveSkill.EnumTargetType.POKEMON)) {
                     moveSkill.onUsed(user, attackTarget, MoveSkill.EnumTargetType.POKEMON);
                  } else if (attackTarget instanceof EntityPlayerMP && moveSkill.hasTargetType(MoveSkill.EnumTargetType.PLAYER)) {
                     moveSkill.onUsed(user, attackTarget, MoveSkill.EnumTargetType.PLAYER);
                  } else if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.MISC_ENTITY)) {
                     moveSkill.onUsed(user, attackTarget, MoveSkill.EnumTargetType.MISC_ENTITY);
                  }

                  return true;
               }
            }

            BattleParticipant participant;
            try {
               participant = this.getThisParticipant(attackTarget);
            } catch (IllegalStateException var14) {
               return false;
            }

            if (attackTarget instanceof EntityPlayer) {
               EntityPlayerMP player = (EntityPlayerMP)attackTarget;
               if (BattleRegistry.getBattle(player) != null) {
                  return false;
               }

               if (this.theEntity instanceof EntityPixelmon && ((EntityPixelmon)this.theEntity).belongsTo(player)) {
                  return false;
               }

               Item currentItem = ((ItemStack)player.field_71071_by.field_70462_a.get(player.field_71071_by.field_70461_c)).func_77973_b();
               if (currentItem == PixelmonItems.trainerEditor) {
                  return false;
               }

               if (currentItem instanceof ItemBlock) {
                  ItemBlock currentItemBlock = (ItemBlock)currentItem;
                  if (currentItemBlock.func_179223_d() == PixelmonBlocks.pixelmonSpawner) {
                     return false;
                  }
               }

               PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
               if (storage.guiOpened) {
                  return false;
               }

               if (storage.countAblePokemon() < 1) {
                  return false;
               }

               EnumBattleType battleType = EnumBattleType.Single;
               if (this.theEntity instanceof NPCTrainer) {
                  NPCTrainer trainer = (NPCTrainer)this.theEntity;
                  battleType = trainer.getBattleType();
                  if (!trainer.battleRules.isDefault()) {
                     TeamSelectionList.addTeamSelection(trainer.battleRules, true, trainer.getPokemonStorage(), Pixelmon.storageManager.getParty(player));
                     return true;
                  }
               }

               Pokemon firstPokemon = storage.findOne((p) -> {
                  return !p.isEgg() && p.getHealth() > 0;
               });
               if (firstPokemon == null) {
                  return false;
               }

               EntityPixelmon pixelmon = firstPokemon.getOrSpawnPixelmon(player);
               PlayerParticipant playerPart;
               if (battleType == EnumBattleType.Single) {
                  playerPart = new PlayerParticipant(player, new EntityPixelmon[]{pixelmon});
               } else {
                  playerPart = new PlayerParticipant(player, storage.findAll((p) -> {
                     return !p.isEgg() && p.getHealth() > 0;
                  }), battleType.numPokemon);
               }

               if (participant instanceof PlayerParticipant && this.theEntity instanceof EntityPixelmon) {
                  new BattleQuery((EntityPlayerMP)((EntityPixelmon)this.theEntity).func_70902_q(), (EntityPixelmon)this.theEntity, player, pixelmon);
               } else {
                  pixelmon.startBattle(playerPart, participant);
               }

               return true;
            }

            if (!(this.theEntity instanceof EntityPixelmon)) {
               return false;
            }

            EntityPixelmon userPokemon = (EntityPixelmon)this.theEntity;
            if (attackTarget instanceof NPCTrainer) {
               NPCTrainer trainerEntity = (NPCTrainer)attackTarget;
               EnumBattleType battleType = trainerEntity.getBattleType();
               if (participant instanceof PlayerParticipant && userPokemon.func_70902_q() instanceof EntityPlayerMP) {
                  EntityPlayerMP player = (EntityPlayerMP)userPokemon.func_70902_q();
                  if (trainerEntity.battleRules.isDefault()) {
                     if (userPokemon != null) {
                        userPokemon.onSendout();
                        TrainerParticipant trainer = new TrainerParticipant(trainerEntity, player, battleType.numPokemon);
                        PlayerParticipant playerPart;
                        if (battleType == EnumBattleType.Single) {
                           playerPart = new PlayerParticipant(player, new EntityPixelmon[]{userPokemon});
                        } else {
                           PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
                           List list = new ArrayList();
                           list.add(userPokemon.getPokemonData());
                           list.addAll((Collection)storage.getTeam().stream().filter((p) -> {
                              return p.getHealth() > 0 && p != userPokemon.getPokemonData();
                           }).collect(Collectors.toList()));
                           playerPart = new PlayerParticipant(player, list, 2);
                        }

                        userPokemon.startBattle(playerPart, trainer, trainerEntity.battleRules);
                     }
                  } else {
                     TeamSelectionList.addTeamSelection(trainerEntity.battleRules, true, trainerEntity.getPokemonStorage(), Pixelmon.storageManager.getParty(player));
                  }

                  return true;
               }

               return false;
            }

            if (!(attackTarget instanceof EntityPixelmon)) {
               return false;
            }

            EntityPixelmon target = (EntityPixelmon)attackTarget;
            if (participant instanceof WildPixelmonParticipant) {
               if (target.getBossMode() != EnumBossMode.NotBoss || userPokemon.getBossMode() != EnumBossMode.NotBoss) {
                  return false;
               }

               if (target.getPokemonData().isShiny() || userPokemon.getPokemonData().isShiny()) {
                  return false;
               }

               if (target.func_70902_q() != null) {
                  return false;
               }
            }

            if (target.hitByPokeball != null) {
               return false;
            }

            if (target.battleController != null) {
               return false;
            }

            if (!(target.func_110143_aJ() <= 0.0F) && !target.field_70128_L) {
               if (target.func_70902_q() == null) {
                  userPokemon.startBattle(participant, new WildPixelmonParticipant(new EntityPixelmon[]{target}));
               } else if (userPokemon.func_70902_q() != target.func_70902_q()) {
                  new BattleQuery((EntityPlayerMP)userPokemon.func_70902_q(), userPokemon, (EntityPlayerMP)target.func_70902_q(), target);
               }

               return true;
            }

            return false;
         }
      } catch (Exception var15) {
      }

      return false;
   }

   public void func_75249_e() {
      this.theEntity.func_70624_b((EntityLivingBase)null);
      if (this.theEntity instanceof EntityPixelmon) {
         ((EntityPixelmon)this.theEntity).update(new EnumUpdateType[]{EnumUpdateType.Target});
      }

   }

   private BattleParticipant getThisParticipant(EntityLivingBase attackTarget) throws IllegalStateException {
      if (this.theEntity instanceof NPCTrainer) {
         NPCTrainer trainer = (NPCTrainer)this.theEntity;
         return new TrainerParticipant(trainer, (EntityPlayerMP)attackTarget, trainer.getBattleType().numPokemon);
      } else {
         EntityPixelmon pixelmon = (EntityPixelmon)this.theEntity;
         return (BattleParticipant)(pixelmon.hasOwner() ? new PlayerParticipant((EntityPlayerMP)pixelmon.func_70902_q(), new EntityPixelmon[]{pixelmon}) : new WildPixelmonParticipant(new EntityPixelmon[]{pixelmon}));
      }
   }
}
